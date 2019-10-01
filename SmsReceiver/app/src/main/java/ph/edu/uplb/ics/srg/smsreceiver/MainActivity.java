package ph.edu.uplb.ics.srg.smsreceiver;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final static int REQUEST_CODE_PERMISSION = 456;

    private String[] appPermissions = {
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS
    };

    private static String odetteNum     = "+15555215554";
//    public static String endPoint = "http://10.0.3.57:6200";
    private static String endPoint      = "http://10.0.2.2:3001";
//    private static String stakeholders  ="https://api.myjson.com/bins/xh0d5";   // my number
    private static String stakeholders  ="https://api.myjson.com/bins/1aotzt";   // emulator number

    private static InboxFragment inboxFragment;
    private static SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inboxFragment = new InboxFragment(this);
        settingsFragment = new SettingsFragment(this);


        if (isNetworkConnected()) {
            if(!checkAndRequestPermissions()) {
                closeNow();
            }
            BottomNavigationView navigation = findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(this);
            loadFragment(inboxFragment);  //initial fragment when app loads
        } else {
            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
            closeNow();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private boolean checkAndRequestPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();
        for(String perm : appPermissions) {
            if(ActivityCompat.checkSelfPermission(this,perm) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(perm);
            }
        }

        if(!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                        REQUEST_CODE_PERMISSION);
            return false;
        }
        return true;
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    static String getOdetteNum() {
        return MainActivity.odetteNum;
    }

    static String getEndPoint() {
        return MainActivity.endPoint;
    }

    static String getStakeholders() {
        return MainActivity.stakeholders;
    }

    static void setOdetteNum(String value) {
        MainActivity.odetteNum = value;
    }

    static void setEndPoint(String value) {
        MainActivity.endPoint = value;
    }

    static void setStakeholders(String value) {
        MainActivity.stakeholders = value;
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment=null;
        switch (menuItem.getItemId()){  //switch case for which option is selected in the bottom navigation
            case R.id.navigation_inbox:
                fragment = inboxFragment;
                break;
            case R.id.navigation_settings:
                fragment = settingsFragment;
                break;
        }
        return loadFragment(fragment);
    }

}