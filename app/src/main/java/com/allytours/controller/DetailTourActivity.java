package com.allytours.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.model.TourModel;
import com.allytours.controller.fragment.DetailTourFragment;

public class DetailTourActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    public static TextView tvTitle, tvCityName, tvAdultPrice, tvChildPrice;
    private ImageButton btnBack;
    private static Context mContext;

    public static TourModel tourModel;

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
        fragmentManager = getSupportFragmentManager();
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
        tvCityName = (TextView)toolbar.findViewById(R.id.tv_citynames);
        tvAdultPrice = (TextView)toolbar.findViewById(R.id.tv_adult_price);
        tvChildPrice = (TextView)toolbar.findViewById(R.id.tv_child_price);

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, new DetailTourFragment())
                .commit();

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
