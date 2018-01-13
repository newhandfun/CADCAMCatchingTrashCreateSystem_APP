package com.project.me.ntnu.cadcamtrashcreatorcatcher.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.project.me.ntnu.cadcamtrashcreatorcatcher.IMainActivityAction;
import com.project.me.ntnu.cadcamtrashcreatorcatcher.R;

/**
 * Created by SPLAB_Note on 2018/1/11.
 */

public class TaketaskFragment extends ListFragment {

    ITaskAction iTaskAction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.taketasklayout,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ITaskAction iTaskAction =  (ITaskAction) context;
        if(iTaskAction!=null){
            this.iTaskAction = iTaskAction;
        }
    }

}
