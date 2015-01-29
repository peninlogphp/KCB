package kcb.android;


import java.util.List;

import kcb.android.EcobankHome.ConfirmationDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import kcb.android.R;
import lipuka.android.model.MsisdnRegex;
import lipuka.android.model.Navigation;
import lipuka.android.model.responsehandlers.ConfirmSendMoneyHandler;
import lipuka.android.model.responsehandlers.SignUpHandler;
import lipuka.android.util.Formatters;
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
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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

public class SendMoney extends Activity implements OnClickListener, ResponseActivity{
	   
	Button submit, submitSaved;
	EditText amount, amountSaved;
	EditText fname, lname, countryNew, stateNew, cityNew, goldCardNoNew, promoCodeNew, testQuestionNew, testAnswerNew;
	EditText goldCardNoSaved, promoCodeSaved, testQuestionSaved, testAnswerSaved;
	TextView countrySaved, stateSaved, citySaved;
	AutoCompleteTextView country;
	EditText beneficiaryAlias;
RelativeLayout help;
	ImageButton closeHelp;
	CheckBox acceptTerms, savedRecipientAcceptTerms;
	CheckBox saveBeneficiary;
	
	String selectedBeneficiary, selectedBeneficiaryValue;

	LipukaApplication lipukaApplication;
	String selectedSourceAccount, selectedSourceAccountValue,
	selectedSourceOfFundsNew, selectedPurposeOfFundsNew, 
	selectedSourceOfFundsSaved, selectedPurposeOfFundsSaved, selectedCountry,
	selectedCountryValue;
	LipukaListItem[] sourceAccountsArray, beneficiariesArray,
	sourceOfFundsArray, purposeOfFundsArray;
	JSONArray destinationCountriesArray;
	String amountStr, destinationStr;
	


