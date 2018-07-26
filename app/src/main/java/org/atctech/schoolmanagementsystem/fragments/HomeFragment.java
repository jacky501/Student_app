package org.atctech.schoolmanagementsystem.fragments;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.atctech.schoolmanagementsystem.ApiRequest.ApiRequest;
import org.atctech.schoolmanagementsystem.R;
import org.atctech.schoolmanagementsystem.model.ClassInformation;
import org.atctech.schoolmanagementsystem.preferences.Session;
import org.atctech.schoolmanagementsystem.utils.GPSTracker;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    FloatingActionButton messageBtn;

    Session session;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");

        session = Session.getInstance(getContext().getSharedPreferences("prefs", MODE_PRIVATE));

        messageBtn = view.findViewById(R.id.messageBtn);

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessegesFragment fragment = new MessegesFragment();
                getFragmentManager().beginTransaction().replace(R.id.main_fragment,fragment).addToBackStack("Home").commit();
            }
        });


//        session = Session.getInstance(getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE));
//
//
//
//        BarChart barChart = view.findViewById(R.id.barChart);
//
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(8f, 0));
//        entries.add(new BarEntry(2f, 1));
//        entries.add(new BarEntry(5f, 2));
//        entries.add(new BarEntry(20f, 3));
//        entries.add(new BarEntry(15f, 4));
//        entries.add(new BarEntry(19f, 5));
//        entries.add(new BarEntry(8f, 6));
//        entries.add(new BarEntry(2f, 7));
//        entries.add(new BarEntry(5f, 8));
//        entries.add(new BarEntry(20f, 9));
//        entries.add(new BarEntry(15f, 10));
//        entries.add(new BarEntry(19f, 11));
//
//        BarDataSet bardataset = new BarDataSet(entries, "Cells");
//
//        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("Dec");
//        labels.add("Nov");
//        labels.add("Oct");
//        labels.add("Sep");
//        labels.add("Aug");
//        labels.add("Jul");
//        labels.add("Jun");
//        labels.add("May");
//        labels.add("Apr");
//        labels.add("Mar");
//        labels.add("Feb");
//        labels.add("Jan");
//
//        BarData barData = new BarData(bardataset);
//        barChart.setData(barData);
//
//        Description description = new Description();
//        description.setText("Studence Monthly Report");
//
//        barChart.setDescription(description);
//
//        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        barChart.animateY(4000);

        return view;
    }
//
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                 GPSTracker   gps = new GPSTracker(getContext(), getActivity());

                    if (gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        gps.showSettingsAlert();
                    }

                } else {

                    Toast.makeText(getContext(), "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}
