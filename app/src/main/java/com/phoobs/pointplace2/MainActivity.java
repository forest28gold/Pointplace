package com.phoobs.pointplace2;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phoobs.pointplace2.models.Global;
import com.phoobs.pointplace2.utility.DBService;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static final String TAG = "MainActivity";

    private FragmentDrawer drawerFragment;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    public Intent intent_service;

    TextView txt_title;
    ImageView img_logo;

    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Global.dbService = new DBService(this);

        Locale current_locale = getResources().getConfiguration().locale;
        String language = current_locale.getLanguage();

        if (language.equals("da")) {
            Global.settings_language = Global.LOCAL_VALUE_SETTINGS_DA;
        } else if (language.equals("es")) {
            Global.settings_language = Global.LOCAL_VALUE_SETTINGS_ES;
        } else {
            Global.settings_language = Global.LOCAL_VALUE_SETTINGS_EN;
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        txt_title = (TextView)findViewById(R.id.textView_nav_title);
        img_logo = (ImageView) findViewById(R.id.imageView_nav_title);

        txt_title.setVisibility(View.INVISIBLE);
        img_logo.setVisibility(View.VISIBLE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, toolbar);
        drawerFragment.setDrawerListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_LOCATION, 1);
            return;
        }

        if (isServiceRunning(PhoobsLocationService.class)) {

            Log.d(TAG, "PhoobsLocationService is already running!!!");
        } else {

            Log.d(TAG, "New PhoobsLocationService start!");
            intent_service = new Intent(MainActivity.this, PhoobsLocationService.class);
            startService(intent_service);
        }

        onShowFragment(Global.FRAGMENT_HOME);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void onShowFragment(String select_fragment) {

        Fragment fr = null;

        switch (select_fragment) {
            case Global.FRAGMENT_HOME:
                fr = new FragmentHome();
                break;
            case Global.FRAGMENT_FAVORITES:
                fr = new FragmentFavorites();
                break;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_place, fr, "menu_fragment");
        ft.commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void displayView(int position) {
        switch (position) {
            case 0:  // Home
                txt_title.setVisibility(View.INVISIBLE);
                img_logo.setVisibility(View.VISIBLE);

                onShowFragment(Global.FRAGMENT_HOME);

                break;
            case 1:  // Favorites
                txt_title.setVisibility(View.VISIBLE);
                img_logo.setVisibility(View.INVISIBLE);

                onShowFragment(Global.FRAGMENT_FAVORITES);

                break;
            default:
                break;
        }
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (isServiceRunning(PhoobsLocationService.class)) {
                stopService(intent_service);
            }
            finish();
            System.exit(0);
        }
    }
}
