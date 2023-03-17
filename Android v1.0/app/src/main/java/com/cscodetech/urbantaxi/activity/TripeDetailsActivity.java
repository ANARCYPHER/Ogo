package com.cscodetech.urbantaxi.activity;


import static com.cscodetech.urbantaxi.utility.LocationUpdateService.currentLocation;
import static com.cscodetech.urbantaxi.utility.Utility.drawTextToBitmap;
import static com.cscodetech.urbantaxi.utility.Utility.getUrl;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.map.FetchURL;
import com.cscodetech.urbantaxi.map.TaskLoadedCallback;
import com.cscodetech.urbantaxi.model.RestResponse;
import com.cscodetech.urbantaxi.model.RideDetails;
import com.cscodetech.urbantaxi.model.RiderData;
import com.cscodetech.urbantaxi.model.TripeDetails;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.retrofit.GetResult;
import com.cscodetech.urbantaxi.utility.CustPrograssbar;
import com.cscodetech.urbantaxi.utility.SessionManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class TripeDetailsActivity extends AppCompatActivity implements GetResult.MyListener, OnMapReadyCallback, TaskLoadedCallback {


    @BindView(R.id.txt_date)
    public TextView txtDate;
    @BindView(R.id.txt_orders)
    public TextView txtOrders;

    @BindView(R.id.txt_pickup)
    public TextView txtPickup;
    @BindView(R.id.txt_drop)
    public TextView txtDrop;
    @BindView(R.id.txt_totalpay)
    public TextView txtTotalpay;
    @BindView(R.id.ptype)
    public TextView ptype;
    @BindView(R.id.txt_ptypeamount)
    public TextView txtPtypeamount;
    @BindView(R.id.btn_reject)
    public TextView btnReject;
    @BindView(R.id.btn_accept)
    public TextView btnAccept;
    @BindView(R.id.lvl_aceptreject)
    public LinearLayout lvlAceptreject;
    @BindView(R.id.btn_rechlocation)
    public TextView btnRechlocation;
    @BindView(R.id.lvl_rechlocation)
    public LinearLayout lvlRechlocation;
    @BindView(R.id.ed_otp)
    public EditText edOtp;
    @BindView(R.id.btn_cancel)
    public TextView btnCancel;
    @BindView(R.id.btn_start)
    public TextView btnStart;
    @BindView(R.id.lvl_startcancel)
    public LinearLayout lvlStartcancel;
    @BindView(R.id.btn_tripend)
    public TextView btnTripend;
    @BindView(R.id.lvl_tripend)
    public LinearLayout lvlTripend;
    CustPrograssbar custPrograssbar;
    String oid;
    SessionManager sessionManager;
    SupportMapFragment mapFragment;
    RideDetails details;
    RiderData riderData;
    private FirebaseFirestore db;
    private Polyline currentPolyline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripe_details);
        ButterKnife.bind(this);
        db = FirebaseFirestore.getInstance();

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        riderData = sessionManager.getUserDetails();
        oid = getIntent().getStringExtra("order_id");


        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void getOrderDetails() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", oid);


            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().tripDetails(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void tripaccept(String status) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", oid);
            jsonObject.put("rider_id", riderData.getId());
            jsonObject.put("status", status);


            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().makeDecision(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void reachLocation() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", oid);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().reachLocation(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void startCancelTrip(String comment, String status, String otp) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", oid);
            jsonObject.put("status", status);
            jsonObject.put("otp", otp);
            jsonObject.put("comment_reject", comment);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().tripStartCancle(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "4");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void endTrip() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", oid);

            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().tripEnd(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "5");
            custPrograssbar.prograssCreate(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void bottonOrderMakeDecision() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_trip_status, null);
        mBottomSheetDialog.setContentView(sheetView);

        RadioGroup radio = sheetView.findViewById(R.id.radio);
        TextView btnTripend = sheetView.findViewById(R.id.btn_tripend);

        btnTripend.setOnClickListener(view -> {
            mBottomSheetDialog.cancel();
            int selectedId = radio.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = (RadioButton) findViewById(selectedId);

            startCancelTrip(radioButton.getText().toString(), "2", "");

            Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();

        });

        mBottomSheetDialog.show();
    }


    @OnClick({R.id.btn_reject, R.id.btn_accept, R.id.btn_rechlocation, R.id.btn_cancel, R.id.btn_start, R.id.btn_tripend, R.id.img_back})
    public void onBindClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reject:
                tripaccept("2");

                break;
            case R.id.btn_accept:
                tripaccept("1");
                break;
            case R.id.btn_rechlocation:
                reachLocation();
                break;
            case R.id.btn_cancel:
                bottonOrderMakeDecision();

                break;
            case R.id.btn_start:
                if (!TextUtils.isEmpty(edOtp.getText().toString())) {
                    startCancelTrip("", "1", edOtp.getText().toString());
                } else {
                    edOtp.setError("Enter Trip Code");
                }
                break;
            case R.id.btn_tripend:
                endTrip();
                break;
            case R.id.img_back:
                finish();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();


            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                TripeDetails tripeDetails = gson.fromJson(result.toString(), TripeDetails.class);


                if (tripeDetails.getResult().equalsIgnoreCase("true")) {
                    details = tripeDetails.getRideDetails();
                    txtDate.setText("" + details.getOrderDate());
                    txtOrders.setText("" + details.getOrderid());

                    txtPickup.setText("" + details.getPickAddress());

                    txtDrop.setText("" + details.getDropAddress());

                    txtTotalpay.setText(sessionManager.getStringData(SessionManager.currency) + details.getTotalAmt());
                    txtPtypeamount.setText(sessionManager.getStringData(SessionManager.currency) + details.getTotalAmt());
                    ptype.setText("" + details.getPMethodName());

                    switch (details.getOStatus()) {
                        case "Pending":
                            lvlAceptreject.setVisibility(View.VISIBLE);
                            lvlRechlocation.setVisibility(View.GONE);
                            lvlStartcancel.setVisibility(View.GONE);
                            lvlTripend.setVisibility(View.GONE);

                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(details.getPickLat(), details.getPickLong()))
                                    .title("Pick")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup_map_pin)));

                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(details.getDropLat(), details.getDropLong()))
                                    .title("Drop")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_drop_map_pin)));


                            CameraUpdate center =
                                    CameraUpdateFactory.newLatLng(new LatLng(details.getDropLat(), details.getDropLong()));
                            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(new LatLng(details.getPickLat(), details.getPickLong()), 13);

                            map.moveCamera(center);
                            map.animateCamera(zoom);

                            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

                            break;
                        case "Accepted":
                            lvlAceptreject.setVisibility(View.GONE);
                            lvlRechlocation.setVisibility(View.VISIBLE);
                            lvlStartcancel.setVisibility(View.GONE);
                            lvlTripend.setVisibility(View.GONE);
                            LatLng place1 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            LatLng place2 = new LatLng(details.getPickLat(), details.getPickLong());
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(place1, 14));

                            drawPath(place1, place2);
                            break;
                        case "Reach Location":
                            lvlAceptreject.setVisibility(View.GONE);
                            lvlRechlocation.setVisibility(View.GONE);
                            lvlStartcancel.setVisibility(View.VISIBLE);
                            lvlTripend.setVisibility(View.GONE);

                            LatLng place1111 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            LatLng place2112 = new LatLng(details.getDropLat(), details.getDropLong());

                            Bitmap bitmap = drawTextToBitmap(TripeDetailsActivity.this, R.drawable.cars, "");
                            Bitmap bitmap1 = drawTextToBitmap(TripeDetailsActivity.this, R.drawable.ic_map, "");

                            map.addMarker(new MarkerOptions()
                                    .position(place1111)
                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                            map.addMarker(new MarkerOptions()
                                    .position(place2112)
                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap1)));

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(place1111, 14));

                            break;
                        case "Ride Start":
                            lvlAceptreject.setVisibility(View.GONE);
                            lvlRechlocation.setVisibility(View.GONE);
                            lvlStartcancel.setVisibility(View.GONE);
                            lvlTripend.setVisibility(View.VISIBLE);

                            LatLng place11 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            LatLng place21 = new LatLng(details.getDropLat(), details.getDropLong());

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(place11, 14));

                            drawPath(place11, place21);

                            break;

                            case "Completed":
                            lvlAceptreject.setVisibility(View.GONE);
                            lvlRechlocation.setVisibility(View.GONE);
                            lvlStartcancel.setVisibility(View.GONE);
                            lvlTripend.setVisibility(View.GONE);

                            LatLng place111 = new LatLng(details.getPickLat(), details.getPickLong());

                            LatLng place211 = new LatLng(details.getDropLat(), details.getDropLong());

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(place111, 14));

                            drawPath(place111, place211);

                            break;
                    }

                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {

                    lvlAceptreject.setVisibility(View.GONE);
                    lvlRechlocation.setVisibility(View.VISIBLE);
                    lvlStartcancel.setVisibility(View.GONE);
                    lvlTripend.setVisibility(View.GONE);


                    Map<String, Object> city = new HashMap<>();

                    city.put("orderid", oid);

                    city.put("request_step", "2");

                    db.collection("Admin").document(riderData.getId()).update(city);

                    LatLng place1 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    LatLng place2 = new LatLng(details.getPickLat(), details.getPickLong());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(place1, 14));

                    drawPath(place1, place2);

                } else {
                    finish();
                }
                Toast.makeText(this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();

            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    lvlAceptreject.setVisibility(View.GONE);
                    lvlRechlocation.setVisibility(View.GONE);
                    lvlStartcancel.setVisibility(View.VISIBLE);
                    lvlTripend.setVisibility(View.GONE);

                    Map<String, Object> city = new HashMap<>();

                    city.put("orderid", oid);
                    city.put("request_step", "3");

                    db.collection("Admin").document(riderData.getId()).update(city);


                } else {
                    finish();
                }
                Toast.makeText(this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();
            } else if (callNo.equalsIgnoreCase("4")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);

                Map<String, Object> city = new HashMap<>();

                city.put("orderid", oid);
                city.put("request_step", "4");

                db.collection("Admin").document(riderData.getId()).update(city);
                LatLng place1 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                LatLng place2 = new LatLng(details.getDropLat(), details.getDropLat());

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(place1, 14));

                drawPath(place1, place2);


                finish();
                Toast.makeText(this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();
            } else if (callNo.equalsIgnoreCase("5")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    lvlAceptreject.setVisibility(View.GONE);
                    lvlRechlocation.setVisibility(View.GONE);
                    lvlStartcancel.setVisibility(View.GONE);
                    lvlTripend.setVisibility(View.GONE);

                    Map<String, Object> city = new HashMap<>();

                    city.put("orderid", oid);
                    city.put("request_step", "5");

                    db.collection("Admin").document(riderData.getId()).update(city);

                } else {
                    finish();
                }
                Toast.makeText(this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.e("Error--> ", e.getMessage());
        }

    }

    GoogleMap map;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (details == null) {
            getOrderDetails();

        }


    }


    private void drawPath(LatLng place1, LatLng place2) {

        Bitmap bitmap = drawTextToBitmap(TripeDetailsActivity.this, R.drawable.cars, "");
        Bitmap bitmap1 = drawTextToBitmap(TripeDetailsActivity.this, R.drawable.ic_map, "");

        map.addMarker(new MarkerOptions()
                .position(place1)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

        map.addMarker(new MarkerOptions()
                .position(place2)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap1)));


        CameraUpdate center =
                CameraUpdateFactory.newLatLng(place2);
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(place1, 15);

        map.moveCamera(center);
        map.animateCamera(zoom);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(TripeDetailsActivity.this, R.raw.map_style));


        new FetchURL(TripeDetailsActivity.this).execute(getUrl(place1, place2, "driving"), "driving");
    }

    @Override
    public void onTaskDone(Object... values) {

        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);

    }
}