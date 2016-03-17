package com.allytours.view;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.view.fragment.DetailTourFragment;

public class ReviewActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView tvTitle;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        initVariables();
        initUI();
    }
    private void initVariables(){
        mContext = this;
    }
    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnBack = (ImageButton)toolbar.findViewById(R.id.btn_back_from_detail);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle = (TextView)toolbar.findViewById(R.id.tv_title_detail);
        TextView tvPrice = (TextView)toolbar.findViewById(R.id.tv_price_detail);
        tvPrice.setVisibility(View.GONE);


    }
}
