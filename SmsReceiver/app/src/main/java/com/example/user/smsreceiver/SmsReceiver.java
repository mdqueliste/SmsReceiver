package com.example.user.smsreceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String sender=null;
        String msg = null;

        if(bundle!=null){
            Object[] pdus = (Object[]) bundle.get("pdus");

            for(int i=0; i<pdus.length; i++){
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sender = sms.getOriginatingAddress();
                msg = sms.getDisplayMessageBody().toString();

                Toast.makeText(context, "From: " + sender + " Message: "+msg, Toast.LENGTH_LONG).show();
            }

            SmsManager smsManager =SmsManager.getDefault();
            smsManager.sendTextMessage(sender, null, msg, null, null);
        }
    }
}