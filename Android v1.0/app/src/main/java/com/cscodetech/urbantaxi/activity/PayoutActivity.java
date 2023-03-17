package com.cscodetech.urbantaxi.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.adepter.PayoutAdapter;
import com.cscodetech.urbantaxi.model.Payout;
import com.cscodetech.urbantaxi.model.RestResponse;
import com.cscodetech.urbantaxi.model.RiderData;
import com.cscodetech.urbantaxi.model.Withdraw;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.retrofit.GetResult;
import com.cscodetech.urbantaxi.utility.CustPrograssbar;
import com.cscodetech.urbantaxi.utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PayoutActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.txt_withdraw)
    public TextView txtWithdraw;
    @BindView(R.id.btn_earnig)
    public TextView btnEarnig;
    @BindView(R.id.btn_limit)
    public TextView btnLimit;
    @BindView(R.id.recyviewtrip)
    public RecyclerView recyviewtrip;
    @BindView(R.id.animationView)
    public LottieAnimationView animationView;
    @BindView(R.id.lvl_notfound)
    public LinearLayout lvlNotfound;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    RiderData riderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(this);

        riderData = sessionManager.getUserDetails();
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyviewtrip.setLayoutManager(mLayoutManager2);
        recyviewtrip.setItemAnimator(new DefaultItemAnimator());

        getEarnReport();
    }

    private void getEarnReport() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rider_id", riderData.getId());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().earnReport(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(PayoutActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendWithdraw(String amt, String type, String bankname, String accnumber, String acc_name, String ifsccode, String upiid, String paypalid) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rider_id", riderData.getId());
            jsonObject.put("amt", amt);
            jsonObject.put("r_type", type);
            jsonObject.put("bank_name", bankname);
            jsonObject.put("acc_number", accnumber);
            jsonObject.put("acc_name", acc_name);
            jsonObject.put("ifsc_code", ifsccode);
            jsonObject.put("upi_id", upiid);
            jsonObject.put("paypal_id", paypalid);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().requestWithdraw(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "2");
            custPrograssbar.prograssCreate(PayoutActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getPayoutList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rider_id", riderData.getId());

            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getPayoutlist(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");
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
                Withdraw withdraw = gson.fromJson(result.toString(), Withdraw.class);
                if (withdraw.getResult().equalsIgnoreCase("true")) {
                    btnLimit.setText(sessionManager.getStringData(SessionManager.currency) + withdraw.getReportData().getWithdrawLimit());
                    btnEarnig.setText(sessionManager.getStringData(SessionManager.currency) + withdraw.getReportData().getTotalEarning());
                    getPayoutList();
                }

            } else if (callNo.equalsIgnoreCase("2")) {

                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    finish();
                }
            } else if (callNo.equalsIgnoreCase("3")) {

                Gson gson = new Gson();
                Payout restResponse = gson.fromJson(result.toString(), Payout.class);
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    if (restResponse.getPayoutlist().size() != 0) {
                        recyviewtrip.setVisibility(View.VISIBLE);
                        lvlNotfound.setVisibility(View.GONE);
                        recyviewtrip.setAdapter(new PayoutAdapter(this, restResponse.getPayoutlist()));
                    } else {
                        recyviewtrip.setVisibility(View.GONE);
                        lvlNotfound.setVisibility(View.VISIBLE);
                    }
                } else {
                    recyviewtrip.setVisibility(View.GONE);
                    lvlNotfound.setVisibility(View.VISIBLE);
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.img_back, R.id.txt_withdraw})

    public void onBindClick(View view) {
        int id = view.getId();
        if (id == R.id.img_back) {
            finish();
        } else if (id == R.id.txt_withdraw) {
            bottonWithdraw();
        }
    }

    public void bottonWithdraw() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_withdraw, null);
        mBottomSheetDialog.setContentView(sheetView);

        RadioGroup radio = sheetView.findViewById(R.id.radio);
        TextView btnSubmit = sheetView.findViewById(R.id.btn_submit);
        EditText edAmount = sheetView.findViewById(R.id.ed_amount);

        EditText edbankname = sheetView.findViewById(R.id.ed_bankname);
        EditText edaccountno = sheetView.findViewById(R.id.ed_accountno);
        EditText edaccountname = sheetView.findViewById(R.id.ed_accountname);
        EditText edaccountifsc = sheetView.findViewById(R.id.ed_accountifsc);
        EditText edaccountupi = sheetView.findViewById(R.id.ed_accountupi);
        EditText edaccountpaypal = sheetView.findViewById(R.id.ed_accountpaypal);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = sheetView.findViewById(i);
                Log.e("IT", "-->" + i);
                switch (radioButton.getText().toString()) {
                    case "upi":
                        edbankname.setVisibility(View.GONE);
                        edaccountno.setVisibility(View.GONE);
                        edaccountname.setVisibility(View.GONE);
                        edaccountifsc.setVisibility(View.GONE);
                        edaccountupi.setVisibility(View.VISIBLE);
                        edaccountpaypal.setVisibility(View.GONE);
                        break;
                    case "bank transfer":
                        edbankname.setVisibility(View.VISIBLE);
                        edaccountno.setVisibility(View.VISIBLE);
                        edaccountname.setVisibility(View.VISIBLE);
                        edaccountifsc.setVisibility(View.VISIBLE);
                        edaccountupi.setVisibility(View.GONE);
                        edaccountpaypal.setVisibility(View.GONE);
                        break;
                    case "paypal":
                        edbankname.setVisibility(View.GONE);
                        edaccountno.setVisibility(View.GONE);
                        edaccountname.setVisibility(View.GONE);
                        edaccountifsc.setVisibility(View.GONE);
                        edaccountupi.setVisibility(View.GONE);
                        edaccountpaypal.setVisibility(View.VISIBLE);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + radioButton.getText().toString());
                }
            }
        });
        btnSubmit.setOnClickListener(view -> {


            int selectedId = radio.getCheckedRadioButtonId();
            if (selectedId <= 0) {
                Toast.makeText(this, "Choose Radio Button Please", Toast.LENGTH_SHORT).show();
            } else {
                // find the radiobutton by returned id
                RadioButton radioButton = sheetView.findViewById(selectedId);
                switch (radioButton.getText().toString()) {
                    case "upi":
                        if (TextUtils.isEmpty(edaccountupi.getText().toString())) {
                            edaccountupi.setError("");

                        } else {
                            mBottomSheetDialog.cancel();
                            sendWithdraw(edAmount.getText().toString(), radioButton.getText().toString(), edbankname.getText().toString()
                                    , edaccountno.getText().toString(), edaccountname.getText().toString(), edaccountifsc.getText().toString(), edaccountupi.getText().toString(), edaccountpaypal.getText().toString());
                        }
                        break;
                    case "bank transfer":
                        if (TextUtils.isEmpty(edbankname.getText().toString())) {
                            edbankname.setError("");


                        } else if (TextUtils.isEmpty(edaccountname.getText().toString())) {
                            edaccountname.setError("");

                        } else if (TextUtils.isEmpty(edaccountno.getText().toString())) {
                            edaccountno.setError("");

                        } else if (TextUtils.isEmpty(edaccountifsc.getText().toString())) {
                            edaccountifsc.setError("");

                        } else {
                            mBottomSheetDialog.cancel();
                            sendWithdraw(edAmount.getText().toString(), radioButton.getText().toString(), edbankname.getText().toString()
                                    , edaccountno.getText().toString(), edaccountname.getText().toString(), edaccountifsc.getText().toString(), edaccountupi.getText().toString(), edaccountpaypal.getText().toString());
                        }
                        break;
                    case "paypal":
                        if (TextUtils.isEmpty(edaccountpaypal.getText().toString())) {
                            edaccountpaypal.setError("");

                        } else {
                            mBottomSheetDialog.cancel();
                            sendWithdraw(edAmount.getText().toString(), radioButton.getText().toString(), edbankname.getText().toString()
                                    , edaccountno.getText().toString(), edaccountname.getText().toString(), edaccountifsc.getText().toString(), edaccountupi.getText().toString(), edaccountpaypal.getText().toString());
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + radioButton.getText().toString());
                }


            }


        });

        mBottomSheetDialog.show();
    }
}