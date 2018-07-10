package com.example.user.smsreceiver;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button refresh;

    private ListView lv;
    private ArrayList<String> allmsg = new ArrayList<String>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv= (ListView) findViewById(R.id.lv);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allmsg);
        lv.setAdapter(arrayAdapter);
        refresh = (Button) findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshInbox();
            }
        });
    }

    public void refreshInbox(){
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);

        int indexBody = cursor.getColumnIndex("body");
        int indexAddress = cursor.getColumnIndex("address");

        if(indexBody<0 || !cursor.moveToFirst()) return; //if sms is empty

        arrayAdapter.clear();
        do{
            String str = "Sender: "+ cursor.getString(indexAddress)+"\n Message: "+  cursor.getString(indexBody);
            arrayAdapter.add(str);
        }while (cursor.moveToNext());
    }

    public void updateList(String latest){
        arrayAdapter.insert(latest, 0);
        arrayAdapter.notifyDataSetChanged();
    }
}