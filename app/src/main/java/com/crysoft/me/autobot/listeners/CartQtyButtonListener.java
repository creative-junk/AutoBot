package com.crysoft.me.autobot.listeners;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.crysoft.me.autobot.CartActivity;
import com.crysoft.me.autobot.R;
import com.crysoft.me.autobot.adapters.CartAdapter;
import com.crysoft.me.autobot.database.DBAdapter;
import com.crysoft.me.autobot.models.ProductsModel;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Maxx on 7/22/2016.
 */
public class CartQtyButtonListener implements View.OnClickListener {
    private Context mContext;
    private ProductsModel myProduct;
    private DBAdapter databaseAdapter;
    private CartAdapter mCartAdapter;
    private List<ProductsModel> mProductList;
    private int mOperation;

    //Weak Reference to Prevent Memory Leaks
    //Refer to http://stackoverflow.com/questions/9723106/get-activity-instance
    private static WeakReference<CartActivity> mCartActivityRef;

    public CartQtyButtonListener(Context context, ProductsModel product, CartAdapter cartAdapter, List<ProductsModel> productList, int operation) {
        mContext = context;
        myProduct = product;
        mCartAdapter = cartAdapter;
        mProductList = productList;
        databaseAdapter = DBAdapter.getInstance(context);
        mOperation=operation;
    }


    @Override
    public void onClick(View view) {
        //We use the Weak Refernce to avoid memory leaks
        //See http://stackoverflow.com/questions/9723106/get-activity-instance
        CartActivity v= mCartActivityRef.get();

        databaseAdapter.updateQuantity(myProduct.getObjectId(),mOperation);
        List<ProductsModel> cartItems = databaseAdapter.getCartItems();
        int nrOfItems = databaseAdapter.getNumberOfItems();
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
        } else if (nrOfItems == 1) {
            cartTitle.setText("You have 1 item ready in your Shopping Cart");
        } else if (nrOfItems >1){
            cartTitle.setText("You have " + nrOfItems + " items ready in your Shopping Cart");
        }

    }


    public static void updateButtonActivity(CartActivity cartActivity) {
        mCartActivityRef = new WeakReference<CartActivity>(cartActivity);
    }
}
