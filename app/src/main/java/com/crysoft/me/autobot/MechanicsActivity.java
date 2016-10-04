package com.crysoft.me.autobot;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.crysoft.me.autobot.ParseModels.Mechanic;
import com.crysoft.me.autobot.helpers.Constants;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MechanicsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    // Represents the circle around a map
    private Circle mapCircle;

    private float radius;
    private float lastRadius;

    //Map change processing helpers
    private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();
    private int mostRecentMapUpdate;
    private boolean hasSetUpInitialLocation;
    private String selectedMechanicObjectId;
    private Location lastLocation;
    private Location currentLocation;

    //Request Location Services
    private LocationRequest locationRequest;

    // Store the current instantiation of the location client in this object
    private GoogleApiClient locationClient;

    //Parse Query Adapter
    private ParseQueryAdapter<Mechanic> mechanicsQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radius = 1250.0f;
        lastRadius = radius;

        setContentView(R.layout.activity_mechanics);
        //Setup the Map Fragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mechanicsMap);
        mapFragment.getMapAsync(this);

        //Create a new Global location parameters object
        locationRequest = LocationRequest.create();

        //Setup update interval
        locationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);

        //Use high accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //set the interval ceiling to one minute
        locationRequest.setFastestInterval(Constants.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        //Create a New location Client, using enclosing class to handle callbacks
        locationClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Setup the Query
        ParseQueryAdapter.QueryFactory<Mechanic> factory =
                new ParseQueryAdapter.QueryFactory<Mechanic>() {
                    @Override
                    public ParseQuery<Mechanic> create() {
                        Location myLocation = (currentLocation == null) ? lastLocation : currentLocation;
                        ParseQuery<Mechanic> query = Mechanic.getQuery();
                        query.orderByDescending("first_name");
                        query.whereWithinKilometers("location", geoPointFromMyLocation(myLocation), radius * Constants.METERS_PER_FEET / Constants.METERS_PER_KILOMETER);
                        query.setLimit(Constants.MAX_SEARCH_RESULTS);
                        return query;
                    }

                };
        //Setup the query Adapter
        mechanicsQueryAdapter = new ParseQueryAdapter<Mechanic>(this, factory) {
            @Override
            public View getItemView(Mechanic mechanic, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.mechanic_item, null);
                }
                TextView firstName = (TextView) view.findViewById(R.id.first_name);
                TextView lastName = (TextView) view.findViewById(R.id.last_name);

                firstName.setText(mechanic.getFirstName());
                lastName.setText(mechanic.getLastName());
                return view;
            }
        };
        //Disable automatic loading when adapter is attached to view
        mechanicsQueryAdapter.setAutoload(false);
        //Disable pagination,we already have limits set
        mechanicsQueryAdapter.setPaginationEnabled(false);
        //Attach the Query Adapter to the listview
        ListView mechanicsList = (ListView) findViewById(R.id.mechanics_listview);
        mechanicsList.setAdapter(mechanicsQueryAdapter);

        //handle user selection
        mechanicsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Mechanic item = mechanicsQueryAdapter.getItem(position);
                selectedMechanicObjectId = item.getObjectId();
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLng(new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude())),
                        new GoogleMap.CancelableCallback() {

                            @Override
                            public void onFinish() {
                                Marker marker = mapMarkers.get(item.getObjectId());
                                if (marker != null) {
                                    marker.showInfoWindow();
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        }
                );
                Marker marker = mapMarkers.get(item.getObjectId());
                if (marker != null) {
                    marker.showInfoWindow();
                }
            }
        });


    }

    //Called when the activity is no longer in view. Stop Updates and Disconnect
    @Override
    protected void onStop() {
        //If the Client is connected
        if (locationClient.isConnected()) {
            stopPeriodicUpdates();
        }
        //After Disconnect, consider the client dead
        locationClient.disconnect();
        super.onStop();
    }

    //Called When the Activity becomes visible
    @Override
    protected void onStart() {
        super.onStart();
        //Connect the client
        locationClient.connect();
    }

    //Called when the Activity is resumed. Updates the View.
    @Override
    protected void onResume() {
        super.onResume();
        //Check the Last Known Location to show cached data if available
        if (lastLocation != null) {
            LatLng myLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            //If the Search Radius has been modified in the preferences move the Map Bounds
            if (lastRadius != radius) {
                updateZoom(myLatLng);
            }
            //Update the Circle on the Map
            updateCircle(myLatLng);
        }
        //Save the current radius
        lastRadius = radius;
        //Query for the latest data to update views
        doMapQuery();
        doListQuery();
    }
    /*
   * Handle results returned to this Activity by other Activities started with
   * startActivityForResult(). In particular, the method onConnectionFailed() in
   * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to start
   * an Activity that handles Google Play services problems. The result of this call returns here,
   * to onActivityResult.
   */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Choose what to do based on the requestCode
        switch (requestCode) {
            // If the request code matches the code sent in onConnectionFailed
            case Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch (resultCode) {
                    //If Google Services resolved the problem
                    case Activity.RESULT_OK:
                        if (MainApplication.APP_DEBUG) {
                            // Log the result
                            Log.d(MainApplication.APPTAG, "Connected to Google Play services");
                        }
                        break;
                    //If any other result was returned by Google Play Services
                    default:
                        if (MainApplication.APP_DEBUG) {
                            // Log the result
                            Log.d(MainApplication.APPTAG, "Could not connect to Google Play services");
                        }
                        break;
                }

                //Any other Result code
            default:
                if (MainApplication.APP_DEBUG) {
                    // Report that this Activity received an unknown requestCode
                    Log.d(MainApplication.APPTAG, "Unknown request code received for the activity");
                }
                break;
        }

    }

    /**
     * Verify that Google Play Services is available before making the Request
     *
     * @return true if Google Play Services is available, false otherwise
     */
    private boolean servicesConnected() {
        //Check that Google Play Services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        //if Google Play Services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            if (MainApplication.APP_DEBUG) {
                // In debug mode, log the status
                Log.d(MainApplication.APPTAG, "Google play services available");
            }
            //Continue
            return true;
        } else {
            //For some reason Play Services were not available
            //Display an Error Dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
                errorDialogFragment.setDialog(dialog);
                errorDialogFragment.show(getSupportFragmentManager(), MainApplication.APPTAG);
            }
            return false;
        }
    }

    /**
     *  Set Up Query for the List View
     *
     */
    private void doListQuery() {
        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
        //if location info is available load data
        if (myLoc != null) {
            //Refresh list with new data based on Lcation Updates
            mechanicsQueryAdapter.loadObjects();
        }
    }

    /**
     *  Set up Query to update Map
     *
     */
    private void doMapQuery() {
        final int myUpdateNumber = ++mostRecentMapUpdate;
        Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
        //if location isnt available clean up existing markers
        if (myLoc == null) {
            cleanUpMarkers(new HashSet<String>());
            return;
        }
        final ParseGeoPoint myPoint = geoPointFromMyLocation(myLoc);
        //Create the Map Parse Query
        ParseQuery<Mechanic> mapQuery = Mechanic.getQuery();
        //set up additional query filters
        mapQuery.whereWithinKilometers("location", myPoint, Constants.MAX_SEARCH_DISTANCE);
        mapQuery.setLimit(Constants.MAX_SEARCH_RESULTS);
        //Kickoff query in the background
        mapQuery.findInBackground(new FindCallback<Mechanic>() {
            @Override
            public void done(List<Mechanic> objects, ParseException e) {
                if (e != null) {
                    if (MainApplication.APP_DEBUG) {
                        Log.d(MainApplication.APPTAG, "An error occurred while querying for map posts.", e);
                    }
                    return;
                }
                /*
                * Make sure we're processing results from
                * the most recent update, in case there
                * may be more than one in progress.
                */
                if (myUpdateNumber != mostRecentMapUpdate) {
                    return;
                }
                //items to show on the Map
                Set<String> toKeep = new HashSet<String>();
                //Loop through the results of the search
                for (Mechanic mechanic : objects) {
                    //Add this item to the list of map pins to keep
                    toKeep.add(mechanic.getObjectId());
                    //Check for an existing marker for this item
                    Marker oldMarker = mapMarkers.get(mechanic.getObjectId());
                    //Setup the map Marker's location
                    MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(mechanic.getLocation().getLatitude(), mechanic.getLocation().getLongitude()));
                    //Setup the Marker Options if it's within the Search radius
                    if (mechanic.getLocation().distanceInKilometersTo(myPoint) > radius * Constants.METERS_PER_FEET / Constants.METERS_PER_KILOMETER) {
                        //Check for a =n existing out of range marker
                        if (oldMarker != null) {
                            if (oldMarker.getSnippet() == null) {
                                //Out of range Marker already exists, skip adding it
                                continue;
                            } else {
                                //Marker out of range, needs to be refreshed
                                oldMarker.remove();
                            }
                        }
                        //Display a red Marker with a predertermined title and no snippet
                        markerOptions = markerOptions.title(getResources().getString(R.string.out_of_range)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    } else {
                        //Check for existing in range Marker
                        if (oldMarker != null) {
                            if (oldMarker.getSnippet() != null) {
                                //In range Marker alreday exists, skip adding it
                                continue;
                            } else {
                                //Marker is now in range, needs to be refreshed
                                oldMarker.remove();
                            }

                        }
                        //Display a green Marker with the Mechanics Name
                        markerOptions = markerOptions.title(mechanic.getLastName()).snippet(mechanic.getFirstName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                    }
                    //Add a new Marker
                    Marker marker = mMap.addMarker(markerOptions);
                    mapMarkers.put(mechanic.getObjectId(), marker);
                    if (mechanic.getObjectId().equals(selectedMechanicObjectId)) {
                        marker.showInfoWindow();
                        selectedMechanicObjectId = null;
                    }
                }
                cleanUpMarkers(toKeep);

            }

        });
    }

    /** Helper Method to clean up Markers
     *
     */
    private void cleanUpMarkers(Set<String> markersToKeep) {
        for (String objId : new HashSet<String>(mapMarkers.keySet())) {
            if (!markersToKeep.contains(objId)) {
                Marker marker = mapMarkers.get(objId);
                marker.remove();
                mapMarkers.get(objId).remove();
                mapMarkers.remove(objId);
            }
        }
    }


    /**
     * Helper Method to get the Parse GeoPoint representation of a Location
     * @param myLocation
     * @return
     */

    private ParseGeoPoint geoPointFromMyLocation(Location myLocation) {
        return new ParseGeoPoint(myLocation.getLatitude(), myLocation.getLongitude());
    }

    /**
     *
     * Displays a Circle on the Map representing the search radius
     */
    private void updateCircle(LatLng myLatLng) {
        if (mapCircle == null) {
            mapCircle = mMap.addCircle(new CircleOptions().center(myLatLng).radius(radius * Constants.METERS_PER_FEET));
            int baseColor = Color.DKGRAY;
            mapCircle.setStrokeColor(baseColor);
            mapCircle.setStrokeWidth(2);
            mapCircle.setFillColor(Color.argb(50, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor)));

        }
        mapCircle.setCenter(myLatLng);
        mapCircle.setRadius(radius * Constants.METERS_PER_FEET);//Cnvert radius in feet to meters
    }

    /**
     *  Zooms the Map to the Area of Interest based on search radius
     * @param myLatLng
     */
    private void updateZoom(LatLng myLatLng) {
        //get the Bounds to Zoom
        LatLngBounds bounds = calculateBoundsWithCenter(myLatLng);
        //Zoom to the Given Bounds
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5));
    }

    /**
     * Calculate the offset for the bounds used in Map Zooming
     * @param mylatLng
     * @param bLatOffset
     * @return double
     */
    private double calculateLatLngOffset(LatLng mylatLng, boolean bLatOffset) {
        //the return offset initialised as the default difference
        double latLngOffset = Constants.OFFSET_CALCULATION_INIT_DIFF;
        //Set up the desired distance in meters
        float desiredOffsetMeters = radius * Constants.METERS_PER_FEET;
        //Setup Variables for distance calculation
        float[] distance = new float[1];
        boolean foundMax = false;
        double foundMinDiff = 0;
        //Loop through and get offset
        do {
            //calculate the distance between the Point of interest and the current offset in the latitute or longitude direction
            if (bLatOffset) {
                Location.distanceBetween(mylatLng.latitude, mylatLng.longitude, mylatLng.latitude + latLngOffset, mylatLng.longitude, distance);

            } else {
                Location.distanceBetween(mylatLng.latitude, mylatLng.longitude, mylatLng.latitude, mylatLng.longitude + latLngOffset, distance);
            }
            //Compare the current distance with the desired one
            float distanceDiff = distance[0] - desiredOffsetMeters;
            if (distanceDiff < 0) {
                //Need to catch up with the desired distance
                if (!foundMax) {
                    foundMinDiff = latLngOffset;
                    //Increase the calculated offset
                    latLngOffset *= 2;
                } else {
                    double tmp = latLngOffset;
                    //increase the required offset but slower
                    latLngOffset += (latLngOffset - foundMinDiff) / 2;
                    foundMinDiff = tmp;
                }
            } else {
                //Overshot the required distance
                //Decrease the calculated offset
                latLngOffset -= (latLngOffset - foundMinDiff) / 2;
                foundMax = true;
            }
        }
        while (Math.abs(distance[0] - desiredOffsetMeters) > Constants.OFFSET_CALCULATION_ACCURACY);

        return latLngOffset;
    }

    /**
     * Calculate the Bounds for Map Zooming
     * @param myLatLng
     * @return LatLngBounds
     */
    private LatLngBounds calculateBoundsWithCenter(LatLng myLatLng) {
        //Create a bounds
        LatLngBounds.Builder builder = LatLngBounds.builder();

        //Calculate the East/West points that should be included in the Bounds
        double lngDifference = calculateLatLngOffset(myLatLng, false);
        LatLng east = new LatLng(myLatLng.latitude, myLatLng.longitude + lngDifference);
        builder.include(east);
        LatLng west = new LatLng(myLatLng.latitude, myLatLng.longitude - lngDifference);
        builder.include(west);

        //calculate the North/South points that should be included in the bounds
        double latDifference = calculateLatLngOffset(myLatLng, false);
        LatLng north = new LatLng(myLatLng.latitude + latDifference, myLatLng.longitude);
        builder.include(north);
        LatLng south = new LatLng(myLatLng.latitude - latDifference, myLatLng.longitude);
        builder.include(south);

        return builder.build();


    }

    /*
       * Called by Location Services when the request to connect the client finishes successfully. At
       * this point, you can request the current location or start periodic updates
       */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (MainApplication.APP_DEBUG) {
            Log.d("Connected location", MainApplication.APPTAG);
        }
        currentLocation = getLocation();
        startPeriodicUpdates();
    }

    /*
   * Called by Location Services if the connection to the location client drops because of an error.
   */
    public void onDisconnected() {
        if (MainApplication.APP_DEBUG) {
            Log.d("Disconnected location", MainApplication.APPTAG);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(MainApplication.APPTAG, "GoogleApiClient connection has been suspend");
    }

    /*
   * Called by Location Services if the attempt to Location Services fails.
   */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Google Play services can resolve some errors it detects. If the error has a resolution, try
        // sending an Intent to start a Google Play services activity that can resolve error.
        if (connectionResult.hasResolution()) {
            try {
                //start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                if (MainApplication.APP_DEBUG) {
                    //Thrown if Google Play Services cancelled the original Pending Intent
                    Log.d(MainApplication.APPTAG, "An error occurred when connecting to location services.", e);
                }
            }
        } else {
            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /**
     * Report Location Changes to the UI
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        if (lastLocation != null && geoPointFromMyLocation(location).distanceInKilometersTo(geoPointFromMyLocation(lastLocation)) < 0.01) {
            //Location hasn't changed, ignore
            return;
        }
        lastLocation = location;
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!hasSetUpInitialLocation) {
            //Zoom to the current Location
            updateZoom(myLatLng);
            hasSetUpInitialLocation = true;
        }
        //Update Map Circle radius indicator
        updateCircle(myLatLng);
        doMapQuery();
        doListQuery();
    }

    /**
     * In response to a request to start updates, send  a request to Location Services
     */
    private void startPeriodicUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(locationClient, locationRequest, this);
    }

    /**
     * in response to a request to stop updates, send a request to Location Services
     */
    private void stopPeriodicUpdates() {
        locationClient.disconnect();
    }

    /**
     * Get Current Location
     */
    private Location getLocation() {
        //If Google Play Service is available
        if (servicesConnected()) {
            //get Current Location
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return TODO;
            }
            return LocationServices.FusedLocationApi.getLastLocation(locationClient);
        } else {
            return null;
        }
    }

    /**
     * Show a Dialog for the Code returned by Play Services
     *
     * @param errorCode
     */
    private void showErrorDialog(int errorCode) {
        //get the error dialog from Play Services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
        //Check if Play services ca =n actually provide the Dialog Box
        if (errorDialog != null) {
            //Create a new Dialog Fragment in which to show the error
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            //set the Dialog in the Dialog Fragment
            errorFragment.setDialog(errorDialog);
            //Show the error Dialog in the Dialog Fragment
            errorFragment.show(getSupportFragmentManager(), MainApplication.APPTAG);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //enable the current location blue dot
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        //Setup Camera Change Handler
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //When Camera Changes, update the Query
                doMapQuery();
            }
        });


    }

    /***
     *
     * Dialog Fragment Class to show the error dialog generated in the showErrorDialog
     */
    public static class ErrorDialogFragment extends DialogFragment {
        //Global field to contain the error dialog
        private Dialog mDialog;
        /** Default constructor.sets the dialog filed to null.
         *
         */
        public ErrorDialogFragment(){
            super();
            mDialog = null;
        }
        /**
         * Set the Dialog to display
         * @param dialog an Error Dialog
         */
        public void setDialog(Dialog dialog){
            mDialog = dialog;
        }
        /**
         * This method must return a Dialog to the Dialog Fragment
         *
         */
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

}
