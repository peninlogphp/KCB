package kcb.android;


import java.util.ArrayList;
import java.util.List;



import org.json.JSONArray;
import org.json.JSONObject;

import com.devsmart.android.ui.HorizontalListView;






import kcb.android.R;
import lipuka.android.data.HomeItem;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.adapter.ComboBoxAdapter;
import lipuka.android.view.adapter.MerchantCategoriesAdapter;
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

public class Merchants extends Activity implements OnClickListener, ResponseActivity{
	   
	Button submit, submitMerchants, submitOther, submitSupermarket, submitRestaurant, submitGasStations;
	EditText amount, amountMerchants, amountOther, amountRestaurants, amountGasStations;
	EditText merchantsAccountNo, merchantsBillAlias;
	EditText otherAccountNo, otherBillAlias;
	AutoCompleteTextView merchantName, restaurantName;
	CheckBox merchantSaveBill, otherSaveBill;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedSavedBill, selectedMerchant, merchantHit, merchantHitValue,
	selectedSupermarket, restaurantHit, restaurantHitValue, selectedLocation,
	selectedSourceAccount, selectedGasStation;

	LipukaApplication lipukaApplication;

	LipukaListItem[] savedBillsArray, merchantsArray, supermarketsArray, 
	restaurantsArray, locationsArray, sourceAccountsArray, gasStationsArray;
	String amountStr, destinationStr;
	int selectedCategory;
	
	ViewFlipper flipper;
	int selected;
	//LinearLayout newCard, savedCard;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.merchants);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Pay Bill");
	        
	        List<HomeItem> topArtistesList = new ArrayList<HomeItem>();
	        topArtistesList.add(new HomeItem(0, "Make it better", "Utility Bills", R.drawable.funds_transfer, true));
	        topArtistesList.add(new HomeItem(1, "I remember", "Supermarkets", R.drawable.pay_bill, true));
	        topArtistesList.add(new HomeItem(2, "Through with love", "Restaurants", R.drawable.western_union, true));
	        topArtistesList.add(new HomeItem(3, "Piano Man", "Gas Stations", R.drawable.airtime, true));
	       
	        HorizontalListView topArtistesListView = (HorizontalListView) findViewById(R.id.categories_list);
	        final MerchantCategoriesAdapter topArtistesAdapter = new MerchantCategoriesAdapter(this, topArtistesList);
	        topArtistesListView.setAdapter(topArtistesAdapter);
	        topArtistesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					if (selectedCategory > position) {
	     			    flipper.setInAnimation(inFromLeftAnimation());
	     			    flipper.setOutAnimation(outToRightAnimation());
	     			    flipper.setDisplayedChild(position);
	     			   }else if (selectedCategory < position) {
		     			    flipper.setInAnimation(outToLeftAnimation());
		     			    flipper.setOutAnimation(inFromRightAnimation());
		     			    flipper.setDisplayedChild(position);
		     			   }else{
	     				   return;
	     			   }
					selectedCategory = position;	
	topArtistesAdapter.notifyDataSetChanged();

				}
	        });
	        
