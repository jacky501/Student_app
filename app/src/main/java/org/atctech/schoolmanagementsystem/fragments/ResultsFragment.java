package org.atctech.schoolmanagementsystem.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.atctech.schoolmanagementsystem.ApiRequest.ApiRequest;
import org.atctech.schoolmanagementsystem.R;
import org.atctech.schoolmanagementsystem.adapter.ResultAdapter;
import org.atctech.schoolmanagementsystem.model.LiveResults;
import org.atctech.schoolmanagementsystem.preferences.Session;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ApiRequest service;
    //private ProgressDialog progressDialog;


// instantiate it within the onCreate method


    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Results");


//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Fetching data....");
//        progressDialog.show();



        recyclerView = view.findViewById(R.id.recyclerView1);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_api))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiRequest.class);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        String id = sharedPreferences.getString("UID", null);

        Call<List<LiveResults>> liveResultCall = service.getResult(id);

        liveResultCall.enqueue(new Callback<List<LiveResults>>() {
            @Override
            public void onResponse(Call<List<LiveResults>> call, Response<List<LiveResults>> response) {

                if (response.isSuccessful()) {
                   // progressDialog.dismiss();
                    List<LiveResults> liveResults = response.body();
                    ResultAdapter adapter = new ResultAdapter(getActivity().getApplicationContext(), liveResults);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "no result found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LiveResults>> call, Throwable t) {

            }
        });

        return view;
    }


    }


