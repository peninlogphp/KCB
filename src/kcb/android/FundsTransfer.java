package kcb.android;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;

import kcb.android.R;
import lipuka.android.data.Bank;
import lipuka.android.model.MsisdnRegex;
import lipuka.android.model.Navigation;
import lipuka.android.model.responsehandlers.ConfirmSendMoneyHandler;
import lipuka.android.model.responsehandlers.FetchBeneficiaryCardMasksHandler;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.adapter.ComboBoxAdapter;
import lipuka.android.view.adapter.ContactsAutoCompleteCursorAdapter;
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
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;

public class FundsTransfer extends Activity implements OnClickListener, ResponseActivity{
	   
	Button submit, submitAnotherPersonNew, submitAnotherPersonSaved;
	EditText amount, amountAnotherPersonNew, amountAnotherPersonSaved;
	AutoCompleteTextView destinationMobileNo;
	EditText phoneAlias;
	CheckBox savePhone;
	
	Button submitCardSaved, submitCardNew;
	EditText amountCardSaved, amountCardNew;
	EditText destinationCard;
	EditText cardAlias;
	CheckBox saveCard;

	
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedSourceAccount, selectedSourceAccountAnotherPerson, selectedMyAccountDestinationAccount, 
	selectedAnotherPersonDestinationCard, selectedSourceOfFundsNew, selectedPurposeOfFundsNew, 
	selectedSourceOfFundsSaved, selectedPurposeOfFundsSaved, selectedNewDestinationCard,
	selectedSavedDestinationCard, selectedDestinationCardNumber;
int selectedSourceIndex, selectedDestinationIndex, selectedAnotherSourceIndex,
selectedCardIndex, selectedNewDestinationCardIndex, selectedSavedDestinationCardIndex,
selectedDestinationCardNumberIndex;
	String idTypeSelected, idTypeID, idTypePassport;

	LipukaApplication lipukaApplication;

	LipukaListItem[] sourceAccountsArray, sourceAccountsAnotherPersonArray, myAccountDestinationAccountsArray,
	anotherPersonDestinationCardsArray, sourceOfFundsArray, purposeOfFundsArray,
	newDestinationCardsArray, savedDestinationCardsArray, destinationCardNumbersArray;
	String amountStr, destinationStr;
	


	ViewFlipper flipper;
	GestureDetector gestureDetector;
	int selected;
	LinearLayout phoneNumber, cardNumber;
	TextView phoneNumberText, cardNumberText;
	
	int selectedBankPosition;
	LipukaListItem[] itemsArray;
	int bankCode, branchCode = -1;
	ArrayList<Bank> banksList;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.funds_transfer);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Send Money");
	        amount = (EditText) findViewById(R.id.to_my_account_amount_field);
	        amountAnotherPersonNew = (EditText) findViewById(R.id.new_card_amount_field);
	        amountAnotherPersonSaved = (EditText) findViewById(R.id.saved_card_amount_field);
	        
	        destinationMobileNo = (AutoCompleteTextView) findViewById(R.id.mobile_no_field);

	        Cursor c = lipukaApplication.getContactsAutoCompleteCursor();
	          ContactsAutoCompleteCursorAdapter contactsAdapter = new ContactsAutoCompleteCursorAdapter(this, c);
	          destinationMobileNo.setAdapter(contactsAdapter);
	          
	          destinationMobileNo.addTextChangedListener(new TextWatcher(){
	              public void afterTextChanged(Editable s) {
	            	  
	              }
	              public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	              public void onTextChanged(CharSequence s, int start, int before, int count){
	            	  selectedNewDestinationCard = null;
	            	  String mobileNoStr = destinationMobileNo.getText().toString();	
		    			boolean valid = true;
		    			StringBuffer errorBuffer = new StringBuffer();

	         			if(mobileNoStr == null || mobileNoStr.length() == 0){
	      				valid = false;
	      				errorBuffer.append("Mobile number is missing\n");
	      			}	else{
	      				mobileNoStr = lipukaApplication.ensureCountryCode(mobileNoStr);
	      			}
	      			if(mobileNoStr != null && mobileNoStr.length() == 12){
	      				if(!MsisdnRegex.isValidMsisdn(mobileNoStr)){
		      				valid = false;
		      				errorBuffer.append("Enter valid mobile number\n");
		      			}	
	      			}else{
	      				valid = false;
	      				
	      			}
	    				if(valid){
	        				lipukaApplication.putPayload("msisdn", mobileNoStr);
	    					lipukaApplication.consumeService("72", new FetchBeneficiaryCardMasksHandler(lipukaApplication, FundsTransfer.this, FetchBeneficiaryCardMasksHandler.NEW_CARD));
	    				}else{
	    					//Toast.makeText(FundsTransfer.this, errorBuffer.toString(), Toast.LENGTH_LONG).show(); 	    					
	    				}	/*  mobileNoChanged = true;
	            	  selectedNewDestinationCard = null;
	      	        Button submit = (Button) findViewById(R.id.new_card_submit);            	  
	              submit.setText("Load Cards");*/
	              }
	          }); 
	          phoneAlias = (EditText) findViewById(R.id.new_card_alias_field);
 savePhone = (CheckBox) findViewById(R.id.new_card_save);

	        
	       // accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountAnotherPersonNew.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountAnotherPersonSaved.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	         	        
	       
	        amountCardNew = (EditText) findViewById(R.id.new_card_number_amount_field);
	        amountCardSaved = (EditText) findViewById(R.id.saved_card_number_amount_field);
	        
	        destinationCard = (EditText) findViewById(R.id.new_card_destination_field);
