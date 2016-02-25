package com.allytours.view.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.app.FragmentManager;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.controller.Utilities.MediaUtility;
import com.allytours.controller.Utilities.Utils;
import com.allytours.view.camera.AlbumStorageDirFactory;
import com.allytours.view.camera.BaseAlbumDirFactory;
import com.allytours.view.camera.FroyoAlbumDirFactory;
import com.allytours.model.Constant;
import com.allytours.model.UserModel;
import com.allytours.view.HomeActivity;
import com.allytours.widget.SelectDateFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.CustomMultipartRequest;
import com.android.volley.request.CustomRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnClickListener{

    private static final int ACTION_TAKE_USER_PHOTO = 1;
    private static final int ACTION_TAKE_LICENSE_PHOTO = 2;

    private static final String JPEG_FILE_PREFIX = "AllyTour_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private Button ibBack, btnDone;
    private EditText etFullname, etEmail, etPassword, etCountryCode,etPhoneNumber, etBirthday,etCityCountry, etAddress;
    private ImageView ivUserPhoto, ivLicense;
    private TextView tvLicense;
    private RadioButton rbCustomer, rbOperator, rbMale, rbFemale;
    private LinearLayout llContainer1, llContainer2;
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
        return view;
    }

    private void initVariable() {
        mContext = getContext();

        userPhotoPath = "";
        licensePhotoPath = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }
    }
    private void initUI(View view) {
        ibBack = (Button)view.findViewById(R.id.ib_signup_back);
        ibBack.setOnClickListener(this);
        btnDone = (Button)view.findViewById(R.id.btn_signup_done);
        btnDone.setOnClickListener(this);

        btnCardScan = (Button)view.findViewById(R.id.btn_signup_card_scan);
        btnCardScan.setOnClickListener(this);

        etFullname = (EditText)view.findViewById(R.id.et_signup_fullname);
        etEmail = (EditText)view.findViewById(R.id.et_signup_email);
        etPassword = (EditText)view.findViewById(R.id.et_signup_password);
        etCountryCode = (EditText)view.findViewById(R.id.et_signup_country_code);
        etPhoneNumber = (EditText)view.findViewById(R.id.et_signup_phonenumber);
        etBirthday = (EditText)view.findViewById(R.id.et_signup_birthday);
        etBirthday.setOnClickListener(this);
        etCityCountry = (EditText)view.findViewById(R.id.et_signup_city_country);
        etAddress = (EditText)view.findViewById(R.id.et_signup_address);

        etCardNumber = (EditText)view.findViewById(R.id.et_signup_cardnumber);
        etCVV = (EditText)view.findViewById(R.id.et_signup_cvv);
        etExprieDate = (EditText)view.findViewById(R.id.et_signup_expire_date);

        ivUserPhoto = (ImageView)view.findViewById(R.id.iv_signup_userphoto);
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
        llCardInfo = (LinearLayout)view.findViewById(R.id.ll_signup_card_info);
        llCardInfoContainer = (LinearLayout)view.findViewById(R.id.ll_signup_card_info_container);
    }

    @Override
    public void onClick(View v) {
        if (v == ibBack) {
            ((HomeActivity)mContext).backToSingin();
        }
        if (v == btnDone) {
            if (checkValue()) {
                if (rbOperator.isChecked()) {
                    operatorSignupStep1();
                } else {
                    customerSignup();
//                    testSignup();
                }
            }
        }
        if (v == btnCardScan) {
            onScanPress();
        }
        if (v == rbCustomer) {
            llContainer1.setVisibility(View.GONE);
            llContainer2.setVisibility(View.GONE);
            etCityCountry.setVisibility(View.GONE);
            etAddress.setVisibility(View.GONE);
            llCardInfoContainer.setVisibility(View.VISIBLE);
        }
        if (v == rbOperator) {
            llContainer1.setVisibility(View.VISIBLE);
            llContainer2.setVisibility(View.VISIBLE);
            etCityCountry.setVisibility(View.VISIBLE);
            etAddress.setVisibility(View.VISIBLE);
            llCardInfoContainer.setVisibility(View.GONE);
        }
        if (v == ivUserPhoto) {
            showChooseDialog(mContext, "Choose picture from", ACTION_TAKE_USER_PHOTO);
        }
        if (v == ivLicense) {
            showChooseDialog(mContext, "Choose picture from", ACTION_TAKE_LICENSE_PHOTO);
        }
        if (v == etBirthday) {
            DialogFragment newFragment = new SelectDateFragment(etBirthday);
            newFragment.show(getFragmentManager(), "Birthday");
        }
    }
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
    private boolean checkValue() {
        userModel = new UserModel();
        userModel.setUserPhotoURL(userPhotoPath);
        if (TextUtils.isEmpty(etFullname.getText().toString()) || TextUtils.isEmpty(etEmail.getText().toString()) || TextUtils.isEmpty(etPassword.getText().toString())
                || TextUtils.isEmpty(etPhoneNumber.getText().toString()) || TextUtils.isEmpty(etCountryCode.getText().toString())
                 ) {
            Utils.showOKDialog(mContext, "Please input data");
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
        userModel.setPhoneNumber(etCountryCode.getText().toString() + etPhoneNumber.getText().toString());
        userModel.setUsertype("C");
        ////in case of operator
        if (rbOperator.isChecked()) {
            if ( TextUtils.isEmpty(etBirthday.getText().toString()) || TextUtils.isEmpty(etCityCountry.getText().toString()) || TextUtils.isEmpty(etAddress.getText().toString()) || TextUtils.isEmpty(licensePhotoPath)) {
                Utils.showOKDialog(mContext, "Please input data");
                return false;
            }
            userModel.setBirthday(etBirthday.getText().toString());
            userModel.setCity_country(etCityCountry.getText().toString());
            userModel.setAddress(etAddress.getText().toString());
            userModel.setLicensePhotoURL(licensePhotoPath);
            if (rbFemale.isChecked()) {
                userModel.setGender("F");
            } else if (rbMale.isChecked()) {
                userModel.setGender("M");
            }
            userModel.setUsertype("O");
        } else if (rbCustomer.isChecked()) {
            if (TextUtils.isEmpty(etCardNumber.getText().toString()) || TextUtils.isEmpty(etCVV.getText().toString()) || TextUtils.isEmpty(etExprieDate.getText().toString())) {
                Utils.showOKDialog(mContext, "Please input card information");
            } else {
                userModel.setCardNumber(etCardNumber.getText().toString());
                userModel.setCvv(etCVV.getText().toString());
                userModel.setExpireday(etExprieDate.getText().toString());
            }
        }
        return true;
    }
    private void operatorSignupStep1(){
        CustomMultipartRequest customMultipartRequest = new CustomMultipartRequest(API.SIGNUP_STEP1,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String success = response.getString("success");
                            if (success.equals("true")) {
                                Utils.setOnPreference(mContext, Constant.PHONE_NUMBER, userModel.getPhoneNumber());
                                mContext.startActivity(new Intent(mContext, PhoneVerifyFragment.class));
                            } else {
                                String reason = response.getString("reason");
                                if (reason.equals("401")) {
                                    Utils.showOKDialog(mContext, "Email is already registered");
                                }else if (reason.equals("402")) {
                                    Utils.showOKDialog(mContext, "Photo size is too big");
                                } else if (reason.equals("403")) {
                                    Utils.showOKDialog(mContext, "");
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
//                        Toast.makeText(MainActivity.this,error.toString(), Toast.LENGTH_LONG).show();
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
                .addFilePart("licensePhoto", licensePhotoPath);
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
    ///Sign up
    private void customerSignup() {

        CustomMultipartRequest customMultipartRequest = new CustomMultipartRequest(API.CUSTOMER_SIGNUP,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String success = response.getString("success");
                            if (success.equals("true")) {

                                Utils.setOnPreference(mContext, Constant.USER_PHOTO, response.getJSONObject("data").getString("photo"));
                                Utils.setOnPreference(mContext, Constant.ID, response.getJSONObject("data").getString("id"));

                                Utils.setOnPreference(mContext, Constant.USER_TYPE, Constant.USER_TYPE_CUSTOMER);
                                Utils.setOnPreference(mContext, Constant.PHONE_NUMBER, userModel.getPhoneNumber());
                                Utils.setOnPreference(mContext, Constant.EMAIL, userModel.getEmail());
                                Utils.setOnPreference(mContext, Constant.PASSWORD, userModel.getPassword());
                                Utils.setOnPreference(mContext, Constant.FULLNAME, userModel.getFullname());

                                if (HomeActivity.fromWhere == 0) {
                                    mContext.startActivity(new Intent(mContext, HomeActivity.class));
                                    ((HomeActivity) mContext).finish();
                                } else if (HomeActivity.fromWhere == 1) {
                                    ((HomeActivity) mContext).finishForPurchase(-1);
                                }

                            } else {
                                String reason = response.getString("reason");
                                if (reason.equals("401")) {
                                    Utils.showOKDialog(mContext, "Invalid parameter");
                                }else if (reason.equals("402")) {
                                    Utils.showOKDialog(mContext, "Failed validation check");
                                }else if (reason.equals("403")) {
                                    Utils.showOKDialog(mContext, "Email or phone number registered already");
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
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        customMultipartRequest
                .addStringPart("fullname", userModel.getFullname())
                .addStringPart("email", userModel.getEmail())
                .addStringPart("password", userModel.getPassword())
//                .addStringPart("card_number", userModel.getCardNumber())
//                .addStringPart("cvv", userModel.getCvv())
//                .addStringPart("expire_date", userModel.getExpireday())
                .addStringPart("phonenumber", userModel.getPhoneNumber());
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

    ///card scan
    private static final int MY_SCAN_REQUEST_CODE = 100; // arbitrary int
    public void onScanPress() {
        // This method is set up as an onClick handler in the layout xml
        // e.g. android:onClick="onScanPress"

        Intent scanIntent = new Intent(getContext(), CardIOActivity.class);

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
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = mContext.getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    userPhotoPath = cursor.getString(columnIndex);
                    cursor.close();

                    handleSmallCameraPhoto(data, ACTION_TAKE_USER_PHOTO);
//                    handleBigCameraPhoto();

                }
                break;
            } // ACTION_TAKE_USER_PHOTO

            case ACTION_TAKE_LICENSE_PHOTO: {
                if (resultCode == Activity.RESULT_OK) {
                    handleBigCameraPhoto();
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












    /////////////capture photo
    public void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch(actionCode) {
//            case ACTION_TAKE_USER_PHOTO:
//                File f = null;
//
//                try {
//                    f = setUpPhotoFile(ACTION_TAKE_USER_PHOTO);
//                    userPhotoPath = f.getAbsolutePath();
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    f = null;
//                    userPhotoPath = "";
//                }
//                break;

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
    private void handleBigCameraPhoto() {

        if (licensePhotoPath != null) {
            setPic();
            galleryAddPic();
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

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = ivLicense.getWidth();
        int targetH = ivLicense.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(licensePhotoPath, bmOptions);
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

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(licensePhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        ivLicense.setImageBitmap(bitmap);
        tvLicense.setVisibility(View.GONE);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(licensePhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }























}
