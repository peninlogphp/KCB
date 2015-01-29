package kcb.android;


import java.util.List;

import kcb.android.CreditCardPayments.OnSourceAccountSelectedListener;
import kcb.android.EcobankHome.ConfirmationDialog;
import kcb.android.TransferFunds.MyGestureDetector;


import org.json.JSONArray;
import org.json.JSONObject;






import kcb.android.R;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.SignInDialog;
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

public class InvestmentsNcredit extends Activity implements OnClickListener, ResponseActivity,
ShowSignOutActivity, PartialSignInActivity{
	   
	Button submitDeposit, submitApplyForCredit;
	EditText amount, emailAddress;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedSourceAccount, selectedSavingsAccount, selectedPeriod;

	LipukaApplication lipukaApplication;

	LipukaListItem[] sourceAccountsArray, savingsAccountsArray, periodsArray;
	String amountStr, destinationStr;
	byte action;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.investments_n_credit);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Investments");
	        amount = (EditText) findViewById(R.id.deposit_amount_field);
	        emailAddress = (EditText) findViewById(R.id.email_address_field);
	        
	       // accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        emailAddress.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );			        
 	        
	       
	        final Spinner spinner = (Spinner) findViewById(R.id.source_account_spinner);
	        spinner.setOnItemSelectedListener(new OnSourceAccountSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSourceAcounts());
	         sourceAccountsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	sourceAccountsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating source accounts list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceAccountsArray);
	   	spinner.setAdapter(adapter);
	   	
	   	final Spinner spinnerDestination = (Spinner) findViewById(R.id.destination_account_spinner);
	        spinnerDestination.setOnItemSelectedListener(new OnSavingsAccountSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.savings_accounts));
	         savingsAccountsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	savingsAccountsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapterDestination = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, savingsAccountsArray);
	   	spinnerDestination.setAdapter(adapterDestination);
	   	
	
	       final Spinner periodSpinner = (Spinner) findViewById(R.id.period_spinner);
	       periodSpinner.setOnItemSelectedListener(new OnPeriodSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.fixed_deposit_periods));
	         periodsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	periodsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter periodAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, periodsArray);
	   	periodSpinner.setAdapter(periodAdapter);	   
  	
	   	submitDeposit = (Button) findViewById(R.id.deposit_submit);
	        submitDeposit.setOnClickListener(this);
	        submitApplyForCredit = (Button) findViewById(R.id.apply_for_credit_submit);
	        submitApplyForCredit.setOnClickListener(this);
	        
	        Button showOrHideProductInfo = (Button) findViewById(R.id.show_or_hide_product_info);
	        showOrHideProductInfo.setOnClickListener(this);
	        Button showOrHideDeposits = (Button) findViewById(R.id.show_or_hide_deposits);
	        showOrHideDeposits.setOnClickListener(this);
	        Button showOrHideApplyForCredit = (Button) findViewById(R.id.show_or_hide_apply_for_credit);
	        showOrHideApplyForCredit.setOnClickListener(this);
     
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
	    	Log.d(Main.TAG, "creating investments n credit error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(InvestmentsNcredit.class, true);
			
			Button signOutButton = (Button)findViewById(R.id.sign_out);

			if(lipukaApplication.getProfileID() == 0){
				 signOutButton.setVisibility(View.GONE);
				    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
				    dividerTwo.setVisibility(View.GONE);
				}else{
					 signOutButton.setVisibility(View.VISIBLE);
					    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
					    dividerTwo.setVisibility(View.VISIBLE);			
				}
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(InvestmentsNcredit.class, false);
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
	    		if (submitDeposit == arg0){
	    		    lipukaApplication.clearNavigationStack();
	
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String amountStr = amount.getText().toString();	
	    			
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
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to deposit KES "+amountStr+" into your "+selectedSavingsAccount+". Press \"OK\" to deposit now or \"Cancel\" to edit deposit details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/	
	    			    				showResponse();
	    			    			this.amountStr = amountStr;
destinationStr  = "Dear Alice, you have successfully deposited KES "+amountStr+" into your "+selectedSavingsAccount+". Thank you";
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitApplyForCredit == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String emailAddressStr = emailAddress.getText().toString();
	    			
	    			if(emailAddressStr == null || emailAddressStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Please enter email address\n");
	    			}
	    			
	    			/*if(valid){
	    				if(!Regex.isEmailAddress(emailAddressStr)){
	        				valid = false;
	        				errorBuffer.append("Please enter a valid email address\n");
	        			}	
	    			}	*/	
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_product_info){
	    	    		lipukaApplication.setCurrentDialogTitle("Response");
	    	              lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
	    	              showDialog(Main.DIALOG_MSG_ID);
	    	       /*       LinearLayout  productInfo = (LinearLayout) findViewById(R.id.investments_n_credit_product_info);
	    	    		Drawable img = null;
		if(productInfo.isShown()){
			//productInfo.setVisibility(View.GONE);
	    				ExpandAnimation.collapse(productInfo);
    					img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//productInfo.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(productInfo);
							img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  deposits = (LinearLayout) findViewById(R.id.investments_n_credit_deposits);
		    	    		deposits.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideDeposits = (Button) findViewById(R.id.show_or_hide_deposits);
	    	    	        showOrHideDeposits.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    	 		LinearLayout  applyForCredit = (LinearLayout) findViewById(R.id.investments_n_credit_apply_for_credit);
		    	    		applyForCredit.setVisibility(View.GONE);
	    	    	        Button showOrHideApplyForCredit = (Button) findViewById(R.id.show_or_hide_apply_for_credit);
	    	    	        showOrHideApplyForCredit.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );*/

		    	    	}else if (arg0.getId() == R.id.show_or_hide_deposits){
		    	    		if(lipukaApplication.getProfileID() == 0)	{
		    	    			lipukaApplication.setCurrentDialogTitle("Sign In");
			    			      //lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
			    			action = SignInDialog.INVESTMENTS_N_CREDIT;
			    			showDialog(Main.DIALOG_SIGN_IN_ID);
		    				}else{
		    	    		LinearLayout  deposits = (LinearLayout) findViewById(R.id.investments_n_credit_deposits);
		    	    		Drawable img = null;
			if(deposits.isShown()){
				//deposits.setVisibility(View.GONE);
				ExpandAnimation.collapse(deposits);
    			img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//deposits.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(deposits);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  productInfo = (LinearLayout) findViewById(R.id.investments_n_credit_product_info);
			    	    		productInfo.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideProductInfo = (Button) findViewById(R.id.show_or_hide_product_info);
		    	    	        showOrHideProductInfo.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		    	    	        LinearLayout  applyForCredit = (LinearLayout) findViewById(R.id.investments_n_credit_apply_for_credit);
			    	    		applyForCredit.setVisibility(View.GONE);
		    	    	        Button showOrHideApplyForCredit = (Button) findViewById(R.id.show_or_hide_apply_for_credit);
		    	    	        showOrHideApplyForCredit.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	
		    	    	}
			    	    	}else if (arg0.getId() == R.id.show_or_hide_apply_for_credit){
			    	    		LinearLayout  applyForCredit = (LinearLayout) findViewById(R.id.investments_n_credit_apply_for_credit);
			    	    		Drawable img = null;
				if(applyForCredit.isShown()){
					//applyForCredit.setVisibility(View.GONE);
					ExpandAnimation.collapse(applyForCredit);
					img = getResources().getDrawable( R.drawable.show );
			    	    		}else{
			    	    			//applyForCredit.setVisibility(View.VISIBLE);
			    	    			ExpandAnimation.expand(applyForCredit);
			    	    			img = getResources().getDrawable( R.drawable.hide );
			    	    			LinearLayout  productInfo = (LinearLayout) findViewById(R.id.investments_n_credit_product_info);
				    	    		productInfo.setVisibility(View.GONE);
				    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
			    	    	        Button showOrHideProductInfo = (Button) findViewById(R.id.show_or_hide_product_info);
			    	    	        showOrHideProductInfo.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	        LinearLayout  deposits = (LinearLayout) findViewById(R.id.investments_n_credit_deposits);
				    	    		deposits.setVisibility(View.GONE);
			    	    	        Button showOrHideDeposits = (Button) findViewById(R.id.show_or_hide_deposits);
			    	    	        showOrHideDeposits.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
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
	    	        case Main.DIALOG_SIGN_IN_ID:
	    		    	SignInDialog sid = new SignInDialog(this);
	    		    	sid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    		    	sid.setMessage(lipukaApplication.getCurrentDialogMsg());
	    		    	sid.setAction(action);
	    	        	dialog = sid;
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
	    	        case Main.DIALOG_SIGN_IN_ID:
	    		    	SignInDialog sid = (SignInDialog)dialog;
	    		    	sid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    		    	sid.setMessage(lipukaApplication.getCurrentDialogMsg());
	    		    	sid.setAction(action);
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
	    		public class OnSourceAccountSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSourceAccount = sourceAccountsArray[pos].getValue();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnSavingsAccountSelectedListener implements OnItemSelectedListener {
	    			boolean firstTimeSelection = true;
		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSavingsAccount = savingsAccountsArray[pos].getText();
		 	        	if(firstTimeSelection){
		 	        		firstTimeSelection = false;
		 	        		return;
		 	        	}
			 	   	   	final LinearLayout fixedDepositPeriodLayout = (LinearLayout) findViewById(R.id.fixed_deposit_period_layout);
 	if(selectedSavingsAccount.equals("Fixed Deposit Account")){
 		fixedDepositPeriodLayout.setVisibility(View.VISIBLE);    				 	        		
			 	        	}else{
 	        			 			fixedDepositPeriodLayout.setVisibility(View.GONE);    				 	        		
		 	        			 	        	}
 	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnPeriodSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedPeriod = periodsArray[pos].getText();
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
	    public void showSignOutBtn(){
		    Button signOutButton = (Button)findViewById(R.id.sign_out);

					 signOutButton.setVisibility(View.VISIBLE);
					    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
					    dividerTwo.setVisibility(View.VISIBLE);	
	    }
	    public void action(){
    		LinearLayout  deposits = (LinearLayout) findViewById(R.id.investments_n_credit_deposits);
    		Drawable img = null;
if(deposits.isShown()){
//deposits.setVisibility(View.GONE);
	ExpandAnimation.collapse(deposits);
		img = getResources().getDrawable( R.drawable.show );
    		}else{
    			//deposits.setVisibility(View.VISIBLE);
    			ExpandAnimation.expand(deposits);
    			img = getResources().getDrawable( R.drawable.hide );
    			LinearLayout  productInfo = (LinearLayout) findViewById(R.id.investments_n_credit_product_info);
	    		productInfo.setVisibility(View.GONE);
	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
    	        Button showOrHideProductInfo = (Button) findViewById(R.id.show_or_hide_product_info);
    	        showOrHideProductInfo.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
    	        LinearLayout  applyForCredit = (LinearLayout) findViewById(R.id.investments_n_credit_apply_for_credit);
	    		applyForCredit.setVisibility(View.GONE);
    	        Button showOrHideApplyForCredit = (Button) findViewById(R.id.show_or_hide_apply_for_credit);
    	        showOrHideApplyForCredit.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
Button showOrHideDeposits = (Button) findViewById(R.id.show_or_hide_deposits);
showOrHideDeposits.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

	    }
	    	    }