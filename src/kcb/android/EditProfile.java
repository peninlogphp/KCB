package kcb.android;


import java.util.List;

import kcb.android.FundsTransfer.ConfirmationDialog;





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
import android.view.Window;
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

public class EditProfile extends Activity implements OnClickListener{
	   
	Button submit;
	EditText username;
	EditText currentPassword;
	EditText newPassword;
	EditText confirmPassword;
	EditText emailAddress;
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
	        setContentView(R.layout.edit_profile);	    
	        
	        TextView title = (TextView)findViewById(R.id.title);
	        title.setText("Edit Profile");
	        
	        currentPassword = (EditText) findViewById(R.id.current_password_field);
	        newPassword = (EditText) findViewById(R.id.new_password_field);
	        confirmPassword = (EditText) findViewById(R.id.confirm_password_field);
	        emailAddress = (EditText) findViewById(R.id.email_address_field);
	        //emailAddress.setText("mali@mail.com");
     
	        currentPassword.setInputType(InputType.TYPE_CLASS_TEXT | 
	          		InputType.TYPE_TEXT_VARIATION_PASSWORD);
	        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | 
	          		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
	          confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | 
		          		InputType.TYPE_TEXT_VARIATION_PASSWORD);
	          emailAddress.setInputType(InputType.TYPE_CLASS_TEXT | 
		          		InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);		

	        submit = (Button) findViewById(R.id.submit);
	        submit.setOnClickListener(this);
	        
	        Button cancel = (Button) findViewById(R.id.cancel);
	        cancel.setOnClickListener(this);
	        
	        Button helpButton = (Button)findViewById(R.id.help);
		    helpButton.setOnClickListener(this);
		    Button homeButton = (Button)findViewById(R.id.home_button);
		    homeButton.setOnClickListener(this);
		   
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
			lipukaApplication.setActivityState(EditProfile.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(EditProfile.class, false);
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
	    		if (submit == arg0){
	    			
	    			   lipukaApplication.clearNavigationStack();
	    			lipukaApplication.setServiceID("35");

	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String currentPasswordStr = currentPassword.getText().toString();
	    			String newPasswordStr = newPassword.getText().toString();	
	    			String confirmPasswordStr = confirmPassword.getText().toString();		    			
	    			String emailAddressStr = emailAddress.getText().toString();		    			
	    				    			
	    				    				if(currentPasswordStr == null || currentPasswordStr.length() == 0){
	    					    				valid = false;
	    					    				errorBuffer.append("Current Password is missing\n");
	    					    			}	
	    				    				if(newPasswordStr.length() > 0){
	    				    					if(confirmPasswordStr == null || confirmPasswordStr.length() == 0){
		    					    				valid = false;
		    					    				errorBuffer.append("Please confirm password\n");
		    					    			} 
	    					    			}
	    				    					
	    				    				if(newPasswordStr.length() > 0 && confirmPasswordStr.length() > 0){
	    				    					if(!newPasswordStr.equals(confirmPasswordStr)){
	    						    				valid = false;
	    						    				errorBuffer.append("New password values entered do not match\n");
	    						    			}
	    					    			}
	    				    				if(emailAddressStr == null || emailAddressStr.length() == 0){
	    					    				valid = false;
	    					    				errorBuffer.append("Email address is missing\n");
	    					    			}	    				    				
	    				    				currentPassword.setText("");
	    				    				newPassword.setText("");
	    				    				confirmPassword.setText("");

	    			   			if(valid){
	    				    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			    				
	    			    			    	   				payloadBuffer.append(currentPasswordStr+"|");
	    			    			    	   				payloadBuffer.append(newPasswordStr+"|");
	    			    			    	   				
	    				    			    	   			Navigation nav = new Navigation();	
	    				    			    	   			nav.setPayload(payloadBuffer.toString());
	    				    			    				lipukaApplication.pushNavigationStack(nav);
	    				    			    				lipukaApplication.setPin(null);
	    				    			    				lipukaApplication.setCurrentDialogTitle("Confirmation");
	    				    			    	        	lipukaApplication.setCurrentDialogMsg("Please confirm that you would like to save changes by clicking \"OK\"");
	    				    			    	            lipukaApplication.showDialog(Main.DIALOG_CONFIRM_ID);
	    				    			    			
	    				    			    			}else{
	    				    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    				    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    				    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    				    			    			}
	    		}else if(arg0.getId() ==  R.id.cancel){
					finish();
	    		}else if(arg0.getId() ==  R.id.help){
					//help.setVisibility(View.VISIBLE);
			       // help.startAnimation(LipukaAnim.inFromRightAnimation());
	    		}else if(arg0.getId() ==  R.id.home_button){
				 Intent i = new Intent(this, PaymaxHome.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
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
	    	        	cd.setActivityShowing(CustomDialog.EDIT_PROFILE);
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
	    	        	cd.setActivityShowing(CustomDialog.EDIT_PROFILE);
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
		    			lipukaApplication.setCurrentDialogTitle("Response");
	    	        	lipukaApplication.setCurrentDialogMsg("Dear Mali, you have successfully edited details of your card 6464646468746632. Thank you");
	    	            lipukaApplication.showDialog(Main.DIALOG_MSG_ID);
	    			
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
	    	    }