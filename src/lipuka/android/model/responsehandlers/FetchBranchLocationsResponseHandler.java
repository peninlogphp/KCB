package lipuka.android.model.responsehandlers;

import kcb.android.FindAtm;
import kcb.android.FindBranch;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import lipuka.android.model.DbHelper;


import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Activity;
import android.content.Intent;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;


import kcb.android.R;

public class FetchBranchLocationsResponseHandler extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;
   public FetchBranchLocationsResponseHandler(LipukaApplication lipukaApplication,
		   Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;  
   }
   
   public void onStart() {
	     Log.d(Main.TAG, "started fetching branch locations remotely");	 	 

    }

    public void onFinish() {
    	
	     Log.d(Main.TAG, "called onfinish");

    }

    public void onSuccess(String content) {
	     Log.d(Main.TAG, "got branch locations: "+content);
	    
	     if (activity == lipukaApplication.getCurrentActivity()){
	 		 	lipukaApplication.dismissProgressDialog();
	         }
	   //  content = content.substring(content.indexOf("{")); 
    	  try {

    		JSONObject response = new JSONObject(content);
    		//JSONObject songsObject = response.getJSONObject("songs");
   	    // Log.d(Main.TAG, "name: "+songsObject.getString("contentName"));

        if (response.getInt("STATUS_CODE") == 1){
        	JSONArray locationsArray = response.getJSONArray("branches");

        	lipukaApplication.saveBranchLocations(locationsArray);
            if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
            	return;
            }       
        	((FindBranch)activity).addOverlays(locationsArray);

}else{
	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
		lipukaApplication.showGenericNotification(response.getString("STATUS_MESSAGE"), activity.getClass(), R.id.notification_response);
	return;
    }
	lipukaApplication.setCurrentDialogTitle("Response");
	lipukaApplication.setCurrentDialogMsg(response.getString("STATUS_MESSAGE"));
	lipukaApplication.showDialog( Main.DIALOG_ERROR_ID);
   	((FindBranch)activity).setAutoCompleteData(new JSONArray());

        }
    	}
        catch (org.json.JSONException jsonError) {
   	     Log.d(Main.TAG, "jsonError: ", jsonError);
   	  if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
  		lipukaApplication.showGenericNotification("Sorry, there was an error in getting a response, please try again later", activity.getClass(), R.id.notification_response);
 	return;
      }
   	lipukaApplication.setCurrentDialogTitle("Error");
	lipukaApplication.setCurrentDialogMsg("Sorry, there was an error in getting a response, please try again later");
	lipukaApplication.showDialog( Main.DIALOG_ERROR_ID);
   	((FindBranch)activity).setAutoCompleteData(new JSONArray());
       }
        
    	}
    public void onFailure(Throwable error) {
    	if (activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }	
    	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
      		lipukaApplication.showGenericNotification(activity.getString(R.string.network_error), activity.getClass(), R.id.notification_response);
   	return;
        }
    	
    	lipukaApplication.setCurrentDialogTitle("Network Error");
    	  lipukaApplication.setCurrentDialogMsg(activity.getString(R.string.network_error));
	lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
  	  	     Log.d(Main.TAG, "Network error: "+error.getMessage());
	  	     	((FindBranch)activity).setAutoCompleteData(new JSONArray());

    }
}