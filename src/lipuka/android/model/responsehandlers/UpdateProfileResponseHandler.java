package lipuka.android.model.responsehandlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;


import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Activity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class UpdateProfileResponseHandler  extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;

   public UpdateProfileResponseHandler(LipukaApplication lipukaApplication,
		   Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;   
}
   
   public void onStart() {
	   lipukaApplication.showProgress("Sending Activation Request");
    }

    public void onFinish() {
    	
    	}

    public void onSuccess(String content) {
    	try{
  		     Log.d(Main.TAG, "update profile response: "+content);

    		JSONObject response = new JSONObject(content);
    		if (response.getString("updated").equals("true")){
    			JSONObject accounts = response.getJSONObject("accounts");
    			JSONObject ecobankAccounts;
                JSONArray accountsArray = accounts.names();

    			int length = accountsArray.length();

    			int count = 0;
    	        for(int i = 0; i < length; i++){
    	        			if(accountsArray.getString(i).contains(LipukaApplication.CLIENT_ID+"_")){
    	        				count++;			
    	        			}
    	        						
    	        		}
    	        if(count != 0){
    	        	ecobankAccounts = new JSONObject();
    	        		for(int i = 0; i < length; i++){
    	        if(accountsArray.getString(i).contains(LipukaApplication.CLIENT_ID+"_")){
    	        	ecobankAccounts.put(accountsArray.get(i).toString(), accounts.get(accountsArray.get(i).toString()));
    	        }
    	        }
	                lipukaApplication.saveProfileUpdate(response.getString("profile"), ecobankAccounts);
    	        }else{
    	        	if (lipukaApplication.getCurrentActivity() != null && activity == lipukaApplication.getCurrentActivity()){
    	    		 	lipukaApplication.dismissProgressDialog();
    	          }
    	    	  if (activity != lipukaApplication.getCurrentActivity() || !lipukaApplication.isActivityVisible(activity.getClass())){
    	    		  lipukaApplication.showProfileUpdateNotification("You no longer have any accounts");
		  return;
    	          }
    	              	lipukaApplication.setCurrentDialogTitle("Profile update failed");
    	              	lipukaApplication.setCurrentDialogMsg("You no longer have any accounts");
    	              	lipukaApplication.showDialog( Main.DIALOG_PROFILE_UPDATE_ERROR_ID);
    	              	}
                

  }else{
  	if (lipukaApplication.getCurrentActivity() != null && activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
      }
	  if (activity != lipukaApplication.getCurrentActivity() || !lipukaApplication.isActivityVisible(activity.getClass())){
		  lipukaApplication.showProfileUpdateNotification("Update failed "+response.getString("reason"));
		  return;
      }
          	lipukaApplication.setCurrentDialogTitle("Profile update failed");
          	lipukaApplication.setCurrentDialogMsg(response.getString("reason"));
          	lipukaApplication.showDialog( Main.DIALOG_PROFILE_UPDATE_ERROR_ID);
          }
      	}
          catch (org.json.JSONException jsonError) {
   		     Log.d(Main.TAG, "JSONException: ", jsonError);
   		  if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
   	  		lipukaApplication.showProfileUpdateNotification("Sorry, there was an error in getting a response, please try again later");
   		return;
   	      }
   		   	lipukaApplication.setCurrentDialogTitle("Error");
   		  	  lipukaApplication.setCurrentDialogMsg("Sorry, there was an error in getting a response, please try again later");
   	          	lipukaApplication.showDialog( Main.DIALOG_PROFILE_UPDATE_ERROR_ID);
           	}

    	}
    public void onFailure(Throwable error) {
    	if (lipukaApplication.getCurrentActivity() != null && activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
    	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
  		  lipukaApplication.showProfileUpdateNotification(activity.getString(R.string.network_error));
return;
        }
    	lipukaApplication.setCurrentDialogTitle("Network Error");
       	lipukaApplication.setCurrentDialogMsg(activity.getString(R.string.network_error));

       		     Log.d(Main.TAG, "Network error: "+error.getMessage());

    	lipukaApplication.showDialog(Main.DIALOG_PROFILE_UPDATE_ERROR_ID);
    }
}