package com.crysoft.me.autobot.ParseModels;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Maxx on 10/4/2016.
 */
@ParseClassName("Mechanic")
public class Mechanic extends ParseObject {
    public String getFirstName(){
        return getString("first_name");
    }
    public String getLastName(){
        return getString("last_name");
    }
    public ParseFile getProfilePic(){
        return getParseFile("profile_img");
    }
    public ParseGeoPoint getLocation(){
        return getParseGeoPoint("location");
    }
    public String getTelephone(){
        return getString("telephone");
    }
    public void setFirstName(String firstName){
        put("first_name",firstName);
    }
    public void setLastName(String lastName){
        put("last_name",lastName);
    }
    public void setProfilePic(ParseFile profilePic){
        put("profile_img",profilePic);
    }
    public void setLocation(ParseGeoPoint value){
        put("location",value);
    }
    public void getTelephone(String telephone){
        put("telephone",telephone);
    }
    public static ParseQuery<Mechanic> getQuery(){
        return ParseQuery.getQuery(Mechanic.class);
    }
}
