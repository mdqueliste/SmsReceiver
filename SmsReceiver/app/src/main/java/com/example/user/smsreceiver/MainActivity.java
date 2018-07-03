package com.example.user.smsreceiver;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private final static int SEND_PERMISSION_CODE=123;
    private final static int READ_PERMISSION_CODE=456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkPermission(Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{(Manifest.permission.READ_SMS)}, READ_PERMISSION_CODE);
        }

        if(!checkPermission(Manifest.permission.SEND_SMS)){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {(Manifest.permission.SEND_SMS)}, SEND_PERMISSION_CODE);
        }
    }

    private boolean checkPermission (String permission){
        int checkPermission = ContextCompat.checkSelfPermission(this, permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case SEND_PERMISSION_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                break;
        }
    }
}
