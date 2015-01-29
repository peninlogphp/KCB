package lipuka.android.model.responsehandlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;
import android.app.Activity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class UpdateServiceXmlResponseHandler  extends AsyncHttpResponseHandler{
	  
	LipukaApplication lipukaApplication;
	
   public UpdateServiceXmlResponseHandler(LipukaApplication lipukaApplication,
		   Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;   
   }
   
   public void onStart() {
	   //lipukaApplication.showProgress("Sending Activation Request");
    }

    public void onFinish() {
    //	lipukaApplication.dismissProgressDialog();	
    }

    public void onSuccess(String content) {
    	lipukaApplication.setCurrentDialogTitle("Activation");
    	lipukaApplication.setCurrentDialogMsg("Activation request sent\nResult will be sent shortly");
    	//lipukaApplication.showDialog(activity, Main.DIALOG_MSG_ID);
    	}

    public void onFailure(Throwable error) {
       	lipukaApplication.setCurrentDialogTitle("Network Error");
       	lipukaApplication.setCurrentDialogMsg(lipukaApplication.getString(R.string.network_error));

       		     Log.d(Main.TAG, "Network error: "+error.getMessage());

    	//lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    }
}