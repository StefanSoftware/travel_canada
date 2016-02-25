package com.allytours.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.controller.Utilities.SocialUtility;
import com.allytours.controller.Utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.view.HomeActivity;
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
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.card.payment.CardIOActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment implements View.OnClickListener{



    private Button btnToSignup, btnSignin;
    private EditText etEmail, etPassword;
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
        mContext = getContext();
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
        etPassword = (EditText)view.findViewById(R.id.et_signin_password);
    }

    @Override
    public void onClick(View v) {
        if (v == btnToSignup) {
            if (Utils.getFromPreference(mContext, Constant.PHONE_NUMBER).length() > 0) {
                ((HomeActivity)mContext).goToPhoneVerify();
            } else {
                ((HomeActivity)mContext).goToSignup();
            }

        }
        if (v == btnSignin) {
          signin();
        }
        if (v == btnFacebookSingin) {
            checkToSigninFacebook();
        }
    }
//    fb sign in
    private void fbSignIn(){
        String email = Utils.getFromPreference(mContext, Constant.FB_EMAIL);

        if (email.length() == 0) {
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);

            CustomRequest signinRequest = new CustomRequest(Request.Method.POST, API.FB_SINGIN, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
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
                                    Utils.setOnPreference(mContext, Constant.ID, id);
                                    Utils.setOnPreference(mContext, Constant.USER_PHOTO, userphoto);

                                    if (userType.equals(Constant.USER_TYPE_OPERATOR)) {
                                        String country_city = jsonObject.getString("country_city");
                                        String address = jsonObject.getString("address");
                                        String gender = jsonObject.getString("gender");
                                        String cardnumber = jsonObject.getString("cardnumber");
                                        String cvv = jsonObject.getString("cvv");
                                        String expireday = jsonObject.getString("expireday");
                                        String licensePhoto = jsonObject.getString("licensePhoto");


                                        Utils.setOnPreference(mContext, Constant.PHONE_NUMBER, phonenumber);
                                        Utils.setOnPreference(mContext, Constant.COUNTRY_CITY, country_city);
                                        Utils.setOnPreference(mContext, Constant.ADDRESS, address);
                                        Utils.setOnPreference(mContext, Constant.GENDER, gender);
                                        Utils.setOnPreference(mContext, Constant.CARD_NUMBER, cardnumber);
                                        Utils.setOnPreference(mContext, Constant.CVV, cvv);
                                        Utils.setOnPreference(mContext, Constant.EXPIRE_DAY, expireday);
                                        Utils.setOnPreference(mContext, Constant.LICENSE_PHOTO, licensePhoto);
                                    }


                                    mContext.startActivity(new Intent(mContext, HomeActivity.class));
                                    ((HomeActivity) mContext).finish();
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

        if (email.length() == 0 || password.length() == 0) {
        } else {
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", email);
            params.put("password", password);

            CustomRequest signinRequest = new CustomRequest(Request.Method.POST, API.SINGIN, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
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
                                    Utils.setOnPreference(mContext, Constant.ID, id);
                                    Utils.setOnPreference(mContext, Constant.USER_PHOTO, userphoto);

                                    if (userType.equals(Constant.USER_TYPE_OPERATOR)) {
                                        String country_city = jsonObject.getString("country_city");
                                        String address = jsonObject.getString("address");
                                        String gender = jsonObject.getString("gender");
                                        String cardnumber = jsonObject.getString("cardnumber");
                                        String cvv = jsonObject.getString("cvv");
                                        String expireday = jsonObject.getString("expireday");
                                        String licensePhoto = jsonObject.getString("licensePhoto");


                                        Utils.setOnPreference(mContext, Constant.PHONE_NUMBER, phonenumber);
                                        Utils.setOnPreference(mContext, Constant.COUNTRY_CITY, country_city);
                                        Utils.setOnPreference(mContext, Constant.ADDRESS, address);
                                        Utils.setOnPreference(mContext, Constant.GENDER, gender);
                                        Utils.setOnPreference(mContext, Constant.CARD_NUMBER, cardnumber);
                                        Utils.setOnPreference(mContext, Constant.CVV, cvv);
                                        Utils.setOnPreference(mContext, Constant.EXPIRE_DAY, expireday);
                                        Utils.setOnPreference(mContext, Constant.LICENSE_PHOTO, licensePhoto);
                                    }


                                    mContext.startActivity(new Intent(mContext, HomeActivity.class));
                                    ((HomeActivity) mContext).finish();
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
                            Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(signinRequest);
        }

    }

}
