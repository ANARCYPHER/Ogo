package com.cscodetech.urbantaxi.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.cscodetech.urbantaxi.utility.Utility.bottonConfirm;
import static com.cscodetech.urbantaxi.utility.Utility.isvarification;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.imagepicker.ImageCompressionListener;
import com.cscodetech.urbantaxi.imagepicker.ImagePicker;
import com.cscodetech.urbantaxi.model.Login;
import com.cscodetech.urbantaxi.model.RiderData;
import com.cscodetech.urbantaxi.model.Vehicle;
import com.cscodetech.urbantaxi.model.VehicleDataItem;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.retrofit.GetResult;
import com.cscodetech.urbantaxi.utility.CustPrograssbar;
import com.cscodetech.urbantaxi.utility.FileUtils;
import com.cscodetech.urbantaxi.utility.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SignUpwithOTPActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.img_frount)
    public ImageView imgFrount;
    @BindView(R.id.txt_uploadfrount)
    public TextView txtUploadfrount;
    @BindView(R.id.img_backend)
    public ImageView imgBackend;
    @BindView(R.id.txt_uploadback)
    public TextView txtUploadback;
    @BindView(R.id.img_frountl)
    public ImageView imgFrountl;
    @BindView(R.id.txt_uploadfrountl)
    public TextView txtUploadfrountl;
    @BindView(R.id.img_backendl)
    public ImageView imgBackendl;
    @BindView(R.id.txt_uploadbackl)
    public TextView txtUploadbackl;
    @BindView(R.id.ed_eamil)
    public EditText edEamil;
    @BindView(R.id.btn_create)
    public TextView btnCreate;
    ImagePicker imagePicker;
    boolean isFrount = false;
    int image = 0;

    @BindView(R.id.spinner)
    public Spinner spinner;
    List<VehicleDataItem> cars = new ArrayList<>();
    String selectcar;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    RiderData riderData;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_upwith_otpactivity);
        db = FirebaseFirestore.getInstance();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        riderData = sessionManager.getUserDetails();
        ButterKnife.bind(this);
        imagePicker = new ImagePicker();
        getVehicleList();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectcar = cars.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getVehicleList() {
        JSONObject jsonObject = new JSONObject();

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getVehicleList(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");
    }


    private void uploadMultiFile() {


        List<MultipartBody.Part> identity_f = new ArrayList<>();
        if (riderData.getIdentityFront() != null) {
            identity_f.add(FileUtils.prepareFilePart("identity_front", riderData.getIdentityFront()));
        }
        List<MultipartBody.Part> identity_b = new ArrayList<>();
        if (riderData.getIdentityBack() != null) {
            identity_b.add(FileUtils.prepareFilePart("identity_back", riderData.getIdentityBack()));
        }

        List<MultipartBody.Part> license_f = new ArrayList<>();
        if (riderData.getLicenseFront() != null) {
            license_f.add(FileUtils.prepareFilePart("license_front", riderData.getLicenseFront()));
        }

        List<MultipartBody.Part> license_b = new ArrayList<>();
        if (riderData.getLicenseBack() != null) {
            license_b.add(FileUtils.prepareFilePart("license_back", riderData.getLicenseBack()));
        }

        List<MultipartBody.Part> pro_img = new ArrayList<>();
        if (riderData.getProImg() != null) {
            pro_img.add(FileUtils.prepareFilePart("pro_img", riderData.getProImg()));
        }


        custPrograssbar.prograssCreate(this);
        RequestBody fname = FileUtils.createPartFromString(riderData.getFname());
        RequestBody lname = FileUtils.createPartFromString(riderData.getLname());
        RequestBody vehicle_number = FileUtils.createPartFromString(edEamil.getText().toString());
        RequestBody email = FileUtils.createPartFromString(riderData.getEmail());
        RequestBody mobile = FileUtils.createPartFromString(riderData.getMobile());
        RequestBody ccode = FileUtils.createPartFromString(riderData.getCcode());
        RequestBody address = FileUtils.createPartFromString(riderData.getAddress());
        RequestBody password = FileUtils.createPartFromString(riderData.getPassword());
        RequestBody gender = FileUtils.createPartFromString(riderData.getGender());
        RequestBody vehicle_id = FileUtils.createPartFromString(selectcar);

        Call<JsonObject> call = APIClient.getInterface().personalDocument(fname, lname, vehicle_number, email, mobile, ccode, address, password, gender, vehicle_id, identity_f, identity_b, license_f, license_b, pro_img);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @OnClick({R.id.img_back, R.id.txt_uploadfrount, R.id.txt_uploadback, R.id.txt_uploadfrountl, R.id.txt_uploadbackl, R.id.btn_create})
    public void onBindClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_uploadfrount:

                txtUploadfrount.setError(null);
                isFrount = true;
                image = 1;
                bottonConfirm(this, imagePicker);

                break;
            case R.id.txt_uploadback:

                txtUploadback.setError(null);
                isFrount = false;
                image = 2;
                bottonConfirm(this, imagePicker);

                break;
            case R.id.txt_uploadfrountl:

                txtUploadfrountl.setError(null);
                isFrount = true;
                image = 3;
                bottonConfirm(this, imagePicker);

                break;
            case R.id.txt_uploadbackl:

                txtUploadbackl.setError(null);
                isFrount = false;
                image = 4;
                bottonConfirm(this, imagePicker);

                break;
            case R.id.btn_create:
                if (TextUtils.isEmpty(edEamil.getText().toString())) {
                    edEamil.setError(getString(R.string.entervehicalnumber));

                } else if (riderData.getIdentityFront() == null) {
                    txtUploadfrount.setError(getString(R.string.selectimage));

                } else if (riderData.getIdentityBack() == null) {
                    txtUploadback.setError(getString(R.string.selectimage));

                } else if (riderData.getLicenseFront() == null) {
                    txtUploadfrountl.setError(getString(R.string.selectimage));

                } else if (riderData.getLicenseBack() == null) {
                    txtUploadbackl.setError(getString(R.string.selectimage));

                } else {

                    startActivity(new Intent(SignUpwithOTPActivity.this, SMSCodeActivity.class));

                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == RESULT_OK) {
            imagePicker.addOnCompressListener(new ImageCompressionListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompressed(String filePath) {
                    if (filePath != null) {
                        if (isFrount) {
                            if (image == 1) {
                                riderData.setIdentityFront(filePath);
                                Glide.with(SignUpwithOTPActivity.this)
                                        .load(filePath)
                                        .into(imgFrount);
                            } else if (image == 3) {
                                riderData.setLicenseFront(filePath);
                                Glide.with(SignUpwithOTPActivity.this)
                                        .load(filePath)
                                        .into(imgFrountl);
                            }

                        } else {
                            if (image == 2) {
                                riderData.setIdentityBack(filePath);
                                Glide.with(SignUpwithOTPActivity.this)
                                        .load(filePath)
                                        .into(imgBackend);
                            } else if (image == 4) {
                                riderData.setLicenseBack(filePath);
                                Glide.with(SignUpwithOTPActivity.this)
                                        .load(filePath)
                                        .into(imgBackendl);
                            }

                        }

                    }
                }
            });
            String filePath = imagePicker.getImageFilePath(data);
            if (filePath != null) {
                if (isFrount) {
                    if (image == 1) {
                        riderData.setIdentityFront(filePath);
                        Glide.with(SignUpwithOTPActivity.this)
                                .load(filePath)
                                .into(imgFrount);
                    } else if (image == 3) {
                        riderData.setLicenseFront(filePath);
                        Glide.with(SignUpwithOTPActivity.this)
                                .load(filePath)
                                .into(imgFrountl);
                    }

                } else {
                    if (image == 2) {
                        riderData.setIdentityBack(filePath);
                        Glide.with(SignUpwithOTPActivity.this)
                                .load(filePath)
                                .into(imgBackend);
                    } else if (image == 4) {
                        riderData.setLicenseBack(filePath);
                        Glide.with(SignUpwithOTPActivity.this)
                                .load(filePath)
                                .into(imgBackendl);
                    }

                }
            }

        }
    }


    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Login login = gson.fromJson(result.toString(), Login.class);
                if (login.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setUserDetails(login.getRiderData());
                    sessionManager.setBooleanData("login", true);
                    startActivity(new Intent(SignUpwithOTPActivity.this, HomeActivity.class));

                    Map<String, Object> user = new HashMap<>();
                    user.put("name", login.getRiderData().getFname());
                    user.put("image", login.getRiderData().getProImg());
                    user.put("id", login.getRiderData().getId());
                    user.put("carimage", login.getRiderData().getCarimage());
                    user.put("type", login.getRiderData().getCarTyep());
                    user.put("isavailable", false);
                    OneSignal.sendTag("riderid", login.getRiderData().getId());
                    db.collection("Admin").document(login.getRiderData().getId())
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Error writing document", e);
                                }
                            });
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Vehicle contry = gson.fromJson(result.toString(), Vehicle.class);
                cars = contry.getVehicleData();
                List<String> arealist = new ArrayList<>();
                for (int i = 0; i < cars.size(); i++) {
                    if (cars.get(i).getStatus().equalsIgnoreCase("1")) {
                        arealist.add(cars.get(i).getTitle());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arealist);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner.setAdapter(dataAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isvarification == 4) {
            isvarification = -1;
            uploadMultiFile();
        }
    }
}