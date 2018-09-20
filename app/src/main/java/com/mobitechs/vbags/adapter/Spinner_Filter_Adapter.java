package com.mobitechs.vbags.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Spinner_Filter_Adapter extends ArrayAdapter {

    private ArrayList arrayList;
    private Context context;


    public Spinner_Filter_Adapter(FragmentActivity activity, int spinneritem, List<String> standardNameList) {
        super(activity,spinneritem,standardNameList);
    }

    public Spinner_Filter_Adapter(Context context, int spinner_item, List<String> orderStatusNameList) {
        super(context,spinner_item,orderStatusNameList);
    }


    @Override
    public boolean isEnabled(int position) {
        if(position == 0) {
            // Disable the first item from Spinner
            // First item will be use for hint
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        View view = super.getDropDownView(position, convertView, parent);
        TextView tv = (TextView) view;
        if(position == 0){
            // Set the hint text color gray
            tv.setTextColor(Color.GRAY);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
        else {
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
        return view;
    }
}
