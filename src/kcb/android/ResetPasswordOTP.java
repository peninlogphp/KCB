package kcb.android;

import java.util.List;

import kcb.android.R;


import lipuka.android.model.MsisdnRegex;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.adapter.ContactsAutoCompleteCursorAdapter;
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
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResetPasswordOTP extends Activity implements OnClickListener{
	   
	Button submit;
	EditText oneTimePassword;
	EditText password;
	EditText confirmPassword;
	CheckBox ownNumber;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedNetwork;
	String selectedAmount;

	String idTypeSelected, idTypeID, idTypePassport;

	LipukaApplication lipukaApplication;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.reset_password_otp);	    
	        
	        TextView title = (TextView)findViewById(R.id.title);
	        title.setText("Reset Password");
	        oneTimePassword = (EditText) findViewById(R.id.one_time_password_field);
	        
	        password = (EditText) findViewById(R.id.password_field);
	        confirmPassword = (EditText) findViewById(R.id.confirm_password_field);	        
     
	        oneTimePassword.setInputType(InputType.TYPE_CLASS_TEXT | 
	          		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
	        password.setInputType(InputType.TYPE_CLASS_TEXT | 
	          		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
	          confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | 
		          		InputType.TYPE_TEXT_VARIATION_PASSWORD);		

	        submit = (Button) findViewById(R.id.submit);
	        submit.setOnClickListener(this);
	        
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
		    	myWebView.loadUrl("file:///android_asset/withdrawcash.html");
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
	Log.d(Main.TAG, "creating topup error", e);

} 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(ResetPasswordOTP.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(ResetPasswordOTP.class, false);
	    }
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       // MenuInflater inflater = getMenuInflater();
	        //inflater.inflate(R.menu.help_menu, menu);
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
	    			String oneTimePasswordStr = oneTimePassword.getText().toString();
String passwordStr = password.getText().toString();	
String confirmPasswordStr = confirmPassword.getText().toString();		    			
	    			
	    				if(oneTimePasswordStr == null || oneTimePasswordStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("One time password is missing\n");
		    			}	
	    				if(passwordStr == null || passwordStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("New password is missing\n");
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
	    				oneTimePassword.setText("");
	    				password.setText("");
	    				confirmPassword.setText("");

   			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
    			    				
    			    	   				payloadBuffer.append(oneTimePasswordStr+"|");
    			    	   				payloadBuffer.append(passwordStr+"|");
    			    	   				
	    			    	   			Navigation nav = new Navigation();	
	    			    	   			nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.setCurrentDialogTitle("Response");
	    			    	        	lipukaApplication.setCurrentDialogMsg("Your request was successful. Click \"OK\" to sign in with your username and new password. Thank you");
	    			    	            lipukaApplication.showDialog(Main.DIALOG_MSG_ID);
	    			    			
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}

	    		}else if(arg0.getId() ==  R.id.help){
					//help.setVisibility(View.VISIBLE);
			        //help.startAnimation(LipukaAnim.inFromRightAnimation());
	    		}else if(arg0.getId() ==  R.id.home_button){
				 /*Intent i = new Intent(this, PaymaxHome.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);*/
	    		}else if (closeHelp == arg0){
	    			help.startAnimation(LipukaAnim.outToRightAnimation());
	    	    	help.setVisibility(View.GONE);
	    	    	}
	    			}
	    		
	    		protected Dialog onCreateDialog(int id) {
	    	        Dialog dialog = null;
	    	        switch(id) {
	    	        case Main.DIALOG_MSG_ID:
	    	        	CustomDialog cd = new CustomDialog(this);
	    	        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	cd.setActivityShowing(CustomDialog.RESET_PASSWORD_OTP);
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
	    	        	cd.setActivityShowing(CustomDialog.RESET_PASSWORD_OTP);
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
	    	    }