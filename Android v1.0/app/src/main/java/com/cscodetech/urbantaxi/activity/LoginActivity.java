package com.cscodetech.urbantaxi.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.model.Contry;
import com.cscodetech.urbantaxi.model.CountryCodeItem;
import com.cscodetech.urbantaxi.model.Login;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.retrofit.GetResult;
import com.cscodetech.urbantaxi.utility.CustPrograssbar;
import com.cscodetech.urbantaxi.utility.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.spinner)
    public Spinner spinner;
    @BindView(R.id.ed_username)
    public EditText edUsername;
    @BindView(R.id.ed_password)
    public EditText edPassword;
    @BindView(R.id.show_pass_btn)
    public ImageView showPassBtn;
    @BindView(R.id.rlt_password)
    public RelativeLayout rltPassword;
    @BindView(R.id.btn_login)
    public TextView btnLogin;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    List<CountryCodeItem> cCodes = new ArrayList<>();
    String codeSelect;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        db=FirebaseFirestore.getInstance();

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

    private void login() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edUsername.getText().toString());
            jsonObject.put("ccode", codeSelect);
            jsonObject.put("password", edPassword.getText().toString());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().login(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void getCode() {
        JSONObject jsonObject = new JSONObject();

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCodelist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }


    @OnClick({R.id.btn_login})
    public void onBindClick(View view) {
        if (view.getId() == R.id.btn_login) {
            if (TextUtils.isEmpty(edUsername.getText().toString())) {
                edUsername.setError("");

            } else if (TextUtils.isEmpty(edPassword.getText().toString())) {
                edPassword.setError("");
            } else {
                login();

            }
        }
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
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Login login = gson.fromJson(result.toString(), Login.class);
                if (login.getResult().equalsIgnoreCase("true")) {

                    sessionManager.setUserDetails(login.getRiderData());
                    sessionManager.setBooleanData("login", true);


                    OneSignal.sendTag("riderid",login.getRiderData().getId());
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));


                    Map<String, Object> user = new HashMap<>();
                    user.put("name", login.getRiderData().getFname());
                    user.put("image", login.getRiderData().getProImg());
                    user.put("carimage", login.getRiderData().getCarimage());
                    user.put("id", login.getRiderData().getId());
                    user.put("type", login.getRiderData().getCarTyep());
                    user.put("isavailable", false);
                    OneSignal.sendTag("riderid",login.getRiderData().getId());
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

                }else {
                    Toast.makeText(LoginActivity.this,login.getResponseMsg(),Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
e.printStackTrace();
        }
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
}