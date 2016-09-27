package com.crysoft.me.autobot.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Maxx on 8/3/2016.
 */
public class ShippingModel implements Parcelable {
    private String name,timing,fee;

    protected ShippingModel(Parcel in) {
        String[] array = new String[3];
        in.readStringArray(array);
        name = array[0];
        timing = array[1];
        fee = array[2];
    }
    public ShippingModel(){

    }

    public static final Creator<ShippingModel> CREATOR = new Creator<ShippingModel>() {
        @Override
        public ShippingModel createFromParcel(Parcel in) {
            return new ShippingModel(in);
        }

        @Override
        public ShippingModel[] newArray(int size) {
            return new ShippingModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.name,this.timing, this.fee});
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

}
