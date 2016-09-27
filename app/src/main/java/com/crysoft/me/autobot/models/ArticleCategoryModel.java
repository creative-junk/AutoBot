package com.crysoft.me.autobot.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Maxx on 9/23/2016.
 */
public class ArticleCategoryModel implements Parcelable{
    private String categoryName;
    private String categoryImage;
    private String categoryTag;
    private String categoryType;
    private String objectId;
    private String parentId;

    public ArticleCategoryModel(Parcel in) {
        String[] array = new String[6];
        in.readStringArray(array);
        categoryName = array[0];
        categoryImage = array[1];
        categoryTag = array[2];
        objectId = array[3];
        categoryType = array[4];
        parentId = array[5];
    }
    public ArticleCategoryModel(){

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public static final Parcelable.Creator<ArticleCategoryModel> CREATOR = new Parcelable.Creator<ArticleCategoryModel>() {
        @Override
        public ArticleCategoryModel createFromParcel(Parcel in) {
            return new ArticleCategoryModel(in);
        }

        @Override
        public ArticleCategoryModel[] newArray(int size) {
            return new ArticleCategoryModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.categoryName, this.categoryImage, this.categoryTag, this.objectId, this.categoryType,this.parentId});
    }


}
