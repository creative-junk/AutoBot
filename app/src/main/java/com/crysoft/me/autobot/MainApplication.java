package com.crysoft.me.autobot;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.crysoft.me.autobot.ParseModels.Mechanic;
import com.crysoft.me.autobot.ParseModels.PartsStore;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Maxx on 5/25/2016.
 */
public class MainApplication extends Application {
    // Debugging switch
    public static final boolean APP_DEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "Autobot";

    //Shared Preferences
    private static SharedPreferences preferences;
    //Key for Saving the Near me Preference
    private static String KEY_NEAR_ME="Near Me Key";
    //The default value for Near Me
    private static String NEAR_ME_ENABLED="No";

    private static final float DEFAULT_SEARCH_DISTANCE = 250.0f;
    public MainApplication(){

    }

    @Override
    public void onCreate() {
        super.onCreate();

		/*
		 * Parse credentials
		 */

        ParseObject.registerSubclass(Mechanic.class);
        ParseObject.registerSubclass(PartsStore.class);

        //Parse Initialization
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("autokit")
                .clientKey("r153ofth3n00b5")
                .server("http://autokit.herokuapp.com/parse/")
                .build()
        );
		/*
		 * This app lets an anonymous user create and save photos of meals
		 * they've eaten. An anonymous user is a user that can be created
		 * without a username and password but still has all of the same
		 * capabilities as any other ParseUser.
		 *
		 * After logging out, an anonymous user is abandoned, and its data is no
		 * longer accessible. In your own app, you can convert anonymous users
		 * to regular users so that data persists.
		 *
		 * Learn more about the ParseUser class:
		 * https://www.parse.com/docs/android_guide#users
		 *
        ParseUser.enableAutomaticUser();

		/*
		 * For more information on app security and Parse ACL:
		 * https://www.parse.com/docs/android_guide#security-recommendations
		 */
        ParseACL defaultACL = new ParseACL();

		/*
		 * If you would like all objects to be private by default, remove this
		 * line
		 */
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);

        //Facebook API essentials
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(getApplicationContext());

        AppEventsLogger.activateApp(this);



    }
    public static String getNearMeEnabled(){
        return preferences.getString(KEY_NEAR_ME, NEAR_ME_ENABLED);
    }

    public static void setKeyNearMe(String keyNearMe) {
        preferences.edit().putString(KEY_NEAR_ME, keyNearMe).commit();
    }
}
