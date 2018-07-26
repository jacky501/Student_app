package org.atctech.schoolmanagementsystem.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.atctech.schoolmanagementsystem.ApiRequest.ApiRequest;
import org.atctech.schoolmanagementsystem.R;
import org.atctech.schoolmanagementsystem.model.CoursesTeacher;
import org.atctech.schoolmanagementsystem.preferences.Session;

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
public class TeachingEvaluationFragment extends Fragment {

    private Spinner spinner;
    private ApiRequest service;
    private Session session;
    private RadioGroup radioGroupTeaching,radioGroupListening,radioGroupBehaviour,radioGroupWriting,radioGroupAttaining;
    private RadioButton radioButtonTeaching,radioButtonListening,radioButtonBehaviour,radioButtonWriting,radioButtonAttaining;
    private Button submit;
    private EditText comment;
    List<CoursesTeacher> coursesTeachers;

    public TeachingEvaluationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_teaching_evaluation, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Teaching Evaluation");

        spinner = view.findViewById(R.id.spinnerTeahersName);
        radioGroupTeaching = view.findViewById(R.id.radioGroupTeaching);
        radioGroupListening = view.findViewById(R.id.radioGroupListening);
        radioGroupBehaviour = view.findViewById(R.id.radioGroupBehaviour);
        radioGroupWriting = view.findViewById(R.id.radioGroupWriting);
        radioGroupAttaining = view.findViewById(R.id.radioGroupAttaining);
        comment = view.findViewById(R.id.commenttxt);
        submit = view.findViewById(R.id.submitBtn);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_api))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiRequest.class);
        session = Session.getInstance(getActivity().getSharedPreferences("prefs", getContext().MODE_PRIVATE));
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREFS",getContext().MODE_PRIVATE);

         final String id = sharedPreferences.getString("UID",null);

        String classID = session.getUser().getClass_id();

        Call<List<CoursesTeacher>> listCall = service.getAllCourseTeacher(classID);

        listCall.enqueue(new Callback<List<CoursesTeacher>>() {
            @Override
            public void onResponse(Call<List<CoursesTeacher>> call, Response<List<CoursesTeacher>> response) {
                if (response.isSuccessful())
                {

                     coursesTeachers = response.body();

                    String[] names;
                    if (coursesTeachers != null) {
                        names = new String[coursesTeachers.size()];

                        for (int i = 0; i < coursesTeachers.size(); i++) {
                            names[i] = coursesTeachers.get(i).getFname() + " " + coursesTeachers.get(i).getLname() + "("+coursesTeachers.get(i).getSub1()+")";
                        }
                        spinner.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, names));


                    }else
                    {
                        Toast.makeText(getContext(), "no teacher found for this class", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CoursesTeacher>> call, Throwable t) {
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLUE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = spinner.getSelectedItemPosition();

                String teacherName = coursesTeachers.get(position).getU_id();

                    int selectedIdTeaching= radioGroupTeaching.getCheckedRadioButtonId();
                    View radioButton = radioGroupTeaching.findViewById(selectedIdTeaching);
                    int radioId = radioGroupTeaching.indexOfChild(radioButton);
                    radioButtonTeaching= (RadioButton) radioGroupTeaching.getChildAt(radioId);
                    String teaching = radioButtonTeaching.getText().toString();

                int selectedIdListening= radioGroupListening.getCheckedRadioButtonId();
                View radioButton1 = radioGroupListening.findViewById(selectedIdListening);
                int radioId1 = radioGroupListening.indexOfChild(radioButton1);
                radioButtonListening= (RadioButton) radioGroupListening.getChildAt(radioId1);
                String listening = radioButtonListening.getText().toString();

                int selectedIdBehaviour= radioGroupBehaviour.getCheckedRadioButtonId();
                View radioButton2 = radioGroupBehaviour.findViewById(selectedIdBehaviour);
                int radioId2 = radioGroupBehaviour.indexOfChild(radioButton2);
                radioButtonBehaviour= (RadioButton) radioGroupBehaviour.getChildAt(radioId2);
                String behaviour = radioButtonBehaviour.getText().toString();

                int selectedIdWriting= radioGroupWriting.getCheckedRadioButtonId();
                View radioButton3 = radioGroupWriting.findViewById(selectedIdWriting);
                int radioId3 = radioGroupWriting.indexOfChild(radioButton3);
                radioButtonWriting= (RadioButton) radioGroupWriting.getChildAt(radioId3);
                String writing = radioButtonWriting.getText().toString();

                int selectedIdAttaining= radioGroupAttaining.getCheckedRadioButtonId();
                View radioButton4 = radioGroupAttaining.findViewById(selectedIdAttaining);
                int radioId4 = radioGroupAttaining.indexOfChild(radioButton4);
                radioButtonAttaining= (RadioButton) radioGroupAttaining.getChildAt(radioId4);
                String attaining = radioButtonAttaining.getText().toString();


                String commentTxt = comment.getText().toString();


                Call<ResponseBody> bodyCall = service.submitTeachingEvo(id,teacherName,teaching,listening,behaviour,writing,attaining,commentTxt);

                bodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            Toast.makeText(getContext(), "Thanks for your feedback", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        Toast.makeText(getContext(), "Failed response", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return view;
    }

}
