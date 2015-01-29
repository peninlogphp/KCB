package lipuka.android.model.responsehandlers;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;
import lipuka.android.model.Navigation;
import lipuka.android.model.synchronization.BackgroundTaskDone;
import android.app.Activity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;



public class RemoteRequestResponseHandler  extends AsyncHttpResponseHandler{
	  
	LipukaApplication lipukaApplication;
	Activity activity;

   public RemoteRequestResponseHandler(LipukaApplication lipukaApplication,
		   Activity activity){
	   super();
	this.lipukaApplication = lipukaApplication;   
	this.activity = activity;   
 }
   
   public void onStart() {
	   lipukaApplication.showProgress("Sending Request...");

    }

    public void onFinish() {
    	//lipukaApplication.dismissProgressDialog();	
    }

    public void onSuccess(String content) {
	     BackgroundTaskDone.setDone(true);
		     Log.d(Main.TAG, "Activity Class: "+activity.getClass());

	if (activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
	
    	if(activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
    		if (content != null && content.length() != 0){
    			lipukaApplication.parseRemoteActivity(content.trim(), false);
    			lipukaApplication.clearNavigationStack();
    		}else{
    			popNavigationStack();
        		lipukaApplication.showResponseNotification(lipukaApplication.getString(R.string.network_error));  	    	
    		}
	return;        	
        }
    		if (content != null && content.length() != 0){
    			lipukaApplication.parseRemoteActivity(content.trim(), true);
    			
    			lipukaApplication.clearNavigationStack();
    		
        	Log.d(Main.TAG, content.trim()); 

    		}else{
    			popNavigationStack();
    			
    			lipukaApplication.setCurrentDialogTitle("Network Error");
    		   	lipukaApplication.setCurrentDialogMsg(activity.getString(R.string.network_error));
    	    	lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    	    	
    		}
    	}
    public void onFailure(Throwable error) {
	     BackgroundTaskDone.setDone(true);
	     Log.d(Main.TAG, "Activity Class: "+activity.getClass());

if (activity == lipukaApplication.getCurrentActivity()){
		 	lipukaApplication.dismissProgressDialog();
        }
    	popNavigationStack();

    	if(activity == null || !lipukaApplication.isActivityVisible(activity.getClass())){
    		lipukaApplication.showResponseNotification(lipukaApplication.getString(R.string.network_error));
	return;        	
        }
	   	lipukaApplication.setCurrentDialogTitle("Network Error");
	   	lipukaApplication.setCurrentDialogMsg(activity.getString(R.string.network_error));

	   		     Log.d(Main.TAG, "Network error: "+error.getMessage());

    	lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);

    }
    
    public void popNavigationStack() {
    	  
    	Navigation nav = lipukaApplication.peekNavigationStack();
    	String act = nav.getActivity();
    	if (act != null){
    	if(!act.equals("main")){
       	 lipukaApplication.popNavigationStack();
		     Log.d(Main.TAG, "popped stack: ");

    	}
    	}else{
          	 lipukaApplication.popNavigationStack();   
		     Log.d(Main.TAG, "popped stack: ");

    	}    	 
    }
}