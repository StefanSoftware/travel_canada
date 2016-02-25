package com.allytours.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.allytours.controller.Utilities.Utils;
import com.allytours.model.Constant;
import com.allytours.view.HomeActivity;
import com.allytours.R;

import java.util.ArrayList;


public class NavigationMenuFragment extends Fragment {

    ListView menuListView ;

    Activity mActivity;
    ArrayList<String> arrCurrentTitles;
    ArrayList<MenuModel> arrMenu;
    ArrayList<MenuModel> arrCurrentMenu;

    String[] titles = {"AllyTours", "Tours", "My Tours", "Change Password", "Payment Detail", "Contact Us",
            "About", "Sign in", "Tour Listing", "My Tours", "Profile", "Sign Out"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_menu, container, false);

        initVariables();
        initView(view);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        super.onAttach(activity);
    }

    private void initVariables() {
        arrMenu = new ArrayList<>();

        for (int i = 0; i < 12; i ++ ) {
            MenuModel menuModel = new MenuModel();
            menuModel.setId(i);
            menuModel.setTitle(titles[i]);

            arrMenu.add(menuModel);
        }
        String userType = Utils.getFromPreference(mActivity, Constant.USER_TYPE);
        if (userType.equals("")) {
            creatDefaultMenu();
        } else if (userType.equals(Constant.USER_TYPE_CUSTOMER)) {
            creatCustomerMenu();
        } else if (userType.equals(Constant.USER_TYPE_OPERATOR)) {
            creatOperatorMenu();
        }
    }
    private void creatDefaultMenu() {
        arrCurrentMenu = new ArrayList<>();
        arrCurrentMenu.add(arrMenu.get(0));
        arrCurrentMenu.add(arrMenu.get(1));
        arrCurrentMenu.add(arrMenu.get(5));
        arrCurrentMenu.add(arrMenu.get(6));
        arrCurrentMenu.add(arrMenu.get(7));

        arrCurrentTitles = new ArrayList<>();
        arrCurrentTitles.add(arrMenu.get(0).getTitle());
        arrCurrentTitles.add(arrMenu.get(1).getTitle());
        arrCurrentTitles.add(arrMenu.get(5).getTitle());
        arrCurrentTitles.add(arrMenu.get(6).getTitle());
        arrCurrentTitles.add(arrMenu.get(7).getTitle());
    }
    private void creatCustomerMenu() {
        arrCurrentMenu = new ArrayList<>();
        arrCurrentMenu.add(arrMenu.get(0));
        arrCurrentMenu.add(arrMenu.get(1));
        arrCurrentMenu.add(arrMenu.get(2));
        arrCurrentMenu.add(arrMenu.get(3));
        arrCurrentMenu.add(arrMenu.get(4));
        arrCurrentMenu.add(arrMenu.get(5));
        arrCurrentMenu.add(arrMenu.get(6));
        arrCurrentMenu.add(arrMenu.get(11));

        arrCurrentTitles = new ArrayList<>();
        arrCurrentTitles.add(arrMenu.get(0).getTitle());
        arrCurrentTitles.add(arrMenu.get(1).getTitle());
        arrCurrentTitles.add(arrMenu.get(2).getTitle());
        arrCurrentTitles.add(arrMenu.get(3).getTitle());
        arrCurrentTitles.add(arrMenu.get(4).getTitle());
        arrCurrentTitles.add(arrMenu.get(5).getTitle());
        arrCurrentTitles.add(arrMenu.get(6).getTitle());
        arrCurrentTitles.add(arrMenu.get(11).getTitle());
    }

    private void creatOperatorMenu() {
        arrCurrentMenu = new ArrayList<>();
        arrCurrentMenu.add(arrMenu.get(0));
        arrCurrentMenu.add(arrMenu.get(1));
        arrCurrentMenu.add(arrMenu.get(8));
        arrCurrentMenu.add(arrMenu.get(9));
        arrCurrentMenu.add(arrMenu.get(5));
        arrCurrentMenu.add(arrMenu.get(6));
        arrCurrentMenu.add(arrMenu.get(11));

        arrCurrentTitles = new ArrayList<>();
        arrCurrentTitles.add(arrMenu.get(0).getTitle());
        arrCurrentTitles.add(arrMenu.get(1).getTitle());
        arrCurrentTitles.add(arrMenu.get(8).getTitle());
        arrCurrentTitles.add(arrMenu.get(9).getTitle());
        arrCurrentTitles.add(arrMenu.get(5).getTitle());
        arrCurrentTitles.add(arrMenu.get(6).getTitle());
        arrCurrentTitles.add(arrMenu.get(11).getTitle());
    }
    private class MenuModel {
        int id;
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCurrentId() {
            return currentId;
        }

        public void setCurrentId(int currentId) {
            this.currentId = currentId;
        }

        String title;
        int currentId;
    }
    ///////////////init menu
    private void initView(View view) {

        menuListView = (ListView)view.findViewById(R.id.lv_menu);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, arrCurrentTitles);
        menuListView.setAdapter(arrayAdapter);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigationTo(arrCurrentMenu.get(position).getId());
            }
        });
    }

    private void navigationTo(int position) {
        ((HomeActivity)mActivity).navigationTo(position);


    }
}