	ViewFlipper flipper;
	GestureDetector gestureDetector;
	int selected, selectedBeneficiaryIndex;
	LinearLayout newRecipient, savedRecipient;
	TextView newRecipientText, savedRecipientText;
	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.wu_send_money);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Send Money");
	        fname = (EditText) findViewById(R.id.fname_field);
	        lname = (EditText) findViewById(R.id.lname_field);

	        country = (AutoCompleteTextView) findViewById(R.id.new_recipient_country_field);
	        country.setHint("country name");
	        String[] countriesArray = null; 
	        try{       destinationCountriesArray = new JSONArray(lipukaApplication.loadAppData("countries", R.raw.all_countries));
	        countriesArray = new String[destinationCountriesArray.length()];

	            JSONObject currentSource;
	              for(int i = 0; i < destinationCountriesArray.length(); i++){
	              	currentSource = destinationCountriesArray.getJSONObject(i);
	            		
	              	countriesArray[i]=  currentSource.getString("name");   	
	              }
	              }catch(Exception ex){
	        	    	Log.d(Main.TAG, "creating countries list error", ex);

	          	}
	              ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, countriesArray);

	         final String[] countriesArray2 = countriesArray;
	        	 country.setAdapter(countryAdapter);    
	        country.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
		         int index = -1;

	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                long arg3) {
	        	index = -1;
	        	selectedCountry = country.getText().toString();
    	        LinearLayout newRecipientTestLayout = (LinearLayout) findViewById(R.id.new_recipient_test_question_n_answer_layout);
    	        for(String currentCountry: countriesArray2){
	        		index++;
    	        	if(currentCountry.equals(selectedCountry)){
    			    	Log.d(Main.TAG, "got selected country");

    	        		break;
    	        	}			
    	        	
    	        	}
    	  try{
    		  selectedCountryValue = destinationCountriesArray.getJSONObject(index).getString("value");
    	        if(destinationCountriesArray.getJSONObject(index).getString("test_question").equals("1")){
	        		newRecipientTestLayout.setVisibility(View.VISIBLE);
			    	Log.d(Main.TAG, "set to visible");

	        	}else{
	        		newRecipientTestLayout.setVisibility(View.GONE);
			    	Log.d(Main.TAG, "set to gone");

	        	}
	        }catch(Exception ex){
    	    	Log.d(Main.TAG, "creating countries list error", ex);

      	}
	        }
	        });
		      
	        countrySaved = (TextView) findViewById(R.id.saved_recipient_country_field);
	        stateNew = (EditText) findViewById(R.id.new_recipient_state_field);
	        cityNew = (EditText) findViewById(R.id.new_recipient_city_field);
	        countrySaved = (TextView) findViewById(R.id.saved_recipient_country_field);
	        stateSaved = (TextView) findViewById(R.id.saved_recipient_state_field);
	        citySaved = (TextView) findViewById(R.id.saved_recipient_city_field);
	        
	        goldCardNoNew = (EditText) findViewById(R.id.new_recipient_gold_card_no_field);
	        promoCodeNew = (EditText) findViewById(R.id.new_recipient_promotion_code_field);
	        
	        goldCardNoSaved = (EditText) findViewById(R.id.saved_recipient_gold_card_no_field);
	        promoCodeSaved = (EditText) findViewById(R.id.saved_recipient_promotion_code_field);
	        
	        amount = (EditText) findViewById(R.id.new_recipient_amount_field);
	        amountSaved = (EditText) findViewById(R.id.saved_recipient_amount_field);
	        
	       
	        testQuestionNew = (EditText) findViewById(R.id.new_recipient_test_question_field);
	        testAnswerNew = (EditText) findViewById(R.id.new_recipient_test_answer_field);
	        
	        beneficiaryAlias = (EditText) findViewById(R.id.alias_field);
	        saveBeneficiary = (CheckBox) findViewById(R.id.save_beneficiary);

	        testQuestionSaved = (EditText) findViewById(R.id.saved_recipient_test_question_field);
	        testAnswerSaved = (EditText) findViewById(R.id.saved_recipient_test_answer_field);
	        
	        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountSaved.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        
	        goldCardNoNew.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        promoCodeNew.setInputType(InputType.TYPE_CLASS_NUMBER);
	        goldCardNoSaved.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        promoCodeSaved.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        String goldCardNoStr = lipukaApplication.getProfileData("gold_card_no");
	        if(goldCardNoStr != null){
	        	goldCardNoNew.setText(goldCardNoStr);
		        goldCardNoSaved.setText(goldCardNoStr);        	
	        }
        
	        final Spinner spinner = (Spinner) findViewById(R.id.source_account_spinner);
	        spinner.setOnItemSelectedListener(new OnSourceAccountSelectedListener());
	        
	  try{      JSONArray sources = lipukaApplication.getProfileDataArray("accounts");
	         sourceAccountsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("account_alias"), currentSource.getString("account_id"));
	        	sourceAccountsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating source accounts list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceAccountsArray);
	   	spinner.setAdapter(adapter);
	   	
	   	final Spinner beneficiarySpinner = (Spinner) findViewById(R.id.beneficiary_spinner);
	   	beneficiarySpinner.setOnItemSelectedListener(new OnBeneficiarySelectedListener());
	        
	  try{      JSONArray sources = lipukaApplication.getProfileDataArray("beneficiaries");
	    if(sources != null){   
	    	beneficiariesArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("alias"), currentSource.getString("beneficiary_id"));
	        	beneficiariesArray[i]= lipukaListItem;   	
	        }
	        }else{
		    	beneficiariesArray = new LipukaListItem[1];
	LipukaListItem lipukaListItem = new LipukaListItem("", "None", "-1");
		    		        	beneficiariesArray[0]= lipukaListItem;        	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating beneficiaries list error", ex);
	
	    	}
	        ComboBoxAdapter beneficiaryAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, beneficiariesArray);
		   	beneficiarySpinner.setAdapter(beneficiaryAdapter); 
	   		
	   	final Spinner sourceOfFundsNewSpinner = (Spinner) findViewById(R.id.new_recipient_source_of_funds_spinner);
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
	   	
	       final Spinner sourceOfFundsSavedSpinner = (Spinner) findViewById(R.id.saved_recipient_source_of_funds_spinner);
	       sourceOfFundsSavedSpinner.setOnItemSelectedListener(new OnSourceOfFundsSavedSelectedListener());
	        
	   	 ComboBoxAdapter sourceOfFundSavedAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceOfFundsArray);
	   	sourceOfFundsSavedSpinner.setAdapter(sourceOfFundSavedAdapter);
      
	   	final Spinner purposeOfFundsNewSpinner = (Spinner) findViewById(R.id.new_recipient_purpose_of_funds_spinner);
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
	   	
	       final Spinner purposeOfFundsSavedSpinner = (Spinner) findViewById(R.id.saved_recipient_purpose_of_funds_spinner);
	       purposeOfFundsSavedSpinner.setOnItemSelectedListener(new OnPurposeOfFundsSavedSelectedListener());
	        
	   	 ComboBoxAdapter purposeOfFundSavedAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, purposeOfFundsArray);
	   	purposeOfFundsSavedSpinner.setAdapter(purposeOfFundSavedAdapter);
	   	
        acceptTerms = (CheckBox) findViewById(R.id.new_recipient_accept_terms);
        savedRecipientAcceptTerms = (CheckBox) findViewById(R.id.saved_recipient_accept_terms);

        TextView termsLink = (TextView) findViewById(R.id.new_recipient_terms_link);
        SpannableString mySpannableString = new SpannableString("Terms of Service");
    			 mySpannableString.setSpan(new UnderlineSpan(), 0, mySpannableString.length(), 0);
    			termsLink.setText(mySpannableString);
    			termsLink.setOnClickListener(this);
    			
    	TextView savedRecipientTermsLink = (TextView) findViewById(R.id.saved_recipient_terms_link);
    	savedRecipientTermsLink.setText(mySpannableString);
    	    			 savedRecipientTermsLink.setOnClickListener(this);
    	    			 
	   	submit = (Button) findViewById(R.id.new_recipient_submit);
	        submit.setOnClickListener(this);
	        submitSaved = (Button) findViewById(R.id.saved_recipient_submit);
	        submitSaved.setOnClickListener(this);
	   	
	        gestureDetector = new GestureDetector(this, new MyGestureDetector());
	        flipper = (ViewFlipper) findViewById(R.id.flipper);
	        
	        newRecipient = (LinearLayout)findViewById(R.id.wu_send_money_new);
	        savedRecipient = (LinearLayout)findViewById(R.id.wu_send_money_saved);
	        newRecipientText = (TextView)findViewById(R.id.new_recipient_text);
	        savedRecipientText = (TextView)findViewById(R.id.saved_recipient_text);
	        newRecipientText.setOnClickListener(this);
	        savedRecipientText.setOnClickListener(this);
	       
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
	    	Log.d(Main.TAG, "creating send money error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
	        if(lipukaApplication.getProfileID() == 0){
	        	Intent i = new Intent(this, WUhome.class);
	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);        	
	        }
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(SendMoney.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(SendMoney.class, false);
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
	    			String fnameStr = fname.getText().toString();	
	    			String lnameStr = lname.getText().toString();	
	    			String cityStr = cityNew.getText().toString();	
	    			String selectedCountry = country.getText().toString();	
	    			String goldCardNoStr = goldCardNoNew.getText().toString();	
	    			String amountStr = amount.getText().toString();	
	    			String beneficiaryAliasStr = beneficiaryAlias.getText().toString();	

	    			if(fnameStr == null || fnameStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("First name is missing\n");
	    			}
	    			
	    			if(lnameStr == null || lnameStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Last name is missing\n");
	    			}
	    			if(selectedCountryValue == null || selectedCountryValue.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Please select country from the list shown as you type in country name\n");
	    			}
	    			if(cityStr == null || cityStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("City is missing\n");
	    			}
	    			
	    			if(amountStr == null || amountStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Amount is missing\n");
	    			}
	    			if(saveBeneficiary.isChecked()){

			    	   	if(beneficiaryAliasStr == null || beneficiaryAliasStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Beneficiary alias is missing\n");
		    			}    		
			    	}   			
	    			
	    			if(valid){
	    			    				
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			payloadBuffer.append(amountStr+"|");	    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.putPayload("source_account_id", selectedSourceAccountValue);
	    			    				lipukaApplication.putPayload("first_name", fnameStr);
	    			    				lipukaApplication.putPayload("last_name", lnameStr);
	    			    				lipukaApplication.putPayload("destination_country_id", selectedCountryValue);
	    			    				lipukaApplication.putPayload("state", stateNew.getText().toString());
	    			    				lipukaApplication.putPayload("city", cityStr);
	    			    				lipukaApplication.putPayload("gold_card_no", goldCardNoNew.getText().toString());
	    			    				lipukaApplication.putPayload("promotion_code", promoCodeNew.getText().toString());
	    			    				lipukaApplication.putPayload("amount", amountStr);
	    			    				lipukaApplication.putPayload("source_of_funds_id", selectedSourceOfFundsNew);
	    			    				lipukaApplication.putPayload("purpose_of_fund_id", selectedPurposeOfFundsNew);
	    			    				lipukaApplication.putPayload("test_question", testQuestionNew.getText().toString());
	    			    				lipukaApplication.putPayload("test_answer", testAnswerNew.getText().toString());
	    			    				lipukaApplication.putPayload("beneficiary_alias", beneficiaryAlias.getText().toString());
	    			    				if(saveBeneficiary.isChecked()){
		    			    				lipukaApplication.putPayload("add_beneficiary", "1");	    			    					
	    			    				}else{
		    			    				lipukaApplication.putPayload("add_beneficiary", "0");	    			    					
	    			    				}
	    			    				JSONObject sendMoneyDetails = new JSONObject();
			    	  
				      	 try {
				      		sendMoneyDetails.put("amount", amountStr);
				      		sendMoneyDetails.put("fname", fnameStr);
				      		sendMoneyDetails.put("lname", lnameStr);
				      		sendMoneyDetails.put("country", selectedCountry);
				      		sendMoneyDetails.put("source_account", selectedSourceAccount);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.d(Main.TAG, "jsonError", e);
						}
	    			    				lipukaApplication.consumeService("34", new ConfirmSendMoneyHandler(lipukaApplication, this, sendMoneyDetails));
	    			    				
	    			    				
	    			    			this.amountStr = "You have successfully sent KES "+amountStr+
		    			    		  " to "+fnameStr+" "+lnameStr+" in "+selectedCountry+" for a fee of KES 20. KES 1 = KES 22.84. A total of KES "+(Integer.valueOf(amountStr)+20)+" has been deducted from your "+selectedSourceAccount+". Thank you.";
destinationStr  = "yourself";
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitSaved == arg0){
	  		    lipukaApplication.clearPayloadObject();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();	
	    			String countryStr = countrySaved.getText().toString();	
	    			String cityStr = citySaved.getText().toString();	
	    			String amountStr = amountSaved.getText().toString();	
	    		
	    			
	    			if(selectedBeneficiaryValue.equals("-1")){
	    				valid = false;
	    				errorBuffer.append("You do not have any saved beneficiaries to select\n");
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

	    			    				JSONObject sendMoneyDetails = new JSONObject();
	    			    				lipukaApplication.putPayload("source_account_id", selectedSourceAccountValue);
	    			    			    try{
	    					 	        	JSONArray sources = lipukaApplication.getProfileDataArray("beneficiaries");
	    					 		      JSONObject currentSource;
	    					 		 	currentSource = sources.getJSONObject(selectedBeneficiaryIndex);
	    					 		 	lipukaApplication.putPayload("first_name", currentSource.getString("first_name"));
	    			    				lipukaApplication.putPayload("last_name", currentSource.getString("last_name"));
	    			    				lipukaApplication.putPayload("destination_country_id", currentSource.getString("destination_country_id"));

	    			    				sendMoneyDetails.put("amount", amountStr);
	    					      		sendMoneyDetails.put("fname", currentSource.getString("first_name"));
	    					      		sendMoneyDetails.put("lname", currentSource.getString("last_name"));
	    					      		sendMoneyDetails.put("country", countryStr);
	    					      		sendMoneyDetails.put("source_account", selectedSourceAccount);

	    			    			    }catch(Exception ex){
	    					 			    	Log.d(Main.TAG, "error", ex);
	    					 		
	    					 		    	}
	    			    				lipukaApplication.putPayload("state", stateSaved.getText().toString());
	    			    				lipukaApplication.putPayload("city", cityStr);
	    			    				lipukaApplication.putPayload("gold_card_no", goldCardNoSaved.getText().toString());
	    			    				lipukaApplication.putPayload("promotion_code", promoCodeSaved.getText().toString());
	    			    				lipukaApplication.putPayload("amount", amountStr);
	    			    				lipukaApplication.putPayload("source_of_funds_id", selectedSourceOfFundsSaved);
	    			    				lipukaApplication.putPayload("purpose_of_fund_id", selectedPurposeOfFundsSaved);
	    			    				lipukaApplication.putPayload("test_question", testQuestionSaved.getText().toString());
	    			    				lipukaApplication.putPayload("test_answer", testAnswerSaved.getText().toString());
	    			    				lipukaApplication.putPayload("beneficiary_alias", "");
	    			    				lipukaApplication.putPayload("add_beneficiary", "0");	    			    					
    	  
				      	 
	    			    				lipukaApplication.consumeService("34", new ConfirmSendMoneyHandler(lipukaApplication, this, sendMoneyDetails));
	    			    					
	    			    			this.amountStr = "You have successfully sent KES "+amountStr+
		    			    		  " to "+selectedBeneficiary+" in "+countryStr+" for a fee of KES 20. AED 1 = KES 22.84. A total of KES "+(Integer.valueOf(amountStr)+20)+" has been deducted from your "+selectedSourceAccount+". Thank you.";
destinationStr  = "yourself";
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}
	    		}else if(arg0.getId() == R.id.new_recipient_text){
	    			 if (selected == 1) {
		     			    flipper.setInAnimation(inFromLeftAnimation());
		     			    flipper.setOutAnimation(outToRightAnimation());
		     			    flipper.setDisplayedChild(0);
		     			    selected = 0;
		     		   setSelectedBg();
		     			   }else{
		     				   return;
		     			   }			   
		     	}else if(arg0.getId() == R.id.saved_recipient_text){
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
	    		public class OnSourceAccountSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSourceAccount = sourceAccountsArray[pos].getText();
		 	        	selectedSourceAccountValue = sourceAccountsArray[pos].getValue();
		 	        
		 	        try{
		 	        	JSONArray sources = lipukaApplication.getProfileDataArray("accounts");
		 		      JSONObject currentSource;
		 		 	currentSource = sources.getJSONObject(pos);
		 		 	TextView accountBalance = (TextView) findViewById(R.id.account_balance);
		 		 	accountBalance.setText("KES "+ Formatters.formatAmount(currentSource.getDouble("account_balance")));
		 		        }catch(Exception ex){
		 			    	Log.d(Main.TAG, "creating source accounts list error", ex);
		 		
		 		    	}
		 	        }
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnBeneficiarySelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedBeneficiaryIndex = pos;
		 	        	selectedBeneficiary = beneficiariesArray[pos].getText();
		 	        	selectedBeneficiaryValue = beneficiariesArray[pos].getValue();
			 	    if(!selectedBeneficiaryValue.equals("-1")){  
		 	        	JSONArray sources = lipukaApplication.getProfileDataArray("beneficiaries");
try{
			 		      JSONObject currentSource;
			 		 	currentSource = sources.getJSONObject(pos);
			 		 	TextView country = (TextView) findViewById(R.id.saved_recipient_country_field);
			 		 	TextView state = (TextView) findViewById(R.id.saved_recipient_state_field);
			 		 	TextView city = (TextView) findViewById(R.id.saved_recipient_city_field);
			 		 	country.setText(currentSource.getString("country_name"));
			 		 	state.setText(currentSource.getString("state"));
			 		 	city.setText(currentSource.getString("city"));
			 		 	
			 		 	String selectedCountryID = sources.getJSONObject(selectedBeneficiaryIndex).getString("destination_country_id");
			 		 	sources = new JSONArray(lipukaApplication.loadAppData("countries", R.raw.all_countries));
			 		 	 
			 		     LinearLayout savedRecipientTestLayout = (LinearLayout) findViewById(R.id.saved_recipient_test_question_n_answer_layout);
			    	        for(int i = 0; i < sources.length(); i++){
			    	        	if(selectedCountryID.equals(sources.getJSONObject(i).getString("value"))){
			    			    	Log.d(Main.TAG, "got selected country");
					    	        if(sources.getJSONObject(i).getString("test_question").equals("1")){
						        		savedRecipientTestLayout.setVisibility(View.VISIBLE);
								    	Log.d(Main.TAG, "set to visible");

						        	}else{
						        		savedRecipientTestLayout.setVisibility(View.GONE);
								    	Log.d(Main.TAG, "set to gone");

						        	}
			    	        		break;
			    	        	}			
			    	        	
			    	        	}
			 		        }catch(Exception ex){
			 			    	Log.d(Main.TAG, "error", ex);
			 		
			 		    	}
			 		        
			 		  
			    	  
				        
			 	    }
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
	    			lipukaApplication.setServiceID("28");
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
    	  newRecipientText.setBackgroundDrawable(res.getDrawable(R.drawable.wu_tab_selected ));
    	  savedRecipientText.setBackgroundDrawable(res.getDrawable(R.drawable.wu_tab_unselected ));
    	  newRecipientText.setTextColor(Color.BLACK);
    	  savedRecipientText.setTextColor(Color.WHITE);

    	  break;
    	     case 1:
    	    	  newRecipientText.setBackgroundDrawable(res.getDrawable(R.drawable.wu_tab_unselected ));
    	    	  savedRecipientText.setBackgroundDrawable(res.getDrawable(R.drawable.wu_tab_selected ));
    	    	  newRecipientText.setTextColor(Color.WHITE);
    	    	  savedRecipientText.setTextColor(Color.BLACK);
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