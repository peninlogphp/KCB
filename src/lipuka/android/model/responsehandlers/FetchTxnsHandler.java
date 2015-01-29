package lipuka.android.model.responsehandlers;

import kcb.android.CreateMobileAccount;
import kcb.android.EcobankMain;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.ManageTransactionsList;
import kcb.android.MgTransactionHistory;
import kcb.android.MgTransactionHistoryList;
import kcb.android.StanChartHome;
import kcb.android.TransactionHistory;
import kcb.android.TransactionHistoryList;
import lipuka.android.data.Constants;
import kcb.android.R;


import org.json.JSONObject;



import android.app.Activity;
import android.content.Intent;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class FetchTxnsHandler extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;
	String[] names;
    int[] ids;
    byte type;
   public FetchTxnsHandler(LipukaApplication lipukaApplication,
		   Activity activity, byte type){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;  
	this.type = type;   

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
            Intent i = null;
   	  	if(activity instanceof TransactionHistory){
   	  		i = new Intent(activity, TransactionHistoryList.class);
   	  		if(type == TransactionHistory.SEND_MONEY){
        i.putExtra(Constants.TXNS, response.getString("send_money_txns"));
   	  		}else{
   	         i.putExtra(Constants.TXNS, response.getString("receive_money_txns"));
   	  		}
        i.putExtra(TransactionHistory.TRANSACTION_TYPE, type);
   	  	}else if(activity instanceof MgTransactionHistory){
   	  		i = new Intent(activity, MgTransactionHistoryList.class);
   	  		if(type == MgTransactionHistory.SEND_MONEY){
        i.putExtra(Constants.TXNS, response.getString("send_money_txns"));
   	  		}else{
   	         i.putExtra(Constants.TXNS, response.getString("receive_money_txns"));
   	  		}
        i.putExtra(TransactionHistory.TRANSACTION_TYPE, type);
   	  	}else{
   	 	i = new Intent(activity, ManageTransactionsList.class);
        i.putExtra(Constants.TXNS, response.getString("send_money_txns"));  	  		
   	  	}
        activity.startActivity(i);
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