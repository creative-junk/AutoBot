package com.crysoft.me.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class QuickTipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_tips);

    }
    public void goToSafetyTips(View view){
        Intent intent = new Intent(this,QuickTipsList.class);
        intent.putExtra("title", "Safety Tips");
        intent.putExtra("id", "80");
        startActivity(intent);
    }
    public void goToService(View view){
        Intent intent = new Intent(this,QuickTipsList.class);
        intent.putExtra("title", "Service");
        intent.putExtra("id", "81");
        startActivity(intent);
    }
    public void goToFuelEconomy(View view){
        Intent intent = new Intent(this,QuickTipsList.class);
        intent.putExtra("title", "Fuel Economy");
        intent.putExtra("id", "82");

        startActivity(intent);
    }

}
