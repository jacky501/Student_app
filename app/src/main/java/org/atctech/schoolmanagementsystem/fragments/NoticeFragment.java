package org.atctech.schoolmanagementsystem.fragments;


import android.os.Bundle;
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
import org.atctech.schoolmanagementsystem.adapter.NoticeAdapter;
import org.atctech.schoolmanagementsystem.model.AllNotice;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends Fragment {

    RecyclerView recyclerView;
    ApiRequest service;

    public NoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Notice");

        recyclerView = view.findViewById(R.id.noticeRecyclerView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_api))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiRequest.class);

        Call<List<AllNotice>> allNoticesCall = service.getAllStudentNotice();

        allNoticesCall.enqueue(new Callback<List<AllNotice>>() {
            @Override
            public void onResponse(Call<List<AllNotice>> call, Response<List<AllNotice>> response) {

                if (response.isSuccessful())
                {
                    List<AllNotice> allNotices = response.body();
                    NoticeAdapter adapter = new NoticeAdapter(getActivity().getApplicationContext(), allNotices);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else
                {
                    Toast.makeText(getContext(), "No Notice Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AllNotice>> call, Throwable t) {

            }
        });

        return view;
    }

}
