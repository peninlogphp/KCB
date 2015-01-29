package kcb.android;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import kcb.android.EcobankHome.ConfirmationDialog;
import kcb.android.MoneyGram.OnBeneficiaryCurrencySelectedListener;
import kcb.android.MoneyGram.OnRecipientCurrencySelectedListener;
import kcb.android.TransferFunds.MyGestureDetector;


import org.json.JSONArray;
import org.json.JSONObject;






import kcb.android.R;
import lipuka.android.data.Bank;
import lipuka.android.model.ActivityDateListener;
import lipuka.android.model.Navigation;
import lipuka.android.model.responsehandlers.ConfirmSendMoneyHandler;
import lipuka.android.model.responsehandlers.FetchBeneficiariesHandler;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.EcobankDatePickerDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.adapter.ComboBoxAdapter;
import lipuka.android.view.adapter.ManageBeneficiariesAdapter;
import lipuka.android.view.adapter.ManageTransactionsAdapter;
import lipuka.android.view.adapter.MgManageBeneficiariesAdapter;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;

public class MgManageBeneficiaries extends Activity implements OnClickListener, ResponseActivity,
AdapterView.OnItemClickListener{

	public static final String CURRENT_BENEFICIARY = "current_beneficiary";
	public static final byte SEND_MONEY = 1;
	public static final byte RECEIVE_MONEY = 2;

	Button submit;
	EditText fname, lname, state, city, alias;	
	AutoCompleteTextView country;
	RelativeLayout help;
	ImageButton closeHelp;

	LipukaApplication lipukaApplication;
	String amountStr, destinationStr;
	String selectedAccount, selectedCountry, selectedCountryValue, deletedBeneficiaryID;
	LipukaListItem[] accountsArray;
	JSONArray destinationCountriesArray;
	List<JSONObject> items;
	
	LipukaListItem[] itemsArray;
	int bankCode, branchCode = -1;
	ArrayList<Bank> banksList;
	boolean fetchedBeneficiaries = false;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.mg_beneficiaries);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Manage Beneficiaries");
	        fname = (EditText) findViewById(R.id.fname_field);
	        lname = (EditText) findViewById(R.id.lname_field);
	        
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
	        	             
	        	 	        state = (EditText) findViewById(R.id.state_field);
	        	 	        city = (EditText) findViewById(R.id.city_field);
	        	 	        alias = (EditText) findViewById(R.id.alias_field);
	        	 	        
	          	
	        	   	submit = (Button) findViewById(R.id.submit);
	        	   	submit.setOnClickListener(this);

	        	    
	        	    
	        Button showOrHideAddBeneficiary = (Button) findViewById(R.id.show_or_hide_add_beneficiary);
	        showOrHideAddBeneficiary.setOnClickListener(this);
	        Button showOrHideEditBeneficiary = (Button) findViewById(R.id.show_or_hide_edit_beneficiary);
	        showOrHideEditBeneficiary.setOnClickListener(this);

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
	    	Log.d(Main.TAG, "creating manage beneficiaries error", ex);

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
			lipukaApplication.setActivityState(MgManageBeneficiaries.class, true);	    
	    }
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(MgManageBeneficiaries.class, false);
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
		    			String cityStr = city.getText().toString();	
		    			String selectedCountry = country.getText().toString();	
		    			String aliasStr = alias.getText().toString();	
		    			
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
		    			if(aliasStr == null || aliasStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Alias is missing\n");
		    			}  
		    			if(valid){
		    		        		
		    		        				StringBuffer payloadBuffer = new StringBuffer();
		    		        				
		    		        			/*	payloadBuffer.append(enteredStartDate.get(Calendar.YEAR));
		    		        				payloadBuffer.append(enteredStartDate.get(Calendar.MONTH));
		    		        				payloadBuffer.append(enteredStartDate.get(Calendar.DAY_OF_MONTH));
		    		        				payloadBuffer.append("|");
		    		        				payloadBuffer.append(enteredEndDate.get(Calendar.YEAR));
		    		        				payloadBuffer.append(enteredEndDate.get(Calendar.MONTH));
		    		        				payloadBuffer.append(enteredEndDate.get(Calendar.DAY_OF_MONTH));
		    		        				payloadBuffer.append("|");*/
		    		        				
		    		        	   			Navigation nav = new Navigation();
		    		        			    nav.setPayload(payloadBuffer.toString());
		    		        				lipukaApplication.pushNavigationStack(nav);
		    		        				
		    			    				
		    			    				lipukaApplication.putPayload("first_name", fnameStr);
		    			    				lipukaApplication.putPayload("last_name", lnameStr);
		    			    				lipukaApplication.putPayload("destination_country_id", selectedCountryValue);
		    			    				lipukaApplication.putPayload("state", state.getText().toString());
		    			    				lipukaApplication.putPayload("city", cityStr);
		    			    				lipukaApplication.putPayload("beneficiary_alias", aliasStr);
		    			    				
		    				    			lipukaApplication.setServiceID("31");

		    		        				lipukaApplication.setCurrentDialogTitle("PIN");
		    		    		    	      lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
		    		    		    	showDialog(Main.DIALOG_PIN_ID);
amountStr = "You have successfully added "+aliasStr+" as beneficiary. Thank you.";
		    		        			}else{
		    		        				lipukaApplication.setCurrentDialogTitle("Validation Error");
		    		        	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
		    		        	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
		    		        			}

	    		}if(arg0.getId() == R.id.edit){
    				JSONObject currentBeneficiary = (JSONObject)arg0.getTag();
    		    	Intent intent = new Intent(this, MgEditBeneficiary.class);        
    		        intent.putExtra(CURRENT_BENEFICIARY, currentBeneficiary.toString());
    		        startActivity(intent);	    		        
    			
    			}else if(arg0.getId() == R.id.cancel){
    				lipukaApplication.setCurrentDialogTitle("PIN");
		    	      lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
		    	showDialog(Main.DIALOG_PIN_ID);  
				JSONObject currentBeneficiary = (JSONObject)arg0.getTag();
lipukaApplication.clearPayloadObject();
		    	try{
		    		lipukaApplication.putPayload("beneficiary_id", currentBeneficiary.getString("beneficiary_id"));
    				deletedBeneficiaryID = currentBeneficiary.getString("beneficiary_id");
	    			lipukaApplication.setServiceID("33");
		    		amountStr = "You have successfully deleted "+currentBeneficiary.getString("alias")+" from your beneficiaries list. Thank you.";
    			    }
    			    catch (org.json.JSONException jsonError) {
    				     Log.d(Main.TAG, "jsonError: ", jsonError);
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_add_beneficiary){
	    	    		LinearLayout  addBeneficiary = (LinearLayout) findViewById(R.id.add_beneficiary);
	    	    		Drawable img = null;
		if(addBeneficiary.isShown()){
			//myAccount.setVisibility(View.GONE);
			ExpandAnimation.collapse(addBeneficiary);
			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//myAccount.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(addBeneficiary);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  editBeneficiary = (LinearLayout) findViewById(R.id.edit_beneficiary);
		    	    		editBeneficiary.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideEditBeneficiary = (Button) findViewById(R.id.show_or_hide_edit_beneficiary);
	    	    	        showOrHideEditBeneficiary.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_edit_beneficiary){
		    	    		LinearLayout  editBeneficiary = (LinearLayout) findViewById(R.id.edit_beneficiary);
		    	    		Drawable img = null;
			if(editBeneficiary.isShown()){
				//anotherPerson.setVisibility(View.GONE);
				ExpandAnimation.collapse(editBeneficiary);
				img = getResources().getDrawable( R.drawable.show );
				((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	
    		}else{
if(!fetchedBeneficiaries){
    lipukaApplication.clearPayloadObject();
	lipukaApplication.consumeService("30", new FetchBeneficiariesHandler(lipukaApplication, this));
}else{
	//myAccount.setVisibility(View.VISIBLE);
	ExpandAnimation.expand(editBeneficiary);
	img = getResources().getDrawable( R.drawable.hide );
	LinearLayout  addBeneficiary = (LinearLayout) findViewById(R.id.add_beneficiary);
	addBeneficiary.setVisibility(View.GONE);
	Drawable img2 = getResources().getDrawable( R.drawable.show );
    Button showOrHideAddBeneficiary = (Button) findViewById(R.id.show_or_hide_add_beneficiary);
    showOrHideAddBeneficiary.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

}
}

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
	    	        case Main.DATE_DIALOG_ID:
	    	        	dialog = new EcobankDatePickerDialog(this);

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
	    	        case Main.DATE_DIALOG_ID:

	    	        	EcobankDatePickerDialog dpd = (EcobankDatePickerDialog)dialog;
	    	        	dpd.resetToCurrentDate();
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

	   @Override
	   public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	   }
	   public void createList(JSONArray beneficiaries){
		   fetchedBeneficiaries = true;
		   ListView listView = (ListView)findViewById(R.id.lipuka_list_view);
		    items = new ArrayList<JSONObject>();
  try{      
                 for(int i = 0; i < beneficiaries.length(); i++){
                 	items.add(beneficiaries.getJSONObject(i));
                 }
                 }catch(Exception ex){
           	    	Log.d(Main.TAG, "creating list error", ex);

             	}
   		MgManageBeneficiariesAdapter adapter = new MgManageBeneficiariesAdapter(this, items);
   		listView.setAdapter(adapter);
   		Log.d(Main.TAG, "created listview");

   		AnimationSet set = new AnimationSet(true);

   	    Animation animation = new AlphaAnimation(0.0f, 1.0f);
   	    animation.setDuration(50);
   	    set.addAnimation(animation);

   	    animation = new TranslateAnimation(
   	        Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
   	        Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
   	    );
   	    animation.setDuration(100);
   	    set.addAnimation(animation);

   	    LayoutAnimationController controller =
   	            new LayoutAnimationController(set, 0.5f);
   	    listView.setLayoutAnimation(controller);
   	    listView.setOnItemClickListener(this);	
   	    
		LinearLayout  editBeneficiary = (LinearLayout) findViewById(R.id.edit_beneficiary);
		Drawable img = null;

			ExpandAnimation.expand(editBeneficiary);
			img = getResources().getDrawable( R.drawable.hide );
			LinearLayout  addBeneficiary = (LinearLayout) findViewById(R.id.add_beneficiary);
    		addBeneficiary.setVisibility(View.GONE);
    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	        Button showOrHideAddBeneficiary = (Button) findViewById(R.id.show_or_hide_add_beneficiary);
	        showOrHideAddBeneficiary.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

Button showOrHideEditBeneficiary = (Button) findViewById(R.id.show_or_hide_edit_beneficiary);

showOrHideEditBeneficiary.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

	   }
	   public void deleteBeneficiary(){
		   JSONObject beneficiaryToDelete = null;   
		   try{
		   		 for(JSONObject beneficiary: items){
	if(deletedBeneficiaryID.equals(beneficiary.getString("beneficiary_id"))){
		beneficiaryToDelete = beneficiary;
			        	}	
		           }
		   		 }catch(Exception ex){
		   	    	Log.d(Main.TAG, "error", ex);

		     	} 
		   		items.remove(beneficiaryToDelete); 
		   		ListView listView = (ListView)findViewById(R.id.lipuka_list_view);
		   		((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
	   }
	   }