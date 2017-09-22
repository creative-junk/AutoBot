package com.crysoft.me.autobot;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crysoft.me.autobot.adapters.ArticleAdapter;
import com.crysoft.me.autobot.database.DBAdapter;
import com.crysoft.me.autobot.helpers.Constants;
import com.crysoft.me.autobot.helpers.Utils;
import com.crysoft.me.autobot.models.ArticleModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class QuickTipsList extends AppCompatActivity {
    ListView list_detail;
    ArrayList<ArticleModel> quicktips;
    ArrayList<ArticleModel> articlesList;

    DBAdapter databaseAdapter;

    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private ArticleAdapter articleAdapter;
    int pos;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_tips_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent articleIntent = getIntent();
        String title = articleIntent.getExtras().getString("title");
        id = articleIntent.getExtras().getString("id");
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyView = (LinearLayout) findViewById(R.id.emptyView);

        databaseAdapter =DBAdapter.getInstance(this);

        quicktips = new ArrayList<>();

        new ArticleAdapter(QuickTipsList.this,quicktips,new ArticleAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(ArticleModel article) {

                Intent i = new Intent(QuickTipsList.this, ArticleDetails.class);

                //Get the Quicktip Details
                ArticleModel tipDetails = new ArticleModel();
                tipDetails.setObjectId(article.getObjectId());
                tipDetails.setArticleTitle(article.getArticleTitle());
                tipDetails.setArticleSummary(article.getArticleSummary());
                tipDetails.setArticleContent(article.getArticleContent());
                tipDetails.setFeaturedImage(article.getFeaturedImage());
                //  tipDetails.setContent(quicktips.get(pos).getContent());
                Log.i("Featured Image",article.getFeaturedImage());
                //Push the Parceable Model through the intent
                i.putExtra("tip_details", tipDetails);
                i.putExtra("image",article.getFeaturedImage());
                startActivity(i);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1,dpToPx(10),true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(articleAdapter);


        if (Utils.isOnline(this)){
            new QuickTips().execute();
        }
    }
    public void updateListView(){

    }
    public class QuickTips extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(QuickTipsList.this);
        InputStream inputStream = null;
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //emptyView.setVisibility(View.GONE);
          //  list_detail.setVisibility(View.GONE);

            progressDialog.setMessage("Getting Content...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }});

        }

        @Override
        protected Void doInBackground(Void... params) {
            URL hp = null;
            try{
                quicktips.clear();
                hp = new URL(getString(R.string.liveurl) + id);

                Log.i("URL", "" + hp);
                URLConnection hpCon = hp.openConnection();

                hpCon.connect();
                inputStream = hpCon.getInputStream();

                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line + '\n');
                }
                inputStream.close();
                result = stringBuilder.toString();
                //Get and Set the retrieved Data
                JSONArray quicktipsArray = new JSONArray(result);

                Log.i("URL", "" + quicktipsArray.toString());

                for (int i=0;i < quicktipsArray.length();i++){
                    JSONObject quicktipsObject = quicktipsArray.getJSONObject(i);
                    JSONObject links = new JSONObject(quicktipsObject.getString("better_featured_image"));
                    JSONObject embeddedMedia = new JSONObject(links.getString("media_details"));
                    JSONObject thumbnail = new JSONObject(embeddedMedia.getString("sizes"));
                    JSONObject href = new JSONObject(thumbnail.getString("thumbnail"));

                    ArticleModel tip = new ArticleModel();
                    tip.setObjectId(quicktipsObject.getString("id"));
                    tip.setArticleTitle(new JSONObject(quicktipsObject.getString("title")).getString("rendered"));
                    tip.setArticleSummary(new JSONObject(quicktipsObject.getString("excerpt")).getString("rendered"));
                    tip.setFeaturedImage(href.getString("source_url"));
                    //Log.i("Image source",href.getString("source_url"));
                    tip.setArticleContent(new JSONObject(quicktipsObject.getString("content")).getString("rendered"));
                    quicktips.add(tip);
                }
                //articleAdapter.notifyDataSetChanged();

                //databaseAdapter.updateQuickTips(quicktips);

            } catch (MalformedURLException e) {
                //Error = "Wrong URL";
                e.printStackTrace();
            } catch (IOException e) {
                //Error = "File Not Found";
                e.printStackTrace();
            } catch (JSONException e) {
                //Error = "Bad Results";
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if (quicktips.isEmpty()){
                    emptyView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                //list_detail.setVisibility(View.VISIBLE);
            ArticleAdapter tipAdapter = new ArticleAdapter(QuickTipsList.this,quicktips,new ArticleAdapter.OnItemClickListener(){

                @Override
                public void onItemClick(ArticleModel article) {

                    Intent i = new Intent(QuickTipsList.this, ArticleDetails.class);

                    //Get the Quicktip Details
                    ArticleModel tipDetails = new ArticleModel();
                    tipDetails.setObjectId(article.getObjectId());
                    tipDetails.setArticleTitle(article.getArticleTitle());
                    tipDetails.setArticleSummary(article.getArticleSummary());
                    tipDetails.setArticleContent(article.getArticleContent());
                    tipDetails.setFeaturedImage(article.getFeaturedImage());
                    //  tipDetails.setContent(quicktips.get(pos).getContent());

                    //Push the Parceable Model through the intent
                    i.putExtra("tip_details", tipDetails);
                    startActivity(i);
                }
            });
            recyclerView.setAdapter(tipAdapter);

            tipAdapter.notifyDataSetChanged();


        }

    }

    private int dpToPx(int dp){
        Resources r = getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics()));
    }


    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount=spanCount;
            this.spacing=spacing;
            this.includeEdge=includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge){
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount){
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            }else{
                outRect.left = column * spacing / spanCount;
                outRect.right =spacing - (column + 1) * spacing/ spanCount;
                if (position >= spanCount){
                    outRect.top = spacing;
                }
            }

        }

    }
}
