package ph.edu.uplb.ics.srg.smsreceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if(intent.getAction().equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED")){
            if(bundle!=null) {
                Object[] pdu = (Object[]) bundle.get("pdus"); //PDU-protocol data unit
                String msg = "";
                SmsMessage smsMessage;
                String num="";
                String body="";

                for (int i = 0; i < pdu.length; i++) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        String format = bundle.getString("format");
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdu[i], format);
                    }else{
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdu[i]);
                    }
                    body = smsMessage.getMessageBody().toString();  //get message body
                    num = smsMessage.getOriginatingAddress().toString();    //get sender's number

                    if(num.equals(MainActivity.getOdetteNum())){
                        msg += "Sender: " + num + "\nMessage: " + body;
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        FetchData process = new FetchData(context, body);  //only use save() if the number is that of Project Odette's
                        process.execute();
                    }
                }

                InboxFragment instance = InboxFragment.getInstance();
                instance.updateList(msg);
            }
        }
    }
}