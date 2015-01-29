package lipuka.android.model.database.handlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import lipuka.android.model.database.AsyncDatabaseHandler;
import lipuka.android.model.database.DatabaseResponse;


import org.json.JSONArray;
import org.json.JSONObject;






import android.util.Log;

public class SaveProfileHandler extends AsyncDatabaseHandler {
		
	LipukaApplication lipukaApplication;
	JSONObject message;

   public SaveProfileHandler(LipukaApplication lipukaApplication, JSONObject message){
	   super();
	this.lipukaApplication = lipukaApplication;
	this.message = message;

   }
   
   public void onStart() {
    }

    public void onFinish() {
    
    }
   
    public void onSuccess(DatabaseResponse response) {
	try{	
		                JSONArray accounts = message.getJSONObject("accounts").names();
		                int length = accounts.length();
		                
		                int count = 0;
     for(int i = 0; i < length; i++){
		        			if(accounts.getString(i).contains(LipukaApplication.CLIENT_ID+"_")){
		        				count++;			
		        			}
		        						
		        		}
     if(count != 0){
		lipukaApplication.setAccountsCount((byte)count);
		for(int i = 0; i < length; i++){
			if(accounts.getString(i).contains(LipukaApplication.CLIENT_ID+"_")){
				lipukaApplication.fetchServiceXml(message.getString("servicexml")+accounts.getString(i)+".xml", 
						accounts.getString(i));				
			}
						
		}
     }else{
			lipukaApplication.createActivationNotification("You are not yet registered for mobile banking. Please go to your bank to register");
     }
	}catch (org.json.JSONException jsonError) {
	    Log.d(Main.TAG, "JSON Error", jsonError);
	}
	     Log.d(Main.TAG, "Saved profile");	 	 
    	}
    public void onFailure(String error) {
    
    }
}