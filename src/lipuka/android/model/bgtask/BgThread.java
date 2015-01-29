package lipuka.android.model.bgtask;


import kcb.android.Main;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public final class BgThread extends Thread {
	
 
    private Handler handler;
	
	private int totalQueued;
	
	
	public BgThread() {
	}
	
	@Override
	public void run() {
		try {
		Looper.prepare();

			handler = new Handler();
			
			Looper.loop();
			
		} catch (Throwable t) {
			Log.e(Main.TAG, "Bg Thread halted due to an error", t);
		} 
	}
	
	public synchronized void requestStop() {
		
		handler.post(new Runnable() {
			@Override
			public void run() {
		Log.i(Main.TAG, "Bg Thread loop quitting by request");
				
				Looper.myLooper().quit();
			}
		});
	}
	

	public synchronized void enqueueTask(final BgTask task) {
		  try {
	        while(handler == null){	
	        	Log.d(Main.TAG, "handler is null");
	          Thread.sleep(100);  // 
	        }
			handler.post(task);

	        } catch (InterruptedException e) {  // 
	        }
			}
	public synchronized void removeTask(final BgTask task) {
		  try {
			handler.removeCallbacks(task);
	        } catch (Exception e) {  // 
	        }
			}
	public synchronized int getTotalQueued() {
		return totalQueued;
	}
	public synchronized int incrementTotalQueued() {
		return ++totalQueued;
	}
	public synchronized int decrementTotalQueued() {
		return --totalQueued;
	}

   
}
