package com.crysoft.me.autobot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crysoft.me.autobot.R;
import com.crysoft.me.autobot.models.ProductsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Maxx on 7/20/2016.
 */
public class WishlistAdapter extends BaseAdapter {
    private List<ProductsModel> productList;
    private LayoutInflater layoutInflater;
    private Context myContext;

    public WishlistAdapter(LayoutInflater layoutInflater, List<ProductsModel> productList, Context context){
        this.layoutInflater = layoutInflater;
        this.productList = productList;
        this.myContext = context;
    }
    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.wishlist_items,null);
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.ivItemImage);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.tvItemName);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.tvItemPrice);
            //viewHolder.productDescription = (TextView) convertView.findViewById(R.id.tvItemDescription);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProductsModel productDetails = productList.get(position);
        int length;
        if (productDetails.getProductName().length()<50){
            length = productDetails.getProductName().length();
        }else{
            length=50;
        }
        viewHolder.productName.setText(productDetails.getProductName().substring(0,length)+"...");
        viewHolder.productPrice.setText(productDetails.getProductPrice());
//        viewHolder.productDescription.setText(productDetails.getDescription());

        if (productDetails.getImageFile()!=null){
            Picasso.with(myContext).load(productDetails.getImageFile()).into(viewHolder.productImage);
        }else{

        }
        return convertView;
    }
    private class ViewHolder{
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productDescription;
    }
}
