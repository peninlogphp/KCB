package lipuka.android.model.database.handlers;

import kcb.android.LipukaApplication;
import lipuka.android.model.database.AsyncDatabaseHandler;
import lipuka.android.model.database.DatabaseResponse;

public class SaveServiceHandler extends AsyncDatabaseHandler {
		
	LipukaApplication lipukaApplication;
	
   public SaveServiceHandler(LipukaApplication lipukaApplication){
	   super();
	this.lipukaApplication = lipukaApplication;

   }
   
   public void onStart() {
    }

    public void onFinish() {
    
    }

    public void onSuccess(DatabaseResponse response) {
	     lipukaApplication.checkActivation();
    	}
    public void onFailure(String error) {
    
    }
}