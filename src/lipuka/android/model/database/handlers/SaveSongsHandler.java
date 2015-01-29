package lipuka.android.model.database.handlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import lipuka.android.model.database.AsyncDatabaseHandler;
import lipuka.android.model.database.DatabaseResponse;
import android.util.Log;

public class SaveSongsHandler extends AsyncDatabaseHandler {
		
	LipukaApplication lipukaApplication;
	
   public SaveSongsHandler(LipukaApplication lipukaApplication){
	   super();
	this.lipukaApplication = lipukaApplication;

   }
   
   public void onStart() {
    }

    public void onFinish() {
    
    }

    public void onSuccess(DatabaseResponse response) {
	     Log.d(Main.TAG, "Saved songs");	 	 
    	}
    public void onFailure(String error) {
    
    }
}