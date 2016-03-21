package com.allytours.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.model.Constant;
import com.allytours.model.LocationModel;
import com.allytours.model.TourModel;
import com.allytours.utilities.Utils;
import com.allytours.view.HomeActivity;
import com.allytours.view.adapter.TourAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.CustomRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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

        ((HomeActivity)getActivity()).showSearchView(true);
    }
    public static void search(String query) {
        searchQuery = query;
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
//        if (v == ivBack) {
//            ((HomeActivity)getActivity()).navigationTo(2);
//        }
    }
    private static ArrayList<TourModel> filter1() {
        if (searchQuery.length() == 0) {
            return arrTourModel;
        } else {
            ArrayList<TourModel> arrayList = new ArrayList<>();
            for (int i = 0; i < arrTourModel.size(); i ++ ) {
                if (arrTourModel.get(i).getTitle().contains(searchQuery) || arrTourModel.get(i).getAttractions().contains(searchQuery)) {
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
            params.put("location_id", ((HomeActivity) getActivity()).strCityIDs);

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
                                        tourModel.setSpecifiedCityIds(jsonObject.getString("specifiedCity"));
                                        tourModel.setStart_time(jsonObject.getString("startTime"));
                                        tourModel.setStartDate(jsonObject.getString("startDate"));
                                        tourModel.setStartDay(jsonObject.getString("startDay"));
                                        tourModel.setDurationDay(jsonObject.getString("tourDurationUnit"));
                                        tourModel.setDurationTime(jsonObject.getString("tourDuration"));
                                        tourModel.setAdultPrice(jsonObject.getString("priceAdult"));
                                        tourModel.setChildPrice(jsonObject.getString("priceChild"));
                                        tourModel.setCreated_date(jsonObject.getString("created_at"));
                                        tourModel.setAverage_rating(jsonObject.getString("avgRating"));
                                        tourModel.setTotal_reviews(jsonObject.getString("totalReviews"));
                                        String pictures = jsonObject.getString("pictures");
                                        String[] strings = pictures.split(",");
                                        ArrayList<String> arrayList = new ArrayList<>();
                                        for (int k = 0; k < strings.length; k ++) {
                                            arrayList.add(strings[k]);
                                        }
                                        tourModel.setArrImage(arrayList);

                                        arrTourModel.add(tourModel);

                                    }
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

}
