package lipuka.android.model.synchronization;

public class BackgroundTaskDone {

	static boolean done = true;
	
	 
	public static synchronized boolean isDone() { 
		return done;
		}
		public static synchronized void setDone(boolean doneParam) { 
			done = doneParam;
			}
}