package com.allytours.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allytours.R;
import com.allytours.view.PurchaseActivity;
import com.allytours.widget.HorizontalListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTourFragment extends Fragment implements View.OnClickListener{

    private Button btnQuery, btnBuy;
    private Context mContext;
    private LinearLayout llButtonContainer;

    public DetailTourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_tour, container, false);
        initVariable();
        initUI(view);
        return view;
    }

    private void initVariable() {
        mContext = getContext();

    }
    private void initUI(View view) {

        btnQuery = (Button)view.findViewById(R.id.btn_query_detail);
        btnQuery.setOnClickListener(this);
        btnBuy = (Button)view.findViewById(R.id.btn_buy_detail);
        btnBuy.setOnClickListener(this);

        llButtonContainer = (LinearLayout)view.findViewById(R.id.ll_detail_tour_button_container);
//        if (!(Utils.getFromPreference(getContext(), Constant.USER_TYPE).equals(Constant.USER_TYPE_CUSTOMER))) {
//            llButtonContainer.setVisibility(View.GONE);
//        }
        HorizontalListView horizontalListView = (HorizontalListView)view.findViewById(R.id.horizontalListView);
//        ImageAdapter imageAdapter = new ImageAdapter(mContext, PurchaseActivity.tourModel.getArrImage());
//        horizontalListView.setAdapter(imageAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v == btnQuery) {

        }
        if (v == btnBuy) {
            PurchaseActivity.pushFragment(1);
        }

    }
    private class ImageAdapter extends BaseAdapter {

        ArrayList<Integer> arrImages;
        Context mContext;
        LayoutInflater mlayoutInflater;
        public ImageAdapter (Context context, ArrayList<Integer> arrTours) {
            this.arrImages = arrTours;
            this.mContext = context;
            mlayoutInflater = LayoutInflater.from(mContext);
        }
        @Override
        public int getCount() {
            return arrImages.size();
        }

        @Override
        public Object getItem(int position) {
            return arrImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1 = convertView;
            if (view1 == null) {
                view1 = mlayoutInflater.inflate(R.layout.item_detail_tour_image, null);
            }
            ImageView imageView = (ImageView)view1.findViewById(R.id.iv_image);
            imageView.setImageDrawable(mContext.getResources().getDrawable(arrImages.get(position)));
            return view1;
        }
    }
}
