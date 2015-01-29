package lipuka.android.model;

import kcb.android.Main;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver { // 

  @Override
  public void onReceive(Context context, Intent intent) { // 
    context.startService(new Intent(context, NotificationsService.class)); // 
    Log.d(Main.TAG, "Boot Intent Received");
  }

}