package kcb.android;


import java.util.List;

import kcb.android.EcobankHome.ConfirmationDialog;
import kcb.android.TransferFunds.MyGestureDetector;


import org.json.JSONArray;
import org.json.JSONObject;






import kcb.android.R;
import lipuka.android.model.MsisdnRegex;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
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

public class AgencyBanking extends Activity implements OnClickListener, ResponseActivity{
	   
	Button submit, submitWithdraw;
	EditText amount, amountWithdraw;
	EditText depositorMobileNo, recipientMobileNo;
	EditText destinationAccountNo, agentCode;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedSavedBill, selectedMerchant;

	LipukaApplication lipukaApplication;

	LipukaListItem[] savedBillsArray, merchantsArray;
	String amountStr, destinationStr;
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.agency_banking);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Agency Transactions");
	        amount = (EditText) findViewById(R.id.deposit_cash_amount_field);
	        amountWithdraw = (EditText) findViewById(R.id.withdraw_cash_amount_field);
	        
	        depositorMobileNo = (EditText) findViewById(R.id.depositor_mobile_no_field);
	        destinationAccountNo = (EditText) findViewById(R.id.destination_account_field);
 agentCode = (EditText) findViewById(R.id.agent_code_field);

 recipientMobileNo = (EditText) findViewById(R.id.recipient_mobile_no_field);

	        
	       // accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountWithdraw.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	     
	        destinationAccountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        depositorMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);	
	        recipientMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);		
	        agentCode.setInputType(InputType.TYPE_CLASS_NUMBER);

	   	submit = (Button) findViewById(R.id.deposit_cash_submit);
	        submit.setOnClickListener(this);
	        submitWithdraw = (Button) findViewById(R.id.withdraw_cash_submit);
	        submitWithdraw.setOnClickListener(this);
	        
	        Button showOrHideDepositCash = (Button) findViewById(R.id.show_or_hide_deposit_cash);
	        showOrHideDepositCash.setOnClickListener(this);
	        Button showOrHideWithdrawCash = (Button) findViewById(R.id.show_or_hide_withdraw_cash);
	        showOrHideWithdrawCash.setOnClickListener(this);
     
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
	    	Log.d(Main.TAG, "creating agency banking error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(AgencyBanking.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(AgencyBanking.class, false);
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
	    			String depositorMobileNoStr = depositorMobileNo.getText().toString();	
	    			String recipientMobileNoStr = recipientMobileNo.getText().toString();	
	    			String destinationAccountNoStr = destinationAccountNo.getText().toString();	
	    			String amountStr = amount.getText().toString();	

	       			if(depositorMobileNoStr == null || depositorMobileNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Depositor's mobile number is missing\n");
	    			}	
	    			if(depositorMobileNoStr != null && depositorMobileNoStr.length() > 0){
	    				depositorMobileNoStr = lipukaApplication.ensureCountryCode(depositorMobileNoStr);
	    				if(depositorMobileNoStr != null){
	    if(!MsisdnRegex.isValidMsisdn(depositorMobileNoStr)){
	    				valid = false;
	    				errorBuffer.append("Enter valid depositor's mobile number\n");
	    			}	
	    				}else{
	    					valid = false;
	    					errorBuffer.append("Enter valid depositor's mobile number\n");			
	    				}
	    			}
	    			
	    			if(recipientMobileNoStr == null || recipientMobileNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Recipient's mobile number is missing\n");
	    			}	
	    			if(recipientMobileNoStr != null && recipientMobileNoStr.length() > 0){
	    				recipientMobileNoStr = lipukaApplication.ensureCountryCode(recipientMobileNoStr);
	    				if(recipientMobileNoStr != null){
	    if(!MsisdnRegex.isValidMsisdn(recipientMobileNoStr)){
	    				valid = false;
	    				errorBuffer.append("Enter valid recipient's mobile number\n");
	    			}	
	    				}else{
	    					valid = false;
	    					errorBuffer.append("Enter valid recipient's mobile number\n");			
	    				}
	    			}
	    			if(destinationAccountNoStr == null || destinationAccountNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Destination account number is missing\n");
	    			}
	    			if(amountStr == null || amountStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Amount is missing\n");
	    			}
	    			    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			payloadBuffer.append(amountStr+"|");	    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				/*lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to deposit Ksh. "+amountStr+" into account "+destinationAccountNoStr+". Press \"OK\" to deposit now or \"Cancel\" to edit deposit cash details.");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/
	    			    				showResponse();
	    			    			this.amountStr = amountStr;
destinationStr  = "Dear Alice, you have successfully desposited Ksh. "+amountStr+" into account "+destinationAccountNoStr+". Thank you.";
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitWithdraw == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String agentCodeStr = agentCode.getText().toString();	
	    			String amountStr = amountWithdraw.getText().toString();
	    			
	    			if(agentCodeStr == null || agentCodeStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Agent code is missing\n");
	    			}
	    			
			    			if(amountStr == null || amountStr.length() == 0){
			    				valid = false;
			    				errorBuffer.append("Amount is missing\n");
			    			}
	    						if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
    			    	   				payloadBuffer.append("Other|");
    			    	   				 				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				/*lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to withdraw Ksh. "+amountStr+
	    			    			    		  " from your current account, agent code is "+agentCodeStr+". Press \"OK\" to withdraw now or \"Cancel\" to edit withdraw cash details.");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/	
	    			    				showResponse();
	    			    			this.amountStr = amountStr;
destinationStr  = "Dear Alice, you have successfully withdrawn Ksh. "+amountStr+" from agent "+agentCodeStr+". Thank you.";
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_deposit_cash){
	    	    		LinearLayout  depositCash = (LinearLayout) findViewById(R.id.deposit_cash);
	    	    		Drawable img = null;
		if(depositCash.isShown()){
			//savedBills.setVisibility(View.GONE);
			ExpandAnimation.collapse(depositCash);
  			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//savedBills.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(depositCash);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  withdrawCash = (LinearLayout) findViewById(R.id.withdraw_cash);
		    	    		withdrawCash.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideWithdrawCash = (Button) findViewById(R.id.show_or_hide_withdraw_cash);
	    	    	        showOrHideWithdrawCash.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    	 		
	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_withdraw_cash){
		    	    		LinearLayout  withdrawCash = (LinearLayout) findViewById(R.id.withdraw_cash);
		    	    		Drawable img = null;
			if(withdrawCash.isShown()){
				//merchants.setVisibility(View.GONE);
				ExpandAnimation.collapse(withdrawCash);
				img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//merchants.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(withdrawCash);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  depositCash = (LinearLayout) findViewById(R.id.deposit_cash);
			    	    		depositCash.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideDepositCash = (Button) findViewById(R.id.show_or_hide_deposit_cash);
		    	    	        showOrHideDepositCash.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

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
	    		public class OnSavedBillSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSavedBill = savedBillsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnMerchantSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedMerchant = merchantsArray[pos].getText();
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
		      /*lipukaApplication.setCurrentDialogMsg(destinationStr);
		      lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
		      showDialog(Main.DIALOG_SERVICE_RESPONSE_ID);*/
		      lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
	           showDialog(Main.DIALOG_MSG_ID);
	   }
	    	    }