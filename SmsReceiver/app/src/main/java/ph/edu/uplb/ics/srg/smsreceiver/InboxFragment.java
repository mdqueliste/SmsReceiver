package ph.edu.uplb.ics.srg.smsreceiver;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class InboxFragment extends Fragment {
    private Context context;
    private ArrayList<String> allmsg = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    public static InboxFragment instance;

    private final static int REQUEST_CODE_PERMISSION_READ=456;

    public static InboxFragment Instance(){
        return instance;
    }

    public InboxFragment(Context context){
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.inbox, null);
        ListView lv= view.findViewById(R.id.lv);
        arrayAdapter = new ArrayAdapter(this.context, android.R.layout.simple_list_item_1, allmsg);
        lv.setAdapter(arrayAdapter);    //listview for the list of messages
        instance=this;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            refreshInbox();
        } else requestPermissions(new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_PERMISSION_READ);

        return view;
    }

    public void refreshInbox(){
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);

        int indexBody = cursor.getColumnIndex("body");
        int indexAddress = cursor.getColumnIndex("address");

        if(indexBody<0 || !cursor.moveToFirst()) return; //if sms is empty

        arrayAdapter.clear();
        do{
            String num=cursor.getString(indexAddress);
            if(num.equals(MainActivity.odetteNum)){ //get the messages from phone inbox whose sender is Project Odette
                String str = "Sender: "+ num +"\nMessage: "+  cursor.getString(indexBody);
                arrayAdapter.add(str);
            }
        }while (cursor.moveToNext());
    }

    public void updateList(final String msg){
        arrayAdapter.insert(msg, 0);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION_READ && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //this.refresh.setEnabled(true);
            refreshInbox();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}