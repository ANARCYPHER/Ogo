package com.cscodetech.urbantaxi.activity;

import static android.content.ContentValues.TAG;
import static com.cscodetech.urbantaxi.utility.SessionManager.language;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.adepter.TripAdapter;
import com.cscodetech.urbantaxi.adepter.VerifyListAdapter;
import com.cscodetech.urbantaxi.model.Homepage;
import com.cscodetech.urbantaxi.model.Identy;
import com.cscodetech.urbantaxi.model.OrderHistoryItem;
import com.cscodetech.urbantaxi.model.RiderData;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.retrofit.GetResult;
import com.cscodetech.urbantaxi.utility.CustPrograssbar;
import com.cscodetech.urbantaxi.utility.LocationUpdateService;
import com.cscodetech.urbantaxi.utility.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class HomeActivity extends BaseActivity implements OnMapReadyCallback,  BottomNavigationView.OnNavigationItemSelectedListener, VerifyListAdapter.RecyclerTouchListener, GetResult.MyListener, TripAdapter.RecyclerTouchListener {

    @BindView(R.id.txt_stats)
    public TextView txtStats;
    @BindView(R.id.switchs)
    public Switch switchs;
    @BindView(R.id.img_profile)
    public CircleImageView imgProfile;
    @BindView(R.id.recycleview)
    public RecyclerView recycleview;
    @BindView(R.id.bottomNavigationView)
    public BottomNavigationView bottomNavigationView;

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private boolean locationPermissionGranted;
    SessionManager sessionManager;
    RiderData riderData;
    private FirebaseFirestore db;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        riderData = sessionManager.getUserDetails();
        db = FirebaseFirestore.getInstance();
        custPrograssbar = new CustPrograssbar();
        Glide.with(this).load(APIClient.baseUrl + "/" + riderData.getProImg()).into(imgProfile);
        Log.e("user","-->"+APIClient.baseUrl + "/" + riderData.getProImg());

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        recycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycleview.setItemAnimator(new DefaultItemAnimator());
        if (!isServiceRunning(LocationUpdateService.class)) {
            switchs.setChecked(false);
            txtStats.setTextColor(getResources().getColor(R.color.white));
        } else {
            switchs.setChecked(true);
            txtStats.setTextColor(getResources().getColor(R.color.green));
        }
        switchs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);

                if (b) {
                    txtStats.setTextColor(getResources().getColor(R.color.green));
                    if (!isServiceRunning(LocationUpdateService.class)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(new Intent(HomeActivity.this, LocationUpdateService.class));
                        } else {
                            startService(new Intent(HomeActivity.this, LocationUpdateService.class));
                        }

                    }

                } else {
                    txtStats.setTextColor(getResources().getColor(R.color.white));
                    stopService(new Intent(HomeActivity.this, LocationUpdateService.class));

                }
                Map<String, Object> user = new HashMap<>();
                user.put("isavailable", b);
                db.collection("Admin").document(riderData.getId())
                        .update(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(MotionEffect.TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(MotionEffect.TAG, "Error writing document", e);
                            }
                        });
            }
        });
        getHomepage();
    }

    private void getHomepage() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rider_id", riderData.getId());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().homeData(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Homepage homepage = gson.fromJson(result.toString(), Homepage.class);
                if (homepage.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setStringData(SessionManager.currency, homepage.getCurrency());
                    sessionManager.setStringData(language, homepage.getAppLan());

                    if (homepage.getPartnerData().getIsApprove().equalsIgnoreCase("1")) {
                        switchs.setVisibility(View.VISIBLE);

                        TripAdapter categoryAdapter = new TripAdapter(this, homepage.getOrderHistory(), this);
                        recycleview.setAdapter(categoryAdapter);

                    } else {
                        switchs.setVisibility(View.GONE);
                        riderData.setIdentityFront(homepage.getPartnerData().getIdentityFront());
                        riderData.setIdentityBack(homepage.getPartnerData().getIdentityBack());

                        riderData.setLicenseFront(homepage.getPartnerData().getLicenseFront());
                        riderData.setLicenseBack(homepage.getPartnerData().getLicenseBack());
                        sessionManager.setUserDetails(riderData);

                        List<Identy> identies = new ArrayList<>();
                        if (homepage.getPartnerData().getIdentityStatus().equalsIgnoreCase("0")) {
                            Identy identy = new Identy();
                            identy.setFront(homepage.getPartnerData().getIdentityFront());
                            identy.setBack(homepage.getPartnerData().getIdentityBack());
                            identy.setTitle(getResources().getString(R.string.upload_identity));
                            identy.setStatus("Pending");
                            identies.add(identy);
                        }
                        if (homepage.getPartnerData().getLicenseStatus().equalsIgnoreCase("0")) {
                            Identy identy = new Identy();
                            identy.setFront(homepage.getPartnerData().getLicenseFront());
                            identy.setBack(homepage.getPartnerData().getLicenseBack());
                            identy.setTitle(getResources().getString(R.string.upload_license));
                            identy.setStatus("Pending");
                            identies.add(identy);
                        }

                        if (homepage.getPartnerData().getIdentityStatus().equalsIgnoreCase("2")) {
                            Identy identy = new Identy();
                            identy.setFront(homepage.getPartnerData().getIdentityFront());
                            identy.setBack(homepage.getPartnerData().getIdentityBack());
                            identy.setTitle(getResources().getString(R.string.upload_identity));
                            identy.setStatus("Reject");
                            identies.add(identy);
                        }
                        if (homepage.getPartnerData().getLicenseStatus().equalsIgnoreCase("2")) {
                            Identy identy = new Identy();
                            identy.setFront(homepage.getPartnerData().getLicenseFront());
                            identy.setBack(homepage.getPartnerData().getLicenseBack());
                            identy.setTitle(getResources().getString(R.string.upload_license));

                            identy.setStatus("Reject");
                            identies.add(identy);
                        }

                        if (identies.size() != 0) {
                            VerifyListAdapter categoryAdapter = new VerifyListAdapter(this, identies, this);
                            recycleview.setAdapter(categoryAdapter);
                        }


                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        // [END map_current_place_set_info_window_adapter]

        // Prompt the user for permission.
        getLocationPermission();
        // [END_EXCLUDE]

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 15));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(new LatLng(-33.8523341, 151.2106085), 15));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }




    @Override
    public void onClickVeryfyItem(Identy item, int position) {

        startActivity(new Intent(HomeActivity.this, ChangeDocActivity.class).putExtra("type", item.getTitle()));

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.panding) {
            if (switchs.isChecked()) {
                startActivity(new Intent(HomeActivity.this, TripHistoryActivity.class));
            } else {
                Toast.makeText(HomeActivity.this, "Please Switch On", Toast.LENGTH_LONG).show();
            }

            return true;
        } else if (itemId == R.id.completed) {
            startActivity(new Intent(HomeActivity.this, TripHistoryCompletActivity.class));

            return true;
        } else if (itemId == R.id.withdraw) {
            startActivity(new Intent(HomeActivity.this, PayoutActivity.class));
            return true;
        } else if (itemId == R.id.profile) {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            return true;
        }
        return false;
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onClickWheelerInfo(OrderHistoryItem item, int position) {
        if (switchs.isChecked()) {
            startActivity(new Intent(HomeActivity.this, TripeDetailsActivity.class).putExtra("order_id", item.getRideId()));
        } else {
            Toast.makeText(HomeActivity.this, "Please Switch On", Toast.LENGTH_LONG).show();
        }

    }
}