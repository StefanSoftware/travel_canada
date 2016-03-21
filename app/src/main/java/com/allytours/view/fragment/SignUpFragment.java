package com.allytours.view.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.controller.Helpers.InitHelper;
import com.allytours.utilities.TimeZoneHelper;
import com.allytours.utilities.MediaUtility;
import com.allytours.utilities.TimeUtility;
import com.allytours.utilities.UIUtility;
import com.allytours.utilities.Utils;
import com.allytours.utilities.GPSTracker;
import com.allytours.utilities.camera.AlbumStorageDirFactory;
import com.allytours.utilities.camera.BaseAlbumDirFactory;
import com.allytours.utilities.camera.FroyoAlbumDirFactory;
import com.allytours.model.Constant;
import com.allytours.model.UserModel;
import com.allytours.view.SigninActivity;
import com.allytours.widget.MyCircularImageView;
import com.allytours.widget.SelectDateFragment;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.CustomMultipartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener {

    private static final int ACTION_TAKE_USER_PHOTO = 1;
    private static final int ACTION_TAKE_LICENSE_PHOTO = 2;

    private static final String JPEG_FILE_PREFIX = "AllyTour_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private Button  btnDone;
    private EditText etFullname, etEmail, etPassword,etReenterPassword, etCountryCode,etPhoneNumber,etSSN, etBirthday, etAddress;
    private AutoCompleteTextView actv;
    private ImageView  ivLicense;
    private MyCircularImageView ivUserPhoto;
    private TextView tvLicense;
    private RadioButton rbCustomer, rbOperator, rbMale, rbFemale;
    private LinearLayout llContainer1, llContainer2, llContainerOperatorOptions;
    private EditText etCardNumber, etCVV, etExprieDate;
    private Button btnCardScan;
    private LinearLayout llCardInfoContainer, llCardInfo;

    private Context mContext;

    private UserModel userModel;

    String userPhotoPath, licensePhotoPath;

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_sign_up, container, false);

        initVariable();
        initUI(view);
        getTimeZoneOffset();
        return view;
    }

    ///init variables
    private void initVariable() {
        mContext = getActivity();

        userPhotoPath = "";
        licensePhotoPath = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
    }
    ////init UI
    private void initUI(View view) {
        btnDone = (Button)view.findViewById(R.id.btn_signup_done);
        btnDone.setOnClickListener(this);

        btnCardScan = (Button)view.findViewById(R.id.btn_signup_card_scan);
        btnCardScan.setOnClickListener(this);

        etFullname = (EditText)view.findViewById(R.id.et_signup_fullname);
        final Drawable originalDrawable = etFullname.getBackground();
        etFullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etFullname.getText().toString().trim().length() == 0) {
                        etFullname.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etFullname.setBackgroundColor(getResources().getColor(R.color.transparent));

                }
            }
        });

        etEmail = (EditText)view.findViewById(R.id.et_signup_email);
        etEmail.requestFocus();
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etEmail.getText().toString().trim().length() == 0) {
                        etEmail.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etEmail.setBackgroundColor(getResources().getColor(R.color.transparent));
//                    etEmail.setBackground(getResources().getDrawable(android.R.drawable.edit_text));
                }
            }
        });

        etPassword = (EditText)view.findViewById(R.id.et_signup_password);

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etPassword.getText().toString().trim().length() == 0) {
                        etPassword.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etPassword.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        etReenterPassword = (EditText)view.findViewById(R.id.et_signup_retype_password);

        etReenterPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etReenterPassword.getText().toString().trim().length() == 0) {
                        etReenterPassword.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etReenterPassword.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        etCountryCode = (EditText)view.findViewById(R.id.et_signup_country_code);
        etCountryCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etCountryCode.getText().toString().trim().length() == 0) {
                        etCountryCode.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etCountryCode.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        etPhoneNumber = (EditText)view.findViewById(R.id.et_signup_phonenumber);
        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etPhoneNumber.getText().toString().trim().length() < 10) {
                        etPhoneNumber.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etPhoneNumber.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        etSSN = (EditText)view.findViewById(R.id.et_signup_ssn);
        etSSN.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etSSN.getText().toString().trim().length() < 9) {
                        etSSN.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etSSN.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });

        etBirthday = (EditText)view.findViewById(R.id.et_signup_birthday);
        etBirthday.setOnClickListener(this);
