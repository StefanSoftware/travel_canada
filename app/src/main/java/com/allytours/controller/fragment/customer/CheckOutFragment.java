package com.allytours.controller.fragment.customer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.controller.PurchaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckOutFragment extends Fragment implements View.OnClickListener{


    private Button btnPay;
    private Context mContext;
    private TextView tvTourName, tvAdultsCount, tvChildrenCount, tvDate, tvPickUp, tvTotalPrice, tvCurrency;
    private ImageView ivFlag;

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
        mContext = getActivity();
    }

    private void initUI(View view) {
        btnPay = (Button)view.findViewById(R.id.btn_checkout_pay);
        btnPay.setOnClickListener(this);
        tvTourName = (TextView)view.findViewById(R.id.tv_checkout_tourname);
        tvAdultsCount = (TextView)view.findViewById(R.id.tv_checkout_tourname);
        tvChildrenCount = (TextView)view.findViewById(R.id.tv_checkout_tourname);
        tvDate = (TextView)view.findViewById(R.id.tv_checkout_tourname);
        tvPickUp = (TextView)view.findViewById(R.id.tv_checkout_tourname);
        tvTotalPrice = (TextView)view.findViewById(R.id.tv_checkout_tourname);
        tvCurrency = (TextView)view.findViewById(R.id.tv_checkout_tourname);

        ivFlag = (ImageView)view.findViewById(R.id.iv_checkout_flag);

    }

    @Override
    public void onClick(View v) {
        if (v == btnPay) {
            PurchaseActivity.pushFragment(3);
        }
    }
}
