package com.crysoft.me.autobot;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

public class MainHome extends AppCompatActivity {
    private ParseUser currentUser;
    private TextView username;
    private EditText latitude;
    private EditText longitude;
    private ParseGeoPoint geoPoint;
    private ImageView ivMenuOverflow;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = ParseUser.getCurrentUser();
        setContentView(R.layout.activity_home);
        username = (TextView) findViewById(R.id.tvWelcomeTxt);
        mContext = this;


        if(currentUser==null){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            username.setText("Welcome back " + currentUser.getString("first_name")+"...");
        }
    }

    public void findAMechanic(View view){
        Intent intent = new Intent(this,MechanicsActivity.class);
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
        Intent intent = new Intent(this,StoreLocatorActivity.class);
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
