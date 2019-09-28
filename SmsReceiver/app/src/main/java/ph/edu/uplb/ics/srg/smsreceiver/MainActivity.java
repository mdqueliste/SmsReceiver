package ph.edu.uplb.ics.srg.smsreceiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final static int REQUEST_CODE_PERMISSION_RECEIVE=456;

    public static String odetteNum="+15555215554";
    //    public static String endPoint="http://10.0.3.57:6200";
    public static String endPoint="http://10.0.2.2:3001";
    public static String farmerNum="https://api.myjson.com/bins/9007q";
    private static InboxFragment inboxFragment;
    private static SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},REQUEST_CODE_PERMISSION_RECEIVE);

        inboxFragment = new InboxFragment(this);
        settingsFragment = new SettingsFragment(this);

        if(isNetworkConnected()) {
            BottomNavigationView navigation = findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(this);
            loadFragment(inboxFragment);  //initial fragment when app loads
        }else{
            Toast.makeText(this, "No internet connection found.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
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