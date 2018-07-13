package com.example.user.smsreceiver;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button refresh;

    private ListView lv;
    private ArrayList<String> allmsg = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lv= findViewById(R.id.lv);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allmsg);
        lv.setAdapter(arrayAdapter);
        this.refresh = findViewById(R.id.refresh);
        this.refresh.setOnClickListener(new View.OnClickListener() {
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
}