package com.allytours.view;

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
import com.allytours.model.TourModel;
import com.allytours.view.fragment.customer.CheckOutFragment;
import com.allytours.view.fragment.customer.ConfirmationFragment;
import com.allytours.view.fragment.DetailTourFragment;
import com.allytours.view.fragment.customer.SelectOptionFragment;

public class PurchaseActivity extends AppCompatActivity implements View.OnClickListener{

    public static FragmentManager fragmentManager;

    public static TextView tvTitle, tvPrice;
    private ImageButton btnBack;
    private Context mContext;

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
        setContentView(R.layout.activity_detail_tour);

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
        tvPrice = (TextView)toolbar.findViewById(R.id.tv_price_detail);

        fragments[0] = new DetailTourFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragments[0])
                .commit();

    }
    public static void pushFragment(int pageNum) {
        switch (pageNum) {
            case 1:
                fragments[1] = new SelectOptionFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[1])
                        .commit();
                currentPageNumber = 1;
                tvTitle.setText("Select Options");
                tvPrice.setVisibility(View.GONE);
                break;
            case 2:
                fragments[2] = new CheckOutFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[2])
                        .commit();
                currentPageNumber = 2;
                tvTitle.setText("Check Out");
                break;
            case 3:
                fragments[3] = new ConfirmationFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[3])
                        .commit();
                currentPageNumber = 3;
                tvTitle.setText("Confirmation");
                break;
            case 4:

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
                tvPrice.setVisibility(View.VISIBLE);
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
