package com.project.finalyear.thaispellinggame.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.eventbus.EventBus;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.finalyear.thaispellinggame.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;



public class RegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText regis_username;
    private EditText regis_email;
    private EditText regis_password;
    private Button btn_create_account;
    private ProgressBar progressBar;
    //  LoginButton loginButton;
    private FirebaseAuth.AuthStateListener fireAthStateListener;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private CallbackManager callbackManager;
    private Button btnFacebook;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_register);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Sign Up");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regis_username = (EditText) findViewById(R.id.register_name);
        regis_email = (EditText) findViewById(R.id.register_email);
        regis_password = (EditText) findViewById(R.id.register_password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_create_account = (Button) findViewById(R.id.create_account_button);
//        loginButton = (LoginButton) findViewById(R.id.facebook_account_button);
        btnFacebook = (Button) findViewById(R.id.facebook_account_button);
        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = regis_username.getText().toString();
                String email = regis_email.getText().toString();
                String password = regis_password.getText().toString();

                if ((TextUtils.isEmpty(name)) | (TextUtils.isEmpty(email)) | (TextUtils.isEmpty(password))) {
                    Toast.makeText(RegisterActivity.this,
                            "กรุณากรอกข้อมูลให้ครบ!", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "รหัสไม่ถูกต้อง ลองใหม่อีกครั้ง!", Toast.LENGTH_SHORT).show();
                        return;

                    } else RegisterAccount(name, email, password);
                }
            }
        });

        //---------------- Login with facebook--------------//

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("email", "public_profile"));

                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handelFacebookAccessToken(loginResult.getAccessToken());
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        });

        mAuth = FirebaseAuth.getInstance();
        fireAthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser  firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    goMainScreen();

                }
            }
        };

    }

    private void goMainScreen() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    //---------------- get id and set data to Users --------------//
    private void handelFacebookAccessToken(final AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Error Login", Toast.LENGTH_LONG).show();
                }else {

                    GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                String  emailFB = response.getJSONObject().getString("email").toString();
                                String nameFB = response.getJSONObject().getString("name").toString();
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    String userId = firebaseUser.getUid();
                                    mDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                            .child("Users").child(userId);
                                }else {
                                    Toast.makeText(getApplicationContext(), "ID Null" , Toast.LENGTH_LONG).show();
                                }

                                mDatabaseRef.child("name").setValue(nameFB);
                                mDatabaseRef.child("email").setValue(emailFB);
                                mDatabaseRef.child("online").setValue(true);
                                mDatabaseRef.child("image").setValue("default_profile_pic");
                                mDatabaseRef.child("thumb_image").setValue("default");
                                mDatabaseRef.child("rank").setValue(0);
                                mDatabaseRef.child("level").setValue(0);
                                mDatabaseRef.child("score").setValue(0);
                                mDatabaseRef.child("bestScore").setValue(0);

                                Log.d("email", emailFB);
                                Log.d("name", nameFB);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    //------- push to database fire base --------//
                    GraphRequest.executeBatchAsync(request);
                    Bundle bundle = new Bundle();
                    bundle.putString("fields", "email,name");
                    request.setParameters(bundle);
                    request.executeAsync();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void RegisterAccount(final String name, final String email, String password) {

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {

                            Toast.makeText(RegisterActivity.this,
                                    "เกิดข้อผิดพลาด: " + task.getException() + " กรุณาลองอีกครั้ง!", Toast.LENGTH_SHORT).show();

                        } else {

                            String current_user_id = mAuth.getCurrentUser().getUid();
                            //Toast.makeText(RegisterActivity.this,"id  : " + current_user_id, Toast.LENGTH_SHORT).show();
                            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);

                            mDatabaseRef.child("name").setValue(name);
                            mDatabaseRef.child("email").setValue(email);
                            mDatabaseRef.child("online").setValue(true);
                            mDatabaseRef.child("image").setValue("default_profile_pic");
                            mDatabaseRef.child("thumb_image").setValue("default");
                            mDatabaseRef.child("rank").setValue(0);
                            mDatabaseRef.child("level").setValue(0);
                            mDatabaseRef.child("score").setValue(0);
                            mDatabaseRef.child("bestScore").setValue(0)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {

                                                Toast.makeText(RegisterActivity.this,
                                                        "สำเร็จ!", Toast.LENGTH_SHORT).show();

                                                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                                //mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                            }
                                        }
                                    });
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(fireAthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(fireAthStateListener);
    }
}
