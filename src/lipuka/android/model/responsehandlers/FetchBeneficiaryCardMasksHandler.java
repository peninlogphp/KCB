package lipuka.android.model.responsehandlers;

import kcb.android.AccountStatementMiniNfull;
import kcb.android.AccountStmt;
import kcb.android.AgencyBanking;
import kcb.android.AirtimeTopup;
import kcb.android.CreateMobileAccount;
import kcb.android.CreditCardPayments;
import kcb.android.EcobankMain;
import kcb.android.FundsTransfer;
import kcb.android.Insurance;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.ManageBeneficiaries;
import kcb.android.ManageTransactions;
import kcb.android.MgEditProfile;
import kcb.android.MgManageBeneficiaries;
import kcb.android.MgReceiveMoney;
import kcb.android.MgSendMoney;
import kcb.android.MgTransactionHistory;
import kcb.android.Mkodi;
import kcb.android.MobileMoneyTransfer;
import kcb.android.MoneyGram;
import kcb.android.MyAccount;
import kcb.android.PartialSignInActivity;
import kcb.android.PayBills;
import kcb.android.PrepaidCards;
import kcb.android.ReceiveMoney;
import kcb.android.SalaryAdvance;
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



public class FetchBeneficiaryCardMasksHandler extends AsyncHttpResponseHandler{

	public static final byte NEW_CARD = 1;
	public static final byte SAVED_CARD = 2;

LipukaApplication lipukaApplication;
	Activity activity;
	private byte action = 0;
    int[] ids;
   public FetchBeneficiaryCardMasksHandler(LipukaApplication lipukaApplication,
		   Activity activity, byte action){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;   
	this.action = action;   
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
        		//lipukaApplication.showGenericNotification(response.getString("STATUS_MESSAGE"), StanChartHome.class, R.id.notification_response);
return;
            }
   FundsTransfer  fundsTransfer =((FundsTransfer)activity);

            switch(action){
			case NEW_CARD:
				fundsTransfer.populateNewCardDestinationCardsList(response.getJSONArray("card_holders"));
				break;			
				case SAVED_CARD:
					fundsTransfer.populateSavedCardDestinationCardsList(response.getJSONArray("card_holders"));
				    				break;				
			default:
			}
          
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