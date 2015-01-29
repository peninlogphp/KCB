package lipuka.android.model.database.handlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import lipuka.android.model.database.AsyncDatabaseHandler;
import lipuka.android.model.database.DatabaseResponse;
import android.app.Activity;
import android.util.Log;

public class SaveProfileUpdateHandler extends AsyncDatabaseHandler {
		
	LipukaApplication lipukaApplication;
	Activity activity;

   public SaveProfileUpdateHandler(LipukaApplication lipukaApplication, Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;
	this.activity = activity;   

   }
   
   public void onStart() {
    }

    public void onFinish() {
    
    }
   
    public void onSuccess(DatabaseResponse response) {
    	if (lipukaApplication.getCurrentActivity() != null && activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
    	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
  		  lipukaApplication.showProfileUpdateNotification("Profile update succeeded");
	return;
        }
    	lipukaApplication.setCurrentDialogTitle("Profile Update");
    	//lipukaApplication.setCurrentDialogMsg("Activation request sent\nResult will be sent shortly");
    	lipukaApplication.setCurrentDialogMsg("Profile update succeeded");
lipukaApplication.showDialog(Main.DIALOG_PROFILE_UPDATE_MSG_ID);
   Log.d(Main.TAG, "Profile update succeeded");	 	 
    	}
    public void onFailure(String error) {
    	if (lipukaApplication.getCurrentActivity() != null && activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
    	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
        	return;
        }
    	lipukaApplication.setCurrentDialogTitle("Profile Update");
       	lipukaApplication.setCurrentDialogMsg("Profile update failed");

       		     Log.d(Main.TAG, "Profile Update error: "+error);

    	lipukaApplication.showDialog(Main.DIALOG_PROFILE_UPDATE_ERROR_ID);
    }
}