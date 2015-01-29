package lipuka.android.model.responsehandlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class DownloadServiceXmlResponseHandler  extends AsyncHttpResponseHandler{
	  
	LipukaApplication lipukaApplication;
	String account;
   public DownloadServiceXmlResponseHandler(LipukaApplication lipukaApplication){
	   super();
	this.lipukaApplication = lipukaApplication;   
   }
   
   public String getAccount() {
	   return account;
	   }

	       public void setAccount(String account) {
	       	this.account = account;   
	       }
	       
	       public void onStart() {
	   //lipukaApplication.showProgress("Sending Activation Request");
    }

    public void onFinish() {
    	//lipukaApplication.dismissProgressDialog();	
    }

    public void onSuccess(String content) {
	     Log.d(Main.TAG, "Downloaded service xml for "+account);
lipukaApplication.saveServiceXml(content, account);
    	}

    public void onFailure(Throwable error) {
	     Log.d(Main.TAG, "Downloading service xml failed");
			lipukaApplication.createActivationNotification(lipukaApplication.getString(R.string.activation_network_error_msg));
    }
}