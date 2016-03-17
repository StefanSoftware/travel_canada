package com.allytours.widget;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import com.allytours.R;
import com.allytours.utilities.TimeUtility;
import com.allytours.utilities.Utils;

import java.util.Calendar;

@SuppressLint("ValidFragment")
public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText editText;

    public  SelectDateFragment (EditText editText) {
        this.editText = editText;
    }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            // Show selected date
            String date = String.valueOf(day) + "/"  + String.valueOf(month + 1) + "/" + String.valueOf(year);
            String timeStamp = String.valueOf(TimeUtility.getTimeStampFromString(date));
            if ((Long.parseLong(timeStamp) + 24 * 3600) < Long.parseLong(TimeUtility.getCurrentTimeStamp())) {

            } else {
                editText.setText(year + "-" + month + "-" + day);
            }

        }

    }