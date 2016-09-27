package com.crysoft.me.autobot.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crysoft.me.autobot.R;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * Created by Maxx on 7/29/2016.
 */
public class MainCategoryAdapter extends ParseQueryAdapter<ParseObject> {
    public MainCategoryAdapter(Context context) {
        super(context,new ParseQueryAdapter.QueryFactory<ParseObject>(){

            @Override
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Category");
                query.whereEqualTo("category_type","main");
                return query;
            }
        });
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v==null){
            v = View.inflate(getContext(), R.layout.main_category_items,null);
        }
        super.getItemView(object, v, parent);

        //Add & download Image
        ParseImageView categoryImage = (ParseImageView) v.findViewById(R.id.mainCategoryImage);
        ParseFile imageFile = object.getParseFile("category_image");
        if (imageFile != null){
            categoryImage.setParseFile(imageFile);
            categoryImage.loadInBackground();
        }

        TextView titleLink = (TextView) v.findViewById(R.id.mainCategoryLink);
        titleLink.setText(object.getString("category_name"));

        return v;
    }
}
