package kcb.android;


import java.util.List;



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
import lipuka.android.view.adapter.ContactsAutoCompleteCursorAdapter;
import lipuka.android.view.anim.LipukaAnim;
import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import kcb.android.EcobankHome.ConfirmationDialog;
import kcb.android.TransferFunds.MyGestureDetector;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
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

public class AirtimeTopup extends Activity implements OnClickListener, ResponseActivity{
	   
	Button submit, submitOther;
	EditText amount, amountOther;
	AutoCompleteTextView mobileNo;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedSourceAccount, selectedOwnNetwork, selectedOtherNetwork, selectedOwnAmount, selectedOtherAmount;
int selectedSourceIndex;
	LipukaApplication lipukaApplication;
	String selectedSourceOfFunds, selectedPurposeOfFunds, selectedCountry;
	LipukaListItem[] sourceAccountsArray, networksArray;
	String amountStr, destinationStr;
	


	ViewFlipper flipper;
	GestureDetector gestureDetector;
	int selected;
	LinearLayout ownNumber, otherNumber;
	TextView ownNumberText, otherNumberText;
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.airtime_topup);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Airtime Topup");
	        amount = (EditText) findViewById(R.id.amount_field);
	        amountOther = (EditText) findViewById(R.id.other_amount_field);
	        
	        mobileNo = (AutoCompleteTextView) findViewById(R.id.mobile_no_field);
	        
	        Cursor c = lipukaApplication.getContactsAutoCompleteCursor();
	          ContactsAutoCompleteCursorAdapter contactsAdapter = new ContactsAutoCompleteCursorAdapter(this, c);
	          mobileNo.setAdapter(contactsAdapter);
	          
	          OnClickListener ownAmountListener = new OnClickListener() {
		            public void onClick(View v) {
		                // Perform action on clicks
		                RadioButton rb = (RadioButton) v;
		                if(rb.getId() == R.id.specify_amount){
		                	selectedOwnAmount = null;
			                amount.setEnabled(true);
		                }else{
		                	selectedOwnAmount = rb.getText().toString();
			                amount.setText("");
			                amount.setEnabled(false);

		                }
		            }
		        };
		        
		        RadioButton hundred = (RadioButton) findViewById(R.id.hundred);
		        RadioButton twohundrednfifty = (RadioButton) findViewById(R.id.twohundrednfifty);
		        RadioButton fivehundred = (RadioButton) findViewById(R.id.fivehundred);
		        RadioButton thousand = (RadioButton) findViewById(R.id.thousand);
		        RadioButton specifyAmount = (RadioButton) findViewById(R.id.specify_amount);
		        
		        hundred.setOnClickListener(ownAmountListener);
		        twohundrednfifty.setOnClickListener(ownAmountListener);
		        fivehundred.setOnClickListener(ownAmountListener);
		        thousand.setOnClickListener(ownAmountListener);
		        specifyAmount.setOnClickListener(ownAmountListener);
		        
		        OnClickListener otherAmountListener = new OnClickListener() {
		            public void onClick(View v) {
		                // Perform action on clicks
		                RadioButton rb = (RadioButton) v;
		                if(rb.getId() == R.id.other_specify_amount){
		                	selectedOtherAmount = null;
			                amountOther.setEnabled(true);
		                }else{
		                	selectedOtherAmount = rb.getText().toString();
		                	amountOther.setText("");
		                	amountOther.setEnabled(false);

		                }
		            }
		        };
		        
		        RadioButton otheHundred = (RadioButton) findViewById(R.id.other_hundred);
		        RadioButton otherHundrednfifty = (RadioButton) findViewById(R.id.other_twohundrednfifty);
		        RadioButton otherFivehundred = (RadioButton) findViewById(R.id.other_fivehundred);
		        RadioButton otherThousand = (RadioButton) findViewById(R.id.other_thousand);
		        RadioButton otherSpecifyAmount = (RadioButton) findViewById(R.id.other_specify_amount);
		        
		        otheHundred.setOnClickListener(otherAmountListener);
		        otherHundrednfifty.setOnClickListener(otherAmountListener);
		        otherFivehundred.setOnClickListener(otherAmountListener);
		        otherThousand.setOnClickListener(otherAmountListener);
		        otherSpecifyAmount.setOnClickListener(otherAmountListener);
		        
		        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountOther.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        
	        final Spinner spinner = (Spinner) findViewById(R.id.mobile_network_spinner);
	        spinner.setOnItemSelectedListener(new OnOwnNetworkSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadMobileNetworks());
	         networksArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	networksArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating mobile networks list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, networksArray);
	   	spinner.setAdapter(adapter);
	   		
	       final Spinner otherSpinner = (Spinner) findViewById(R.id.other_mobile_network_spinner);
	       otherSpinner.setOnItemSelectedListener(new OnOtherNetworkSelectedListener());
	        
	   	 ComboBoxAdapter otherAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, networksArray);
	   	otherSpinner.setAdapter(otherAdapter);
      
	    final Spinner spinnerSourceAccount = (Spinner) findViewById(R.id.source_account_spinner);
        spinnerSourceAccount.setOnItemSelectedListener(new OnSourceAccountSelectedListener());
   	
    int remittanceIndex = -1, salaryIndex = -1;
    
	  try{      JSONArray sources = lipukaApplication.getProfileDataArray("accounts");
	  sourceAccountsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("account_alias"), currentSource.getString("account_id"));
	        	sourceAccountsArray[i]= lipukaListItem;  
	        	if(currentSource.getString("account_type").equals("Remittance Account")){
	        		remittanceIndex = i;	
	        	}else{
	        		salaryIndex = i;			        		
	        	}
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating source accounts list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapterSourceAccount = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceAccountsArray);
	   	spinnerSourceAccount.setAdapter(adapterSourceAccount);
	   	
	   	submit = (Button) findViewById(R.id.own_number_submit);
	        submit.setOnClickListener(this);
	        submitOther = (Button) findViewById(R.id.other_number_submit);
	        submitOther.setOnClickListener(this);
	   	
	        gestureDetector = new GestureDetector(this, new MyGestureDetector());
	        flipper = (ViewFlipper) findViewById(R.id.flipper);
	        
	        ownNumber = (LinearLayout)findViewById(R.id.airtime_topup_own_number);
	        otherNumber = (LinearLayout)findViewById(R.id.airtime_topup_other_number);
	        ownNumberText = (TextView)findViewById(R.id.own_number_text);
	        otherNumberText = (TextView)findViewById(R.id.other_number_text);
	        ownNumberText.setOnClickListener(this);
	        otherNumberText.setOnClickListener(this);
	       
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
	    	Log.d(Main.TAG, "creating funds transfer error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(AirtimeTopup.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(AirtimeTopup.class, false);
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
	    			String amountStr = amount.getText().toString();	
	    			
	    			if((selectedOwnAmount == null || selectedOwnAmount.length() == 0) &&
	    					(amountStr == null || amountStr.length() == 0)){
	    				valid = false;
	    				errorBuffer.append("Select or enter amount\n");
	    			}else if((selectedOwnAmount != null && selectedOwnAmount.length() > 0) &&
	    					(amountStr != null && amountStr.length() > 0)){
	    				valid = false;
	    				errorBuffer.append("Select or enter amount, not both\n");
	    			}
	    			    			
	    			    			if(valid){
	    			    				if(selectedOwnAmount != null){
	    			    					amountStr = selectedOwnAmount;
	    				    			}
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			payloadBuffer.append(amountStr+"|");	    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				
	    			    				lipukaApplication.putPayload("source_account_id", sourceAccountsArray[selectedSourceIndex].getValue());
	    			    				lipukaApplication.putPayload("amount", amountStr);
	    		    	    			lipukaApplication.setServiceID("75");
		
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to buy KES "+amountStr+
	    			    			    		  " worth of airtime for yourself. Press \"OK\" to top up now or \"Cancel\" to edit top-up details details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);		
	    			    			this.amountStr = amountStr;
destinationStr  = "yourself";
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitOther == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
   			String amountStr = amountOther.getText().toString();	
   			String mobileNoStr = mobileNo.getText().toString();	
	    			
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
			if((selectedOtherAmount == null || selectedOtherAmount.length() == 0) &&
	    					(amountStr == null || amountStr.length() == 0)){
	    				valid = false;
	    				errorBuffer.append("Select or enter amount\n");
	    			}else if((selectedOtherAmount != null && selectedOtherAmount.length() > 0) &&
	    					(amountStr != null && amountStr.length() > 0)){
	    				valid = false;
	    				errorBuffer.append("Select or enter amount, not both\n");
	    			}
	    			    			
	    			    			if(valid){
	    			    				if(selectedOtherAmount != null){
	    			    					amountStr = selectedOtherAmount;
	    				    			}
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			payloadBuffer.append(amountStr+"|");	    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.putPayload("source_account_id", sourceAccountsArray[selectedSourceIndex].getValue());
	    			    				lipukaApplication.putPayload("amount", amountStr);
	    			    				lipukaApplication.putPayload("msisdn", mobileNoStr);
	    		    	    			lipukaApplication.setServiceID("76");
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to buy KES "+amountStr+
	    			    			    		  " worth of airtime for "+mobileNoStr+". Press \"OK\" to top up now or \"Cancel\" to edit top-up details details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);		
	    			    			this.amountStr = amountStr;
destinationStr  = mobileNoStr;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}		

	    		}else if(arg0.getId() == R.id.own_number_text){
	    			 if (selected == 1) {
		     			    flipper.setInAnimation(inFromLeftAnimation());
		     			    flipper.setOutAnimation(outToRightAnimation());
		     			    flipper.setDisplayedChild(0);
		     			    selected = 0;
		     		   setSelectedBg();
		     			   }else{
		     				   return;
		     			   }			   
		     	}else if(arg0.getId() == R.id.other_number_text){
		     		if (selected == 0) {
		     		    flipper.setInAnimation(inFromRightAnimation());
		     		    flipper.setOutAnimation(outToLeftAnimation());
		     		    flipper.setDisplayedChild(1);
		     		    selected = 1;
		     			   setSelectedBg();
		     		   }else{
		     			   return;
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
	    		public class OnOwnNetworkSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedOwnNetwork = networksArray[pos].getValue();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnOtherNetworkSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedOtherNetwork = networksArray[pos].getValue();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnSourceAccountSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSourceAccount = sourceAccountsArray[pos].getText();
		 	        	selectedSourceIndex = pos;
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
		     /* lipukaApplication.setCurrentDialogMsg("Dear Alice, you have successfully bought KES "+amountStr+" worth of airtime for "+destinationStr+". Thank you");
		      lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
		      showDialog(Main.DIALOG_SERVICE_RESPONSE_ID);*/
			 lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
	           showDialog(Main.DIALOG_MSG_ID);
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
    	  ownNumberText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
    	  otherNumberText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	         break;
    	     case 1:
    	    	  ownNumberText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	    	  otherNumberText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
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