cardAlias = (EditText) findViewById(R.id.new_card_number_alias_field);
 saveCard = (CheckBox) findViewById(R.id.new_card_number_save);

	        
 amountCardNew.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountCardSaved.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        
	        destinationCard.setInputType(InputType.TYPE_CLASS_NUMBER);		

	        final Spinner spinner = (Spinner) findViewById(R.id.to_my_account_source_account_spinner);
	        spinner.setOnItemSelectedListener(new OnSourceAccountSelectedListener());
	   	
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
			    	Log.d(Main.TAG, "creating destination accounts list error", ex);
		
		    	}
		   	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceAccountsArray);
		   	spinner.setAdapter(adapter);	
		   	if(salaryIndex != -1){
		   		spinner.setSelection(salaryIndex);
		   	}
	   	
	
	       final Spinner myAccountDestinationAccountSpinner = (Spinner) findViewById(R.id.to_my_account_destination_account_spinner);
	       myAccountDestinationAccountSpinner.setOnItemSelectedListener(new OnMyAccountDestinationAccountSelectedListener());
	        
	   	 ComboBoxAdapter myAccountDestinationAccountAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceAccountsArray);
	   	myAccountDestinationAccountSpinner.setAdapter(myAccountDestinationAccountAdapter);
	   	
		if(remittanceIndex != -1){
			myAccountDestinationAccountSpinner.setSelection(remittanceIndex);
	   	}
		
	    final Spinner sourceAccountsAnotherPersonSpinner = (Spinner) findViewById(R.id.to_another_person_source_account_spinner);
	    sourceAccountsAnotherPersonSpinner.setOnItemSelectedListener(new OnSourceAccountAnotherPersonSelectedListener());
        
   	 ComboBoxAdapter sourceAccountsAnotherPersonAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceAccountsArray);
   	sourceAccountsAnotherPersonSpinner.setAdapter(sourceAccountsAnotherPersonAdapter);
   	
   	final Spinner sourceOfFundsNewSpinner = (Spinner) findViewById(R.id.new_card_source_of_funds_spinner);
	   	sourceOfFundsNewSpinner.setOnItemSelectedListener(new OnSourceOfFundsNewSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.source_of_funds));
	         sourceOfFundsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	sourceOfFundsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating sourceOfFundsArray list error", ex);
	
	    	}
	   	 ComboBoxAdapter sourceOfFundNewAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceOfFundsArray);
	   	sourceOfFundsNewSpinner.setAdapter(sourceOfFundNewAdapter);
	   	
	       final Spinner sourceOfFundsSavedSpinner = (Spinner) findViewById(R.id.saved_card_source_of_funds_spinner);
	       sourceOfFundsSavedSpinner.setOnItemSelectedListener(new OnSourceOfFundsSavedSelectedListener());
	        
	   	 ComboBoxAdapter sourceOfFundSavedAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceOfFundsArray);
	   	sourceOfFundsSavedSpinner.setAdapter(sourceOfFundSavedAdapter);
      
	   	final Spinner purposeOfFundsNewSpinner = (Spinner) findViewById(R.id.new_card_purpose_of_funds_spinner);
	   	purposeOfFundsNewSpinner.setOnItemSelectedListener(new OnPurposeOfFundsNewSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.purpose_of_funds));
	         purposeOfFundsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	purposeOfFundsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating purposeOfFundsArray list error", ex);
	
	    	}
	   	 ComboBoxAdapter purposeOfFundNewAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, purposeOfFundsArray);
	   	purposeOfFundsNewSpinner.setAdapter(purposeOfFundNewAdapter);
	   	
	       final Spinner purposeOfFundsSavedSpinner = (Spinner) findViewById(R.id.saved_card_purpose_of_funds_spinner);
	       purposeOfFundsSavedSpinner.setOnItemSelectedListener(new OnPurposeOfFundsSavedSelectedListener());
	        
	   	 ComboBoxAdapter purposeOfFundSavedAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, purposeOfFundsArray);
	   	purposeOfFundsSavedSpinner.setAdapter(purposeOfFundSavedAdapter);
	   	
	   	final Spinner savedCardsSpinner = (Spinner) findViewById(R.id.saved_card_spinner);
	   	savedCardsSpinner.setOnItemSelectedListener(new OnSavedCardSelectedListener());
	        
	  try{      JSONArray sources = lipukaApplication.getProfileDataArray("card_to_card_beneficiaries");
	    if(sources != null){   
 anotherPersonDestinationCardsArray = new LipukaListItem[sources.length()+1];
 LipukaListItem lipukaListItem = new LipukaListItem("", 
	      "", "");
	        	anotherPersonDestinationCardsArray[0]= lipukaListItem;
      JSONObject currentSource;
        for(int i = 0; i < sources.length(); i++){
        	currentSource = sources.getJSONObject(i);
        	lipukaListItem = new LipukaListItem("", 
      currentSource.getString("alias"), currentSource.getString("phone_no"));
        	anotherPersonDestinationCardsArray[i+1]= lipukaListItem;
        }
	  }else{
		  anotherPersonDestinationCardsArray = new LipukaListItem[1];
LipukaListItem lipukaListItem = new LipukaListItem("", "None", "-1");
anotherPersonDestinationCardsArray[0]= lipukaListItem;        	
      }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating anotherPersonDestinationCardsArray list error", ex);
	
	    	}
	   	 ComboBoxAdapter savedCardsAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, anotherPersonDestinationCardsArray);
	   	savedCardsSpinner.setAdapter(savedCardsAdapter);
	   	
	   	final Spinner savedCardNumbersSpinner = (Spinner) findViewById(R.id.saved_card_number_spinner);
	   	savedCardNumbersSpinner.setOnItemSelectedListener(new OnSavedCardNumberSelectedListener());
	        
	  try{      JSONArray sources = lipukaApplication.getProfileDataArray("card_to_card_beneficiaries_ben");
	    if(sources != null){   
 destinationCardNumbersArray = new LipukaListItem[sources.length()];
      JSONObject currentSource;
        for(int i = 0; i < sources.length(); i++){
        	currentSource = sources.getJSONObject(i);
        	LipukaListItem lipukaListItem = new LipukaListItem("", 
      currentSource.getString("alias"), currentSource.getString("saved_card_beneficiary_id"));
        	destinationCardNumbersArray[i]= lipukaListItem;
        }
	  }else{
		  destinationCardNumbersArray = new LipukaListItem[1];
LipukaListItem lipukaListItem = new LipukaListItem("", "None", "-1");
destinationCardNumbersArray[0]= lipukaListItem;        	
      }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating destinationCardNumbersArray list error", ex);
	
	    	}
	   	 ComboBoxAdapter savedCardNumbersAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, destinationCardNumbersArray);
	   	savedCardNumbersSpinner.setAdapter(savedCardNumbersAdapter);

	   	submit = (Button) findViewById(R.id.to_my_account_submit);
	        submit.setOnClickListener(this);
	        submitAnotherPersonNew = (Button) findViewById(R.id.new_card_submit);
	        submitAnotherPersonNew.setOnClickListener(this);
	        submitAnotherPersonSaved = (Button) findViewById(R.id.saved_card_submit);
	        submitAnotherPersonSaved.setOnClickListener(this);
	        
	        submitCardNew = (Button) findViewById(R.id.new_card_number_submit);
	        submitCardNew.setOnClickListener(this);
	        submitCardSaved = (Button) findViewById(R.id.saved_card_number_submit);
	        submitCardSaved.setOnClickListener(this);

	        
	        Button showOrHidePhoneNumberSaved = (Button) findViewById(R.id.show_or_hide_phone_number_saved);
	        showOrHidePhoneNumberSaved.setOnClickListener(this);
	        Button showOrHidePhoneNumberNew = (Button) findViewById(R.id.show_or_hide_phone_number_new);
	        showOrHidePhoneNumberNew.setOnClickListener(this);
	        
	        Button showOrHideCardNumberSaved = (Button) findViewById(R.id.show_or_hide_card_number_saved);
	        showOrHideCardNumberSaved.setOnClickListener(this);
	        Button showOrHideCardNumberNew = (Button) findViewById(R.id.show_or_hide_card_number_new);
	        showOrHideCardNumberNew.setOnClickListener(this);

	        gestureDetector = new GestureDetector(this, new MyGestureDetector());
	        flipper = (ViewFlipper) findViewById(R.id.flipper);
	        
	        phoneNumber = (LinearLayout)findViewById(R.id.funds_transfer_phone_number);
	        cardNumber = (LinearLayout)findViewById(R.id.funds_transfer_card_number);
	        phoneNumberText = (TextView)findViewById(R.id.phone_number_text);
	        cardNumberText = (TextView)findViewById(R.id.card_number_text);
	        phoneNumberText.setOnClickListener(this);
	        cardNumberText.setOnClickListener(this);
	        
	       
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
	        if(lipukaApplication.getProfileID() == 0){
	        	Intent i = new Intent(this, StanChartHome.class);
	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);        	
	        }
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(FundsTransfer.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(FundsTransfer.class, false);
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
	    			String selectedSourceAccountID = null;
	    			String selectedDestinationAccountID = null;

	    			  try{      JSONArray sources = lipukaApplication.getProfileDataArray("accounts");
	 	    		 JSONObject currentSource = sources.getJSONObject(selectedDestinationIndex);
	 	    		selectedDestinationAccountID = currentSource.getString("account_id");
	 	    		
	   			        	if(!currentSource.getString("account_type").equals("Remittance Account")){
	   			        		valid = false;
	   		    				errorBuffer.append("You can only transfer funds from your Salary Account to your Remittance Account\n");
	   			        	}
	   			        	
	   			        	currentSource = sources.getJSONObject(selectedSourceIndex);
	   		 	    		selectedSourceAccountID = currentSource.getString("account_id");

	   			        	if(valid || !currentSource.getString("account_type").equals("Salary Account")){
	   			        		valid = false;
	   		    				errorBuffer.append("You can only transfer funds from your Salary Account to your Remittance Account\n");
	   			        	}
	 	    			        }catch(Exception ex){
	 	    				    	Log.d(Main.TAG, "error", ex);
	 	    			
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
	    		        				lipukaApplication.putPayload("source_account_id", selectedSourceAccountID);
	    		        				lipukaApplication.putPayload("destination_account_id", selectedDestinationAccountID);
	    		        				lipukaApplication.putPayload("amount", amountStr);

	    		    	    			lipukaApplication.setServiceID("41");

	    		    	    			lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
	    			    			    		  " to your "+selectedMyAccountDestinationAccount+" from your "+selectedSourceAccount+". Press \"OK\" to send now or \"Cancel\" to edit details.");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
	    			    			this.amountStr = "You have successfully transferred KES "+amountStr+
		    			    		  " to your "+selectedMyAccountDestinationAccount+" from your "+selectedSourceAccount+". Thank you.";
destinationStr  = selectedMyAccountDestinationAccount;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitAnotherPersonNew == arg0){
	  		    lipukaApplication.clearPayloadObject();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String destinationCardStr = destinationMobileNo.getText().toString();
	    			String amountStr = amountAnotherPersonNew.getText().toString();
	    			String cardAliasStr = phoneAlias.getText().toString();
	    			
	    			if(selectedNewDestinationCard == null || selectedNewDestinationCard.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Destination card number is missing\n");
	    			}
	    			if(amountStr == null || amountStr.length() == 0){
			    				valid = false;
			    				errorBuffer.append("Amount is missing\n");
			    			}
	    			if(savePhone.isChecked()){

			    	   	if(cardAliasStr == null || cardAliasStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Recipient alias is missing\n");
		    			}    		
			    	}   
	    						if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();

    			    	   				payloadBuffer.append("Other|");
    			    	   				 				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				
	    			    				lipukaApplication.putPayload("source_account_id", sourceAccountsArray[selectedAnotherSourceIndex].getValue());
	    		        				lipukaApplication.putPayload("destination_account_id", newDestinationCardsArray[selectedNewDestinationCardIndex].getValue());
	    		        				lipukaApplication.putPayload("amount", amountStr);
	    		        				lipukaApplication.putPayload("source_of_funds", selectedSourceOfFundsNew);
	    		        				lipukaApplication.putPayload("purpose_of_funds", selectedPurposeOfFundsNew);
	    		        				if(savePhone.isChecked()){
		    		        				lipukaApplication.putPayload("add_card", "1"); 		
	    		    			    	} else{
		    		        				lipukaApplication.putPayload("add_card", "0"); 			    		    			    		
	    		    			    	}
	    		        				lipukaApplication.putPayload("card_alias", cardAliasStr);

	    		    	    			lipukaApplication.setServiceID("73");
		
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    				lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
	    			    			    		  " to "+destinationCardStr+" from "+selectedSourceAccountAnotherPerson+". Press \"OK\" to send now or \"Cancel\" to edit details.");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
	    			    			this.amountStr = "You have successfully transferred KES "+amountStr+
		    			    		  " to card "+destinationCardStr+" from your "+selectedSourceAccountAnotherPerson+". Thank you.";
