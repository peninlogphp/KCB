package lipuka.android.model.database;

import kcb.android.Main;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class AsyncDatabaseHandler {

	private static final int SUCCESS_MESSAGE = 0;
    private static final int FAILURE_MESSAGE = 1;
    private static final int START_MESSAGE = 2;
    private static final int FINISH_MESSAGE = 3;
 
	private Handler handler;
	
	
	   public AsyncDatabaseHandler() {
	        // Set up a handler to post events back to the correct thread if possible
	        if(Looper.myLooper() != null) {
	            handler = new Handler(){
	                public void handleMessage(Message msg){
	                	AsyncDatabaseHandler.this.handleMessage(msg);
	                }
	            };
	        }
	    }

	   /**
	     * Fired when the request is started, override to handle in your own code
	     */
	    public void onStart() {}

	    /**
	     * Fired in all cases when the request is finished, after both success and failure, override to handle in your own code
	     */
	    public void onFinish() {}

	    /**
	     * Fired when a request returns successfully, override to handle in your own code
	     * @param content the body of the HTTP response from the server
	     */
	    public void onSuccess(DatabaseResponse response) {}

	    /**
	     * Fired when a request fails to complete, override to handle in your own code
	     * @param error the underlying cause of the failure
	     */
	    public void onFailure(String error) {}


	    //
	    // Pre-processing of messages (executes in background threadpool thread)
	    //

	    protected void sendSuccessMessage(DatabaseResponse response) {
	        sendMessage(obtainMessage(SUCCESS_MESSAGE, response));
	    }

	    protected void sendFailureMessage(String message) {
	        sendMessage(obtainMessage(FAILURE_MESSAGE, message));
	    }

	    protected void sendStartMessage() {
	        sendMessage(obtainMessage(START_MESSAGE, null));
	    }

	    protected void sendFinishMessage() {
	        sendMessage(obtainMessage(FINISH_MESSAGE, null));
	    }


	    //
	    // Pre-processing of messages (in original calling thread, typically the UI thread)
	    //

	    protected void handleSuccessMessage(DatabaseResponse response) {
	        onSuccess(response);
	    }

	    protected void handleFailureMessage(String message) {
	        onFailure(message);
	    }



	    // Methods which emulate android's Handler and Message methods
	    protected void handleMessage(Message msg) {
	        switch(msg.what) {
	            case SUCCESS_MESSAGE:
	                handleSuccessMessage((DatabaseResponse)msg.obj);
	                break;
	            case FAILURE_MESSAGE:
	                handleFailureMessage((String)msg.obj);
	                break;
	            case START_MESSAGE:
	                onStart();
	                break;
	            case FINISH_MESSAGE:
	                onFinish();
	                break;
	        }
	    }

	    protected void sendMessage(Message msg) {
	        if(handler != null){
	            handler.sendMessage(msg);
	        } else {
	            handleMessage(msg);
	            Log.d(Main.TAG, "No looper");

	        }
	    }

	    protected Message obtainMessage(int responseMessage, Object response) {
	        Message msg = null;
	        if(handler != null){
	            msg = this.handler.obtainMessage(responseMessage, response);
	        }else{
	            msg = new Message();
	            msg.what = responseMessage;
	            msg.obj = response;
	        }
	        return msg;
	    }


	    void sendResponseMessage(DatabaseResponse response) {
	        if(response.getStatus() == DatabaseResponse.OK) {
                sendSuccessMessage(response);
	        } else {
	            sendFailureMessage(response.getMessage());
	        }
	    }
}
