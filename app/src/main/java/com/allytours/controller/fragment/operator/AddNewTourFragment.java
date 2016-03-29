package com.allytours.controller.fragment.operator;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.model.API;
import com.allytours.controller.Helpers.DBHelper;
import com.allytours.utilities.GPSTracker;
import com.allytours.utilities.LocationUtility;
import com.allytours.utilities.MediaUtility;
import com.allytours.utilities.UIUtility;
import com.allytours.utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.model.LocationModel;
import com.allytours.model.TourModel;
import com.allytours.utilities.camera.TakePictureFromCameraManager;
import com.allytours.widget.SelectDateFragment;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.CustomMultipartRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewTourFragment extends Fragment implements View.OnClickListener{

    private MultiAutoCompleteTextView mactLocation, mactRoundTrip;
    private EditText etTitle, etAdultPrice, etChildPrice;
    private TextView tvAdultCurrnceUnit, tvChildCurrencyUnit;
    private Button btnAddPicture, btnRemovePicture;
    private LinearLayout llPictureContainer;
    private RelativeLayout rlFlagE, rlFlagS, rlFlagF, rlFlagP, rlFlagC, rlFlagG, rlFlagJ;
    private CheckBox cbRomantic, cbSightseeing, cbAdventure;
    private RadioButton rbPublic, rbPrivate;
    private Spinner spFrequency;
    private LinearLayout llStartTimeContainer;
    private Button btnAddStartTime, btnRemoveStartTime;
    private Spinner spDuration, spDurationUnit;
    private EditText etAttraction;
    private CheckBox cbTaxes, cbTips, cbRoundTrip, cbBreakfast, cbLunch, cbDinner, cbEquipment, cbEntranceFee,
        cbOther, cbMandataryFee;
    private EditText etInclusionOther;
    private Button btnDone;

    private Context mContext;
    private String[] arrCiies;
    List<LocationModel> arrLocation;
    private TourModel mNewTourModel;

    private int pictureCount;
    private boolean[] bFlags;
    private ArrayList<String> arrPhotoPathes;
    private ArrayList<ImageView> arrImageViews;
    private  LayoutInflater mLayoutInflater;
    ArrayList<String> arrCity ;

    int myOffset;

    public AddNewTourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_add_new_tour, container, false);
        initVariables();
        initUI(view);
        return  view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void initVariables() {

        mContext = getActivity();

        mLayoutInflater = getActivity().getLayoutInflater();

        //get city list
        arrLocation = DBHelper.getAllLocation();
        int cityCount = arrLocation.size();
        arrCiies = new String[cityCount];
        for (int i = 0; i < cityCount; i ++) {
            arrCiies[i] = arrLocation.get(i).getCity();
        }
        ///init tour model
        mNewTourModel = new TourModel();

        pictureCount = 0;

        arrPhotoPathes = new ArrayList<>();
        arrImageViews = new ArrayList<>();

        bFlags = new boolean[7];
        bFlags[0]  = false;
        bFlags[1]  = false;
        bFlags[2]  = false;
        bFlags[3]  = false;
        bFlags[4]  = false;
        bFlags[5]  = false;
        bFlags[6]  = false;

        arrCity = getWholeCities();

        takePictureFromCameraManager = new TakePictureFromCameraManager(mContext);

        String offset1 = Utils.getFromPreference(mContext, "rawOffset");
        myOffset = Integer.parseInt(offset1) / 3600;
    }
