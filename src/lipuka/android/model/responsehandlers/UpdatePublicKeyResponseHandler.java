package lipuka.android.model.responsehandlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;


import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Activity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class UpdatePublicKeyResponseHandler  extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;

   public UpdatePublicKeyResponseHandler(LipukaApplication lipukaApplication,
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
  		     Log.d(Main.TAG, "update public key response: "+content);

    		JSONObject response = new JSONObject(content);
    		if (response.getInt("STATUS_CODE") == 1){
    			lipukaApplication.savePublicKey(response.getString("public_key"), response.getInt("public_key_version"));              
  }else{

          }
      	}
          catch (org.json.JSONException jsonError) {
   		     Log.d(Main.TAG, "JSONException: ", jsonError);
           	}

    	}
    public void onFailure(Throwable error) {
       		     Log.d(Main.TAG, "Network error: "+error.getMessage());
    }
}