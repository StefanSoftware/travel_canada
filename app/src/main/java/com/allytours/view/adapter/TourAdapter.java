package com.allytours.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.model.Constant;
import com.allytours.model.TourModel;
import com.allytours.utilities.BitmapUtil;
import com.allytours.utilities.DiskBitmapCache;
import com.allytours.utilities.TimeUtility;
import com.allytours.utilities.image_downloader.UrlImageViewCallback;
import com.allytours.utilities.image_downloader.UrlRectangleImageViewHelper;
import com.allytours.view.DetailTourActivity;
import com.allytours.view.PurchaseActivity;
import com.allytours.view.ReviewActivity;
import com.allytours.view.SearchTourActivity;
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

        TextView tvTitle = (TextView)view.findViewById(R.id.tv_it_title);
        TextView tvCityName = (TextView)view.findViewById(R.id.tv_it_city);
        TextView tvAdultPrice = (TextView)view.findViewById(R.id.tv_it_adult_price);
        final TextView tvAdultPriceUnit = (TextView)view.findViewById(R.id.tv_it_adult_price_unit);
        TextView tvDurationHours = (TextView)view.findViewById(R.id.tv_it_duration);
        TextView tvDurationDays = (TextView)view.findViewById(R.id.tv_it_duration_unit);
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
        ImageView ivPrivate = (ImageView)view.findViewById(R.id.iv_it_private);

        LinearLayout llReviewContainer = (LinearLayout)view.findViewById(R.id.ll_it_review_container);

        final TourModel tourModel = arrTours.get(position);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int imageNum, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                View dialogLayout = mlayoutInflater.inflate(R.layout.detail_image_dialog_view, null);
                ImageView image = (ImageView) dialogLayout.findViewById(R.id.iv_didv);
                if (!tourModel.getArrImage().get(position).equals("")) {
                    UrlRectangleImageViewHelper.setUrlDrawable(image, API.TOUR_URL + tourModel.getArrImage().get(position), R.drawable.default_tour, new UrlImageViewCallback() {
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
                Intent intent = new Intent(mContext, DetailTourActivity.class);
                intent.putExtra("tour", tourModel);
                ((SearchTourActivity)mContext).startActivityForResult(intent, 201);
            }
        });
        tvCityName.setText(tourModel.getCityName());
        tvAdultPrice.setText(tourModel.getAdultPrice());
        tvAdultPriceUnit.setText(tourModel.getCurrency_unit());
        tvReviewCount.setText(tourModel.getTotal_reviews() + " Reviews");
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
            ivPrivate.setImageDrawable(mContext.getResources().getDrawable(R.drawable.small_car));
        } else {
            ivPrivate.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logo_72));
        }
        tvDurationHours.setText(tourModel.getDurationTime());
        tvDurationDays.setText(tourModel.getDurationUnit());
        //set start date or day
        if (tourModel.getFrequency().equals("Once")) {
            String[] strings = tourModel.getStartDate().split(",");
            String startDate = "";
            for (int j = 0; j < strings.length; j ++ ) {
                String strFormattedTime = TimeUtility.formatterToDateMonth(strings[j]);
                if (!startDate.contains(strFormattedTime)) {
                    startDate = startDate + strFormattedTime + ",";
                }

            }
            tvStartTimeDate.setText(startDate.substring(0, startDate.length() - 1));
        } else {
            String[] strDays = mContext.getResources().getStringArray(R.array.start_time_day);
            String[] strings = tourModel.getStartDay().split(",");
            String startDays = "";
            for (int j = 0; j < strings.length; j ++ ) {
                String strDay = strDays[Integer.parseInt(strings[j])];
                if (!startDays.contains(strDay)) {
                    startDays = startDays + strDay + ",";
                }

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view1 = convertView;
            if (view1 == null) {
                view1 = mlayoutInflater.inflate(R.layout.item_detail_tour_image, null);
            }
            ImageView imageView = (ImageView)view1.findViewById(R.id.iv_image);

//            mImageLoader.get(API.TOUR_URL + arrImages.get(position), new FadeInImageListener(imageView,mContext));

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
