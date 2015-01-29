package lipuka.android.model.responsehandlers;

import kcb.android.EcobankMain;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.ManageBeneficiaries;
import kcb.android.ManageSavedCards;
import kcb.android.StanChartHome;
import kcb.android.R;


import org.json.JSONObject;



import android.app.Activity;
import android.util.Log;



import com.loopj.android.http.AsyncHttpResponseHandler;



public class DeleteCardHandler extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;
	String[] names;
    int[] ids;
   public DeleteCardHandler(LipukaApplication lipukaApplication,
		   Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;   
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

            if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
        		lipukaApplication.showGenericNotification(response.getString("STATUS_MESSAGE"), StanChartHome.class, R.id.notification_response);
return;
            }
        	 ((ManageSavedCards)activity).deleteCard();
				lipukaApplication.putProfileData("card_to_card_beneficiaries", response.getJSONArray("card_to_card_beneficiaries"));

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