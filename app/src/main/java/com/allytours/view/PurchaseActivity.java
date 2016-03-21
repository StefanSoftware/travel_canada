package com.allytours.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.model.TourModel;
import com.allytours.view.fragment.customer.CheckOutFragment;
import com.allytours.view.fragment.customer.ConfirmationFragment;
import com.allytours.view.fragment.DetailTourFragment;
import com.allytours.view.fragment.customer.SelectOptionFragment;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener{


    public static FragmentManager fragmentManager;

    public static TextView tvTitle;
    private ImageButton btnBack;
    private static Context mContext;

    public static TourModel tourModel;

    public static int currentPageNumber;//0: detail page, 1: select option, 2: check out, 3: confirmation

    public static DetailTourFragment detailTourFragment;
    public static SelectOptionFragment selectOptionFragment;
    public static CheckOutFragment checkOutFragment;
    public static ConfirmationFragment confirmationFragment;

    public static Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        initVariable();
        initUI();

    }

    private void initVariable() {
        mContext = this;
        tourModel = (TourModel)getIntent().getSerializableExtra("tour");
        currentPageNumber = 0;
        fragmentManager = getSupportFragmentManager();
        fragments = new Fragment[5];
    }
    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnBack = (ImageButton)toolbar.findViewById(R.id.btn_back_from_detail);
        btnBack.setOnClickListener(this);
        tvTitle = (TextView)toolbar.findViewById(R.id.tv_title_detail);

        fragments[0] = new DetailTourFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragments[0])
                .commit();

    }
    public static void setTitle(String title) {
        tvTitle.setText(title);
    }
    public static void pushFragment(int pageNum) {
        switch (pageNum) {
            case 1:
                fragments[1] = new SelectOptionFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[1])
                        .commit();
                currentPageNumber = 1;
                setTitle("Select Options");
                break;
            case 2:
                fragments[2] = new CheckOutFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[2])
                        .commit();
                currentPageNumber = 2;
                setTitle("Check Out");
                break;
            case 3:
                if (!Utils.getFromPreference(mContext, Constant.USER_TYPE).equals(Constant.USER_TYPE_CUSTOMER)) {
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    intent.putExtra("login_for_payment", "1") ;
                    ((PurchaseActivity)mContext).startActivityForResult(intent, 100);
                }
                fragments[3] = new ConfirmationFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[3])
                        .commit();
                currentPageNumber = 3;
                setTitle("Confirmation");
                break;
            case 4:

                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {

                }
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

    @Override
    public void onClick(View v) {

        if (v == btnBack) {
            if (currentPageNumber == 0) {
                finish();
            } else {
                popFragment();
            }

        }
    }
    private void popFragment() {
        currentPageNumber --;
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragments[currentPageNumber])
                .commit();

        switch (currentPageNumber) {
            case 0:
                tvTitle.setText(tourModel.getTitle());
                break;
            case 1:
                tvTitle.setText("Select Options");
                break;
            case 2:
                tvTitle.setText("Check Out");
                break;
            case 3:
                break;
        }
    }

}
