package lipuka.android.model;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import lipuka.android.model.receivers.NotificationsReceiver;
import pubnub.Pubnub;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationsService extends Service {

	  private boolean runFlag = false;  // 
	  private Updater updater;
		LipukaApplication lipukaApplication;
NotificationsReceiver receiver;

  @Override
  public IBinder onBind(Intent intent) { // 
    return null;
  }

  @Override
  public void onCreate() { // 
    super.onCreate();
    Log.d(Main.TAG, "Service Created");
    this.updater = new Updater(); // 
    lipukaApplication = (LipukaApplication)getApplication();
receiver = new NotificationsReceiver(lipukaApplication);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) { // 
    super.onStartCommand(intent, flags, startId);
    Log.d(Main.TAG, "Service Started");
    if(!runFlag){
    this.runFlag = true; // 
    this.updater.start();   
    }
    return START_STICKY;
  }

  @Override
  public void onDestroy() { // 
    super.onDestroy();
    this.runFlag = false; // 
    this.updater.interrupt(); // 
    this.updater = null;
    Log.d(Main.TAG, "Service Destroyed");
  }
  
  /**
   * Thread that performs the actual update from the online service
   */
  private class Updater extends Thread {  // 

    public Updater() {
      super("UpdaterService-Updater");  // 
    }

    @Override
    public void run() { // 
      NotificationsService updaterService = NotificationsService.this;  // 
      while (updaterService.runFlag) {  // 
        Log.d(Main.TAG, "Updater running");
    	   Pubnub pn = new Pubnub( "demo",       
        		"demo", "", true );
        NotificationsReceiver rcv = new NotificationsReceiver(lipukaApplication);
        try {
        		pn.subscribe( lipukaApplication.getIMSI(), rcv ); 
      }catch (Exception e){
          Log.d(Main.TAG, "subscribe exception", e);

      }
        Log.d(Main.TAG, "Subscribe done");
       
      }
    }
  } // Updater
  
}