package com.crysoft.me.autobot.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Maxx on 10/5/2016.
 */
@ParseClassName("PartsStore")
public class PartsStore extends ParseObject{
    public String getStoreName(){
        return getString("store_name");
    }
    public ParseFile getStoreLogo(){
        return getParseFile("store_logo");
    }
    public ParseGeoPoint getLocation(){
        return getParseGeoPoint("location");
    }
    public String getTelephone(){
        return getString("telephone");
    }
    public void setStoreName(String storeName){
        put("store_name",storeName);
    }
    public void setStoreLogo(ParseFile storeLogo){
        put("store_logo",storeLogo);
    }
    public void setLocation(ParseGeoPoint value){
        put("location",value);
    }
    public void getTelephone(String telephone){
        put("telephone",telephone);
    }
    public static ParseQuery<PartsStore> getQuery(){
        return ParseQuery.getQuery(PartsStore.class);
    }
}
