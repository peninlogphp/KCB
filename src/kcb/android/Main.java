package kcb.android;


import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;

import java.util.Calendar;

import kcb.android.R;
import lipuka.android.model.Bank;
import lipuka.android.model.LipukaDateListener;
import lipuka.android.model.synchronization.BackgroundTaskDone;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomDialogMain;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaEditText;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Main extends GDActivity {
	public static final String TAG = "LipukaAndroid";

	public static final int DIALOG_MSG_ID = 0;
	public static final int DIALOG_ERROR_ID = 1;
	public static final int DIALOG_PROGRESS_ID = 2;
	public static final int DIALOG_CONFIRM_ID = 3;
	public static final int DIALOG_PROFILE_UPDATE_MSG_ID = 4;
	public static final int DIALOG_PROFILE_UPDATE_ERROR_ID = 5;
	public static final int DATE_DIALOG_ID = 6;
	public static final int DIALOG_PIN_ID = 7;
	public static final int DIALOG_BALANCE_ID = 8;
	public static final int DIALOG_ACCOUNT_ID = 9;
	public static final int DIALOG_TRANSFER_FUNDS_OPTIONS_ID = 10;
	public static final int DIALOG_FT_INPUT_ID = 11;
	public static final int DIALOG_SINGLE_INPUT_ID = 12;
	public static final int DIALOG_CHEQUE_NUMBER_ID = 13;
	public static final int DIALOG_SERVICE_RESPONSE_ID = 14;
	public static final int DIALOG_SIGN_IN_ID = 15;
	public static final int TIME_DIALOG_ID = 16;

	public static final String CURRENT_BANK = "CURRENT_BANK";
	public static final String CURRENT_ACCOUNT = "CURRENT_ACCOUNT";
	public static final String CURRENT_SERVICE_XML = "CURRENT_SERVICE_XML";
	public static final String CURRENT_DIALOG_TITLE = "CURRENT_DIALOG_TITLE";
	public static final String CURRENT_DIALOG_MSG = "CURRENT_DIALOG_MSG";

	public static final byte MSG_START = 1;
	public static final byte MSG_FINISH = 2;
	
	public static final int PROMPT_PIN = 1;

	LipukaApplication lipukaApplication;
	Animation layoutAnimation;
	LipukaDateListener dateSetListener = new LipukaDateListener(this);
	LipukaDateFieldListener lipukaDateFieldListener = new LipukaDateFieldListener();
	EditText currentDateEditText;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.lipuka_layout);
        lipukaApplication = (LipukaApplication)getApplication();
       // LinearLayout layout = (LinearLayout) findViewById(R.id.lipuka_layout);
        lipukaApplication.initApp(this);
    	//layoutAnimation = AnimationUtils.loadAnimation(this, R.anim.zoom_enter);

    	/*final Boolean configurationChanged = (Boolean)getLastNonConfigurationInstance();
        if (configurationChanged == null) {
        	 lipukaApplication.goToHome();              
             } 
        else{
        	lipukaApplication.restoreAfterActivityRestart();
        }*/
    	if(savedInstanceState != null) {
    	    Bank currentBank = (Bank)savedInstanceState.getSerializable(CURRENT_BANK);
    	    lipukaApplication.setCurrentBank(currentBank);

    	    lipukaApplication.setAccount((String)savedInstanceState.getSerializable(CURRENT_ACCOUNT));
    	    lipukaApplication.setServiceXML((String)savedInstanceState.getSerializable(CURRENT_SERVICE_XML));
    	    lipukaApplication.setCurrentDialogTitle((String)savedInstanceState.getSerializable(CURRENT_DIALOG_TITLE));
    	    lipukaApplication.setCurrentDialogMsg((String)savedInstanceState.getSerializable(CURRENT_DIALOG_MSG));

    	    lipukaApplication.initHome();
    	}
       // lipukaApplication.setLayout(layout);
    	
        lipukaApplication.goToHome();  
		 lipukaApplication.parseServiceVersion();
 
        //startService(new Intent(lipukaApplication, UpdaterService.class));

    /*    addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
                .setDrawable(new ActionBarDrawable(this, R.drawable.home)), R.id.action_bar_home);
   */
      /* addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
                .setDrawable(new ActionBarDrawable(this, R.drawable.help)), R.id.action_bar_view_help);  
       */

		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setMainActivity(this);
 	
    }
  
    @Override
    protected void onStart() {
        super.onStart();
		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setMainActivity(this);
		lipukaApplication.setActivityState(Main.class, true);
		
		Log.d(Main.TAG, "called Main onStart"); 

	}
   /* @Override
    protected void onResume() {
        super.onResume();
        lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setActivityState(Main.class, true);
		Log.d(Main.TAG, "called Main onResume"); 
		}*/