destinationStr  = selectedMyAccountDestinationAccount;
}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitAnotherPersonSaved == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String amountStr = amountAnotherPersonSaved.getText().toString();

	    			if(anotherPersonDestinationCardsArray[selectedCardIndex].getValue().equals("-1")){
	    				valid = false;
	    				errorBuffer.append("You do not have any saved cards to select\n");
	    			}
	    			if(valid && (selectedSavedDestinationCard == null || selectedSavedDestinationCard.length() == 0)){
	    				valid = false;
	    				errorBuffer.append("Destination card number is missing\n");
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
	    			    				
	    			    				lipukaApplication.putPayload("source_account_id", sourceAccountsArray[selectedAnotherSourceIndex].getValue());
	    		        				lipukaApplication.putPayload("destination_account_id", savedDestinationCardsArray[selectedSavedDestinationCardIndex].getValue());
	    		        				lipukaApplication.putPayload("amount", amountStr);
	    		        				lipukaApplication.putPayload("source_of_funds", selectedSourceOfFundsSaved);
	    		        				lipukaApplication.putPayload("purpose_of_funds", selectedPurposeOfFundsSaved);
	    		        				

	    		    	    			lipukaApplication.setServiceID("73");
	    		    	    			
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    				lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
	    			    			    		  " to "+selectedAnotherPersonDestinationCard+" from "+selectedSourceAccountAnotherPerson+". Press \"OK\" to send now or \"Cancel\" to edit send details.");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
	    			    			this.amountStr = "You have successfully transferred KES "+amountStr+
		    			    		  " to "+selectedAnotherPersonDestinationCard+" from your "+selectedSourceAccountAnotherPerson+". Thank you.";
