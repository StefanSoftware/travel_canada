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
public class SelectOptionFragment extends Fragment implements View.OnClickListener{

    private Button btnBuy, btnPlusA, btnPlusC, btnMinusA, btnMinusC;
    private Context mContext;
    public SelectOptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_select_option, container, false);
        initVariables();
        initUI(view);
        return view;
    }

    private void initVariables() {
        mContext = getContext();
    }

    private void initUI(View view) {
        btnBuy = (Button)view.findViewById(R.id.btn_so_buy);
        btnBuy.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == btnBuy) {
            PurchaseActivity.pushFragment(2);
        }

    }
}
