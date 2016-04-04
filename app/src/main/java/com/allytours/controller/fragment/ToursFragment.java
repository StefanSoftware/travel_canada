package com.allytours.controller.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.controller.Helpers.DBHelper;
import com.allytours.model.API;
import com.allytours.model.LocationModel;
import com.allytours.model.TourModel;
import com.allytours.utilities.TimeUtility;
import com.allytours.utilities.Utils;

import com.allytours.controller.SearchTourActivity;
import com.allytours.controller.adapter.TourAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.CustomRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToursFragment extends Fragment implements View.OnClickListener{

    private Button btnPopular, btnSight, btnAdventure, btnRomantic;
    private ListView lvHome;
    private PullToRefreshListView mPullRefreshHomeListView;
    private static TourAdapter tourAdapter;
//    private ImageButton ivBack;

    static String searchQuery;
    private Context mContext;
    static boolean isLast;
    static ArrayList<TourModel> arrTourModel;
    static ArrayList<TourModel> arrBufferTourModel;
    static ArrayList<TourModel> arrBufferTourModel2;
    static int offset;
    static int currentState;
    int myOffset;

    public ToursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_tours, container, false);

        initVariable();
        initUI(view);
        fetch_tours();
        return  view;
    }
    private void initVariable() {
        mContext = getActivity();
        isLast = false;
        offset = 0;
        arrTourModel = new ArrayList<>();
        arrBufferTourModel = new ArrayList<>();
        arrBufferTourModel2 = new ArrayList<>();
        searchQuery = "";
        currentState = 0;
        String offset1 = Utils.getFromPreference(mContext, "rawOffset");
        myOffset = Integer.parseInt(offset1) / 3600;
    }
    private void initUI(View view) {
        btnAdventure = (Button)view.findViewById(R.id.btn_tours_adventure);
        btnSight = (Button)view.findViewById(R.id.btn_tours_sightseeing);
        btnPopular = (Button)view.findViewById(R.id.btn_tours_popular);
        btnRomantic = (Button)view.findViewById(R.id.btn_tours_romantic);

        btnAdventure.setOnClickListener(this);
        btnSight.setOnClickListener(this);
        btnPopular.setOnClickListener(this);
        btnRomantic.setOnClickListener(this);

//        ivBack = (ImageButton)view.findViewById(R.id.ib_tours_back);

        ///create listview
        mPullRefreshHomeListView = (PullToRefreshListView)view.findViewById(R.id.lv_tour_list);
//        mPullRefreshHomeListView.setVisibility(View.GONE);
        mPullRefreshHomeListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                if (!isLast) {
//                    defaluteFetchTrip(currentCategory, GlobalVariable.getInstance().currentCountryName);
                    reDrawListView();
                }
                mPullRefreshHomeListView.onRefreshComplete();

            }
        });
        lvHome = mPullRefreshHomeListView.getRefreshableView();
        tourAdapter = new TourAdapter(mContext, arrBufferTourModel2);
        lvHome.setAdapter(tourAdapter);

    }
    public static void search() {
        searchQuery = SearchTourActivity.searchQuery;
        filter2(filter1(), currentState);


    }

    private void selectTabbar(int num) {
        switch (num) {
            case 0:
                btnPopular.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnSight.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnRomantic.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnAdventure.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                currentState = 0;
                filter2(filter1(), 0);
                break;
            case 1:
                btnPopular.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnSight.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnRomantic.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnAdventure.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                currentState = 2;
                filter2(filter1(), 2);
                break;
            case 2:
                btnPopular.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnSight.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnRomantic.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnAdventure.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                currentState = 1;
                filter2(filter1(), 1);
                break;
            case 3:
                btnPopular.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnSight.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnRomantic.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnAdventure.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                currentState = 3;
                filter2(filter1(), 3);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnPopular) {
            selectTabbar(0);
        }
        if (v == btnSight) {
            selectTabbar(1);
        }
        if (v == btnRomantic) {
            selectTabbar(2);
        }
        if (v == btnAdventure) {
            selectTabbar(3);
        }

    }
    private static ArrayList<TourModel> filter1() {
        if (searchQuery.length() == 0) {
            return arrTourModel;
        } else {
            ArrayList<TourModel> arrayList = new ArrayList<>();
            for (int i = 0; i < arrTourModel.size(); i ++ ) {
                if (arrTourModel.get(i).getTitle().toLowerCase().contains(searchQuery.toLowerCase()) || arrTourModel.get(i).getAttractions().toLowerCase().contains(searchQuery.toLowerCase())) {
                    arrayList.add(arrTourModel.get(i));
                }
            }
            return arrayList;
        }
    }
    private static void filter2(ArrayList<TourModel> arrTours, int type) {
        ArrayList<TourModel> arrayList = arrTours;
        arrBufferTourModel = new ArrayList<>();
        arrBufferTourModel2.clear();
        offset = 0;
        isLast = false;
        switch (type) {
            case 0://default
                arrBufferTourModel = arrayList;
                break;
            case 1://romantic
                for (int i = 0; i < arrayList.size(); i ++ ) {
                    if (arrayList.get(i).getTourType().contains("R")) {
                        arrBufferTourModel.add(arrayList.get(i));
                    }
                }
                break;
            case 2://sightseeing
                for (int i = 0; i < arrayList.size(); i ++ ) {
                    if (arrayList.get(i).getTourType().contains("S")) {
                        arrBufferTourModel.add(arrayList.get(i));
                    }
                }

                break;
            case 3://adventure
                for (int i = 0; i < arrayList.size(); i ++ ) {
                    if (arrayList.get(i).getTourType().contains("A")) {
                        arrBufferTourModel.add(arrayList.get(i));
                    }
                }
                break;


        }
        reDrawListView();
    }
    private static void reDrawListView() {
        int num = 0;
        if ((offset * 5 + 5) > arrBufferTourModel.size()) {
            num = arrBufferTourModel.size();
            isLast = true;
        } else {
            num = (offset * 5 + 5);
        }

        for (int i = offset * 5; i < num; i ++ ) {
            arrBufferTourModel2.add(arrBufferTourModel.get(i));
        }
        offset ++;
        tourAdapter.notifyDataSetChanged();
    }
    private void fetch_tours() {

        {

            Utils.showProgress(mContext);

            Map<String, String> params = new HashMap<String, String>();
            params.put("location_id", ((SearchTourActivity) getActivity()).strCityIDs);

            CustomRequest signinRequest = new CustomRequest(Request.Method.POST, API.FETCH_TOUR, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.hideProgress();
                            try {
                                String success = response.getString("success");
                                if (success.equals("true")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    int tourCount = jsonArray.length();
                                    for (int i = 0; i < tourCount; i ++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        TourModel tourModel = new TourModel();

//                                        tourModel.setLocationIds(jsonObject.getString("locationId"));
                                        tourModel.setCityName(jsonObject.getString("city"));
                                        tourModel.setTour_id(jsonObject.getString("tourId"));
                                        tourModel.setTitle(jsonObject.getString("title"));
                                        tourModel.setUserId(jsonObject.getString("operatorId"));
                                        tourModel.setActive(jsonObject.getString("active"));
                                        tourModel.setTourType(jsonObject.getString("tourType"));
                                        tourModel.setCurrency_unit(jsonObject.getString("currency"));
                                        tourModel.setIs_private(jsonObject.getString("private"));
                                        tourModel.setLanguages(jsonObject.getString("language"));
                                        tourModel.setAttractions(jsonObject.getString("attractions"));
                                        tourModel.setInclusions(jsonObject.getString("inclusions"));
                                        tourModel.setInclusionOthers(jsonObject.getString("notes"));
                                        tourModel.setFrequency(jsonObject.getString("frequency"));
                                        tourModel.setSpecifiedCityNames(jsonObject.getString("specifiedCity"));
                                        tourModel.setStartTime(jsonObject.getString("startTime"));
                                        tourModel.setStartDate(jsonObject.getString("startDate"));
                                        tourModel.setStartDay(jsonObject.getString("startDay"));
                                        tourModel.setDurationUnit(jsonObject.getString("tourDurationUnit"));
                                        tourModel.setDurationTime(jsonObject.getString("tourDuration"));
                                        tourModel.setAdultPrice(jsonObject.getString("priceAdult"));
                                        tourModel.setChildPrice(jsonObject.getString("priceChild"));
                                        tourModel.setCreated_date(jsonObject.getString("created_at"));
                                        tourModel.setAverage_rating(jsonObject.getString("avgRating"));
                                        tourModel.setTotal_reviews(jsonObject.getString("totalReviews"));
                                        tourModel.setOperatorName(jsonObject.getString("fullname"));
                                        String pictures = jsonObject.getString("pictures");
                                        String[] strings = pictures.split(",");
                                        ArrayList<String> arrayList = new ArrayList<>();
                                        for (int k = 0; k < strings.length; k ++) {
                                            arrayList.add(strings[k]);
                                        }
                                        tourModel.setArrImage(arrayList);

                                        //////////calculate past time
                                        if (tourModel.getFrequency().equals("Once")) {
                                            String[] strDates = tourModel.getStartDate().split(",");
                                            String[] strTimes = tourModel.getStartTime().split(",");
                                            String[] strTimeArray = getResources().getStringArray(R.array.start_time);
                                            String recoverStartDate = "";
                                            String recoverStartTime = "";
                                            int maxOffset = getMaxTimeZone(tourModel.getCityName());

                                            for (int j = 0; j < strDates.length; j ++ ) {

////////////////////
                                                //get time after 12 hours(by utc)
                                                DateTime now = DateTime.now();
                                                DateTime after12hours = now.plusHours(12 - myOffset + maxOffset);

                                                ///count city time(by utc)
                                                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                                                DateTime dt = formatter.parseDateTime(strDates[j]);

                                                DateTime selectedTime = null;
                                                String startTime = strTimeArray[Integer.parseInt(strTimes[j])];
                                                if (startTime.contains("Morning")) {
                                                    selectedTime = dt.plusHours(8 );
                                                } else if (startTime.contains("Afternoon")) {
                                                    selectedTime = dt.plusHours(12);
                                                }else if (startTime.contains("Evening")) {
                                                    selectedTime = dt.plusHours(16);
                                                }
                                                else if (startTime.contains("Night")) {
                                                    selectedTime = dt.plusHours(20);
                                                }

                                                //compare time
                                                int result = DateTimeComparator.getInstance().compare(after12hours, selectedTime);
                                                if (result == 1) {
                                                } else {
                                                    recoverStartDate = recoverStartDate + strDates[j] + ",";
                                                    recoverStartTime = recoverStartTime + strTimes[j] + ",";

                                                }

                                            }
                                            if (recoverStartDate.length() > 0) {
                                                tourModel.setStartDate(recoverStartDate.substring(0, recoverStartDate.length() - 1));
                                                tourModel.setStartTime(recoverStartTime.substring(0, recoverStartTime.length() - 1));
                                                arrTourModel.add(tourModel);
                                            }
                                        } else {
                                            arrTourModel.add(tourModel);
                                        }

                                        /////


                                    }
//                                    search();
                                    filter2(filter1(), 0);

                                } else {
                                    String reason = response.getString("reason");
                                    if (reason.equals("401")) {
                                        Utils.showOKDialog(mContext, "Email is unregistered");
                                    } else if (reason.equals("402")) {
                                        Utils.showOKDialog(mContext, "Password incorrect");
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
                            Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(signinRequest);
        }

    }
    private int getMaxTimeZone(String strCities) {
        int maxOffset = -100;
        if (strCities.length() > 0) {
            String[] arrCities = strCities.split(",");
            List<LocationModel> arrayLocatons = DBHelper.getAllLocation();
            for (int i = 0; i < arrCities.length; i ++) {
                for (int k = 0; k < arrayLocatons.size(); k ++) {
                    if (arrayLocatons.get(k).getCity().equals(arrCities[i])) {
                        if (Integer.parseInt(arrayLocatons.get(k).getTimezone()) > maxOffset) {
                            maxOffset = Integer.parseInt(arrayLocatons.get(k).getTimezone());
                        }
                        break;
                    }
                }
            }
        }

        return maxOffset;

    }

}
