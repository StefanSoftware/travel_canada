package com.allytours.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.controller.fragment.SignInFragment;
import com.allytours.controller.fragment.SignUpFragment;
import com.allytours.controller.fragment.SignupStep2Fragment;

public class SigninActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    public static TextView tvTitle;
    private ImageButton btnBack;
    private static Context mContext;

    public static int currentPageNumber;//0: detail page, 1: select option, 2: check out, 3: confirmation
    public static Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        initVariable();
        initUI();
    }
    private void initVariable() {
        mContext = this;
        currentPageNumber = 0;
        fragmentManager = getSupportFragmentManager();
        fragments = new Fragment[3];
    }
    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnBack = (ImageButton)toolbar.findViewById(R.id.btn_back_from_detail);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPageNumber == 0) {
                    finish();
                } else {
                    popFragment();
                }
            }
        });
        tvTitle = (TextView)toolbar.findViewById(R.id.tv_title_detail);

        fragments[0] = new SignInFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragments[0])
                .commit();
        setTitle("Sing in");

    }
    public static void setTitle(String title) {
        tvTitle.setText(title);
    }
    public static void pushFragment(int pageNum) {
        switch (pageNum) {
            case 1:
                fragments[1] = new SignUpFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[1])
                        .commit();
                currentPageNumber = 1;
                setTitle("Sign up");
                break;
            case 2:
                fragments[2] = new SignupStep2Fragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[2])
                        .commit();
                currentPageNumber = 2;
                setTitle("Banking Information");
                break;



        }
    }
    private void popFragment() {
        currentPageNumber --;
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragments[currentPageNumber])
                .commit();

        switch (currentPageNumber) {
            case 0:
                setTitle("Sign in");
                break;
            case 1:
                setTitle("Sign up");
                break;

        }
    }
    @Override
    public void onBackPressed() {

        if (currentPageNumber > 0) {
            popFragment();
        } else {
            finish();
        }
    }
}
