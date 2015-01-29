package lipuka.android.model.database.handlers;


import kcb.android.FindAtm;
import kcb.android.LipukaApplication;
import kcb.android.Main;

import org.json.JSONArray;
import org.json.JSONObject;





import kcb.android.R;
import lipuka.android.model.DbHelper;
import lipuka.android.model.database.AsyncDatabaseHandler;
import lipuka.android.model.database.DatabaseResponse;
import lipuka.android.model.responsehandlers.FetchLariAtmLocationsResponseHandler;
import lipuka.android.model.responsehandlers.FetchOtherBanksAtmLocationsResponseHandler;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class FetchOtherBanksAtmLocationsHandler extends AsyncDatabaseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;
   public FetchOtherBanksAtmLocationsHandler(LipukaApplication lipukaApplication,
		   Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;   

   }
   
   public void onStart() {
	   lipukaApplication.showProgress("Fetching content...");
    }

    public void onFinish() {
    	
	     Log.d(Main.TAG, "called onfinish");

    }
    public void onSuccess(DatabaseResponse response) {
	     Log.d(Main.TAG, "got other banks atm locations");
	     Cursor locationsCursor = response.getCursor();
    	try{	
    	
        if (locationsCursor.moveToFirst()){
        	
        	if (activity == lipukaApplication.getCurrentActivity()){
	 		 	lipukaApplication.dismissProgressDialog();
	         }
        	
        	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
       	     Log.d(Main.TAG, "activity not visible"+activity.getClass());
	locationsCursor.close();
            	return;
            }
       	
        int total = locationsCursor.getCount();
        
    int dataIndex = locationsCursor.getColumnIndex(DbHelper.C_JSON_DATA);
    JSONArray locationsArray = new JSONArray();
    
            for (int i = 0; i < total ; i++){
        JSONObject currentLocation = new JSONObject(locationsCursor.getString(dataIndex));
        locationsArray.put(i, currentLocation);
        locationsCursor.moveToNext();

            }
                     
        	((FindAtm)activity).addOverlays(locationsArray);
       
}else{
	lipukaApplication.putPayload("bank_id", "2");
		lipukaApplication.setServiceID("6");
		lipukaApplication.consumeService(lipukaApplication.getServiceID(), new FetchOtherBanksAtmLocationsResponseHandler(lipukaApplication, activity));
	            }
    	}
        catch (org.json.JSONException jsonError) {
   	     Log.d(Main.TAG, "jsonError: ", jsonError);
   	  lipukaApplication.putPayload("bank_id", "2");
 		lipukaApplication.setServiceID("6");
 		lipukaApplication.consumeService(lipukaApplication.getServiceID(), new FetchOtherBanksAtmLocationsResponseHandler(lipukaApplication, activity));
 	            }
        locationsCursor.close();
    	}
    public void onFailure(String error) {
    	
    	lipukaApplication.putPayload("bank_id", "2");
   		lipukaApplication.setServiceID("6");
   		lipukaApplication.consumeService(lipukaApplication.getServiceID(), new FetchOtherBanksAtmLocationsResponseHandler(lipukaApplication, activity));
   	    
    }
}