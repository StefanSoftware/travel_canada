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

    public static TextView tvTitle, tvCityName;
    private ImageButton btnBack;
    private static Context mContext;

    public static TourModel tourModel;

    public static int currentPageNumber;//0: detail page, 1: select option, 2: check out, 3: confirmation

    public static DetailTourFragment detailTourFragment;
    public static SelectOptionFragment selectOptionFragment;
    public static CheckOutFragment checkOutFragment;
    public static ConfirmationFragment confirmationFragment;

    public static Fragment[] fragments;
    public static String strCityIDs;
    String cityNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        initVariable();
        initUI();

    }

    private void initVariable() {
        mContext = this;
        tourModel = new TourModel();
        currentPageNumber = 0;
        fragmentManager = getSupportFragmentManager();
        fragments = new Fragment[6];
        strCityIDs = getIntent().getStringExtra("city_ids");
        cityNames = getIntent().getStringExtra("city_names");

    }
    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBack = (ImageButton)toolbar.findViewById(R.id.btn_back_from_detail);
        btnBack.setOnClickListener(this);

        tvTitle = (TextView)toolbar.findViewById(R.id.tv_title_detail);
        tvCityName = (TextView)toolbar.findViewById(R.id.tv_citynames);
        setTitle(cityNames);

        fragments[0] = new ToursFragment();
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
                fragments[1] = new DetailTourFragment();
                fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragments[1], "")
                .commit();
                currentPageNumber = 1;
                setTitle(tourModel.getTitle());
                tvCityName.setVisibility(View.VISIBLE);
                showSearchView(false);
//                tvCityName.setText(tourModel.getCityName());
                break;
            case 2:
                fragments[2] = new SelectOptionFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[2])
                        .commit();
                currentPageNumber = 2;
                setTitle("Select Options");
                tvCityName.setVisibility(View.GONE);
                showSearchView(false);
                break;
            case 3:
                fragments[3] = new CheckOutFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragments[3])
                        .commit();
                currentPageNumber = 3;
                setTitle("Check Out");
                tvCityName.setVisibility(View.GONE);
                showSearchView(false);
                break;
            case 4:
                if (!Utils.getFromPreference(mContext, Constant.USER_TYPE).equals(Constant.USER_TYPE_CUSTOMER)) {
                    Intent intent = new Intent(mContext, SigninActivity.class);
                    ((PurchaseActivity)mContext).startActivityForResult(intent, 100);
                } else {
                    fragments[4] = new ConfirmationFragment();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragments[4])
                            .commit();
                    currentPageNumber = 4;
                    setTitle("Confirmation");
                    tvCityName.setVisibility(View.GONE);
                    showSearchView(false);
                }

                break;
            case 5:

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
    private void popFragment() {
        currentPageNumber --;
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragments[currentPageNumber])
                .commit();

        switch (currentPageNumber) {
            case 0:
                showSearchView(true);
                setTitle(cityNames);
                break;
            case 1:
                setTitle(tourModel.getTitle());
                break;
            case 2:
                tvTitle.setText("Select Options");
                break;
            case 3:
                tvTitle.setText("Check Out");
                break;
            case 4:
                break;
        }
    }
    public static void showSearchView(boolean flag) {
        if (flag) {
            searchItem.setVisible(true);
        } else {
            searchItem.setVisible(false);
        }
    }
    private static MenuItem searchItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        searchItem = menu.findItem(R.id.action_search);
        MenuItem newTourItem = menu.findItem(R.id.add_new_tour);
        newTourItem.setVisible(false);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                ToursFragment.search(query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    ToursFragment.search("");
                }
                return false;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ToursFragment.search("");

                return true;
            }
        });

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
