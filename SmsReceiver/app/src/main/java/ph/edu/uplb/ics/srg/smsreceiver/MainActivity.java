package ph.edu.uplb.ics.srg.smsreceiver;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SmsReceiver.Listener{

    public static String odetteNum="+15555215554";
    //    public static String endPoint="http://10.0.3.57:6200";
    public static String endPoint="http://10.0.2.2:3001";
    public static String farmerNum="https://api.myjson.com/bins/9007q";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SmsReceiver.setListener(this);

        if(isNetworkConnected()) {
            BottomNavigationView navigation = findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(this);
            loadFragment(new InboxFragment(this));  //initial fragment when app loads
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
                fragment= new InboxFragment(this);
                break;
            case R.id.navigation_settings:
                fragment=new SettingsFragment(this);
                break;
        }
        return loadFragment(fragment);
    }

    public void onTextReceived(String text) {
        Toast.makeText(this, "SMS received.", Toast.LENGTH_LONG).show();
    }
}