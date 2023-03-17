package com.cscodetech.urbantaxi.activity;

import static com.cscodetech.urbantaxi.utility.SessionManager.language;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.urbantaxi.R;
import com.cscodetech.urbantaxi.utility.SessionManager;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        sessionManager = new SessionManager(this);

        if(sessionManager.getStringData(language).equalsIgnoreCase("ar")){
            setApplicationlanguage("ar");
        }else {
            setApplicationlanguage(sessionManager.getStringData(language));

        }

    }

    public void setApplicationlanguage(String language) {
        try {
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                conf.setLocale(new Locale(language)); // API 17+ only.
            } else {
                conf.locale = new Locale(language);
            }
            res.updateConfiguration(conf, dm);
        } catch (Exception e) {
            Log.e("Error for RTL ", "-->" + e.getMessage());
        }

    }
}