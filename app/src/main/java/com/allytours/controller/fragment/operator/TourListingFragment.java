package com.allytours.controller.fragment.operator;


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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.controller.DetailTourActivity;
import com.allytours.controller.Helpers.DBHelper;
import com.allytours.controller.ReviewActivity;
import com.allytours.controller.SearchTourActivity;
import com.allytours.model.API;
import com.allytours.model.LocationModel;
import com.allytours.model.TourModel;
import com.allytours.utilities.DiskBitmapCache;
import com.allytours.utilities.TimeUtility;
import com.allytours.utilities.Utils;
import com.allytours.utilities.image_downloader.UrlImageViewCallback;
import com.allytours.utilities.image_downloader.UrlRectangleImageViewHelper;
import com.allytours.widget.HorizontalListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.CustomRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TourListingFragment extends Fragment implements View.OnClickListener{

    private Button btnActive, btnInactive;
    private ListView lvHome;
    private PullToRefreshListView mPullRefreshHomeListView;
    private static TourListingAdapter tourListingAdapter;
    private Context mContext;
    static ArrayList<TourModel> arrActiveTourModel;
    static ArrayList<TourModel> arrInactiveTourModel;
    static ArrayList<TourModel> arrBufferTourModel;
    static ArrayList<TourModel> arrBufferTourModel2;
    static boolean isLast;
    static int offset;
    static boolean isActive;
    static String searchQuery;
    int myOffset;
//    static int currentState;


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
//        fetch_tours();
        return  view;
    }

    private void initVariable() {
        mContext = getActivity();
        isLast = false;
        offset = 0;
        isActive = true;
        arrActiveTourModel = new ArrayList<>();
        arrInactiveTourModel = new ArrayList<>();
        arrBufferTourModel = new ArrayList<>();
        arrBufferTourModel2 = new ArrayList<>();
        searchQuery = "";
//        currentState = 0;
        String offset1 = Utils.getFromPreference(mContext, "rawOffset");
        myOffset = Integer.parseInt(offset1) / 3600;
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
                    reDrawListView();
                }
                mPullRefreshHomeListView.onRefreshComplete();

            }
        });
        lvHome = mPullRefreshHomeListView.getRefreshableView();
        tourListingAdapter = new TourListingAdapter(mContext, arrBufferTourModel2);
        lvHome.setAdapter(tourListingAdapter);

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
        filter();
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


    private static ArrayList<TourModel> filter() {
        searchQuery = SearchTourActivity.searchQuery;
        arrBufferTourModel = new ArrayList<>();
        offset = 0;
        isLast = false;


        if (searchQuery.length() == 0) {
           if(isActive) {
               return arrActiveTourModel;
           } else {
               return arrInactiveTourModel;
           }
        } else {
            if(isActive) {
                for (int i = 0; i < arrActiveTourModel.size(); i ++ ) {
                    if (arrActiveTourModel.get(i).getTitle().toLowerCase().contains(searchQuery.toLowerCase()) || arrActiveTourModel.get(i).getAttractions().toLowerCase().contains(searchQuery.toLowerCase())) {
                        arrBufferTourModel.add(arrActiveTourModel.get(i));
                    }
                }
            } else {
                for (int i = 0; i < arrInactiveTourModel.size(); i ++ ) {
                    if (arrInactiveTourModel.get(i).getTitle().toLowerCase().contains(searchQuery.toLowerCase()) || arrInactiveTourModel.get(i).getAttractions().toLowerCase().contains(searchQuery.toLowerCase())) {
                        arrBufferTourModel.add(arrInactiveTourModel.get(i));
                    }
                }
            }

            reDrawListView();
            return arrBufferTourModel;
        }
    }

    private static void reDrawListView() {
        arrBufferTourModel2 = new ArrayList<>();
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
        tourListingAdapter.notifyDataSetChanged();
    }
    private void fetch_tours() {

        {

            Utils.showProgress(mContext);

            Map<String, String> params = new HashMap<String, String>();
            params.put("location_id", ((SearchTourActivity) getActivity()).strCityIDs);

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
                                        tourModel.setCityName(jsonObject.getString("city"));
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
                                        tourModel.setSpecifiedCityNames(jsonObject.getString("specifiedCity"));
                                        tourModel.setStartTime(jsonObject.getString("startTime"));
                                        tourModel.setStartDate(jsonObject.getString("startDate"));
                                        tourModel.setStartDay(jsonObject.getString("startDay"));
                                        tourModel.setDurationUnit(jsonObject.getString("tourDurationUnit"));
                                        tourModel.setDurationTime(jsonObject.getString("tourDuration"));
                                        tourModel.setAdultPrice(jsonObject.getString("priceAdult"));
                                        tourModel.setChildPrice(jsonObject.getString("priceChild"));
                                        tourModel.setCreated_date(jsonObject.getString("created_at"));
                                        tourModel.setAverage_rating(jsonObject.getString("avgRating"));
                                        tourModel.setTotal_reviews(jsonObject.getString("totalReviews"));
                                        tourModel.setOperatorName(jsonObject.getString("fullname"));
                                        String pictures = jsonObject.getString("pictures");
                                        String[] strings = pictures.split(",");
                                        ArrayList<String> arrayList = new ArrayList<>();
                                        for (int k = 0; k < strings.length; k ++) {
                                            arrayList.add(strings[k]);
                                        }
                                        tourModel.setArrImage(arrayList);

                                        //////////calculate past time
                                        if (tourModel.getFrequency().equals("Once")) {
                                            String[] strDates = tourModel.getStartDate().split(",");
                                            String[] strTimes = tourModel.getStartTime().split(",");
                                            String[] strTimeArray = getResources().getStringArray(R.array.start_time);
                                            String recoverStartDate = "";
                                            String recoverStartTime = "";
                                            int maxOffset = getMaxTimeZone(tourModel.getCityName());

                                            for (int j = 0; j < strDates.length; j ++ ) {

                                                //get time after 12 hours(by utc)
                                                DateTime now = DateTime.now();
                                                DateTime after12hours = now.plusHours(12 - myOffset + maxOffset);

                                                ///count city time(by utc)
                                                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                                                DateTime dt = formatter.parseDateTime(strDates[j]);

                                                DateTime selectedTime = null;
                                                String startTime = strTimeArray[Integer.parseInt(strTimes[j])];
                                                if (startTime.contains("Morning")) {
                                                    selectedTime = dt.plusHours(8 );
                                                } else if (startTime.contains("Afternoon")) {
                                                    selectedTime = dt.plusHours(12);
                                                }else if (startTime.contains("Evening")) {
                                                    selectedTime = dt.plusHours(16);
                                                }
                                                else if (startTime.contains("Night")) {
                                                    selectedTime = dt.plusHours(20);
                                                }

                                                //compare time
                                                int result = DateTimeComparator.getInstance().compare(after12hours, selectedTime);
                                                if (result == 1) {
                                                } else {
                                                    recoverStartDate = recoverStartDate + strDates[j] + ",";
                                                    recoverStartTime = recoverStartTime + strTimes[j] + ",";

                                                }

                                            }
                                            if (recoverStartDate.length() > 0) {
                                                tourModel.setStartDate(recoverStartDate.substring(0, recoverStartDate.length() - 1));
                                                tourModel.setStartTime(recoverStartTime.substring(0, recoverStartTime.length() - 1));
                                                tourModel.setActive("1");
                                                arrActiveTourModel.add(tourModel);
                                            } else {
                                                tourModel.setActive("0");
                                                arrInactiveTourModel.add(tourModel);
                                            }
                                        } else {
                                            if (tourModel.getActive().equals("1")) {
                                                arrActiveTourModel.add(tourModel);
                                            } else {
                                                arrInactiveTourModel.add(tourModel);
                                            }
                                        }

                                        /////


                                    }
                                    filter();

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

    private void active(boolean toActive, int position) {
        TourModel tourModel = arrBufferTourModel2.get(position);
        arrBufferTourModel2.remove(position);
        arrBufferTourModel.remove(position);
        if (toActive) {

            for (int i = 0; i < arrInactiveTourModel.size(); i ++) {
                if (tourModel.equals(arrInactiveTourModel.get(i))) {
                    arrInactiveTourModel.remove(i);
                    break;
                }
            }
            tourModel.setActive("1");
            arrActiveTourModel.add(0, tourModel);

        } else {

            for (int i = 0; i < arrActiveTourModel.size(); i ++) {
                if (tourModel.equals(arrActiveTourModel.get(i))) {
                    arrActiveTourModel.remove(i);
                    break;
                }
            }
            tourModel.setActive("0");
            arrInactiveTourModel.add(0, tourModel);
        }
        tourListingAdapter.notifyDataSetChanged();
    }
    public class TourListingAdapter extends BaseAdapter {
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


        public TourListingAdapter (Context context, ArrayList<TourModel> arrTours) {
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
                view = mlayoutInflater.inflate(R.layout.item_tour_listing, null);
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

            Switch sw =  (Switch)view.findViewById(R.id.sw_active);

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
            if (tourModel.getActive().equals("1")) {
                sw.setChecked(true);
                sw.setText("Inactive");
            } else {
                sw.setChecked(false);
                sw.setText("Active");
                if (tourModel.getFrequency().equals("Once")) {
                    sw.setVisibility(View.GONE);
                }
            }
            sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        active(true, position);
                    } else {
                        active(false, position);
                    }
                }
            });
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

}
