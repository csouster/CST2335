package com.example.chad.androidlabs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends Activity {

    protected static String ACTIVITY_NAME = "LoginActivity";
    protected Button loginBotton;
    SharedPreferences sharedPref;
    EditText emailAddressInput;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate");

        loginBotton = findViewById(R.id.buttonLogin);
        emailAddressInput = findViewById(R.id.editTextEmail);
        sharedPref = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        emailAddressInput.setText(sharedPref.getString("emailAddress", "DefaultEmail@domain.com"));

        loginBotton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sharedPref.edit().putString("emailAddress", emailAddressInput.getText().toString()).apply();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume");

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart");

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause");

    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop");

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy");

    }




}
