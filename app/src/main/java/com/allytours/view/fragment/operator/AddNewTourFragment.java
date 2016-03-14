package com.allytours.view.fragment.operator;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.allytours.R;
import com.allytours.controller.Helpers.DBHelper;
import com.allytours.model.LocationModel;
import com.allytours.model.TourModel;
import com.android.volley.request.StringRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewTourFragment extends Fragment {

    private MultiAutoCompleteTextView mactLocation, mactRoundTrip;
    private EditText etTitle, etAdultPrice, etChildPrice;
    private TextView tvAdultCurrnceUnit, tvChildCurrencyUnit;
    private Button btnAddPicture;
    private LinearLayout llPictureContainer;
    private RelativeLayout rlFlagE, rlFlagS, rlFlagF, rlFlagP, rlFlagC, rlFlagG, rlFlagJ;
    private CheckBox cbRomantic, cbSightseeing, cbAdventure;
    private RadioButton rbPublic, rbPrivate;
    private Spinner spFrequency;
    private LinearLayout llStartTimeContainer, llStartTimeAdd;
    private Spinner spDurationHour, spDurationDay;
    private EditText etAttraction;
    private CheckBox cbTaxes, cbTips, cbRoundTrip, cbBreakfast, cbLunch, cbDinner, cbEquipment, cbEntranceFee,
        cbOther, cbMandataryFee;
    private EditText etInclusionOther;
    private Button btnDone;

    private Context mContext;
    private String[] arrCiies;

    private TourModel mNewTourModel;

    public AddNewTourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_add_new_tour, container, false);
        initVariables();
        initUI(view);
        return  view;
    }
    private void initVariables() {
        mContext = getContext();
        //get city list
        List<LocationModel> arrayList = DBHelper.getAllLocation();
        int cityCount = arrayList.size();
        arrCiies = new String[cityCount];
        for (int i = 0; i < cityCount; i ++) {
            arrCiies[i] = arrayList.get(i).getCity();
        }
        mNewTourModel = new TourModel();

    }
//////    /////
    private void initUI(View view) {
        initMultiAutoCompletTextView(view);

        etTitle = (EditText)view.findViewById(R.id.et_nt_title);
        etAdultPrice = (EditText)view.findViewById(R.id.et_nt_adult_price);
        etChildPrice = (EditText)view.findViewById(R.id.et_nt_child_price);
        etAttraction = (EditText)view.findViewById(R.id.et_nt_attraction);
        etInclusionOther = (EditText)view.findViewById(R.id.et_nt_inclusion_other);

        tvAdultCurrnceUnit = (TextView)view.findViewById(R.id.tv_nt_adult_currency);
        tvChildCurrencyUnit = (TextView)view.findViewById(R.id.tv_nt_child_currency);

        btnAddPicture = (Button)view.findViewById(R.id.btn_nt_add_picture);
        btnDone = (Button)view.findViewById(R.id.btn_nt_done);

        llPictureContainer = (LinearLayout)view.findViewById(R.id.ll_nt_picture_container);
        llStartTimeContainer = (LinearLayout)view.findViewById(R.id.ll_nt_start_time_container);
        llStartTimeAdd = (LinearLayout)view.findViewById(R.id.ll_nt_start_time_add);

        rlFlagE = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_e);
        rlFlagS = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_s);
        rlFlagF = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_f);
        rlFlagP = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_p);
        rlFlagC = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_c);
        rlFlagG = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_g);
        rlFlagJ = (RelativeLayout)view.findViewById(R.id.rl_nt_flag_j);

        cbRomantic = (CheckBox)view.findViewById(R.id.cb_nt_romantic);
        cbSightseeing = (CheckBox)view.findViewById(R.id.cb_nt_sightseeing);
        cbAdventure = (CheckBox)view.findViewById(R.id.cb_nt_adventure);

        cbTaxes = (CheckBox)view.findViewById(R.id.cb_nt_taxes);
        cbTips = (CheckBox)view.findViewById(R.id.cb_nt_tips);
        cbRoundTrip = (CheckBox)view.findViewById(R.id.cb_nt_round_trip_transfer);
        cbBreakfast = (CheckBox)view.findViewById(R.id.cb_nt_breakfast);
        cbLunch = (CheckBox)view.findViewById(R.id.cb_nt_lunch);
        cbDinner = (CheckBox)view.findViewById(R.id.cb_nt_dinner);
        cbEquipment = (CheckBox)view.findViewById(R.id.cb_nt_equipment_rental);
        cbEntranceFee = (CheckBox)view.findViewById(R.id.cb_nt_entrance_fee);
        cbOther = (CheckBox)view.findViewById(R.id.cb_nt_other);
        cbMandataryFee = (CheckBox)view.findViewById(R.id.cb_nt_mandatory_fee);

        rbPublic = (RadioButton)view.findViewById(R.id.rb_nt_public);
        rbPrivate = (RadioButton)view.findViewById(R.id.rb_nt_private);

        spDurationHour = (Spinner)view.findViewById(R.id.sp_nt_duration_hours);
        spDurationDay = (Spinner)view.findViewById(R.id.sp_nt_duration_days);

    }

    private void initMultiAutoCompletTextView(View view) {
        ///init first MultiAutoCompleteTextView
        mactLocation = (MultiAutoCompleteTextView)view.findViewById(R.id.mact_nt_cities);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext ,android.R.layout.simple_dropdown_item_1line, arrCiies);
        mactLocation.setAdapter(adapter);
        mactLocation.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        ///init second MultiAutoCompleteTextView
        String[] cityName = getResources().getStringArray(R.array.country_state_city);
        ArrayList<String> arrCity = new ArrayList<>();
        for(int i = 0; i < cityName.length; i ++) {
            arrCity.add(cityName[i].substring(0, cityName[i].indexOf(",")));
        }
        mactRoundTrip = (MultiAutoCompleteTextView)view.findViewById(R.id.mact_nt_specify_city);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, arrCity);
        mactRoundTrip.setAdapter(adapter1);
        mactRoundTrip.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }
////////
    private boolean checkFields() {

        return true;
    }
}
