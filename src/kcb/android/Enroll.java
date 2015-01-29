package kcb.android;


import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;


import org.json.JSONArray;
import org.json.JSONObject;







import kcb.android.R;
import lipuka.android.model.ActivityDateListener;
import lipuka.android.model.Navigation;
import lipuka.android.model.responsehandlers.SignUpHandler;
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
import kcb.android.EcobankHome.ConfirmationDialog;
import kcb.android.SignUp.EcobankDateFieldListener;
import kcb.android.TransferFunds.MyGestureDetector;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
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
import android.view.inputmethod.EditorInfo;
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

public class Enroll extends Activity implements OnClickListener, DateCaptureActivity{
	   
	Button submit, submitAccount;
	EditText fname, mname, lname, accountNo, dateOfBirth, idNo;
	EditText cardHolderName, 
	cardNo, securityCode;
	CheckBox acceptTerms, acceptTermsAccount;
	RelativeLayout help;
	ImageButton closeHelp;

	LipukaApplication lipukaApplication;
	
	String selectedMonth, selectedYear; 
	LipukaListItem[] monthsArray, yearsArray;
	
	ActivityDateListener activityDateListener;
	EditText currentDateField;
	
	ViewFlipper flipper;
	GestureDetector gestureDetector;
	int selected;
	LinearLayout account, card;
	TextView accountText, cardText;
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.enroll);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Sign Up");
	        cardHolderName = (EditText) findViewById(R.id.card_holder_name_field);
	        cardNo = (EditText) findViewById(R.id.card_no_field);
	        securityCode = (EditText) findViewById(R.id.security_code_field);
	        acceptTerms = (CheckBox) findViewById(R.id.accept_terms);
	        
	        TextView termsLink = (TextView) findViewById(R.id.terms_link);
	        SpannableString mySpannableString = new SpannableString("Terms of Service");
	    			 mySpannableString.setSpan(new UnderlineSpan(), 0, mySpannableString.length(), 0);
	    			termsLink.setText(mySpannableString);
	    			termsLink.setOnClickListener(this);
	    			
	    			 fname = (EditText) findViewById(R.id.fname_field);
	    		        mname = (EditText) findViewById(R.id.mname_field);
	    		        lname = (EditText) findViewById(R.id.lname_field);
	    		        accountNo = (EditText) findViewById(R.id.account_number_field);
	    		        dateOfBirth = (EditText) findViewById(R.id.date_of_birth_field);
	    		        idNo = (EditText) findViewById(R.id.id_number_field);
	    		        acceptTermsAccount = (CheckBox) findViewById(R.id.accept_terms_account);
	    		        
	    		        TextView termsLinkAccount = (TextView) findViewById(R.id.terms_link_account);
	    		        termsLinkAccount.setText(mySpannableString);
	    		    			 termsLinkAccount.setOnClickListener(this);
	    		    			
  cardNo.setInputType(InputType.TYPE_CLASS_NUMBER);
  securityCode.setInputType(InputType.TYPE_CLASS_NUMBER);
  securityCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
  securityCode.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
  
  accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
  dateOfBirth.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
  idNo.setInputType(InputType.TYPE_CLASS_NUMBER);
    dateOfBirth.setOnTouchListener(new EcobankDateFieldListener());
    
    
  final Spinner spinner = (Spinner) findViewById(R.id.expiry_month_spinner);
  spinner.setOnItemSelectedListener(new OnExpiryMonthSelectedListener());
  
try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.expirymths));
   monthsArray = new LipukaListItem[sources.length()];
