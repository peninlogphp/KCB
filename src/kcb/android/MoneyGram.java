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

public class MoneyGram extends Activity implements OnClickListener, ResponseActivity{
	   
	Button submitSendMoneyRecipient, submitSendMoneyBeneficiary,
	submitReceiveMoneyAccount, submitReceiveMoneyPrepaidCard, addBeneficiary, editBeneficiary;
	EditText amountSendMoneyRecipient, amountSendMoneyBeneficiary,
	amountReceiveMoneyAccount, amountReceiveMoneyPrepaidCard;
	EditText recipientMobileNo, recipientName, destinationCountry;
	EditText accountNo, prepaidCardNo, accountIdNo, prepaidCardIdNo, 
	accountMobileNo, prepaidCardMobileNo, accountRefNo, prepaidCardRefNo;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedSourceAccount, selectedDestinationCountry, 
	selectedRecipientCurrency, selectedBeneficiary, selectedBeneficiaryCurrency;


	LipukaApplication lipukaApplication;
	LipukaListItem[] sourceAccountsArray, destinationCountriesArray,
	recipientCurrenciesArray, beneficiariesArray, beneficiaryCurrenciesArray;
	String amountStr, destinationStr;
	


	ViewFlipper sendMoneyFlipper, receiveMoneyFlipper;
	GestureDetector gestureDetector;
	int selectedSendMoneyOption, selectedReceiveMoneyOption;
	LinearLayout sendMoneyRecipient, sendMoneyBeneficiary;
	LinearLayout receiveMoneyAccount, receiveMoneyPrepaid;
	TextView sendMoneyRecipientText, sendMoneyBeneficiaryText;
	TextView receiveMoneyAccountText, receiveMoneyPrepaidText;
	byte service;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.moneygram);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Western Union");
	        amountSendMoneyRecipient = (EditText) findViewById(R.id.send_money_recipient_amount_field);
	        amountSendMoneyBeneficiary = (EditText) findViewById(R.id.send_money_beneficiaries_amount_field);
	        amountReceiveMoneyAccount = (EditText) findViewById(R.id.receive_money_account_amount_field);
	        amountReceiveMoneyPrepaidCard = (EditText) findViewById(R.id.receive_money_prepaid_card_amount_field);
	        
	        recipientMobileNo = (EditText) findViewById(R.id.mobile_number_field);
	        recipientName = (EditText) findViewById(R.id.name_field);
	        accountNo = (EditText) findViewById(R.id.account_number_field);
	        accountIdNo = (EditText) findViewById(R.id.account_id_number_field);
	        accountMobileNo = (EditText) findViewById(R.id.account_mobile_number_field);
	        accountRefNo = (EditText) findViewById(R.id.account_ref_number_field);
 prepaidCardNo = (EditText) findViewById(R.id.prepaid_card_number_field);
 prepaidCardIdNo = (EditText) findViewById(R.id.prepaid_card_id_number_field);
 prepaidCardMobileNo = (EditText) findViewById(R.id.prepaid_card_mobile_number_field);
 prepaidCardRefNo = (EditText) findViewById(R.id.prepaid_card_ref_number_field);


	        
	       recipientMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);		
	        amountSendMoneyRecipient.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountSendMoneyBeneficiary.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountReceiveMoneyAccount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountReceiveMoneyPrepaidCard.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        
	        accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
	        prepaidCardNo.setInputType(InputType.TYPE_CLASS_NUMBER);
	        accountIdNo.setInputType(InputType.TYPE_CLASS_NUMBER);
	        prepaidCardIdNo.setInputType(InputType.TYPE_CLASS_NUMBER);
	        accountMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);
	        prepaidCardMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);
	        accountRefNo.setInputType(InputType.TYPE_CLASS_NUMBER);
	        prepaidCardRefNo.setInputType(InputType.TYPE_CLASS_NUMBER);



	       
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
	   	
	
	       final Spinner recipientCurrencySpinner = (Spinner) findViewById(R.id.recipient_currency_spinner);
	       recipientCurrencySpinner.setOnItemSelectedListener(new OnRecipientCurrencySelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadCurrencies());
	         recipientCurrenciesArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	recipientCurrenciesArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating curencies list error", ex);
	
	    	}
	   	 ComboBoxAdapter recipientCurrencyAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, recipientCurrenciesArray);
	   	recipientCurrencySpinner.setAdapter(recipientCurrencyAdapter);
	   	
	    final Spinner beneficiariesCurrencySpinner = (Spinner) findViewById(R.id.beneficiaries_currency_spinner);
	    beneficiariesCurrencySpinner.setOnItemSelectedListener(new OnBeneficiaryCurrencySelectedListener());
	        
	   	 ComboBoxAdapter beneficiariesCurrencyAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, recipientCurrenciesArray);
	   	beneficiariesCurrencySpinner.setAdapter(beneficiariesCurrencyAdapter);
	   	
	   	final Spinner beneficiarySpinner = (Spinner) findViewById(R.id.beneficiary_spinner);
	    beneficiarySpinner.setOnItemSelectedListener(new OnBeneficiarySelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSavedBeneficiaries());
	  beneficiariesArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	beneficiariesArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating beneficiaries list error", ex);
	
	    	}
	   	 ComboBoxAdapter beneficiariesAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, beneficiariesArray);
	   	beneficiarySpinner.setAdapter(beneficiariesAdapter);
	   	
	   
	        submitReceiveMoneyAccount = (Button) findViewById(R.id.receive_money_account_submit);
	        submitReceiveMoneyAccount.setOnClickListener(this);
	        submitReceiveMoneyPrepaidCard = (Button) findViewById(R.id.receive_money_prepaid_card_submit);
	        submitReceiveMoneyPrepaidCard.setOnClickListener(this);
	        submitSendMoneyRecipient = (Button) findViewById(R.id.send_money_recipient_submit);
	        submitSendMoneyRecipient.setOnClickListener(this);
	        submitSendMoneyBeneficiary = (Button) findViewById(R.id.send_money_beneficiaries_submit);
	        submitSendMoneyBeneficiary.setOnClickListener(this);
	        addBeneficiary = (Button) findViewById(R.id.add_beneficiary);
	        addBeneficiary.setOnClickListener(this);
	        editBeneficiary = (Button) findViewById(R.id.edit_beneficiary);
	        editBeneficiary.setOnClickListener(this);
	        
	        Button showOrHideSendMoney = (Button) findViewById(R.id.show_or_hide_send_money);
	        showOrHideSendMoney.setOnClickListener(this);
	        Button showOrHideReceiveMoney = (Button) findViewById(R.id.show_or_hide_receive_money);
	        showOrHideReceiveMoney.setOnClickListener(this);
	        
	        Button showOrHideBeneficiaries = (Button) findViewById(R.id.show_or_hide_beneficiaries);
	        showOrHideBeneficiaries.setOnClickListener(this);
	        
	       
	   	
	        gestureDetector = new GestureDetector(this, new MyGestureDetector());
	        sendMoneyFlipper = (ViewFlipper) findViewById(R.id.send_money_flipper);
	        receiveMoneyFlipper = (ViewFlipper) findViewById(R.id.receive_money_flipper);

	        /*sendMoneyFlipper.setOnTouchListener(new OnTouchListener() {

	     	   public boolean onTouch(View v, MotionEvent event) {

	     	    if (gestureDetector.onTouchEvent(event)) {

	     	     return true;

	     	    } else {
	 				//Log.i(SalamaSureMain.TAG, "onTouch() returned false ");

	     	     return false;
	     	    }
	     	   }
	     	});
	        receiveMoneyFlipper.setOnTouchListener(new OnTouchListener() {

		     	   public boolean onTouch(View v, MotionEvent event) {

		     	    if (gestureDetector.onTouchEvent(event)) {

		     	     return true;

		     	    } else {
		 				//Log.i(SalamaSureMain.TAG, "onTouch() returned false ");

		     	     return false;
		     	    }
		     	   }
		     	});*/
	        
	        sendMoneyRecipient = (LinearLayout)findViewById(R.id.moneygram_send_money_recipient);
	        sendMoneyBeneficiary = (LinearLayout)findViewById(R.id.moneygram_send_money_beneficiaries);
	        sendMoneyRecipientText = (TextView)findViewById(R.id.recipient_text);
	        sendMoneyBeneficiaryText = (TextView)findViewById(R.id.beneficiaries_text);
	        sendMoneyRecipientText.setOnClickListener(this);
	        sendMoneyBeneficiaryText.setOnClickListener(this);
	        
	        receiveMoneyAccount = (LinearLayout)findViewById(R.id.moneygram_receive_money_account);
	        receiveMoneyPrepaid = (LinearLayout)findViewById(R.id.moneygram_receive_money_prepaid_card);
	        receiveMoneyAccountText = (TextView)findViewById(R.id.account_text);
	        receiveMoneyPrepaidText = (TextView)findViewById(R.id.prepaid_card_text);
	        receiveMoneyAccountText.setOnClickListener(this);
	        receiveMoneyPrepaidText.setOnClickListener(this);
	   	
