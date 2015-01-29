package lipuka.android.model.sms;


import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
 
public class ActivateSMS  
{
   
	LipukaApplication lipukaApplication;
	Activity activity;

   public ActivateSMS(LipukaApplication lipukaApplication,
		   Activity activity){
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;   
}
   
    public void sendSMS(String phoneNumber, String message)
    {        
 	   lipukaApplication.showProgress("Sending Activation Request");

 	   String SENT = "lipuka.android.ACTIVATION_SMS_SENT";
        String DELIVERED = "lipuka.android.ACTIVATION_SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(lipukaApplication, R.id.pending_intent_activation,
            new Intent(SENT), PendingIntent.FLAG_UPDATE_CURRENT);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(lipukaApplication, R.id.pending_intent_activation,
            new Intent(DELIVERED), PendingIntent.FLAG_UPDATE_CURRENT);
 
        //---when the SMS has been sent---
        lipukaApplication.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
            	String message = null;
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    	message = lipukaApplication.getString(R.string.activation_sms_sent);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    	message = lipukaApplication.getString(R.string.activation_sms_failed);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                    	message = lipukaApplication.getString(R.string.activation_sms_service_unavailable);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        //Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    	message = lipukaApplication.getString(R.string.activation_sms_service_off);
                        break;
                }
                if (activity == lipukaApplication.getCurrentActivity()){
        		 	lipukaApplication.dismissProgressDialog();
                }
            	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
          lipukaApplication.createActivationNotification(message);
    	return;
                }
            	lipukaApplication.setCurrentDialogTitle("Activation");
            	lipukaApplication.setCurrentDialogMsg(message);
        lipukaApplication.showDialog(Main.DIALOG_MSG_ID);
           
            }
        }, new IntentFilter(SENT));
 
        //---when the SMS has been delivered---
        lipukaApplication.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
        	lipukaApplication.createActivationNotification(lipukaApplication.getString(R.string.activation_sms_delivered));
                        break;
                    case Activity.RESULT_CANCELED:
           lipukaApplication.createActivationNotification(lipukaApplication.getString(R.string.activation_sms_not_delivered));
                        break;                        
                }
            }
        }, new IntentFilter(DELIVERED));        
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI); 
    }
}