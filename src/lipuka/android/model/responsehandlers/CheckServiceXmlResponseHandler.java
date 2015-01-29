package lipuka.android.model.responsehandlers;



import kcb.android.LipukaApplication;
import kcb.android.Main;

import org.json.JSONObject;



import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class CheckServiceXmlResponseHandler  extends AsyncHttpResponseHandler{
	  
	LipukaApplication lipukaApplication;
	String account;
   public CheckServiceXmlResponseHandler(LipukaApplication lipukaApplication, String account){
	   super();
	this.lipukaApplication = lipukaApplication; 
	this.account = account;   

   }
   
   public void onStart() {
	  // lipukaApplication.showProgress("Sending Activation Request");
    }

    public void onFinish() {
    	//lipukaApplication.dismissProgressDialog();	
    }

    public void onSuccess(String content) {

        	try {
            	JSONObject message = new JSONObject(content);
        	if (message.getString("updated").equals("false")){
    			lipukaApplication.saveServiceXmlUpdate(message.getString("servicexml"), account);
                Log.d(Main.TAG, "Updating account: "+account);
    		 }
        	}catch (org.json.JSONException jsonError) {
                Log.d(Main.TAG, "JSON Error", jsonError);
             	}
    	}

    public void onFailure(Throwable error) {
       	
    }
}