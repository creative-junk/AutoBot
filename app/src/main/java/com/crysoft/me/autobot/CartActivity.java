package com.crysoft.me.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crysoft.me.autobot.adapters.CartAdapter;
import com.crysoft.me.autobot.database.DBAdapter;
import com.crysoft.me.autobot.listeners.CartOverflowSelectedListener;
import com.crysoft.me.autobot.listeners.CartQtyButtonListener;
import com.crysoft.me.autobot.models.ProductsModel;
import com.parse.ParseUser;

import java.util.List;

public class CartActivity extends AppCompatActivity {
   public CartAdapter cartAdapter;
   private DBAdapter databaseAdapter;
   public static CartActivity cartActivity;

   private ListView listView;
   public List<ProductsModel> productList;
   private LinearLayout emptyView;

   private TextView cartTitle;
   private TextView subTotal;
   private TextView total;
    private EditText etQty;
   private LinearLayout llTotals;
   private Button checkoutBtn;
   private Button continueShoppingBtn;

   String totalAmount;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       //Get an Instance of this activity for use in the OnCliCk Listener
       //cartActivity = this;
       //Getting a static reference for this class has the potential to cause a Memory Leak so the approach below is used
       //Check http://stackoverflow.com/questions/9723106/get-activity-instance
       CartOverflowSelectedListener.updateActivity(this);
       CartQtyButtonListener.updateButtonActivity(this);

       setContentView(R.layout.activity_cart);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);


       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       //Setup the DB
       databaseAdapter = DBAdapter.getInstance(this);


       //Setup the layout
       listView = (ListView) findViewById(R.id.cartList);
       emptyView = (LinearLayout) findViewById(R.id.emptyCartList);
       llTotals = (LinearLayout) findViewById(R.id.lltotals);
       etQty = (EditText) findViewById(R.id.etQty);

       checkoutBtn = (Button) findViewById(R.id.btnCheckout);
       subTotal = (TextView) findViewById(R.id.subTotal);
       total = (TextView) findViewById(R.id.tvTotal);
       cartTitle = (TextView) findViewById(R.id.cartTitle);

       checkoutBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               gotoCheckout();
           }
       });


       populateList();

   }
   public void populateList(){
       productList = databaseAdapter.getCartItems();
       totalAmount = databaseAdapter.getCartAmount();

       subTotal.setText(totalAmount);
       total.setText(totalAmount);

       if (productList.size() == 0) {
           emptyView.setVisibility(View.VISIBLE);
           listView.setVisibility(View.GONE);
           cartTitle.setVisibility(View.GONE);
           llTotals.setVisibility(View.GONE);
           checkoutBtn.setVisibility(View.GONE);


           Log.i("List is", "Empty");
       } else {


           cartAdapter = new CartAdapter(getLayoutInflater(), productList, this);

           if (databaseAdapter.getNumberOfItems() == 1) {
               cartTitle.setText("You have 1 item ready in your Shopping Cart");
           } else {
               cartTitle.setText("You have " + databaseAdapter.getNumberOfItems() + " items ready in your Shopping Cart");
           }

           listView.setAdapter(cartAdapter);
           /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                   ProductsModel productDetails = productList.get(position);
                   intent.putExtra("product_details", productDetails);
                   startActivity(intent);
                   ((Activity) getActivity()).overridePendingTransition(0, 0);
               }
           });*/
       }
   }
    private void gotoCheckout(){
        Intent i = new Intent(this,CheckoutActivity.class);
        this.startActivity(i);
    }
    private void continueShopping(View view){
        finish();
    }

   @Override
   public void onResume() {
       super.onResume();
       populateList();
       productList.clear();
       productList.addAll(databaseAdapter.getCartItems());

       // cartAdapter.swapItems(databaseAdapter.getCartItems());

   }



   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.home_menu, menu);
       return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();
       if (id == android.R.id.home) {
           finish();
       }
       switch (id) {
           case R.id.action_search:
               newSearch();
               return true;
           case R.id.action_cart:
               showCart();
               return true;
           case R.id.nav_signin:

               return true;

           default:
               return super.onOptionsItemSelected(item);
       }
   }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (ParseUser.getCurrentUser() != null) {
            menu.findItem(R.id.nav_signin).setTitle("Home");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void newSearch(){

    }
    public void showCart(){
        Intent i = new Intent(this,CartActivity.class);
        this.startActivity(i);
    }



}
