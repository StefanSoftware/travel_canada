package com.allytours.view.fragment.operator;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.allytours.R;
import com.allytours.controller.Helpers.DBHelper;
import com.allytours.utilities.MediaUtility;
import com.allytours.utilities.UIUtility;
import com.allytours.utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.model.LocationModel;
import com.allytours.model.TourModel;
import com.allytours.utilities.camera.TakePictureFromCameraManager;
import com.allytours.widget.SelectDateFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private Spinner spDurationHour, spDurationDay;
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
    private void initVariables() {

        mContext = getContext();

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

        takePictureFromCameraManager = new TakePictureFromCameraManager(mContext);
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

        spDurationHour = (Spinner)view.findViewById(R.id.sp_nt_duration_hours);
        spDurationDay = (Spinner)view.findViewById(R.id.sp_nt_duration_days);
        spFrequency = (Spinner)view.findViewById(R.id.sp_nt_frequency);


        initSpinners(spDurationDay, R.array.duratin_day);
        initSpinners(spDurationHour, R.array.duratin_hour);
        initSpinners(spFrequency, R.array.frequency);

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
                    }
                } else {
                    mactLocation.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        ///init second MultiAutoCompleteTextView
        ArrayList<String> arrCity = getWholeCities();
        mactRoundTrip = (MultiAutoCompleteTextView)view.findViewById(R.id.mact_nt_specify_city);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, arrCity);
        mactRoundTrip.setAdapter(adapter1);
        mactRoundTrip.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void initSpinners(Spinner spinner, int arrayId) {

        // Spinner click listener
//        spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        String[] holderType;
        holderType = getResources().getStringArray(arrayId);
        // Creating adapter for spinner
        ArrayAdapter<String> holderTypeAdapter = new ArrayAdapter <String>(mContext, android.R.layout.simple_spinner_item, holderType);
        // Drop down layout style - list view with radio button
        holderTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(holderTypeAdapter);

    }

    private ArrayList<String> getWholeCities() {
        String[] cityName = getResources().getStringArray(R.array.country_state_city);
        ArrayList<String> arrCity = new ArrayList<>();
        for(int i = 0; i < cityName.length; i ++) {
            arrCity.add(cityName[i].substring(0, cityName[i].indexOf(",")));
        }
        return arrCity;
    }
////////
    private boolean checkFields() {
        mNewTourModel.setUserId(Utils.getFromPreference(mContext, Constant.USER_ID));
        if (TextUtils.isEmpty(mactLocation.getText().toString().trim())) {
            Utils.showOKDialog(mContext, "Please input city");
            return false;
        } else {
            mNewTourModel.setLocationIds(getLocationIdFromCityName(mactLocation.getText().toString().trim()));
        }
        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            Utils.showOKDialog(mContext, "Please input title");
            return false;
        } else {
            mNewTourModel.setTitle(etTitle.getText().toString().trim());
        }

        String strAdultPrice = etAdultPrice.getText().toString().trim();
        if (TextUtils.isEmpty(strAdultPrice)) {
            Utils.showOKDialog(mContext, "Please input adult price");
            return false;
        } else if (Integer.parseInt(strAdultPrice) < 10){
            Utils.showOKDialog(mContext, "Please input adult price bigger than 10");
            return false;
        } else {
            mNewTourModel.setTitle(strAdultPrice);
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
            mNewTourModel.setTitle(strChildPrice);
        }
        if (rbPrivate.isChecked()) {
            mNewTourModel.setIs_private("Y");
        } else if (rbPublic.isChecked()) {
            mNewTourModel.setIs_private("N");
        }
        String tourType = "";
        if (cbAdventure.isChecked()) tourType = tourType + "A,";
        if (cbSightseeing.isChecked()) tourType = tourType + "S,";
        if (cbRomantic.isChecked()) tourType = tourType + "R,";
        if (tourType.length() == 0) {
            Utils.showOKDialog(mContext, "Please input tour type");
            return false;
        } else {
            mNewTourModel.setTourType(tourType.substring(0, tourType.length() - 2));
        }
        mNewTourModel.setFrequency(spFrequency.getSelectedItem().toString());
        if (mNewTourModel.getFrequency().equals("Once")) {

        } else if (mNewTourModel.getFrequency().equals("Recurring")) {

        }
        mNewTourModel.setDurationDay(spDurationDay.getSelectedItem().toString());
        mNewTourModel.setDurationTime(spDurationHour.getSelectedItem().toString());
        if (TextUtils.isEmpty(etAttraction.getText().toString().trim())) {
            Utils.showOKDialog(mContext, "Please input attraction");
            return false;
        } else {
            mNewTourModel.setAttractions(etAttraction.getText().toString().trim());
        }
        String strInclustion = "";
        if (cbTaxes.isChecked()) {
            strInclustion = strInclustion + "0,";
        }
        if (cbTips.isChecked()) {
            strInclustion = strInclustion + "1,";
        }
        if (cbRoundTrip.isChecked()) {
            strInclustion = strInclustion + "2,";
            mNewTourModel.setSpecifiedCityIds(mactRoundTrip.getText().toString().trim());

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
            mNewTourModel.setInclusions(strInclustion.substring(0, strInclustion.length() - 2));
        }
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
            mNewTourModel.setLanguages(strFlag.substring(0, strFlag.length() - 2));

        }
        return true;
    }
    private String getLocationIdFromCityName(String cityNames) {
        String[] cities = spliteStringByComma(cityNames + " ");
        int cityCount = cities.length;
        String cityIds = "";
        for (int i = 0; i < cityCount; i ++ ) {
            for (int k = 0; k < arrCiies.length; k ++ ) {
                if (cities[i].equals(arrCiies[k])) {
                    cityIds = cityIds + arrLocation.get(k).getId() + ",";
                }
            }
        }
        if (cityIds.length() > 0) {
            return cityIds.substring(0, cityIds.length() - 2);
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
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {

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
                                tvTime.setText(strTimes[whichButton]);
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
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
                    Drawable d = Drawable.createFromPath(arrPhotoPathes.get(arrPhotoPathes.size() - 1));
//                    ImageView ivUser = (ImageView)findViewById(R.id.iv_register_user);
                    Drawable drawable = new BitmapDrawable(getResources(), MediaUtility.adjustBitmap(arrPhotoPathes.get(arrPhotoPathes.size() - 1)));
                    arrImageViews.get(pictureCount - 1).setImageDrawable(drawable);

                }
                break;
            case 1011:
                if (resultCode == Activity.RESULT_OK) {
                    pictureCount = arrPhotoPathes.size();
                    addNewImageview();
                    takePictureFromCameraManager.handleBigCameraPhoto();
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
