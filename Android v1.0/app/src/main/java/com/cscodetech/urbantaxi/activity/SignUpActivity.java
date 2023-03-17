package com.cscodetech.urbantaxi.activity;

import static com.cscodetech.urbantaxi.utility.Utility.bottonConfirm;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.imagepicker.ImageCompressionListener;
import com.cscodetech.urbantaxi.imagepicker.ImagePicker;
import com.cscodetech.urbantaxi.model.Contry;
import com.cscodetech.urbantaxi.model.CountryCodeItem;
import com.cscodetech.urbantaxi.model.RiderData;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.retrofit.GetResult;
import com.cscodetech.urbantaxi.utility.CustPrograssbar;
import com.cscodetech.urbantaxi.utility.SessionManager;
import com.cscodetech.urbantaxi.utility.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SignUpActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.ed_firstname)
    public EditText edFirstname;
    @BindView(R.id.ed_lastname)
    public EditText edLastname;
    @BindView(R.id.ed_eamil)
    public EditText edEamil;
    @BindView(R.id.ed_phone)
    public EditText edPhone;
    @BindView(R.id.ed_password)
    public EditText edPassword;
    @BindView(R.id.ed_address)
    public EditText edAddress;
    @BindView(R.id.show_pass_btn)
    public ImageView showPassBtn;
    @BindView(R.id.img_profiel)
    public ImageView imgProfiel;

    @BindView(R.id.btn_create)
    public TextView btnCreate;
    @BindView(R.id.radioGrp)
    public RadioGroup radioGrp;

    @BindView(R.id.spinner)
    public Spinner spinner;
    List<CountryCodeItem> cCodes = new ArrayList<>();
    String codeSelect;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    ImagePicker imagePicker;
    String selectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        imagePicker = new ImagePicker();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);


        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getCode();
    }

    private void getCode() {
        JSONObject jsonObject = new JSONObject();

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCodelist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");
    }


    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Contry contry = gson.fromJson(result.toString(), Contry.class);
                cCodes = contry.getCountryCode();
                List<String> arealist = new ArrayList<>();
                for (int i = 0; i < cCodes.size(); i++) {
                    if (cCodes.get(i).getStatus().equalsIgnoreCase("1")) {
                        arealist.add(cCodes.get(i).getCcode());
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


    @OnClick({R.id.img_back, R.id.btn_create, R.id.img_profiel})
    public void onBindClick(View view) {
        int id = view.getId();
        if (id == R.id.img_back) {
            finish();
        } else if (id == R.id.img_profiel) {
            bottonConfirm(this, imagePicker);
        } else if (id == R.id.btn_create && validationCreate()) {
            RiderData user = new RiderData();
            user.setCcode(codeSelect);
            user.setFname("" + edFirstname.getText().toString());
            user.setLname("" + edLastname.getText().toString());
            user.setEmail("" + edEamil.getText().toString());
            user.setMobile("" + edPhone.getText().toString());
            user.setPassword("" + edPassword.getText().toString());
            user.setAddress("" + edAddress.getText().toString());
            int selectedId = radioGrp.getCheckedRadioButtonId();
            RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
            user.setGender("" + radioSexButton.getText().toString());
            user.setProImg(selectImage);
            sessionManager.setUserDetails(user);
            Utility.isvarification = 1;
            startActivity(new Intent(this, SignUpwithOTPActivity.class));


        }
    }


    public boolean validationCreate() {
        if (TextUtils.isEmpty(edFirstname.getText().toString())) {
            edFirstname.setError("Enter Name");
            return false;
        }
        if (TextUtils.isEmpty(edLastname.getText().toString())) {
            edLastname.setError("Enter Mobile");
            return false;
        }

        if (TextUtils.isEmpty(edEamil.getText().toString())) {
            edEamil.setError("Enter Password");
            return false;
        }
        if (TextUtils.isEmpty(edPassword.getText().toString())) {
            edPassword.setError("Enter Password");
            return false;
        }

        return true;
    }

    public void ShowHidePass(View view) {

        if (view.getId() == R.id.show_pass_btn) {

            if (edPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.hidden);

                //Show Password
                edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.eye);

                //Hide Password
                edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
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
                        selectImage = filePath;

                        Glide.with(SignUpActivity.this)
                                .load(filePath)
                                .into(imgProfiel);

                    }
                }
            });
            String filePath = imagePicker.getImageFilePath(data);
            if (filePath != null) {
                selectImage = filePath;
                Glide.with(SignUpActivity.this)
                        .load(filePath)
                        .into(imgProfiel);

            }

        }
    }


}