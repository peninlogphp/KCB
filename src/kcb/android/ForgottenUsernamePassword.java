package kcb.android;


import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import kcb.android.AccountStatement.EcobankDateFieldListener;
import kcb.android.TransferFunds.OnBankSelectedListener;


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
import lipuka.android.view.anim.LipukaAnim;
import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
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
import android.widget.AdapterView.OnItemSelectedListener;

public class ForgottenUsernamePassword extends Activity implements OnClickListener, DateCaptureActivity{
	   
	Button submit, submitPassword;
	EditText cardNumber;
EditText username;
	EditText dateOfBirth, dateOfBirthPassword;
	EditText passportNumber, passportNumberPassport;
	EditText idNumber, idNumberPassport;
	RelativeLayout help;
	ImageButton closeHelp;
Button date, datePassport, call;
	String selectedEnrollment;
	String selectedMerchant, selectedAlias;

	String idTypeSelected, idTypeID, idTypePassport;

	LipukaApplication lipukaApplication;
	String selectedCurrency;
	LipukaListItem[] itemsArray;	EditText currentDateField;
	private byte activityShowing;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();

	   try{
		   setContentView(R.layout.forgotten_username_password);
		   cardNumber = (EditText) findViewById(R.id.card_number_field);
	        cardNumber.setInputType(InputType.TYPE_CLASS_NUMBER);		

username = (EditText) findViewById(R.id.username_field);
	        
	        dateOfBirth = (EditText) findViewById(R.id.date_field);
	        dateOfBirthPassword = (EditText) findViewById(R.id.date_field_password);

	        dateOfBirth.setOnTouchListener(new EcobankDateFieldListener());
	        dateOfBirthPassword.setOnTouchListener(new EcobankDateFieldListener());
	        
	          dateOfBirth.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);	
	          dateOfBirthPassword.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);	
	         		        
		        passportNumber = (EditText) findViewById(R.id.passport_number_field);
		        passportNumberPassport = (EditText) findViewById(R.id.passport_number_field_password);
       
		        idNumber = (EditText) findViewById(R.id.id_number_field);
		        idNumberPassport = (EditText) findViewById(R.id.id_number_field_password);
       
				
		        InputFilter[] filterArray = new InputFilter[1];
		     	   filterArray[0] = new InputFilter.LengthFilter(20);
		     	  passportNumber.setFilters(filterArray);
		     	 passportNumberPassport.setFilters(filterArray);
		     	idNumber.setFilters(filterArray);
		     	idNumberPassport.setFilters(filterArray);
		     	   
		        submit = (Button) findViewById(R.id.submit);
	        submit.setOnClickListener(this);
	        submitPassword = (Button) findViewById(R.id.submit_password);
	        submitPassword.setOnClickListener(this);
	        
	        date = (Button) findViewById(R.id.date_button);
	        date.setOnClickListener(this);

	        datePassport = (Button) findViewById(R.id.date_button_password);
	        datePassport.setOnClickListener(this);
	        call = (Button) findViewById(R.id.call_button);
	        call.setOnClickListener(this);
	        
	        Button showOrHideRetrieveUsername = (Button) findViewById(R.id.show_or_hide_retrieve_username);
	        showOrHideRetrieveUsername.setOnClickListener(this);
	        Button showOrHideRestPassword = (Button) findViewById(R.id.show_or_hide_reset_password);
	        showOrHideRestPassword.setOnClickListener(this);	      
	   	
	   	Button helpButton = (Button)findViewById(R.id.help);
		    helpButton.setOnClickListener(this);
		    Button homeButton = (Button)findViewById(R.id.home_button);
		    homeButton.setOnClickListener(this);
		    Button signOutButton = (Button)findViewById(R.id.sign_out);
		    signOutButton.setOnClickListener(this);
		    
		    homeButton.setVisibility(View.GONE);
		    signOutButton.setVisibility(View.GONE);
		    LinearLayout dividerOne = (LinearLayout) findViewById(R.id.divider_one);
		    dividerOne.setVisibility(View.GONE);
		    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
		    dividerTwo.setVisibility(View.GONE);
		    
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
			lipukaApplication.setCurrentActivity(this);
	    }  catch(Exception e){
			Log.d(Main.TAG, "creating Forgotten Username/Password error", e);

	} 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(ForgottenUsernamePassword.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(ForgottenUsernamePassword.class, false);
	    }
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	      //  MenuInflater inflater = getMenuInflater();
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
	    		if (submit == arg0){
	    			     lipukaApplication.clearNavigationStack();
	
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String cardNumberStr = cardNumber.getText().toString();
	    			String dateOfBirthStr = dateOfBirth.getText().toString();	
	    			String passportNumberStr = passportNumber.getText().toString();	
	    			String idNumberStr = idNumber.getText().toString();	

    				if(cardNumberStr.length() < 16 || cardNumberStr.length() > 16){
	    				valid = false;
errorBuffer.append("Please enter a card number with 16 digits\n");
	    			}
       				if(dateOfBirthStr.length() == 0 && passportNumberStr.length() == 0
       						&& idNumberStr.length() == 0){
	    				valid = false;
errorBuffer.append("Please provide either your date of birth, passport number, or ID number\n");
	    			}	
   		        	Calendar enteredDate = Calendar.getInstance();
			
       				try{	
       	    			if(valid){

       		        	StringTokenizer tokens = new StringTokenizer(dateOfBirthStr, "-");
       		        	int yr = Integer.parseInt(tokens.nextToken());
       		        	int mth = Integer.parseInt(tokens.nextToken())-1;
       		        	int day = Integer.parseInt(tokens.nextToken());
       		        	
       		        	enteredDate.set(yr, mth, day);
       		        	
       		        	Calendar currentDate = Calendar.getInstance();
       		        	
       		        	if(currentDate.before(enteredDate)){
       		        		valid = false;
       						errorBuffer.append("Date should not be in the future\n");	
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
    			    	   				payloadBuffer.append(cardNumberStr+"|");	    				
	    			    				payloadBuffer.append(dateOfBirthStr+"|");
	    			    				payloadBuffer.append(passportNumberStr+"|");
	    			    				payloadBuffer.append(idNumberStr+"|");
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				
	    			    				lipukaApplication.setCurrentDialogTitle("Response");
	    			    	        	lipukaApplication.setCurrentDialogMsg("Your request was successful. A one time password will be sent to your mobile number. You will be prompted to enter it for you to see your username. Thank you");
	    			    	            activityShowing = CustomDialog.RETRIEVE_USERNAME;	
	    			    	            lipukaApplication.showDialog(Main.DIALOG_MSG_ID);
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitPassword == arg0){
	    			
	   		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String usernameStr = username.getText().toString();	
	    			String dateOfBirthStr = dateOfBirthPassword.getText().toString();	
	    			String passportNumberStr = passportNumberPassport.getText().toString();	
	    			String idNumberStr = idNumberPassport.getText().toString();	

    				if(usernameStr == null || usernameStr.length() == 0){
	    				valid = false;
errorBuffer.append("Username is missing\n");
	    			}
       				if(dateOfBirthStr.length() == 0 && passportNumberStr.length() == 0
       						&& idNumberStr.length() == 0){
	    				valid = false;
errorBuffer.append("Please provide either your date of birth, passport number, or ID number\n");
	    			}	    			
   		        	Calendar enteredDate = Calendar.getInstance();
		try{	
       	    			if(valid){

       		        	StringTokenizer tokens = new StringTokenizer(dateOfBirthStr, "-");
       		        	int yr = Integer.parseInt(tokens.nextToken());
       		        	int mth = Integer.parseInt(tokens.nextToken())-1;
       		        	int day = Integer.parseInt(tokens.nextToken());
       		        	
       		        	enteredDate.set(yr, mth, day);
       		        	
       		        	Calendar currentDate = Calendar.getInstance();
       		        	
       		        	if(currentDate.before(enteredDate)){
       		        		valid = false;
       						errorBuffer.append("Date should not be in the future\n");	
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
    			    	   				payloadBuffer.append(usernameStr+"|");	    				
	    			    				payloadBuffer.append(dateOfBirthStr+"|");
	    			    				payloadBuffer.append(passportNumberStr+"|");
	    			    				payloadBuffer.append(idNumberStr+"|");
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.setCurrentDialogTitle("Response");
	    			    	        	lipukaApplication.setCurrentDialogMsg("Your request was successful. A one time password will be sent to your mobile number. You will be prompted to enter it and change it immediately by providing a new password. Thank you");
	    			    	            activityShowing = CustomDialog.RESET_PASSWORD;	
	    			    	            lipukaApplication.showDialog(Main.DIALOG_MSG_ID);

	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (arg0.getId() == R.id.date_button){
	    			currentDateField = dateOfBirth;
		        	showDialog(Main.DATE_DIALOG_ID);
	    	    }else if (arg0.getId() == R.id.date_button_password){
	        			currentDateField = dateOfBirthPassword;
	    	        	showDialog(Main.DATE_DIALOG_ID);
	        	}else if(arg0.getId() == R.id.call_button){
	        		try {
	        	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:+97126224443"));
	        	        startActivity(callIntent);
	        	    } catch (ActivityNotFoundException e) {
	        	        Log.d(Main.TAG, "Call failed", e);
	        	    }
	        	    }else if(arg0.getId() ==  R.id.help){
					//help.setVisibility(View.VISIBLE);
			      //  help.startAnimation(LipukaAnim.inFromRightAnimation());
	    		}else if(arg0.getId() ==  R.id.home_button){
				/* Intent i = new Intent(this, PaymaxHome.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);*/
	    		}else if (closeHelp == arg0){
	    			help.startAnimation(LipukaAnim.outToRightAnimation());
	    	    	help.setVisibility(View.GONE);
	    	    	}else if (arg0.getId() == R.id.show_or_hide_retrieve_username){
	    	    		FrameLayout  retrieveUsernameLayout = (FrameLayout) findViewById(R.id.retrieve_username_layout);
	    	    		Drawable img = null;
		if(retrieveUsernameLayout.isShown()){
			retrieveUsernameLayout.setVisibility(View.GONE);
	    	    			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			retrieveUsernameLayout.setVisibility(View.VISIBLE);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		FrameLayout  resetPasswordLayout = (FrameLayout) findViewById(R.id.reset_password_layout);
		    	    		resetPasswordLayout.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideResetPassword = (Button) findViewById(R.id.show_or_hide_reset_password);
	    	    	        showOrHideResetPassword.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_reset_password){
	    	    		FrameLayout  resetPasswordLayout = (FrameLayout) findViewById(R.id.reset_password_layout);
	    	    		Drawable img = null;
		if(resetPasswordLayout.isShown()){
			resetPasswordLayout.setVisibility(View.GONE);
	    	    			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			resetPasswordLayout.setVisibility(View.VISIBLE);
	    	    			img = getResources().getDrawable( R.drawable.hide );
	    	        		FrameLayout  retrieveUsernameLayout = (FrameLayout) findViewById(R.id.retrieve_username_layout);
		    	    		retrieveUsernameLayout.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideRetrieveUsername = (Button) findViewById(R.id.show_or_hide_retrieve_username);
	    	    	        showOrHideRetrieveUsername.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    			    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}
	    			}
	    		
	    		protected Dialog onCreateDialog(int id) {
	    	        Dialog dialog = null;
	    	        switch(id) {
	    	        case Main.DIALOG_MSG_ID:
	    	        	CustomDialog cd = new CustomDialog(this);
	    	        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	cd.setActivityShowing(activityShowing);

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
	    	        case Main.DATE_DIALOG_ID:
	    	        	dialog = new EcobankDatePickerDialog(this);

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
	    	        	cd.setActivityShowing(activityShowing);
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
	    	        case Main.DATE_DIALOG_ID:
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
	    		public class OnCurrencySelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedCurrency = itemsArray[pos].getValue();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    	    public class EcobankDateFieldListener implements View.OnTouchListener{

	                public EcobankDateFieldListener(){

	                }

	    		@Override
	    		public boolean onTouch(View v, MotionEvent event) {
	    			//Log.d(SalamaSureMain.TAG, "View ID: "+((LipukaEditText)v).getID()); 
	    				EditText editText = (EditText)v;
	    				currentDateField = editText;
	    	        	showDialog(Main.DATE_DIALOG_ID);	
	    			
	    			return true;
	    		}
	        }
	    	    public void setDate(String date){
	    			currentDateField.setText(date);		
	    		}
	    	    }