//////    /////
    Drawable originalDrawableOfFlag;
    private void initUI(View view) {
        initMultiAutoCompletTextView(view);

        etTitle = (EditText)view.findViewById(R.id.et_nt_title);

        etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String title = etTitle.getText().toString().trim();
                    if (title.length() == 0) {
                        etTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etTitle.setBackgroundColor(getResources().getColor(R.color.transparent));

                }
            }
        });
        etAdultPrice = (EditText)view.findViewById(R.id.et_nt_adult_price);

        etAdultPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String adultPrice = etAdultPrice.getText().toString().trim();
                    if (adultPrice.length() == 0 || Integer.parseInt(adultPrice) < 10) {
                        etAdultPrice.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etAdultPrice.setBackgroundColor(getResources().getColor(R.color.transparent));

                }
            }
        });
        etChildPrice = (EditText)view.findViewById(R.id.et_nt_child_price);
        etChildPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String adultPrice = etChildPrice.getText().toString().trim();
                    if (adultPrice.length() == 0 || Integer.parseInt(adultPrice) < 10) {
                        etChildPrice.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etChildPrice.setBackgroundColor(getResources().getColor(R.color.transparent));

                }
            }
        });
        etAttraction = (EditText)view.findViewById(R.id.et_nt_attraction);
        etAttraction.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etAttraction.getText().toString().trim().length() == 0) {
                        etAttraction.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etAttraction.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });
        etInclusionOther = (EditText)view.findViewById(R.id.et_nt_inclusion_other);

        tvAdultCurrnceUnit = (TextView)view.findViewById(R.id.tv_nt_adult_currency);
        tvChildCurrencyUnit = (TextView)view.findViewById(R.id.tv_nt_child_currency);

        btnAddPicture = (Button)view.findViewById(R.id.btn_nt_add_picture);
        btnAddPicture.setOnClickListener(this);
        btnRemovePicture = (Button)view.findViewById(R.id.btn_nt_remove_picture);
        btnRemovePicture.setOnClickListener(this);
        btnDone = (Button)view.findViewById(R.id.btn_nt_done);
        btnDone.setOnClickListener(this);

        llPictureContainer = (LinearLayout)view.findViewById(R.id.ll_nt_picture_container);
        llStartTimeContainer = (LinearLayout)view.findViewById(R.id.ll_nt_start_time_container);

        btnAddStartTime = (Button)view.findViewById(R.id.btn_nt_add_start_time);
        btnAddStartTime.setOnClickListener(this);
        btnRemoveStartTime = (Button)view.findViewById(R.id.btn_nt_remove_start_time);
        btnRemoveStartTime.setOnClickListener(this);

        rlFlagE = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_e);
        rlFlagS = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_s);
        rlFlagF = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_f);
        rlFlagP = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_p);
        rlFlagC = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_c);
        rlFlagG = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_g);
        rlFlagJ = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_j);
        rlFlagE.setOnClickListener(this);
        rlFlagS.setOnClickListener(this);
        rlFlagF.setOnClickListener(this);
        rlFlagP.setOnClickListener(this);
        rlFlagC.setOnClickListener(this);
        rlFlagG.setOnClickListener(this);
        rlFlagJ.setOnClickListener(this);
        originalDrawableOfFlag = rlFlagE.getBackground();

        cbRomantic = (CheckBox)view.findViewById(R.id.cb_nt_romantic);
        cbSightseeing = (CheckBox)view.findViewById(R.id.cb_nt_sightseeing);
        cbAdventure = (CheckBox)view.findViewById(R.id.cb_nt_adventure);
        cbRomantic.setOnClickListener(this);
        cbSightseeing.setOnClickListener(this);
        cbAdventure.setOnClickListener(this);

        cbTaxes = (CheckBox)view.findViewById(R.id.cb_nt_taxes);
        cbTips = (CheckBox)view.findViewById(R.id.cb_nt_tips);
        cbRoundTrip = (CheckBox)view.findViewById(R.id.cb_nt_round_trip_transfer);
        cbBreakfast = (CheckBox)view.findViewById(R.id.cb_nt_breakfast);
        cbLunch = (CheckBox)view.findViewById(R.id.cb_nt_lunch);
        cbDinner = (CheckBox)view.findViewById(R.id.cb_nt_dinner);
        cbEquipment = (CheckBox)view.findViewById(R.id.cb_nt_equipment_rental);
        cbEntranceFee = (CheckBox)view.findViewById(R.id.cb_nt_entrance_fee);
        cbOther = (CheckBox)view.findViewById(R.id.cb_nt_other);
        cbMandataryFee = (CheckBox)view.findViewById(R.id.cb_nt_mandatory_fee);
        cbTaxes.setOnClickListener(this);
        cbTips.setOnClickListener(this);
        cbRoundTrip.setOnClickListener(this);
        cbBreakfast.setOnClickListener(this);
        cbLunch.setOnClickListener(this);
        cbDinner.setOnClickListener(this);
        cbEquipment.setOnClickListener(this);
        cbEntranceFee.setOnClickListener(this);
        cbOther.setOnClickListener(this);
        cbMandataryFee.setOnClickListener(this);

        rbPublic = (RadioButton)view.findViewById(R.id.rb_nt_public);
        rbPrivate = (RadioButton)view.findViewById(R.id.rb_nt_private);
        rbPublic.setOnClickListener(this);
        rbPrivate.setOnClickListener(this);

        spDuration = (Spinner)view.findViewById(R.id.sp_nt_duration_hours);
        spDurationUnit = (Spinner)view.findViewById(R.id.sp_nt_duration_days);
        spFrequency = (Spinner)view.findViewById(R.id.sp_nt_frequency);


        initSpinners(spDurationUnit, R.array.duration_unit);
        initSpinners(spDuration, R.array.duration);
        initSpinners(spFrequency, R.array.frequency);

        GPSTracker gpsTracker = new GPSTracker(mContext);
        LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        String countryName = LocationUtility.getCountryName(mContext, latLng);
        if (countryName.equals("Canada")) {
            tvAdultCurrnceUnit.setText("CAD");
            tvChildCurrencyUnit.setText("CAD");
        } else if (countryName.equals("United States")) {
            tvAdultCurrnceUnit.setText("USD");
            tvChildCurrencyUnit.setText("USD");
        }
        tvAdultCurrnceUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String[] strCurrency = getResources().getStringArray(R.array.currency_upper);
                new AlertDialog.Builder(mContext)
                        .setSingleChoiceItems(strCurrency, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                     /* User clicked on a radio button do some stuff */

                                ((TextView)v).setText(strCurrency[whichButton]);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        tvChildCurrencyUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String[] strCurrency = getResources().getStringArray(R.array.currency_upper);
                new AlertDialog.Builder(mContext)
                        .setSingleChoiceItems(strCurrency, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                     /* User clicked on a radio button do some stuff */

                                ((TextView)v).setText(strCurrency[whichButton]);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void initMultiAutoCompletTextView(View view) {
        ///init first MultiAutoCompleteTextView
        mactLocation = (MultiAutoCompleteTextView)view.findViewById(R.id.mact_nt_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext ,android.R.layout.simple_dropdown_item_1line, arrCiies);
        mactLocation.setAdapter(adapter);
        mactLocation.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mactLocation.getText().toString().trim().length() == 0) {
                        mactLocation.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        String finalCity = "";
                        String removedCity = "";
                        int citycount = 0;
                        String[] items = mactLocation.getText().toString().split(", ");
                        for (int k = 0; k < items.length; k ++) {
                            if (citycount == 5) break;
                            for (int i = 0; i < arrCiies.length; i ++) {
                                if (citycount == 5) break;
                                if (items[k].equals(arrCiies[i])) {
                                    finalCity = finalCity + ", " + items[k];
                                    citycount ++;
                                    break;
                                }
                                if (i == arrCiies.length - 1) {
                                    removedCity = removedCity + ", " + items[k];
                                }
                            }
                        }
                        if (finalCity.length() > 1) {
                            mactLocation.setText(finalCity.substring(2, finalCity.length()));
                        } else {
                            mactLocation.setText("");
                        }

                        if (removedCity.length() > 0) {
                            Utils.showOKDialog(mContext, removedCity.substring(2, removedCity.length()) + " not found" );
                        }

                    }
                } else {
                    mactLocation.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        ///init second MultiAutoCompleteTextView

        mactRoundTrip = (MultiAutoCompleteTextView)view.findViewById(R.id.mact_nt_specify_city);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, arrCity);
        mactRoundTrip.setAdapter(adapter1);
        mactRoundTrip.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactRoundTrip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mactRoundTrip.getText().toString().trim().length() == 0) {
                        mactRoundTrip.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        String finalCity = "";
                        String removedCity = "";
                        int citycount = 0;
                        String[] items = mactRoundTrip.getText().toString().split(", ");
                        for (int k = 0; k < items.length; k++) {
                            if (citycount == 5) break;
                            for (int i = 0; i < arrCity.size(); i++) {
                                if (citycount == 5) break;
                                if (items[k].equals(arrCity.get(i))) {
                                    finalCity = finalCity + ", " + items[k];
                                    citycount++;
                                    break;
                                }
                                if (i == arrCity.size() - 1) {
                                    removedCity = removedCity + ", " + items[k];
                                }
                            }
                        }
                        if (finalCity.length() > 1) {
                            mactRoundTrip.setText(finalCity.substring(2, finalCity.length()));
                        } else {
                            mactRoundTrip.setText("");
                        }
                        if (removedCity.length() > 0) {
                            Utils.showOKDialog(mContext, removedCity.substring(2, removedCity.length()) + " not found");
                        }

                    }
                } else {
                    mactRoundTrip.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });
    }

    private void initSpinners(final Spinner spinner, int arrayId) {

        // Spinner click listener
//        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        final String[] holderType;
        holderType = getResources().getStringArray(arrayId);
        // Creating adapter for spinner
        ArrayAdapter<String> holderTypeAdapter = new ArrayAdapter <String>(mContext, android.R.layout.simple_spinner_item, holderType);
        // Drop down layout style - list view with radio button
        holderTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(holderTypeAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Utils.showToast(mContext, holderType[position]);
                if (llStartTimeContainer.getChildCount() > 0 && spinner == spFrequency) {
                    llStartTimeContainer.removeAllViews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private ArrayList<String> getWholeCities() {
        String[] cityName = getResources().getStringArray(R.array.country_state_city);
        ArrayList<String> arrCity = new ArrayList<>();
        for(int i = 0; i < cityName.length; i ++) {
            arrCity.add(cityName[i].substring(0, cityName[i].indexOf(",")));
        }
        return arrCity;
    }

    ////post new tour
    private void showReviewDialog() {
        new AlertDialog.Builder(mContext)
                .setMessage("Plese confirm information again")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postNewTour();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Review", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }
    private void postNewTour() {
        Utils.showProgress(mContext);
        CustomMultipartRequest customMultipartRequest = new CustomMultipartRequest(API.ADD_TOUR,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.hideProgress();
                        try {
                            String success = response.getString("success");
                            if (success.equals("true")) {

                                getActivity().finish();
//                                ((HomeActivity) mContext).navigationTo(1);
                            } else {
                                String reason = response.getString("reason");
                                if (reason.equals("401")) {
                                    Utils.showOKDialog(mContext, "Failed add tour");
                                }else if (reason.equals("402")) {
                                    Utils.showOKDialog(mContext, "Email is registered already");
                                }else if (reason.equals("403")) {
                                    Utils.showOKDialog(mContext, "Invalid card");
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
        customMultipartRequest
                .addStringPart("id", mNewTourModel.getUserId())
                .addStringPart("locationid", mNewTourModel.getLocationIds())
                .addStringPart("title", mNewTourModel.getTitle())
                .addStringPart("adult_price", mNewTourModel.getAdultPrice())
                .addStringPart("child_price", mNewTourModel.getChildPrice())
                .addStringPart("currency_unit", mNewTourModel.getCurrency_unit())
//                .addStringPart("currency_unit", "CAD")
                .addStringPart("picture_count", mNewTourModel.getPictureCount())
                .addStringPart("active", "1")
                .addStringPart("tourtype", mNewTourModel.getTourType())
                .addStringPart("private", mNewTourModel.getIs_private())
                .addStringPart("language", mNewTourModel.getLanguages())
                .addStringPart("attractions", mNewTourModel.getAttractions())
                .addStringPart("inclusions", mNewTourModel.getInclusions())
                .addStringPart("frequency", mNewTourModel.getFrequency())
                .addStringPart("start_time", mNewTourModel.getStartTime())
                .addStringPart("tour_duration", mNewTourModel.getDurationTime())
                .addStringPart("tour_duration_unit", mNewTourModel.getDurationUnit());
        if (cbRoundTrip.isChecked()) {
            customMultipartRequest.addStringPart("specified_city", mNewTourModel.getSpecifiedCityNames());
        } else {
            customMultipartRequest.addStringPart("specified_city", "");
        }
        if (cbOther.isChecked()) {
            customMultipartRequest.addStringPart("notes", mNewTourModel.getInclusionOthers());
        } else {
            customMultipartRequest.addStringPart("notes", "");
        }
        if (mNewTourModel.getFrequency().equals("Once")) {
            customMultipartRequest.addStringPart("start_date", mNewTourModel.getStartDate());
            customMultipartRequest.addStringPart("start_day", "");
        } else {
            customMultipartRequest.addStringPart("start_day", mNewTourModel.getStartDate());
            customMultipartRequest.addStringPart("start_date", "");
        }


        for (int i = 0; i < pictureCount; i ++ ) {
            customMultipartRequest.addFilePart("picture" + String.valueOf(i + 1), mNewTourModel.getArrPicturePath().get(i));
        }


        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(customMultipartRequest);

    }
////////
    private String getCountryName(LatLng latLng) {
    String countryName = "";
    ////
    Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
    try {
        List<Address> addressList = geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1);
        if (addressList != null && addressList.size() > 0) {
            Address address = addressList.get(0);
            StringBuilder sb = new StringBuilder();
//                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                            sb.append(address.getAddressLine(i)).append("\n");
//                        }
//                        sb.append(address.getLocality());
//                        sb.append(address.getPostalCode()).append("\n");
//                        sb.append(address.getCountryName());
//                        searchKey = sb.toString();
//                        searchKey = address.getAddressLine(address.getMaxAddressLineIndex() - 1);
            if (address.getAdminArea() != null) {
                countryName = address.getCountryName();/////Address[addressLines=[0:"Allen, OK 74825",1:"USA"],feature=74825,admin=Oklahoma,sub-admin=null,locality=Allen,thoroughfare=null,postalCode=74825,countryCode=US,countryName=United States,hasLatitude=true,latitude=34.8031254,hasLongitude=true,longitude=-96.429036,phone=null,url=null,extras=null]
//                            searchKey = searchKey.replace(" ", "_");

            }

        }
    } catch (IOException e) {
//                    Log.e(TAG, "Unable connect to Geocoder", e);
        e.printStackTrace();
    }
    return countryName;
}
    private String getCountry(String cities) {
        String country = "";
        int canada = 0;
        int america = 0;
        String[] strings = cities.split(", ");
        List<LocationModel> arrLocation = DBHelper.getAllLocation();
        for (int i = 0; i < arrLocation.size(); i ++) {
            for (int k = 0; k < strings.length; k ++) {
                if (arrLocation.get(i).getCity().equals(strings[k])) {
                    LatLng latLng = new LatLng(Double.parseDouble(arrLocation.get(i).getLatitude()), Double.parseDouble(arrLocation.get(i).getLongitude()));
                    country = getCountryName(latLng);
                    if(country.equals("Canada")) {
                        canada = 1;
                    } else if (country.equals("United States")) {
                        america = 1;
                    }
                }
            }
        }
        if (canada == america) {
            return "";
        } else {
            return country;
        }
    }

    private boolean checkFields() {
        mNewTourModel.setUserId(Utils.getFromPreference(mContext, Constant.USER_ID));
        ///check city
        if (TextUtils.isEmpty(mactLocation.getText().toString().trim())) {
            Utils.showOKDialog(mContext, "Please input city");
            return false;
        } else {
            String finalCity = "";
            String removedCity = "";
            int citycount = 0;
            String[] items = mactLocation.getText().toString().split(", ");
            for (int k = 0; k < items.length; k ++) {
                if (citycount == 5) break;
                for (int i = 0; i < arrCiies.length; i ++) {
                    if (citycount == 5) break;
                    if (items[k].equals(arrCiies[i])) {
                        finalCity = finalCity + ", " + items[k];
                        citycount ++;
                        break;
                    }
                    if (i == arrCiies.length - 1) {
                        removedCity = removedCity + ", " + items[k];
                    }
                }
            }
            if (finalCity.length() > 1) {
                mactLocation.setText(finalCity.substring(2, finalCity.length()));
            } else {
                mactLocation.setText("");
            }

            if (removedCity.length() > 0) {
                Utils.showOKDialog(mContext, removedCity.substring(2, removedCity.length()) + " not found" );
                return false;
            }
            mNewTourModel.setLocationIds(getLocationIdFromCityName(mactLocation.getText().toString()));
        }
        //check title
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            Utils.showOKDialog(mContext, "Please input title");
            return false;
        } else {
            mNewTourModel.setTitle(etTitle.getText().toString().trim());
        }
        ///check price
        String strAdultPrice = etAdultPrice.getText().toString().trim();
        if (TextUtils.isEmpty(strAdultPrice)) {
            Utils.showOKDialog(mContext, "Please input adult price");
            return false;
        } else if (Integer.parseInt(strAdultPrice) < 10){
            Utils.showOKDialog(mContext, "Please input adult price bigger than 10");
            return false;
        } else {
            mNewTourModel.setAdultPrice(strAdultPrice);
        }

        String strChildPrice = etChildPrice.getText().toString().trim();
        if (TextUtils.isEmpty(strChildPrice)) {
            Utils.showOKDialog(mContext, "Please input child price");
            return false;
        } else if (Integer.parseInt(strChildPrice) < 10){
            Utils.showOKDialog(mContext, "Please input child price bigger than 10");
            return false;
        }
        else {
            mNewTourModel.setChildPrice(strChildPrice);
        }
        ///set currency unit
        String country = getCountry(mactLocation.getText().toString());
        if (country.length() > 0) {
            if (country.equals("Canada")) {
                if (tvAdultCurrnceUnit.getText().toString().equals("USD") || tvChildCurrencyUnit.getText().toString().equals("USD")) {

                    tvAdultCurrnceUnit.setText("CAD");
                    tvChildCurrencyUnit.setText("CAD");
                    Utils.showOKDialog(mContext, "Currency unit should be CAD");
                    return false;
                }


            } else if (country.equals("United States")) {
                if (tvAdultCurrnceUnit.getText().toString().equals("CAD") || tvChildCurrencyUnit.getText().toString().equals("CAD")) {

                    tvAdultCurrnceUnit.setText("USD");
                    tvChildCurrencyUnit.setText("USD");
                    Utils.showOKDialog(mContext, "Currency unit should be USD");
                    return false;
                }

            }
        }

        if (tvAdultCurrnceUnit.getText().toString().equals(tvChildCurrencyUnit.getText().toString())) {
            mNewTourModel.setCurrency_unit(tvChildCurrencyUnit.getText().toString());
        } else {
            Utils.showOKDialog(mContext, "Currency unit should be same");
            return false;
        }


        //check private
        if (rbPrivate.isChecked()) {
            mNewTourModel.setIs_private("Y");
        } else if (rbPublic.isChecked()) {
            mNewTourModel.setIs_private("N");
        }
        ///check tour type
        String tourType = "";
        if (cbAdventure.isChecked()) tourType = tourType + "A,";
        if (cbSightseeing.isChecked()) tourType = tourType + "S,";
        if (cbRomantic.isChecked()) tourType = tourType + "R,";
        if (tourType.length() == 0) {
            Utils.showOKDialog(mContext, "Please input tour type");
            return false;
        } else {
            mNewTourModel.setTourType(tourType.substring(0, tourType.length() - 1));
        }
        ///set frequency
        mNewTourModel.setFrequency(spFrequency.getSelectedItem().toString());
        ////set duration
        mNewTourModel.setDurationUnit(spDurationUnit.getSelectedItem().toString());
        mNewTourModel.setDurationTime(spDuration.getSelectedItem().toString());
        ///check attraction
        if (TextUtils.isEmpty(etAttraction.getText().toString().trim())) {
            Utils.showOKDialog(mContext, "Please input attraction");
            return false;
        } else {
            mNewTourModel.setAttractions(etAttraction.getText().toString().trim());
        }
        ///check inclusion
        String strInclustion = "";
        if (cbTaxes.isChecked()) {
            strInclustion = strInclustion + "0,";
        }
        if (cbTips.isChecked()) {
            strInclustion = strInclustion + "1,";
        }
        if (cbRoundTrip.isChecked()) {
            strInclustion = strInclustion + "2,";
            if (mactRoundTrip.getText().toString().trim().length() == 0) {
                Utils.showOKDialog(mContext, "Please input specified city");
                return false;
            } else {
                String finalCity = "";
                String removedCity = "";
                int citycount = 0;
                String[] items = mactRoundTrip.getText().toString().split(", ");
                for (int k = 0; k < items.length; k++) {
                    if (citycount == 5) break;
                    for (int i = 0; i < arrCity.size(); i++) {
                        if (citycount == 5) break;
                        if (items[k].equals(arrCity.get(i))) {
                            finalCity = finalCity + ", " + items[k];
                            citycount++;
                            break;
                        }
                        if (i == arrCity.size() - 1) {
                            removedCity = removedCity + ", " + items[k];
                        }
                    }
                }
                if (finalCity.length() > 1) {
                    mactRoundTrip.setText(finalCity.substring(2, finalCity.length()));
                } else {
                    mactRoundTrip.setText("");
                }

                if (removedCity.length() > 0) {
                    Utils.showOKDialog(mContext, removedCity.substring(2, removedCity.length()) + " not found");
                    return false;
                }
                mNewTourModel.setSpecifiedCityNames(mactRoundTrip.getText().toString().trim());
            }


        }
        if (cbBreakfast.isChecked()) {
            strInclustion = strInclustion + "3,";
        }
        if (cbLunch.isChecked()) {
            strInclustion = strInclustion + "4,";
        }
        if (cbDinner.isChecked()) {
            strInclustion = strInclustion + "5,";
        }
        if (cbEquipment.isChecked()) {
            strInclustion = strInclustion + "6,";
        }
        if (cbEntranceFee.isChecked()) {
            strInclustion = strInclustion + "7,";
        }
        if (cbOther.isChecked()) {
            strInclustion = strInclustion + "9,";
            mNewTourModel.setInclusionOthers(etInclusionOther.getText().toString().trim());
        }
        if (cbMandataryFee.isChecked()) {
            strInclustion = strInclustion + "8,";
        }
        if (strInclustion.length() == 0) {
            Utils.showOKDialog(mContext, "Please input inclusion");
            return false;
        } else {
            mNewTourModel.setInclusions(strInclustion.substring(0, strInclustion.length() - 1));
        }
        /////check language
        String strFlag = "";
        if (bFlags[0]) {
            strFlag = strFlag + "E,";
        }
        if (bFlags[1]) {
            strFlag = strFlag + "S,";
        }
        if (bFlags[2]) {
            strFlag = strFlag + "F,";
        }
        if (bFlags[3]) {
            strFlag = strFlag + "P,";
        }
        if (bFlags[4]) {
            strFlag = strFlag + "C,";
        }
        if (bFlags[5]) {
            strFlag = strFlag + "G,";
        }
        if (bFlags[6]) {
            strFlag = strFlag + "J,";
        }
        if (strFlag.length() == 0) {
            Utils.showOKDialog(mContext, "Please input language");
            return false;
        } else {
            mNewTourModel.setLanguages(strFlag.substring(0, strFlag.length() - 1));

        }
        ///check pictures
        if (arrPhotoPathes.size() < 2) {
            Utils.showOKDialog(mContext, "Please input at least 2 pictures");
            return false;
        } else {
            mNewTourModel.setArrPicturePath(arrPhotoPathes);
            mNewTourModel.setPictureCount(String.valueOf(arrPhotoPathes.size()));
        }
        ///check start time
        String strDate  = "";
        String strTime = "";
        int startTimeCount = llStartTimeContainer.getChildCount();
        if (startTimeCount == 0) {
            Utils.showOKDialog(mContext, "Please input start time");
            return false;
        } else {
            String strChecking = "";
            for (int i = 0; i < startTimeCount; i ++) {
                View view = llStartTimeContainer.getChildAt(i);
                EditText etDate = (EditText)view.findViewById(R.id.tv_item_nt_start_date);
                EditText etTime = (EditText)view.findViewById(R.id.tv_item_nt_start_time);

                String date = etDate.getText().toString().trim();
                String time = etTime.getText().toString().trim();

                if (!strChecking.contains(date + time)) {
                    if (spFrequency.getSelectedItem().toString().equals("Once")) {
                        strDate = strDate + etDate.getText().toString() + ",";
                    } else {
                        strDate = strDate + convertDayToNumber(date) + ",";
                    }
                    strTime = strTime + convertTimeToNumber(time) + ",";
                }
                strChecking  = strChecking + date + time + ",";


            }
            mNewTourModel.setStartTime(strTime);
            mNewTourModel.setStartDate(strDate);

        }
        return true;
    }
    private String convertDayToNumber(String day) {
        String num = "0";
        String[] strings = getResources().getStringArray(R.array.start_time_day);
        for(int i = 0; i < strings.length; i ++) {
            if (day.equals(strings[i])) {
                num = String.valueOf(i);
                break;
            }
        }
        return num;
    }
    private String convertTimeToNumber(String time) {
        String num = "0";
        String[] strings = getResources().getStringArray(R.array.start_time);
        for(int i = 0; i < strings.length; i ++) {
            if (time.equals(strings[i])) {
                num = String.valueOf(i);
                break;
            }
        }
        return num;
    }
    private String getLocationIdFromCityName(String cityNames) {
//        ArrayList<String> arrayList = StringUtility.spliteStringByCommaReturnArray(cityNames);
        String[] cities = spliteStringByComma(cityNames);
        int cityCount = cities.length;
        String cityIds = "";
        for (int i = 0; i < cityCount; i ++ ) {
            for (int k = 0; k < arrCiies.length; k ++ ) {
                if (cities[i].equals(arrCiies[k])) {
                    cityIds = cityIds + arrLocation.get(k).getLocation_id() + ",";
                }
            }
        }
        if (cityIds.length() > 0) {
            return cityIds.substring(0, cityIds.length() - 1);
        } else {
            return cityIds;
        }

    }
    private String[] spliteStringByComma(String str) {
        String[] strings = str.split(", ");
        return strings;
    }


    @Override
    public void onClick(View v) {
        if (!(v instanceof EditText)) {
            if (UIUtility.keyboardShown(getActivity())) {
                UIUtility.hideSoftKeyboard(getActivity());
            }

        }
        if (v == btnAddPicture) {
            if (pictureCount < 5) {
                showChooseDialog(mContext, "Choose picture from");
            }

        }
        if (v == btnRemovePicture) {
            removeImageView();
        }
        if (v == btnAddStartTime) {
            addStartTimeView();
        }
        if (v == btnRemoveStartTime) {
            removeStartTimeView();
        }
        if (v == btnDone) {
            if (checkFields()) {
               showReviewDialog();
            }
        }
        if (v == cbRoundTrip) {
            if (!cbRoundTrip.isChecked()) {
                mactRoundTrip.setVisibility(View.GONE);
            } else {
                mactRoundTrip.setVisibility(View.VISIBLE);
            }
        }
        if (v == cbOther ) {
            if (!cbOther.isChecked()) {
                etInclusionOther.setVisibility(View.GONE);
            } else {
                etInclusionOther.setVisibility(View.VISIBLE);
            }
        }
        if (v == rlFlagE) {
            if (bFlags[0]) {
                bFlags[0] = false;
                rlFlagE.setBackground(originalDrawableOfFlag);
            } else {
                bFlags[0] = true;
                rlFlagE.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }
        if (v == rlFlagS) {
            if (bFlags[1]) {
                bFlags[1] = false;
                rlFlagS.setBackground(originalDrawableOfFlag);
            } else {
                bFlags[1] = true;
                rlFlagS.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }
        if (v == rlFlagF) {
            if (bFlags[2]) {
                bFlags[2] = false;
                rlFlagF.setBackground(originalDrawableOfFlag);
            } else {
                bFlags[2] = true;
                rlFlagF .setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }
        if (v == rlFlagP) {
            if (bFlags[3]) {
                bFlags[3] = false;
                rlFlagP.setBackground(originalDrawableOfFlag);
            } else {
                bFlags[3] = true;
                rlFlagP.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }
        if (v == rlFlagC) {
            if (bFlags[4]) {
                bFlags[4] = false;
                rlFlagC.setBackground(originalDrawableOfFlag);
            } else {
                bFlags[4] = true;
                rlFlagC.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }
        if (v == rlFlagG) {
            if (bFlags[5]) {
                bFlags[5] = false;
                rlFlagG.setBackground(originalDrawableOfFlag);
            } else {
                bFlags[5] = true;
                rlFlagG.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }
        if (v == rlFlagJ) {
            if (bFlags[6]) {
                bFlags[6] = false;
                rlFlagJ.setBackground(originalDrawableOfFlag);
            } else {
                bFlags[6] = true;
                rlFlagJ.setBackgroundColor(getResources().getColor(R.color.grey));
            }
        }
    }

    ////add start time module
    TextView currentTextView;
    private void addStartTimeView() {
        if (llStartTimeContainer.getChildCount() >= 5) {
            return;
        }
        ///check previous view
        if (llStartTimeContainer.getChildCount() > 0) {
            View lastView = llStartTimeContainer.getChildAt(llStartTimeContainer.getChildCount() - 1);
            EditText etLastDate = (EditText)lastView.findViewById(R.id.tv_item_nt_start_date);
            EditText etLastTime = (EditText)lastView.findViewById(R.id.tv_item_nt_start_time);
            if (TextUtils.isEmpty(etLastDate.getText().toString().trim()) || TextUtils.isEmpty(etLastTime.getText().toString().trim())) {
                return;
            }
        }


        View view = mLayoutInflater.inflate(R.layout.item_new_tour_start_time, null);
        final EditText tvDate = (EditText)view.findViewById(R.id.tv_item_nt_start_date);
        final EditText tvTime = (EditText)view.findViewById(R.id.tv_item_nt_start_time);
        UIUtility.hideSoftKeyboard(getActivity());
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spFrequency.getSelectedItem().toString().equals("Once")) {

                    DialogFragment newFragment = new SelectDateFragment(tvDate);
                    newFragment.show(getFragmentManager(), "Start Date");
                } else {
                    final String[] strDays = getResources().getStringArray(R.array.start_time_day);
                    new AlertDialog.Builder(mContext)
                            .setSingleChoiceItems(strDays, 0, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                     /* User clicked on a radio button do some stuff */

                                    tvDate.setText(strDays[whichButton]);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] strTimes = getResources().getStringArray(R.array.start_time);
                new AlertDialog.Builder(mContext)
                        .setSingleChoiceItems(strTimes, 0, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                /* User clicked on a radio button do some stuff */
                                if (tvDate.getText().toString().length() > 0) {
                                    if (spFrequency.getSelectedItem().toString().equals("Once")) {
                                        int maxOffset = 0;
                                        if (mactLocation.getText().toString().length() == 0) {
                                            Utils.showOKDialog(mContext, "Please input city first");
                                            return;
                                        } else {
                                            maxOffset = getMaxTimeZone(mactLocation.getText().toString());

                                        }
                                        //get time after 12 hours
                                        DateTime now = DateTime.now();
                                        DateTime after12hours = now.plusHours(12 - myOffset + maxOffset);
                                        ///count selected time
                                        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                                        DateTime dt = formatter.parseDateTime(tvDate.getText().toString());
                                        DateTime selectedTime = null;
                                        switch (whichButton) {
                                            case 0:
                                                selectedTime = dt.plusHours(6);
                                                break;
                                            case 1:
                                                selectedTime = dt.plusHours(12);
                                                break;
                                            case 2:
                                                selectedTime = dt.plusHours(16);
                                                break;
                                            case 3:
                                                selectedTime = dt.plusHours(20);
                                                break;
                                        }
                                        //compare time
                                        int result = DateTimeComparator.getInstance().compare(after12hours, selectedTime);
                                        if (result == 1) {
                                            Utils.showOKDialog(mContext, "Please choose date and time after 12 hours");
                                        } else {
                                            tvTime.setText(strTimes[whichButton]);
                                        }
                                    } else{
                                        tvTime.setText(strTimes[whichButton]);
                                    }
                                } else {
                                    Utils.showOKDialog(mContext, "Please input date");
                                }


                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
        llStartTimeContainer.addView(view);
    }
    private void removeStartTimeView() {
        if (llStartTimeContainer.getChildCount() == 0) {
            return;
        }
        llStartTimeContainer.removeViewAt(llStartTimeContainer.getChildCount() - 1);
    }
    private int getMaxTimeZone(String strCities) {
        int maxOffset = -100;
        if (strCities.length() > 0) {
            String[] arrCities = strCities.split(", ");
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




    /////add picture module
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1010:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = mContext.getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    arrPhotoPathes.add(cursor.getString(columnIndex));
                    pictureCount = arrPhotoPathes.size();
                    cursor.close();

                    ///create new image view
                    addNewImageview();

                    //convert bitmap to drawable
//                    Drawable d = Drawable.createFromPath(arrPhotoPathes.get(arrPhotoPathes.size() - 1));
//                    Drawable drawable = new BitmapDrawable(getResources(), MediaUtility.adjustBitmap(arrPhotoPathes.get(arrPhotoPathes.size() - 1)));
//                    arrImageViews.get(pictureCount - 1).setImageDrawable(drawable);

                    Bitmap adjustedBitmap = MediaUtility.adjustBitmap(arrPhotoPathes.get(arrPhotoPathes.size() - 1));
                    //crop image
//                    Bitmap croppedBitmap = MediaUtility.cropBitmapAnySize(adjustedBitmap, 200, 200);
//                    MediaUtility.saveBitmapToLocal(croppedBitmap, arrPhotoPathes.get(arrPhotoPathes.size() - 1), arrPhotoPathes.get(arrPhotoPathes.size() - 1));

                    arrImageViews.get(pictureCount - 1).setImageBitmap(adjustedBitmap);

                }
                break;
            case 1011:
                if (resultCode == Activity.RESULT_OK) {
                    pictureCount = arrPhotoPathes.size();
                    addNewImageview();
                    takePictureFromCameraManager.handleBigCameraPhoto();
                } else {
                    arrPhotoPathes.remove(arrPhotoPathes.size() - 1);
                }
                break;
        }
    }

    private TakePictureFromCameraManager takePictureFromCameraManager;

    private void addNewImageview() {
        final View view = mLayoutInflater.inflate(R.layout.item_new_tour_picture, null);
        ImageView imageView;
        if (view != null) {
            imageView = (ImageView)view.findViewById(R.id.iv_item_nt_picture);
            takePictureFromCameraManager.setDestinationImageView(imageView);
            arrImageViews.add(imageView);
            llPictureContainer.addView(view);
        }
    }
    private void removeImageView() {
        if (llPictureContainer.getChildCount() == 0) {
            return;
        }
        llPictureContainer.removeViewAt(pictureCount - 1);
        arrImageViews.remove(pictureCount - 1);
        if (arrPhotoPathes.size() == pictureCount ) {
            arrPhotoPathes.remove(pictureCount - 1);
        }
        pictureCount --;
    }
    ///photo choose dialog
    public void showChooseDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constant.INDECATOR);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dispatchTakePictureIntent();
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Gallary",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        takePictureFromGallery();
                        dialog.cancel();
                    }
                });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();

            }
        });
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(false);
        AlertDialog alert = builder.create();
        alert.show();
    }
    //////////////////take a picture from gallery
    private void takePictureFromGallery()
    {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1010);


    }

    /////////////capture photo
    public void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        File f = null;

        try {
            f = takePictureFromCameraManager.setUpPhotoFile();
            arrPhotoPathes.add( f.getAbsolutePath());
            takePictureFromCameraManager.setPhotoPath(f.getAbsolutePath());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        } catch (IOException e) {
            e.printStackTrace();
            f = null;

        }
        startActivityForResult(takePictureIntent, 1011);
    }

}
