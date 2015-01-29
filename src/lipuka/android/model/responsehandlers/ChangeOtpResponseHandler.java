package lipuka.android.model.responsehandlers;

import kcb.android.EcobankHome;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.OneTimePINActivity;
import kcb.android.R;
import android.app.Activity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class ChangeOtpResponseHandler  extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;

   public ChangeOtpResponseHandler(LipukaApplication lipukaApplication,
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
		     Log.d(Main.TAG, "change OTP response: "+content);
		     
		     if (activity == lipukaApplication.getCurrentActivity()){
				 	lipukaApplication.dismissProgressDialog();
		        }
		     
    	if (activity != lipukaApplication.getCurrentActivity() || !lipukaApplication.isActivityVisible(activity.getClass())){
    		lipukaApplication.showChangeOTPNotification(content, EcobankHome.class);
return;
          }
              	lipukaApplication.setCurrentDialogTitle("Change PIN Response");
              	lipukaApplication.setCurrentDialogMsg(content);
              	lipukaApplication.showDialog( Main.DIALOG_MSG_ID);

    	}
    public void onFailure(Throwable error) {
    	if (activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
    	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
    		lipukaApplication.showChangeOTPNotification(activity.getString(R.string.network_error), OneTimePINActivity.class);
    		return;
        }
    	lipukaApplication.setCurrentDialogTitle("Network Error");
       	lipukaApplication.setCurrentDialogMsg(activity.getString(R.string.network_error));

       		     Log.d(Main.TAG, "Network error: "+error.getMessage());

    	lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    }
}