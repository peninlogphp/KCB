package lipuka.android.model.receivers;



import kcb.android.LipukaApplication;
import kcb.android.Main;

import org.json.JSONObject;




import pubnub.Callback;
import android.util.Log;

public class NotificationsReceiver implements Callback {
	
	LipukaApplication lipukaApplication;
	
	public NotificationsReceiver(LipukaApplication lipukaApplication){
	this.lipukaApplication = lipukaApplication;	
	}
    @Override
    public boolean execute(JSONObject message) {
        Log.d(Main.TAG, "Received Msg");

    	try {

    	if (message.getString("activated").equals("true")){
			lipukaApplication.fetchProfile(message);
		 }else{
				lipukaApplication.createActivationNotification(message.getString("reason"));
	            Log.d(Main.TAG, "not activated");				
			}
    	}catch (org.json.JSONException jsonError) {
            Log.d(Main.TAG, "JSON Error", jsonError);
         	}
    	catch (Exception error) {
            Log.d(Main.TAG, "Error", error);
         	}
    	return true;
    }
}
