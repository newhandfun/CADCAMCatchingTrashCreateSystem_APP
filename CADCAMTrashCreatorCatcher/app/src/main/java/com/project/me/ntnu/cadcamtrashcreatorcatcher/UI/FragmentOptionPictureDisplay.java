package com.project.me.ntnu.cadcamtrashcreatorcatcher.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.me.ntnu.cadcamtrashcreatorcatcher.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOptionPictureDisplay extends Fragment {


    public FragmentOptionPictureDisplay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option_picture_display, container, false);
    }

}
