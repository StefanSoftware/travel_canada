package com.allytours.controller.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.allytours.controller.Helpers.DBHelper;
import com.allytours.model.API;
import com.allytours.model.LocationModel;
import com.allytours.model.TourModel;
import com.allytours.utilities.DiskBitmapCache;
import com.allytours.utilities.TimeUtility;
import com.allytours.utilities.Utils;
import com.allytours.utilities.image_downloader.UrlImageViewCallback;
import com.allytours.utilities.image_downloader.UrlRectangleImageViewHelper;
import com.allytours.controller.DetailTourActivity;
import com.allytours.controller.ReviewActivity;
import com.allytours.controller.SearchTourActivity;
import com.allytours.widget.HorizontalListView;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2/9/2016.
 */
public class TourAdapter extends BaseAdapter {

    ArrayList<TourModel> arrTours;
    Context mContext;
    LayoutInflater mlayoutInflater;
    RequestQueue mVolleyQueue;
    ImageLoader mImageLoader;
//    int myOffset ;
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

//        String offset1 = Utils.getFromPreference(mContext, "rawOffset");
//        myOffset = Integer.parseInt(offset1) / 3600;

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
                if (!tourModel.getArrImage().get(imageNum).equals("")) {
                    UrlRectangleImageViewHelper.setUrlDrawable(image, API.TOUR_URL + tourModel.getArrImage().get(imageNum), R.drawable.default_tour, new UrlImageViewCallback() {
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
            ivPrivate.setImageDrawable(mContext.getResources().getDrawable(R.drawable.car24));
        } else {
            ivPrivate.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bus24));
        }
        tvDurationHours.setText(tourModel.getDurationTime());
        tvDurationDays.setText(tourModel.getDurationUnit());
        //set start date or day

        if (tourModel.getFrequency().equals("Once")) {
            String[] strDates = tourModel.getStartDate().split(",");
            String startDate = "";

            for (int j = 0; j < strDates.length; j ++) {
                String strFormattedTime = TimeUtility.formatterToDateMonth(strDates[j]);
                if (!startDate.contains(strFormattedTime)) {
                    startDate = startDate + strFormattedTime + ",";
                }
            }

            if (startDate.length() > 0) {
                tvStartTimeDate.setText(startDate.substring(0, startDate.length() - 1));
            }

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
        ///set type
        if (tourModel.getTourType().contains("R")) {
            ivRomantic.setVisibility(View.VISIBLE);
        } else {
            ivRomantic.setVisibility(View.GONE);
        }
        if (tourModel.getTourType().contains("S")) {
            ivSightSeeing.setVisibility(View.VISIBLE);
        } else {
            ivSightSeeing.setVisibility(View.GONE);
        }

        if (tourModel.getTourType().contains("A")) {
            ivAdventure.setVisibility(View.VISIBLE);
        } else {
            ivAdventure.setVisibility(View.GONE);
        }
        ///set language
        if (tourModel.getLanguages().contains("E")) {
            ivFlagE.setVisibility(View.VISIBLE);
        } else {
            ivFlagE.setVisibility(View.GONE);
        }
        if (tourModel.getLanguages().contains("S")) {
            ivFlagS.setVisibility(View.VISIBLE);
        } else {
            ivFlagS.setVisibility(View.GONE);
        }
        if (tourModel.getLanguages().contains("F")) {
            ivFlagF.setVisibility(View.VISIBLE);
        } else {
            ivFlagF.setVisibility(View.GONE);
        }
        if (tourModel.getLanguages().contains("P")) {
            ivFlagP.setVisibility(View.VISIBLE);
        } else {
            ivFlagP.setVisibility(View.GONE);
        }
        if (tourModel.getLanguages().contains("C")) {
            ivFlagC.setVisibility(View.VISIBLE);
        } else {
            ivFlagC.setVisibility(View.GONE);
        }
        if (tourModel.getLanguages().contains("G")) {
            ivFlagG.setVisibility(View.VISIBLE);
        } else {
            ivFlagG.setVisibility(View.GONE);
        }
        if (tourModel.getLanguages().contains("J")) {
            ivFlagJ.setVisibility(View.VISIBLE);
        } else {
            ivFlagJ.setVisibility(View.GONE);
        }
        return view;
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
