package org.atctech.schoolmanagementsystem.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.atctech.schoolmanagementsystem.ApiRequest.ApiRequest;
import org.atctech.schoolmanagementsystem.R;
import org.atctech.schoolmanagementsystem.adapter.ChatAdapter;
import org.atctech.schoolmanagementsystem.model.MessagesAll;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessegesFragment extends Fragment {

    private AlertDialog.Builder builder;
    private ApiRequest service;
    FloatingActionButton button;
    RecyclerView chatRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public MessegesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messeges, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Message");

        button = view.findViewById(R.id.messageBtn);
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

                String u_id = sharedPreferences.getString("UID",null);

                GetMessageList(u_id);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        String u_id = sharedPreferences.getString("UID",null);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_api))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiRequest.class);

        GetMessageList(u_id);

        builder = new AlertDialog.Builder(getContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoticePopup();
            }
        });


        return view;
    }

    public  void  showNoticePopup(){

        LayoutInflater inflater = this.getLayoutInflater();

        View dview = inflater.inflate(R.layout.dialog_enquiry, null);
        final TextView txtsubject = dview.findViewById(R.id.subject);
        final TextView txtmessage = dview.findViewById(R.id.message);

        builder.setView(dview)

                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        if (txtmessage.getText().length() > 0  && txtsubject.getText().length() > 0){

                            SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

                            String student_id = sharedPreferences.getString("UID",null);

                            String subject = txtsubject.getText().toString();
                            String message = txtmessage.getText().toString();



                            Call<ResponseBody> responseBodyCall = service.sendMessage(student_id,subject,message);

                            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful())
                                    {
                                        Toast.makeText(getContext(),"Successfully send sms", Toast.LENGTH_SHORT).
                                        show();
                                        MessegesFragment fragment = new MessegesFragment();
                                        getFragmentManager().beginTransaction().replace(R.id.main_fragment,fragment).commitAllowingStateLoss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });

                        }
                    }
                });
        builder.show();
    }

    public void GetMessageList(String u_id)
    {
        Call<List<MessagesAll>> messageCall = service.getAllMessages(u_id);

        messageCall.enqueue(new Callback<List<MessagesAll>>() {
            @Override
            public void onResponse(Call<List<MessagesAll>> call, Response<List<MessagesAll>> response) {
                if (response.isSuccessful())
                {
                    swipeRefreshLayout.setRefreshing(false);
                    List<MessagesAll> allMessage = response.body();
                    ChatAdapter adapter = new ChatAdapter(getContext(),allMessage);
                    chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    chatRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else
                {
                    Toast.makeText(getContext(), "No Message Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MessagesAll>> call, Throwable t) {

            }
        });
    }
}