JSONObject currentSource;
  for(int i = 0; i < sources.length(); i++){
  	currentSource = sources.getJSONObject(i);
  	LipukaListItem lipukaListItem = new LipukaListItem("", 
currentSource.getString("name"), currentSource.getString("value"));
  	monthsArray[i]= lipukaListItem;   	
  }
  }catch(Exception ex){
  	Log.d(Main.TAG, "creating months list error", ex);

	}
	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, monthsArray);
	spinner.setAdapter(adapter);
	
	final Spinner spinnerExpiryYear = (Spinner) findViewById(R.id.expiry_year_spinner);
	spinnerExpiryYear.setOnItemSelectedListener(new OnExpiryYearSelectedListener());
	  
	try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.expiryyrs));
	   yearsArray = new LipukaListItem[sources.length()];
	JSONObject currentSource;
	  for(int i = 0; i < sources.length(); i++){
	  	currentSource = sources.getJSONObject(i);
	  	LipukaListItem lipukaListItem = new LipukaListItem("", 
	currentSource.getString("name"), currentSource.getString("value"));
	  	yearsArray[i]= lipukaListItem;   	
	  }
	  }catch(Exception ex){
	  	Log.d(Main.TAG, "creating years list error", ex);

		}
		 ComboBoxAdapter adapterExpiryYear = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, yearsArray);
		spinnerExpiryYear.setAdapter(adapterExpiryYear);		      

	   	submit = (Button) findViewById(R.id.submit);
	   	submit.setOnClickListener(this);
	   	submitAccount = (Button) findViewById(R.id.submit_account);
	   	submitAccount.setOnClickListener(this);
	   	Button dateOfbirthButton = (Button)findViewById(R.id.date_of_birth_button);
	       dateOfbirthButton.setOnClickListener(this);
	       
 activityDateListener = new ActivityDateListener();
	       
	       gestureDetector = new GestureDetector(this, new MyGestureDetector());
	        flipper = (ViewFlipper) findViewById(R.id.flipper);
	        
	        account = (LinearLayout)findViewById(R.id.enroll_account);
	        card = (LinearLayout)findViewById(R.id.enroll_card);
	        accountText = (TextView)findViewById(R.id.account_text);
	        cardText = (TextView)findViewById(R.id.card_text);
	        accountText.setOnClickListener(this);
	        cardText.setOnClickListener(this);
	       
	   	Button helpButton = (Button)findViewById(R.id.help);
		    helpButton.setOnClickListener(this);
		    Button homeButton = (Button)findViewById(R.id.home_button);
		    homeButton.setOnClickListener(this);

		    Button signOutButton = (Button)findViewById(R.id.sign_out);
		    signOutButton.setVisibility(View.GONE);
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
	    }catch(Exception ex){
	    	Log.d(Main.TAG, "creating sign up error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(Enroll.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(Enroll.class, false);
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
	    		if (submit == arg0){
	    		    lipukaApplication.clearPayloadObject();
	
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String cardHolderNameStr = cardHolderName.getText().toString();
	    			String cardNoStr = cardNo.getText().toString();	
	    			String securityCodeStr = securityCode.getText().toString();	

	    			if(cardHolderNameStr == null || cardHolderNameStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Card holder name is missing\n");
	    			}
	    			if(cardNoStr == null || cardNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Card number is missing\n");
	    			}
	    			if(securityCodeStr == null || securityCodeStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Security code is missing\n");
	    			}    			
	    				if(!acceptTerms.isChecked()){
	    				valid = false;
	    				errorBuffer.append("Please read and agree with our terms of service\n");
	    			} 
	    				securityCode.setText("");
	    			    			if(valid){
	    			    				
	    			    				lipukaApplication.putPayload("card_no", cardNoStr);
	    			    				lipukaApplication.putPayload("cvv_code", securityCodeStr);
	    			    				lipukaApplication.putPayload("card_name", cardHolderNameStr);
	    			    				lipukaApplication.putPayload("expiry_date", "20"+selectedYear+"-"+selectedMonth+"-01");
	    			    				lipukaApplication.consumeService("70", new SignUpHandler(lipukaApplication, this, cardNoStr, cardHolderNameStr,
	    			    						securityCodeStr, "20"+selectedYear+"-"+selectedMonth+"-01"));
	    			    				
	    			    				//Intent i = new Intent(Enroll.this, CreateMobileAccount.class);
	    				    			  //startActivity(i);

	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}
	    		}else if (submitAccount == arg0){
	    		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    		    			StringBuffer errorBuffer = new StringBuffer();
	    		    			String fnameStr = fname.getText().toString();	
	    		    			String mnameStr = mname.getText().toString();	
	    		    			String lnameStr = lname.getText().toString();	
	    		    			String accountNoStr = accountNo.getText().toString();	
	    		    			String dateOfBirthStr = dateOfBirth.getText().toString();
	    		    			String idNoStr = idNo.getText().toString();	

	    		    			if(fnameStr == null || fnameStr.length() == 0){
	    		    				valid = false;
	    		    				errorBuffer.append("First name is missing\n");
	    		    			}
	    		    			if(mnameStr == null || mnameStr.length() == 0){
	    		    				valid = false;
	    		    				errorBuffer.append("Middle name is missing\n");
	    		    			}
	    		    			if(lnameStr == null || lnameStr.length() == 0){
	    		    				valid = false;
	    		    				errorBuffer.append("Last name is missing\n");
	    		    			}
	    		    			if(accountNoStr == null || accountNoStr.length() == 0){
	    		    				valid = false;
	    		    				errorBuffer.append("Account number is missing\n");
	    		    			}
	    		    			if(dateOfBirthStr == null || dateOfBirthStr.length() == 0){
	    		    				valid = false;
	    		    				errorBuffer.append("Date of birth  is missing\n");
	    		    			}
	    		    			if(idNoStr == null || idNoStr.length() == 0){
	    		    				valid = false;
	    		    				errorBuffer.append("ID number is missing\n");
	    		    			}    			
	    		    			Calendar enteredDateOfBirth = null;
	    		    			try{	
	    		        			if(valid){
	    		    	
	    		        				enteredDateOfBirth = Calendar.getInstance();

	    		    	        	StringTokenizer tokens = new StringTokenizer(dateOfBirthStr, "-");
	    		    	        	int yr = Integer.parseInt(tokens.nextToken());
	    		    	        	int mth = Integer.parseInt(tokens.nextToken())-1;
	    		    	        	int day = Integer.parseInt(tokens.nextToken());
	    		    	        	
	    		    	        	enteredDateOfBirth.set(yr, mth, day);
	    		    	        	
	    		    	        	Calendar currentDate = Calendar.getInstance();
	    		    	        	
	    		    	        	if(currentDate.before(enteredDateOfBirth)){
	    		    	        		valid = false;
	    		    					errorBuffer.append("Date of birth should not be in the future\n");	
	    		    	        	}
	    		        			}
	    		        		}catch (NumberFormatException nfe){
	    		        			valid = false;
	    		    				errorBuffer.append("Enter a valid date value\n");			
	    		        		}catch (Exception e){
	    		        			valid = false;
	    		    				errorBuffer.append("Enter a valid date value\n");		
	    		        		}
	    		        		if(!acceptTermsAccount.isChecked()){
	    		    				valid = false;
	    		    				errorBuffer.append("Please read and agree with our terms of service\n");
	    		    			} 
	    			    			if(valid){
	    		    			    				
	    		    			    				Intent i = new Intent(Enroll.this, CreateMobileAccountNormal.class);
	    		    			    			startActivity(i);
	    		    			    			}else{
	    		    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    		    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    		    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    		    			    			}	

	    		    		}else if(arg0.getId() ==  R.id.home_button){
				 Intent i = new Intent(this, StanChartHome.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
	    		}else if (closeHelp == arg0){
	    			help.startAnimation(LipukaAnim.outToRightAnimation());
	    	    	help.setVisibility(View.GONE);
	    	    	}else if (R.id.sign_out == arg0.getId()){
	    	    		lipukaApplication.setProfileID(0);
	    	    		Intent i = new Intent(this, StanChartHome.class);
	    	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	    		startActivity(i);
		    	    	}else if (arg0.getId() == R.id.date_of_birth_button){
		        			currentDateField = dateOfBirth;
		    	        	showDialog(Main.DATE_DIALOG_ID);
		        	}else if(arg0.getId() == R.id.card_text){
	    			 if (selected == 1) {
		     			    flipper.setInAnimation(inFromLeftAnimation());
		     			    flipper.setOutAnimation(outToRightAnimation());
		     			    flipper.setDisplayedChild(0);
		     			    selected = 0;
		     		   setSelectedBg();
		     			   }else{
		     				   return;
		     			   }			   
		     	}else if(arg0.getId() == R.id.account_text){
		     		if (selected == 0) {
		     		    flipper.setInAnimation(inFromRightAnimation());
		     		    flipper.setOutAnimation(outToLeftAnimation());
		     		    flipper.setDisplayedChild(1);
		     		    selected = 1;
		     			   setSelectedBg();
		     		   }else{
		     			   return;
		     		   }			   
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
	    		public class OnExpiryMonthSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedMonth = monthsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnExpiryYearSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedYear = yearsArray[pos].getText();
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
		/*	lipukaApplication.setCurrentDialogTitle("Response");
		      lipukaApplication.setCurrentDialogMsg("Dear Alice, you have successfully paid "+destinationStr+". Amount paid was KES "+amountStr+". Thank you");
		      lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
		      showDialog(Main.DIALOG_SERVICE_RESPONSE_ID);*/
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
	   
	   public void setDate(String date){
			currentDateField.setText(date);		
		}   
	   
	   private void increaseSelected(){
   		selected++;
   		if(selected == 2){
   			selected = 0;
   		}
   	}
   	private void dereaseSelected(){
   		selected--;
   		if(selected == -1){
   			selected = 1;
   		}
   	}
   	private void setSelectedBg(){
   		Resources res = getResources();
   		 switch (selected) {
   	     case 0:
   	    	cardText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
   	    	accountText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
   	         break;
   	     case 1:
   	    	cardText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
   	    	accountText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
   	    	          break;
   	         default:
   	             return;
   	     }
   	}
   	@Override
   	public boolean dispatchTouchEvent(MotionEvent ev) {
   	    if (gestureDetector != null) {
   	        gestureDetector.onTouchEvent(ev);
   	    }
   	    return super.dispatchTouchEvent(ev);
   	}
   	class MyGestureDetector implements GestureDetector.OnGestureListener {
			
			final float scale = getResources().getDisplayMetrics().density;
	private final int SWIPE_MIN_DISTANCE = (int) (120 * scale + 0.5f);
			  private final int SWIPE_MAX_OFF_PATH = (int) (250 * scale + 0.5f);
			  private final int SWIPE_THRESHOLD_VELOCITY = (int) (400 * scale + 0.5f);

			  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
					
			   if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
			    return false;
			   if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
			     && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			    flipper.setInAnimation(inFromRightAnimation());
			    flipper.setOutAnimation(outToLeftAnimation());
			    flipper.showNext();
			    increaseSelected();
			   } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
			     && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			    flipper.setInAnimation(inFromLeftAnimation());
			    flipper.setOutAnimation(outToRightAnimation());
			    flipper.showPrevious();
			    dereaseSelected();
			   }
			   setSelectedBg();
			   return true;
			  }

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			}
		
		protected Animation inFromRightAnimation() {
	   	 
	        Animation inFromRight = new TranslateAnimation(
	                        Animation.RELATIVE_TO_PARENT, +1.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f);
	        inFromRight.setDuration(500);
	        inFromRight.setInterpolator(new AccelerateInterpolator());
	        return inFromRight;
	}

	protected Animation outToLeftAnimation() {
	        Animation outtoLeft = new TranslateAnimation(
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, -1.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f);
	        outtoLeft.setDuration(500);
	        outtoLeft.setInterpolator(new AccelerateInterpolator());
	        return outtoLeft;
	}

	protected Animation inFromLeftAnimation() {
	        Animation inFromLeft = new TranslateAnimation(
	                        Animation.RELATIVE_TO_PARENT, -1.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f);
	        inFromLeft.setDuration(500);
	        inFromLeft.setInterpolator(new AccelerateInterpolator());
	        return inFromLeft;
	}

	protected Animation outToRightAnimation() {
	        Animation outtoRight = new TranslateAnimation(
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, +1.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f);
	        outtoRight.setDuration(500);
	        outtoRight.setInterpolator(new AccelerateInterpolator());
	        return outtoRight;
	}
	  }