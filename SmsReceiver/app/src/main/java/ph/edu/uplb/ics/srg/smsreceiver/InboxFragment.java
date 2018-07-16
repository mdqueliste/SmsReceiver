package ph.edu.uplb.ics.srg.smsreceiver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class InboxFragment extends Fragment {
    private Context context;
    private View view;
    private Button refresh;
    private ListView lv;
    private ArrayList<String> allmsg = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    public InboxFragment(Context context){
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.inbox, null);
        this.lv= view.findViewById(R.id.lv);
        arrayAdapter = new ArrayAdapter(this.context, android.R.layout.simple_list_item_1, allmsg);
        lv.setAdapter(arrayAdapter);
        this.refresh = view.findViewById(R.id.refresh);
        this.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshInbox();
            }
        });
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
            if(num.equals(MainActivity.odetteNum)){
                String str = "Sender: "+ num +"\n Message: "+  cursor.getString(indexBody);
                arrayAdapter.add(str);
            }
        }while (cursor.moveToNext());
    }
}