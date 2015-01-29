package lipuka.android.model.database.handlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import lipuka.android.model.database.AsyncDatabaseHandler;
import lipuka.android.model.database.DatabaseResponse;
import android.app.Activity;
import android.util.Log;

public class SaveAppDataHandler extends AsyncDatabaseHandler {
		
	LipukaApplication lipukaApplication;

   public SaveAppDataHandler(LipukaApplication lipukaApplication){
	   super();
	this.lipukaApplication = lipukaApplication;

   }
   
   public void onStart() {
    }

    public void onFinish() {
    
    }
   
    public void onSuccess(DatabaseResponse response) {
    
   Log.d(Main.TAG, "save app data succeeded");	 	 
    	}
    public void onFailure(String error) {
       		     Log.d(Main.TAG, "save app data error: "+error);

    }
}