destinationStr  = selectedMyAccountDestinationAccount;
}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitCardNew == arg0){
	  		    lipukaApplication.clearPayloadObject();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String destinationCardStr = destinationCard.getText().toString();
	    			String amountStr = amountCardNew.getText().toString();
	    			String cardAliasStr = cardAlias.getText().toString();
	    			
	    			if(destinationCardStr == null || destinationCardStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Destination card number is missing\n");
	    			}
	    			if(amountStr == null || amountStr.length() == 0){
			    				valid = false;
			    				errorBuffer.append("Amount is missing\n");
			    			}
	    			if(saveCard.isChecked()){

			    	   	if(cardAliasStr == null || cardAliasStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Recipient alias is missing\n");
		    			}    		
			    	}   
	    						if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();

    			    	   				payloadBuffer.append("Other|");
    			    	   				 				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				
	    			    				lipukaApplication.putPayload("source_account_id", sourceAccountsArray[selectedAnotherSourceIndex].getValue());
	    		        				lipukaApplication.putPayload("destination_card_no", destinationCardStr);
	    		        				lipukaApplication.putPayload("amount", amountStr);
	    		        				lipukaApplication.putPayload("source_of_funds", selectedSourceOfFundsNew);
	    		        				lipukaApplication.putPayload("purpose_of_funds", selectedPurposeOfFundsNew);
	    		        				if(saveCard.isChecked()){
		    		        				lipukaApplication.putPayload("add_card", "1"); 		
	    		    			    	} else{
		    		        				lipukaApplication.putPayload("add_card", "0"); 			    		    			    		
	    		    			    	}
	    		        				lipukaApplication.putPayload("card_alias", cardAliasStr);

	    		    	    			lipukaApplication.setServiceID("81");
		
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    				lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
	    			    			    		  " to card "+destinationCardStr+" from "+selectedSourceAccountAnotherPerson+". Press \"OK\" to send now or \"Cancel\" to edit details.");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
	    			    			this.amountStr = "You have successfully transferred KES "+amountStr+
		    			    		  " to card "+destinationCardStr+" from your "+selectedSourceAccountAnotherPerson+". Thank you.";
