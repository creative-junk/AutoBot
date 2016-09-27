package com.crysoft.me.autobot.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.crysoft.me.autobot.helpers.Constants;
import com.crysoft.me.autobot.helpers.Utils;
import com.crysoft.me.autobot.models.CategoryModel;
import com.crysoft.me.autobot.models.ProductsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxx on 7/19/2016.
 */
public class DBAdapter extends SQLiteOpenHelper {
    //First let's make this class a singleton to avoid memory leaks and some other scary stuff like unnecessary relocations
    private static DBAdapter sInstance;
    private Context context;
    //Let's tag this b*
    private static final String TAG = "DBAdapter";

    //Database Information
    private static final String DATABASE_NAME = "Tobias";
    private static final int DATABASE_VERSION = 2;
    //Table Names
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_MAIN_CATEGORIES = "main_categories";
    private static final String TABLE_SHOPPING_CART = "shopping_cart";
    private static final String TABLE_FAVOURITES = "favourites";
    private static final String TABLE_RECENT_SEARCHES = "search_table";
    private static final String TABLE_RECENTLY_VIEWED = "recently_viewed";
    private static final String TABLE_SLIDESHOW ="slideshow_table";
    private static final String TABLE_ARTICLE_CATEGORIES ="article_categories";
    private static final String TABLE_ARTICLES ="articles";

    //MAIN CATEGORIES table columns
    private static final String KEY_MAIN_CATEGORY_ID = "_id";
    private static final String KEY_MAIN_CATEGORY_OBJECT_ID = "main_category_id";
    private static final String KEY_MAIN_CATEGORY_NAME = "main_category_name";
    private static final String KEY_MAIN_CATEGORY_IMAGE = "main_category_image";

    //CATEGORIES table columns
    private static final String KEY_CATEGORY_ID ="_id";
    private static final String KEY_CATEGORY_OBJECT_ID ="category_id";
    private static final String KEY_CATEGORY_NAME ="category_name";
    private static final String KEY_CATEGORY_IMAGE ="category_image";
    private static final String KEY_CATEGORY_TAG ="category_tag";
    private static final String KEY_CATEGORY_TYPE="category_type";
    private static final String KEY_CATEGORY_PARENT_ID ="parent_id";

    //CART table columns
    private static final String KEY_CART_ID = "_id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_PRODUCT_DESCRIPTION = "product_description";
    private static final String KEY_PRODUCT_PRICE = "product_price";
    private static final String KEY_PRODUCT_IMAGE = "product_image";
    private static final String KEY_PRODUCT_STATUS = "product_status";
    private static final String KEY_PRODUCT_QUANTITY = "product_quantity";
    private static final String KEY_CART_STATUS = "cart_status";

    //FAVOURITES table columns
    private static final String KEY_FAVOURITES_ID = "_id";
    private static final String KEY_FAV_PRODUCT_ID = "fav_product_id";
    private static final String KEY_FAV_PRODUCT_NAME = "fav_product_name";
    private static final String KEY_FAV_PRODUCT_DESCRIPTION = "fav_product_description";
    private static final String KEY_FAV_PRODUCT_PRICE = "fav_product_price";
    private static final String KEY_FAV_PRODUCT_IMAGE = "fav_product_image";
    private static final String KEY_FAV_PRODUCT_QUANTITY = "fav_product_quantity";
    private static final String KEY_FAV_PRODUCT_STATUS = "fav_product_status";
    private static final String KEY_FAV_STATUS = "fav_status";

    //SEARCH TABLE
    private static final String KEY_SEARCH_ID ="_id";
    private static final String KEY_SEARCH_WORD = "search_word";

