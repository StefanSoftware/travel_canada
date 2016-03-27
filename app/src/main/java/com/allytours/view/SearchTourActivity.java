package com.allytours.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.allytours.model.Constant;
import com.allytours.model.TourModel;
import com.allytours.utilities.Utils;
import com.allytours.view.fragment.ToursFragment;

public class SearchTourActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    public static TextView tvTitle, tvCityName;
    private ImageButton btnBack;
    private static Context mContext;

    public static String strCityIDs;
    String cityNames;
    public static String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tour);

        initVariable();
        initUI();
    }
    private void initVariable() {
        mContext = this;
        fragmentManager = getSupportFragmentManager();
        strCityIDs = getIntent().getStringExtra("city_ids");
        cityNames = getIntent().getStringExtra("city_names");
        searchQuery = "";
    }
    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnBack = (ImageButton)toolbar.findViewById(R.id.btn_back_from_detail);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(100);
                finish();
            }
        });

        tvTitle = (TextView)toolbar.findViewById(R.id.tv_title_detail);
        tvCityName = (TextView)toolbar.findViewById(R.id.tv_citynames);
        setTitle(cityNames);

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container,  new ToursFragment())
                .commit();

    }
    public static void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 201 && resultCode == 100) {
            setResult(100);
            finish();
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
                searchQuery = query;
                ToursFragment.search();
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    searchQuery = "";
                    ToursFragment.search();
                }
                return false;
            }

        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchQuery = "";
                ToursFragment.search();

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
