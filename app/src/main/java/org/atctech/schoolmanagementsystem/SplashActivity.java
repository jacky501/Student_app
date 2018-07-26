package org.atctech.schoolmanagementsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.atctech.schoolmanagementsystem.preferences.Session;
import org.atctech.schoolmanagementsystem.utils.Utilities;

public class SplashActivity extends Activity {

    View rootView;
    ProgressBar progressBar;
    Session myAppPrefsManager;

    MyTask myTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.splash_loadingBar);
        rootView = progressBar;

        myAppPrefsManager = Session.getInstance(getSharedPreferences("prefs", Context.MODE_PRIVATE));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myTask = new MyTask();
                myTask.execute();
            }
        }, 3000);
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            if (Utilities.isNetworkAvailable(SplashActivity.this)) {

                return "1";
            } else {
                return "0";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase("0")) {

                progressBar.setVisibility(View.GONE);

              Snackbar snackbar =  Snackbar.make(rootView, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                progressBar.setVisibility(View.VISIBLE);

                                // Restart MyTask after 3 seconds
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        myTask = new MyTask();
                                        myTask.execute();
                                    }
                                }, 3000);
                            }
                        }).setActionTextColor(Color.RED);
              View sView = snackbar.getView();
              sView.setBackgroundColor(Color.parseColor("#000000"));
              snackbar.show();

            }
            else {

                if (myAppPrefsManager.isFirstTimeLaunch()) {
                    // Navigate to IntroScreen
                    startActivity(new Intent(SplashActivity.this, IntroScreen.class));
                    finish();
                }
                else {
                    // Navigate to MainActivity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }

    }
}
