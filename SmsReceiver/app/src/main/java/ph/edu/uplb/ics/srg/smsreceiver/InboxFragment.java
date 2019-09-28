package ph.edu.uplb.ics.srg.smsreceiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class InboxFragment extends Fragment {
    private Context context;
    private ArrayList<String> allmsg = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    public static InboxFragment instance;

    public static InboxFragment Instance(){
        return instance;
    }

    public InboxFragment(Context context){
        this.context=context;
        this.arrayAdapter = new ArrayAdapter(this.context, android.R.layout.simple_list_item_1, allmsg);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inbox, null);
        ListView lv = view.findViewById(R.id.lv);
        lv.setAdapter(arrayAdapter);    //listview for the list of messages
        instance=this;

        return view;
    }

    public void updateList(final String msg){
        arrayAdapter.insert(msg, 0);
        arrayAdapter.notifyDataSetChanged();
    }
}