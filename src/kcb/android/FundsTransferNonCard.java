package kcb.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;

import kcb.android.R;
import lipuka.android.data.Bank;
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

public class FundsTransferNonCard extends Activity implements OnClickListener, ResponseActivity{
	   
	Button submit, submitExternalSaved, submitExternalDestination,
	submitInternalSaved, submitInternalDestination;
	EditText amount, amountExternalSaved, amountExternalDestination,
	amountInternalSaved, amountInternalDestination;
	EditText externalAccountNo, externalAccountName, externalAccountAlias;
	EditText internalAccountNo, internalAccountName, internalAccountAlias;
	CheckBox externalSaveAccount, internalSaveAccount;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedSourceAccount, selectedMyAccountDestinationAccount, 
	selectedInternalSavedAccount, selectedExternalSavedAccount, selectedSavedCurrency,
	selectedDestinationCurrency;

	String idTypeSelected, idTypeID, idTypePassport;

	LipukaApplication lipukaApplication;
	String selectedSourceOfFunds, selectedPurposeOfFunds, selectedCountry;
	LipukaListItem[] sourceAccountsArray, myAccountDestinationAccountsArray,
	internalSavedAccountsArray, externalSavedAccountsArray, countriesArray,
	currenciesArray;
	String amountStr, destinationStr;
	


	ViewFlipper flipper;
	GestureDetector gestureDetector;
	int selected;
	LinearLayout internalFT, externalFT;
	TextView internalFTText, externalFTText;
	
	int selectedBankPosition;
	LipukaListItem[] itemsArray;
	int bankCode, branchCode = -1;
	ArrayList<Bank> banksList;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.ft_noncard);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Funds Transfer");
	        amount = (EditText) findViewById(R.id.to_my_account_amount_field);
	        amountExternalSaved = (EditText) findViewById(R.id.external_saved_amount_field);
	        amountExternalDestination = (EditText) findViewById(R.id.external_destination_amount_field);
	        amountInternalSaved = (EditText) findViewById(R.id.internal_saved_amount_field);
	        amountInternalDestination = (EditText) findViewById(R.id.internal_destination_amount_field);
	        
	        externalAccountNo = (EditText) findViewById(R.id.external_destination_account_field);
	        internalAccountNo = (EditText) findViewById(R.id.internal_destination_account_field);
	        externalAccountName = (EditText) findViewById(R.id.external_account_name_field);
	        internalAccountName = (EditText) findViewById(R.id.internal_account_name_field);
 externalAccountAlias = (EditText) findViewById(R.id.external_destination_alias_field);
 internalAccountAlias = (EditText) findViewById(R.id.internal_destination_alias_field);
 externalSaveAccount = (CheckBox) findViewById(R.id.external_save_account);
 internalSaveAccount = (CheckBox) findViewById(R.id.internal_save_account);

	        
	       // accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountExternalSaved.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountExternalDestination.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountInternalSaved.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountInternalDestination.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        
	        externalAccountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        internalAccountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
 	        
	       
	       final Spinner spinner = (Spinner) findViewById(R.id.source_account_spinner);
	        spinner.setOnItemSelectedListener(new OnSourceAccountSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.transactional_accounts));
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
	   	
	
	       final Spinner myAccountDestinationAccountSpinner = (Spinner) findViewById(R.id.to_my_account_destination_account_spinner);
	       myAccountDestinationAccountSpinner.setOnItemSelectedListener(new OnMyAccountDestinationAccountSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.transactional_accounts));
	         myAccountDestinationAccountsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	myAccountDestinationAccountsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating destination accounts list error", ex);
	
	    	}
	   	 ComboBoxAdapter myAccountDestinationAccountAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, myAccountDestinationAccountsArray);
	   	myAccountDestinationAccountSpinner.setAdapter(myAccountDestinationAccountAdapter);
	   	
	    final Spinner externalSavedAccountSpinner = (Spinner) findViewById(R.id.external_saved_account_spinner);
	    externalSavedAccountSpinner.setOnItemSelectedListener(new OnExternalSavedAccountSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSavedAcounts());
	         externalSavedAccountsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	externalSavedAccountsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating saved accounts list error", ex);
	
	    	}
	   	 ComboBoxAdapter externalSavedAccountAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, externalSavedAccountsArray);
	   	externalSavedAccountSpinner.setAdapter(externalSavedAccountAdapter);
	   	
	   	final Spinner internalSavedAccountSpinner = (Spinner) findViewById(R.id.internal_saved_account_spinner);
	    internalSavedAccountSpinner.setOnItemSelectedListener(new OnInternalSavedAccountSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSavedAcounts());
	         internalSavedAccountsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	internalSavedAccountsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating saved accounts list error", ex);
	
	    	}
	   	 ComboBoxAdapter internalSavedAccountAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, internalSavedAccountsArray);
	   	internalSavedAccountSpinner.setAdapter(internalSavedAccountAdapter);
	   	
		final Spinner countrySpinner = (Spinner) findViewById(R.id.country_spinner);
		countrySpinner.setOnItemSelectedListener(new OnCountrySelectedListener());
	     
	    try{      JSONArray sources = new JSONArray(lipukaApplication.loadCountries());
	    countriesArray = new LipukaListItem[sources.length()];
     JSONObject currentSource;
       for(int i = 0; i < sources.length(); i++){
       	currentSource = sources.getJSONObject(i);
       	LipukaListItem lipukaListItem = new LipukaListItem("", 
     currentSource.getString("name"), currentSource.getString("value"));
       	countriesArray[i]= lipukaListItem;   	
       }
       }catch(Exception ex){
	    	Log.d(Main.TAG, "creating countries list error", ex);

   	}
  	 ComboBoxAdapter countriesAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, countriesArray);
  	countrySpinner.setAdapter(countriesAdapter);
  	
  	 final Spinner savedCurrencySpinner = (Spinner) findViewById(R.id.saved_currency_spinner);
     savedCurrencySpinner.setOnItemSelectedListener(new OnSavedCurrencySelectedListener());
      
