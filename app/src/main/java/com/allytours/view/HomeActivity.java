package com.allytours.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.utilities.ExceptionHandler;
import com.allytours.controller.Helpers.InitHelper;
import com.allytours.utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.view.fragment.AboutFragment;
import com.allytours.view.fragment.ContactUsFragment;
import com.allytours.view.fragment.MapFragment;
import com.allytours.view.fragment.ProfileFragment;
import com.allytours.view.fragment.PromotionFragment;
import com.allytours.view.fragment.SignInFragment;
import com.allytours.view.fragment.SignUpFragment;
import com.allytours.view.fragment.SignupStep2Fragment;
import com.allytours.view.fragment.ToursFragment;
import com.allytours.view.fragment.customer.CustomerToursFragment;
import com.allytours.view.fragment.NavigationMenuFragment;
import com.allytours.view.fragment.operator.OperatorToursFragment;
import com.allytours.view.fragment.operator.TourListingFragment;

public class HomeActivity extends AppCompatActivity {

    public static DrawerLayout mDrawerLayout;
    public static FragmentManager fragmentManager;
    private Toolbar toolbar;
    private TextView tvTitle;
    private MenuItem searchItem, newTourItem;
    private NavigationMenuFragment navigationMenuFragment;
    private MapFragment mainFragment;
    private Context mContext;


    public  static int fromWhere;//0: default, 1: from purchaseActivity to log in for payment
    public static String strCityIDs;// need to fetch tours in search tour page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ///set exception handler
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        initVariable();
        initUI();
    }
    private void initVariable() {
        mContext = this;
        fromWhere = getIntent().getIntExtra("login_for_payment", 0);
        strCityIDs = "";
    }
    private void initUI() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton btnMenu = (ImageButton)toolbar.findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        tvTitle = (TextView)toolbar.findViewById(R.id.tv_home_title);
        setTitle("Allytours");


        //////////////navigation view
        fragmentManager = getSupportFragmentManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawerlayout);
        navigationMenuFragment = new NavigationMenuFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.main_menu_container, navigationMenuFragment)
                .commit();

        mainFragment = new MapFragment();
        if (fromWhere == 0) {
            toolbar.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, mainFragment)
                    .commit();
        } else {
            toolbar.setVisibility(View.INVISIBLE);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new SignInFragment())
                    .commit();
        }

    }

    public void setTitle(String strTitle) {
        tvTitle.setText(strTitle);
//        toolbar.setTitle(strTitle);
//        TextView tvTitle = (TextView)toolbar.findViewById(R.id.tv_home_title);
//        tvTitle.setText(strTitle);
    }
    public android.app.FragmentManager getFragmentManager() {
        return getFragmentManager();
    }
    public void finishForPurchase(int result) {
        setResult(result);
        finish();
    }

    public void goToSignup() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new SignUpFragment(), "")
                .commit();

    }
    public void backToSingin() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new SignInFragment(), "")
                .commit();
    }

    public void goToSingupStep2() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new SignupStep2Fragment(), "")
                .commit();
    }
    public void goToTourSearchPage() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new ToursFragment(), "")
                .commit();
    }
    public void showHideSearchView(boolean flag) {
        if (flag) {
            searchItem.setVisible(true);
        } else {
            searchItem.setVisible(false);
        }
    }
    public void navigationTo(int num) {
        switch (num) {

            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new CustomerToursFragment(), "")
                        .commit();
                showHideSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new OperatorToursFragment(), "")
                        .commit();
                showHideSearchView(false);
                newTourItem.setVisible(false);
                break;

            case 2:
                mainFragment = new MapFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mainFragment, "")
                        .commit();
                showHideSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new TourListingFragment(), "")
                        .commit();
                showHideSearchView(false);
                newTourItem.setVisible(true);
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new PromotionFragment(), "")
                        .commit();
                showHideSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 5:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new ContactUsFragment(), "")
                        .commit();
                showHideSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 6:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new AboutFragment(), "")
                        .commit();
                showHideSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 7:
                signout();
                break;
            case 10:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment(), "")
                        .commit();
                showHideSearchView(false);
                newTourItem.setVisible(false);
                break;
            case 11:
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new SignInFragment(), "")
                        .commit();
                showHideSearchView(false);
                newTourItem.setVisible(false);
                break;
        }
        mDrawerLayout.closeDrawers();
    }
    private void signout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getResources().getString(R.string.app_name));
        builder.setMessage("Do you want sign out?");
        builder.setCancelable(true);
        builder.setPositiveButton("Sign Out",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        InitHelper.initPreference(mContext);

                        Utils.setOnPreference(HomeActivity.this, Constant.FB_ACCESS_TOKEN, "");
//                        Utils.setOnPreference(HomeActivity.this, Constant.FB_PHOTO, "");
                        Utils.setOnPreference(HomeActivity.this, Constant.FB_EMAIL, "");
//                        Utils.setOnPreference(HomeActivity.this, Constant.FB_NAME, "");
//                        Utils.setOnPreference(HomeActivity.this, Constant.FB_ID, "");

                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        finish();
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
//        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "Close is clicked", Toast.LENGTH_LONG).show();
//
//            }
//        });
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        searchItem = menu.findItem(R.id.action_search);
        newTourItem = menu.findItem(R.id.add_new_tour);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
//                mainFragment.search(query);
                ToursFragment.search(query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchItem.setVisible(false);
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
        } else if (id == R.id.add_new_tour) {
            startActivity(new Intent(this, AddNewTourActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
