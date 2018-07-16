package ph.edu.uplb.ics.srg.smsreceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if(intent.getAction().equalsIgnoreCase("android.provider.Telephony.SMS_RECEIVED")){
            if(bundle!=null) {
                Object[] pdu = (Object[]) bundle.get("pdus"); //protocol data unit
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
                    body = smsMessage.getMessageBody().toString();
                    num = smsMessage.getOriginatingAddress().toString();

                    if(num.equals(MainActivity.odetteNum)){
                        msg += "Sender: " + num + " Message: " + body;
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        FetchData process = new FetchData(context, num, body);
                        process.execute();
                    }
                }
            }
        }
    }
}