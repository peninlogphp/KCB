package kcb.android;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;


import kcb.android.R;
import lipuka.android.model.ActivityDateListener;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.EcobankDatePickerDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.adapter.ComboBoxAdapter;
import lipuka.android.view.anim.ExpandAnimation;
import lipuka.android.view.anim.LipukaAnim;
import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;

public class Alerts extends Activity implements OnClickListener, ResponseActivity,
DateCaptureActivity{
	   
	Button submitAccountDebitNcreditsAlerts, submitOffers, submitBillReminders;
	EditText billName, billReminderDate, billReminderTime;
	CheckBox alertOnDebits, alertOnCredits, alertOnOffers;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedDebitsCreditsMeans, selectedOffersMeans, selectedRepetition;

	LipukaApplication lipukaApplication;

	LipukaListItem[] alertMeansArray, repetitionArray;
	String amountStr, destinationStr;
	ActivityDateListener activityDateListener;
	EditText currentDateField;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.alerts);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Alerts");
	        billName = (EditText) findViewById(R.id.bill_name_field);
	        billReminderDate = (EditText) findViewById(R.id.bill_reminder_date_field);
	        billReminderTime = (EditText) findViewById(R.id.bill_reminder_time_field);
	        
 alertOnDebits = (CheckBox) findViewById(R.id.alert_on_debit);
 alertOnCredits = (CheckBox) findViewById(R.id.alert_on_credit);
 alertOnOffers = (CheckBox) findViewById(R.id.alert_on_offers);

	        billReminderDate.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
	       
	        billReminderDate.setOnTouchListener(new EcobankDateFieldListener());
	          
            Button billReminderDateButton = (Button)findViewById(R.id.bill_reminder_date_button);
            billReminderDateButton.setOnClickListener(this);
            
	        billReminderTime.setOnTouchListener(new TimeFieldListener());
 Button billReminderTimeButton = (Button)findViewById(R.id.bill_reminder_time_button);
            billReminderTimeButton.setOnClickListener(this);
            
            activityDateListener = new ActivityDateListener();
            
            final Spinner spinner = (Spinner) findViewById(R.id.means_spinner);
	        spinner.setOnItemSelectedListener(new OnDebitsCreditsMeansSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.alert_means));
	         alertMeansArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	alertMeansArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, alertMeansArray);
	   	spinner.setAdapter(adapter);
	   	
	
	       final Spinner offersMeansSpinner = (Spinner) findViewById(R.id.offers_means_spinner);
	       offersMeansSpinner.setOnItemSelectedListener(new OnOffersMeansSelectedListener());

	   	 ComboBoxAdapter offersMeansAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, alertMeansArray);
	   	offersMeansSpinner.setAdapter(offersMeansAdapter);	   
  	
	       final Spinner repetitionSpinner = (Spinner) findViewById(R.id.repetition_spinner);
	       repetitionSpinner.setOnItemSelectedListener(new OnRepetitionSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.repetition));
	         repetitionArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	repetitionArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter repetitionAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, repetitionArray);
	   	repetitionSpinner.setAdapter(repetitionAdapter);
	   	
	   	submitAccountDebitNcreditsAlerts = (Button) findViewById(R.id.account_debits_n_credits_submit);
	        submitAccountDebitNcreditsAlerts.setOnClickListener(this);
	        submitOffers = (Button) findViewById(R.id.offers_submit);
	        submitOffers.setOnClickListener(this);
	        submitBillReminders = (Button) findViewById(R.id.bill_reminder_submit);
	        submitBillReminders.setOnClickListener(this);
	        
	        Button showOrHideAccountDebitNcredits = (Button) findViewById(R.id.show_or_hide_account_debits_n_credits);
	        showOrHideAccountDebitNcredits.setOnClickListener(this);
	        Button showOrHideOffers = (Button) findViewById(R.id.show_or_hide_offers);
	        showOrHideOffers.setOnClickListener(this);
	        Button showOrHideBillReminders = (Button) findViewById(R.id.show_or_hide_bill_reminders);
	        showOrHideBillReminders.setOnClickListener(this);
     
	   	Button helpButton = (Button)findViewById(R.id.help);
		    helpButton.setOnClickListener(this);
		    Button homeButton = (Button)findViewById(R.id.home_button);
		    homeButton.setOnClickListener(this);

		    Button signOutButton = (Button)findViewById(R.id.sign_out);
		    signOutButton.setOnClickListener(this);
		   
			 help = (RelativeLayout) findViewById(R.id.help_layout);
		        WebView myWebView = (WebView) findViewById(R.id.webview);
		        WebSettings webSettings = myWebView.getSettings();
		        webSettings.setJavaScriptEnabled(true);
		    	myWebView.loadUrl("file:///android_asset/paybill.html");
		    	myWebView.setBackgroundColor(0);

		        closeHelp = (ImageButton) findViewById(R.id.close);
		        closeHelp.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                help.startAnimation(LipukaAnim.outToRightAnimation());
		            	help.setVisibility(View.GONE);
		            }
		        });  
	    }catch(Exception ex){
	    	Log.d(Main.TAG, "creating alerts error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(Alerts.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(Alerts.class, false);
	    }
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       // MenuInflater inflater = getMenuInflater();
	      //  inflater.inflate(R.menu.help_menu, menu);
	        return true;
	    }
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle item selection
	        switch (item.getItemId()) {
	        case R.id.help:
	        	help.setVisibility(View.VISIBLE);
	        	help.startAnimation(LipukaAnim.inFromRightAnimation());   
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    } 
	/*    @Override
	    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

	        switch (item.getItemId()) {
	            case R.id.action_bar_view_help:
	            	help.setVisibility(View.VISIBLE);
	                help.startAnimation(LipukaAnim.inFromRightAnimation());
	                break;

	            default:
	                return super.onHandleActionBarItemClick(item, position);
	        }

	        return true;
	    }*/

	    		public void onClick(View arg0) {
	    		if (submitAccountDebitNcreditsAlerts == arg0){
	    		    lipukaApplication.clearNavigationStack();
	
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			
	    			    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			payloadBuffer.append(amountStr+"|");	    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.setCurrentDialogTitle("Response");
	    			    		           lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
	    			    		           showDialog(Main.DIALOG_MSG_ID);		
	    			    			this.amountStr = amountStr;
destinationStr  = selectedDebitsCreditsMeans;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitOffers == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			
	    			    	/*if(alertOnDebits.isChecked()){

	    			    	   	if(accountAliasStr == null || accountAliasStr.length() == 0){
	    		    				valid = false;
	    		    				errorBuffer.append("Bill alias is missing\n");
	    		    			}    		
	    			    	}*/
	    						if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
    			    	   				payloadBuffer.append("Other|");
    			    	   				 				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.setCurrentDialogTitle("Response");
	    			    		           lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
	    			    		           showDialog(Main.DIALOG_MSG_ID);		
	    			    			this.amountStr = amountStr;
	    			    			//destinationStr  = selectedOffersMeans+", account number "+merchantsAccountNoStr;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitBillReminders == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	  			boolean valid = true;
    			StringBuffer errorBuffer = new StringBuffer();
    			String billNameStr = billName.getText().toString();
     			String billReminderDateStr = billReminderDate.getText().toString();	
String billReminderTimeStr = billReminderTime.getText().toString();	
Calendar enteredBillReminderDate = null;

    			if(billNameStr == null || billNameStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Bill name is missing\n");
    			}
    			if(billReminderDateStr == null || billReminderDateStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Date is missing\n");
    			}    			
		    			if(billReminderTimeStr == null || billReminderTimeStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Time is missing\n");
		    			}
    			    			
		    			try{	
		        			if(valid){
		    	
			    	        	Calendar currentDate = Calendar.getInstance();

			    	        	enteredBillReminderDate = Calendar.getInstance();

		    	        	StringTokenizer tokens = new StringTokenizer(billReminderDateStr, "-");
		    	        	int yr = Integer.parseInt(tokens.nextToken());
		    	        	int mth = Integer.parseInt(tokens.nextToken())-1;
		    	        	int day = Integer.parseInt(tokens.nextToken());
		    	        	
		    	        	enteredBillReminderDate.set(yr, mth, day);
		    	        			    	        	
		    	        	if(currentDate.after(enteredBillReminderDate)){
		    	        		valid = false;
		    					errorBuffer.append("Date should not be in the past\n");	
		    	        	}
		        			}
		        		}catch (NumberFormatException nfe){
		        			valid = false;
		    				errorBuffer.append("Enter a valid date value\n");			
		        		}catch (Exception e){
		        			valid = false;
		    				errorBuffer.append("Enter a valid date value\n");		
		        		} 	
    						if(valid){
    			    				StringBuffer payloadBuffer = new StringBuffer();
			    	   				payloadBuffer.append("Other|");
			    	   				 				
    			    	   			Navigation nav = new Navigation();
    			    			    nav.setPayload(payloadBuffer.toString());
    			    				lipukaApplication.pushNavigationStack(nav);
    			    				lipukaApplication.setPin(null);
    			    				lipukaApplication.setCurrentDialogTitle("Response");
 			    		           lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
 			    		           showDialog(Main.DIALOG_MSG_ID);			
    			    			this.amountStr = billReminderTimeStr;
    			    		//	destinationStr  = billNameStr+", account number "+merchantsAccountNoStr;
    			    			}else{
    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    			    			}		

	    		}else if(arg0.getId() ==  R.id.help){
					//help.setVisibility(View.VISIBLE);
			       // help.startAnimation(LipukaAnim.inFromRightAnimation());
	    		}else if(arg0.getId() ==  R.id.home_button){
				 Intent i = new Intent(this, StanChartHome.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
	    		}else if (closeHelp == arg0){
	    			help.startAnimation(LipukaAnim.outToRightAnimation());
	    	    	help.setVisibility(View.GONE);
	    	    	}else if (arg0.getId() == R.id.show_or_hide_account_debits_n_credits){
	    	    		LinearLayout  accountDebitsNcredits = (LinearLayout) findViewById(R.id.alerts_account_debits_n_credits);
	    	    		Drawable img = null;
		if(accountDebitsNcredits.isShown()){
			//accountDebitsNcredits.setVisibility(View.GONE);
			ExpandAnimation.collapse(accountDebitsNcredits);
	    	    			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//accountDebitsNcredits.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(accountDebitsNcredits);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  offers = (LinearLayout) findViewById(R.id.alerts_offers);
		    	    		offers.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideOffers = (Button) findViewById(R.id.show_or_hide_offers);
	    	    	        showOrHideOffers.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    	 		LinearLayout  billReminders = (LinearLayout) findViewById(R.id.alerts_bill_reminders);
		    	    		billReminders.setVisibility(View.GONE);
	    	    	        Button showOrHideBillReminders = (Button) findViewById(R.id.show_or_hide_bill_reminders);
	    	    	        showOrHideBillReminders.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_offers){
		    	    		LinearLayout  offers = (LinearLayout) findViewById(R.id.alerts_offers);
		    	    		Drawable img = null;
			if(offers.isShown()){
				//offers.setVisibility(View.GONE);
				ExpandAnimation.collapse(offers);
    			img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//offers.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(offers);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  accountDebitsNcredits = (LinearLayout) findViewById(R.id.alerts_account_debits_n_credits);
			    	    		accountDebitsNcredits.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideAccountDebitsNcredits = (Button) findViewById(R.id.show_or_hide_account_debits_n_credits);
		    	    	        showOrHideAccountDebitsNcredits.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		    	    	        LinearLayout  billReminders = (LinearLayout) findViewById(R.id.alerts_bill_reminders);
			    	    		billReminders.setVisibility(View.GONE);
		    	    	        Button showOrHideBillReminders = (Button) findViewById(R.id.show_or_hide_bill_reminders);
		    	    	        showOrHideBillReminders.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

			    	    	}else if (arg0.getId() == R.id.show_or_hide_bill_reminders){
			    	    		LinearLayout  billReminders = (LinearLayout) findViewById(R.id.alerts_bill_reminders);
			    	    		Drawable img = null;
				if(billReminders.isShown()){
					//billReminders.setVisibility(View.GONE);
					ExpandAnimation.collapse(billReminders);
  			img = getResources().getDrawable( R.drawable.show );
			    	    		}else{
			    	    			//billReminders.setVisibility(View.VISIBLE);
			    	    			ExpandAnimation.expand(billReminders);
			    	    			img = getResources().getDrawable( R.drawable.hide );
			    	    			LinearLayout  accountDebitsNcredits = (LinearLayout) findViewById(R.id.alerts_account_debits_n_credits);
				    	    		accountDebitsNcredits.setVisibility(View.GONE);
				    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
			    	    	        Button showOrHideAccountDebitsNcredits = (Button) findViewById(R.id.show_or_hide_account_debits_n_credits);
			    	    	        showOrHideAccountDebitsNcredits.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	        LinearLayout  offers = (LinearLayout) findViewById(R.id.alerts_offers);
				    	    		offers.setVisibility(View.GONE);
			    	    	        Button showOrHideOffers = (Button) findViewById(R.id.show_or_hide_offers);
			    	    	        showOrHideOffers.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	 		}
				((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

				    	    	}else if (R.id.sign_out == arg0.getId()){
				    	    		lipukaApplication.setProfileID(0);
				    	    		Intent i = new Intent(this, StanChartHome.class);
				    	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    	    		startActivity(i);
					    	    	}else if (arg0.getId() == R.id.bill_reminder_date_button){
					        			currentDateField = billReminderDate;
					    	        	showDialog(Main.DATE_DIALOG_ID);
					        	    }else if (arg0.getId() == R.id.bill_reminder_time_button){
					            			currentDateField = billReminderTime;
					        	        	showDialog(Main.TIME_DIALOG_ID);
					            	}
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
	    	        case Main.DIALOG_SERVICE_RESPONSE_ID:
	    	        	ResponseDialog rd = new ResponseDialog(this);
	    	        	rd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	rd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	dialog = rd;
	    	        	break;
	    	        case Main.DIALOG_CONFIRM_ID:
	    	        	ConfirmationDialog cfd = new ConfirmationDialog(this);
	    	        	cfd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cfd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	dialog = cfd;
	    	        	break;
	    	        case Main.DATE_DIALOG_ID:
	    	        	dialog = new EcobankDatePickerDialog(this);

	    	            break;
	    	        case Main.TIME_DIALOG_ID:
	    	        	dialog = new lipuka.android.view.TimePickerDialog(this);

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
	    	        case Main.DIALOG_SERVICE_RESPONSE_ID:
	    	        	ResponseDialog rd = (ResponseDialog)dialog;
	    	        	rd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	rd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	break;
	    	        case Main.DIALOG_CONFIRM_ID:
	    	        	ConfirmationDialog cfd = (ConfirmationDialog)dialog;
	    	        	cfd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cfd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	break;
	    	        case Main.DATE_DIALOG_ID:

	    	        	EcobankDatePickerDialog dpd = (EcobankDatePickerDialog)dialog;
	    	        	dpd.resetToCurrentDate();
	    	           break;
	    	        case Main.TIME_DIALOG_ID:

	    	        	lipuka.android.view.TimePickerDialog tpd = (lipuka.android.view.TimePickerDialog)dialog;
	    	        	tpd.resetToCurrentTime();
	    	           break;
	    	        default:
	    	            dialog = null;
	    	        }
	    	    }
	    	    @Override
	    	    public void onConfigurationChanged(Configuration newConfig) {
	    	        super.onConfigurationChanged(newConfig);
	    	        }
	    		@Override
	    	    public void onUserInteraction()
	    	    {
	    	        super.onUserInteraction();
	    	        lipukaApplication.touch();
	    	    }
	    		public class OnDebitsCreditsMeansSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedDebitsCreditsMeans = alertMeansArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnOffersMeansSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedOffersMeans = alertMeansArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnRepetitionSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedRepetition = repetitionArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
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

	    			yesButton.setText("OK");
	    			noButton.setText("Cancel");
	    			}

	    			@Override
	    			public void onClick(View v) {
	    			/** When OK Button is clicked, dismiss the dialog */
	    			if (v == yesButton){
	    			dismiss();
	    			lipukaApplication.setCurrentDialogTitle("PIN");
  			      lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
  			showDialog(Main.DIALOG_PIN_ID);
	    		}else if (v == noButton){
	    			dismiss();
	    			}
	    			}

	    			public void setCustomTitle(String title) {
	    			this.title.setText(title);
	    			}
	    			public void setMessage(String message) {
	    				this.message.setText(message);
	    				}
	    			}
	   public void showResponse(){
			lipukaApplication.setCurrentDialogTitle("Response");
		      lipukaApplication.setCurrentDialogMsg("Dear Alice, you have successfully paid "+destinationStr+". Amount paid was KES "+amountStr+". Thank you");
		      lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
		      showDialog(Main.DIALOG_SERVICE_RESPONSE_ID);
	   }
	   public class EcobankDateFieldListener implements View.OnTouchListener{

           public EcobankDateFieldListener(){

           }

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//Log.d(SalamaSureMain.TAG, "View ID: "+((LipukaEditText)v).getID()); 
				EditText editText = (EditText)v;
				currentDateField = editText;
				activityDateListener.setEditText(editText);
	        	showDialog(Main.DATE_DIALOG_ID);	
			
			return true;
		}
   }
	   public class TimeFieldListener implements View.OnTouchListener{

           public TimeFieldListener(){

           }

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//Log.d(SalamaSureMain.TAG, "View ID: "+((LipukaEditText)v).getID()); 
				EditText editText = (EditText)v;
				currentDateField = editText;
				activityDateListener.setEditText(editText);
	        	showDialog(Main.TIME_DIALOG_ID);	
			
			return true;
		}
   }
	   public void setDate(String date){
			currentDateField.setText(date);		
		}
	    	    }