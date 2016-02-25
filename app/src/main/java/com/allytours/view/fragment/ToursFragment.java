package com.allytours.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.allytours.R;
import com.allytours.model.TourModel;
import com.allytours.view.adapter.TourAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToursFragment extends Fragment implements View.OnClickListener{

    private Button btnPopular, btnSight, btnAdventure, btnRomantic;
    private ListView lvHome;
    private PullToRefreshListView mPullRefreshHomeListView;

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


        ////for test
        makeList();
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
    }

    //////for test
    private void  makeList() {
        TourModel tourModel = new TourModel();
        tourModel.setArrImage(makeTours());
        ArrayList<TourModel> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            arrayList.add(tourModel);
        }
        TourAdapter tourAdapter = new TourAdapter(mContext, arrayList);
        lvHome.setAdapter(tourAdapter);
    }
    private ArrayList<Integer> makeTours() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.download3);
        arrayList.add(R.drawable.download1);
        arrayList.add(R.drawable.download2);
        arrayList.add(R.drawable.download3);
        return arrayList;
    }
}
