package kcb.android;


import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import kcb.android.AccountStmt.EcobankDateFieldListener;
import kcb.android.EcobankHome.ConfirmationDialog;
import kcb.android.FundsTransfer.OnSourceAccountSelectedListener;
import kcb.android.TransferFunds.MyGestureDetector;


import org.json.JSONArray;
import org.json.JSONObject;






import kcb.android.R;
import lipuka.android.model.ActivityDateListener;
import lipuka.android.model.MsisdnRegex;
import lipuka.android.model.Navigation;
import lipuka.android.model.responsehandlers.ConsumeServiceHandler;
import lipuka.android.model.responsehandlers.SignUpHandler;
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
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
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

public class ForgotCredentials extends Activity implements OnClickListener, ResponseActivity{
	   
	Button submit, submitPassword;
	EditText cardNumber;
EditText cardNumberPassword;
	EditText dateOfBirth, dateOfBirthPassword;
	EditText securityCode;
	EditText idNumber, idNumberPassword;
	EditText secretAnswer;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedSecretQuestion;

	LipukaApplication lipukaApplication;

	LipukaListItem[] secretQuestionsArray;
	String amountStr, destinationStr;

	ActivityDateListener activityDateListener;
	EditText currentDateField;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.forgot_credentials);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Forgot Credentials");
	        cardNumber = (EditText) findViewById(R.id.card_number_field);
	        idNumber = (EditText) findViewById(R.id.id_number_field);	

