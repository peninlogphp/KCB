package lipuka.android.model.responsehandlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;


import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Activity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class UpdateProfileInBgResponseHandler  extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;

   public UpdateProfileInBgResponseHandler(LipukaApplication lipukaApplication,
		   Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;   
}
   
   public void onStart() {
	   //lipukaApplication.showProgress("Sending Activation Request");
    }

    public void onFinish() {
    	
    	}

    public void onSuccess(String content) {
    	try{
  		    // Log.d(Main.TAG, "update profile response: "+content);

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
	                lipukaApplication.saveProfileUpdateInBg(response.getString("profile"), ecobankAccounts);
    	        }else{
    	        	}
                

  }else{

          }
      	}
          catch (org.json.JSONException jsonError) {
   		    // Log.d(Main.TAG, "JSONException: ", jsonError);
           	}

    	}
    public void onFailure(Throwable error) {
       		    // Log.d(Main.TAG, "Network error: "+error.getMessage());
    }
}