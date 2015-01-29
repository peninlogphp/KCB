package lipuka.android.model.responsehandlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;
import android.app.Activity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class UpdateResponseHandler  extends AsyncHttpResponseHandler{
	  
	LipukaApplication lipukaApplication;
	Activity activity;

   public UpdateResponseHandler(LipukaApplication lipukaApplication){
	   super();
	this.lipukaApplication = lipukaApplication;   
}
   
   public void onStart() {
	   lipukaApplication.showProgress("Sending Activation Request");
    }

    public void onFinish() {
    	
    	}

    public void onSuccess(String content) {
    	if (activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
    	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
        	return;
        }
    	lipukaApplication.setCurrentDialogTitle("Activation");
    	//lipukaApplication.setCurrentDialogMsg("Activation request sent\nResult will be sent shortly");
    	lipukaApplication.setCurrentDialogMsg(content);
lipukaApplication.showDialog(Main.DIALOG_MSG_ID);
    	}
    public void onFailure(Throwable error) {
    	if (activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
    	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
        	return;
        }
    	lipukaApplication.setCurrentDialogTitle("Network Error");
       	lipukaApplication.setCurrentDialogMsg(activity.getString(R.string.network_error));

       		     Log.d(Main.TAG, "Network error: "+error.getMessage());

    	lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    }
}