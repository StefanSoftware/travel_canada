package com.allytours.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.controller.fragment.operator.AddNewTourFragment;

public class AddNewTourActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_tour);

        initVariable();
        initUI();

    }
    private void initVariable() {
        mContext = this;
        fragmentManager = getSupportFragmentManager();
    }
    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_new_tour_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvTitle = (TextView)toolbar.findViewById(R.id.tv_add_new_tour_title);

        AddNewTourFragment addNewTourFragment = new AddNewTourFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.add_new_tour_fragment_container, addNewTourFragment)
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_tour, menu);
        return true;
    }
}
