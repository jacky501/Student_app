package org.atctech.schoolmanagementsystem.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.atctech.schoolmanagementsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Intro_Slide_1 extends Fragment {


    public Intro_Slide_1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.intro_slide_1, container, false);

    }

}
