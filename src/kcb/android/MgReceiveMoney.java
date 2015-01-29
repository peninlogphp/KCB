package kcb.android;


import java.util.List;

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
import lipuka.android.view.adapter.ComboBoxAdapter;
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
import android.text.method.PasswordTransformationMethod;
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
import android.view.inputmethod.EditorInfo;
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

public class MgReceiveMoney extends Activity implements OnClickListener, ResponseActivity{
	   
	Button submit;
	EditText refNo, fname, lname, amount, testQuestionNew, testAnswerNew;
	AutoCompleteTextView country;
	RelativeLayout help;
	ImageButton closeHelp;

	LipukaApplication lipukaApplication;

	String amountStr, destinationStr;
	String selectedDestinationAccount, selectedCountry, selectedCountryValue;
	LipukaListItem[] destinationAccountsArray ;
	JSONArray destinationCountriesArray;
	int selectedDestinationIndex;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.mg_receive_money);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Receive Money");
	        refNo = (EditText) findViewById(R.id.ref_no_field);
	        fname = (EditText) findViewById(R.id.fname_field);
	        lname = (EditText) findViewById(R.id.lname_field);
amount = (EditText) findViewById(R.id.amount_field);
testQuestionNew = (EditText) findViewById(R.id.test_question_field);
testAnswerNew = (EditText) findViewById(R.id.test_answer_field);

refNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		

	        testAnswerNew.setTransformationMethod(PasswordTransformationMethod.getInstance());
	        testAnswerNew.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	        
	        final Spinner spinner = (Spinner) findViewById(R.id.destination_account_spinner);
	        spinner.setOnItemSelectedListener(new OnDestinationAccountSelectedListener());
		      int remittanceIndex = -1;
	        
	  try{      JSONArray sources = lipukaApplication.getProfileDataArray("accounts");
      destinationAccountsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("account_alias"), currentSource.getString("account_id"));
	        	destinationAccountsArray[i]= lipukaListItem;  
	        	if(currentSource.getString("account_type").equals("Remittance Account")){
	        		remittanceIndex = i;	
	        	}
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating destination accounts list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, destinationAccountsArray);
	   	spinner.setAdapter(adapter);	
	   	if(remittanceIndex != -1){
	   		spinner.setSelection(remittanceIndex);
	   	}
	
	   	country = (AutoCompleteTextView) findViewById(R.id.country_field);
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
	        for(String currentCountry: countriesArray2){
        		index++;
	        	if(currentCountry.equals(selectedCountry)){
			    	Log.d(Main.TAG, "got selected country");

	        		break;
	        	}			
	        	
	        	}
	  try{
		  selectedCountryValue = destinationCountriesArray.getJSONObject(index).getString("value");

        }catch(Exception ex){
	    	Log.d(Main.TAG, "error", ex);

  	}
        }
        });   
	   	submit = (Button) findViewById(R.id.submit);
	   	submit.setOnClickListener(this);
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
	    	Log.d(Main.TAG, "creating credit card payments error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
	        if(lipukaApplication.getProfileID() == 0){
	        	Intent i = new Intent(this, MgHome.class);
	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);        	
	        }
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(MgReceiveMoney.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(MgReceiveMoney.class, false);
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
	    			String refNoStr = refNo.getText().toString();	
	    			String fnameStr = fname.getText().toString();	
	    			String lnameStr = lname.getText().toString();	
	    			String selectedCountry = country.getText().toString();	
	    			String amountStr = amount.getText().toString();	

	    			/*  try{      JSONArray sources = lipukaApplication.getProfileDataArray("accounts");
	    		 JSONObject currentSource = sources.getJSONObject(selectedDestinationIndex);

  			        	if(!currentSource.getString("account_type").equals("Remittance Account")){
  			        		valid = false;
  		    				errorBuffer.append("You can only receive money into a Remittance Account\n");
  			        	}
	    			        }catch(Exception ex){
	    				    	Log.d(Main.TAG, "error", ex);
	    			
	    			    	}*/
	    			        
	    			        if(refNoStr == null || refNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Reference number is missing\n");
	    			}
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
	    			    				
	    			    				lipukaApplication.putPayload("destination_account_id", destinationAccountsArray[selectedDestinationIndex].getValue());
	    			    				lipukaApplication.putPayload("mtcn", refNoStr);
	    			    				lipukaApplication.putPayload("first_name", fnameStr);
	    			    				lipukaApplication.putPayload("last_name", lnameStr);
	    			    				lipukaApplication.putPayload("sender_country_id", selectedCountryValue);
	    			    				lipukaApplication.putPayload("amount", amountStr);
	    			    				lipukaApplication.putPayload("test_question", testQuestionNew.getText().toString());
	    			    				lipukaApplication.putPayload("test_answer", testAnswerNew.getText().toString());
	    			    					    			    				
	    			    				lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to receive KES "+amountStr+
	    			    			    		  ", using reference number "+refNoStr+", sent by "+fnameStr+" "+lnameStr+" from "+selectedCountry+" into your "+selectedDestinationAccount+". Press \"OK\" to receive now or \"Cancel\" to edit receive money details.");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);			
	    			    			this.amountStr = "You have successfully received KES "+amountStr+
		    			    		  ", using reference number "+refNoStr+", sent by "+fnameStr+" "+lnameStr+" from "+selectedCountry+" into your "+selectedDestinationAccount+". Your new balance is KES 20,000. Thank you.";
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
	    		public class OnDestinationAccountSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedDestinationAccount = destinationAccountsArray[pos].getText();
		 	        	selectedDestinationIndex = pos;
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
	    			lipukaApplication.setServiceID("29");
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
	    	    }