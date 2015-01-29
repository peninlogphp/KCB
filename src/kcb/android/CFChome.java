package kcb.android;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import kcb.android.R;

import greendroid.app.GDActivity;
import lipuka.android.data.HomeItem;
import lipuka.android.model.bgtask.BgTask;
import lipuka.android.model.bgtask.BgThread;
import lipuka.android.model.Navigation;
import lipuka.android.model.database.handlers.SetActivatedHandler;
import lipuka.android.view.CircleLayout;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.InfoGrafixImage;
import lipuka.android.view.InfoGrafixObject;
import lipuka.android.view.LipukaListItem;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class CFChome extends Activity implements View.OnClickListener{

	public static final int PICK_PHOTO = 1, PICK_COLOR_SHADING = 2, PICK_COLOR_TEXT = 3;

	LipukaApplication lipukaApplication;
	private Stack<String> moodStack = new Stack<String>();
	JSONObject currentSong;
	CircleLayout view;
	int centerX = 0;
	int centerY = 0;
	int displayWidth = 0, displayHeight = 0; 
	TextView menuItemsHeader;
	LinearLayout imageSubmenu;
	LinearLayout colorSubmenu, transformSubmenu;
	boolean showing = false;
	private NotificationManager notificationManager;
private Handler handler;
	BgThread bgThread;
	private TreeSet<Integer> tasks;
	LinearLayout textPrompt;
	int selectedPosition;
	LipukaListItem[] fontArray;
	TextView colorField;
	int color = Color.BLACK;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	   try{
		   lipukaApplication = (LipukaApplication)getApplication();
		   lipukaApplication.initHome();
        setContentView(R.layout.infografix_home);
       /* LinearLayout paletteLayout = (LinearLayout) findViewById(R.id.palette_layout);
        BitmapFactory.Options opts = new BitmapFactory.Options();        
       // opts.inJustDecodeBounds = true;
        opts.inJustDecodeBounds = false; 
        //opts. = 4;             // scaled down by 4
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunset);
        View view = new Palette2(this, bitmap, 0, 0+0, 0);
       paletteLayout.addView(view);*/
        //setContentView(view);
        LinearLayout paletteLayout = (LinearLayout) findViewById(R.id.palette_layout);
		view = new CircleLayout(this, "MyStory");
       paletteLayout.addView(view);
     
       Resources res = getResources();
       DisplayMetrics metrics = res.getDisplayMetrics();

       this.displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
       		metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
       this.displayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
       		metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);

       centerX =  displayWidth / 2;
       centerY = displayHeight / 2;
       
       Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.funds_transfer);
		InfoGrafixImage im = new InfoGrafixImage(this, bmp, centerX, centerY, 120, 120);
		view.addImage(im);
       
       
	}catch(Exception e){
		Log.d(Main.TAG, "infografix home error", e);
	}
		lipukaApplication.setCurrentActivity(this);

    }
	
	 @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(CFChome.class, true);
			}
	    		
		@Override
		protected void onStop() {
			super.onStop();
			lipukaApplication.setActivityState(CFChome.class, false);
		}
		
		
		@Override
		public void onClick(View arg0) {
			
		}
		
		protected Dialog onCreateDialog(int id) {
		    Dialog dialog = null;
		    switch(id) {
		    case Main.DIALOG_MSG_ID:
		    	CustomDialog cd = new CustomDialog(this);
		    	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
		    	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
		    	dialog = cd;
		    	break;
		    case Main.DIALOG_ERROR_ID:
		    	cd = new CustomDialog(this);
		    	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
		    	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
		    	dialog = cd;
		    	break;
		    case Main.DIALOG_PROGRESS_ID:
		    	//builder = new AlertDialog.Builder(this);

		    	CustomProgressDialog pd = new CustomProgressDialog(this);
		    	dialog = pd;

		    	break;
		    default:
		        dialog = null;
		    }
		    return dialog;
		}
		protected  void onPrepareDialog(int id, Dialog dialog){
			//AlertDialog ad = (AlertDialog) dialog;
			switch(id) {
		    case Main.DIALOG_MSG_ID:
		    	CustomDialog cd = (CustomDialog)dialog;
		    	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
		    	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
		    	break;
		    case Main.DIALOG_ERROR_ID:
		      	cd = (CustomDialog)dialog;
		    	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
		    	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
		    	break;
		    case Main.DIALOG_PROGRESS_ID:
		    	CustomProgressDialog pd = (CustomProgressDialog)dialog;
		ProgressBar pb = (ProgressBar)pd.findViewById(R.id.progressbar_default);
		pb.setVisibility(View.GONE);
		pb.setVisibility(View.VISIBLE);
		    	break;
		    default:
		        dialog = null;
		    }
		}
}