destinationStr  = selectedMyAccountDestinationAccount;
}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitCardSaved == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String amountStr = amountCardSaved.getText().toString();

	    			if(destinationCardNumbersArray[selectedDestinationCardNumberIndex].getValue().equals("-1")){
	    				valid = false;
	    				errorBuffer.append("You do not have any saved cards to select\n");
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
	    			    				
	    			    				lipukaApplication.putPayload("source_account_id", sourceAccountsArray[selectedAnotherSourceIndex].getValue());
	    		        				lipukaApplication.putPayload("destination_beneficiary_id", destinationCardNumbersArray[selectedDestinationCardNumberIndex].getValue());
	    		        				lipukaApplication.putPayload("amount", amountStr);
	    		        				lipukaApplication.putPayload("source_of_funds", selectedSourceOfFundsSaved);
	    		        				lipukaApplication.putPayload("purpose_of_funds", selectedPurposeOfFundsSaved);
	    		        				

	    		    	    			lipukaApplication.setServiceID("82");
	    		    	    			
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    				lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
	    			    			    		  " to "+destinationCardNumbersArray[selectedDestinationCardNumberIndex].getText()+" from "+selectedSourceAccountAnotherPerson+". Press \"OK\" to send now or \"Cancel\" to edit details.");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
	    			    			this.amountStr = "You have successfully transferred KES "+amountStr+
		    			    		  " to "+selectedAnotherPersonDestinationCard+" from your "+selectedSourceAccountAnotherPerson+". Thank you.";
