package lipuka.android.model.responsehandlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;


import org.json.JSONArray;
import org.json.JSONObject;



import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class DownloadProfileAfterSAResponseHandler  extends AsyncHttpResponseHandler{
	  
	LipukaApplication lipukaApplication;
	JSONObject message;
   public DownloadProfileAfterSAResponseHandler(LipukaApplication lipukaApplication){
	   super();
	this.lipukaApplication = lipukaApplication;   
   }
    
   public JSONObject getMessage() {
return message;
}

    public void setMessage(JSONObject message) {
    	this.message = message;   
    }
    public void onStart() {
	   //lipukaApplication.showProgress("Sending Activation Request");
    }

    public void onFinish() {
    	//lipukaApplication.dismissProgressDialog();	
    }

    public void onSuccess(String content) {
	     Log.d(Main.TAG, "Downloaded profile");
	     try {
         	JSONObject response = new JSONObject(content);
         	
         	JSONObject message = new JSONObject();
    		JSONObject accounts = new JSONObject();
    		message.put("servicexml", response.getString("service_xml_files_url"));
    		JSONArray accountsArray = response.getJSONArray("service_xml_files");
    		for(int i = 0; i < accountsArray.length(); i++){
        		String account = accountsArray.getString(i);
    			accounts.put(account, account);
    		}
    		message.put("accounts", accounts);
    		
    	lipukaApplication.saveProfile(response.getString("profile"), message);

     	}catch (org.json.JSONException jsonError) {
             Log.d(Main.TAG, "JSON Error", jsonError);
          	}

    	}
    
    public void onFailure(Throwable error) {
	     Log.d(Main.TAG, "Downloading profile failed");
			lipukaApplication.createActivationNotification(lipukaApplication.getString(R.string.activation_network_error_msg));
    }
}