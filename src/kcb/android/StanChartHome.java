package kcb.android;


import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

import javax.microedition.khronos.opengles.GL;

import kcb.android.EcobankHome.ConfirmationDialog;


import org.json.JSONException;
import org.json.JSONObject;


import kcb.android.R;

import geno.playtime.deftui.DeftUIRenderer;
import geno.playtime.deftui.MatrixTrackingGL;
import greendroid.app.GDActivity;
import greendroid.widget.PageIndicator;
import greendroid.widget.PagedAdapter;
import greendroid.widget.PagedView;
import greendroid.widget.PagedView.OnPagedViewChangeListener;
import kcb.android.AirtimeTopup;
import kcb.android.Main;
import lipuka.android.data.Constants;
import lipuka.android.model.bgtask.BgTask;
import lipuka.android.model.bgtask.BgThread;
import lipuka.android.model.Navigation;
import lipuka.android.model.ProfileXMLParser;
import lipuka.android.model.database.handlers.SetActivatedHandler;
import lipuka.android.view.CircleLayout;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.InfoGrafixImage;
import lipuka.android.view.InfoGrafixObject;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.SignInDialog;
import lipuka.android.view.adapter.LanguageListAdapter;
import lipuka.android.view.adapter.SetLanguageAdapter;
import lipuka.android.view.anim.LipukaAnim;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class StanChartHome extends Activity implements View.OnClickListener, ResponseActivity, ShowSignOutActivity,
AdapterView.OnItemClickListener{

	public static final  String TAG = "DeftUI";
	private GLSurfaceView surface;
	private DeftUIRenderer renderer;
	private GestureDetector gestureDetector;
	private float startX, startY;
	private float rayStart[], rayVector[];

	public boolean swiped = false;
	int topHeight;
	LipukaApplication lipukaApplication;
	RelativeLayout help, about;
String helpFile;
byte action;
SetLanguageAdapter adapter;
@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	   try{
		   lipukaApplication = (LipukaApplication)getApplication();
		   lipukaApplication.initHome();
		   
		   if(lipukaApplication.getPref("language") != null){ 
	  	    	
	   			createDashboard();
	   			
	}else{
	createSetLanguage();
	    }
        
       
        Button helpButton = (Button)findViewById(R.id.help);
	    helpButton.setOnClickListener(this);
	    Button homeButton = (Button)findViewById(R.id.home_button);
	    homeButton.setOnClickListener(this);
	    Button signOutButton = (Button)findViewById(R.id.sign_out);
	    signOutButton.setOnClickListener(this);
	    
	    homeButton.setVisibility(View.GONE);
	    LinearLayout dividerOne = (LinearLayout) findViewById(R.id.divider_one);
	    dividerOne.setVisibility(View.GONE);
	   
	    
	    help = (RelativeLayout) findViewById(R.id.help_layout);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    	myWebView.loadUrl(helpFile);
    	myWebView.setBackgroundColor(0);

        
            
       
	}catch(Exception e){
		Log.d(Main.TAG, "paymax home error", e);
	}
		lipukaApplication.setCurrentActivity(this);

    }
	
	 @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(StanChartHome.class, true);
		    Button signOutButton = (Button)findViewById(R.id.sign_out);

			if(lipukaApplication.getProfileID() == 0){
				 signOutButton.setVisibility(View.GONE);
				    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
				    dividerTwo.setVisibility(View.GONE);
				}else{
					 signOutButton.setVisibility(View.VISIBLE);
					    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
					    dividerTwo.setVisibility(View.VISIBLE);			
				}
			}
	    		
		@Override
		protected void onStop() {
			super.onStop();
			lipukaApplication.setActivityState(StanChartHome.class, false);
		}
		
		
		@Override
		protected void onPause() {
			super.onPause();
			if(gestureDetector != null){
	surface.onPause();
			}
			}

		@Override
		protected void onResume() {
			super.onResume();
			if(gestureDetector != null){
		surface.onResume();
			}
			}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if(gestureDetector != null){
				if (gestureDetector.onTouchEvent(event)) {
				return true;
			}
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				//DeftUIRenderer.sceneState.dxSpeed = 0.0f;
				
				//DeftUIRenderer.sceneState.dySpeed = 0.0f;
				renderer.sceneState.saveRotation();
				//startX = event.getX();
				//startY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				//DeftUIRenderer.sceneState.dy = event.getY() - startY;
				break;
			case MotionEvent.ACTION_UP:

				if(renderer.scrolled){ 
					if (Math.abs(renderer.sceneState.dy * 0.2f) > 26.565f && !swiped){
						if (renderer.sceneState.dy > 0) {
							renderer.sceneState.dx = -360;
								swiped = true;
								renderer.swipeIcons(true);
								   } else {
									   renderer.sceneState.dx = 360;
										swiped = true;
										renderer.swipeIcons(false);
								   }   
						renderer.sceneState.dy = 0.0f;
					  // DeftUIRenderer.sceneState.dy = -DeftUIRenderer.sceneState.dy;
				   }
				 //DeftUIRenderer.sceneState.dy = 0.0f;
					renderer.sceneState.setDampenDy(true);
					renderer.scrolled = false;
					swiped = false;
				 }

				break;
			}
		}
			return super.onTouchEvent(event);
		}

		private class GlAppGestureListener extends GestureDetector.SimpleOnGestureListener
	    {
			final float scale = getResources().getDisplayMetrics().density;
			private final int SWIPE_MIN_DISTANCE = (int) (120 * scale + 0.5f);
					  private final int SWIPE_MAX_OFF_PATH = (int) (250 * scale + 0.5f);
					  private final int SWIPE_THRESHOLD_VELOCITY = (int) (300 * scale + 0.5f);

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				//DeftUIRenderer.sceneState.switchToNextObject();
				return true;
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		 if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					    return false;
					   if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					     && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						   renderer.sceneState.dx = -360;
							swiped = true;
							renderer.swipeIcons(true);
							lipukaApplication.putPref("swipe", "swipe");
					        	  LinearLayout swipeLayout = (LinearLayout) findViewById(R.id.swipe_layout);
								    swipeLayout.setVisibility(View.GONE);
					   } else if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					     && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						   renderer.sceneState.dx = 360;
							swiped = true;
							renderer.swipeIcons(false);
							lipukaApplication.putPref("swipe", "swipe");
				        	  LinearLayout swipeLayout = (LinearLayout) findViewById(R.id.swipe_layout);
							    swipeLayout.setVisibility(View.GONE);
					   }
					   // measure speed in milliseconds

				//DeftUIRenderer.sceneState.dxSpeed = velocityX / 1000;
				//DeftUIRenderer.sceneState.dySpeed = velocityY / 1000;
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
			float tap[] = {	e.getX(), e.getY()};
			/*float ray[] = new float[3];
			float ray1[] = renderer.getWorldCoords(tap, 1.0f);
			float ray2[] = renderer.getWorldCoords(tap, -1.0f);
			ray[0] = ray2[0]-ray1[0];
			ray[1] = ray2[1]-ray1[1];
			ray[2] = ray2[2]-ray1[2];
		   */
			//float ray[] = renderer.getViewRay(tap);
			//float ray1[] = renderer.getWorldCoords(tap, 1.0f);

			getViewRay(tap);

			byte selection = renderer.testMenuHit(rayStart, rayVector);
			if(selection > 0){
				//Log.d(TAG, "center icon selected");
				
				 if(!lipukaApplication.isSavedProfile()){
			        	lipukaApplication.initHome();
			        }

			        	Intent i;
				int hit = renderer.getSelection();

				switch(hit){
				case 0:
					switch(selection){
					case 1:
						
						Vibrator vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, Forex.class);
						    startActivity(i);
						break;
					case 2:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, Settings.class);
						    startActivity(i);
					  	break;
					case 3:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, Locator.class);
						    startActivity(i);
					      break;
					case 4:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, MgHome.class);
						    startActivity(i);
						    break;
					case 5:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
							showResponse();
					      break;
					case 6:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      showResponse();
					      break;
					case 7:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, Cheques.class);
						    startActivity(i);
						    break;
						default:
							Log.d(TAG, "none selected ");		
					}
		     		  break;
				case 1:
					switch(selection){
					case 1:
						// i = new Intent(StanChartHome.this, MusicActivity.class);
			     		//  startActivity(i);
						Vibrator vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, FundsTransferNonCard.class);
						    startActivity(i);
						break;
					case 2:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, Merchants.class);
						    startActivity(i);
					      break;
					case 3:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, WUhome.class);
						    startActivity(i);
					      break;
					case 4:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      if(lipukaApplication.getProfileID() != 0)	{
								i = new Intent(StanChartHome.this, AirtimeTopup.class);
							    startActivity(i);				}else{
							lipukaApplication.setCurrentDialogTitle("Sign In");
						      //lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
						action = SignInDialog.AIRTIME_TOP_UP;
						showDialog(Main.DIALOG_SIGN_IN_ID);
							}	
					      break;
					case 5:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      if(lipukaApplication.getProfileID() != 0)	{
								i = new Intent(StanChartHome.this, AccountStmt.class);
							    startActivity(i);				}else{
							lipukaApplication.setCurrentDialogTitle("Sign In");
						      //lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
						action = SignInDialog.ACCOUNT_STATEMENT;
						showDialog(Main.DIALOG_SIGN_IN_ID);
							}
					      break;
					case 6:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      if(lipukaApplication.getProfileID() != 0)	{
					    	  lipukaApplication.setServiceID(String.valueOf(Constants.FETCH_BALANCE));
					    	  lipukaApplication.setCurrentDialogTitle("PIN");
			  			      lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
			  			showDialog(Main.DIALOG_PIN_ID);				}else{
							lipukaApplication.setCurrentDialogTitle("Sign In");
						      //lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
						action = SignInDialog.BALANCE;
						showDialog(Main.DIALOG_SIGN_IN_ID);
							}	
					      break;
					case 7:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      if(lipukaApplication.getProfileID() != 0)	{
								i = new Intent(StanChartHome.this, FundsTransfer.class);
							    startActivity(i);				}else{
							lipukaApplication.setCurrentDialogTitle("Sign In");
						      //lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
						action = SignInDialog.FUNDS_TRANSFER;
						showDialog(Main.DIALOG_SIGN_IN_ID);
							}	
					      break;
						default:
							Log.d(TAG, "none selected ");		
					}
	          	    break;
				case 2:
					switch(selection){
					case 1:
						Vibrator vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, AgencyBanking.class);
					      startActivity(i);
					      break;
					case 2:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      if(lipukaApplication.getProfileID() != 0)	{
								i = new Intent(StanChartHome.this, MyAccount.class);
							    startActivity(i);				}else{
							lipukaApplication.setCurrentDialogTitle("Sign In");
						      //lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
						action = SignInDialog.MY_ACCOUNT;
						showDialog(Main.DIALOG_SIGN_IN_ID);
							}
						break;
					case 3:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, InvestmentsNcredit.class);
					      startActivity(i);
					      break;
					case 4:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, PrepaidCards.class);
					      startActivity(i);
					      break;
					case 5:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, MobileMoneyTransfer.class);
						    startActivity(i);		
					      break;
					case 6:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, Insurance.class);
						    startActivity(i);	
						  	break;
					case 7:
						vibe = ( Vibrator ) getSystemService( VIBRATOR_SERVICE );
					      vibe.vibrate( 50 );
					      i = new Intent(StanChartHome.this, Tickets.class);
						    startActivity(i);
						    break;
						default:
							Log.d(TAG, "none selected ");		
					}
	      		  break;
					default:
						Log.d(TAG, "none selected ");		
				}
			}else{
				Log.d(TAG, "center icon not selected");				
			}
			/*int hit = renderer.testIntersection(rayStart, rayVector);

			switch(hit){
			case 1:
				Log.d(TAG, "music selected ");		
				break;
			case 2:
				Log.d(TAG, "banking selected ");		
				break;
			case 3:
				Log.d(TAG, "info selected ");		
				break;
				default:
					Log.d(TAG, "none selected ");		
			}*/
				return true;
			}
			@Override  
			public boolean onScroll(MotionEvent e1, MotionEvent e2,  
			        float distanceX, float distanceY) {  
				renderer.sceneState.dy += -distanceX;

				renderer.sceneState.setDampenDy(false);
				renderer.scrolled = true;
			    renderer.dz = renderer.dz-0.1f;
			    if(renderer.dz < -3.5f){
			    	renderer.dz = -3.5f;
			    }
			    return true;  
			} 
		
	    }
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(gestureDetector != null){
				switch (keyCode)
			{
			case KeyEvent.KEYCODE_L:
				//DeftUIRenderer.sceneState.toggleLighting();
				break;
			case KeyEvent.KEYCODE_F:
				//DeftUIRenderer.sceneState.switchToNextFilter();
				break;
			case KeyEvent.KEYCODE_SPACE:
				//DeftUIRenderer.sceneState.switchToNextObject();
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				renderer.sceneState.saveRotation();
				//DeftUIRenderer.sceneState.dxSpeed = 0.0f;
				//DeftUIRenderer.sceneState.dySpeed = 0.0f;
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				renderer.sceneState.saveRotation();
				//DeftUIRenderer.sceneState.dxSpeed -= 0.1f;
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				//DeftUIRenderer.sceneState.saveRotation();
				//DeftUIRenderer.sceneState.dxSpeed += 0.1f;
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				//DeftUIRenderer.sceneState.saveRotation();
				//DeftUIRenderer.sceneState.dySpeed -= 0.1f;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				//DeftUIRenderer.sceneState.saveRotation();
				//DeftUIRenderer.sceneState.dySpeed += 0.1f;
				break;
			}
		}
			   if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
		                && keyCode == KeyEvent.KEYCODE_BACK
		                && event.getRepeatCount() == 0) {
		            // Take care of calling this method on earlier versions of
		            // the platform where it doesn't exist.
		            onBackPressed();
		        }
			return super.onKeyDown(keyCode, event);
		}

		private void getViewRay(float tap[]){
			int width =  renderer.getScreenWidth();
			int height = renderer.getScreenHeight();
			tap[0] = tap[0] - renderer.getXShift();
			tap[1] = tap[1] - renderer.getYShift();
			tap[1] = tap[1] - topHeight;

			float halfWidth = width/2.0f;
			float halfHeight = height/2.0f;
			float aspectRatio = (float)width / (float)height;
			double halfAngleRadians = Math.toRadians(45/2);
			float tanHalfFOV = (float)Math.tan(halfAngleRadians);
			float ratioX =  tanHalfFOV *(tap[0]/halfWidth-1.0f)/aspectRatio;
			float ratioY = tanHalfFOV *(1.0f-tap[1]/halfHeight);

			float point1In[] = {ratioX*1, ratioY*1, -1.0f, 1.0f};
			float point2In[] = {ratioX*100, ratioY*100, -100.0f, 1.0f};
			float point1[] = new float[4];
			float point2[] = new float[4];
			
			float[] inverseTransformationMatrix;
			inverseTransformationMatrix = new float[16];
	    
		Matrix.invertM(inverseTransformationMatrix, 0, renderer.getMatrixGrabber().mModelView, 0);
		
		Log.d(TAG, "point1 x value: "+point1In[0]);
		Log.d(TAG, "point1 y value: "+point1In[1]);
		Log.d(TAG, "point1 z value: "+point1In[2]);
		
		/*
		 * Apply the inverse to the point in clip space
		 */
		Matrix.multiplyMV(point1, 0, inverseTransformationMatrix, 0, point1In, 0);
		Matrix.multiplyMV(point2, 0, inverseTransformationMatrix, 0, point2In, 0);
		
		rayStart[0] = point1[0];
		rayStart[1] = point1[1];
		rayStart[2] = point1[2];
		rayStart[3] = 1.0f;
	    
	    rayVector[0] = point2[0]-point1[0];
	    rayVector[1] = point2[1]-point1[1];
	    rayVector[2] = point2[2]-point1[2];
	    rayVector[3] = 0.0f;
		}
		
		@Override
		public void onAttachedToWindow() {
		    super.onAttachedToWindow();
		    Window window = getWindow();
		    window.setFormat(PixelFormat.RGBA_8888);
		}	
		
		private void setTopHeight() {
			Rect r = new Rect();		
		    Window w = getWindow();
		    w.getDecorView().getWindowVisibleDisplayFrame(r);
		    int statusBarHeight = r.top;
			    //int viewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
			    //int titleBarHeight = (viewTop - statusBarHeight);
		    final float scale = getResources().getDisplayMetrics().density;
		    int titleBarHeight = (int) (52 * scale + 0.5f);
			    topHeight = statusBarHeight + titleBarHeight;
				Log.d(TAG, "statusBarHeight: "+statusBarHeight);
				Log.d(TAG, "titleBarHeight: "+titleBarHeight);
				Log.d(TAG, "topHeight: "+topHeight);

			}
		@Override
		public void onClick(View arg0) {
			switch(arg0.getId()){
			
			case R.id.sign_in_button:
				if(lipukaApplication.getProfileID() != 0)	{
					lipukaApplication.setCurrentDialogTitle("Sign In");
		              lipukaApplication.setCurrentDialogMsg("You are already signed in");
		              showDialog(Main.DIALOG_MSG_ID);
		              }else{
				lipukaApplication.setCurrentDialogTitle("Sign In");
			      //lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
			action = SignInDialog.NOTHING;
			showDialog(Main.DIALOG_SIGN_IN_ID);
				}	
				break;
			case R.id.sign_up_button:
				Intent i = new Intent(StanChartHome.this, Enroll.class);
			    startActivity(i);
				break;
			case R.id.next_button:
		        PagedView pagedView = (PagedView) findViewById(R.id.paged_view);
 pagedView.smoothScrollToNext();
				break;
			case R.id.prev_button:
		        pagedView = (PagedView) findViewById(R.id.paged_view);
 pagedView.smoothScrollToPrevious();
				break;
			case R.id.sign_out:
	    		lipukaApplication.setProfileID(0);
				arg0.setVisibility(View.GONE);
				    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
				    dividerTwo.setVisibility(View.GONE);
				break;
			default:
			}
			//Toast.makeText(Home.this, "Clicked button", Toast.LENGTH_LONG).show(); 
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
		    case Main.DIALOG_PIN_ID:
	        	PinInputDialog pid = new PinInputDialog(this);
	        	pid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	        	pid.setMessage(lipukaApplication.getCurrentDialogMsg());
	        	dialog = pid;
	        	break;
		    case Main.DIALOG_SIGN_IN_ID:
		    	SignInDialog sid = new SignInDialog(this);
		    	sid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
		    	sid.setMessage(lipukaApplication.getCurrentDialogMsg());
		    	sid.setAction(action);
	        	dialog = sid;
	        	break;
		    case Main.DIALOG_CONFIRM_ID:
		    	ConfirmationDialog cfd = new ConfirmationDialog(this);
		    	cfd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
		    	cfd.setMessage(lipukaApplication.getCurrentDialogMsg());
		    	dialog = cfd;
		    	break;
		    case Main.DIALOG_SERVICE_RESPONSE_ID:
	        	ResponseDialog rd = new ResponseDialog(this);
	        	rd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	        	rd.setMessage(lipukaApplication.getCurrentDialogMsg());
	        	dialog = rd;
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
		    case Main.DIALOG_PIN_ID:
	        	PinInputDialog pid = (PinInputDialog)dialog;
	        	pid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	        	pid.setMessage(lipukaApplication.getCurrentDialogMsg());
	        	break;
		    case Main.DIALOG_SIGN_IN_ID:
		    	SignInDialog sid = (SignInDialog)dialog;
		    	sid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
		    	sid.setMessage(lipukaApplication.getCurrentDialogMsg());
		    	sid.setAction(action);
	        	break;
		    case Main.DIALOG_CONFIRM_ID:
		    	ConfirmationDialog cfd = (ConfirmationDialog)dialog;
		    	cfd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
		    	cfd.setMessage(lipukaApplication.getCurrentDialogMsg());
		    	break;
		    case Main.DIALOG_SERVICE_RESPONSE_ID:
	        	ResponseDialog rd = (ResponseDialog)dialog;
	        	rd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	        	rd.setMessage(lipukaApplication.getCurrentDialogMsg());
	        	break;
		    default:
		        dialog = null;
		    }
		}
	
	
	    public void showResponse(){
        	lipukaApplication.setCurrentDialogTitle("Response");
              lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
              showDialog(Main.DIALOG_MSG_ID);
        }
	    
	    public class ConfirmationDialog extends Dialog implements OnClickListener {
	    	Button yesButton;
	    	Button noButton;
	    	TextView title;
	    	TextView message;
	    	public ConfirmationDialog(Context context) {
	    	super(context);

	    	/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
	    	requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	/** Design the dialog in main.xml file */
	    	setContentView(R.layout.confirmation_dialog);
	    	yesButton = (Button) findViewById(R.id.yes_button);
	    	yesButton.setOnClickListener(this);
	    	noButton = (Button) findViewById(R.id.no_button);
	    	noButton.setOnClickListener(this);
	    	title = (TextView) findViewById(R.id.title);
	    	message = (TextView) findViewById(R.id.message);

	    	}

	    	@Override
	    	public void onClick(View v) {
	    	/** When OK Button is clicked, dismiss the dialog */
	    	if (v == yesButton){
	    		lipukaApplication.setProfileID(0);
	    	dismiss();
	    }else if (v == noButton){
	    	dismiss();
	    	}
	    	finish();

	    	}

	    	public void setCustomTitle(String title) {
	    	this.title.setText(title);
	    	}
	    	public void setMessage(String message) {
	    		this.message.setText(message);
	    		}
	    	}

	    @Override
	    public void onBackPressed() {
	    	if(lipukaApplication.getProfileID() != 0){
	    	lipukaApplication.setCurrentDialogTitle("Sign Out");
	       	lipukaApplication.setCurrentDialogMsg("Would you like to sign out?");
	    	lipukaApplication.showDialog(Main.DIALOG_CONFIRM_ID);
	    	}else{
	    		super.onBackPressed();
	    	}
	    }
	    public void showSignOutBtn(){
		    Button signOutButton = (Button)findViewById(R.id.sign_out);

					 signOutButton.setVisibility(View.VISIBLE);
					    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
					    dividerTwo.setVisibility(View.VISIBLE);	
	    }
	    private void createDashboard(){
	        setContentView(R.layout.stanchart_home);

	        gestureDetector = new GestureDetector(this, new GlAppGestureListener());

	         surface = (GLSurfaceView)findViewById(R.id.glView);
	    
	         //new GLSurfaceView(this);
	         surface.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	         surface.getHolder().setFormat(PixelFormat.RGBA_8888);
	         surface.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	         surface.setZOrderOnTop(true);
	         Display display = getWindowManager().getDefaultDisplay(); 
	       
	         renderer = new DeftUIRenderer(display.getWidth(), display.getHeight(), this);
	         
	         surface.setGLWrapper(new GLSurfaceView.GLWrapper()
	         {
	 			@Override
	 			public GL wrap(GL gl) {
	 				// TODO Auto-generated method stub
	                 return new MatrixTrackingGL(gl);
	 			}
	         });
	         surface.setRenderer(renderer);
	   
	         rayStart = new float[4];
	         rayVector = new float[4];  
	         surface.post(new Runnable()
	         {
	         	public void run()
	         	{
	         	setTopHeight();
	         	}
	         	});
	         
	        /* Button next = (Button)StanChartHome.this.findViewById(R.id.next_button);
	     	Button prev = (Button)StanChartHome.this.findViewById(R.id.prev_button);
	     	next.setOnClickListener(this);
	     	prev.setOnClickListener(this);*/
	     	
	         Button signIn = (Button)StanChartHome.this.findViewById(R.id.sign_in_button);
	         Button signUp = (Button)StanChartHome.this.findViewById(R.id.sign_up_button);
	         signIn.setOnClickListener(this);
	         signUp.setOnClickListener(this);

	         
	  helpFile = "file:///android_asset/main.html";
	         
	 		
	 		//end temp
	       /*  addActionBarItem(getActionBar()
	                 .newActionBarItem(NormalActionBarItem.class)
	                 .setDrawable(new ActionBarDrawable(this, R.drawable.gd_action_bar_help)), R.id.action_bar_view_help);        
	 	 	*/
	        
	         
	         /*if(lipukaApplication.getPref("swipe") == null){
	         	  LinearLayout swipeLayout = (LinearLayout) findViewById(R.id.swipe_layout);
	 			    swipeLayout.setVisibility(View.VISIBLE);		
	 			}*/
	         ImageButton close = (ImageButton) findViewById(R.id.close);
	         close.setOnClickListener(new View.OnClickListener() {
	             @Override
	             public void onClick(View v) {
	             	surface.setVisibility(View.VISIBLE);
	   help.startAnimation(LipukaAnim.outToRightAnimation());
	             	help.setVisibility(View.GONE);
	             }
	         });         	
	    }
