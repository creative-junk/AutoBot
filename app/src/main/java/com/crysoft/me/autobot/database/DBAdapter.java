package com.crysoft.me.autobot.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.crysoft.me.autobot.ArticleDetails;
import com.crysoft.me.autobot.helpers.Constants;
import com.crysoft.me.autobot.helpers.Utils;
import com.crysoft.me.autobot.models.ArticleModel;
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
    private static final String DATABASE_NAME = "Autofix";
    private static final int DATABASE_VERSION = 1;
    //Table Names
    private static final String TABLE_ARTICLES ="articles";

    //ARTICLES TABLE
    private static final String KEY_ARTICLE_ID ="_id";
    private static final String KEY_ARTICLE_OBJECT_ID ="article_id";
    private static final String KEY_ARTICLE_COVER = "article_cover";
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
        String CREATE_ARTICLES_TABLE = "CREATE TABLE " + TABLE_ARTICLES +
                "(" +
                KEY_ARTICLE_ID + " INTEGER PRIMARY KEY," +
                KEY_ARTICLE_OBJECT_ID + " INTEGER," +
                KEY_ARTICLE_TITLE + " TEXT," +
                KEY_ARTICLE_COVER + " TEXT," +
                KEY_ARTICLE_CATEGORY + " TEXT," +
                KEY_ARTICLE_CONTENT + " TEXT" +
                ")";
        db.execSQL(CREATE_ARTICLES_TABLE);

    }

    //If we upgrade, we drop the database for now. Not really sure is this is the best approach but for now let's stick to basic implementations
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
            onCreate(db);

        }
    }

    //CRUD METHODS

    //Add or Update Article
    public boolean addOrUpdateArticle(ArticleModel articleDetails) {
        //Use the Cached Connection
        SQLiteDatabase db = getWritableDatabase();
        Boolean transactionSuccessful = false;

        //As usual Wrap it in a transaction
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ARTICLE_OBJECT_ID, articleDetails.getObjectId());
            values.put(KEY_ARTICLE_TITLE, articleDetails.getArticleTitle());
            values.put(KEY_ARTICLE_CATEGORY, articleDetails.getArticleCategory());
            values.put(KEY_ARTICLE_CONTENT, articleDetails.getArticleContent());
            values.put(KEY_ARTICLE_COVER, articleDetails.getFeaturedImage());

            //Let's try to update the Saved Article if it exists.
            int rows = db.update(TABLE_ARTICLES, values, KEY_ARTICLE_OBJECT_ID + "= ?", new String[]{articleDetails.getObjectId()});

            //Let's check if the update worked
            if (rows == 1) {
                //Ok, we have updated a Saved Product, we could probably get the Product updated at this point if we needed to
                db.setTransactionSuccessful();
                transactionSuccessful = true;

            } else {
                //No Such Article Here, insert it
                db.insertOrThrow(TABLE_ARTICLES, null, values);
                db.setTransactionSuccessful();
                transactionSuccessful = true;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error trying to Update Article");
            transactionSuccessful = false;
        } finally {
            db.endTransaction();
        }
        return transactionSuccessful;

    }

    //Get Articles
    public ArrayList<ArticleModel> getArticles() {
        ArrayList<ArticleModel> articleList = new ArrayList<ArticleModel>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTICLES, new String[]{
                KEY_ARTICLE_OBJECT_ID, KEY_ARTICLE_TITLE, KEY_ARTICLE_COVER, KEY_ARTICLE_CONTENT,KEY_ARTICLE_CATEGORY
        }, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ArticleModel articleDetails = new ArticleModel();
                    articleDetails.setObjectId(cursor.getString(1));
                    articleDetails.setArticleTitle(cursor.getString(2));
                    articleDetails.setFeaturedImage(cursor.getString(3));
                    articleDetails.setArticleContent(cursor.getString(4));
                    articleDetails.setArticleCategory(cursor.getString(5));
                    articleList.add(articleDetails);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error Retrieving Articles");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return articleList;

    }
    public void updateArticles(ArrayList<ArticleModel> articles){
        for (int i = 0; i < articles.size(); i++) {
            ArticleModel articleDetails = articles.get(i);
            addOrUpdateArticle(articleDetails);
        }
    }
    //remove an Article
    private boolean removeArticle(String objectId) {
        SQLiteDatabase db = getWritableDatabase();
        String delSQLString = "DELETE FROM " + TABLE_ARTICLES + " WHERE " + KEY_ARTICLE_OBJECT_ID + "=" + objectId + ";";
        db.execSQL(delSQLString);
        db.close();
        return true;
    }






}

