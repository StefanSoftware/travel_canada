package com.allytours.controller.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.model.API;
import com.allytours.utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.controller.SigninActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.CustomRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener{



    private Button btnToSignup, btnSignin;
    private EditText etEmail, etPassword;
    private TextView tvForgotPassword;
    private Context mContext;

    private Button btnFacebookSingin;

    //fb signin
    CallbackManager callbackManager;
    AccessToken fbAccessToken ;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_signin, container, false);

        initVariable();
        initUI(view);

        return view;
    }
    private void initVariable() {
        mContext = getActivity();
//        String keyhash = SocialUtility.printKeyHash(mContext);

        initFBLogin();

    }
    //////////////for fb login
    private void initFBLogin() {
        //////////////////////////////////FACEBOOK LOGIN==start
        FacebookSdk.sdkInitialize(mContext);
        //get current token
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                Profile.fetchProfileForCurrentAccessToken();
                final LoginResult result = loginResult;

                fbAccessToken = loginResult.getAccessToken();

                Log.d("username===============", loginResult.toString());
                Log.d("success", "==========================");

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                                Log.v("result======", graphResponse.toString());
                                try {
                                    ///get facebook profile data
                                    String email = jsonObject.getString("email");
//                                    String name = jsonObject.getString("name");
//                                    String id = jsonObject.getString("id");
//                                    String photo = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                                    String access_token = loginResult.getAccessToken().toString();

                                    AccessToken currentAccestoken = AccessToken.getCurrentAccessToken();
                                    access_token = currentAccestoken.getToken();
                                    // save fb profile data as preference
                                    Utils.setOnPreference(mContext, Constant.FB_ACCESS_TOKEN, access_token);
//                                    Utils.setOnPreference(mContext, Constant.FB_ID, id);
//                                    Utils.setOnPreference(mContext, Constant.FB_NAME, name);
                                    Utils.setOnPreference(mContext, Constant.FB_EMAIL, email);
//                                    Utils.setOnPreference(mContext, Constant.FB_PHOTO, photo);
                                    fbSignIn();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday,first_name,last_name,age_range,picture.type(normal)");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
//                Utilitie.showToast(mContext, "Facebook login failed!");
                AccessToken.setCurrentAccessToken(null);
            }

            @Override
            public void onError(FacebookException e) {
                AccessToken.setCurrentAccessToken(null);
            }
        });
    }
    public void checkToSigninFacebook() {
        if (Utils.haveNetworkConnection(mContext)) {
            String accessToken = Utils.getFromPreference(mContext, Constant.FB_ACCESS_TOKEN);
            if(accessToken.equals("")){
                try{
                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, email, user_photos"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                fbSignIn();
            }
        } else {

            Utils.showOKDialog(mContext, "No internet access");
        }
//
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void initUI(View view) {
        btnToSignup = (Button)view.findViewById(R.id.btn_to_signup);
        btnToSignup.setOnClickListener(this);
        btnSignin = (Button)view.findViewById(R.id.btn_signin);
        btnSignin.setOnClickListener(this);

        btnFacebookSingin = (Button)view.findViewById(R.id.btn_facebook_signin);
        btnFacebookSingin.setOnClickListener(this);

        etEmail = (EditText)view.findViewById(R.id.et_signin_email);
        final Drawable originalDrawable = etEmail.getBackground();
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etEmail.getText().toString().trim().length() == 0) {
                        etEmail.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etEmail.setBackground(originalDrawable);

                }
            }
        });
        etPassword = (EditText)view.findViewById(R.id.et_signin_password);
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etPassword.getText().toString().trim().length() == 0) {
                        etPassword.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etPassword.setBackground(originalDrawable);

                }
            }
        });
        tvForgotPassword = (TextView)view.findViewById(R.id.tv_signin_forgot_pass);
        tvForgotPassword.setOnClickListener(this);

        ((SigninActivity)mContext).setTitle(Constant.TITLE_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        if (v == btnToSignup) {
            ((SigninActivity)mContext).pushFragment(1);


        }
        if (v == btnSignin) {
          signin();
        }
        if (v == btnFacebookSingin) {
            checkToSigninFacebook();
        }
        if (v == tvForgotPassword) {
            forgotPassword();
        }
    }