AutoCompleteTextView countries = (AutoCompleteTextView) findViewById(R.id.country_field);
countries.setHint("country name");
String[] countriesArray = null; 
try{      JSONArray sources = new JSONArray(lipukaApplication.loadAllCountries());
countriesArray = new String[sources.length()];
	destinationCountriesArray = new LipukaListItem[sources.length()];

    JSONObject currentSource;
      for(int i = 0; i < sources.length(); i++){
      	currentSource = sources.getJSONObject(i);
    	LipukaListItem lipukaListItem = new LipukaListItem("", 
  currentSource.getString("name"), currentSource.getString("value"));
    	destinationCountriesArray[i]= lipukaListItem;   	
      	countriesArray[i]=  currentSource.getString("name");   	
      }
      }catch(Exception ex){
	    	Log.d(Main.TAG, "creating countries list error", ex);

  	}
      ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, countriesArray);

      countries.setAdapter(countryAdapter);    
countries.setOnItemClickListener(new AdapterView.OnItemClickListener() { 

@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        long arg3) {
selectedDestinationCountry  = destinationCountriesArray[arg2].getText(); 
}
});
	  
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
			lipukaApplication.setActivityState(MoneyGram.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(MoneyGram.class, false);
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
	    		if (submitSendMoneyRecipient == arg0){
	    		    lipukaApplication.clearNavigationStack();
	
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String mobileNoStr = recipientMobileNo.getText().toString();	
	    			String nameStr = recipientName.getText().toString();	
	    			String amountStr = amountSendMoneyRecipient.getText().toString();	
	    			
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
	    			if(nameStr == null || nameStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Name is missing\n");
	    			}
	    			if(selectedDestinationCountry == null || selectedDestinationCountry.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Destination country is missing, please select one from the list suggested\n");
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
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to send KES "+amountStr+
	    			    			    		  " to "+nameStr+", mobile number "+mobileNoStr+". Press \"OK\" to send now or \"Cancel\" to edit send money details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
	    			    			this.amountStr = amountStr;
destinationStr  = nameStr+", mobile number "+mobileNoStr;
service = 1;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitSendMoneyBeneficiary == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();	
	    			String amountStr = amountSendMoneyBeneficiary.getText().toString();	
	    				    			
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
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to send KES "+amountStr+
	    			    			    		  " to "+selectedBeneficiary+". Press \"OK\" to send now or \"Cancel\" to edit send money details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
	    			    			this.amountStr = amountStr;
destinationStr  = selectedBeneficiary;
service = 1;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	
	    		}else if (submitReceiveMoneyAccount == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String accountNoStr = accountNo.getText().toString();	
	    			String accountIdNoStr = accountIdNo.getText().toString();	
	    			String accountMobileNoStr = accountMobileNo.getText().toString();	
	    			String accountRefNoStr = accountRefNo.getText().toString();	
	    			String amountStr = amountReceiveMoneyAccount.getText().toString();	
	    			
	    			if(accountNoStr == null || accountNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Account number is missing\n");
	    			}
	    			if(accountIdNoStr == null || accountIdNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("ID number is missing\n");
	    			}
	    			if(accountMobileNoStr == null || accountMobileNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Mobile number is missing\n");
	    			}
	    			if(accountMobileNoStr != null && accountMobileNoStr.length() > 0){
	    				accountMobileNoStr = lipukaApplication.ensureCountryCode(accountMobileNoStr);
	    				if(accountMobileNoStr != null){
	    if(!MsisdnRegex.isValidMsisdn(accountMobileNoStr)){
	    				valid = false;
	    				errorBuffer.append("Enter valid mobile number\n");
	    			}	
	    				}else{
	    					valid = false;
	    					errorBuffer.append("Enter valid mobile number\n");			
	    				}
	    			}
	    			if(accountRefNoStr == null || accountRefNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Reference number is missing\n");
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
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to receive KES "+amountStr+
	    			    			    		  " into "+accountNoStr+". Press \"OK\" to receive now or \"Cancel\" to edit receive money details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
	    			    			this.amountStr = amountStr;
destinationStr  = accountNoStr;
service = 2;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}
	    		}else if (submitReceiveMoneyPrepaidCard == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	  			
    			boolean valid = true;
    			StringBuffer errorBuffer = new StringBuffer();
    			String prepaidCardNoStr = prepaidCardNo.getText().toString();	
    			String prepaidCardIdNoStr = prepaidCardIdNo.getText().toString();	
    			String prepaidCardMobileNoStr = prepaidCardMobileNo.getText().toString();	
    			String prepaidCardRefNoStr = prepaidCardRefNo.getText().toString();	
    			String amountStr = amountReceiveMoneyPrepaidCard.getText().toString();	
    			
    			if(prepaidCardNoStr == null || prepaidCardNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Card number is missing\n");
    			}
    			if(prepaidCardIdNoStr == null || prepaidCardIdNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("ID number is missing\n");
    			}
    			if(prepaidCardMobileNoStr == null || prepaidCardMobileNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Mobile number is missing\n");
    			}
    			if(prepaidCardMobileNoStr != null && prepaidCardMobileNoStr.length() > 0){
    				prepaidCardMobileNoStr = lipukaApplication.ensureCountryCode(prepaidCardMobileNoStr);
    				if(prepaidCardMobileNoStr != null){
    if(!MsisdnRegex.isValidMsisdn(prepaidCardMobileNoStr)){
    				valid = false;
    				errorBuffer.append("Enter valid mobile number\n");
    			}	
    				}else{
    					valid = false;
    					errorBuffer.append("Enter valid mobile number\n");			
    				}
    			}
    			if(prepaidCardRefNoStr == null || prepaidCardRefNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Reference number is missing\n");
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
    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
    			    			      lipukaApplication.setCurrentDialogMsg("You are about to receive KES "+amountStr+
    			    			    		  " into "+prepaidCardNoStr+". Press \"OK\" to receive now or \"Cancel\" to edit receive money details");
    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
    			    			this.amountStr = amountStr;
destinationStr  = prepaidCardNoStr;
service = 2;
    			    			}else{
    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    			    			}
	    		}else if(arg0.getId() == R.id.recipient_text){
	    			 if (selectedSendMoneyOption == 1) {
		     			    sendMoneyFlipper.setInAnimation(inFromLeftAnimation());
		     			    sendMoneyFlipper.setOutAnimation(outToRightAnimation());
		     			    sendMoneyFlipper.setDisplayedChild(0);
		     			    selectedSendMoneyOption = 0;
		     		   setSelectedSendMoneyBg();
		     			   }else{
		     				   return;
		     			   }			   
		     	}else if(arg0.getId() == R.id.beneficiaries_text){
		     		if (selectedSendMoneyOption == 0) {
		     		    sendMoneyFlipper.setInAnimation(inFromRightAnimation());
		     		    sendMoneyFlipper.setOutAnimation(outToLeftAnimation());
		     		    sendMoneyFlipper.setDisplayedChild(1);
		     		    selectedSendMoneyOption = 1;
		     			   setSelectedSendMoneyBg();
		     		   }else{
		     			   return;
		     		   }			   
		     }else if(arg0.getId() == R.id.account_text){
    			 if (selectedReceiveMoneyOption == 1) {
	     			    receiveMoneyFlipper.setInAnimation(inFromLeftAnimation());
	     			   receiveMoneyFlipper.setOutAnimation(outToRightAnimation());
	     			  receiveMoneyFlipper.setDisplayedChild(0);
	     			    selectedReceiveMoneyOption = 0;
	     		   setSelectedReceiveMoneyBg();
	     			   }else{
	     				   return;
	     			   }			   
	     	}else if(arg0.getId() == R.id.prepaid_card_text){
	     		if (selectedReceiveMoneyOption == 0) {
	     			receiveMoneyFlipper.setInAnimation(inFromRightAnimation());
	     			receiveMoneyFlipper.setOutAnimation(outToLeftAnimation());
	     			receiveMoneyFlipper.setDisplayedChild(1);
	     			selectedReceiveMoneyOption = 1;
	     			   setSelectedReceiveMoneyBg();
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_send_money){
	    	    		LinearLayout  sendMoney = (LinearLayout) findViewById(R.id.moneygram_send_money);
	    	    		Drawable img = null;
		if(sendMoney.isShown()){
			//sendMoney.setVisibility(View.GONE);
			ExpandAnimation.collapse(sendMoney);
    			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//sendMoney.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(sendMoney);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  receiveMoney = (LinearLayout) findViewById(R.id.moneygram_receive_money);
		    	    		receiveMoney.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideReceiveMoney = (Button) findViewById(R.id.show_or_hide_receive_money);
	    	    	        showOrHideReceiveMoney.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    	 		LinearLayout  beneficiaries = (LinearLayout) findViewById(R.id.moneygram_beneficiaries);
		    	    		beneficiaries.setVisibility(View.GONE);
	    	    	        Button showOrHideBeneficiaries = (Button) findViewById(R.id.show_or_hide_beneficiaries);
	    	    	        showOrHideBeneficiaries.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_receive_money){
		    	    		LinearLayout  receiveMoney = (LinearLayout) findViewById(R.id.moneygram_receive_money);
		    	    		Drawable img = null;
			if(receiveMoney.isShown()){
				//receiveMoney.setVisibility(View.GONE);
				ExpandAnimation.collapse(receiveMoney);
				img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//receiveMoney.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(receiveMoney);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  sendMoney = (LinearLayout) findViewById(R.id.moneygram_send_money);
			    	    		sendMoney.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideSendMoney = (Button) findViewById(R.id.show_or_hide_send_money);
		    	    	        showOrHideSendMoney.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		    	    	        LinearLayout  beneficiaries = (LinearLayout) findViewById(R.id.moneygram_beneficiaries);
			    	    		beneficiaries.setVisibility(View.GONE);
		    	    	        Button showOrHideBeneficiaries = (Button) findViewById(R.id.show_or_hide_beneficiaries);
		    	    	        showOrHideBeneficiaries.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

			    	    	}else if (arg0.getId() == R.id.show_or_hide_beneficiaries){
			    	    		LinearLayout  beneficiaries = (LinearLayout) findViewById(R.id.moneygram_beneficiaries);
			    	    		Drawable img = null;
				if(beneficiaries.isShown()){
					//beneficiaries.setVisibility(View.GONE);
					ExpandAnimation.collapse(beneficiaries);
					img = getResources().getDrawable( R.drawable.show );
			    	    		}else{
			    	    			//beneficiaries.setVisibility(View.VISIBLE);
			    	    			ExpandAnimation.expand(beneficiaries);
			    	    			img = getResources().getDrawable( R.drawable.hide );
			    	    			LinearLayout  sendMoney = (LinearLayout) findViewById(R.id.moneygram_send_money);
				    	    		sendMoney.setVisibility(View.GONE);
				    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
			    	    	        Button showOrHideSendMoney = (Button) findViewById(R.id.show_or_hide_send_money);
			    	    	        showOrHideSendMoney.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	        LinearLayout  receiveMoney = (LinearLayout) findViewById(R.id.moneygram_receive_money);
				    	    		receiveMoney.setVisibility(View.GONE);
			    	    	        Button showOrHideReceiveMoney = (Button) findViewById(R.id.show_or_hide_receive_money);
			    	    	        showOrHideReceiveMoney.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	 		}
				((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

				    	    	}else if (arg0.getId() == R.id.add_beneficiary){
				    	    		Intent i = new Intent(MoneyGram.this, AddBeneficiary.class);
				    			    startActivity(i);
				    	    	}else if (arg0.getId() == R.id.edit_beneficiary){
				    	    		lipukaApplication.setCurrentDialogTitle("Response");
  			    	              lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
  			    	              showDialog(Main.DIALOG_MSG_ID);
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
	    		public class OnSourceAccountSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSourceAccount = sourceAccountsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnRecipientCurrencySelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedRecipientCurrency = recipientCurrenciesArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    	
	    		public class OnBeneficiaryCurrencySelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedBeneficiaryCurrency = recipientCurrenciesArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnBeneficiarySelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedBeneficiary = beneficiariesArray[pos].getText();
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
	String resp = null;
	if(service == 1){
			resp =  "Dear Alice, you have successfully sent KES "+amountStr+" to "+destinationStr+". Thank you";   
		   }else{
				resp =  "Dear Alice, you have successfully received KES "+amountStr+" into "+destinationStr+". Thank you";   			   
		   }
			lipukaApplication.setCurrentDialogTitle("Response");
		      lipukaApplication.setCurrentDialogMsg(resp);
		      lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
		      showDialog(Main.DIALOG_SERVICE_RESPONSE_ID);
	   }
	   
		private void increaseSendMoneySelected(){
    		selectedSendMoneyOption++;
    		if(selectedSendMoneyOption == 2){
    			selectedSendMoneyOption = 0;
    		}
    	}
    	private void dereaseSendMoneySelected(){
    		selectedSendMoneyOption--;
    		if(selectedSendMoneyOption == -1){
    			selectedSendMoneyOption = 1;
    		}
    	}
		private void increaseReceiveMoneySelected(){
    		selectedReceiveMoneyOption++;
    		if(selectedReceiveMoneyOption == 2){
    			selectedReceiveMoneyOption = 0;
    		}
    	}
    	private void dereaseReceiveMoneySelected(){
    		selectedReceiveMoneyOption--;
    		if(selectedReceiveMoneyOption == -1){
    			selectedReceiveMoneyOption = 1;
    		}
    	}
    	private void setSelectedSendMoneyBg(){
    		Resources res = getResources();
    		 switch (selectedSendMoneyOption) {
    	     case 0:
    	  sendMoneyRecipientText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
    	  sendMoneyBeneficiaryText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	         break;
    	     case 1:
    	    	  sendMoneyRecipientText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	    	  sendMoneyBeneficiaryText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
    	    	          break;
    	         default:
    	             return;
    	     }
    	}
    	private void setSelectedReceiveMoneyBg(){
    		Resources res = getResources();
    		 switch (selectedReceiveMoneyOption) {
    	     case 0:
    	  receiveMoneyAccountText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
    	  receiveMoneyPrepaidText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	         break;
    	     case 1:
    	    	 receiveMoneyAccountText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	    	 receiveMoneyPrepaidText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
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
	    			LinearLayout  sendMoney = (LinearLayout) findViewById(R.id.moneygram_send_money);
	    			LinearLayout  receiveMoney = (LinearLayout) findViewById(R.id.moneygram_receive_money);
if(sendMoney.isShown()){
			    sendMoneyFlipper.setInAnimation(inFromRightAnimation());
			    sendMoneyFlipper.setOutAnimation(outToLeftAnimation());
			    sendMoneyFlipper.showNext();
			    increaseSendMoneySelected();
				   setSelectedSendMoneyBg();
}else if(receiveMoney.isShown()){
	 receiveMoneyFlipper.setInAnimation(inFromRightAnimation());
	 receiveMoneyFlipper.setOutAnimation(outToLeftAnimation());
	 receiveMoneyFlipper.showNext();
	    increaseReceiveMoneySelected();
		   setSelectedReceiveMoneyBg();
}
			   } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
			     && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
	    			LinearLayout  sendMoney = (LinearLayout) findViewById(R.id.moneygram_send_money);
	    			LinearLayout  receiveMoney = (LinearLayout) findViewById(R.id.moneygram_receive_money);
if(sendMoney.isShown()){
	 sendMoneyFlipper.setInAnimation(inFromLeftAnimation());
	    sendMoneyFlipper.setOutAnimation(outToRightAnimation());
	    sendMoneyFlipper.showPrevious();
	    dereaseSendMoneySelected();
				   setSelectedSendMoneyBg();
}else if(receiveMoney.isShown()){
	receiveMoneyFlipper.setInAnimation(inFromLeftAnimation());
	receiveMoneyFlipper.setOutAnimation(outToRightAnimation());
	receiveMoneyFlipper.showPrevious();
	dereaseReceiveMoneySelected();
		   setSelectedReceiveMoneyBg();
}
			   			   }
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