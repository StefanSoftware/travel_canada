package com.allytours.controller.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.model.API;
import com.allytours.model.Constant;
import com.allytours.model.TourModel;
import com.allytours.utilities.DiskBitmapCache;
import com.allytours.utilities.TimeUtility;
import com.allytours.utilities.Utils;
import com.allytours.utilities.image_downloader.UrlImageViewCallback;
import com.allytours.utilities.image_downloader.UrlRectangleImageViewHelper;
import com.allytours.controller.ChatActivity;
import com.allytours.controller.DetailTourActivity;
import com.allytours.controller.PurchaseActivity;
import com.allytours.controller.SigninActivity;
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
//    TextView tvAdultPrice ;
//    TextView tvChildPrice ;
//    TextView tvAdultPriceUnit;
//    TextView tvChildPriceUnit;
    TextView tvOperatorName;
    TextView tvDurationHours;
    TextView tvDurationDays;
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
    ImageView ivPrivate;
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
        mTourModel = DetailTourActivity.tourModel;
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
        ImageAdapter imageAdapter = new ImageAdapter(mContext, mTourModel.getArrImage());
        horizontalListView.setAdapter(imageAdapter);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int imageNum, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                View dialogLayout = getActivity().getLayoutInflater().inflate(R.layout.detail_image_dialog_view, null);
                ImageView image = (ImageView) dialogLayout.findViewById(R.id.iv_didv);
                if (!mTourModel.getArrImage().get(imageNum).equals("")) {
                    UrlRectangleImageViewHelper.setUrlDrawable(image, API.TOUR_URL + mTourModel.getArrImage().get(imageNum), R.drawable.default_tour, new UrlImageViewCallback() {
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
                builder.setView(dialogLayout);
                builder.show();
            }
        });

        tvOperatorName = (TextView)view.findViewById(R.id.tv_dt_operator_name);
        tvDurationHours = (TextView)view.findViewById(R.id.tv_dt_duration_hours);
        tvDurationDays = (TextView)view.findViewById(R.id.tv_dt_duration_days);
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
        ivPrivate = (ImageView)view.findViewById(R.id.iv_dt_private);

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
        DetailTourActivity.tvTitle.setText(mTourModel.getTitle());
        DetailTourActivity.tvCityName.setText(mTourModel.getCityName());
        DetailTourActivity.tvAdultPrice.setText(mTourModel.getAdultPrice() + " " + mTourModel.getCurrency_unit());
        DetailTourActivity.tvChildPrice.setText(mTourModel.getChildPrice() + " " + mTourModel.getCurrency_unit());

        tvOperatorName.setText(mTourModel.getOperatorName());

        tvReviewCount.setText(mTourModel.getTotal_reviews() + " Reviews");
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
            ivPrivate.setImageDrawable(getResources().getDrawable(R.drawable.car24));
        } else {
            ivPrivate.setImageDrawable(getResources().getDrawable(R.drawable.bus24));
        }
        tvDurationHours.setText(mTourModel.getDurationTime());
        tvDurationDays.setText(mTourModel.getDurationUnit());
        tvAttraction.setText(mTourModel.getAttractions());
        ///set start time
        if (llStartTimeContainer.getChildCount() > 0) {
            llStartTimeContainer.removeAllViews();
        }
        String[] strDays = mContext.getResources().getStringArray(R.array.start_time_day);
        String[] strTimes = mContext.getResources().getStringArray(R.array.start_time);

        String[] startTimeNumber = mTourModel.getStartTime().split(",");
        for (int j = 0; j < startTimeNumber.length; j ++) {
            View startTimeView = getActivity().getLayoutInflater().inflate(R.layout.item_detail_tour_start_time, null);
            TextView tvStartDate = (TextView)startTimeView.findViewById(R.id.tv_dt_start_time_date);
            TextView tvStartTime = (TextView)startTimeView.findViewById(R.id.tv_dt_start_time_time);
            tvStartTime.setText(strTimes[Integer.parseInt(startTimeNumber[j])]);
            if (mTourModel.getFrequency().equals("Once")) {
                String[] strings = mTourModel.getStartDate().split(",");
                tvStartDate.setText(TimeUtility.formatterToDateMonth(strings[j]));
            } else {

                String[] strings = mTourModel.getStartDay().split(",");
                tvStartDate.setText(strDays[Integer.parseInt(strings[j])]);
            }
            llStartTimeContainer.addView(startTimeView);
        }

        ///set type
        if (mTourModel.getTourType().contains("R")) {
            ivRomantic.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getTourType().contains("S")) {
            ivSightSeeing.setVisibility(View.VISIBLE);
        }

        if (mTourModel.getTourType().contains("A")) {
            ivAdventure.setVisibility(View.VISIBLE);
        }
        ///set language
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
        ////set inclusion
        if (mTourModel.getInclusions().contains("0")) {
            llTaxes.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("1")) {
            llTips.setVisibility(View.VISIBLE);
        }
        if (mTourModel.getInclusions().contains("2")) {
            llRoundTrips.setVisibility(View.VISIBLE);
            tvSpecifiedCity.setVisibility(View.VISIBLE);
            tvSpecifiedCity.setText(mTourModel.getSpecifiedCityNames());
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
            tvNote.setText(mTourModel.getInclusionOthers());
        }
    }
    @Override
    public void onClick(View v) {
        if (v == btnQuery) {
            String usertype =Utils.getFromPreference(mContext, Constant.USER_TYPE);
            if (usertype.length() == 0) {
                Intent intent = new Intent(mContext, SigninActivity.class);
                startActivityForResult(intent, Constant.SIGNIN_FOR_QUERY);//sign in for query
            } else if (usertype.equals(Constant.USER_TYPE_CUSTOMER)) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                startActivity(intent);
            }
        }
        if (v == btnBuy) {
            String usertype =Utils.getFromPreference(mContext, Constant.USER_TYPE);
            if (usertype.length() == 0) {
                Intent intent = new Intent(mContext, SigninActivity.class);
                startActivityForResult(intent, Constant.SIGNIN_FOR_BUY);//sign in for buy
            } else if (usertype.equals(Constant.USER_TYPE_CUSTOMER)) {
                Intent intent = new Intent(mContext, PurchaseActivity.class);
                startActivity(intent);
            }
//
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String usertype = Utils.getFromPreference(mContext, Constant.USER_TYPE);
        switch (requestCode) {

            case 301://when click query
                if (resultCode == getActivity().RESULT_OK) {
                    if (usertype.equals(Constant.USER_TYPE_CUSTOMER)) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        startActivity(intent);
//                        finish();
                    } else if (usertype.equals(Constant.USER_TYPE_OPERATOR)){
                        getActivity().setResult(100);
                        getActivity().finish();
                    }
                }
                break;
            case 302://when click buy
                if (resultCode == getActivity().RESULT_OK) {
                    if (usertype.equals(Constant.USER_TYPE_CUSTOMER)) {
                        Intent intent = new Intent(mContext, PurchaseActivity.class);
                        startActivityForResult(intent, 303);
                    } else if (usertype.equals(Constant.USER_TYPE_OPERATOR)){
                        getActivity().setResult(100);
                        getActivity().finish();
                    }
                }
                break;
            case 303:
                break;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view1 = convertView;
            if (view1 == null) {
                view1 = mlayoutInflater.inflate(R.layout.item_detail_tour_image, null);
            }
            final ImageView imageView = (ImageView)view1.findViewById(R.id.iv_image);
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
