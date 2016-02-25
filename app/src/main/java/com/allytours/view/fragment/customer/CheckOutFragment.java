package com.allytours.view.fragment.customer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.allytours.R;
import com.allytours.view.PurchaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOutFragment extends Fragment implements View.OnClickListener{


    private Button btnPay;
    private Context mContext;
    public CheckOutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        initVariable();
        initUI(view);

        return view;
    }
    private void initVariable() {
        mContext = getContext();
    }

    private void initUI(View view) {
        btnPay = (Button)view.findViewById(R.id.btn_checkout_pay);
        btnPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnPay) {
            PurchaseActivity.pushFragment(3);
        }
    }
}