@Override
protected void onStop() {
    super.onStop();
	lipukaApplication.setActivityState(Main.class, false);
	lipukaApplication.clearPIN();
}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
    	if(BackgroundTaskDone.isDone()){
    	 WebView webView =	lipukaApplication.getWebView();
    	 if (lipukaApplication.isWebView()) {
    		 if (webView.canGoBack()) {
    	         	webView.goBack();
    	         }
         	webView.goBack();
         }
    	 if (!lipukaApplication.handleBackButton()){
    		 super.onBackPressed();
    	 }
    	}else{
    		lipukaApplication.setCurrentDialogTitle("Info");
    	   	lipukaApplication.setCurrentDialogMsg(lipukaApplication.getString(R.string.request_on));
        	lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    	}
    }
    
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
        case DIALOG_MSG_ID:
        	CustomDialogMain cd = new CustomDialogMain(this);
        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
        	dialog = cd;
        	break;
        case DIALOG_ERROR_ID:
        	CustomDialog cde = new CustomDialog(this);
        	cde.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	cde.setMessage(lipukaApplication.getCurrentDialogMsg());
        	dialog = cde;
        	break;
        case DIALOG_PROGRESS_ID:
        	//builder = new AlertDialog.Builder(this);

        	CustomProgressDialog pd = new CustomProgressDialog(this);
        	dialog = pd;

        	break;
        case DATE_DIALOG_ID:
        	 final Calendar c = Calendar.getInstance();
             int mYear = c.get(Calendar.YEAR);
             int mMonth = c.get(Calendar.MONTH);
             int mDay = c.get(Calendar.DAY_OF_MONTH);
        	dialog = new DatePickerDialog(this, dateSetListener, mYear, mMonth, mDay);

            break;
        default:
            dialog = null;
        }
        return dialog;
    }
    protected  void onPrepareDialog(int id, Dialog dialog){
    	//AlertDialog ad = (AlertDialog) dialog;
    	switch(id) {
        case DIALOG_MSG_ID:
        	CustomDialogMain cd = (CustomDialogMain)dialog;
        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
        	break;
        case DIALOG_ERROR_ID:
        	CustomDialog cde = (CustomDialog)dialog;
        	cde.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	cde.setMessage(lipukaApplication.getCurrentDialogMsg());
        	break;
        case DIALOG_PROGRESS_ID:
        	CustomProgressDialog pd = (CustomProgressDialog)dialog;
ProgressBar pb = (ProgressBar)pd.findViewById(R.id.progressbar_default);
pb.setVisibility(View.GONE);
pb.setVisibility(View.VISIBLE);
        	break;
        case DATE_DIALOG_ID:

        	DatePickerDialog dpd = (DatePickerDialog)dialog;
        	final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            dpd.updateDate(mYear, mMonth, mDay);
        
           break;
        default:
            dialog = null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setMainActivity(this);
		lipukaApplication.setActivityState(Main.class, true);
switch(requestCode){
case PROMPT_PIN:
if(resultCode == Activity.RESULT_OK){
	String pin = (String) intent.
    getSerializableExtra(PINActivity.KEY_PIN);
	lipukaApplication.setPin(pin);
	lipukaApplication.executeRemoteRequest();
}
        break;
        default:
}
    }
    
    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
        case R.id.action_bar_home:
        	if(lipukaApplication.getNavigationStack().size()> 1){
        		lipukaApplication.goToHome();
        	}
			break;

        case R.id.action_bar_view_help:
            Toast.makeText(this, "Help pressed", Toast.LENGTH_SHORT).show();
            break;
        default:
            return super.onHandleActionBarItemClick(item, position);
        }
        return true;

    }
    
    public Animation getAnimation()  {
        return layoutAnimation;
    }
  /*  @Override
    public Object onRetainNonConfigurationInstance() {
    	super.onRetainNonConfigurationInstance();
    	//TODO save state
        if(lipukaApplication.getLayout() != null){
  Navigation nav =  lipukaApplication.peekNavigationStack();
        if(nav != null){
    LipukaDefaultActivity.saveData(lipukaApplication.getLayout(), nav.getHashMap());
        }
        }
        //final Boolean configurationChanged = Boolean.valueOf(true);
        final Boolean configurationChanged = null;
 return configurationChanged;
    }*/
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_BANK, lipukaApplication.getCurrentBank());
        outState.putSerializable(CURRENT_ACCOUNT, lipukaApplication.getAccount());
        outState.putSerializable(CURRENT_SERVICE_XML, lipukaApplication.getServiceXML());
        outState.putSerializable(CURRENT_DIALOG_TITLE, lipukaApplication.getCurrentDialogTitle());
        outState.putSerializable(CURRENT_DIALOG_MSG, lipukaApplication.getCurrentDialogMsg());

    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        }
    
    public class LipukaDateFieldListener implements View.OnTouchListener{

            public LipukaDateFieldListener(){

            }

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//Log.d(Main.TAG, "View ID: "+((LipukaEditText)v).getID()); 
				//LipukaEditText lipukaEditText = (LipukaEditText)v;
	        	currentDateEditText = (LipukaEditText)v;
	        	showDialog(DATE_DIALOG_ID);	
			
			return true;
		}
    }
	/*public DatePickerDialog.OnDateSetListener getCurrentDateSetListener() {
		return currentDateSetListener;
	}
	public void setCurrentDateSetListener(DatePickerDialog.OnDateSetListener currentDateSetListener){
		this.currentDateSetListener = currentDateSetListener;
	}*/
	public LipukaDateFieldListener getLipukaDateFieldListener() {
		return lipukaDateFieldListener;
	}
	public EditText getCurrentDateEditText() {
		return currentDateEditText;
	}

}