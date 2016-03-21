package com.allytours.view.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.utilities.UIUtility;
import com.allytours.utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.controller.Helpers.DBHelper;
import com.allytours.model.LocationModel;
import com.allytours.view.HomeActivity;
import com.allytours.widget.markerclusterer.MarkerCluster;
import com.allytours.widget.markerclusterer.MarkerClusterer;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.CustomRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapFragment extends Fragment implements View.OnClickListener{

    private GoogleMap googleMap;
    LinearLayout llContainer;
    TextView tvTour;
    TextView tvTourCount;
    TextView tvTourTitles;
    ImageView ivFlag;

    private Activity mActivity;

    List<LocationModel> marrLocations;
    ///for google map
    float mCurrentZoom;
    ArrayList<Marker> mMarkers;
    ArrayList<Marker> mClusterMarkers;
    ArrayList<MarkerCluster> markerClusters;

    String searchKey;

    public MapFragment() {
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initDB();

        initVariables();

        initUI(view);

        getLocation();
//        setupMap();

        return view;
    }

    /////////////////=========================================================================================
    private void initDB() {
        DBHelper.deleteAllLocation();
    }

    private void initVariables() {
        // initialize marker list
        mMarkers = new ArrayList<Marker>();
        mClusterMarkers = new ArrayList<Marker>();
        marrLocations = new ArrayList<>();
        searchKey = "";
    }
    private void initUI(View view) {
        llContainer = (LinearLayout)view.findViewById(R.id.ll_markerview);
        llContainer.setVisibility(View.INVISIBLE);
        llContainer.setOnClickListener(this);
        tvTour = (TextView)view.findViewById(R.id.tv_markerview_tour);
        tvTourCount = (TextView)view.findViewById(R.id.tv_markerview_tour_count);
        tvTourTitles = (TextView)view.findViewById(R.id.tv_markerview_tour_titles);
        ivFlag = (ImageView)view.findViewById(R.id.iv_markerview_flag);


    }

    @Override
    public void onResume() {
        super.onResume();

        // check google play services
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (isAvailable != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(isAvailable, mActivity, 1).show();
        } else {

        }

    }
    @Override
    public void onClick(View v) {
        if (!(v instanceof AutoCompleteTextView)) {
            UIUtility.hideSoftKeyboard(mActivity);
        }

        if (v == llContainer) {
            ((HomeActivity)mActivity).goToTourSearchPage();
        }
    }
    public void search(String query) {
        searchKey = query;
        if (searchKey.length() == 0) {
            createMarkers();
        } else {
            initMarkerwithSearchQuery();
        }
    }
    ///init marker with search query and refresh map
    private void initMarkerwithSearchQuery() {
        llContainer.setVisibility(View.INVISIBLE);

        getGoogleMap().clear();
        mMarkers.clear();
        mClusterMarkers.clear();
        // create  markers
        for (int i = 0; i < marrLocations.size(); i++) {

            // add to list
            if (marrLocations.get(i).getTours_region_country().toLowerCase().contains(searchKey)) {
                // create random position
//            LatLng markerPos = new LatLng(minLat
//                    + (Math.random() * (maxLat - minLat)), minLng
//                    + (Math.random() * (maxLng - minLng)));
                LatLng markerPos = new LatLng(Double.parseDouble(marrLocations.get(i).getLatitude()),
                        Double.parseDouble(marrLocations.get(i).getLongitude()));

                // create marker as non-visible
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(marrLocations.get(i).getCity())
                        .snippet(marrLocations.get(i).getTotal_tours())
                        .position(markerPos)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_25))
                        .visible(false);

                // create marker
                Marker marker = getGoogleMap().addMarker(markerOptions);
                mClusterMarkers.add(marker);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), Constant.minZoomout));
            }

        }
        createClusterMarkers();
