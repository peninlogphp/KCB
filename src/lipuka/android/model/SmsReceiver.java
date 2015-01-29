package lipuka.android.model;


import java.util.StringTokenizer;

import kcb.android.Main;







import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
 
public class SmsReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        //---get the SMS message passed in---
     try{
    	 Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String str = "";            
        String originator = null, payload =  null;
        StringBuffer payloadBuffer = new StringBuffer();

        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];            
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                str += "SMS from " + msgs[i].getOriginatingAddress();                     
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "\n";        
                originator = msgs[i].getOriginatingAddress(); 
                payloadBuffer.append(msgs[i].getMessageBody().toString());
            }
            payload = payloadBuffer.toString();
            
            //---display the new SMS message---
//15555215554 
            Log.d(Main.TAG, payload);

        if(originator.equals("5500") || originator.equals("ECOBANK") || originator.equals("8089")|| originator.equals("+254724899311")
        		 ){  
      		StringTokenizer tokens = new StringTokenizer(payload, "*");
      		String token = tokens.nextToken();
      			        if (token.equals("1") || token.equals("0")){
      			        	Intent postMessageintent = new Intent(context,
      			            		ActivationService.class);

      				        postMessageintent.putExtra(ActivationService.ORIGINATOR, originator);
      				        postMessageintent.putExtra(ActivationService.PAYLOAD, payload);
      			        	 context.startService(postMessageintent); // 
      			             Log.d(Main.TAG, str);
      			         	abortBroadcast ();
      		    		 }else if (token.equals("SA")){
      			        	Intent postMessageintent = new Intent(context,
      			            		SelfActivationService.class);

      				        postMessageintent.putExtra(ActivationService.ORIGINATOR, originator);
      				        postMessageintent.putExtra(ActivationService.PAYLOAD, payload);
      			        	 context.startService(postMessageintent); // 
      			             Log.d(Main.TAG, str);
      			         	abortBroadcast ();
      		    		 }else if(payload.contains("Confirmed, one time pin change for")){
      		    			Intent setOTPIntent = new Intent(context,
      			            		SetOTPService.class);
      		    			setOTPIntent.putExtra(ActivationService.ORIGINATOR, originator);

      			        	 context.startService(setOTPIntent); 	 
      		    		 }

}
        } 
    }
	catch (Exception error) {
        Log.d(Main.TAG, "Error", error);
     	} 
    }
}