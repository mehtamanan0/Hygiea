package com.example.manan.hygiea;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        String photo = intent.getStringExtra("photo");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        SharedPreferences sharedpreferences = getSharedPreferences("signin", Context.MODE_PRIVATE);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        Fragment hygiea = new MainActivity();
        hygiea.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, hygiea);
        fragmentTransaction.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hview = navigationView.getHeaderView(0);
        de.hdodenhof.circleimageview.CircleImageView profilePhoto = (de.hdodenhof.circleimageview.CircleImageView)hview.findViewById(R.id.imageView);
        TextView  personName = (TextView)hview.findViewById(R.id.name);
        TextView  personEamil = (TextView)hview.findViewById(R.id.email);
        personName.setText(sharedpreferences.getString("name",""));
        personEamil.setText(sharedpreferences.getString("email",""));
        Glide.with(this).load(sharedpreferences.getString("photo","")).into(profilePhoto);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Intent intent = getIntent();
        SharedPreferences sharedpreferences = getSharedPreferences("signin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String name = sharedpreferences.getString("name","");
        String photo = sharedpreferences.getString("photo","");
        String email = sharedpreferences.getString("email","");
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("email", email);
        bundle.putString("photo", photo);
        if (id == R.id.nav_hygiea) {
            Fragment hygiea = new MainActivity();
            hygiea.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, hygiea);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_locate) {
            Fragment locate = new LocateFragent();
            fragmentTransaction.replace(R.id.container, locate);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_profile) {
        Fragment profile = new ProfieFragment();
        fragmentTransaction.replace(R.id.container, profile);
        fragmentTransaction.commit();
    } else if (id == R.id.nav_signout){
            editor.clear();
            editor.apply();
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            Intent intent = new Intent(NavigationDrawer.this, GoogleSIgnInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
