package com.crysoft.me.autobot.helpers;

/**
 * Created by Maxx on 7/20/2016.
 * This Class Handles App wide Constants
 */
public class Constants {
    public static final int PRODUCT_STATUS_AVAILABLE = 1;
    public static final int PRODUCT_STATUS_OUTOFSTOCK = 2;
    public static final int PRODUCT_STATUS_REMOVED = 3;

    public static final int CART_STATUS_ACTIVE = 1;
    public static final int CART_STATUS_INVALIDATED = 2;
    public static final int CART_STATUS_NEEDS_REFRESH = 3;

    public static final int FAV_STATUS_ACTIVE = 1;
    public static final int FAV_STATUS_INVALIDATED = 2;
    public static final int FAV_STATUS_NEEDS_REFRESH = 3;

    public static final String ROOT_FOLDER_NAME ="Autokit";
    public static final String FOLDER_IMAGE ="Autokit Images";

    public static final String CURRENCY = "Ksh ";

    public static int INCREASE_QTY = 1;
    public static int DECREASE_QTY=2;

    /*
 * Define a request code to send to Google Play services This code is returned in
 * Activity.onActivityResult
 */
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*
     * Constants for location update parameters
     */
    // Milliseconds per second
    public static final int MILLISECONDS_PER_SECOND = 1000;

    // The update interval
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // A fast interval ceiling
    public static final int FAST_CEILING_IN_SECONDS = 1;

    // Update interval in milliseconds
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;

    // A fast ceiling of update intervals, used when the app is visible
    public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
            * FAST_CEILING_IN_SECONDS;

    /*
     * Constants for handling location results
     */
    // Conversion from feet to meters
    public static final float METERS_PER_FEET = 0.3048f;

    // Conversion from kilometers to meters
    public static final int METERS_PER_KILOMETER = 1000;

    // Initial offset for calculating the map bounds
    public static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

    // Accuracy for calculating the map bounds
    public static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

    // Maximum results returned from a Parse query
    public static final int MAX_SEARCH_RESULTS = 20;

    // Maximum post search radius for map in kilometers
    public static final int MAX_SEARCH_DISTANCE = 100;

}
