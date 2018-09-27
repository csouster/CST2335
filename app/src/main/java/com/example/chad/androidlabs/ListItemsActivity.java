package com.example.chad.androidlabs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.content.Context;

public class ListItemsActivity extends Activity {

    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    protected static final int REQUEST_IMAGE_CAPTURE = 1;

    protected ImageButton imageButton;
    protected Switch switchStatus;
    protected CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "In onCreate");

        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        switchStatus = findViewById(R.id.switchButton);

        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(switchStatus.isChecked()){
                    CharSequence text = getString(R.string.switchOnMessage);
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }else{
                    CharSequence text = getString(R.string.switchOffMessage);
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                    builder.setMessage(R.string.dialog_Message);
                    builder.setTitle(R.string.dialog_Title);
                    builder.setPositiveButton(R.string.dialog_Confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked Continue
                            //CharSequence text = "Exited Activity Successfully!";
                            //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("Response", getString(R.string.Intent_Message));
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    });

                    builder.setNegativeButton(R.string.dialog_Deny, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User cancelled the dialog
                            //CharSequence text = "Exit Activity Aborted!";
                            //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.show();


                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageButton.setImageBitmap(imageBitmap);
        }
    }

}