//        redrawMap();
    }
    ///get location
    private void getLocation() {

        Utils.showProgress(mActivity);

        CustomRequest signinRequest = new CustomRequest(Request.Method.POST, API.GET_LOCATIOIN, null,
        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.hideProgress();
                        try {

                            String success = response.getString("success");
                            if (success.equals("true")) {

                                JSONArray jsonArray = response.getJSONArray("data");
                                int n = jsonArray.length();
                                for (int i = 0; i < n; i ++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String location_id = jsonObject.getString("locationId");
                                    String city = jsonObject.getString("city");
                                    String region = jsonObject.getString("region");
                                    String region_group = jsonObject.getString("regionGroup");
                                    String country = jsonObject.getString("country");
                                    String default_currency = jsonObject.getString("defaultCurrency");
                                    String totop = jsonObject.getString("totalOp");
                                    String tot_tours = jsonObject.getString("count");
                                    String tot_tours_amount = jsonObject.getString("totalToursAmt");
                                    String tour_count = jsonObject.getString("tourCnt");
                                    String tot_reviews = jsonObject.getString("totalReviews");
                                    String average_rating = jsonObject.getString("avgRating");
                                    String updated_date = jsonObject.getString("updated_at");
                                    String latitude = jsonObject.getString("latitude");
                                    String longitude = jsonObject.getString("longitude");

                                    LocationModel summallyModel = new LocationModel();

                                    summallyModel.setLocation_id(location_id);
                                    summallyModel.setCity(city);
                                    summallyModel.setRegion(region);
                                    summallyModel.setRegion_group(region_group);
                                    summallyModel.setCountry(country);
                                    summallyModel.setDefault_currency(default_currency);
                                    summallyModel.setTours_region_country(city + " " + region + " " + country);
                                    summallyModel.setTotal_op(totop);
                                    summallyModel.setTour_count(tour_count);
                                    summallyModel.setTotal_tours(tot_tours);
                                    summallyModel.setTotal_tour_amount(tot_tours_amount);
                                    summallyModel.setTotal_reviews(tot_reviews);
                                    summallyModel.setAverage_rating(average_rating);
                                    summallyModel.setUpdated_date(updated_date);
                                    summallyModel.setLatitude(latitude);
                                    summallyModel.setLongitude(longitude);

                                    DBHelper.insertLocation(summallyModel);

//                                    marrLocations.add(summallyModel);
                                }
                                setupMap();
                            } else {
                                String reason = response.getString("reason");
                                if (reason.equals("401")) {
                                    Utils.showOKDialog(mActivity, "Email is unregistered");
                                } else if (reason.equals("402")) {
                                    Utils.showOKDialog(mActivity, "Password incorrect");
                                }
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.hideProgress();
                        Toast.makeText(mActivity, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(mActivity);
        requestQueue.add(signinRequest);

    }
    ///init map
    private void setupMap() {
            if (getGoogleMap() != null) {
//                mCurrentZoom = getGoogleMap().getCameraPosition().zoom;
                mCurrentZoom = Constant.minZoomout;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50, -90), Constant.minZoomout));
//                createMarkers();
//                createRandomMarkers(100);

                getGoogleMap().setOnCameraChangeListener(
                        new GoogleMap.OnCameraChangeListener() {
                            @Override
                            public void onCameraChange(CameraPosition newPosition) {
                                // is clustered?
                                if (mCurrentZoom != newPosition.zoom) {
                                    if (newPosition.zoom < Constant.minZoomout ) {
                                        getGoogleMap().animateCamera(CameraUpdateFactory.zoomTo(Constant.minZoomout));
                                    }else if (newPosition.zoom > Constant.maxZoomin) {
                                        getGoogleMap().animateCamera(CameraUpdateFactory.zoomTo(Constant.maxZoomin));
                                    }
                                    else {
                                        // create cluster markers for new position
                                        if (searchKey.length() == 0) {
                                            recreateClusterMarkers();
                                        }

//                                    // redraw map
//                                    redrawMap();
                                        mCurrentZoom = newPosition.zoom;
                                    }

                                }

                            }
                        });
            }
    }

    private void createMarkers() {
        // clear map
        getGoogleMap().clear();
        // clear marker lists
        mMarkers.clear();
        mClusterMarkers.clear();

        // get projection area
        Projection projection = getGoogleMap().getProjection();
        LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
        // set min/max for lat/lng
//
//
        double minLat = 20;
        double maxLat = 85;
        double minLng = -50;
        double maxLng = -130;

        marrLocations = DBHelper.getAllLocation();
        // create random markers
        for (int i = 0; i < marrLocations.size(); i++) {
            // create random position
//            LatLng markerPos = new LatLng(minLat
//                    + (Math.random() * (maxLat - minLat)), minLng
//                    + (Math.random() * (maxLng - minLng)));
            LatLng markerPos = new LatLng(Double.parseDouble(marrLocations.get(i).getLatitude()),
                    Double.parseDouble(marrLocations.get(i).getLongitude()));

            // create marker as non-visible
            MarkerOptions markerOptions = new MarkerOptions()
                    .title(marrLocations.get(i).getCity())
                    .snippet(marrLocations.get(i).getTotal_tours())
                    .position(markerPos)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_25))
                    .visible(false);

            // create marker
            Marker marker = getGoogleMap().addMarker(markerOptions);
            // add to list
            mMarkers.add(marker);


        }
        createClusterMarkers();
    }
    ///test method
    private void createRandomMarkers(int numberOfMarkers) {
        // clear map
        getGoogleMap().clear();
        // clear marker lists
        mMarkers.clear();
        mClusterMarkers.clear();

        // get projection area
        Projection projection = getGoogleMap().getProjection();
        LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
        // set min/max for lat/lng
//        double minLat = bounds.southwest.latitude;
//        double maxLat = bounds.northeast.latitude;
//        double minLng = bounds.southwest.longitude;
//        double maxLng = bounds.northeast.longitude;
//
        double minLat = 20;
        double maxLat = 85;
        double minLng = -50;
        double maxLng = -130;

        // create random markers
        for (int i = 0; i < numberOfMarkers; i++) {
            // create random position
            LatLng markerPos = new LatLng(minLat
                    + (Math.random() * (maxLat - minLat)), minLng
                    + (Math.random() * (maxLng - minLng)));

            // create marker as non-visible
            MarkerOptions markerOptions = new MarkerOptions()
                    .title("Tour " + String.valueOf(i))
                    .snippet(String.valueOf(i))
                    .position(markerPos)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_25))
                    .visible(false);
            // create marker
            Marker marker = getGoogleMap().addMarker(markerOptions);
            // add to list
            mMarkers.add(marker);
        }
        createClusterMarkers();
    }

    private void createClusterMarkers() {

        if (mClusterMarkers.size() == 0) {
            // set cluster parameters
            int gridSize = 100;
            boolean averageCenter = false;
            // create clusters
            Marker[] markers = mMarkers.toArray(new Marker[mMarkers.size()]);
            markerClusters = new MarkerClusterer(
                    getGoogleMap(), markers, gridSize, averageCenter)
                    .createMarkerClusters();
            // create cluster markers
            for (MarkerCluster cluster : markerClusters) {
                int markerCount = cluster.markers.size();
                if (markerCount == 1) {
                    mClusterMarkers.add(cluster.markers.get(0));
                } else {
                    // get marker view and set text
                    View markerView = mActivity.getLayoutInflater().inflate(
                            R.layout.cluster_marker_view, null);
                    ((TextView) markerView.findViewById(R.id.marker_count))
                            .setText(String.valueOf(markerCount));

                    // create cluster marker
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(cluster.center)
                            .icon(BitmapDescriptorFactory
                                    .fromBitmap(createDrawableFromView(markerView)))
                            .visible(false);
                    //for test===
                    markerOptions.snippet(getToTours(cluster));
                    markerOptions.title(getCities(cluster));

                    ///====
                    Marker clusterMarker = getGoogleMap().addMarker(
                            markerOptions);

                    // add to list
                    mClusterMarkers.add(clusterMarker);
                }
            }
        }
        // redraw map
        redrawMap();
    }
    private String getCities(MarkerCluster cluster) {
        String city = "";
        ArrayList<Marker> arrayList = cluster.markers;
        if (arrayList.size() > 0) {
            for(int i = 0; i < arrayList.size(); i ++ ) {
                city = city + ", " + arrayList.get(i).getTitle();
            }
            city = city.substring(2, city.length());
        }
        return city;
    }
    private String getToTours(MarkerCluster cluster) {
        int totour = 0;
        ArrayList<Marker> arrayList = cluster.markers;
        if (arrayList.size() > 0) {
            for(int i = 0; i < arrayList.size(); i ++ ) {
                totour = totour + Integer.parseInt( arrayList.get(i).getSnippet());
            }

        }
        return String.valueOf(totour);
    }

    private void recreateClusterMarkers() {
        // remove cluster markers from map
        for (Marker marker : mClusterMarkers) {
            marker.remove();
        }
        // clear cluster markers list
        mClusterMarkers.clear();
        // create mew cluster markers
//        createClusterMarkers();
        createMarkers();

    }

    private void redrawMap() {

        // hide all markers
        for (Marker marker : mMarkers) {
            marker.setVisible(false);
        }
        // show markers
        for (Marker marker : mClusterMarkers) {
            marker.setVisible(true);
        }


//        if (true) {
//
//        } else {
//            for (Marker marker : mMarkers) {
//                marker.setVisible(true);
//            }
//        }
    }

    private GoogleMap getGoogleMap() {
        if (googleMap == null) {
            SupportMapFragment mapFragment =  (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            googleMap = mapFragment.getMap();
//            googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

//                    marker.setVisible(false); //disable badge
//                    marker.remove();
                    marker.hideInfoWindow();
                    UIUtility.hideSoftKeyboard(mActivity);

                    llContainer.setVisibility(View.VISIBLE);
                    if (getCountryName(marker.getPosition()).equals("United States")) {
                        ivFlag.setImageDrawable(getResources().getDrawable(R.drawable.america));
                    } else if (getCountryName(marker.getPosition()).equals("Canada")){
                        ivFlag.setImageDrawable(getResources().getDrawable(R.drawable.canada));
                    }

//                    llContainer.setBackground(getResources().getDrawable(R.drawable.custom_info_bubble));
                    tvTourTitles.setText(marker.getTitle());
                    tvTourCount.setText(marker.getSnippet());

                    String strCity = marker.getTitle();
                    if (strCity.length() > 40) {
                        strCity = strCity.substring(0, 37) + "...";
                        tvTourTitles.setText(strCity);
                    }

                    getArrayOfSelectedCitiesId(marker.getTitle());
                    return true;
                }
            });
        }
        return googleMap;
    }
    private void getArrayOfSelectedCitiesId(String strCityName) {
        String[] strings = strCityName.split(", ");
        String strCityID = "";
        List<LocationModel> arrayList = DBHelper.getAllLocation();
        for( int k = 0; k < strings.length; k ++) {
            for (int i = 0; i < arrayList.size(); i ++) {
                if (strings[k].equals(arrayList.get(i).getCity())) {
                    strCityID = strCityID + "," + (arrayList.get(i).getLocation_id());
                    break;
                }
            }
        }
        if (strCityID.length() > 0) {
            strCityID = strCityID.substring(1, strCityID.length());
            ((HomeActivity)mActivity).strCityIDs = strCityID;
        }

    }

    public static Bitmap createDrawableFromView(View view) {

        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }



    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;


        CustomInfoWindowAdapter() {
            mWindow = mActivity.getLayoutInflater().inflate(R.layout.custom_marker_view, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {

            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }


        private void render(Marker marker, View view) {
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
//            if (marker.equals(mBrisbane)) {
//                badge = R.drawable.badge_qld;
//            } else if (marker.equals(mAdelaide)) {
//                badge = R.drawable.badge_sa;
//            }
            LinearLayout llContainer = (LinearLayout)view.findViewById(R.id.ll_markerview);
            TextView tvTour = (TextView)view.findViewById(R.id.tv_markerview_tour);
            TextView tvTourCount = (TextView)view.findViewById(R.id.tv_markerview_tour_count);
            TextView tvTourTitles = (TextView)view.findViewById(R.id.tv_markerview_tour_titles);
//            TextView tvTourOperators = (TextView)view.findViewById(R.id.tv_markerview_tour_operators);

            ImageView ivFlag = (ImageView)view.findViewById(R.id.iv_markerview_flag);
            if (getCountryName(marker.getPosition()).equals("United States")) {
                ivFlag.setImageDrawable(getResources().getDrawable(R.drawable.america));
            } else if (getCountryName(marker.getPosition()).equals("Canada")){
                ivFlag.setImageDrawable(getResources().getDrawable(R.drawable.canada));
            }

            llContainer.setBackground(getResources().getDrawable(R.drawable.custom_info_bubble));
            tvTourTitles.setText(marker.getTitle());
            tvTourCount.setText(marker.getSnippet());
        }
    }
    private String getCountryName(LatLng latLng) {
        String countryName = "";
        ////
        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
//                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                            sb.append(address.getAddressLine(i)).append("\n");
//                        }
//                        sb.append(address.getLocality());
//                        sb.append(address.getPostalCode()).append("\n");
//                        sb.append(address.getCountryName());
//                        searchKey = sb.toString();
//                        searchKey = address.getAddressLine(address.getMaxAddressLineIndex() - 1);
                if (address.getAdminArea() != null) {
                    countryName = address.getCountryName();/////Address[addressLines=[0:"Allen, OK 74825",1:"USA"],feature=74825,admin=Oklahoma,sub-admin=null,locality=Allen,thoroughfare=null,postalCode=74825,countryCode=US,countryName=United States,hasLatitude=true,latitude=34.8031254,hasLongitude=true,longitude=-96.429036,phone=null,url=null,extras=null]
//                            searchKey = searchKey.replace(" ", "_");

                }

            }
        } catch (IOException e) {
//                    Log.e(TAG, "Unable connect to Geocoder", e);
            e.printStackTrace();
        }
        return countryName;
    }
}
