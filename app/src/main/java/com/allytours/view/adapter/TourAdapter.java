package com.allytours.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.model.TourModel;
import com.allytours.utilities.BitmapUtil;
import com.allytours.utilities.DiskBitmapCache;
import com.allytours.view.PurchaseActivity;
import com.allytours.view.ReviewActivity;
import com.allytours.widget.FadeInImageListener;
import com.allytours.widget.HorizontalListView;
import com.android.volley.Cache;
import com.android.volley.RequestQueue;

import com.android.volley.cache.DiskBasedCache;
import com.android.volley.toolbox.ImageCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2/9/2016.
 */
public class TourAdapter extends BaseAdapter {

    ArrayList<TourModel> arrTours;
    Context mContext;
    LayoutInflater mlayoutInflater;
    RequestQueue mVolleyQueue;
    ImageLoader mImageLoader;

    /*
	 * Extends from DisckBasedCache --> Utility from volley toolbox.
	 * Also implements ImageCache, so that we can pass this custom implementation
	 * to ImageLoader.
	 */


    public TourAdapter (Context context, ArrayList<TourModel> arrTours) {
        this.arrTours = arrTours;
        this.mContext = context;
        mlayoutInflater = LayoutInflater.from(mContext);

        // Initialise Volley Request Queue.
        mVolleyQueue = Volley.newRequestQueue(mContext);
        int max_cache_size = 1000000;
        mImageLoader = new ImageLoader(mVolleyQueue, new DiskBitmapCache(mContext.getCacheDir(),max_cache_size));
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
            view = mlayoutInflater.inflate(R.layout.item_detail_tour, null);
        }
        HorizontalListView horizontalListView = (HorizontalListView)view.findViewById(R.id.horizontalListView);
        ImageAdapter imageAdapter = new ImageAdapter(mContext, arrTours.get(position).getArrImage());
        horizontalListView.setAdapter(imageAdapter);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int imageNum, long id) {

            }
        });
        TextView tvTitle = (TextView)view.findViewById(R.id.tv_it_title);
        TextView tvAdultPrice = (TextView)view.findViewById(R.id.tv_it_adult_price);
        TextView tvChildPrice = (TextView)view.findViewById(R.id.tv_it_child_price);
        TextView tvAdultPriceUnit = (TextView)view.findViewById(R.id.tv_it_adult_price_unit);
        TextView tvChildPriceUnit = (TextView)view.findViewById(R.id.tv_it_child__price_unit);
        TextView tvPrivate = (TextView)view.findViewById(R.id.tv_it_private);
        TextView tvDurationHours = (TextView)view.findViewById(R.id.tv_it_duration_hours);
        TextView tvDurationDays = (TextView)view.findViewById(R.id.tv_it_duration_days);
        TextView tvStartTime = (TextView)view.findViewById(R.id.tv_it_start_time);
        TextView tvStartTimeDate = (TextView)view.findViewById(R.id.tv_it_start_time_date);
        TextView tvReviewCount = (TextView)view.findViewById(R.id.tv_it_review_count);

        ImageView ivFlagE = (ImageView)view.findViewById(R.id.iv_it_fg_e);
        ImageView ivFlagS = (ImageView)view.findViewById(R.id.iv_it_fg_s);
        ImageView ivFlagF = (ImageView)view.findViewById(R.id.iv_it_fg_f);
        ImageView ivFlagC = (ImageView)view.findViewById(R.id.iv_it_fg_c);
        ImageView ivFlagP = (ImageView)view.findViewById(R.id.iv_it_fg_p);
        ImageView ivFlagG = (ImageView)view.findViewById(R.id.iv_it_fg_g);
        ImageView ivFlagJ = (ImageView)view.findViewById(R.id.iv_it_fg_j);
        ImageView ivRomantic = (ImageView)view.findViewById(R.id.iv_it_romentic);
        ImageView ivSightSeeing = (ImageView)view.findViewById(R.id.iv_it_sightseeing);
        ImageView ivAdventure = (ImageView)view.findViewById(R.id.iv_it_adventure);
        ImageView ivMark = (ImageView)view.findViewById(R.id.iv_it_mark);

        LinearLayout llReviewContainer = (LinearLayout)view.findViewById(R.id.ll_it_review_container);

        final TourModel tourModel = arrTours.get(position);
        llReviewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReviewActivity.class);
                intent.putExtra("tour_id", tourModel.getTour_id());
                mContext.startActivity(intent);
            }
        });
        tvTitle.setText(tourModel.getTitle());
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PurchaseActivity.class);
                intent.putExtra("tour", arrTours.get(position));
                mContext.startActivity(intent);
            }
        });
        tvAdultPrice.setText(tourModel.getAdultPrice());
        tvAdultPriceUnit.setText(tourModel.getCurrency_unit());
        tvChildPrice.setText(tourModel.getChildPrice());
        tvChildPriceUnit.setText(tourModel.getCurrency_unit());
        tvReviewCount.setText(tourModel.getReview_count() + "Reviews");
        int averageMark = Integer.parseInt(tourModel.getAverage_rating());
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
        if (tourModel.getIs_private().equals("Y")) {
            tvPrivate.setText("Private");
        } else {
            tvPrivate.setText("Public");
        }
        tvDurationHours.setText(tourModel.getDurationTime());
        tvDurationDays.setText(tourModel.getDurationDay());
        if (tourModel.getFrequency().equals("Once")) {
            tvStartTime.setText("Start Time(Once)");
            tvStartTimeDate.setText(tourModel.getStartDate());
        } else {
            tvStartTime.setText("Start Time(Recurring)");
            String[] strDays = mContext.getResources().getStringArray(R.array.start_time_day);
            String[] strings = tourModel.getStartDay().split(",");
            String startDays = "";
            for (int j = 0; j < strings.length; j ++ ) {
                startDays = startDays + strDays[Integer.parseInt(strings[j])] + ",";
            }
            tvStartTimeDate.setText(startDays.substring(0, startDays.length() - 1));
        }
        if (tourModel.getTourType().contains("R")) {
            ivRomantic.setVisibility(View.VISIBLE);
        }
        if (tourModel.getTourType().contains("S")) {
            ivSightSeeing.setVisibility(View.VISIBLE);
        }

        if (tourModel.getTourType().contains("A")) {
            ivAdventure.setVisibility(View.VISIBLE);
        }
        if (tourModel.getLanguages().contains("E")) {
            ivFlagE.setVisibility(View.VISIBLE);
        }
        if (tourModel.getLanguages().contains("S")) {
            ivFlagS.setVisibility(View.VISIBLE);
        }
        if (tourModel.getLanguages().contains("F")) {
            ivFlagF.setVisibility(View.VISIBLE);
        }
        if (tourModel.getLanguages().contains("P")) {
            ivFlagP.setVisibility(View.VISIBLE);
        }
        if (tourModel.getLanguages().contains("C")) {
            ivFlagC.setVisibility(View.VISIBLE);
        }
        if (tourModel.getLanguages().contains("G")) {
            ivFlagG.setVisibility(View.VISIBLE);
        }
        if (tourModel.getLanguages().contains("J")) {
            ivFlagJ.setVisibility(View.VISIBLE);
        }
        return view;
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
//            imageView.setImageDrawable(mContext.getResources().getDrawable(arrImages.get(position)));
            mImageLoader.get(API.BASE_URL + arrImages.get(position), new FadeInImageListener(imageView,mContext));
            return view1;
        }
    }
}
