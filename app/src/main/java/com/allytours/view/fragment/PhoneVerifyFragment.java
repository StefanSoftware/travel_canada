package com.allytours.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.allytours.R;
import com.allytours.controller.API;
import com.allytours.controller.Utilities.Utils;
import com.allytours.model.Constant;
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
public class PhoneVerifyFragment extends Fragment {

    private Button btnSend;
    private EditText etVerifyCode;

    public PhoneVerifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_phone_verify, container, false);

        initVariable();
        initUI(view);

        return view;
    }
    private void initVariable() {

    }
    private void initUI(View view) {
        btnSend = (Button)view.findViewById(R.id.btn_send_code);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etVerifyCode.getText().toString().trim().length() > 0 ) {
                    verify();
                }
            }
        });


        etVerifyCode = (EditText)view.findViewById(R.id.et_verfiy_code);
    }

    ///verify
    private void verify() {
        String verifyCode = etVerifyCode.getText().toString().trim();
        if (verifyCode.equals(Utils.getFromPreference(getContext(), Constant.VERIFY_CODE))) {
            Utils.setOnPreference(getContext(), Constant.VERIFY_CODE, "");
            ((HomeActivity) getContext()).goToSingupStep2();
        } else {
            Utils.showOKDialog(getContext(), "Verification code is incorrect");
        }


    }

}
