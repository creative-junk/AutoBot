package com.crysoft.me.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AlertsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

    }
    public void goToAlerts(View view){
        Intent intent = new Intent(this,QuickTipsList.class);
        intent.putExtra("title", "Alerts");
        intent.putExtra("id", "76");
        startActivity(intent);
    }
    public void goToNews(View view){
        Intent intent = new Intent(this,QuickTipsList.class);
        intent.putExtra("title", "News");
        intent.putExtra("id", "83");
        startActivity(intent);
    }
    public void goToTraffic(View view){
        Intent intent = new Intent(this,QuickTipsList.class);
        intent.putExtra("title", "Traffic");
        intent.putExtra("id", "77");

        startActivity(intent);
    }
    public void goToClosedRoads(View view){
        Intent intent = new Intent(this,QuickTipsList.class);
        intent.putExtra("title", "Traffic");
        intent.putExtra("id", "78");

        startActivity(intent);
    }
    public void goToDiversions(View view){
        Intent intent = new Intent(this,QuickTipsList.class);
        intent.putExtra("title", "Diversions");
        intent.putExtra("id", "82");

        startActivity(intent);
    }


}
