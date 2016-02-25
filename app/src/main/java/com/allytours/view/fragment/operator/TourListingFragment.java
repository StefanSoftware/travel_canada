package com.allytours.view.fragment.operator;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.model.TourModel;
import com.allytours.view.PurchaseActivity;
import com.allytours.view.adapter.TourAdapter;
import com.allytours.widget.HorizontalListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourListingFragment extends Fragment implements View.OnClickListener{

    private Button btnActive, btnInactive;
    private ListView lvHome;
    private PullToRefreshListView mPullRefreshHomeListView;

    private Context mContext;
    boolean isLast;
    boolean isActive;

    public TourListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_tour_listing, container, false);

        initVariable();
        initUI(view);

        return  view;
    }

    private void initVariable() {
        mContext = getContext();
        isLast = false;
        isActive = true;
    }
    private void initUI(View view) {
        btnActive = (Button) view.findViewById(R.id.btn_tourlisting_active);
        btnInactive = (Button) view.findViewById(R.id.btn_tourlisting_inactive);

        btnActive.setOnClickListener(this);
        btnInactive.setOnClickListener(this);

        ///create listview
        mPullRefreshHomeListView = (PullToRefreshListView) view.findViewById(R.id.lv_tour_listing);
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
                btnActive.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                btnInactive.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                isActive = true;

                break;
            case 1:
                btnActive.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                btnInactive.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                isActive = false;
                break;

        }
        makeList();
    }
    @Override
    public void onClick(View v) {
        if (v == btnActive) {
            selectTabbar(0);
        }
        if (v == btnInactive) {
            selectTabbar(1);
        }
    }
    //////for test
    private void  makeList() {

        ArrayList<TourModel> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            TourModel tourModel = new TourModel();
            tourModel.setTitle("Tour " + String.valueOf(i));
            tourModel.setTotalPrice(String.valueOf(152 * i));
            tourModel.setLastMessageCount(String.valueOf(i));
            arrayList.add(tourModel);
        }
        TourListingAdapter tourAdapter = new TourListingAdapter(mContext, arrayList);
        lvHome.setAdapter(tourAdapter);
    }

    private class TourListingAdapter extends BaseAdapter {

        ArrayList<TourModel> arrTours;
        Context mContext;
        LayoutInflater mlayoutInflater;
        public TourListingAdapter (Context context, ArrayList<TourModel> arrTours) {
            this.arrTours = arrTours;
            this.mContext = context;
            mlayoutInflater = LayoutInflater.from(mContext);
        }
        @Override
        public int getCount() {
            return arrTours.size();
        }

        @Override
        public Object getItem(int position) {
            return arrTours.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mlayoutInflater.inflate(R.layout.item_tour_listing, null);
            }
            TourModel tourModel = arrTours.get(position);

            TextView tvTitle = (TextView)view.findViewById(R.id.tv_item_tour_listing_title);
            TextView tvTotalPrice = (TextView)view.findViewById(R.id.tv_item_tour_listing_total_price);
            TextView tvCurrencyUnit = (TextView)view.findViewById(R.id.tv_item_tour_listing_currency_unit);
            TextView tvLastMessage = (TextView)view.findViewById(R.id.tv_item_tour_listing_last_message);
            TextView tvLastMessageCount = (TextView)view.findViewById(R.id.tv_item_tour_listing_last_message_count);
            TextView tvLastMessageTime = (TextView)view.findViewById(R.id.tv_item_tour_listing_last_message_time);

            Button btnActive = (Button)view.findViewById(R.id.btn_item_tour_listing_active);
            if (isActive) {
                btnActive.setText("Active");
            } else {
                btnActive.setText("Inactive");
            }
            tvTitle.setText(tourModel.getTitle());
            tvTotalPrice.setText(tourModel.getTotalPrice());
            if (Integer.parseInt(tourModel.getLastMessageCount()) == 0) {
                tvLastMessageCount.setVisibility(View.GONE);
            } else {
                tvLastMessageCount.setText(tourModel.getLastMessageCount());
            }

            return view;
        }

    }
}
