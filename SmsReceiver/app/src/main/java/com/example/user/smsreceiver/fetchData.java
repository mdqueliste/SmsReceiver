package com.example.user.smsreceiver;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class fetchData extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String num="";
    private static String body;

    public fetchData (Context context, String num, String body){
        this.context=context;
        this.num=num;
        this.body=body;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //==================================using save()=========================================//
        String[] data=body.split(", "); //split the message to get the point id

        if(data.length!=2){
            body=data[1]+", "+data[2]+", "+data[3]+", "+data[4]+", "+data[5]+", "+data[6]+", "+data[7]+", "+data[8];
        }else{
            body="done";
        }

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this.context);
        String url = "http://10.0.3.57:6200/points/"+data[0]+"/logs";

        HashMap<String, String> MyData = new HashMap<String,String>();
        MyData.put("message", body);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject(MyData),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            String result=response.toString();
                            Log.i("MyActivity", result);
                            if(body.equals("done")){
                                sendMessage(result);
                            }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error with sending data.", Toast.LENGTH_LONG).show();
            }
        });
        MyRequestQueue.add(jsObjRequest);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void sendMessage(String result){
        //TODO: fix format later upon fixed deployment
        //Sending of message
        SmsManager smsManager = SmsManager.getDefault();
        List<String> messages = smsManager.divideMessage(result);
        for (String msg : messages) {
            smsManager.sendTextMessage(this.num, null, msg, null, null);
        }
        Toast.makeText(context, "Message forwarded.", Toast.LENGTH_LONG).show();
    }
}