    //RECENTLY VIEWED TABLE
    private static final String KEY_VIEWED_ID ="_id";
    private static final String KEY_VIEWED_PRODUCT_ID = "product_id";
    private static final String KEY_VIEWED_PRODUCT_NAME = "product_name";
    private static final String KEY_VIEWED_PRODUCT_DESCRIPTION = "product_description";
    private static final String KEY_VIEWED_PRODUCT_PRICE = "product_price";
    private static final String KEY_VIEWED_PRODUCT_IMAGE = "product_image";
    private static final String KEY_VIEWED_PRODUCT_STATUS = "product_status";
    private static final String KEY_VIEWED_STATUS = "cart_status";

    //SLIDESHOW TABLE

    //ARTICLE CATEGORY TABLE
    private static final String KEY_ARTICLE_CATEGORY_ID ="_id";
    private static final String KEY_ARTICLE_CATEGORY_OBJECT_ID ="category_id";
    private static final String KEY_ARTICLE_CATEGORY_NAME ="category_name";
    private static final String KEY_ARTICLE_CATEGORY_IMAGE ="category_image";
    private static final String KEY_ARTICLE_CATEGORY_TAG ="category_tag";
    private static final String KEY_ARTICLE_CATEGORY_TYPE="category_type";
    private static final String KEY_ARTICLE_CATEGORY_PARENT_ID ="parent_id";

    //ARTICLES TABLE
    private static final String KEY_ARTICLE_ID ="_id";
    private static final String KEY_ARTICLE_OBJECT_ID ="article_id";
    private static final String KEY_ARTICLE_TITLE = "article_title";
    private static final String KEY_ARTICLE_CONTENT = "article_content";
    private static final String KEY_ARTICLE_CATEGORY ="article_category";


    // lets create a method for giving back an instance of this class so there will only ever be one instance at a time. If we have one we just return it,otherwise we create a new one
    public static synchronized DBAdapter getInstance(Context context) {
        //We use the application context so we don't accidentally leak an activities Context. see http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBAdapter(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    //Initialize the Database AND CREATE our tables if needed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAIN_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_MAIN_CATEGORIES +
                "(" +
                KEY_MAIN_CATEGORY_ID + " INTEGER PRIMARY KEY," +
                KEY_MAIN_CATEGORY_OBJECT_ID + " INTEGER," +
                KEY_MAIN_CATEGORY_NAME + " TEXT," +
                KEY_MAIN_CATEGORY_IMAGE + " TEXT" +
                ")";
        String CREATE_CATEGORIES_TABLE ="CREATE TABLE " + TABLE_CATEGORIES +
                "(" +
                KEY_CATEGORY_ID + " INTEGER PRIMARY KEY," +
                KEY_CATEGORY_OBJECT_ID + " INTEGER," +
                KEY_CATEGORY_NAME + " TEXT," +
                KEY_CATEGORY_IMAGE + " TEXT," +
                KEY_CATEGORY_TAG + " TEXT," +
                KEY_CATEGORY_TYPE + " TEXT," +
                KEY_CATEGORY_PARENT_ID + " TEXT" +
                ")";

        String CREATE_SHOPPING_CART_TABLE = "CREATE TABLE " + TABLE_SHOPPING_CART +
                "(" +
                KEY_CART_ID + " INTEGER PRIMARY KEY," +
                KEY_PRODUCT_ID + " INTEGER," +
                KEY_PRODUCT_NAME + " TEXT," +
                KEY_PRODUCT_PRICE + " TEXT," +
                KEY_PRODUCT_IMAGE + " TEXT," +
                KEY_PRODUCT_DESCRIPTION + " TEXT," +
                KEY_PRODUCT_STATUS + " INTEGER," +
                KEY_CART_STATUS + " INTEGER," +
                KEY_PRODUCT_QUANTITY + " INTEGER" +
                ")";

        String CREATE_FAVOURITES_TABLE = "CREATE TABLE " + TABLE_FAVOURITES +
                "(" +
                KEY_FAVOURITES_ID + " INTEGER PRIMARY KEY," +
                KEY_FAV_PRODUCT_ID + " INTEGER," +
                KEY_FAV_PRODUCT_NAME + " TEXT," +
                KEY_FAV_PRODUCT_PRICE + " TEXT," +
                KEY_FAV_PRODUCT_IMAGE + " TEXT," +
                KEY_FAV_PRODUCT_DESCRIPTION + " TEXT," +
                KEY_FAV_PRODUCT_STATUS + " INTEGER," +
                KEY_FAV_STATUS + " INTEGER," +
                KEY_FAV_PRODUCT_QUANTITY + " INTEGER" +
                ")";
        String CREATE_SEARCH_TABLE = "CREATE TABLE " + TABLE_RECENT_SEARCHES +
                "(" +
                KEY_SEARCH_ID + " INTEGER PRIMARY KEY," +
                KEY_SEARCH_WORD + " TEXT" +
                ")";
        String CREATE_RECENTLY_VIEWED_TABLE ="CREATE TABLE " + TABLE_RECENTLY_VIEWED +
                "(" +
                KEY_VIEWED_ID + " INTEGER," +
                KEY_VIEWED_PRODUCT_ID + " INTEGER," +
                KEY_VIEWED_PRODUCT_NAME + " TEXT," +
                KEY_VIEWED_PRODUCT_PRICE + " TEXT," +
                KEY_VIEWED_PRODUCT_IMAGE + " TEXT," +
                KEY_VIEWED_PRODUCT_DESCRIPTION + " TEXT," +
                KEY_VIEWED_PRODUCT_STATUS + " INTEGER," +
                KEY_VIEWED_STATUS + " INTEGER" +
                ")";
        db.execSQL(CREATE_MAIN_CATEGORIES_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_SHOPPING_CART_TABLE);
        db.execSQL(CREATE_FAVOURITES_TABLE);
        db.execSQL(CREATE_SEARCH_TABLE);
        db.execSQL(CREATE_RECENTLY_VIEWED_TABLE);

    }

