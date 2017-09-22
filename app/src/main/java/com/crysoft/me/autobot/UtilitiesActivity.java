package com.crysoft.me.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Maxx on 9/27/2016.
 */

public class UtilitiesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);

    }
    public void findRestaurant(View view) {
        Intent i = new Intent(this, FoodStopsActivity.class);
        String strName = "restaurant";
        i.putExtra("utility", strName);
        i.putExtra("title","Restaurants Near Me");
        startActivity(i);
    }
    public void findAtms(View view) {
        Intent i = new Intent(this, FoodStopsActivity.class);
        String strName = "atm";
        i.putExtra("utility", strName);
        i.putExtra("title","ATMs Near Me");
        startActivity(i);
    }
    public void findBanks(View view) {
        Intent i = new Intent(this, FoodStopsActivity.class);
        String strName = "bank";
        i.putExtra("utility", strName);
        i.putExtra("title","Banks Near Me");
        startActivity(i);
    }
    public void findHospitals(View view) {
        Intent i = new Intent(this, FoodStopsActivity.class);
        String strName = "hospital";
        i.putExtra("utility", strName);
        i.putExtra("title","Hospitals Near Me");
        startActivity(i);
    }
    public void findAirports(View view) {
        Intent i = new Intent(this, FoodStopsActivity.class);
        String strName = "airport";
        i.putExtra("utility", strName);
        i.putExtra("title","Airports Near Me");
        startActivity(i);
    }
    public void findMalls(View view) {
        Intent i = new Intent(this, FoodStopsActivity.class);
        String strName = "shopping_mall";
        i.putExtra("utility", strName);
        i.putExtra("title","Malls Near Me");
        startActivity(i);
    }
    public void findBars(View view) {
        Intent i = new Intent(this, FoodStopsActivity.class);
        String strName = "bar";
        i.putExtra("utility", strName);
        i.putExtra("title","Bars Near Me");
        startActivity(i);
    }
    public void findPoliceStations(View view) {
        Intent i = new Intent(this, FoodStopsActivity.class);
        String strName = "police";
        i.putExtra("utility", strName);
        i.putExtra("title","Police Stations Near Me");
        startActivity(i);
    }
    public void findEmbassy(View view) {
        Intent i = new Intent(this, FoodStopsActivity.class);
        String strName = "embassy";
        i.putExtra("utility", strName);
        i.putExtra("title","Embassies Near Me");
        startActivity(i);
    }
}
