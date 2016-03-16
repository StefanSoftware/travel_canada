package com.allytours.view.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.model.UserModel;
import com.allytours.view.HomeActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.CustomRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignupStep2Fragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private EditText etHolderName, etAccountNumber, etRouteNumber;
    private Spinner spCountry, spCurrency, spHolderType;
    private Button btnSubmit;
    Context mContext;

    String strHolderType, strCountry, strCurrency;
    UserModel userModel;



    public SignupStep2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup_step2, container, false);
        initVariables();
        initUI(view);
        initSpinners(view);
        return view;
    }

    private void initVariables(){
        mContext = getContext();
        strHolderType = "";
        strCountry = "";
        strCurrency = "";

    }
    private void initUI(View view) {
        etHolderName = (EditText)view.findViewById(R.id.et_signup_account_holder_name);
        final Drawable originalDrawable = etHolderName.getBackground();
        etHolderName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etHolderName.getText().toString().trim().length() == 0) {
                        etHolderName.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etHolderName.setBackground(originalDrawable);

                }
            }
        });
        etAccountNumber = (EditText)view.findViewById(R.id.et_signup_account_number);
        etAccountNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etAccountNumber.getText().toString().trim().length() == 0) {
                        etAccountNumber.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etAccountNumber.setBackground(originalDrawable);

                }
            }
        });
        etRouteNumber = (EditText)view.findViewById(R.id.et_signup_route_number);
        etRouteNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etRouteNumber.getText().toString().trim().length() == 0) {
                        etRouteNumber.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                } else {
                    etRouteNumber.setBackground(originalDrawable);

                }
            }
        });

        btnSubmit = (Button)view.findViewById(R.id.btn_signup_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValue()) {
                    submit();
                }
            }
        });
        ((HomeActivity)mContext).setTitle(Constant.TITLE_SIGN_UP_2);
    }
    ///check bank account info to submit
    private boolean checkValue() {
        userModel = new UserModel();
        if (TextUtils.isEmpty(etAccountNumber.getText().toString()) || TextUtils.isEmpty(etHolderName.getText().toString()) || TextUtils.isEmpty(etRouteNumber.getText().toString())
             || TextUtils.isEmpty(strHolderType) || TextUtils.isEmpty(strCountry) || TextUtils.isEmpty(strCurrency)  ) {
            Utils.showOKDialog(mContext, "Please input data");
            return false;
        }

        userModel.setBank_account_holder_name(etHolderName.getText().toString());
        userModel.setBank_account_number(etAccountNumber.getText().toString());
        userModel.setBank_rounting_number(etRouteNumber.getText().toString());

        strHolderType = spHolderType.getSelectedItem().toString();
        strCurrency = spCurrency.getSelectedItem().toString();
        strCountry = spCountry.getSelectedItem().toString();

        userModel.setBank_account_holder_type(strHolderType);
        userModel.setBank_country(strCountry);
        userModel.setBank_currency(strCurrency);
        userModel.setEmail(Utils.getFromPreference(mContext, Constant.EMAIL));
        return true;
    }
    ///submit bank account info
    private void submit() {
        Utils.showProgress(mContext);
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", userModel.getEmail());
        params.put("country", userModel.getBank_country());
        params.put("currency", userModel.getBank_currency());
        params.put("account_number", userModel.getBank_account_number());
        params.put("routing_number", userModel.getBank_rounting_number());
        params.put("account_holder_name", userModel.getBank_account_holder_name());
//        params.put("account_holder_type", userModel.getBank_account_holder_type());

        CustomRequest submitRequest = new CustomRequest(Request.Method.POST, API.SIGNUP_STEP2, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.hideProgress();
                        try {
                            String success = response.getString("success");
                            if (success.equals("true")) {



                                Utils.setOnPreference(mContext, Constant.BANK_COUNTRY, userModel.getBank_country());
                                Utils.setOnPreference(mContext, Constant.BANK_ACCOUNT_HOLDER_NAME, userModel.getBank_account_holder_name());
                                Utils.setOnPreference(mContext, Constant.BANK_ACCOUNT_HOLDER_TYPE, userModel.getBank_account_holder_type());
                                Utils.setOnPreference(mContext, Constant.BANK_ACCOUNT_NUMBER, userModel.getBank_account_number());
                                Utils.setOnPreference(mContext, Constant.BANK_CURRENCY, userModel.getBank_currency());
                                Utils.setOnPreference(mContext, Constant.BANK_ROUTING_NUMBER, userModel.getBank_rounting_number());

                                Utils.setOnPreference(mContext, Constant.USER_TYPE, Constant.USER_TYPE_OPERATOR);

                                if (HomeActivity.fromWhere == 0) {
                                    mContext.startActivity(new Intent(mContext, HomeActivity.class));
                                    ((HomeActivity) mContext).finish();
                                } else if (HomeActivity.fromWhere == 1) {
                                    ((HomeActivity) mContext).finishForPurchase(-1);
                                }
                            } else {
                                String reason = response.getString("reason");
                                if (reason.equals("401")) {
//                                    Utils.showOKDialog(mContext, "Invalid Bank info");
                                } else if (reason.equals("403")) {
                                    Utils.showOKDialog(mContext, "Invalid bank account");
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
        requestQueue.add(submitRequest);
    }
    /////////////////spinner //////////////////////////////////////////////////////////////////////////////////////////////////
    private void initSpinners(View view) {
        // Spinner element///1111111111111111111 holder type
        spHolderType = (Spinner) view.findViewById(R.id.sp_signup_account_holder_type);
        // Spinner click listener
        spHolderType.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        String[] holderType;
        holderType = getResources().getStringArray(R.array.account_holder_type);
        strHolderType = holderType[0];
        // Creating adapter for spinner
        ArrayAdapter<String> holderTypeAdapter = new ArrayAdapter <String>(mContext, android.R.layout.simple_spinner_item, holderType);
        // Drop down layout style - list view with radio button
        holderTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spHolderType.setAdapter(holderTypeAdapter);


         //Spinner element///2222222222222222   country
        spCountry = (Spinner) view.findViewById(R.id.sp_signup_country);
        // Spinner click listener
        spCountry.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        String[] country;
        country = getResources().getStringArray(R.array.country);
        strCountry = country[0];
        // Creating adapter for spinner
        ArrayAdapter<String> countryAdpater = new ArrayAdapter <String>(mContext, android.R.layout.simple_spinner_item, country);
        // Drop down layout style - list view with radio button
        countryAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spCountry.setAdapter(countryAdpater);


        // Spinner element///33333333333333333   currency
        spCurrency = (Spinner) view.findViewById(R.id.sp_signup_currency);
        // Spinner click listener
        spCurrency.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        String[] currency;
        currency = getResources().getStringArray(R.array.currency);
        strCurrency = currency[0];
        // Creating adapter for spinner
        ArrayAdapter<String> currencyAdapter = new ArrayAdapter <String>(mContext, android.R.layout.simple_spinner_item, currency);
        // Drop down layout style - list view with radio button
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spCurrency.setAdapter(currencyAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if ( view == spHolderType) {
//            // On selecting a spinner item
//            strHolderType = parent.getItemAtPosition(position).toString();
//        }
//        if ( view == spCountry) {
//            // On selecting a spinner item
//            strCountry = parent.getItemAtPosition(position).toString();
//        }
//        if ( view == spCurrency) {
//            // On selecting a spinner item
//            strCurrency = parent.getItemAtPosition(position).toString();
//        }

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
