package lipuka.android.model;


import java.util.StringTokenizer;

import kcb.android.LipukaApplication;
import kcb.android.Main;



import org.json.JSONObject;






import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class ActivationService extends Service {

	  public static final byte SMS = 1;
	  public static final byte ALARM = 2;
	  public static final byte BOOT = 3;

	  public static final String URL = "http://zion.cellulant.com/AndroidAppMessageLatencyTest/appMessageAcknowledger.php";
	  public static final String ORIGINATOR = "ORIGINATOR";
	  public static final String PAYLOAD = "PAYLOAD";
	  public static final String TIMESTAMP = "TIMESTAMP";
	  public static final String CALLER = "CALLER";
	  LipukaApplication lipukaApplication;
	  

  @Override
  public IBinder onBind(Intent intent) { // 
    return null;
  }

  @Override
  public void onCreate() { // 
    super.onCreate();
    Log.d(Main.TAG, "ActivationService Created");
    lipukaApplication = (LipukaApplication)getApplication();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) { // 
    super.onStartCommand(intent, flags, startId);
    Log.d(Main.TAG, "ActivationService Started");
    Bundle extra = intent.getExtras();
    if(extra != null){
    	try {

    		String payload = extra.getString(PAYLOAD);
    		StringTokenizer tokens = new StringTokenizer(payload, "*");
    		
        	if (tokens.nextToken().equals("1")){
        		JSONObject message = new JSONObject();
        		JSONObject accounts = new JSONObject();
        		message.put("profile", tokens.nextToken());
        		message.put("servicexml", tokens.nextToken());
        		while(tokens.hasMoreTokens()){
            		String account = tokens.nextToken();
        			accounts.put(account, account);
        		}
        		message.put("accounts", accounts);
    			lipukaApplication.fetchProfile(message);
    		 }else{
    			 lipukaApplication.putNumber(tokens.nextToken());
    				lipukaApplication.createActivationNotification(tokens.nextToken());
    	            Log.d(Main.TAG, "not activated");				
    			}
        	}catch (org.json.JSONException jsonError) {
                Log.d(Main.TAG, "JSON Error", jsonError);
             	}
        	catch (Exception error) {
                Log.d(Main.TAG, "Error", error);
             	}        
    }
    stopSelf();
    return START_STICKY;
  }

  @Override
  public void onDestroy() { // 
    super.onDestroy();
    Log.d(Main.TAG, "Service Destroyed");
  }
  
}