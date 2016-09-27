package com.crysoft.me.autobot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

public class MainHome extends AppCompatActivity {
    ParseUser currentUser;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = ParseUser.getCurrentUser();
        if(currentUser==null){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            username = (TextView) findViewById(R.id.tvWelcomeTxt);
            username.setText("Welcome back " + currentUser.getString("first_name")+"...");
            setContentView(R.layout.activity_home);
        }


    }
    public void findAMechanic(View view){
        Intent intent = new Intent(this,MechanicsActivity.class);
        startActivity(intent);
    }
    public void goToShop(View view){
        Intent intent = new Intent(this,ShopActivity.class);
        startActivity(intent);
    }
    public void goToSettings(View view){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }
    public void goToAssist(View view){
        Intent intent = new Intent(this,RoadsideAssistActivity.class);
        startActivity(intent);
    }
    public void findAShop(View view){
        Intent intent = new Intent(this,LocationServicesActivity.class);
        startActivity(intent);
    }
    public void goToAlerts(View view){
        Intent intent = new Intent(this,AlertsActivity.class);
        startActivity(intent);
    }
    public void goToTips(View view){
        Intent intent = new Intent(this,QuickTipsActivity.class);
        startActivity(intent);
    }
    public void findFoodStops(View view){
        Intent intent = new Intent(this,UtilitiesActivity.class);
        startActivity(intent);
    }
}
