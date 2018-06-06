package com.project.finalyear.thaispellinggame.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.adapter.ViewPagerAdapter;
import com.project.finalyear.thaispellinggame.fragment.ContactAppFragment;
import com.project.finalyear.thaispellinggame.fragment.ContactUsFragment;
import com.project.finalyear.thaispellinggame.fragment.EditProfileFragment;
import com.project.finalyear.thaispellinggame.fragment.LearningMainFragment;
import com.project.finalyear.thaispellinggame.fragment.TestFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private DatabaseReference usersReference;
    private String url = "https://thaispellinggame-28cfe.firebaseio.com/";

    private Button btnContactUs, btnContactApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        Firebase myFirebaseRef = new Firebase(url);
        myFirebaseRef.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){

            String online_user_id = mAuth.getCurrentUser().getUid();
            usersReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(online_user_id);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle("ThaiMisspellingGame");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setFragment(new ViewPagerAdapter());//init

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in and update UI accordingly.

        if (currentUser == null){

            sendToStart();

        }else if (currentUser != null){

            usersReference.child("online").setValue(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (currentUser != null){

            usersReference.child("online").setValue(false);
        }

        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void sendToStart() {

        Intent startIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startIntent);
        finish();
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home){

            setFragment(new ViewPagerAdapter());

        }else if (id == R.id.nav_learn) {

            setFragment(new LearningMainFragment());

        } else if (id == R.id.nav_exam) {

            //setFragment(new TestFragment());
            Intent intent = new Intent(getApplicationContext(), QuizTestActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_profile) {

            setFragment(new EditProfileFragment());

        }else if (id == R.id.nav_contact) {

            dialogContact();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dialogContact() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_popup_contact);

        btnContactUs = (Button) dialog.findViewById(R.id.btnContactUs);
        btnContactApp = (Button) dialog.findViewById(R.id.btnContactApp);

        btnContactApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new ContactAppFragment());
                dialog.cancel();

            }
        });
        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ContactUsFragment());
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void setFragment(Fragment fragment){
        if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main,fragment);
            ft.commit();
        }
    }

}