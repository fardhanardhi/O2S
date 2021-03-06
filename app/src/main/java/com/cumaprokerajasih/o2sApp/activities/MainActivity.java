package com.cumaprokerajasih.o2sApp.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.cumaprokerajasih.o2sApp.R;
import com.cumaprokerajasih.o2sApp.analytics.Analytics;
import com.cumaprokerajasih.o2sApp.fragments.FragmentHome;
import com.cumaprokerajasih.o2sApp.utilities.DBHelper;
import com.cumaprokerajasih.o2sApp.utilities.utils;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{
            R.drawable.image_1, R.drawable.image_2,
            R.drawable.image_3,R.drawable.image_4
            ,R.drawable.image_5,R.drawable.image_6
    };

    static DBHelper dbhelper;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    static final String TAG = "MainActivity";

    public int posImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();



        Button pilihImg = (Button) findViewById(R.id.pilihBtn);
        pilihImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                // intent nang activity seng dituju
                if (posImg == 0) {
                    // jika button diklik pas slide pertama
                    alertDialog.setMessage("Ngarah nang intent activity slide pertama");
                }
                else if (posImg == 1) {
                    // jika button diklik pas slide kedua
                    alertDialog.setMessage("Ngarah nang intent activity slide kedua");
                }
                else if (posImg == 2) {
                    // jika button diklik pas slide ketiga
                    alertDialog.setMessage("Ngarah nang intent activity slide ketiga");
                }
                else if (posImg == 3) {
                    // jika button diklik pas slide keempat
                    alertDialog.setMessage("Ngarah nang intent activity slide keempat");
                }
                else if (posImg == 4) {
                    // jika button diklik pas slide kelima
                    alertDialog.setMessage("Ngarah nang intent activity slide kelima");
                }
                else if (posImg == 5) {
                    // jika button diklik pas slide keenam
                    alertDialog.setMessage("Ngarah nang intent activity slide keenam");
                }
                alertDialog.show();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.help);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ActivityInformation.class));
            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.main_drawer);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frame_container, new FragmentHome()).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                //mDrawerLayout.closeDrawers();
                //setTitle(menuItem.getTitle());


                if (menuItem.getItemId() == R.id.profile) {
                    startActivity(new Intent(getApplicationContext(), ActivityProfile.class));
                }
                if (menuItem.getItemId() == R.id.logout) {
                   onDestroy();
                    finish();
                }
                return false;
            }

        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        // init analytics tracker
        ((Analytics) getApplication()).getTracker();

        // checking internet connection
        if (!utils.isNetworkAvailable(MainActivity.this)) {
            Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        dbhelper = new DBHelper(this);

        // create database
        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        // then, the database will be open to use
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        // if user has already ordered food previously then show activity_confirm dialog
        if (dbhelper.isPreviousDataExist()) {
            showAlertDialog();
        }

        isStoragePermissionGranted();

    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(MainActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager (auto slide)
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
        // END Auto start of viewpager (auto slide)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                posImg = currentPage;

                if (posImg == 0) {
                    ((TextView)findViewById(R.id.promoTxt)).setText("Keripik Tempe\nDiskon 50%");
                }
                else if (posImg == 1) {
                    ((TextView)findViewById(R.id.promoTxt)).setText("Keripik Pisang\nDiskon 60%");
                }
                else if (posImg == 2) {
                    ((TextView)findViewById(R.id.promoTxt)).setText("Keripik Bayam\nDiskon 70%");
                }
                else if (posImg == 3) {
                    ((TextView)findViewById(R.id.promoTxt)).setText("Keripik Nanas\nDiskon 80%");
                }
                else if (posImg == 4) {
                    ((TextView)findViewById(R.id.promoTxt)).setText("Keripik Kentang\nDiskon 90%");
                }
                else if (posImg == 5) {
                    ((TextView)findViewById(R.id.promoTxt)).setText("Keripik Singkong\nDiskon 100%");
                }

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    // show activity_confirm dialog to ask user to delete previous order or not
    void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(getString(R.string.db_exist_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.option_yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // delete order data when yes button clicked
                dbhelper.deleteAllData();
                dbhelper.close();

            }
        });

        builder.setNegativeButton(getString(R.string.option_no), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // close dialog when no button clicked
                dbhelper.close();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            dbhelper.deleteAllData();
            dbhelper.close();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case android.R.id.home:
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

}
