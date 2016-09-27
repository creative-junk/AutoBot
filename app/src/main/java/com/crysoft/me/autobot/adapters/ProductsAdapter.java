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
 * Created by Maxx on 7/18/2016.
 */
public class ProductsAdapter extends ParseQueryAdapter<ParseObject> {
    public ProductsAdapter(Context context, final String categoryId) {
        super(context,new ParseQueryAdapter.QueryFactory<ParseObject>(){

            @Override
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Product");
                //query.whereEqualTo("category_type","main");
                query.whereEqualTo("category_id", categoryId);
                return query;
            }
        });
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v==null){
            v = View.inflate(getContext(), R.layout.product_items,null);
        }
        super.getItemView(object, v, parent);

        //Add & download Image
        ParseImageView productImage = (ParseImageView) v.findViewById(R.id.productImage);
        ParseFile imageFile = object.getParseFile("product_image");
        if (imageFile != null){
            productImage.setParseFile(imageFile);
            productImage.loadInBackground();
        }

        TextView titleTextView = (TextView) v.findViewById(R.id.productName);
        String productName= object.getString("product_name").substring(0,20);
        productName = productName + "...";
        titleTextView.setText(productName);
        //Log.i("Title",object.getString("category_name"));
        TextView tagTextView = (TextView) v.findViewById(R.id.productPrice);
        String price = "Ksh " + object.getString("price");
        tagTextView.setText(price);
        return v;
    }
}
