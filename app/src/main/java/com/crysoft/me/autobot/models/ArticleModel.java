package com.crysoft.me.autobot.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Maxx on 9/23/2016.
 */
public class ArticleModel implements Parcelable {
    private String articleTitle;
    private String articleContent;
    private String articleCategory;
    private String articleSummary;
    private String objectId;
    private String featuredImage;

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        this.articleCategory = articleCategory;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getArticleSummary() {
        return articleSummary;
    }

    public void setArticleSummary(String articleSummary) {
        this.articleSummary = articleSummary;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public ArticleModel(Parcel in) {
        String[] array = new String[6];
        in.readStringArray(array);
        articleTitle = array[0];
        articleContent = array[1];
        articleCategory = array[2];
        objectId = array[3];
        articleSummary = array[4];
        featuredImage = array[5];
    }
    public ArticleModel(){

    }
    public static final Parcelable.Creator<ArticleModel> CREATOR = new Parcelable.Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel in) {
            return new ArticleModel(in);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.articleTitle, this.articleContent, this.articleCategory,this.objectId,this.articleSummary,this.featuredImage});
    }
}
