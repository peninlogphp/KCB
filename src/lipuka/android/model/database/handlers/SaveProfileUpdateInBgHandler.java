package lipuka.android.model.database.handlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import lipuka.android.model.database.AsyncDatabaseHandler;
import lipuka.android.model.database.DatabaseResponse;
import android.app.Activity;
import android.util.Log;

public class SaveProfileUpdateInBgHandler extends AsyncDatabaseHandler {
		
	LipukaApplication lipukaApplication;
	Activity activity;

   public SaveProfileUpdateInBgHandler(LipukaApplication lipukaApplication, Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;
	this.activity = activity;   

   }
   
   public void onStart() {
    }

    public void onFinish() {
    
    }
   
    public void onSuccess(DatabaseResponse response) {
    
   Log.d(Main.TAG, "Profile update succeeded");	 	 
    	}
    public void onFailure(String error) {
       		     Log.d(Main.TAG, "Profile Update error: "+error);

    }
}