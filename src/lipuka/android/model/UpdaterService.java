package lipuka.android.model;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import lipuka.android.model.responsehandlers.UpdateResponseHandler;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import com.loopj.android.http.AsyncHttpClient;



public class UpdaterService extends Service {

	static final int DELAY = 300000; // 5 minutes 
	  private boolean runFlag = false;  // 
	  private Updater updater;
	  private AsyncHttpClient asyncHttpClient;
		LipukaApplication lipukaApplication;
UpdateResponseHandler handler;
  @Override
  public IBinder onBind(Intent intent) { // 
    return null;
  }

  @Override
  public void onCreate() { // 
    super.onCreate();
    Log.d(Main.TAG, "Service Created");
    this.updater = new Updater(); // 
    this.asyncHttpClient = new AsyncHttpClient(); // 
    lipukaApplication = (LipukaApplication)getApplication();
handler = new UpdateResponseHandler(lipukaApplication);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) { // 
    super.onStartCommand(intent, flags, startId);
    Log.d(Main.TAG, "Service Started");
    if(!runFlag){
    this.runFlag = true; // 
    this.updater.start();   
    }
    //lipukaApplication.checkServiceXml("");
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
      UpdaterService updaterService = UpdaterService.this;  // 
      while (updaterService.runFlag) {  // 
        Log.d(Main.TAG, "Updater running");
        try {
        	//lipukaApplication.getNotifications(handler);
        	Log.d(Main.TAG, "Updater ran");
          Thread.sleep(DELAY);  // 
        } catch (InterruptedException e) {  // 
          updaterService.runFlag = false;
        }
      }
    }
  } // Updater
  
}