package com.allytours.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.model.TourModel;
import com.allytours.view.fragment.ToursFragment;
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

    public static int currentPageNumber;// 0: select option, 1: check out, 2: confirmation

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
        fragments = new Fragment[3];
    }
    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBack = (ImageButton)toolbar.findViewById(R.id.btn_back_from_detail);
        btnBack.setOnClickListener(this);

        tvTitle = (TextView)toolbar.findViewById(R.id.tv_title_detail);
        pushFragment(0);

    }
    public static void setTitle(String title) {
        tvTitle.setText(title);
    }
    public static void pushFragment(int pageNum) {
        switch (pageNum) {

            case 0:
                fragments[pageNum] = new SelectOptionFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[pageNum])
                        .commit();
                currentPageNumber = pageNum;
                setTitle("Select Options");
                break;
            case 1:
                fragments[pageNum] = new CheckOutFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[pageNum])
                        .commit();
                currentPageNumber = pageNum;
                setTitle("Check Out");
                break;
            case 2:
                if (!Utils.getFromPreference(mContext, Constant.USER_TYPE).equals(Constant.USER_TYPE_CUSTOMER)) {
                    Intent intent = new Intent(mContext, SigninActivity.class);
                    ((PurchaseActivity)mContext).startActivityForResult(intent, 100);
                } else {
                    fragments[pageNum] = new ConfirmationFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragments[pageNum])
                            .commit();
                    currentPageNumber = pageNum;
                    setTitle("Confirmation");
                }

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
                tvTitle.setText("Select Options");
                break;
            case 1:
                tvTitle.setText("Check Out");
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                if (Utils.getFromPreference(mContext, Constant.USER_TYPE).length() > 0) {
                    setResult(100);
                }
                finish();
            } else {
                popFragment();
            }

        }
    }

    private static MenuItem searchItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        MenuItem newTourItem = menu.findItem(R.id.add_new_tour);
        newTourItem.setVisible(false);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