flipper = (ViewFlipper) findViewById(R.id.flipper);
	        
	        //newCard = (LinearLayout)findViewById(R.id.funds_transfer_new_card);
	        //savedCard = (LinearLayout)findViewById(R.id.funds_transfer_saved_card);
	        
	        amount = (EditText) findViewById(R.id.saved_bill_amount_field);
	        amountMerchants = (EditText) findViewById(R.id.merchant_amount_field);
	        amountOther = (EditText) findViewById(R.id.other_amount_field);
	        amountRestaurants = (EditText) findViewById(R.id.restaurant_amount_field);
	        amountGasStations = (EditText) findViewById(R.id.gas_station_amount_field);
	        
	        merchantsAccountNo = (EditText) findViewById(R.id.merchant_account_number_field);
	        merchantName = (AutoCompleteTextView) findViewById(R.id.merchant_name_field);
 otherAccountNo = (EditText) findViewById(R.id.other_account_number_field);

 merchantsBillAlias = (EditText) findViewById(R.id.merchant_alias_field);
 otherBillAlias = (EditText) findViewById(R.id.other_alias_field);
 merchantSaveBill = (CheckBox) findViewById(R.id.merchant_save_bill);
 otherSaveBill = (CheckBox) findViewById(R.id.other_save_bill);

 restaurantName = (AutoCompleteTextView) findViewById(R.id.restaurant_name_field);
	        
	       // accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountMerchants.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountOther.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountRestaurants.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        amountGasStations.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	     
	        merchantsAccountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        otherAccountNo.setInputType(InputType.TYPE_CLASS_NUMBER);
 	        
	       
	       final Spinner spinner = (Spinner) findViewById(R.id.saved_bill_spinner);
	        spinner.setOnItemSelectedListener(new OnSavedBillSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSavedBills());
	         savedBillsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	savedBillsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating saved bills list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, savedBillsArray);
	   	spinner.setAdapter(adapter);
	   	
	
	       final Spinner merchantSpinner = (Spinner) findViewById(R.id.merchant_spinner);
	       merchantSpinner.setOnItemSelectedListener(new OnMerchantSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadMerchants());
	         merchantsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	merchantsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating merchants list error", ex);
	
	    	}
	   	 ComboBoxAdapter merchantsAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, merchantsArray);
	   	merchantSpinner.setAdapter(merchantsAdapter);	   
  	
	   	String[] merchantsStringArray = null; 
        try{      
        merchantsStringArray = new String[merchantsArray.length];

              for(int i = 0; i < merchantsArray.length; i++){            		
              	merchantsStringArray[i]=  merchantsArray[i].getText();   	
              }
              }catch(Exception ex){
        	    	Log.d(Main.TAG, "creating merchants list error", ex);

          	}
              ArrayAdapter<String> merchantsSearchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, merchantsStringArray);

         final String[] merchantsStringArray2 = merchantsStringArray;
         merchantName.setAdapter(merchantsSearchAdapter);    
         merchantName.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
	         int index = -1;

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	index = -1;
        	merchantHit = merchantName.getText().toString();
	        for(String currentMerchant: merchantsStringArray2){
        		index++;
	        	if(currentMerchant.equals(merchantHit)){
			    	Log.d(Main.TAG, "got selected merchant");

	        		break;
	        	}			
	        	
	        	}
	  try{
		  merchantHitValue = merchantsArray[index].getValue();

        }catch(Exception ex){
	    	Log.d(Main.TAG, "error", ex);

  	}
        }
        });   
       
         final Spinner spinnerSupermarket = (Spinner) findViewById(R.id.supermarket_spinner);
         spinnerSupermarket.setOnItemSelectedListener(new OnSupermarketSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.supermarkets));
	         supermarketsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	supermarketsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating supermarkets list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapterSupermarket = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, supermarketsArray);
	   	spinnerSupermarket.setAdapter(adapterSupermarket);
	   	
	   	final Spinner spinnerLocations = (Spinner) findViewById(R.id.location_spinner);
	   	spinnerLocations.setOnItemSelectedListener(new OnLocationSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.restaurant_locations));
	         locationsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	locationsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating locations list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapterLocations = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, locationsArray);
	   	spinnerLocations.setAdapter(adapterLocations);
	   	
		String[] restaurantsStringArray = null; 

        try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.restaurants));
        restaurantsArray = new LipukaListItem[sources.length()];
    	restaurantsStringArray = new String[restaurantsArray.length];
     JSONObject currentSource;
       for(int i = 0; i < sources.length(); i++){
       	currentSource = sources.getJSONObject(i);
       	LipukaListItem lipukaListItem = new LipukaListItem("", 
     currentSource.getString("name"), currentSource.getString("value"));
       	restaurantsArray[i]= lipukaListItem;   	
       	restaurantsStringArray[i]= currentSource.getString("name");   	
       }
       }catch(Exception ex){
	    	Log.d(Main.TAG, "creating restaurants list error", ex);

   	}
              ArrayAdapter<String> restaurantsSearchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, restaurantsStringArray);

         final String[] restaurantsStringArray2 = restaurantsStringArray;
         restaurantName.setAdapter(restaurantsSearchAdapter);    
         restaurantName.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
	         int index = -1;

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	index = -1;
        	restaurantHit = restaurantName.getText().toString();
	        for(String currentRestaurant: restaurantsStringArray2){
        		index++;
	        	if(currentRestaurant.equals(restaurantHit)){
			    	Log.d(Main.TAG, "got selected restaurant");

	        		break;
	        	}			
	        	
	        	}
	  try{
		  restaurantHitValue = restaurantsArray[index].getValue();

        }catch(Exception ex){
	    	Log.d(Main.TAG, "error", ex);

  	}
        }
        }); 
         
         final Spinner spinnerSourceAccounts = (Spinner) findViewById(R.id.source_account_spinner);
         spinnerSourceAccounts.setOnItemSelectedListener(new OnSourceAccountSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.pay_bill_accounts));
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
	   	 ComboBoxAdapter adapterSourceAccounts = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, sourceAccountsArray);
	   	spinnerSourceAccounts.setAdapter(adapterSourceAccounts);
	   	
	    final Spinner spinnerGasStations = (Spinner) findViewById(R.id.gas_company_spinner);
	    spinnerGasStations.setOnItemSelectedListener(new OnGasStationSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.gas_companies));
	         gasStationsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	gasStationsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating gas stations list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapterGasStations = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, gasStationsArray);
	   	spinnerGasStations.setAdapter(adapterGasStations);
	   	
	   	submit = (Button) findViewById(R.id.saved_bill_submit);
	        submit.setOnClickListener(this);
	        submitMerchants = (Button) findViewById(R.id.merchant_submit);
	        submitMerchants.setOnClickListener(this);
	        submitOther = (Button) findViewById(R.id.other_submit);
	        submitOther.setOnClickListener(this);
	        submitSupermarket = (Button) findViewById(R.id.supermarket_submit);
	        submitSupermarket.setOnClickListener(this);
	        submitRestaurant = (Button) findViewById(R.id.restaurant_submit);
	        submitRestaurant.setOnClickListener(this);
	        submitGasStations = (Button) findViewById(R.id.gas_station_submit);
	        submitGasStations.setOnClickListener(this);
	        
	        Button showOrHideSavedBills = (Button) findViewById(R.id.show_or_hide_pay_bills_saved);
	        showOrHideSavedBills.setOnClickListener(this);
	        Button showOrHideMerchants = (Button) findViewById(R.id.show_or_hide_pay_bills_merchants);
	        showOrHideMerchants.setOnClickListener(this);
	        Button showOrHideOther = (Button) findViewById(R.id.show_or_hide_pay_bills_other);
	        showOrHideOther.setOnClickListener(this);
     
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
			lipukaApplication.setActivityState(Merchants.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(Merchants.class, false);
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
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to pay \""+selectedSavedBill+"\" bill. Bill Amount is KES "+amountStr+". Press \"OK\" to pay now or \"Cancel\" to edit pay bill details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/
	    				  				showResponse();

	    			    			this.amountStr = amountStr;
destinationStr  = selectedSavedBill;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitMerchants == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String amountStr = amountMerchants.getText().toString();
	    			String merchantsAccountNoStr = merchantsAccountNo.getText().toString();	
	    			String accountAliasStr = merchantsBillAlias.getText().toString();	
	    			
	    			if(merchantsAccountNoStr == null || merchantsAccountNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Account number is missing\n");
	    			}
	    			
			    			if(amountStr == null || amountStr.length() == 0){
			    				valid = false;
			    				errorBuffer.append("Amount is missing\n");
			    			}
	    			    			
	    			
	    			    	if(merchantSaveBill.isChecked()){

	    			    	   	if(accountAliasStr == null || accountAliasStr.length() == 0){
	    		    				valid = false;
	    		    				errorBuffer.append("Bill alias is missing\n");
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
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to pay KES "+amountStr+
	    			    			    		  " to "+selectedMerchant+", account number "+merchantsAccountNoStr+". Press \"OK\" to pay now or \"Cancel\" to edit pay bill details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/
	    			  				showResponse();

	    			    			this.amountStr = amountStr;
	    			    			destinationStr  = selectedMerchant+", account number "+merchantsAccountNoStr;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitOther == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	  			boolean valid = true;
    			StringBuffer errorBuffer = new StringBuffer();
    			String businessNoStr = merchantName.getText().toString();
    			String amountStr = amountOther.getText().toString();
    			String merchantsAccountNoStr = otherAccountNo.getText().toString();	
    			String accountAliasStr = otherBillAlias.getText().toString();	
    			
    			if(businessNoStr == null || businessNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Business number is missing\n");
    			}
    			if(merchantsAccountNoStr == null || merchantsAccountNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Account number is missing\n");
    			}
    			
		    			if(amountStr == null || amountStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Amount is missing\n");
		    			}
    			    			
    			
    			    	if(otherSaveBill.isChecked()){

    			    	   	if(accountAliasStr == null || accountAliasStr.length() == 0){
    		    				valid = false;
    		    				errorBuffer.append("Bill alias is missing\n");
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
    			    			      lipukaApplication.setCurrentDialogMsg("You are about to pay KES "+amountStr+
    			    			    		  " to "+businessNoStr+", account number "+merchantsAccountNoStr+". Press \"OK\" to pay now or \"Cancel\" to edit pay bill details");
    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/	
    				  				showResponse();

    			    			this.amountStr = amountStr;
    			    			destinationStr  = businessNoStr+", account number "+merchantsAccountNoStr;
    			    			}else{
    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    			    			}		

	    		}else if (submitSupermarket == arg0){
		  		    lipukaApplication.clearNavigationStack();
	    			
		  				showResponse();

		    		}else if (submitRestaurant == arg0){
			  		    lipukaApplication.clearNavigationStack();
		    			
			  			boolean valid = true;
		    			StringBuffer errorBuffer = new StringBuffer();
		    			String amountStr = amountRestaurants.getText().toString();
		    			
		    			if(restaurantHitValue == null || restaurantHitValue.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Please select restaurant from the list shown as you type in restaurant name\n");
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
		    			    			/*	lipukaApplication.setCurrentDialogTitle("Confirm");
		    			    			      lipukaApplication.setCurrentDialogMsg("You are about to pay KES "+amountStr+
		    			    			    		  " to "+businessNoStr+", account number "+merchantsAccountNoStr+". Press \"OK\" to pay now or \"Cancel\" to edit pay bill details");
		    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
		    			    			this.amountStr = amountStr;
		    			    			destinationStr  = businessNoStr+", account number "+merchantsAccountNoStr;
		    			    			*/
		    			    		showResponse();		
		    						}else{
		    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
		    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
		    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
		    			    			}		

			    		}else if (submitGasStations == arg0){
				  		    lipukaApplication.clearNavigationStack();
			    			
				  			boolean valid = true;
			    			StringBuffer errorBuffer = new StringBuffer();
			    			String amountStr = amountGasStations.getText().toString();
	
			    			
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
			    			    			/*	lipukaApplication.setCurrentDialogTitle("Confirm");
			    			    			      lipukaApplication.setCurrentDialogMsg("You are about to pay KES "+amountStr+
			    			    			    		  " to "+businessNoStr+", account number "+merchantsAccountNoStr+". Press \"OK\" to pay now or \"Cancel\" to edit pay bill details");
			    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
			    			    			this.amountStr = amountStr;
			    			    			destinationStr  = businessNoStr+", account number "+merchantsAccountNoStr;
			    			    			*/
			    			    		showResponse();		
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_pay_bills_saved){
	    	    		LinearLayout  savedBills = (LinearLayout) findViewById(R.id.pay_bills_saved);
	    	    		Drawable img = null;
		if(savedBills.isShown()){
			//savedBills.setVisibility(View.GONE);
			ExpandAnimation.collapse(savedBills);
  			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//savedBills.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(savedBills);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  merchants = (LinearLayout) findViewById(R.id.pay_bills_merchants);
		    	    		merchants.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideMerchants = (Button) findViewById(R.id.show_or_hide_pay_bills_merchants);
	    	    	        showOrHideMerchants.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    	 		LinearLayout  other = (LinearLayout) findViewById(R.id.pay_bills_other);
		    	    		other.setVisibility(View.GONE);
	    	    	        Button showOrHideOther = (Button) findViewById(R.id.show_or_hide_pay_bills_other);
	    	    	        showOrHideOther.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_pay_bills_merchants){
		    	    		LinearLayout  merchants = (LinearLayout) findViewById(R.id.pay_bills_merchants);
		    	    		Drawable img = null;
			if(merchants.isShown()){
				//merchants.setVisibility(View.GONE);
				ExpandAnimation.collapse(merchants);
				img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//merchants.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(merchants);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  savedBills = (LinearLayout) findViewById(R.id.pay_bills_saved);
			    	    		savedBills.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideSavedBills = (Button) findViewById(R.id.show_or_hide_pay_bills_saved);
		    	    	        showOrHideSavedBills.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		    	    	        LinearLayout  other = (LinearLayout) findViewById(R.id.pay_bills_other);
			    	    		other.setVisibility(View.GONE);
		    	    	        Button showOrHideOther = (Button) findViewById(R.id.show_or_hide_pay_bills_other);
		    	    	        showOrHideOther.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

			    	    	}else if (arg0.getId() == R.id.show_or_hide_pay_bills_other){
			    	    		LinearLayout  other = (LinearLayout) findViewById(R.id.pay_bills_other);
			    	    		Drawable img = null;
				if(other.isShown()){
					//other.setVisibility(View.GONE);
					ExpandAnimation.collapse(other);
					img = getResources().getDrawable( R.drawable.show );
			    	    		}else{
			    	    			//other.setVisibility(View.VISIBLE);
			    	    			ExpandAnimation.expand(other);
			    	    			img = getResources().getDrawable( R.drawable.hide );
			    	    			LinearLayout  savedBills = (LinearLayout) findViewById(R.id.pay_bills_saved);
				    	    		savedBills.setVisibility(View.GONE);
				    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
			    	    	        Button showOrHideSavedBills = (Button) findViewById(R.id.show_or_hide_pay_bills_saved);
			    	    	        showOrHideSavedBills.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	        LinearLayout  merchants = (LinearLayout) findViewById(R.id.pay_bills_merchants);
				    	    		merchants.setVisibility(View.GONE);
			    	    	        Button showOrHideMerchants = (Button) findViewById(R.id.show_or_hide_pay_bills_merchants);
			    	    	        showOrHideMerchants.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
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
	    		public class OnSupermarketSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedSupermarket = supermarketsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnLocationSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedLocation = locationsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
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
	    		public class OnGasStationSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedGasStation = gasStationsArray[pos].getValue();
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
			 lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
	           showDialog(Main.DIALOG_MSG_ID);
	   }
	   public int getSelectedCategory(){
		   return selectedCategory;
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