cardNumberPassword = (EditText) findViewById(R.id.card_number_password_field);
		        securityCode = (EditText) findViewById(R.id.security_code_field);     
		        idNumberPassword = (EditText) findViewById(R.id.id_number_field_password);
		        secretAnswer = (EditText) findViewById(R.id.secret_answer);
       
		        cardNumber.setInputType(InputType.TYPE_CLASS_NUMBER);		
		        idNumber.setInputType(InputType.TYPE_CLASS_NUMBER);	
		        
		        cardNumberPassword.setInputType(InputType.TYPE_CLASS_NUMBER);		
		        securityCode.setInputType(InputType.TYPE_CLASS_NUMBER);
		        idNumberPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
		        
		        securityCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
		        securityCode.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		        InputFilter[] filterArray = new InputFilter[1];
		     	   filterArray[0] = new InputFilter.LengthFilter(20);
		     	  securityCode.setFilters(filterArray);
		     	idNumber.setFilters(filterArray);
		     	idNumberPassword.setFilters(filterArray);
		     	   
		     	  secretAnswer.setInputType(InputType.TYPE_CLASS_TEXT | 
		          		InputType.TYPE_TEXT_VARIATION_PASSWORD);
		     	  
		     	  submit = (Button) findViewById(R.id.forgot_username_submit);
	        submit.setOnClickListener(this);
	        submitPassword = (Button) findViewById(R.id.forgot_password_submit);
	        submitPassword.setOnClickListener(this);
	        
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
	   	
	        Button showOrHideForgotUsername = (Button) findViewById(R.id.show_or_hide_forgot_username);
	        showOrHideForgotUsername.setOnClickListener(this);
	        Button showOrHideForgotPassword = (Button) findViewById(R.id.show_or_hide_forgot_password);
	        showOrHideForgotPassword.setOnClickListener(this);
     
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
	    	Log.d(Main.TAG, "creating forgot credentials error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(ForgotCredentials.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(ForgotCredentials.class, false);
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
	    		    lipukaApplication.clearNavigationStack();
	
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String cardNumberStr = cardNumber.getText().toString();
	    			String idNumberStr = idNumber.getText().toString();	

    				if(cardNumberStr.length() < 16 || cardNumberStr.length() > 16){
	    				valid = false;
errorBuffer.append("Please enter a card number with 16 digits\n");
	    			}
       				if(idNumberStr.length() == 0){
	    				valid = false;
errorBuffer.append("ID number is missing\n");
	    			}	
   		        	
	    			    			if(valid){
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.putPayload("account_no", cardNumberStr);
	    			    				lipukaApplication.putPayload("id_number", idNumberStr);
	    			    				lipukaApplication.consumeService("3", new ConsumeServiceHandler(lipukaApplication, this));
	    			    					    			    				
	    			    	}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	
			
	    			    			
	    		}else if (submitPassword == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String cardNumberStr = cardNumberPassword.getText().toString();	
	    			String securityCodeStr = securityCode.getText().toString();	
	    			String idNumberStr = idNumberPassword.getText().toString();	
	    			String secretAnswerStr = secretAnswer.getText().toString();	

	    			if(cardNumberStr.length() < 16 || cardNumberStr.length() > 16){
	    				valid = false;
errorBuffer.append("Please enter a card number with 16 digits\n");
	    			}
	    			if(securityCodeStr == null || securityCodeStr.length() == 0){
	    				valid = false;
errorBuffer.append("Security code is missing\n");
	    			}
	    			if(idNumberStr.length() == 0){
	    				valid = false;
errorBuffer.append("ID number is missing\n");
	    			}	
       	    		if(secretAnswerStr == null || secretAnswerStr.length() == 0){
	    				valid = false;
errorBuffer.append("Secret answer is missing\n");
	    			}
       	    		securityCode.setText("");
	secretAnswer.setText("");

       	    		if(valid){
	    			    		
	    				lipukaApplication.putPayload("account_no", cardNumberStr);
	    			    				lipukaApplication.putPayload("cvv_number", securityCodeStr);
	    			    				lipukaApplication.putPayload("id_number", idNumberStr);
	    			    				lipukaApplication.putPayload("secret_question_id", selectedSecretQuestion);
	    			    				lipukaApplication.putPayload("secret_question_answer", secretAnswerStr);
	    			    				lipukaApplication.consumeService("65", new ConsumeServiceHandler(lipukaApplication, this));
	    			    				
	    			    				/*lipukaApplication.setCurrentDialogTitle("Response");
	    			    	        	lipukaApplication.setCurrentDialogMsg("Your request was successful. A one time password will be sent to your mobile number. Please change it immediately under \"My Account\" -> \"Edit Profile\". Thank you");
	    			    	        	lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
	    			    			      showDialog(Main.DIALOG_SERVICE_RESPONSE_ID);*/
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_forgot_username){
	    	    		LinearLayout  forgotUsername = (LinearLayout) findViewById(R.id.forgot_username);
	    	    		Drawable img = null;
		if(forgotUsername.isShown()){
			//forgotUsername.setVisibility(View.GONE);
			ExpandAnimation.collapse(forgotUsername);
	    	    			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//forgotUsername.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(forgotUsername);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  forgotPassword = (LinearLayout) findViewById(R.id.forgot_password);
		    	    		forgotPassword.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideForgotPassword = (Button) findViewById(R.id.show_or_hide_forgot_password);
	    	    	        showOrHideForgotPassword.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_forgot_password){
		    	    		LinearLayout  forgotPassword = (LinearLayout) findViewById(R.id.forgot_password);
		    	    		Drawable img = null;
			if(forgotPassword.isShown()){
				//forgotPassword.setVisibility(View.GONE);
				ExpandAnimation.collapse(forgotPassword);
				img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//forgotPassword.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(forgotPassword);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  forgotUsername = (LinearLayout) findViewById(R.id.forgot_username);
			    	    		forgotUsername.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideForgotUsername = (Button) findViewById(R.id.show_or_hide_forgot_username);
		    	    	        showOrHideForgotUsername.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
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
			lipukaApplication.setCurrentDialogTitle("Response");
		      lipukaApplication.setCurrentDialogMsg("Dear Alice, you have successfully paid "+destinationStr+". Amount paid was KES "+amountStr+". Thank you");
		      lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
		      showDialog(Main.DIALOG_SERVICE_RESPONSE_ID);
	   }
	  
	    	    }