destinationStr  = selectedMyAccountDestinationAccount;
}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if(arg0.getId() == R.id.phone_number_text){
	    			 if (selected == 1) {
		     			    flipper.setInAnimation(inFromLeftAnimation());
		     			    flipper.setOutAnimation(outToRightAnimation());
		     			    flipper.setDisplayedChild(0);
		     			    selected = 0;
		     		   setSelectedBg();
		     			   }else{
		     				   return;
		     			   }			   
		     	}else if(arg0.getId() == R.id.card_number_text){
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_phone_number_saved){
	    	    		LinearLayout  phoneNumberSaved = (LinearLayout) findViewById(R.id.funds_transfer_saved_phone_number);
	    	    		Drawable img = null;
		if(phoneNumberSaved.isShown()){
			//myAccount.setVisibility(View.GONE);
			ExpandAnimation.collapse(phoneNumberSaved);
			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//myAccount.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(phoneNumberSaved);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  phoneNumberNew = (LinearLayout) findViewById(R.id.funds_transfer_new_phone_number);
		    	    		phoneNumberNew.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHidePhoneNumberNew = (Button) findViewById(R.id.show_or_hide_phone_number_new);
	    	    	        showOrHidePhoneNumberNew.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_phone_number_new){
		    	    		LinearLayout  phoneNumberNew = (LinearLayout) findViewById(R.id.funds_transfer_new_phone_number);
		    	    		Drawable img = null;
			if(phoneNumberNew.isShown()){
				//anotherPerson.setVisibility(View.GONE);
				ExpandAnimation.collapse(phoneNumberNew);
				img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//anotherPerson.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(phoneNumberNew);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  phoneNumberSaved = (LinearLayout) findViewById(R.id.funds_transfer_saved_phone_number);
			    	    		phoneNumberSaved.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHidePhoneNumberSaved = (Button) findViewById(R.id.show_or_hide_phone_number_saved);
		    	    	        showOrHidePhoneNumberSaved.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

			    	    	}else if (arg0.getId() == R.id.show_or_hide_card_number_saved){
			    	    		LinearLayout  cardNumberSaved = (LinearLayout) findViewById(R.id.funds_transfer_saved_card_number);
			    	    		Drawable img = null;
				if(cardNumberSaved.isShown()){
					//myAccount.setVisibility(View.GONE);
					ExpandAnimation.collapse(cardNumberSaved);
					img = getResources().getDrawable( R.drawable.show );
			    	    		}else{
			    	    			//myAccount.setVisibility(View.VISIBLE);
			    	    			ExpandAnimation.expand(cardNumberSaved);
			    	    			img = getResources().getDrawable( R.drawable.hide );
				    	    		LinearLayout  cardNumberNew = (LinearLayout) findViewById(R.id.funds_transfer_new_card_number);
				    	    		cardNumberNew.setVisibility(View.GONE);
				    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
			    	    	        Button showOrHideCardNumberNew = (Button) findViewById(R.id.show_or_hide_card_number_new);
			    	    	        showOrHideCardNumberNew.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

			    	    		}
				((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

				    	    	}else if (arg0.getId() == R.id.show_or_hide_card_number_new){
				    	    		LinearLayout  cardNumberNew = (LinearLayout) findViewById(R.id.funds_transfer_new_card_number);
				    	    		Drawable img = null;
					if(cardNumberNew.isShown()){
						//anotherPerson.setVisibility(View.GONE);
						ExpandAnimation.collapse(cardNumberNew);
						img = getResources().getDrawable( R.drawable.show );
				    	    		}else{
				    	    			//anotherPerson.setVisibility(View.VISIBLE);
				    	    			ExpandAnimation.expand(cardNumberNew);
				    	    			img = getResources().getDrawable( R.drawable.hide );
				    	    			LinearLayout  cardNumberSaved = (LinearLayout) findViewById(R.id.funds_transfer_saved_card_number);
					    	    		cardNumberSaved.setVisibility(View.GONE);
					    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
				    	    	        Button showOrHideCardNumberSaved = (Button) findViewById(R.id.show_or_hide_card_number_new);
				    	    	        showOrHideCardNumberSaved.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
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
	    		public class OnMyAccountDestinationAccountSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedMyAccountDestinationAccount = sourceAccountsArray[pos].getText();
		 	        	selectedDestinationIndex = pos;
}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnSourceAccountAnotherPersonSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSourceAccountAnotherPerson = sourceAccountsArray[pos].getText();
		 	        	if(selectedSourceAccountAnotherPerson.equals("Salary Account")){
		 	        	   	final Spinner sourceOfFundsNewSpinner = (Spinner) findViewById(R.id.new_card_source_of_funds_spinner);
   		sourceOfFundsNewSpinner.setSelection(0);
   		final Spinner sourceOfFundsSavedSpinner = (Spinner) findViewById(R.id.saved_card_source_of_funds_spinner);
   		sourceOfFundsSavedSpinner.setSelection(0);
		 	        	}
		 	        	selectedAnotherSourceIndex = pos;	
		 	        }
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnSavedCardSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedAnotherPersonDestinationCard = anotherPersonDestinationCardsArray[pos].getText();
		 	        	  selectedSavedDestinationCard = null;
  	if(selectedCardIndex != pos && pos != 0){
	        				lipukaApplication.putPayload("msisdn", anotherPersonDestinationCardsArray[pos].getValue());
			    				lipukaApplication.consumeService("72", new FetchBeneficiaryCardMasksHandler(lipukaApplication, FundsTransfer.this, FetchBeneficiaryCardMasksHandler.SAVED_CARD));

		 	        	}
		 	        	selectedCardIndex = pos;
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnNewDestinationCardSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedNewDestinationCard = newDestinationCardsArray[pos].getText();
		 	        	selectedNewDestinationCardIndex = pos;
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnSavedDestinationCardSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSavedDestinationCard = savedDestinationCardsArray[pos].getText();
		 	        	selectedSavedDestinationCardIndex = pos;
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }	
	    		public class OnSourceOfFundsNewSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSourceOfFundsNew = sourceOfFundsArray[pos].getValue();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnSourceOfFundsSavedSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSourceOfFundsSaved = sourceOfFundsArray[pos].getValue();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnPurposeOfFundsNewSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedPurposeOfFundsNew = purposeOfFundsArray[pos].getValue();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnPurposeOfFundsSavedSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedPurposeOfFundsSaved = purposeOfFundsArray[pos].getValue();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnSavedCardNumberSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedDestinationCardNumber = destinationCardNumbersArray[pos].getText();
		 	        	selectedDestinationCardNumberIndex = pos;
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
		      lipukaApplication.setCurrentDialogMsg(amountStr);
		      lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
		      showDialog(Main.DIALOG_SERVICE_RESPONSE_ID);
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
    	  phoneNumberText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
    	  cardNumberText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	         break;
    	     case 1:
    	    	  phoneNumberText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	    	  cardNumberText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
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

	public void populateNewCardDestinationCardsList(JSONArray sources){
		final Spinner destinationCardsSpinner = (Spinner) findViewById(R.id.destination_card_new_spinner);
	   	destinationCardsSpinner.setOnItemSelectedListener(new OnNewDestinationCardSelectedListener());
	        
	  try{     
	    if(sources != null){   
 newDestinationCardsArray = new LipukaListItem[sources.length()];
      JSONObject currentSource;
        for(int i = 0; i < sources.length(); i++){
        	currentSource = sources.getJSONObject(i);
        	LipukaListItem lipukaListItem = new LipukaListItem("", 
      currentSource.getString("account_mask"), currentSource.getString("accounts_id"));
        	newDestinationCardsArray[i]= lipukaListItem;
        }
	  }else{
		  newDestinationCardsArray = new LipukaListItem[1];
LipukaListItem lipukaListItem = new LipukaListItem("", "None", "-1");
newDestinationCardsArray[0]= lipukaListItem;        	
      }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating newDestinationCardsArray list error", ex);
	
	    	}
	   	 ComboBoxAdapter destinationCardsAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, newDestinationCardsArray);
	   	destinationCardsSpinner.setAdapter(destinationCardsAdapter);
		final LinearLayout destinationCardsLayout = (LinearLayout) findViewById(R.id.destination_card_new_layout);
		destinationCardsLayout.setVisibility(View.VISIBLE);
	}
	public void populateSavedCardDestinationCardsList(JSONArray sources){
		final Spinner destinationCardsSpinner = (Spinner) findViewById(R.id.destination_card_saved_spinner);
	   	destinationCardsSpinner.setOnItemSelectedListener(new OnSavedDestinationCardSelectedListener());
	        
	  try{     
	    if(sources != null){   
 savedDestinationCardsArray = new LipukaListItem[sources.length()];
      JSONObject currentSource;
        for(int i = 0; i < sources.length(); i++){
        	currentSource = sources.getJSONObject(i);
        	LipukaListItem lipukaListItem = new LipukaListItem("", 
      currentSource.getString("account_mask"), currentSource.getString("accounts_id"));
        	savedDestinationCardsArray[i]= lipukaListItem;
        }
	  }else{
		  savedDestinationCardsArray = new LipukaListItem[1];
LipukaListItem lipukaListItem = new LipukaListItem("", "None", "-1");
savedDestinationCardsArray[0]= lipukaListItem;        	
      }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating savedDestinationCardsArray list error", ex);
	
	    	}
	   	 ComboBoxAdapter destinationCardsAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, savedDestinationCardsArray);
	   	destinationCardsSpinner.setAdapter(destinationCardsAdapter);
	   	final LinearLayout destinationCardsLayout = (LinearLayout) findViewById(R.id.destination_card_saved_layout);
	   	destinationCardsLayout.setVisibility(View.VISIBLE);
	}
	}