//        etCityCountry = (EditText)view.findViewById(R.id.et_signup_city_country);

        etAddress = (EditText)view.findViewById(R.id.et_signup_address);
        etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etAddress.getText().toString().trim().length() == 0) {
                        etAddress.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etAddress.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });


        etCardNumber = (EditText)view.findViewById(R.id.et_signup_cardnumber);
        etCVV = (EditText)view.findViewById(R.id.et_signup_cvv);
        etExprieDate = (EditText)view.findViewById(R.id.et_signup_expire_date);

        ivUserPhoto = (MyCircularImageView)view.findViewById(R.id.iv_signup_userphoto);
        ivUserPhoto.setOnClickListener(this);
        ivLicense = (ImageView)view.findViewById(R.id.iv_signup_license);
        ivLicense.setOnClickListener(this);

        tvLicense = (TextView)view.findViewById(R.id.tv_signup_license);
        tvLicense.setVisibility(View.VISIBLE);

        rbCustomer = (RadioButton)view.findViewById(R.id.rb_signup_customer);
        rbCustomer.setOnClickListener(this);
        rbOperator = (RadioButton)view.findViewById(R.id.rb_signup_operator);
        rbOperator.setOnClickListener(this);

        rbMale = (RadioButton)view.findViewById(R.id.rb_signup_male);
        rbFemale = (RadioButton)view.findViewById(R.id.rb_signup_female);

        llContainer1 = (LinearLayout)view.findViewById(R.id.ll_signup_container1);
        llContainer2 = (LinearLayout)view.findViewById(R.id.ll_signup_container2);
        llContainerOperatorOptions = (LinearLayout)view.findViewById(R.id.ll_signup_operator_options);
        llCardInfo = (LinearLayout)view.findViewById(R.id.ll_signup_card_info);
        llCardInfoContainer = (LinearLayout)view.findViewById(R.id.ll_signup_card_info_container);

        ((SigninActivity)mContext).setTitle(Constant.TITLE_SIGN_UP_1);
        initAutoCompleteTextView(view);
    }

    private void initAutoCompleteTextView(View view) {
        String[] arrCountry = getResources().getStringArray(R.array.country_state_city);
        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_item, arrCountry);
        //Getting the instance of AutoCompleteTextView
        actv= (AutoCompleteTextView)view.findViewById(R.id.sp_signup_city_country);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (actv.getText().toString().trim().length() == 0) {
                        actv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    actv.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });
    }
    Activity mActivity;
    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }

    public void onClick(View v) {
        if (!(v instanceof EditText)) {
            if (UIUtility.keyboardShown(mActivity)) {
                UIUtility.hideSoftKeyboard(mActivity);
            }
        }

        if (v == btnDone) {
            if (checkValue()) {
                if (rbOperator.isChecked()) {
                    operatorSignupStep1();
//                    if (Utils.getFromPreference(mContext, Constant.PHONE_NUMBER).length() > 0) {
//                        showVerifyDialog();
//                    } else {
//
//                    }

                } else {
                    customerSignup();
                }
            }
        }
        if (v == btnCardScan) {
            onScanPress();
        }
        if (v == rbCustomer) {
            llContainer2.setVisibility(View.GONE);
            llContainerOperatorOptions.setVisibility(View.GONE);
            llCardInfoContainer.setVisibility(View.VISIBLE);
            ((SigninActivity)mContext).setTitle(Constant.TITLE_SIGN_UP);
        }
        if (v == rbOperator) {
            llContainer2.setVisibility(View.VISIBLE);
            llContainerOperatorOptions.setVisibility(View.VISIBLE);
            llCardInfoContainer.setVisibility(View.GONE);
            ((SigninActivity)mContext).setTitle(Constant.TITLE_SIGN_UP_1);
        }
        if (v == ivUserPhoto) {
            showChooseDialog(mContext, "Choose picture from", ACTION_TAKE_USER_PHOTO);
        }
        if (v == ivLicense) {
            showChooseDialog(mContext, "Choose picture from", ACTION_TAKE_LICENSE_PHOTO);
        }
        if (v == etBirthday) {
            UIUtility.hideSoftKeyboard(mActivity);
            DialogFragment newFragment = new SelectDateFragment(etBirthday);
            newFragment.show(getFragmentManager(), "Birthday");
        }
    }

    ///check values to sign up
    private boolean checkValue() {
        userModel = new UserModel();
        userModel.setUserPhotoURL(userPhotoPath);

        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            Utils.showOKDialog(mContext, "Please input email");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            Utils.showOKDialog(mContext, "Please input password");
            return false;
        }
        if (TextUtils.isEmpty(etReenterPassword.getText().toString())) {
            Utils.showOKDialog(mContext, "Please reenter password");
            return false;
        }
        if (!etReenterPassword.getText().toString().equals(etPassword.getText().toString())) {
            Utils.showOKDialog(mContext, "Passwords are not matching");
            return false;
        }
        if (TextUtils.isEmpty(etFullname.getText().toString()) ) {
            Utils.showOKDialog(mContext, "Please input full name");
            return false;
        }
        if (TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
            Utils.showOKDialog(mContext, "Please input phone number");
            return false;
        }
        if (etPhoneNumber.getText().toString().trim().length() < 10) {
            Utils.showOKDialog(mContext, "Phone number must be 10 digits");
            return false;
        }
        if (TextUtils.isEmpty(etCountryCode.getText().toString())) {
            Utils.showOKDialog(mContext, "Please input country code");
            return false;
        }


        String email = etEmail.getText().toString().trim();
        if ( !Utils.isEmailValid(email)) {
            Utils.showOKDialog(mContext, "Invalid email");
            return false;
        }
        userModel.setFullname(etFullname.getText().toString());
        userModel.setEmail(etEmail.getText().toString());
        userModel.setPassword(etPassword.getText().toString());
        userModel.setPhoneNumber("+" + etCountryCode.getText().toString() + etPhoneNumber.getText().toString());
        userModel.setUsertype("C");
        ////in case of operator
        if (rbOperator.isChecked()) {
            if (TextUtils.isEmpty(etSSN.getText().toString())) {
                Utils.showOKDialog(mContext, "Please input SSN/SIN number");
                return false;
            }
            if (etSSN.getText().toString().trim().length() < 9) {
                Utils.showOKDialog(mContext, "SSN/SIN number must be 9 digits");
                return false;
            }
            if (TextUtils.isEmpty(etBirthday.getText().toString())) {
                Utils.showOKDialog(mContext, "Please input birthday");
                return false;
            }
            if (TextUtils.isEmpty(actv.getText().toString())) {
                Utils.showOKDialog(mContext, "Please input city/country");
                return false;
            }
            if (TextUtils.isEmpty(etAddress.getText().toString())) {
                Utils.showOKDialog(mContext, "Please input address");
                return false;
            }
            if (  TextUtils.isEmpty(licensePhotoPath)) {
                Utils.showOKDialog(mContext, "Please input license photo");
                return false;
            }
            userModel.setSsn_sin(etSSN.getText().toString());
            userModel.setBirthday(etBirthday.getText().toString());
            userModel.setCity_country(actv.getText().toString());
            userModel.setAddress(etAddress.getText().toString());
            userModel.setLicensePhotoURL(licensePhotoPath);
            if (rbFemale.isChecked()) {
                userModel.setGender("F");
            } else if (rbMale.isChecked()) {
                userModel.setGender("M");
            }
            userModel.setUsertype("O");
        } else if (rbCustomer.isChecked()) {///in case of customer
            if (TextUtils.isEmpty(etCardNumber.getText().toString()) || TextUtils.isEmpty(etCVV.getText().toString()) || TextUtils.isEmpty(etExprieDate.getText().toString())) {
                Utils.showOKDialog(mContext, "Please input card information");
                return false;
            } else {
//                userModel.setCardNumber(etCardNumber.getText().toString());
//                userModel.setCvv(etCVV.getText().toString());
                String strExpireDate = etExprieDate.getText().toString().trim();
                userModel.setExpireday_month(strExpireDate.substring(0, strExpireDate.lastIndexOf("/")));
                userModel.setExpireday_year(strExpireDate.substring(strExpireDate.lastIndexOf("/") + 1));
                //for test
                userModel.setCardNumber("4242424242424242");
                userModel.setCvv("314");


            }
        }
        String offset = Utils.getFromPreference(mContext, "rawOffset");
        offset = TimeUtility.getHourFromTimeStamp(offset);
        userModel.setOffset(TimeUtility.getOffset());
        return true;
    }
    ///operator sign up step 1
    private void operatorSignupStep1(){
        Utils.showProgress(mContext);
        CustomMultipartRequest customMultipartRequest = new CustomMultipartRequest(API.SIGNUP_STEP1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.hideProgress();
                        try {
                            String success = response.getString("success");
                            if (success.equals("true")) {
                                Utils.setOnPreference(mContext, Constant.USER_PHOTO, response.getJSONObject("data").getString("photo"));
                                Utils.setOnPreference(mContext, Constant.USER_ID, response.getJSONObject("data").getString("id"));
                                Utils.setOnPreference(mContext, Constant.VERIFY_CODE, response.getJSONObject("data").getString("code"));

                                Utils.setOnPreference(mContext, Constant.PHONE_NUMBER, userModel.getPhoneNumber());
                                Utils.setOnPreference(mContext, Constant.EMAIL, userModel.getEmail());
                                Utils.setOnPreference(mContext, Constant.PASSWORD, userModel.getPassword());
                                Utils.setOnPreference(mContext, Constant.FULLNAME, userModel.getFullname());

                                Utils.setOnPreference(mContext, Constant.SSN_SIN, userModel.getSsn_sin());
                                Utils.setOnPreference(mContext, Constant.COUNTRY_CITY, userModel.getCity_country());
                                Utils.setOnPreference(mContext, Constant.ADDRESS, userModel.getAddress());
                                Utils.setOnPreference(mContext, Constant.GENDER, userModel.getGender());
                                Utils.setOnPreference(mContext, Constant.BIRTHDAY, userModel.getBirthday());

                                showVerifyDialog();
                            } else {
                                String reason = response.getString("reason");
                                if (reason.equals("402")) {
                                    Utils.showOKDialog(mContext, "Email is already registered");
                                }else if (reason.equals("401")) {
                                    Utils.showOKDialog(mContext, "Invalid phone number");
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
                        Toast.makeText(mContext,error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        customMultipartRequest
                .addStringPart("fullname", userModel.getFullname())
                .addStringPart("email", userModel.getEmail())
                .addStringPart("password", userModel.getPassword())
                .addStringPart("phonenumber", userModel.getPhoneNumber())
                .addStringPart("country_city", userModel.getCity_country())
                .addStringPart("address", userModel.getAddress())
                .addStringPart("birthday", userModel.getBirthday())
                .addStringPart("ssn", userModel.getSsn_sin())
                .addStringPart("gender", userModel.getGender())
                .addStringPart("offset", userModel.getOffset())
                .addFilePart("licensephoto", licensePhotoPath);
        if (userPhotoPath.length() > 0) {
            customMultipartRequest
                    .addFilePart("userphoto", userPhotoPath);
//                    .addStringPart("is_userphoto", "true");
        } else {
//            customMultipartRequest.addStringPart("is_userphoto", "false");
        }

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(customMultipartRequest);


    }
    ///customer Sign up
    private void customerSignup() {
        Utils.showProgress(mContext);
        CustomMultipartRequest customMultipartRequest = new CustomMultipartRequest(API.CUSTOMER_SIGNUP,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.hideProgress();
                        try {
                            String success = response.getString("success");
                            if (success.equals("true")) {

                                Utils.setOnPreference(mContext, Constant.USER_PHOTO, response.getJSONObject("data").getString("photo"));
                                Utils.setOnPreference(mContext, Constant.USER_ID, response.getJSONObject("data").getString("id"));

                                Utils.setOnPreference(mContext, Constant.USER_TYPE, Constant.USER_TYPE_CUSTOMER);
                                Utils.setOnPreference(mContext, Constant.PHONE_NUMBER, userModel.getPhoneNumber());
                                Utils.setOnPreference(mContext, Constant.EMAIL, userModel.getEmail());
                                Utils.setOnPreference(mContext, Constant.PASSWORD, userModel.getPassword());
                                Utils.setOnPreference(mContext, Constant.FULLNAME, userModel.getFullname());

                                Utils.setOnPreference(mContext, Constant.CARD_NUMBER, userModel.getCardNumber());
                                Utils.setOnPreference(mContext, Constant.CVV, userModel.getCvv());
                                Utils.setOnPreference(mContext, Constant.EXPIRE_MONTH, userModel.getExpireday_month());
                                Utils.setOnPreference(mContext, Constant.EXPIRE_YEAR, userModel.getExpireday_year());

                                mActivity.setResult(Activity.RESULT_OK);
                                mActivity.finish();

                            } else {
                                String reason = response.getString("reason");
                                if (reason.equals("401")) {
                                    Utils.showOKDialog(mContext, response.getString("comment"));
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
                .addStringPart("fullname", userModel.getFullname())
                .addStringPart("email", userModel.getEmail())
                .addStringPart("password", userModel.getPassword())
                .addStringPart("card_number", userModel.getCardNumber())
                .addStringPart("cvv", userModel.getCvv())
                .addStringPart("expire_date_year", userModel.getExpireday_year())
                .addStringPart("expire_date_month", userModel.getExpireday_month())
                .addStringPart("phonenumber", userModel.getPhoneNumber())
                .addStringPart("offset", userModel.getOffset());

        if (userPhotoPath.length() > 0) {
            customMultipartRequest
                    .addFilePart("userphoto", userPhotoPath);
//                    .addStringPart("is_userphoto", "true");
        } else {
//            customMultipartRequest.addStringPart("is_userphoto", "false");
        }

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(customMultipartRequest);

    }

    private void getTimeZoneOffset() {
        GPSTracker gpsTracker = new GPSTracker(mContext);
        String currentTime = TimeUtility.getCurrentTimeStamp();
        TimeZoneHelper timeZoneHelper = new TimeZoneHelper(mContext, String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()), currentTime);
        timeZoneHelper.getTimeZone();
    }

    //show phone verify dialog
    private void showVerifyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Enter the verification code");

        // Set up the input
        final EditText input = new EditText(mContext);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType( InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                verify(input.getText().toString().trim());
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InitHelper.initPreference(mContext);
                dialog.cancel();
            }
        });

        builder.show();
    }
    ///do verify
    private void verify(String verifyCode) {

        if (verifyCode.equals(Utils.getFromPreference(mContext, Constant.VERIFY_CODE))) {
            Utils.setOnPreference(mContext, Constant.VERIFY_CODE, "");
            ((SigninActivity) mContext).pushFragment(2);
        } else {
            Utils.showOKDialog(mContext, "Verification code is incorrect");
        }


    }


    ///card scan
    private static final int MY_SCAN_REQUEST_CODE = 100; // arbitrary int
    public void onScanPress() {
        // This method is set up as an onClick handler in the layout xml
        // e.g. android:onClick="onScanPress"

        Intent scanIntent = new Intent(mContext, CardIOActivity.class);

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, false); // default: false

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }
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
                    userPhotoPath = cursor.getString(columnIndex);
                    cursor.close();


                    //convert bitmap to drawable
                    Drawable d = Drawable.createFromPath(userPhotoPath);
//                    ImageView ivUser = (ImageView)findViewById(R.id.iv_register_user);
                    Drawable drawable = new BitmapDrawable(getResources(), MediaUtility.adjustBitmap(userPhotoPath));
                    ivUserPhoto.setImageDrawable(drawable);

                }
                break;

            case 1011:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = mContext.getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    licensePhotoPath = cursor.getString(columnIndex);

                    cursor.close();


                    //convert bitmap to drawable
                    Drawable d = Drawable.createFromPath(licensePhotoPath);
//                    ImageView ivUser = (ImageView)findViewById(R.id.iv_register_user);
                    Drawable drawable = new BitmapDrawable(getResources(), MediaUtility.adjustBitmap(licensePhotoPath));
                    ivLicense.setImageDrawable(drawable);
                    tvLicense.setVisibility(View.GONE);

                }
                break;

            case ACTION_TAKE_USER_PHOTO: {
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri selectedImage = data.getData();
//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                    Cursor cursor = mContext.getContentResolver().query(
//                            selectedImage, filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    userPhotoPath = cursor.getString(columnIndex);
//                    cursor.close();
//
//                    handleSmallCameraPhoto(data, ACTION_TAKE_USER_PHOTO);
////                    handleBigCameraPhoto();
//
//                }
                if (resultCode == Activity.RESULT_OK) {
                    handleBigCameraPhoto(ACTION_TAKE_USER_PHOTO);
                }
                break;
            } // ACTION_TAKE_USER_PHOTO

            case ACTION_TAKE_LICENSE_PHOTO: {
                if (resultCode == Activity.RESULT_OK) {
                    handleBigCameraPhoto(ACTION_TAKE_LICENSE_PHOTO);
                }
                break;
            } // ACTION_TAKE_LICENSE_PHOTO

            case MY_SCAN_REQUEST_CODE:
                String resultStr;
                if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                    CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                    llCardInfo.setVisibility(View.VISIBLE);
                    // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                    resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                    etCardNumber.setText(scanResult.getRedactedCardNumber());

                    // Do something with the raw number, e.g.:
                    // myService.setCardNumber( scanResult.cardNumber );

                    if (scanResult.isExpiryValid()) {
                        resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                        etExprieDate.setText(scanResult.expiryMonth + "/" + scanResult.expiryYear);
                    }

                    if (scanResult.cvv != null) {
                        // Never log or display a CVV
                        resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                        etCVV.setText(scanResult.cvv);
                    }

                    if (scanResult.postalCode != null) {
                        resultStr += "Postal Code: " + scanResult.postalCode + "\n";
                    }

                    if (scanResult.cardholderName != null) {
                        resultStr += "Cardholder Name : " + scanResult.cardholderName + "\n";
                    }

                } else {
                    resultStr = "Scan was canceled.";
                }
//                resultTextView.setText(resultStr);
                break;
        }


    }











    ///photo choose dialog
    public void showChooseDialog(Context context, String message, final int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Constant.INDECATOR);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dispatchTakePictureIntent(type);
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Gallary",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        takePictureFromGallery(type);
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
    private void takePictureFromGallery(int type)
    {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        if (type == ACTION_TAKE_USER_PHOTO) {
            startActivityForResult(photoPickerIntent, 1010);
        } else if (type == ACTION_TAKE_LICENSE_PHOTO) {
            startActivityForResult(photoPickerIntent, 1011);
        }

    }

    /////////////capture photo
    public void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch(actionCode) {
            case ACTION_TAKE_USER_PHOTO:
                File f = null;

                try {
                    f = setUpPhotoFile(ACTION_TAKE_USER_PHOTO);
                    userPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    userPhotoPath = "";
                }
                break;

            case ACTION_TAKE_LICENSE_PHOTO:
                File f1 = null;

                try {
                    f1 = setUpPhotoFile(ACTION_TAKE_LICENSE_PHOTO);
                    licensePhotoPath = f1.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f1));
                } catch (IOException e) {
                    e.printStackTrace();
                    f1 = null;
                    licensePhotoPath = "";
                }
                break;
            default:
                break;
        } // switch

        startActivityForResult(takePictureIntent, actionCode);
    }
    private File setUpPhotoFile(int type) throws IOException {

        File f = createImageFile();
        switch (type) {
            case ACTION_TAKE_USER_PHOTO:
                userPhotoPath = f.getAbsolutePath();
                break;
            case ACTION_TAKE_LICENSE_PHOTO:
                licensePhotoPath = f.getAbsolutePath();
                break;
        }


        return f;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }
    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("AllyTours");

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    ///process result of captured photo
    private void handleBigCameraPhoto(int type) {

        if (licensePhotoPath != null) {
            setPic(type);
//            galleryAddPic();
        }

    }
    private void handleSmallCameraPhoto(Intent intent, int type) {
        switch (type) {
            case ACTION_TAKE_USER_PHOTO:
                Bundle extras = intent.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                ivUserPhoto.setImageBitmap(bitmap);
                break;
            case ACTION_TAKE_LICENSE_PHOTO:
                Bundle extras1 = intent.getExtras();
                Bitmap bitmap1 = (Bitmap) extras1.get("data");
                ivLicense.setImageBitmap(bitmap1);
                break;
        }

    }

    private void setPic(int type) {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = ivLicense.getWidth();
        int targetH = ivLicense.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        switch (type) {
            case 1:
                BitmapFactory.decodeFile(userPhotoPath, bmOptions);
                break;
            case 2:
                BitmapFactory.decodeFile(licensePhotoPath, bmOptions);
                break;
        }

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		Bitmap bitmap = null;

		/* Associate the Bitmap to the ImageView */
        switch (type) {
            case 1:
                /* Decode the JPEG file into a Bitmap */
                bitmap = BitmapFactory.decodeFile(userPhotoPath, bmOptions);
//                bitmap = MediaUtility.rotateImage(bitmap, 90);
                ivUserPhoto.setImageBitmap(bitmap);
                break;
            case 2:
                /* Decode the JPEG file into a Bitmap */
                bitmap = BitmapFactory.decodeFile(licensePhotoPath, bmOptions);
                ivLicense.setImageBitmap(bitmap);
                tvLicense.setVisibility(View.GONE);
                break;

        }

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(licensePhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }



}
