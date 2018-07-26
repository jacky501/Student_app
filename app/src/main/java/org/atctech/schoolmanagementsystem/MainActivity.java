package org.atctech.schoolmanagementsystem;
import android.*;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.atctech.schoolmanagementsystem.ApiRequest.ApiRequest;
import org.atctech.schoolmanagementsystem.fragments.ContactUsFragment;
import org.atctech.schoolmanagementsystem.fragments.CourseTeacherFragment;
import org.atctech.schoolmanagementsystem.fragments.FeesFragment;
import org.atctech.schoolmanagementsystem.fragments.HolidayFragment;
import org.atctech.schoolmanagementsystem.fragments.HomeFragment;
import org.atctech.schoolmanagementsystem.fragments.MapFragment;
import org.atctech.schoolmanagementsystem.fragments.NoticeFragment;
import org.atctech.schoolmanagementsystem.fragments.PasswordChangeFragment;
import org.atctech.schoolmanagementsystem.fragments.ProfileFragment;
import org.atctech.schoolmanagementsystem.fragments.ResultsFragment;
import org.atctech.schoolmanagementsystem.fragments.SettingsFragment;
import org.atctech.schoolmanagementsystem.fragments.TeacherFragment;
import org.atctech.schoolmanagementsystem.fragments.TeachingEvaluationFragment;
import org.atctech.schoolmanagementsystem.fragments.TimeTableFragment;
import org.atctech.schoolmanagementsystem.model.CoursesTeacher;
import org.atctech.schoolmanagementsystem.model.StudentDetails;
import org.atctech.schoolmanagementsystem.preferences.Session;
import org.atctech.schoolmanagementsystem.utils.GPSTracker;
import org.atctech.schoolmanagementsystem.utils.Utilities;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    boolean doublePressedBackToExit = false;
    DrawerLayout drawerLayout;
    NavigationView navigationDrawer;
    View navHeader;
    public static ActionBarDrawerToggle actionBarDrawerToggle;
    public static int navItemIndex = 0;
    Session session;
    ApiRequest service;
    TextView profilename;
    ImageView imageView;
    GPSTracker  gps;
    Handler handler;
    Runnable run;

    private static final String TAG_HOME = "home";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_RESULT = "result";
    private static final String TAG_TEACHER = "teacher";
    private static final String TAG_HOLIDAY = "holiday";
    private static final String TAG_TIME_TABLE = "time_table";
    private static final String TAG_FEES = "fees";
    private static final String TAG_NOTICE = "notice";
    private static final String TAG_TEACHING_EVOULUATION = "teaching evaluation";
    private static final String TAG_COURSE_TEACHER = "course teacher";
    private static final String TAG_CONTACT_US = "contact us";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.myToolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationDrawer = findViewById(R.id.main_drawer);


        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("SMS");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        navHeader = navigationDrawer.getHeaderView(0);
        profilename = navHeader.findViewById(R.id.drawer_profile_name);
        imageView = navHeader.findViewById(R.id.drawer_profile_image);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_api))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiRequest.class);

        session = Session.getInstance(getSharedPreferences("prefs", Context.MODE_PRIVATE));

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);

        String id = sharedPreferences.getString("UID",null);
        getStudentdetails(id);
        SendLocationToServer();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog localDialog = new Dialog(MainActivity.this);
                localDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                localDialog.setContentView(R.layout.dialog_book_img);
                localDialog.getWindow().setLayout(-1, -1);
                localDialog.show();
                ImageView localImageView1 = localDialog.findViewById(R.id.iv_dialog_img);
                ImageView localImageView2 = localDialog.findViewById(R.id.iv_dialog_cancle);

                try{
                    if (session.getUser().getFile().equalsIgnoreCase("") && session.getUser().getFile().isEmpty()) {

                        Picasso.with(MainActivity.this).load(R.drawable.profile).into(localImageView1);

                    }else {
                        Picasso.with(MainActivity.this).load(session.getUser().getFile()).placeholder(R.drawable.profile).into(localImageView1);
                    }
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                }


                localImageView2.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View paramAnonymousView)
                    {
                        localDialog.dismiss();
                    }
                });
            }
        });






        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                invalidateOptionsMenu();
            }
        };


        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();


        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        setUpNavigationView();


    }

    private void loadHomeFragment() {

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();

            return;
        }

                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.main_fragment, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

        drawerLayout.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:

                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:

                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 2:

                TeacherFragment teacherFragment = new TeacherFragment();
                return teacherFragment;


            case 3:

                ResultsFragment resultsFragment = new ResultsFragment();
                return resultsFragment;


            case 4:

                TeachingEvaluationFragment teachingEvaluationFragment = new TeachingEvaluationFragment();
                return teachingEvaluationFragment;

            case 5:

                TimeTableFragment timeTableFragment = new TimeTableFragment();
                return timeTableFragment;


            case 6:
                HolidayFragment holidayFragment = new HolidayFragment();
                return holidayFragment;

            case 7:

                CourseTeacherFragment courseTeacherFragment = new CourseTeacherFragment();
                return courseTeacherFragment;

            case 8:
                FeesFragment feesFragment = new FeesFragment();
                return feesFragment;

            case 9:
                NoticeFragment noticeFragment = new NoticeFragment();
                return noticeFragment;

            case 10:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;

            case 11:

                ContactUsFragment contactUsFragment = new ContactUsFragment();
                return contactUsFragment;
            default:
                return new HomeFragment();
        }
    }
    private void setUpNavigationView() {
        navigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.drawerHome:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.drawerAccount:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    case R.id.drawerTeacher:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_TEACHER;
                        break;
                    case R.id.drawerResults:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_RESULT;
                        break;
                    case R.id.teaching_evoluation:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_TEACHING_EVOULUATION;
                        break;
                    case R.id.drawerTimeTable:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_TIME_TABLE;
                        break;
                    case R.id.drawerHoliday:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_HOLIDAY;
                        break;
                    case R.id.drawerCourseTeacher:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_COURSE_TEACHER;
                        break;
                    case R.id.drawerFees:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_FEES;
                        break;
                    case R.id.drawerNotice:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_NOTICE;
                        break;

                    case R.id.drawerSettings:
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.drawerContactUs:
                        navItemIndex = 11;
                        CURRENT_TAG = TAG_CONTACT_US;
                        break;

                    case R.id.drawerLogout:

                        new AlertDialog.Builder(MainActivity.this).setTitle("Logout")
                                .setMessage("Are you sure you want to logout ?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("UID", "");
                                        editor.apply();
                                        session.setLoggedIn(false);
                                        session.deleteUser();
                                        session.deleteClasses();
                                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                                        finish();
                                        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_out_right);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();

                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.settings, menu);



        MenuItem setting = menu.findItem(R.id.settings);
        MenuItem changePassword = menu.findItem(R.id.changePassword);

       changePassword.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem menuItem) {

               if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                   drawerLayout.closeDrawers();

               }

               PasswordChangeFragment fragment = new PasswordChangeFragment();
               getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment,fragment).commitAllowingStateLoss();

               return true;
           }
       });



        return true;
    }

    @Override
    public void onBackPressed() {


        FragmentManager fm = getSupportFragmentManager();



        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();

        }

        else  if (fm.getBackStackEntryCount() > 0) {


            fm.popBackStack();

        }

        else if (doublePressedBackToExit) {
            super.onBackPressed();

        }
        else {
            this.doublePressedBackToExit = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    doublePressedBackToExit = false;
                    session.deleteUser();
                }
            }, 2000);
        }
    }


    public void getStudentdetails(String id){

        Call<StudentDetails> studentDetailsCall = service.getStudentDetails(id);

        studentDetailsCall.enqueue(new Callback<StudentDetails>() {
            @Override
            public void onResponse(Call<StudentDetails> call, Response<StudentDetails> response) {
                if (response.isSuccessful())
                {
                    StudentDetails studentDetails = response.body();
                   session.saveUser(studentDetails);
                   if (studentDetails.getFile().equalsIgnoreCase("") && studentDetails.getFile().isEmpty()) {

                       Picasso.with(MainActivity.this).load(R.drawable.profile).into(imageView);

                   }else {
                       Picasso.with(MainActivity.this).load(studentDetails.getFile()).placeholder(R.drawable.profile).into(imageView);
                   }

                    profilename.setText(session.getUser().getFname()+","+session.getUser().getLname());

                }else {
                    Toast.makeText(getApplicationContext(), "not responding", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StudentDetails> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void SendLocationToServer() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        String uid = sharedPreferences.getString("UID", null);

        double longitude = 0;
        double latitude = 0;
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            gps = new GPSTracker(MainActivity.this, MainActivity.this);

            if (gps.canGetLocation()) {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();


            } else {
                gps.showSettingsAlert();
            }
        }

        Call<ResponseBody> responseBodyCall = service.sendLocation(uid, latitude, longitude);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        scheduleThread();
    }

    public void scheduleThread(){
        handler=new Handler();
        run=new Runnable() {

            @Override
            public void run() {
                if(Utilities.isNetworkAvailable(MainActivity.this)){

                    gps=new GPSTracker(MainActivity.this,MainActivity.this);
                    SendLocationToServer();

                }else{

                }
            }
        };
        handler.postDelayed(run, 30000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            handler.removeCallbacks(run);
            run=null;
        }
    }
}
