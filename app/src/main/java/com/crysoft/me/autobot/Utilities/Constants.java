package com.crysoft.me.autobot.Utilities;

/**
 * Created by Maxx on 9/9/2016.
 */
public class Constants {
    /* Request code to be sent to play services in the OnActivity */
    public static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 100;
    /* Location Update Parameters */
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    public static final int FAST_CEILING_IN_SECONDS = 1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    /* A ceiling for updates for when the APP is visible */
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

    /*Constants for handling the location */
    public static final float METERS_PER_FEET = 0.3048f;
    public static final int METERS_PER_KILOMETER = 1000;
    public static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;
    public static final float OFFSET_CALCULATION_ACCURACY = 0.01f;
    public static final int MAX_POST_SEARCH_RESULTS = 20;
    public static final int MAX_POST_DISTANCE = 100;

    public static final String GOOGLE_MAP_API_KEY = "AIzaSyAtLlTJfzOWgw1O_rQj8oQkRCcotwrbkUY";
}