try{      JSONArray sources = new JSONArray(lipukaApplication.loadEftCurrencies());
       currenciesArray = new LipukaListItem[sources.length()];
    JSONObject currentSource;
      for(int i = 0; i < sources.length(); i++){
      	currentSource = sources.getJSONObject(i);
      	LipukaListItem lipukaListItem = new LipukaListItem("", 
    currentSource.getString("name"), currentSource.getString("value"));
      	currenciesArray[i]= lipukaListItem;   	
      }
      }catch(Exception ex){
	    	Log.d(Main.TAG, "creating currencies list error", ex);

  	}
 	 ComboBoxAdapter savedCurrencyAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, currenciesArray);
 	savedCurrencySpinner.setAdapter(savedCurrencyAdapter);
 	
  final Spinner destinationCurrencySpinner = (Spinner) findViewById(R.id.destination_currency_spinner);
  destinationCurrencySpinner.setOnItemSelectedListener(new OnSavedCurrencySelectedListener());
      
 	 ComboBoxAdapter destinationCurrencyAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, currenciesArray);
 	destinationCurrencySpinner.setAdapter(destinationCurrencyAdapter);
  	
	   	submit = (Button) findViewById(R.id.to_my_account_submit);
	        submit.setOnClickListener(this);
	        submitInternalSaved = (Button) findViewById(R.id.internal_saved_submit);
	        submitInternalSaved.setOnClickListener(this);
	        submitInternalDestination = (Button) findViewById(R.id.internal_destination_submit);
	        submitInternalDestination.setOnClickListener(this);
	        submitExternalSaved = (Button) findViewById(R.id.external_saved_submit);
	        submitExternalSaved.setOnClickListener(this);
	        submitExternalDestination = (Button) findViewById(R.id.external_destination_submit);
	        submitExternalDestination.setOnClickListener(this);
	        
	        Button showOrHideMyAccount = (Button) findViewById(R.id.show_or_hide_my_account);
	        showOrHideMyAccount.setOnClickListener(this);
	        Button showOrHideAnotherPerson = (Button) findViewById(R.id.show_or_hide_another_person);
	        showOrHideAnotherPerson.setOnClickListener(this);
	        
	        Button showOrHideInternalSaved = (Button) findViewById(R.id.show_or_hide_internal_saved);
	        showOrHideInternalSaved.setOnClickListener(this);
	        Button showOrHideInternalDestination = (Button) findViewById(R.id.show_or_hide_internal_destination);
	        showOrHideInternalDestination.setOnClickListener(this);
	        
	        Button showOrHideExternalSaved = (Button) findViewById(R.id.show_or_hide_external_saved);
	        showOrHideExternalSaved.setOnClickListener(this);
	        Button showOrHideExternalDestination = (Button) findViewById(R.id.show_or_hide_external_destination);
	        showOrHideExternalDestination.setOnClickListener(this);
	   	
	        gestureDetector = new GestureDetector(this, new MyGestureDetector());
	        flipper = (ViewFlipper) findViewById(R.id.flipper);
	        
	        internalFT = (LinearLayout)findViewById(R.id.funds_transfer_internal);
	        externalFT = (LinearLayout)findViewById(R.id.funds_transfer_external);
	        internalFTText = (TextView)findViewById(R.id.internal_ft_text);
	        externalFTText = (TextView)findViewById(R.id.external_ft_text);
	        internalFTText.setOnClickListener(this);
	        externalFTText.setOnClickListener(this);
	        
	        Spinner bankSpinner = (Spinner) findViewById(R.id.bank_spinner);
	        bankSpinner.setOnItemSelectedListener(new OnBankSelectedListener());
	        banksList = new ArrayList<Bank>();     
	  try{      JSONObject eftData = new JSONObject(lipukaApplication.loadEftData());
	        JSONArray banks = eftData.getJSONArray("banks");
	         itemsArray = new LipukaListItem[banks.length()];
	      JSONObject currentBank;
	        for(int i = 0; i < banks.length(); i++){
	        	currentBank = banks.getJSONObject(i);
	        	banksList.add(new Bank(currentBank.getInt("bank_code"), currentBank.getString("bank_name"), 
	        			currentBank.getJSONArray("branches")));
	        }
	        Collections.sort( banksList );
	        Bank currentBankObj = null;
	        for(int i = 0; i < banks.length(); i++){
	        	currentBankObj = banksList.get(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	        			currentBankObj.getName(), String.valueOf(currentBankObj.getCode()));
	        	itemsArray[i]= lipukaListItem;  
	        }
	   	 ComboBoxAdapter bankAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, itemsArray);
	   	bankSpinner.setAdapter(bankAdapter);
	   	
