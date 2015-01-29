package lipuka.android.model.database.handlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import lipuka.android.model.DbHelper;
import lipuka.android.model.database.AsyncDatabaseHandler;
import lipuka.android.model.database.DatabaseResponse;
import android.database.Cursor;
import android.util.Log;

public class FetchServicesHandler extends AsyncDatabaseHandler {
		
	LipukaApplication lipukaApplication;
	
   public FetchServicesHandler(LipukaApplication lipukaApplication){
	   super();
	this.lipukaApplication = lipukaApplication;

   }
   
   public void onStart() {
    }

    public void onFinish() {
    
    }

    public void onSuccess(DatabaseResponse response) {
		Cursor cursor = response.getCursor();

		int accountIndex = cursor.getColumnIndex(DbHelper.C_ACCOUNT);
        int serviceXmlIndex = cursor.getColumnIndex(DbHelper.C_SERVICE_XML);
        Log.d(Main.TAG, "Got services");

                while (cursor.moveToNext()){
                	String serviceXML = cursor.getString(serviceXmlIndex);
   	     lipukaApplication.checkServiceXml(cursor.getString(accountIndex), lipukaApplication.getServiceVersion(serviceXML));
         Log.d(Main.TAG, "Checking service: "+cursor.getString(accountIndex));

                }
    	}
    public void onFailure(String error) {
        Log.d(Main.TAG, "fetching services failed: "+error);

    }
}