package com.allytours.view.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.model.TourModel;
import com.allytours.utilities.DiskBitmapCache;
import com.allytours.utilities.image_downloader.UrlImageViewCallback;
import com.allytours.utilities.image_downloader.UrlRectangleImageViewHelper;
import com.allytours.view.PurchaseActivity;
import com.allytours.widget.FadeInImageListener;
import com.allytours.widget.HorizontalListView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailTourFragment extends Fragment implements View.OnClickListener{

    RequestQueue mVolleyQueue;
    ImageLoader mImageLoader;

    private Button btnQuery, btnBuy;
    private Context mContext;
    private LinearLayout llButtonContainer;
    private  HorizontalListView horizontalListView;
    TextView tvTitle;
    TextView tvAdultPrice ;
    TextView tvChildPrice ;
    TextView tvAdultPriceUnit;
    TextView tvChildPriceUnit;
    TextView tvPrivate ;
    TextView tvDurationHours;
    TextView tvDurationDays;
    TextView tvStartTime ;
    TextView tvReviewCount , tvAttraction, tvSpecifiedCity, tvNote;

    ImageView ivFlagE;
    ImageView ivFlagS;
    ImageView ivFlagF;
    ImageView ivFlagC;
    ImageView ivFlagP;
    ImageView ivFlagG;
    ImageView ivFlagJ;
    ImageView ivRomantic;
    ImageView ivSightSeeing;
    ImageView ivAdventure;
    ImageView ivMark;
    LinearLayout llStartTimeContainer;
    LinearLayout llTaxes, llTips, llRoundTrips, llBreakfast, llLunch, llDinner, llEquipmentRental, llMandatoryFee, llEnteranceFee, llOther;
    private TourModel mTourModel;

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
        setData();

        return view;
    }

    private void initVariable() {
        mContext = getActivity();
        mTourModel = (TourModel) getActivity().getIntent().getSerializableExtra("tour");
        // Initialise Volley Request Queue.
        mVolleyQueue = Volley.newRequestQueue(mContext);
        int max_cache_size = 1000000;
        mImageLoader = new ImageLoader(mVolleyQueue, new DiskBitmapCache(mContext.getCacheDir(),max_cache_size));
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
        horizontalListView = (HorizontalListView)view.findViewById(R.id.hlv_dt);
        ImageAdapter imageAdapter = new ImageAdapter(mContext, PurchaseActivity.tourModel.getArrImage());
        horizontalListView.setAdapter(imageAdapter);

//        tvTitle = (TextView)view.findViewById(R.id.tv_dt_title);
        tvAdultPrice = (TextView)view.findViewById(R.id.tv_dt_adult_price);
        tvChildPrice = (TextView)view.findViewById(R.id.tv_dt_child_price);
        tvAdultPriceUnit = (TextView)view.findViewById(R.id.tv_dt_adult_price_unit);
        tvChildPriceUnit = (TextView)view.findViewById(R.id.tv_dt_child__price_unit);
        tvPrivate = (TextView)view.findViewById(R.id.tv_dt_private);
        tvDurationHours = (TextView)view.findViewById(R.id.tv_dt_duration_hours);
        tvDurationDays = (TextView)view.findViewById(R.id.tv_dt_duration_days);
        tvStartTime = (TextView)view.findViewById(R.id.tv_dt_start_time);
        tvAttraction = (TextView)view.findViewById(R.id.tv_dt_attraction);
        tvReviewCount = (TextView)view.findViewById(R.id.tv_dt_review_count);
        tvSpecifiedCity = (TextView)view.findViewById(R.id.tv_dt_specified_city);
        tvNote = (TextView)view.findViewById(R.id.tv_dt_notes);

        ivFlagE = (ImageView)view.findViewById(R.id.iv_dt_fg_e);
        ivFlagS = (ImageView)view.findViewById(R.id.iv_dt_fg_s);
        ivFlagF = (ImageView)view.findViewById(R.id.iv_dt_fg_f);
        ivFlagC = (ImageView)view.findViewById(R.id.iv_dt_fg_c);
        ivFlagP = (ImageView)view.findViewById(R.id.iv_dt_fg_p);
        ivFlagG = (ImageView)view.findViewById(R.id.iv_dt_fg_g);
        ivFlagJ = (ImageView)view.findViewById(R.id.iv_dt_fg_j);
        ivRomantic = (ImageView)view.findViewById(R.id.iv_dt_romentic);
        ivSightSeeing = (ImageView)view.findViewById(R.id.iv_dt_sightseeing);
        ivAdventure = (ImageView)view.findViewById(R.id.iv_dt_adventure);
        ivMark = (ImageView)view.findViewById(R.id.iv_dt_mark);

        llStartTimeContainer = (LinearLayout)view.findViewById(R.id.ll_dt_start_time_container);

        llTaxes = (LinearLayout)view.findViewById(R.id.ll_dt_taxes);
        llTips = (LinearLayout)view.findViewById(R.id.ll_dt_tips);
        llRoundTrips = (LinearLayout)view.findViewById(R.id.ll_dt_roundtrip);
        llBreakfast = (LinearLayout)view.findViewById(R.id.ll_dt_breakfast);
        llLunch = (LinearLayout)view.findViewById(R.id.ll_dt_lunch);
        llDinner = (LinearLayout)view.findViewById(R.id.ll_dt_dinner);
        llEquipmentRental = (LinearLayout)view.findViewById(R.id.ll_dt_equipment_rental);
        llMandatoryFee = (LinearLayout)view.findViewById(R.id.ll_dt_mandatory_fee);
        llEnteranceFee = (LinearLayout)view.findViewById(R.id.ll_dt_entrance_fee);
        llOther = (LinearLayout)view.findViewById(R.id.ll_dt_other);
    }

    private void setData() {
        if (mTourModel == null)
            return;
        PurchaseActivity.setTitle(mTourModel.getTitle());
        tvAdultPrice.setText(mTourModel.getAdultPrice());
        tvAdultPriceUnit.setText(mTourModel.getCurrency_unit());
        tvChildPrice.setText(mTourModel.getChildPrice());
        tvChildPriceUnit.setText(mTourModel.getCurrency_unit());
        tvReviewCount.setText(mTourModel.getReview_count() + "Reviews");
        int averageMark = Integer.parseInt(mTourModel.getAverage_rating());
        if (averageMark < 1.5) {
            ivMark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.smile1));
        } else if (averageMark < 2.5) {
            ivMark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.smile2));
        }else if (averageMark < 3.5) {
            ivMark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.smile3));
        }else if (averageMark < 4.5) {
            ivMark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.smile4));
        }else  {
            ivMark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.smile5));
        }
        if (mTourModel.getIs_private().equals("Y")) {
            tvPrivate.setText("Private");
        } else {
            tvPrivate.setText("Public");
        }
        tvDurationHours.setText(mTourModel.getDurationTime());
        tvDurationDays.setText(mTourModel.getDurationDay());
        tvAttraction.setText(mTourModel.getAttractions());