private void createSetLanguage(){
    setContentView(R.layout.set_language);
    TextView title = (TextView)findViewById(R.id.title);
    title.setText("Select Language");
    ListView listView = (ListView)findViewById(R.id.lipuka_list_view);
	
	ArrayList<LipukaListItem> items= new ArrayList<LipukaListItem>();
	items.add(new LipukaListItem("0", "English", "USD"));
	items.add(new LipukaListItem("0", "Kiswahili", "EUR"));
 

	adapter = new SetLanguageAdapter(this, items);
	listView.setAdapter(adapter);
	Log.d(Main.TAG, "created listview");

	AnimationSet set = new AnimationSet(true);

    Animation animation = new AlphaAnimation(0.0f, 1.0f);
    animation.setDuration(50);
    set.addAnimation(animation);

    animation = new TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
        Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
    );
    animation.setDuration(100);
    set.addAnimation(animation);

    LayoutAnimationController controller =
            new LayoutAnimationController(set, 0.5f);
    listView.setLayoutAnimation(controller);
    listView.setOnItemClickListener(this);
    helpFile = "file:///android_asset/paybill.html";
    
    ImageButton close = (ImageButton) findViewById(R.id.close);
    close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
help.startAnimation(LipukaAnim.outToRightAnimation());
        	help.setVisibility(View.GONE);
        }
    });
	    	
	    }
@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	Intent i = new Intent(StanChartHome.this, PayMaxDashboard.class);
	  startActivity(i);
	  LipukaListItem item = (LipukaListItem)adapter.getItem(position);
	  lipukaApplication.putPref("language", item.getValue());
}
	    }