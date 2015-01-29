package lipuka.android.model.bgtask;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kcb.android.EcobankHome;
import kcb.android.LipukaApplication;
import kcb.android.Main;







import kcb.android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class BgTask extends BroadcastReceiver implements Runnable{ // 
    
	public static final String DOWNLOAD_DEST = "LipukaDownloads";
	   public static final String DOWNLOAD_URL = "url";
	   public static final String TITLE = "title";
	   public static final String CONTENT_ID = "content_id";

	public static final byte MSG_START = 1;
	public static final byte MSG_FINISH = 2;
	
	public static final String DOWNLOAD_CANCELLED = "lipuka.android.DOWNLOAD_CANCELLED";
	public static final String DO_NOTHING = "lipuka.android.DO_NOTHING";
	String title, downloadUrl, message;
int contentId, startId;
    private LipukaApplication lipukaApplication;
    private boolean succeeded=false, cancelled = false;
    private NotificationManager notificationManager;
 private Notification notification;
	private Handler serviceHandler;
	File file;
	HttpURLConnection c;
	FileOutputStream fos = null;

    InputStream in = null;
    BgThread downloadThread;
  // Constructor
  public BgTask(LipukaApplication lipukaApplication, NotificationManager notificationManager, Handler serviceHandler,
		  int contentId, String title, int startId, BgThread downloadThread) { // 
    this.lipukaApplication = lipukaApplication;
    this.notificationManager = notificationManager;
    this.serviceHandler = serviceHandler;
this.contentId = contentId;
    this.title = title;
    this.startId = startId;
    this.downloadThread = downloadThread;
  }

	
	@Override
	public void run() {

 Log.d(Main.TAG,"ran bg task for: "+title);
        Message msg = serviceHandler.obtainMessage();
        msg.what = MSG_FINISH;
        msg.arg1 = startId;
        msg.arg2 = contentId;
        msg.obj =  lipukaApplication.getContacts();


        serviceHandler.sendMessage(msg);

	
	}
	
	 public void downloadFile() {
		 boolean sdcardAvailable = false;
		   String state = Environment.getExternalStorageState();
		    if (Environment.MEDIA_REMOVED.equals(state)) {
		    	message = lipukaApplication.getString(R.string.notification_no_sdcard);
		    	}
		    else if (Environment.MEDIA_UNMOUNTED.equals(state)) {
		    	message =  lipukaApplication.getString(R.string.notification_sdcard_unmounted);
	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		message =  lipukaApplication.getString(R.string.notification_sdcard_read_only);
		    }
	else if (Environment.MEDIA_NOFS.equals(state)) {
		message =  lipukaApplication.getString(R.string.notification_sdcard_nofs);
		    }
	else if (Environment.MEDIA_SHARED.equals(state)) {
		message =  lipukaApplication.getString(R.string.notification_sdcard_shared);
		    }
	else if (Environment.MEDIA_UNMOUNTABLE.equals(state)) {
		message =  lipukaApplication.getString(R.string.notification_sdcard_unmountable);
		    }
	else if (Environment.MEDIA_MOUNTED.equals(state)) {
		sdcardAvailable = true;
		    }

		if (sdcardAvailable){
			StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
	   double avail_sd_space = (double)stat_fs.getAvailableBlocks() *(double)stat_fs.getBlockSize();
	   //double GB_Available = (avail_sd_space / 1073741824);
	   double MB_Available = (avail_sd_space / 1048576);
	   //System.out.println("Available MB : " + MB_Available);
	   Log.d("MB",""+MB_Available);

	  try {
	       File root =new File(Environment.getExternalStorageDirectory()+"/"+DOWNLOAD_DEST);
	       if(root.exists() && root.isDirectory()) {
	       }else{
	           root.mkdir();
	       }
	       URL u = new URL(downloadUrl);
	       c = (HttpURLConnection) u.openConnection();
	       c.setRequestMethod("GET");
	       c.setDoOutput(true);
	       c.connect();
	         int fileSize  = c.getContentLength();
	         if(MB_Available <= (c.getContentLength()/1048576 )){
	        	 //TODO update instead of creating
	            message = lipukaApplication.getString(R.string.notification_no_memory);
	             c.disconnect();
	             return;
	         } 

	          file = new File(root.getPath(), title +".mp3");
	       if(file.exists()){
	           file.delete();
	           Log.d(Main.TAG,"YES");
	       }
	        fos = new FileOutputStream(file);

	        in = new BufferedInputStream(c.getInputStream());

	       byte[] buffer = new byte[1024];
	       int read = 0, total = 0, total2SendProgress = 0;
	    
	       while (in != null && (read = in.read(buffer)) != -1 && !cancelled) {
	           fos.write(buffer, 0, read);
	           total += read;
	           total2SendProgress += read;

	           // publishing the progress....
	           //publishProgress((int)(total*100/fileSize));
	         if(((int)(total2SendProgress*100/fileSize)) >= 5){ 
	        	 total2SendProgress = 0;
	        	 notification.contentView.
	      	 setProgressBar(R.id.status_progress, 100, (int)(total*100/fileSize), false);
	      	 notificationManager.notify(contentId, notification);
	         }
	         }
	       fos.flush();
	       fos.close();
	       in.close();
	       c.disconnect();
	       succeeded=true;
	       } catch (Exception e) {
	       Log.d(Main.TAG,"download error: ", e);
	       
	     message = lipukaApplication.getString(R.string.notification_download_error);
	 	Object params[] = {title};

	 	message = String.format(message, params);
	   }finally{
		  try{ 
			  if(fos != null){
				  fos.close();
				  fos = null;
			  }
			  if(in != null){
				  in.close(); 
			    in = null;
}
			  if(c != null){
				    c.disconnect();
				    c = null;
				}
		  }catch (Exception e) {}
	   }
	 }
	 }
	 
	 public void showProgressNotification() {
			notificationManager.cancel(contentId);
		      
			Intent intent = new Intent(DOWNLOAD_CANCELLED+contentId);
/*Intent intent = new Intent(lipukaApplication, CancelDownloadService.class);
intent.putExtra(DownloadService.TITLE, title);
intent.putExtra(DownloadService.CONTENT_ID, contentId);*/
//The PendingIntent to launch our activity if the user selects this notification
	       PendingIntent contentIntent = PendingIntent.getBroadcast(lipukaApplication, contentId,
	    		   intent, PendingIntent.FLAG_UPDATE_CURRENT);
	       
	       // Set the icon, scrolling text and timestamp
	      notification = new Notification(R.drawable.download, "",
	               System.currentTimeMillis());
	       notification.flags |= Notification.FLAG_ONGOING_EVENT;

	       notification.contentView = new RemoteViews(lipukaApplication.getPackageName(), R.layout.download_progress);
	       notification.contentIntent = contentIntent;
	notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.download);
	String msg = lipukaApplication.getString(R.string.notification_download_started);
	Object params[] = {title};

	msg = String.format(msg, params);

	       notification.contentView.setTextViewText(R.id.status_text, msg);
	       notification.contentView.setProgressBar(R.id.status_progress, 100, 0, false);	       
	     
	       notificationManager.notify(contentId, notification);
	       msg = lipukaApplication.getString(R.string.notification_download_started_toast);

	   	msg = String.format(msg, params);
           Toast.makeText(lipukaApplication, msg, Toast.LENGTH_LONG).show();
	   }
 
	 public void sendMessageNotification(String message)  {
	 	 
		 	long when = System.currentTimeMillis();
		 	Notification notification = new Notification(R.drawable.download, "", when);
		 	
		 	CharSequence contentTitle = "Lipuka Download";
		 	Intent notificationIntent = new Intent(lipukaApplication, EcobankHome.class);
	    	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

	    	
		 	PendingIntent contentIntent = 
		 		PendingIntent.getActivity(lipukaApplication, contentId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		 	notification.setLatestEventInfo(lipukaApplication, contentTitle, 
		 			message, contentIntent);
		 	notification.flags |= Notification.FLAG_AUTO_CANCEL;
		 	notification.defaults |= Notification.DEFAULT_SOUND;
		 	notificationManager.notify(contentId, notification);
		     }

	 public void sendSuccessMessageNotification(String message)  {
	 	 
		 	long when = System.currentTimeMillis();
		 	Notification notification = new Notification(R.drawable.download, "", when);
		 	
		 	CharSequence contentTitle = message;
		 	Intent notificationIntent = new Intent(android.content.Intent.ACTION_VIEW); 
	        notificationIntent.setDataAndType(Uri.fromFile(file), "audio/mp3"); 
	        //notificationIntent.setDataAndType(Uri.fromFile(file), "audio/*"); 
	    	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

	    	
		 	PendingIntent contentIntent = 
		 		PendingIntent.getActivity(lipukaApplication, contentId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		 	notification.setLatestEventInfo(lipukaApplication, contentTitle, 
		 			lipukaApplication.getString(R.string.notification_download_location), contentIntent);
		 	notification.flags |= Notification.FLAG_AUTO_CANCEL;
		 	notification.defaults |= Notification.DEFAULT_SOUND;
		 	notificationManager.notify(contentId, notification);
		     }

	@Override
	public void onReceive(Context context, Intent intent) {
		 Log.d(Main.TAG,"cancelled download task for: "+title);
		 downloadThread.removeTask(this);
		cancelled = true;	
		notificationManager.cancel(contentId);
			lipukaApplication.unregisterReceiver(this);
if(fos != null){
	try{
	fos.close();
	fos = null;
	}catch (Exception e){
		
	}
}
if(in != null){
	try{
	    in.close();
	    in = null;
		}catch (Exception e){
			
		}
}
if(c != null){
    c.disconnect();
    c = null;
}
		Log.d(Main.TAG, "clicked cancel download");
	}
}