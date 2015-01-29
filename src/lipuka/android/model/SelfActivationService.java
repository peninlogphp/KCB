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

public class SelfActivationService extends Service {

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
    Log.d(Main.TAG, "SelfActivationService Created");
    lipukaApplication = (LipukaApplication)getApplication();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) { // 
    super.onStartCommand(intent, flags, startId);
    Log.d(Main.TAG, "SelfActivationService Started");
    Bundle extra = intent.getExtras();
    if(extra != null){
    	try {

    		String payload = extra.getString(PAYLOAD);
    		StringTokenizer tokens = new StringTokenizer(payload, "*");
    		tokens.nextToken();
    			lipukaApplication.fetchProfileAfterSA(tokens.nextToken());

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