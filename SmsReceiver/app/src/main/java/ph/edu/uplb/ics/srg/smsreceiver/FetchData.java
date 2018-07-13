package ph.edu.uplb.ics.srg.smsreceiver;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchData extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String num="";
    private static String body;

    private ArrayList<String> numbers = new ArrayList<>();

    public FetchData(Context context, String num, String body){
        this.context=context;
        this.num=num;
        this.body=body;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        readURL(MainActivity.endPoint);
        //==================================using save()=========================================//
        String[] data=body.split(", "); //split the message to get the point id

        if(data.length!=2){
            body=data[1]+", "+data[2]+", "+data[3]+", "+data[4]+", "+data[5]+", "+data[6]+", "+data[7]+", "+data[8];
        }else{
            body="done";
        }

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this.context);
        String url = "http://10.0.3.57:6200/points/"+data[0]+"/logs";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        if(body.equals("done")){
                            sendMessage(response);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "Error");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("message", body);
                return params;
            }
        };
        MyRequestQueue.add(postRequest);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void sendMessage(String result){
        //Sending of message
        for(int i=0; i<numbers.size(); i++){
            SmsManager smsManager = SmsManager.getDefault();
            List<String> messages = smsManager.divideMessage(result);
            for (String msg : messages) {
                smsManager.sendTextMessage(numbers.get(i), null, msg, null, null);
            }
            Toast.makeText(context, "Message forwarded.", Toast.LENGTH_LONG).show();
        }
    }

    private JSONArray readURL(String link){
        JSONArray ja=null;
        String data="";
        URL url;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        BufferedReader br;

        try {
            url = new URL(link); //get the JSON file from this url
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream));

            String line="";
            while(line!=null){
                line=br.readLine();
                data+=line;
            }
            ja = new JSONArray(data);
            if(ja!=null){
                for(int i=0; i<ja.length(); i++){
                    JSONObject jo = (JSONObject) ja.get(i);
                    numbers.add((String)jo.get("number"));
                }
            }
        }catch (MalformedURLException | JSONException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ja;
    }
}