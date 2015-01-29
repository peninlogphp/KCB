package lipuka.android.model.responsehandlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;


import org.json.JSONObject;



import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class DownloadProfileResponseHandler  extends AsyncHttpResponseHandler{
	  
	LipukaApplication lipukaApplication;
	JSONObject message;
   public DownloadProfileResponseHandler(LipukaApplication lipukaApplication){
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
	lipukaApplication.saveProfile(content, message);

    	}
    
    public void onFailure(Throwable error) {
	     Log.d(Main.TAG, "Downloading profile failed");
			lipukaApplication.createActivationNotification(lipukaApplication.getString(R.string.activation_network_error_msg));
    }
}