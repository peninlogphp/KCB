package lipuka.android.model.responsehandlers;

import kcb.android.AccountStmt;
import kcb.android.EcobankMain;
import kcb.android.FundsTransfer;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.ManageBeneficiaries;
import kcb.android.ManageTransactions;
import kcb.android.MyAccount;
import kcb.android.ReceiveMoney;
import kcb.android.SendMoney;
import kcb.android.ShowSignOutActivity;
import kcb.android.StanChartHome;
import kcb.android.TransactionHistory;
import kcb.android.WuEditProfile;
import lipuka.android.data.Constants;
import lipuka.android.view.SignInDialog;
import kcb.android.R;


import org.json.JSONObject;



import android.app.Activity;
import android.content.Intent;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class EditProfileHandler extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;
	String email;
    int[] ids;
   public EditProfileHandler(LipukaApplication lipukaApplication,
		   Activity activity, String email){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;   
	this.email = email;   
   }
   
   public void onStart() {
	   lipukaApplication.showProgress("Sending request");
    }

    public void onFinish() {
    	
	     Log.d(Main.TAG, "called onfinish");

    }

    public void onSuccess(String content) {
	    lipukaApplication.clearNavigationStack();
     if (activity == lipukaApplication.getCurrentActivity()){
 		 	lipukaApplication.dismissProgressDialog();
         }
	 	
    	  try {
    	   	     Log.d(Main.TAG, "json string: "+content);

    		JSONObject response = new JSONObject(content);
    

        if (response.getInt("STATUS_CODE") == 1){
        	
			lipukaApplication.putProfileData("email", email);
	
        	 if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
        		lipukaApplication.showGenericNotification(response.getString("STATUS_MESSAGE"), StanChartHome.class, R.id.notification_response);
return;
            }
        	 lipukaApplication.setCurrentDialogTitle("Response");
   	  	  lipukaApplication.setCurrentDialogMsg(response.getString("STATUS_MESSAGE"));
             	lipukaApplication.showDialog( Main.DIALOG_SERVICE_RESPONSE_ID);
             	lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
          
}else{
	String errorMsg = response.getString("STATUS_MESSAGE");
	if(errorMsg == null || errorMsg.equals("null")){
		errorMsg = "Sorry, there was an error in processing your request, please try again later";	
	}
	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
		lipukaApplication.showGenericNotification(errorMsg, StanChartHome.class, R.id.notification_response);
    	return;
    }
	lipukaApplication.setCurrentDialogTitle("Error");
	lipukaApplication.setCurrentDialogMsg(errorMsg);
	lipukaApplication.showDialog( Main.DIALOG_ERROR_ID);
        }
    	}
        catch (org.json.JSONException jsonError) {
   	     Log.d(Main.TAG, "jsonError: ", jsonError);
   	  if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
  		lipukaApplication.showGenericNotification("Sorry, there was an error in getting a response, please try again later", StanChartHome.class, R.id.notification_response);
	return;
      }
	   	lipukaApplication.setCurrentDialogTitle("Error");
	  	  lipukaApplication.setCurrentDialogMsg("Sorry, there was an error in getting a response, please try again later");
          	lipukaApplication.showDialog( Main.DIALOG_ERROR_ID);
          }
         	
        
    	}
    public void onFailure(Throwable error) {
	    lipukaApplication.clearNavigationStack();

    	if (activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
    	if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
      		lipukaApplication.showGenericNotification(activity.getString(R.string.network_error), StanChartHome.class, R.id.notification_response);
        	return;
        }
    	
  	   	lipukaApplication.setCurrentDialogTitle("Network Error");
  	  lipukaApplication.setCurrentDialogMsg(activity.getString(R.string.network_error));

  	  	     Log.d(Main.TAG, "Network error: "+error.getMessage());


    	lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    }
}