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



public class SignInHandler extends AsyncHttpResponseHandler{

	LipukaApplication lipukaApplication;
	Activity activity;
	private byte action = 0;
    int[] ids;
   public SignInHandler(LipukaApplication lipukaApplication,
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
        	lipukaApplication.setProfileID(response.getJSONObject("profile_data").getInt("profile_id"));
        	lipukaApplication.setProfileData(response.getJSONObject("profile_data"));
        	
   if (activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
        		//lipukaApplication.showGenericNotification(response.getString("STATUS_MESSAGE"), StanChartHome.class, R.id.notification_response);
return;
            }
            switch(action){
			case SignInDialog.FUNDS_TRANSFER:
				Intent i = new Intent(activity, FundsTransfer.class);
				activity.startActivity(i);
				break;
			case SignInDialog.MY_ACCOUNT:
				i = new Intent(activity, MyAccount.class);
				activity.startActivity(i);
				break;
			case SignInDialog.SEND_MONEY:
				i = new Intent(activity, SendMoney.class);
				activity.startActivity(i);
				break;
			case SignInDialog.RECEIVE_MONEY:
				i = new Intent(activity, ReceiveMoney.class);
				activity.startActivity(i);
				break;
			case SignInDialog.MANAGE_TRANSACTIONS:
				i = new Intent(activity, ManageTransactions.class);
				activity.startActivity(i);
				break;
			case SignInDialog.TRANSACTION_HISTORY:
				i = new Intent(activity, TransactionHistory.class);
				activity.startActivity(i);
				break;
			case SignInDialog.MANAGE_BENEFICIARIES:
				i = new Intent(activity, ManageBeneficiaries.class);
				activity.startActivity(i);
				break;
			case SignInDialog.WU_EDIT_PROFILE:
				i = new Intent(activity, WuEditProfile.class);
				activity.startActivity(i);
				break;
			case SignInDialog.ACCOUNT_STATEMENT:
				i = new Intent(activity, AccountStmt.class);
				activity.startActivity(i);
				break;
			case SignInDialog.BALANCE:
				lipukaApplication.setServiceID(String.valueOf(Constants.FETCH_BALANCE));
		    	  lipukaApplication.setCurrentDialogTitle("PIN");
			      lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
			      activity.showDialog(Main.DIALOG_PIN_ID);		
				break;
				case SignInDialog.NOTHING:
					ShowSignOutActivity  showSignOutActivity =((ShowSignOutActivity)activity);
				   showSignOutActivity.showSignOutBtn();
				    				break;
				case SignInDialog.MG_SEND_MONEY:
					i = new Intent(activity, MgSendMoney.class);
					activity.startActivity(i);
					break;
				case SignInDialog.MG_RECEIVE_MONEY:
					i = new Intent(activity, MgReceiveMoney.class);
					activity.startActivity(i);
					break;
				case SignInDialog.MG_TRANSACTION_HISTORY:
					i = new Intent(activity, MgTransactionHistory.class);
					activity.startActivity(i);
					break;
				case SignInDialog.MG_MANAGE_BENEFICIARIES:
					i = new Intent(activity, MgManageBeneficiaries.class);
					activity.startActivity(i);
					break;
				case SignInDialog.MG_EDIT_PROFILE:
					i = new Intent(activity, MgEditProfile.class);
					activity.startActivity(i);
					break;
				case SignInDialog.AIRTIME_TOP_UP:
					i = new Intent(activity, AirtimeTopup.class);
					activity.startActivity(i);
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