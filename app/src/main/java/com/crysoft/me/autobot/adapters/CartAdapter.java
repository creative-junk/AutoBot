package com.crysoft.me.autobot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crysoft.me.autobot.R;
import com.crysoft.me.autobot.helpers.Constants;
import com.crysoft.me.autobot.helpers.Utils;
import com.crysoft.me.autobot.listeners.CartOverflowSelectedListener;
import com.crysoft.me.autobot.listeners.CartQtyButtonListener;
import com.crysoft.me.autobot.models.ProductsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Maxx on 7/20/2016.
 */
public class CartAdapter extends BaseAdapter{
    private List<ProductsModel> productList;
    private LayoutInflater layoutInflater;
    private Context myContext;
    private CartAdapter cartAdapter;


    public CartAdapter(LayoutInflater layoutInflater, List<ProductsModel> productList, Context context){
        this.layoutInflater = layoutInflater;
        this.productList = productList;
        this.myContext = context;
        this.cartAdapter = this;
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

    public void swapItems(List<ProductsModel> items) {
        this.productList = items;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;


        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.cart_items,null);
            viewHolder.productImage = (ImageView) convertView.findViewById(R.id.ivCartItemImage);
            viewHolder.productName = (TextView) convertView.findViewById(R.id.tvCartItemName);
            viewHolder.productPrice = (TextView) convertView.findViewById(R.id.tvCartItemPrice);
            viewHolder.overflowMenu = (ImageView) convertView.findViewById(R.id.ivCartOverflow);
            viewHolder.cartQty = (EditText) convertView.findViewById(R.id.etQty);
            viewHolder.addQty = (Button) convertView.findViewById(R.id.addQty);
            viewHolder.minusQty = (Button) convertView.findViewById(R.id.minusQty);
            //viewHolder.productDescription = (TextView) convertView.findViewById(R.id.tvItemDescription);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ProductsModel productDetails = productList.get(position);
        viewHolder.overflowMenu.setOnClickListener(new CartOverflowSelectedListener(myContext, productDetails,cartAdapter,productList));
        viewHolder.addQty.setOnClickListener(new CartQtyButtonListener(myContext,productDetails,cartAdapter,productList,Constants.INCREASE_QTY));
        if (Integer.valueOf(productDetails.getQuantity()) > 1){
            viewHolder.minusQty.setOnClickListener(new CartQtyButtonListener(myContext,productDetails,cartAdapter,productList,Constants.DECREASE_QTY));
        }

        viewHolder.productName.setText(productDetails.getProductName().substring(0,20)+"...");
        viewHolder.productPrice.setText(Utils.formatPrice(Integer.valueOf(productDetails.getProductPrice())));
        viewHolder.cartQty.setText(productDetails.getQuantity());

     //   viewHolder.productDescription.setText(productDetails.getDescription());

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
        EditText cartQty;
        Button minusQty;
        Button addQty;
        ImageView overflowMenu;


    }
}
