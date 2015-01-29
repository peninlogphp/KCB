package lipuka.android.model;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;
 
public class SMS  
{
   
    public static void sendSMS(final Context context, String phoneNumber, String message)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
            new Intent(DELIVERED), 0);
 
        //---when the SMS has been sent---
        context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    	
                       Toast.makeText(context, "Activation SMS sent", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                       Toast.makeText(context, "Activation SMS failed", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "Activation SMS failed, No SMS service found", Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        //Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Activation SMS failed, Service turned off", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Activation SMS delivered. \nPlease wait for activation result", Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "Activation SMS not delivered", Toast.LENGTH_LONG).show();
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI); 
    }
}