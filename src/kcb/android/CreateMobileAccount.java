package kcb.android;


import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;


import org.json.JSONArray;
import org.json.JSONObject;







import kcb.android.R;
import lipuka.android.data.Constants;
import lipuka.android.model.ActivityDateListener;
import lipuka.android.model.MsisdnRegex;
import lipuka.android.model.Navigation;
import lipuka.android.model.responsehandlers.CreateAccountHandler;
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
import kcb.android.ForgotCredentials.OnSecretQuestionSelectedListener;
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

public class CreateMobileAccount extends Activity implements OnClickListener{
	   
	Button submit;
	EditText username;
	EditText password;
	EditText confirmPassword;
	EditText secretAnswer, fname, lname, mobileNo, email, postalAddress, postalCode, city;
	AutoCompleteTextView country;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedSecretQuestion, selectedCountry, selectedCountryValue;
	LipukaApplication lipukaApplication;
		LipukaListItem[] secretQuestionsArray;
		JSONArray destinationCountriesArray;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	setContentView(R.layout.create_mobile_account);	    
    
    TextView title = (TextView)findViewById(R.id.title);
    title.setText("Create Account");
username = (EditText) findViewById(R.id.username_field);
    
    password = (EditText) findViewById(R.id.password_field);
    confirmPassword = (EditText) findViewById(R.id.confirm_password_field);	        
    secretAnswer = (EditText) findViewById(R.id.secret_answer);
    fname = (EditText) findViewById(R.id.fname_field);
    lname = (EditText) findViewById(R.id.lname_field);
    mobileNo = (EditText) findViewById(R.id.mobile_no_field);
    email = (EditText) findViewById(R.id.email_field);
    postalAddress = (EditText) findViewById(R.id.postal_address_field);
    postalCode = (EditText) findViewById(R.id.postal_code_field);
    city = (EditText) findViewById(R.id.city_field);

      password.setInputType(InputType.TYPE_CLASS_TEXT | 
      		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
      confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | 
          		InputType.TYPE_TEXT_VARIATION_PASSWORD);
      
      secretAnswer.setInputType(InputType.TYPE_CLASS_TEXT | 
        		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
      
      mobileNo.setInputType(InputType.TYPE_CLASS_PHONE);
      email.setInputType(InputType.TYPE_CLASS_TEXT| 
      		InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
      postalCode.setInputType(InputType.TYPE_CLASS_NUMBER);
      
      country = (AutoCompleteTextView) findViewById(R.id.country_field);
      country.setHint("country name");
      String[] countriesArray = null; 
      try{       destinationCountriesArray = new JSONArray(lipukaApplication.loadAppData("countries", R.raw.all_countries));
      countriesArray = new String[destinationCountriesArray.length()];

          JSONObject currentSource;
            for(int i = 0; i < destinationCountriesArray.length(); i++){
            	currentSource = destinationCountriesArray.getJSONObject(i);
          		
            	countriesArray[i]=  currentSource.getString("name");   	
            }
            }catch(Exception ex){
      	    	Log.d(Main.TAG, "creating countries list error", ex);

        	}
            ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, countriesArray);

       final String[] countriesArray2 = countriesArray;
      	 country.setAdapter(countryAdapter);    
      country.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
	         int index = -1;

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
              long arg3) {
      	index = -1;
      	selectedCountry = country.getText().toString();
	        for(String currentCountry: countriesArray2){
      		index++;
	        	if(currentCountry.equals(selectedCountry)){
			    	Log.d(Main.TAG, "got selected country");

	        		break;
	        	}			
	        	
	        	}
	  try{
		  selectedCountryValue = destinationCountriesArray.getJSONObject(index).getString("value");

      }catch(Exception ex){
	    	Log.d(Main.TAG, "error", ex);

	}
      }
      });   

    submit = (Button) findViewById(R.id.submit);
    submit.setOnClickListener(this);
    
    final Spinner spinner = (Spinner) findViewById(R.id.secret_question_spinner);
    spinner.setOnItemSelectedListener(new OnSecretQuestionSelectedListener());
    