    //If we upgrade, we drop the database for now. Not really sure is this is the best approach but for now let's stick to basic implementations
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN_CATEGORIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_CART);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENT_SEARCHES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECENTLY_VIEWED);
            onCreate(db);

        }
    }

    //CRUD METHODS
    //Add Product to cart
    public boolean insertOrUpdateCart(ProductsModel productDetails) {
        long productId = -1;
        SQLiteDatabase db = getWritableDatabase();
        Boolean transactionSuccessful = false;

        //Let's wrap the operation in a transaction for Performance and Consistency
        db.beginTransaction();
        try {


            //Let's check if the user exists and get the quantity in the process
            if (getProductQty(productDetails.getObjectId()) > 0) {
                int quantity = getProductQty(productDetails.getObjectId());
                Log.i("Current QTY", String.valueOf(quantity));
                int newQuantity = ++quantity;

                ContentValues values = new ContentValues();
                values.put(KEY_PRODUCT_ID, productDetails.getObjectId());
                values.put(KEY_PRODUCT_NAME, productDetails.getProductName());
                values.put(KEY_PRODUCT_PRICE, productDetails.getProductPrice());
                values.put(KEY_PRODUCT_IMAGE, productDetails.getImageFile());
                values.put(KEY_PRODUCT_DESCRIPTION, productDetails.getDescription());
                values.put(KEY_PRODUCT_STATUS, productDetails.getProductStatus());
                values.put(KEY_CART_STATUS, productDetails.getCartStatus());
                //Product Exists, update its quantity
                values.put(KEY_PRODUCT_QUANTITY, newQuantity);
                Log.i("New Qty", String.valueOf(newQuantity));
                int rows = db.update(TABLE_SHOPPING_CART, values, KEY_PRODUCT_ID + "=?", new String[]{productDetails.getObjectId()});
                //int rows = db.update(TABLE_SHOPPING_CART,values,KEY_PRODUCT_ID+ "='"+productDetails.getObjectId()+"'",null);
                db.setTransactionSuccessful();
                transactionSuccessful = true;
            } else {
                //It's a new Product, Insert it
                ContentValues values = new ContentValues();
                values.put(KEY_PRODUCT_ID, productDetails.getObjectId());
                values.put(KEY_PRODUCT_NAME, productDetails.getProductName());
                values.put(KEY_PRODUCT_PRICE, productDetails.getProductPrice());
                values.put(KEY_PRODUCT_IMAGE, productDetails.getImageFile());
                values.put(KEY_PRODUCT_DESCRIPTION, productDetails.getDescription());
                values.put(KEY_PRODUCT_STATUS, productDetails.getProductStatus());
                values.put(KEY_CART_STATUS, productDetails.getCartStatus());
                values.put(KEY_PRODUCT_QUANTITY, "1");
                db.insertOrThrow(TABLE_SHOPPING_CART, null, values);
                db.setTransactionSuccessful();
                transactionSuccessful = true;
            }

        } catch (Exception e) {
            Log.d(TAG, "Error Updating Shopping Cart-InsertOrUpdate");
            Log.i("Error:", e.getMessage());
            e.printStackTrace();
            transactionSuccessful = false;
        } finally {
            db.endTransaction();
        }
        return transactionSuccessful;

    }

    //Get the Quantity given a Product Id
    public int getProductQty(String objectId) {
        int quantity = 0;
        SQLiteDatabase db = getWritableDatabase();
        /*String userSelectQuery = String.format("SELECT % FROM % WHERE %s=",new String[]{ KEY_PRODUCT_QUANTITY, TABLE_SHOPPING_CART, KEY_PRODUCT_ID});
        Cursor cursor = db.rawQuery(userSelectQuery, new String[]{objectId});
        */
        Cursor cursor = db.rawQuery(
                "SELECT " + KEY_PRODUCT_QUANTITY + " FROM " + TABLE_SHOPPING_CART + " WHERE " + KEY_PRODUCT_ID + "=?", new String[]{objectId}
        );

        if (cursor.moveToFirst()) {

            quantity = cursor.getInt(0);
            Log.i("Gotten Qty", String.valueOf(cursor.getInt(0)));
            cursor.close();


        }
        return quantity;

    }

    public void updateQuantity(String objectId, int operation) {
        SQLiteDatabase db = getWritableDatabase();
        String operationMode = "+";
        if (operation == Constants.INCREASE_QTY) {
            operationMode = "+";
        } else {
            operationMode = "-";
        }
        String rawQuery="UPDATE " + TABLE_SHOPPING_CART + " SET " + KEY_PRODUCT_QUANTITY + " = " + KEY_PRODUCT_QUANTITY + " " + operationMode + " "+ 1 + " WHERE " + KEY_PRODUCT_ID + " = '" + objectId + "'";
        Log.i("Query is",rawQuery);

        Cursor c = db.rawQuery(rawQuery,null);
        if(c.moveToFirst())
        {
            Log.i("Result: ","Succeded");
        }else{
            Log.i("Result: ","Failed");
        }
    }

    //Get Cart Items
    public List<ProductsModel> getCartItems() {
        List<ProductsModel> productList = new ArrayList<ProductsModel>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHOPPING_CART, new String[]{
                KEY_CART_ID, KEY_PRODUCT_ID, KEY_PRODUCT_NAME, KEY_PRODUCT_PRICE, KEY_PRODUCT_IMAGE, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_QUANTITY, KEY_PRODUCT_STATUS, KEY_CART_STATUS
        }, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ProductsModel productDetails = new ProductsModel();
                    productDetails.setObjectId(cursor.getString(1));
                    productDetails.setProductName(cursor.getString(2));
                    productDetails.setProductPrice(cursor.getString(3));
                    productDetails.setImageFile(cursor.getString(4));
                    productDetails.setDescription(cursor.getString(5));
                    productDetails.setQuantity(String.valueOf(cursor.getInt(6)));

                    productDetails.setProductStatus(cursor.getString(7));
                    productDetails.setCartStatus(cursor.getString(8));
                    Log.i("Qty", String.valueOf(cursor.getInt(6)));
                    productList.add(productDetails);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving Cart Items");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productList;

    }

    //Get Cart Total
    public String getCartAmount() {
        int amount = 0;


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHOPPING_CART, new String[]{
                KEY_CART_ID, KEY_PRODUCT_ID, KEY_PRODUCT_NAME, KEY_PRODUCT_PRICE, KEY_PRODUCT_IMAGE, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_QUANTITY, KEY_PRODUCT_STATUS, KEY_CART_STATUS
        }, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    amount = amount + Integer.valueOf(cursor.getString(3));
                    amount = amount * Integer.valueOf(cursor.getString(6));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving Cart Items");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return Utils.formatPrice(amount);

    }

    //Get Cart Total
    public int getUnformattedCartAmount() {
        int amount = 0;


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHOPPING_CART, new String[]{
                KEY_CART_ID, KEY_PRODUCT_ID, KEY_PRODUCT_NAME, KEY_PRODUCT_PRICE, KEY_PRODUCT_IMAGE, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_QUANTITY, KEY_PRODUCT_STATUS, KEY_CART_STATUS
        }, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    amount = amount + Integer.valueOf(cursor.getString(3));
                    amount = amount * Integer.valueOf(cursor.getString(6));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving Cart Items");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return amount;

    }
    //Get Number of Items in Cart
    public int getNumberOfItems() {
        int nrItems = 0;


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHOPPING_CART, new String[]{
                KEY_CART_ID, KEY_PRODUCT_ID, KEY_PRODUCT_NAME, KEY_PRODUCT_PRICE, KEY_PRODUCT_IMAGE, KEY_PRODUCT_DESCRIPTION, KEY_PRODUCT_QUANTITY, KEY_PRODUCT_STATUS, KEY_CART_STATUS
        }, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    nrItems = nrItems + Integer.valueOf(cursor.getString(6));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving Cart Items");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return nrItems;

    }

    //Remove Item from Cart
    public void removeFromCart(String objectId) {
        SQLiteDatabase db = getWritableDatabase();
        String delSQLString = "DELETE FROM " + TABLE_SHOPPING_CART + " WHERE " + KEY_PRODUCT_ID + "='" + objectId + "';";
        db.execSQL(delSQLString);
        db.close();
        //Log.i("Delete","Delete Called");
        //cartAdapter.notifyDataSetChanged();


    }

    //Add to Wishlist
    public boolean addOrUpdateWishlist(ProductsModel productDetails) {
        //Use the Cached Connection
        SQLiteDatabase db = getWritableDatabase();
        Boolean transactionSuccessful = false;

        //As usual Wrap it in a transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_FAV_PRODUCT_ID, productDetails.getObjectId());
            values.put(KEY_FAV_PRODUCT_NAME, productDetails.getProductName());
            values.put(KEY_FAV_PRODUCT_PRICE, productDetails.getProductPrice());
            values.put(KEY_FAV_PRODUCT_IMAGE, productDetails.getImageFile());
            values.put(KEY_FAV_PRODUCT_DESCRIPTION, productDetails.getDescription());
            values.put(KEY_FAV_PRODUCT_STATUS, productDetails.getProductStatus());
            values.put(KEY_FAV_STATUS, productDetails.getFavStatus());

            //Let's try to update the Saved Product if it exists.
            int rows = db.update(TABLE_FAVOURITES, values, KEY_FAV_PRODUCT_ID + "= ?", new String[]{productDetails.getObjectId()});

            //Let's check if the update worked
            if (rows == 1) {
                //Ok, we have updated a Saved Product, we could probably get the Product updated at this point if we needed to
                db.setTransactionSuccessful();
                transactionSuccessful = true;

            } else {
                //No Such Product Here, insert it
                db.insertOrThrow(TABLE_FAVOURITES, null, values);
                db.setTransactionSuccessful();
                transactionSuccessful = true;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error trying to Update Wishlist");
            transactionSuccessful = false;
        } finally {
            db.endTransaction();
        }
        return transactionSuccessful;

    }

    //Get Wishlist Items
    public List<ProductsModel> getWishlist() {
        List<ProductsModel> productList = new ArrayList<ProductsModel>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITES, new String[]{
                KEY_FAVOURITES_ID, KEY_FAV_PRODUCT_ID, KEY_FAV_PRODUCT_NAME, KEY_FAV_PRODUCT_PRICE, KEY_FAV_PRODUCT_IMAGE, KEY_FAV_PRODUCT_DESCRIPTION, KEY_FAV_PRODUCT_QUANTITY, KEY_FAV_PRODUCT_STATUS, KEY_FAV_STATUS
        }, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ProductsModel productDetails = new ProductsModel();
                    productDetails.setObjectId(cursor.getString(1));
                    productDetails.setProductName(cursor.getString(2));
                    productDetails.setProductPrice(cursor.getString(3));
                    productDetails.setImageFile(cursor.getString(4));
                    productDetails.setDescription(cursor.getString(5));
                    productDetails.setQuantity(cursor.getString(6));
                    productDetails.setProductStatus(cursor.getString(7));
                    productDetails.setFavStatus(cursor.getString(8));
                    productList.add(productDetails);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving Wishlist Items");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productList;

    }

    //remove Item from Wishlist
    private boolean removeFromWishlist(String objectId) {
        SQLiteDatabase db = getWritableDatabase();
        String delSQLString = "DELETE FROM " + TABLE_FAVOURITES + " WHERE " + KEY_FAV_PRODUCT_ID + "=" + objectId + ";";
        db.execSQL(delSQLString);
        db.close();
        return true;
    }


    //Add to Recently Viewed
    public boolean addOrUpdateRecentlyViewed(ProductsModel productDetails) {
        Log.i("Recently Viewed", "You are here");
        //Use the Cached Connection
        SQLiteDatabase db = getWritableDatabase();
        Boolean transactionSuccessful = false;

        //As usual Wrap it in a transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_VIEWED_PRODUCT_ID, productDetails.getObjectId());
            values.put(KEY_VIEWED_PRODUCT_NAME, productDetails.getProductName());
            values.put(KEY_VIEWED_PRODUCT_PRICE, productDetails.getProductPrice());
            values.put(KEY_VIEWED_PRODUCT_IMAGE, productDetails.getImageFile());
            values.put(KEY_VIEWED_PRODUCT_DESCRIPTION, productDetails.getDescription());
            values.put(KEY_VIEWED_PRODUCT_STATUS, productDetails.getProductStatus());


            //Let's try to update the Saved Product if it exists.
            int rows = db.update(TABLE_RECENTLY_VIEWED, values, KEY_VIEWED_PRODUCT_ID + "= ?", new String[]{productDetails.getObjectId()});

            //Let's check if the update worked
            if (rows == 1) {
                //Ok, we have updated a Saved Product, we could probably get the Product updated at this point if we needed to
                db.setTransactionSuccessful();
                transactionSuccessful = true;

            } else {
                //No Such Product Here, insert it
                db.insertOrThrow(TABLE_RECENTLY_VIEWED, null, values);
                db.setTransactionSuccessful();
                transactionSuccessful = true;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error trying to Update Recently Viewed");
            transactionSuccessful = false;
        } finally {
            db.endTransaction();
        }
        return transactionSuccessful;

    }

    //Get RECENTLY viewed
    public List<ProductsModel> getRecentlyViewed() {
        List<ProductsModel> productList = new ArrayList<ProductsModel>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECENTLY_VIEWED, new String[]{
                KEY_VIEWED_ID,KEY_VIEWED_PRODUCT_ID,KEY_VIEWED_PRODUCT_NAME,KEY_VIEWED_PRODUCT_PRICE,KEY_VIEWED_PRODUCT_IMAGE,KEY_VIEWED_PRODUCT_DESCRIPTION,KEY_VIEWED_PRODUCT_STATUS
        }, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ProductsModel productDetails = new ProductsModel();
                    productDetails.setObjectId(cursor.getString(cursor.getColumnIndex(KEY_VIEWED_PRODUCT_ID)));
                    productDetails.setProductName(cursor.getString(cursor.getColumnIndex(KEY_VIEWED_PRODUCT_NAME)));
                    productDetails.setProductPrice(cursor.getString(cursor.getColumnIndex(KEY_VIEWED_PRODUCT_PRICE)));
                    productDetails.setImageFile(cursor.getString(cursor.getColumnIndex(KEY_VIEWED_PRODUCT_IMAGE)));
                    productDetails.setDescription(cursor.getString(cursor.getColumnIndex(KEY_VIEWED_PRODUCT_DESCRIPTION)));
                    productDetails.setProductStatus(cursor.getString(cursor.getColumnIndex(KEY_VIEWED_PRODUCT_STATUS)));
                    productList.add(productDetails);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving Recently Viewed Items");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return productList;

    }
    public boolean emptyCart() {
        SQLiteDatabase db = getWritableDatabase();
        String delSQLString = "DELETE FROM " + TABLE_SHOPPING_CART + ";";
        db.execSQL(delSQLString);
        db.close();
        return true;
    }
    //Clear the recently viewed Table
    public boolean emptyRecentlyViewed() {
        SQLiteDatabase db = getWritableDatabase();
        String delSQLString = "DELETE FROM " + TABLE_RECENTLY_VIEWED + ";";
        db.execSQL(delSQLString);
        db.close();
        Log.i("Empty recent",delSQLString);
        return true;
    }

    //Add to Recently Searched
    public boolean addSearchWord(String word) {
        //Use the Cached Connection
        SQLiteDatabase db = getWritableDatabase();
        Boolean transactionSuccessful = false;

        //As usual Wrap it in a transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_SEARCH_WORD,word);
                //insert the keyword
                db.insertOrThrow(TABLE_RECENT_SEARCHES, null, values);
                db.setTransactionSuccessful();
                transactionSuccessful = true;

        } catch (Exception e) {
            Log.d(TAG, "Error trying to Update Recently Viewed");
            transactionSuccessful = false;
        } finally {
            db.endTransaction();
        }
        return transactionSuccessful;

    }

    //Get RECENTLY viewed
    public List<String> getRecentSearches() {
        List<String> keyWords = new ArrayList<String>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECENT_SEARCHES, new String[]{
                KEY_SEARCH_ID, KEY_SEARCH_WORD}, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    keyWords.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving Wishlist Items");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return keyWords;

    }
    //Clear the recently viewed Table
    public boolean emptyRecentSearch() {
        SQLiteDatabase db = getWritableDatabase();
        String delSQLString = "DELETE FROM " + TABLE_RECENT_SEARCHES + ";";
        db.execSQL(delSQLString);
        db.close();
        return true;
    }

    //Main Categories
    public void updateMainCategories(ArrayList<CategoryModel> categories){
        for (int i=0;i<categories.size();i++){
            CategoryModel categoryDetails=categories.get(i);
            insertOrUpdateMainCategory(categoryDetails);
        }

    }
    public boolean insertOrUpdateMainCategory(CategoryModel categoryDetails){
        SQLiteDatabase db = getWritableDatabase();
        Boolean transactionSuccessful = false;

        //As usual Wrap it in a transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_MAIN_CATEGORY_OBJECT_ID, categoryDetails.getObjectId());
            values.put(KEY_MAIN_CATEGORY_NAME, categoryDetails.getCategoryName());
            values.put(KEY_MAIN_CATEGORY_IMAGE, categoryDetails.getCategoryImage());


            //Let's try to update the Saved Product if it exists.
            int rows = db.update(TABLE_MAIN_CATEGORIES, values, KEY_MAIN_CATEGORY_OBJECT_ID + "= ?", new String[]{categoryDetails.getObjectId()});

            //Let's check if the update worked
            if (rows == 1) {
                //Ok, we have updated a Saved Product, we could probably get the Product updated at this point if we needed to
                db.setTransactionSuccessful();
                transactionSuccessful = true;

            } else {
                //No Such Product Here, insert it
                db.insertOrThrow(TABLE_MAIN_CATEGORIES, null, values);
                db.setTransactionSuccessful();
                transactionSuccessful = true;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error trying to Update Main Categories");
            transactionSuccessful = false;
        } finally {
            db.endTransaction();
        }
        return transactionSuccessful;

    }
    //Get MAIN CATEGORIES viewed
    public List<CategoryModel> getMainCategories() {
        List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_MAIN_CATEGORIES, new String[]{
                KEY_MAIN_CATEGORY_ID, KEY_MAIN_CATEGORY_OBJECT_ID, KEY_MAIN_CATEGORY_NAME, KEY_MAIN_CATEGORY_IMAGE
        }, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    CategoryModel categoryDetails = new CategoryModel();
                    categoryDetails.setObjectId(cursor.getString(1));
                    categoryDetails.setCategoryName(cursor.getString(2));
                    categoryDetails.setCategoryImage(cursor.getString(3));
                    categoryList.add(categoryDetails);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving Main categories");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return categoryList;

    }


    public void updateAllCategories(ArrayList<CategoryModel> categories){
        for (int i=0;i<categories.size();i++){
            CategoryModel categoryDetails=categories.get(i);
            insertOrUpdateCategory(categoryDetails);
        }

    }
    public boolean insertOrUpdateCategory(CategoryModel categoryDetails){
        SQLiteDatabase db = getWritableDatabase();
        Boolean transactionSuccessful = false;

        //As usual Wrap it in a transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CATEGORY_OBJECT_ID, categoryDetails.getObjectId());
            values.put(KEY_CATEGORY_NAME, categoryDetails.getCategoryName());
            values.put(KEY_CATEGORY_IMAGE, categoryDetails.getCategoryImage());
            values.put(KEY_CATEGORY_TYPE, categoryDetails.getCategoryType());
            values.put(KEY_CATEGORY_TAG, categoryDetails.getCategoryTag());
            values.put(KEY_CATEGORY_PARENT_ID, categoryDetails.getParentId());

            //Let's try to update the Saved Product if it exists.
            int rows = db.update(TABLE_CATEGORIES, values, KEY_CATEGORY_OBJECT_ID + "= ?", new String[]{categoryDetails.getObjectId()});

            //Let's check if the update worked
            if (rows == 1) {
                //Ok, we have updated a Saved Product, we could probably get the Product updated at this point if we needed to
                db.setTransactionSuccessful();
                transactionSuccessful = true;

            } else {
                //No Such Product Here, insert it
                db.insertOrThrow(TABLE_CATEGORIES, null, values);
                db.setTransactionSuccessful();
                transactionSuccessful = true;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error trying to Update Categories");
            transactionSuccessful = false;
        } finally {
            db.endTransaction();
        }
        return transactionSuccessful;

    }
    //Get Categories
    public List<CategoryModel> getAllCategories() {
        List<CategoryModel> categoryList = new ArrayList<CategoryModel>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{
                KEY_CATEGORY_ID, KEY_CATEGORY_OBJECT_ID, KEY_CATEGORY_NAME, KEY_CATEGORY_IMAGE, KEY_CATEGORY_TAG
        }, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    CategoryModel categoryDetails = new CategoryModel();
                    categoryDetails.setObjectId(cursor.getString(1));
                    categoryDetails.setCategoryName(cursor.getString(2));
                    categoryDetails.setCategoryImage(cursor.getString(3));
                    categoryDetails.setCategoryTag(cursor.getString(4));
                    categoryList.add(categoryDetails);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving categories");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return categoryList;

    }




}

