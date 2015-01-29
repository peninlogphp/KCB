package lipuka.android.model.database.handlers;

import kcb.android.LipukaApplication;
import lipuka.android.model.database.AsyncDatabaseHandler;
import lipuka.android.model.database.DatabaseResponse;

public class SetActivatedHandler extends AsyncDatabaseHandler {
		
	LipukaApplication lipukaApplication;
	
   public SetActivatedHandler(LipukaApplication lipukaApplication){
	   super();
	this.lipukaApplication = lipukaApplication;

   }
   
   public void onStart() {
    }

    public void onFinish() {
    
    }

    public void onSuccess(DatabaseResponse response) {
    	}
    public void onFailure(String error) {
    
    }
}