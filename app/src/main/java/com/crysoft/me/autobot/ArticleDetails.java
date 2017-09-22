package com.crysoft.me.autobot;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.crysoft.me.autobot.models.ArticleModel;
import com.squareup.picasso.Picasso;

public class ArticleDetails extends AppCompatActivity {

    ArticleModel article;
    WebView articleText;
    ImageView featuredImage;
    String mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent articleIntent = getIntent();
        article =articleIntent.getExtras().getParcelable("tip_details");
        mainImage = articleIntent.getExtras().getString("image");
        toolbar.setTitle(article.getArticleTitle());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        featuredImage = (ImageView) findViewById(R.id.featuredImage);

        articleText = (WebView) findViewById(R.id.wvArticle);

        Picasso.with(ArticleDetails.this).load(article.getFeaturedImage()).into(featuredImage);

        articleText.getSettings().setJavaScriptEnabled(false);
        articleText.setWebViewClient(new WebViewClient());

        articleText.loadData(article.getArticleContent(),"text/html","UTF-8");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
