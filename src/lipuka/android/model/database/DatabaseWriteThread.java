package lipuka.android.model.database;

import kcb.android.Main;
import lipuka.android.model.DbHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public final class DatabaseWriteThread extends Thread {
	

	private static final int INSERT_PROFILE_XML_MESSAGE = 0;
    private static final int INSERT_SERVICE_XML_MESSAGE = 1;
    private static final int START_MESSAGE = 2;
    private static final int FINISH_MESSAGE = 3;
 
    private Handler handler;
	
	private int totalQueued;
	
	private int totalCompleted;
	private DbHelper dbHelper;
	
	public DatabaseWriteThread() {
	}
	
	@Override
	public void run() {
		try {
		Looper.prepare();

			handler = new Handler();
			
			Looper.loop();
			
		} catch (Throwable t) {
			Log.e(Main.TAG, "Database Write Thread halted due to an error", t);
		} 
	}
	
	public synchronized void requestStop() {
		
		handler.post(new Runnable() {
			@Override
			public void run() {
		Log.i(Main.TAG, "Database Write Thread loop quitting by request");
				
				Looper.myLooper().quit();
			}
		});
	}
	

	public synchronized void enqueueTask(final DatabaseWriteTask task) {
		try {
	        while(handler == null){	
	        	Log.d(Main.TAG, "handler is null");
	          Thread.sleep(100);  // 
	        }
			handler.post(task);

	        } catch (InterruptedException e) {  // 
	        }
			}
   
}
