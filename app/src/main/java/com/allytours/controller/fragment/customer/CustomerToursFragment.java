package com.allytours.controller.fragment.customer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allytours.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerToursFragment extends Fragment {


    public CustomerToursFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_tours, container, false);
    }


}
