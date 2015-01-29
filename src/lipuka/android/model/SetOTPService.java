package lipuka.android.model;

import kcb.android.EcobankHome;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import kcb.android.R;

public class SetOTPService extends Service {

	  LipukaApplication lipukaApplication;
	  

  @Override
  public IBinder onBind(Intent intent) { // 
    return null;
  }

  @Override
  public void onCreate() { // 
    super.onCreate();
    Log.d(Main.TAG, "SetOTPService Created");
    lipukaApplication = (LipukaApplication)getApplication();
  }
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) { // 
    super.onStartCommand(intent, flags, startId);
    Log.d(Main.TAG, "SetOTPService Started");
    Bundle extra = intent.getExtras();
    if(extra != null){
    		String originator = extra.getString(ActivationService.ORIGINATOR);
    		String clientID = null;
    	if (originator.equals("8089")){clientID = LipukaApplication.CLIENT_ID+"_";}
    	else if (originator.equals("5221")){clientID = "1_";}
    	else if (originator.equals("4585")){clientID = "4_";}
    lipukaApplication.putOTP(clientID, true);
    lipukaApplication.showChangeOTPNotification(lipukaApplication.getString(R.string.change_otp_confirmed), EcobankHome.class);
    }
    stopSelf();
    return START_STICKY;
  }

  @Override
  public void onDestroy() { // 
    super.onDestroy();
    Log.d(Main.TAG, "SetOTPService Destroyed");
  }
  
}