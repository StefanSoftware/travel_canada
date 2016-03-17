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
    private ImageButton ivBack;

    private Context mContext;
    boolean isLast;

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
//        fetch_tours();
        return  view;
    }
    private void initVariable() {
        mContext = getContext();
        isLast = false;
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

        ivBack = (ImageButton)view.findViewById(R.id.ib_tours_back);

        ///create listview
        mPullRefreshHomeListView = (PullToRefreshListView)view.findViewById(R.id.lv_tour_list);
//        mPullRefreshHomeListView.setVisibility(View.GONE);
        mPullRefreshHomeListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                if (!isLast) {
//                    defaluteFetchTrip(currentCategory, GlobalVariable.getInstance().currentCountryName);
                }
                mPullRefreshHomeListView.onRefreshComplete();

            }
        });
        lvHome = mPullRefreshHomeListView.getRefreshableView();



    }


    private void selectTabbar(int num) {
        switch (num) {
            case 0:
                btnPopular.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnSight.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnRomantic.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnAdventure.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                break;
            case 1:
                btnPopular.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnSight.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnRomantic.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnAdventure.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                break;
            case 2:
                btnPopular.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnSight.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnRomantic.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnAdventure.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                break;
            case 3:
                btnPopular.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnSight.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnRomantic.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnAdventure.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
        if (v == ivBack) {
            ((HomeActivity)getActivity()).navigationTo(2);
        }
    }
    private void fetch_tours() {

        {

            Utils.showProgress(mContext);

            Map<String, String> params = new HashMap<String, String>();
            params.put("tours", ((HomeActivity) getActivity()).strCityIDs);

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

                                        tourModel.setLocationIds(jsonObject.getString("locationId"));
                                        tourModel.setTour_id(jsonObject.getString("tourId"));
                                        tourModel.setTitle(jsonObject.getString("title"));
                                        tourModel.setUserId(jsonObject.getString("operationId"));
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
                                        tourModel.setDurationDay(jsonObject.getString("tourDurationDays"));
                                        tourModel.setDurationTime(jsonObject.getString("tourDuratinHours"));
                                        tourModel.setAdultPrice(jsonObject.getString("priceAdult"));
                                        tourModel.setChildPrice(jsonObject.getString("priceChild"));
                                        tourModel.setCreated_date(jsonObject.getString("created_at"));
                                        tourModel.setAverage_rating(jsonObject.getString("average_rating"));
                                        tourModel.setTotal_reviews(jsonObject.getString("totalReviews"));
                                        String pictures = jsonObject.getString("pictures");
                                        String[] strings = pictures.split(",");
                                        ArrayList<String> arrayList = new ArrayList<>();
                                        for (int k = 0; k < strings.length; k ++) {
                                            arrayList.add(strings[k]);
                                        }
                                        tourModel.setArrImage(arrayList);
                                    }

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
