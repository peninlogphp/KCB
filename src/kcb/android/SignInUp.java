package kcb.android;


import java.util.List;

import kcb.android.TransferFunds.OnBankSelectedListener;


import org.json.JSONArray;
import org.json.JSONObject;






import kcb.android.R;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
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
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class SignInUp extends Activity implements OnClickListener{
	   
	Button signIn, signUp;
	EditText username;
	EditText password;
	EditText cardNumber;
	EditText authenticationCode;
	EditText name;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedEnrollment;
	String selectedMerchant, selectedAlias;

	String idTypeSelected, idTypeID, idTypePassport;

	LipukaApplication lipukaApplication;
	String selectedCurrency;
	LipukaListItem[] itemsArray;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();

	   try{
		   setContentView(R.layout.sign_in_up);
        
username = (EditText) findViewById(R.id.username_field);
	        
	        password = (EditText) findViewById(R.id.password_field);
	       
	          password.setInputType(InputType.TYPE_CLASS_TEXT | 
	          		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
	          
	          TextView forgotPasswordLink = (TextView) findViewById(R.id.forgot_password_link);
     SpannableString mySpannableString = new SpannableString("Forgot username or password?");
 			 mySpannableString.setSpan(new UnderlineSpan(), 0, mySpannableString.length(), 0);
 			forgotPasswordLink.setText(mySpannableString);
 			forgotPasswordLink.setOnClickListener(this);
	 
 			 cardNumber = (EditText) findViewById(R.id.card_number_field);
		        
		        authenticationCode = (EditText) findViewById(R.id.authentication_code_field);
		        
		        cardNumber.setInputType(InputType.TYPE_CLASS_NUMBER);		
	     
		          authenticationCode.setInputType(InputType.TYPE_CLASS_TEXT | 
		          		InputType.TYPE_TEXT_VARIATION_PASSWORD);		      

	          
	        signIn = (Button) findViewById(R.id.sign_in);
	        signIn.setOnClickListener(this);
	        signUp = (Button) findViewById(R.id.sign_up);
	        signUp.setOnClickListener(this);
	        
	        Button showOrHideSignIn = (Button) findViewById(R.id.show_or_hide_sign_in);
	        showOrHideSignIn.setOnClickListener(this);
	        Button showOrHideSignUp = (Button) findViewById(R.id.show_or_hide_sign_up);
	        showOrHideSignUp.setOnClickListener(this);	      
	   	
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
			Log.d(Main.TAG, "creating SignInUp error", e);

	} 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(SignInUp.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(SignInUp.class, false);
	    }
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       // MenuInflater inflater = getMenuInflater();
	       // inflater.inflate(R.menu.help_menu, menu);
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
	    		if (signIn == arg0){
	    			     lipukaApplication.clearNavigationStack();
	
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String usernameStr = username.getText().toString();	
	    			String passwordStr = password.getText().toString();	

    				if(usernameStr == null || usernameStr.length() == 0){
	    				valid = false;
errorBuffer.append("Username is missing\n");
	    			}
       				if(passwordStr == null || passwordStr.length() == 0){
	    				valid = false;
errorBuffer.append("Password is missing\n");
	    			}	    			
    				password.setText("");
	    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
    			    	   				payloadBuffer.append(usernameStr+"|");	    				
	    			    				payloadBuffer.append(passwordStr+"|");
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				
	    		Intent i = new Intent(SignInUp.this, PaymaxHome.class);
	    				    			  startActivity(i);
	    				    		    	//lipukaApplication.putPref("signout", "signout");

	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (signUp == arg0){
	    			
	   		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String cardNumberStr = cardNumber.getText().toString();
	    			String authenticationCodeStr = authenticationCode.getText().toString();	

    				if(cardNumberStr.length() < 16 || cardNumberStr.length() > 16){
	    				valid = false;
errorBuffer.append("Please enter a card number with 16 digits\n");
	    			}
			    			if(authenticationCodeStr == null || authenticationCodeStr.length() == 0){
			    				valid = false;
			    				errorBuffer.append("Authentication code is missing\n");
			    			}
		    				authenticationCode.setText("");
    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();

    			    	   				payloadBuffer.append(cardNumberStr+"|");
	    			    				payloadBuffer.append(authenticationCodeStr+"|");		
    			    	   				    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				Intent i = new Intent(SignInUp.this, CreateAccount.class);
	    				    			  startActivity(i);

	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if(arg0.getId() ==  R.id.forgot_password_link){
	    			Intent i = new Intent(this, ForgottenUsernamePassword.class);
					startActivity(i);
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_sign_in){
	    	    		FrameLayout  signInLayout = (FrameLayout) findViewById(R.id.sign_in_layout);
	    	    		Drawable img = null;
		if(signInLayout.isShown()){
			signInLayout.setVisibility(View.GONE);
	    	    			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			signInLayout.setVisibility(View.VISIBLE);
	    	    			img = getResources().getDrawable( R.drawable.hide );
	    	        		FrameLayout  signUpLayout = (FrameLayout) findViewById(R.id.sign_up_layout);
	    	        		signUpLayout.setVisibility(View.GONE);
	    	        		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideSignUp = (Button) findViewById(R.id.show_or_hide_sign_up);
	    	    	        showOrHideSignUp.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    		
	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_sign_up){
		    	    		FrameLayout  signUpLayout = (FrameLayout) findViewById(R.id.sign_up_layout);
		    	    		Drawable img = null;
			if(signUpLayout.isShown()){
				signUpLayout.setVisibility(View.GONE);
		    	    			img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			signUpLayout.setVisibility(View.VISIBLE);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	        		FrameLayout  signInLayout = (FrameLayout) findViewById(R.id.sign_in_layout);
		    	        		signInLayout.setVisibility(View.GONE);
		    	        		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideSignIn = (Button) findViewById(R.id.show_or_hide_sign_in);
		    	    	        showOrHideSignIn.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		    	    		
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
		    	
	    	    }