//    fb sign in
    private void fbSignIn(){
        String email = Utils.getFromPreference(mContext, Constant.FB_EMAIL);

        if (email.length() == 0) {
        } else {
            Utils.showProgress(mContext);
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);

            CustomRequest signinRequest = new CustomRequest(Request.Method.POST, API.FB_SINGIN, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.hideProgress();
                            try {
                                String success = response.getString("success");
                                if (success.equals("true")) {

                                    JSONObject jsonObject = response.getJSONObject("data");

                                    String userType = jsonObject.getString("usertype");
                                    String fullname = jsonObject.getString("fullname");
                                    String email = jsonObject.getString("email");
                                    String id = jsonObject.getString("id");
                                    String phonenumber = jsonObject.getString("phonenumber");
                                    String userphoto = jsonObject.getString("userphoto");

                                    Utils.setOnPreference(mContext, Constant.USER_TYPE, userType);
                                    Utils.setOnPreference(mContext, Constant.EMAIL, email);
                                    Utils.setOnPreference(mContext, Constant.FULLNAME, fullname);
                                    Utils.setOnPreference(mContext, Constant.USER_ID, id);
                                    Utils.setOnPreference(mContext, Constant.USER_PHOTO, userphoto);

                                    if (userType.equals(Constant.USER_TYPE_OPERATOR)) {
                                        String country_city = jsonObject.getString("country_city");
                                        String address = jsonObject.getString("address");
                                        String gender = jsonObject.getString("gender");

                                        String licensePhoto = jsonObject.getString("licensephoto");


                                        Utils.setOnPreference(mContext, Constant.PHONE_NUMBER, phonenumber);
                                        Utils.setOnPreference(mContext, Constant.COUNTRY_CITY, country_city);
                                        Utils.setOnPreference(mContext, Constant.ADDRESS, address);
                                        Utils.setOnPreference(mContext, Constant.GENDER, gender);

                                        Utils.setOnPreference(mContext, Constant.LICENSE_PHOTO, licensePhoto);
                                    }

                                    getActivity().setResult(Activity.RESULT_OK);
                                    getActivity().finish();


                                } else {
                                    String reason = response.getString("reason");
                                    if (reason.equals("401")) {
                                        Utils.showOKDialog(mContext, "Please sign up first");
                                    } else if (reason.equals("402")) {
                                        Utils.showOKDialog(mContext, "Please sign up first");
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
    ///Sign in
    private void signin() {
        String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        if (email.length() == 0 ) {
            Utils.showOKDialog(mContext, "Please input email");
            return;
        } else if (!Utils.isEmailValid(email)) {
            Utils.showOKDialog(mContext, "Please input correct email");
            return;
        }
        else if (password.length() == 0) {
            Utils.showOKDialog(mContext, "Please input password");
            return;
        }
        {

            Utils.showProgress(mContext);

            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);
            params.put("password", password);

            CustomRequest signinRequest = new CustomRequest(Request.Method.POST, API.SINGIN, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.hideProgress();
                            try {
                                String success = response.getString("success");
                                if (success.equals("true")) {

                                    JSONObject jsonObject = response.getJSONObject("data");

                                    String userType = jsonObject.getString("usertype");
                                    String fullname = jsonObject.getString("fullname");
                                    String email = jsonObject.getString("email");
                                    String id = jsonObject.getString("id");
                                    String phonenumber = jsonObject.getString("phonenumber");
                                    String userphoto = jsonObject.getString("userphoto");

                                    Utils.setOnPreference(mContext, Constant.USER_TYPE, userType);
                                    Utils.setOnPreference(mContext, Constant.EMAIL, email);
                                    Utils.setOnPreference(mContext, Constant.PASSWORD, password);
                                    Utils.setOnPreference(mContext, Constant.FULLNAME, fullname);
                                    Utils.setOnPreference(mContext, Constant.USER_ID, id);
                                    Utils.setOnPreference(mContext, Constant.USER_PHOTO, userphoto);



                                    if (userType.equals(Constant.USER_TYPE_OPERATOR)) {
                                        String country_city = jsonObject.getString("country_city");
                                        String address = jsonObject.getString("address");
                                        String gender = jsonObject.getString("gender");

                                        String licensePhoto = jsonObject.getString("licensephoto");


                                        Utils.setOnPreference(mContext, Constant.PHONE_NUMBER, phonenumber);
                                        Utils.setOnPreference(mContext, Constant.COUNTRY_CITY, country_city);
                                        Utils.setOnPreference(mContext, Constant.ADDRESS, address);
                                        Utils.setOnPreference(mContext, Constant.GENDER, gender);

                                        Utils.setOnPreference(mContext, Constant.LICENSE_PHOTO, licensePhoto);
                                    } else if (userType.equals(Constant.USER_TYPE_CUSTOMER)) {
//                                        String cardnumber = jsonObject.getString("cardnumber");
//                                        String cvv = jsonObject.getString("cvv");
//                                        String expireday = jsonObject.getString("expireday");
//
//                                        Utils.setOnPreference(mContext, Constant.CARD_NUMBER, cardnumber);
//                                        Utils.setOnPreference(mContext, Constant.CVV, cvv);
//                                        Utils.setOnPreference(mContext, Constant.EXPIRE_DAY, expireday);
                                    }


                                    getActivity().setResult(Activity.RESULT_OK);
                                    getActivity().finish();
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
    private void forgotPassword() {
        String email = etEmail.getText().toString();

        if (email.length() == 0 ) {
            Utils.showOKDialog(mContext, "Please input email");
        } else {

            Utils.showProgress(mContext);

            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);

            CustomRequest signinRequest = new CustomRequest(Request.Method.POST, API.FORGOT_PASSWORD, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.hideProgress();
                            try {
                                String success = response.getString("success");
                                if (success.equals("true")) {
                                    Utils.showOKDialog(mContext, "We sent new password to your email");

                                } else {
                                    String reason = response.getString("reason");
                                    if (reason.equals("401")) {
                                        Utils.showOKDialog(mContext, "Email is unregistered");
                                    } else if (reason.equals("402")) {
                                        Utils.showOKDialog(mContext, "Email address does not exist");
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

}
