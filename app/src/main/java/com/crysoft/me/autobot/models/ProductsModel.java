package com.crysoft.me.autobot.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Maxx on 7/19/2016.
 */
public class ProductsModel implements Parcelable {
    private String imageFile;
    private String productName, productPrice, objectId, description,productStatus,cartStatus,favStatus,quantity;

    public ProductsModel(Parcel in) {
        String[] array = new String[9];
        in.readStringArray(array);
        productName = array[0];
        productPrice = array[1];
        imageFile = array[2];
        objectId = array[3];
        description = array[4];
        productStatus = array[5];
        cartStatus = array[6];
        favStatus = array[7];
        quantity = array[8];
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }


    public ProductsModel() {

    }

    public static final Creator<ProductsModel> CREATOR = new Creator<ProductsModel>() {
        @Override
        public ProductsModel createFromParcel(Parcel in) {
            return new ProductsModel(in);
        }

        @Override
        public ProductsModel[] newArray(int size) {
            return new ProductsModel[size];
        }
    };

    public String getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(String cartStatus) {
        this.cartStatus = cartStatus;
    }

    public String getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(String favStatus) {
        this.favStatus = favStatus;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.productName,this.productPrice, this.imageFile,this.objectId, this.description,this.productStatus,this.cartStatus,this.favStatus,this.quantity});
    }

}
