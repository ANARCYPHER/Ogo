package com.cscodetech.urbantaxi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.adepter.TripAdapter;
import com.cscodetech.urbantaxi.model.OrderHistoryItem;
import com.cscodetech.urbantaxi.model.RiderData;
import com.cscodetech.urbantaxi.model.Tripe;
import com.cscodetech.urbantaxi.retrofit.APIClient;
import com.cscodetech.urbantaxi.retrofit.GetResult;
import com.cscodetech.urbantaxi.utility.CustPrograssbar;
import com.cscodetech.urbantaxi.utility.SessionManager;
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

public class TripHistoryActivity extends AppCompatActivity implements GetResult.MyListener, TripAdapter.RecyclerTouchListener {

    @BindView(R.id.recyviewtrip)
    public RecyclerView recyviewtrip;

    @BindView(R.id.lvl_notfound)
    public LinearLayout lvlNotfound;
    @BindView(R.id.img_back)
    public ImageView imgBack;

    SessionManager sessionManager;
    RiderData user;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails();
        custPrograssbar = new CustPrograssbar();

        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this);
        mLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyviewtrip.setLayoutManager(mLayoutManager2);
        recyviewtrip.setItemAnimator(new DefaultItemAnimator());

        getTripe();

    }

    private void getTripe() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("rider_id", user.getId());


            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().tripependingHistory(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.prograssCreate(TripHistoryActivity.this);
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
                Tripe tripe = gson.fromJson(result.toString(), Tripe.class);
                if (tripe.getResult().equalsIgnoreCase("true")) {
                    if (tripe.getOrderHistory().size() != 0) {
                        recyviewtrip.setVisibility(View.VISIBLE);
                        lvlNotfound.setVisibility(View.GONE);
                        recyviewtrip.setAdapter(new TripAdapter(this, tripe.getOrderHistory(), this));

                    } else {
                        recyviewtrip.setVisibility(View.GONE);
                        lvlNotfound.setVisibility(View.VISIBLE);
                    }
                }
            }
        } catch (Exception e) {
e.printStackTrace();
        }
    }

    @Override
    public void onClickWheelerInfo(OrderHistoryItem item, int position) {

        startActivity(new Intent(TripHistoryActivity.this, TripeDetailsActivity.class).putExtra("order_id", item.getRideId()));

    }

    @OnClick({R.id.img_back})

    public void onBindClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }
}