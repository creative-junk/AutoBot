package com.crysoft.me.autobot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.crysoft.me.autobot.adapters.CategoryAdapter;
import com.crysoft.me.autobot.adapters.LocalCategoryAdapter;
import com.crysoft.me.autobot.adapters.LocalMainCategoryAdapter;
import com.crysoft.me.autobot.adapters.MainCategoryAdapter;
import com.crysoft.me.autobot.database.DBAdapter;
import com.crysoft.me.autobot.models.CategoryModel;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.List;

public class ShopActivity extends AppCompatActivity {
    private DBAdapter databaseAdapter;

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ParseQueryAdapter<ParseObject> mainParseAdapter;

    private CategoryAdapter categoryAdapter;
    private MainCategoryAdapter mainCategoryAdapter;
    private LocalCategoryAdapter localCategoryAdapter;
    private LocalMainCategoryAdapter localMainCategoryAdapter;

    private List<CategoryModel> categoryList;
    private List<CategoryModel> mainCategoryList;

    private GridView gridView;
    private GridView mainGridView;

    private RelativeLayout rlLoading;
    private ViewFlipper mViewFlipper;

    int mFlipping = 0; // Initially flipping is off
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        //databaseAdapter = DBAdapter.getInstance(this);

        gridView = (GridView) findViewById(R.id.categoryGrid);
        mainGridView = (GridView) findViewById(R.id.mainCategoryGrid);
        rlLoading = (RelativeLayout) findViewById(R.id.loadingPanel);
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        mViewFlipper.setAutoStart(true);
        mViewFlipper.startFlipping();
        //Get Categories
        //Main Query Adapter
        mainParseAdapter = new ParseQueryAdapter<ParseObject>(this, "category");
        mainParseAdapter.setTextKey("category_name");
        mainParseAdapter.setImageKey("category_image");
        mainCategoryAdapter = new MainCategoryAdapter(this);
        //Set up the grid
        mainGridView.setAdapter(mainCategoryAdapter);
        //Load Stuff
        mainParseAdapter.loadObjects();
        mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Parse Main Cat:"," Clicked");
                ParseObject parseItem = (ParseObject) parent.getItemAtPosition(position);
                String objectId = parseItem.getObjectId();
                String objectTitle = parseItem.getString("category_name");

                CategoryModel categoryDetails = new CategoryModel();
                categoryDetails.setObjectId(objectId);
                categoryDetails.setCategoryName(objectTitle);
                categoryDetails.setCategoryImage(parseItem.getParseFile("category_image").getUrl());

            }
        });

    }

}