try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.secret_questions));
secretQuestionsArray = new LipukaListItem[sources.length()];
  JSONObject currentSource;
    for(int i = 0; i < sources.length(); i++){
    	currentSource = sources.getJSONObject(i);
    	LipukaListItem lipukaListItem = new LipukaListItem("", 
  currentSource.getString("name"), currentSource.getString("value"));
    	secretQuestionsArray[i]= lipukaListItem;   	
    }
    }catch(Exception ex){
    	Log.d(Main.TAG, "creating secret questions list error", ex);

	}
	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, secretQuestionsArray);
	spinner.setAdapter(adapter);

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
			lipukaApplication.setActivityState(CreateMobileAccount.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(CreateMobileAccount.class, false);
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
	    			String usernameStr = username.getText().toString();
	    			String passwordStr = password.getText().toString();	
	    			String confirmPasswordStr = confirmPassword.getText().toString();		    			
	    			String secretAnswerStr = secretAnswer.getText().toString();	
	    			String fnameStr = fname.getText().toString();	
	    			String lnameStr = lname.getText().toString();	
	    			String mobileNoStr = mobileNo.getText().toString();	
	    			String emailStr = email.getText().toString();	
	    			String postalAddressStr = postalAddress.getText().toString();	
	    			String postalCodeStr = postalCode.getText().toString();	
	    			String cityStr = city.getText().toString();	
	    				    			
	    				    				if(usernameStr == null || usernameStr.length() == 0){
	    					    				valid = false;
	    					    				errorBuffer.append("Username is missing\n");
	    					    			}	
	    				    				if(passwordStr == null || passwordStr.length() == 0){
	    					    				valid = false;
	    					    				errorBuffer.append("Password is missing\n");
	    					    			}
	    				    				if(confirmPasswordStr == null || confirmPasswordStr.length() == 0){
	    					    				valid = false;
	    					    				errorBuffer.append("Please confirm password\n");
	    					    			} 	
	    				    				if(passwordStr.length() > 0 && confirmPasswordStr.length() > 0){
	    				    					if(!passwordStr.equals(confirmPasswordStr)){
	    						    				valid = false;
	    						    				errorBuffer.append("Password values entered do not match\n");
	    						    			}
	    					    			}
	    				    				
	    				    				if(secretAnswerStr == null || secretAnswerStr.length() == 0){
	    					    				valid = false;
	    				errorBuffer.append("Secret answer is missing\n");
	    					    			}
	    				    				
	    				    				if(fnameStr == null || fnameStr.length() == 0){
	    				    					valid = false;
	    				    					errorBuffer.append("First name is missing\n");
	    				    				}	
	    				    				if(lnameStr == null || lnameStr.length() == 0){
	    				    					valid = false;
	    				    					errorBuffer.append("Last name is missing\n");
	    				    				}	
	    				    				if(mobileNoStr == null || mobileNoStr.length() == 0){
	    				    					valid = false;
	    				    					errorBuffer.append("Mobile number is missing\n");
	    				    				}	
	    				    				if(mobileNoStr != null && mobileNoStr.length() > 0){
	    				    					mobileNoStr = lipukaApplication.ensureCountryCode(mobileNoStr);
	    				    					if(mobileNoStr != null){
	    				    	if(!MsisdnRegex.isValidMsisdn(mobileNoStr)){
	    				    					valid = false;
	    				    					errorBuffer.append("Enter valid mobile number\n");
	    				    				}	
	    				    					}else{
	    				    						valid = false;
	    				    						errorBuffer.append("Enter valid mobile number\n");			
	    				    					}
	    				    				}
	    				    				if(emailStr == null || emailStr.length() == 0){
	    					    				valid = false;
	    				errorBuffer.append("Email address is missing\n");
	    					    			}
	    				    				if(postalAddressStr == null || postalAddressStr.length() == 0){
	    					    				valid = false;
	    				errorBuffer.append("Postal address is missing\n");
	    					    			}
	    				    				if(postalCodeStr == null || postalCodeStr.length() == 0){
	    					    				valid = false;
	    				errorBuffer.append("Postal code is missing\n");
	    					    			}
	    				    				if(cityStr == null || cityStr.length() == 0){
	    					    				valid = false;
	    				errorBuffer.append("City is missing\n");
	    					    			}
	    				    				
	    				    				if(selectedCountryValue == null || selectedCountryValue.length() == 0){
	    					    				valid = false;
	    					    				errorBuffer.append("Please select country from the list shown as you type in country name\n");
	    					    			}	password.setText("");
	    				    				confirmPassword.setText("");
	    				       	    		secretAnswer.setText("");
	    			   			if(valid){
	    				    			    				
	    				    			    				 Bundle extras = getIntent().getExtras(); 

	    				    			    			        
	    				    			    			        String cardNumberStr = (String)extras.get(Constants.CURRENT_CARD_NUMBER);
	    				    			    			        String cardHolderNameStr = (String)extras.get(Constants.CURRENT_CARD_HOLDERS_NAME);
	    				    			    			        String securityCodeStr = (String)extras.get(Constants.CURRENT_CARD_CVV_CODE);
	    				    			    			        String expiry_date = (String)extras.get(Constants.CURRENT_CARD_EXPIRY_DATE);
	    				    			    			       
	    				    			    				lipukaApplication.putPayload("card_no", cardNumberStr);
	    				    			    				lipukaApplication.putPayload("card_name", cardHolderNameStr);
	    				    			    				lipukaApplication.putPayload("cvv_code", securityCodeStr);
	    				    			    				lipukaApplication.putPayload("expiry_date", expiry_date);    				    			    				
	    				    			    				lipukaApplication.putPayload("username", usernameStr);
	    				    			    				lipukaApplication.putPayload("password", passwordStr);
	    				    			    				lipukaApplication.putPayload("secret_question_id", selectedSecretQuestion);
	    				    			    				lipukaApplication.putPayload("secret_question_answer", secretAnswerStr);
	    				    			    				lipukaApplication.putPayload("first_name", fnameStr);
	    				    			    				lipukaApplication.putPayload("last_name", lnameStr);
	    				    			    				lipukaApplication.putPayload("msisdn", mobileNoStr);
	    				    			    				lipukaApplication.putPayload("email_address", emailStr);
	    				    			    				lipukaApplication.putPayload("postal_address", postalAddressStr);
	    				    			    				lipukaApplication.putPayload("postal_code", postalCodeStr);
	    				    			    				lipukaApplication.putPayload("city", cityStr);
	    				    			    				lipukaApplication.putPayload("country_id", selectedCountryValue);
	    				    			    				
	    				    			    				
	    				    			    				lipukaApplication.consumeService("71", new CreateAccountHandler(lipukaApplication, this));
	    				    			    					
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
	    		public class OnSecretQuestionSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSecretQuestion = secretQuestionsArray[pos].getValue();
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
	  }