AutoCompleteTextView branches = (AutoCompleteTextView) findViewById(R.id.branch_field);
branches.setHint("branch name");

branches.setOnItemClickListener(new AdapterView.OnItemClickListener() { 

@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        long arg3) {

		try{   
    Bank selectedBank = banksList.get(selectedBankPosition);
    JSONArray branches = selectedBank.getBranches();
    JSONObject selectedBranch = branches.getJSONObject(arg2);
branchCode  = selectedBranch.getInt("branch_code"); 
}catch(Exception ex){
	Log.d(Main.TAG, "setting branches error", ex);

}

}
});
	  }catch(Exception ex){
	    	Log.d(Main.TAG, "creating bank n branches transfer funds error", ex);

  	}
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
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
	    			    			    		  " to "+selectedMyAccountDestinationAccount+". Press \"OK\" to transfer now or \"Cancel\" to edit transfer details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/	
	    			    				showResponse();
	    			    			this.amountStr = amountStr;
destinationStr  = selectedMyAccountDestinationAccount;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitInternalSaved == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String amountStr = amountInternalSaved.getText().toString();
	    			
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
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
	    			    			    		  " to "+selectedInternalSavedAccount+". Press \"OK\" to transfer now or \"Cancel\" to edit transfer details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);		*/
	    			    				showResponse();
	    			    			this.amountStr = amountStr;
	    			    			destinationStr  = selectedInternalSavedAccount;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitInternalDestination == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String amountStr = amountInternalDestination.getText().toString();
	    			String internalAccountNoStr = internalAccountNo.getText().toString();	
	    			String internalAccountNameStr = internalAccountName.getText().toString();	
	    			String accountAliasStr = internalAccountAlias.getText().toString();	
	    			
	    			if(internalAccountNoStr == null || internalAccountNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Account number is missing\n");
	    			}
	    			
	    			if(internalAccountNameStr == null || internalAccountNameStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Account name is missing\n");
	    			}
	    			
			    			if(amountStr == null || amountStr.length() == 0){
			    				valid = false;
			    				errorBuffer.append("Amount is missing\n");
			    			}
	    			    			
	    			
	    			    	if(internalSaveAccount.isChecked()){

	    			    	   	if(accountAliasStr == null || accountAliasStr.length() == 0){
	    		    				valid = false;
	    		    				errorBuffer.append("Account alias is missing\n");
	    		    			}    		
	    			    	}
	    						if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
    			    	   				payloadBuffer.append("Other|");
    			    	   				 				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				/*lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
	    			    			    		  " to "+internalAccountNoStr+". Press \"OK\" to transfer now or \"Cancel\" to edit transfer details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);		*/
	    			    				showResponse();
	    			    			this.amountStr = amountStr;
	    			    			destinationStr  = internalAccountNoStr;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitExternalSaved == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	  			
    			boolean valid = true;
    			StringBuffer errorBuffer = new StringBuffer();
    			String amountStr = amountExternalSaved.getText().toString();
    			
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
    			    			      lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
    			    			    		  " to "+selectedExternalSavedAccount+". Press \"OK\" to transfer now or \"Cancel\" to edit transfer details");
    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/	
    			    				showResponse();
    			    			this.amountStr = amountStr;
    			    			destinationStr  = selectedExternalSavedAccount;
    			    			}else{
    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    			    			}		

	    		}else if (submitExternalDestination == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	  			boolean valid = true;
    			StringBuffer errorBuffer = new StringBuffer();
    			String amountStr = amountExternalDestination.getText().toString();
    			String externalAccountNoStr = externalAccountNo.getText().toString();	
    			String externalAccountNameStr = externalAccountName.getText().toString();	
    			String accountAliasStr = externalAccountAlias.getText().toString();	
    			
    			if(externalAccountNoStr == null || externalAccountNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Account number is missing\n");
    			}
    			
    			if(externalAccountNameStr == null || externalAccountNameStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Account name is missing\n");
    			}
    			if(branchCode == -1){
    				valid = false;
    				errorBuffer.append("Branch is missing\n");
    			}	
		    			if(amountStr == null || amountStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Amount is missing\n");
		    			}
    			    			
    			
    			    	if(externalSaveAccount.isChecked()){

    			    	   	if(accountAliasStr == null || accountAliasStr.length() == 0){
    		    				valid = false;
    		    				errorBuffer.append("Account alias is missing\n");
    		    			}    		
    			    	}
    						if(valid){
    			    				StringBuffer payloadBuffer = new StringBuffer();
			    	   				payloadBuffer.append("Other|");
			    	   				 				
    			    	   			Navigation nav = new Navigation();
    			    			    nav.setPayload(payloadBuffer.toString());
    			    				lipukaApplication.pushNavigationStack(nav);
    			    				lipukaApplication.setPin(null);
    			    				/*lipukaApplication.setCurrentDialogTitle("Confirm");
    			    			      lipukaApplication.setCurrentDialogMsg("You are about to transfer KES "+amountStr+
    			    			    		  " to "+externalAccountNoStr+". Press \"OK\" to transfer now or \"Cancel\" to edit transfer details");
    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/
    			    				showResponse();
    			    			this.amountStr = amountStr;
    			    			destinationStr  = externalAccountNoStr;
    			    			}else{
    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    			    			}	

	    		}else if(arg0.getId() == R.id.internal_ft_text){
	    			 if (selected == 1) {
		     			    flipper.setInAnimation(inFromLeftAnimation());
		     			    flipper.setOutAnimation(outToRightAnimation());
		     			    flipper.setDisplayedChild(0);
		     			    selected = 0;
		     		   setSelectedBg();
		     			   }else{
		     				   return;
		     			   }			   
		     	}else if(arg0.getId() == R.id.external_ft_text){
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_my_account){
	    	    		LinearLayout  myAccount = (LinearLayout) findViewById(R.id.to_my_account);
	    	    		Drawable img = null;
		if(myAccount.isShown()){
			//myAccount.setVisibility(View.GONE);
			ExpandAnimation.collapse(myAccount);
			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//myAccount.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(myAccount);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  anotherPerson = (LinearLayout) findViewById(R.id.to_another_person);
		    	    		anotherPerson.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideAnotherPerson = (Button) findViewById(R.id.show_or_hide_another_person);
	    	    	        showOrHideAnotherPerson.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_another_person){
		    	    		LinearLayout  anotherPerson = (LinearLayout) findViewById(R.id.to_another_person);
		    	    		Drawable img = null;
			if(anotherPerson.isShown()){
				//anotherPerson.setVisibility(View.GONE);
				ExpandAnimation.collapse(anotherPerson);
				img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//anotherPerson.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(anotherPerson);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  myAccount = (LinearLayout) findViewById(R.id.to_my_account);
			    	    		myAccount.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideMyAccount = (Button) findViewById(R.id.show_or_hide_my_account);
		    	    	        showOrHideMyAccount.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

			    	    	}else if (arg0.getId() == R.id.show_or_hide_internal_saved){
			    	    		LinearLayout  internalSaved = (LinearLayout) findViewById(R.id.funds_transfer_internal_saved);
			    	    		Drawable img = null;
				if(internalSaved.isShown()){
					//internalSaved.setVisibility(View.GONE);
					ExpandAnimation.collapse(internalSaved);
					img = getResources().getDrawable( R.drawable.show );
			    	    		}else{
			    	    			//internalSaved.setVisibility(View.VISIBLE);
			    	    			ExpandAnimation.expand(internalSaved);
			    	    			img = getResources().getDrawable( R.drawable.hide );
				    	    		LinearLayout  internalDestination = (LinearLayout) findViewById(R.id.funds_transfer_internal_destination);
				    	    		internalDestination.setVisibility(View.GONE);
				    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
			    	    	        Button showOrHideInternalDestination = (Button) findViewById(R.id.show_or_hide_internal_destination);
			    	    	        showOrHideInternalDestination.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

			    	    		}
				((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

				    	    	}else if (arg0.getId() == R.id.show_or_hide_internal_destination){
				    	    		LinearLayout  internalDestination = (LinearLayout) findViewById(R.id.funds_transfer_internal_destination);
				    	    		Drawable img = null;
					if(internalDestination.isShown()){
						//internalDestination.setVisibility(View.GONE);
						ExpandAnimation.collapse(internalDestination);
						img = getResources().getDrawable( R.drawable.show );
				    	    		}else{
				    	    			//internalDestination.setVisibility(View.VISIBLE);
				    	    			ExpandAnimation.expand(internalDestination);
				    	    			img = getResources().getDrawable( R.drawable.hide );
				    	    			LinearLayout  internalSaved = (LinearLayout) findViewById(R.id.funds_transfer_internal_saved);
					    	    		internalSaved.setVisibility(View.GONE);
					    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
				    	    	        Button showOrHideInternalSaved = (Button) findViewById(R.id.show_or_hide_internal_saved);
				    	    	        showOrHideInternalSaved.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		}
					((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

					    	    	}else if (arg0.getId() == R.id.show_or_hide_external_saved){
					    	    		LinearLayout  externalSaved = (LinearLayout) findViewById(R.id.funds_transfer_external_saved);
					    	    		Drawable img = null;
						if(externalSaved.isShown()){
							//externalSaved.setVisibility(View.GONE);
							ExpandAnimation.collapse(externalSaved);
							img = getResources().getDrawable( R.drawable.show );
					    	    		}else{
					    	    			//externalSaved.setVisibility(View.VISIBLE);
					    	    			ExpandAnimation.expand(externalSaved);
					    	    			img = getResources().getDrawable( R.drawable.hide );
						    	    		LinearLayout  externalDestination = (LinearLayout) findViewById(R.id.funds_transfer_external_destination);
						    	    		externalDestination.setVisibility(View.GONE);
						    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
					    	    	        Button showOrHideExternalDestination = (Button) findViewById(R.id.show_or_hide_external_destination);
					    	    	        showOrHideExternalDestination.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

					    	    		}
						((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

						    	    	}else if (arg0.getId() == R.id.show_or_hide_external_destination){
						    	    		LinearLayout  externalDestination = (LinearLayout) findViewById(R.id.funds_transfer_external_destination);
						    	    		Drawable img = null;
							if(externalDestination.isShown()){
								//externalDestination.setVisibility(View.GONE);
								ExpandAnimation.collapse(externalDestination);
								img = getResources().getDrawable( R.drawable.show );
						    	    		}else{
						    	    			//externalDestination.setVisibility(View.VISIBLE);
						    	    			ExpandAnimation.expand(externalDestination);
						    	    			img = getResources().getDrawable( R.drawable.hide );
						    	    			LinearLayout  externalSaved = (LinearLayout) findViewById(R.id.funds_transfer_external_saved);
							    	    		externalSaved.setVisibility(View.GONE);
							    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
						    	    	        Button showOrHideExternalSaved = (Button) findViewById(R.id.show_or_hide_external_saved);
						    	    	        showOrHideExternalSaved.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
				}
							((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

						    	    	}else if (R.id.sign_out == arg0.getId()){
							    	    		lipukaApplication.putPref("signout", null);
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
	    		public class OnMyAccountDestinationAccountSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedMyAccountDestinationAccount = myAccountDestinationAccountsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnInternalSavedAccountSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedInternalSavedAccount = internalSavedAccountsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnExternalSavedAccountSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedExternalSavedAccount = externalSavedAccountsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnCountrySelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedCountry = countriesArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnSavedCurrencySelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSavedCurrency = currenciesArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    	
	    		public class OnDestinationCurrencySelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedDestinationCurrency = currenciesArray[pos].getText();
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
		      /*lipukaApplication.setCurrentDialogMsg("Dear Alice, you have successfully transferred KES "+amountStr+" to "+destinationStr+". Thank you");
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
    	  internalFTText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
    	  externalFTText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	         break;
    	     case 1:
    	    	  internalFTText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	    	  externalFTText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
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
	public class OnBankSelectedListener implements OnItemSelectedListener {

	        public void onItemSelected(AdapterView<?> parent,
	            View view, int pos, long id) {
	        	selectedBankPosition = pos;
	        	bankCode = Integer.valueOf(itemsArray[selectedBankPosition].getValue());
	        	setBranches();
	        	}
	        public void onNothingSelected(AdapterView parent) {
	          // Do nothing.
	        }
	    }
	
	private void setBranches(){
		try{   
			Bank selectedBank = banksList.get(selectedBankPosition);
		    JSONArray branches = selectedBank.getBranches();

    	String[] itemsArray = new String[branches.length()];
      JSONObject currentBranch;
        for(int i = 0; i < branches.length(); i++){
        	currentBranch = branches.getJSONObject(i);
        	itemsArray[i] = currentBranch.getString("branch_name");   	
        }

AutoCompleteTextView branchesView = (AutoCompleteTextView) findViewById(R.id.branch_field);

ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, itemsArray);

branchesView.setAdapter(adapter);
	}catch(Exception ex){
    	Log.d(Main.TAG, "setting branches error", ex);

	}
	}
	
	    	    }