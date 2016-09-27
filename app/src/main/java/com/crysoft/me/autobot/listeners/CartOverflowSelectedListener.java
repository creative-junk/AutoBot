package com.crysoft.me.autobot.listeners;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.crysoft.me.autobot.CartActivity;
import com.crysoft.me.autobot.R;
import com.crysoft.me.autobot.adapters.CartAdapter;
import com.crysoft.me.autobot.database.DBAdapter;
import com.crysoft.me.autobot.models.ProductsModel;


import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Maxx on 7/21/2016.
 */
public class CartOverflowSelectedListener implements View.OnClickListener {
    private Context mContext;
    private ProductsModel myProduct;
    private DBAdapter databaseAdapter;
    private CartAdapter mCartAdapter;
    private List<ProductsModel> mProductList;
    //Weak Reference to Prevent Memory Leaks
    //Refer to http://stackoverflow.com/questions/9723106/get-activity-instance
    private static WeakReference<CartActivity> mCartActivityRef;


    public CartOverflowSelectedListener(Context context, ProductsModel product, CartAdapter cartAdapter, List<ProductsModel> productList) {

        mContext = context;
        myProduct = product;
        mCartAdapter = cartAdapter;
        mProductList = productList;
        databaseAdapter = DBAdapter.getInstance(context);

    }

    @Override
    public void onClick(View v) {
        final ListView listView = (ListView) v.findViewById(R.id.cartList);
        final View view = v;
        //Use an Android v7 PopUp Menu Widget
        PopupMenu popupMenu = new PopupMenu(mContext, v) {

            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cart_overflow_remove:
                        //databaseAdapter.removeFromCart(myProduct.getObjectId());
                        deleteFromCart(myProduct.getObjectId());
                        return true;
                    default:
                        return super.onMenuItemSelected(menu, item);
                }

            }
        };
        popupMenu.inflate(R.menu.cart_menu);
        //popupMenu.getMenu().removeItem(R.id.cart_overflow_remove);
        popupMenu.show();
    }

    public void deleteFromCart(String objectId) {
        //We use the Weak Refernce to avoid memory leaks
        //See http://stackoverflow.com/questions/9723106/get-activity-instance
        CartActivity v = mCartActivityRef.get();

        databaseAdapter.removeFromCart(objectId);
        List<ProductsModel> cartItems = databaseAdapter.getCartItems();
        int nrOfItems=databaseAdapter.getNumberOfItems();
        String totalAmount = databaseAdapter.getCartAmount();
        mProductList.clear();
        mProductList.addAll(cartItems);

        mCartAdapter.notifyDataSetChanged();
        TextView cartTitle = (TextView) v.findViewById(R.id.cartTitle);
        TextView subTotal = (TextView) v.findViewById(R.id.subTotal);
        TextView total = (TextView) v.findViewById(R.id.tvTotal);
        subTotal.setText(totalAmount);
        total.setText(totalAmount);
        if (nrOfItems == 0) {
            v.findViewById(R.id.emptyCartList).setVisibility(View.VISIBLE);
            v.findViewById(R.id.cartList).setVisibility(View.GONE);
            v.findViewById(R.id.cartTitle).setVisibility(View.GONE);
            v.findViewById(R.id.lltotals).setVisibility(View.GONE);
            v.findViewById(R.id.btnCheckout).setVisibility(View.GONE);
        } else if (nrOfItems== 1) {
            cartTitle.setText("You have 1 item ready in your Shopping Cart");
        } else {
            cartTitle.setText("You have " + nrOfItems + " items ready in your Shopping Cart");
        }

    }



    public static void updateActivity(CartActivity cartActivity) {
        mCartActivityRef = new WeakReference<CartActivity>(cartActivity);
    }
}


