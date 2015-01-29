package lipuka.android.model.responsehandlers;

import kcb.android.EcobankHome;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.OneTimePINActivity;
import kcb.android.SelfActivation;
import kcb.android.R;
import android.app.Activity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;


public class SelfActivateResponseHandler  extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;

   public SelfActivateResponseHandler(LipukaApplication lipukaApplication,
		   Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;   
}
   
   public void onStart() {
	   lipukaApplication.showProgress("Sending Request");
    }

    public void onFinish() {
    	
    	}

    public void onSuccess(String content) {
		     Log.d(Main.TAG, "self activate response: "+content);
		     
		     if (activity == lipukaApplication.getCurrentActivity()){
				 	lipukaApplication.dismissProgressDialog();
		        }
		     
    	if (activity != lipukaApplication.getCurrentActivity() || !lipukaApplication.isActivityVisible(activity.getClass())){
    		lipukaApplication.showGenericNotification(content, EcobankHome.class, R.id.self_activate_response);
return;
          }
              	lipukaApplication.setCurrentDialogTitle("Self Activate Response");
              	lipukaApplication.setCurrentDialogMsg(content);
              	lipukaApplication.showDialog( Main.DIALOG_MSG_ID);

    	}
    public void onFailure(Throwable error) {
    	if (activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
    	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
    		lipukaApplication.showGenericNotification(activity.getString(R.string.network_error), SelfActivation.class, R.id.self_activate_response);
    		return;
        }
    	lipukaApplication.setCurrentDialogTitle("Network Error");
       	lipukaApplication.setCurrentDialogMsg(activity.getString(R.string.network_error));

       		     Log.d(Main.TAG, "Network error: "+error.getMessage());

    	lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    }
}