//        if (mTourModel.getFrequency().equals("Once")) {
//            tvStartTime.setText("Start Time(Once)");
//            tvStartTimeDate.setText(mTourModel.getStartDate());
//        } else {
//            tvStartTime.setText("Start Time(Recurring)");
//            String[] strDays = mContext.getResources().getStringArray(R.array.start_time_day);
//            String[] strings = mTourModel.getStartDay().split(",");
//            String startDays = "";
//            for (int j = 0; j < strings.length; j ++ ) {
//                startDays = startDays + strDays[Integer.parseInt(strings[j])] + ",";
//            }
//            tvStartTimeDate.setText(startDays.substring(0, startDays.length() - 1));
//        }
        if (mTourModel.getTourType().contains("R")) {
            ivRomantic.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getTourType().contains("S")) {
            ivSightSeeing.setVisibility(View.VISIBLE);
        }

        if (mTourModel.getTourType().contains("A")) {
            ivAdventure.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getLanguages().contains("E")) {
            ivFlagE.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getLanguages().contains("S")) {
            ivFlagS.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getLanguages().contains("F")) {
            ivFlagF.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getLanguages().contains("P")) {
            ivFlagP.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getLanguages().contains("C")) {
            ivFlagC.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getLanguages().contains("G")) {
            ivFlagG.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getLanguages().contains("J")) {
            ivFlagJ.setVisibility(View.VISIBLE);
        }

        if (mTourModel.getInclusions().contains("0")) {
            llTaxes.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("1")) {
            llTips.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("2")) {
            llRoundTrips.setVisibility(View.VISIBLE);
            tvSpecifiedCity.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("3")) {
            llBreakfast.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("4")) {
            llLunch.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("5")) {
            llDinner.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("6")) {
            llEquipmentRental.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("7")) {
            llEnteranceFee.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("8")) {
            llMandatoryFee.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("9")) {
            llOther.setVisibility(View.VISIBLE);
            tvNote.setVisibility(View.VISIBLE);
        }
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

        ArrayList<String> arrImages;
        Context mContext;
        LayoutInflater mlayoutInflater;
        public ImageAdapter (Context context, ArrayList<String> arrTours) {
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
//            mImageLoader.get(API.BASE_URL + arrImages.get(position), new FadeInImageListener(imageView, mContext));

            if (!arrImages.get(position).equals("")) {
                UrlRectangleImageViewHelper.setUrlDrawable(imageView, API.TOUR_URL + arrImages.get(position), R.drawable.default_tour, new UrlImageViewCallback() {
                    @Override
                    public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
                        if (!loadedFromCache) {
                            ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
                            scale.setDuration(10);
                            scale.setInterpolator(new OvershootInterpolator());
                            imageView.startAnimation(scale);
                        }
                    }
                });
            }
            return view1;
        }
    }
}
