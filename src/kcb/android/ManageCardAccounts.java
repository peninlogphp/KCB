package kcb.android;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import kcb.android.EcobankHome.ConfirmationDialog;
import kcb.android.Enroll.OnExpiryMonthSelectedListener;
import kcb.android.Enroll.OnExpiryYearSelectedListener;
import kcb.android.MoneyGram.OnBeneficiaryCurrencySelectedListener;
import kcb.android.MoneyGram.OnRecipientCurrencySelectedListener;
import kcb.android.TransferFunds.MyGestureDetector;


import org.json.JSONArray;
import org.json.JSONObject;






import kcb.android.R;
import lipuka.android.data.Bank;
import lipuka.android.model.ActivityDateListener;
import lipuka.android.model.Navigation;
import lipuka.android.model.responsehandlers.AddCardAccountHandler;
import lipuka.android.model.responsehandlers.ConfirmSendMoneyHandler;
import lipuka.android.model.responsehandlers.FetchBeneficiariesHandler;
import lipuka.android.model.responsehandlers.FetchCardAccountsHandler;
import lipuka.android.model.responsehandlers.FetchCardsHandler;
import lipuka.android.model.responsehandlers.SignUpHandler;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.EcobankDatePickerDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.adapter.ComboBoxAdapter;
import lipuka.android.view.adapter.ManageBeneficiariesAdapter;
import lipuka.android.view.adapter.ManageCardAccountsAdapter;
import lipuka.android.view.adapter.ManageSavedCardsAdapter;
import lipuka.android.view.adapter.ManageTransactionsAdapter;
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
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
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

public class ManageCardAccounts extends Activity implements OnClickListener, ResponseActivity,
AdapterView.OnItemClickListener{

	public static final String CURRENT_CARD = "current_card";
	public static final byte SEND_MONEY = 1;
	public static final byte RECEIVE_MONEY = 2;

	Button submit;
	EditText cardHolderName, 
	cardNo, securityCode;	
	RelativeLayout help;
	ImageButton closeHelp;

	LipukaApplication lipukaApplication;
	String selectedMonth, selectedYear; 
	LipukaListItem[] monthsArray, yearsArray;
	String amountStr, destinationStr;
	String deletedCardID;
	List<JSONObject> items;
	
	
	boolean fetchedCards = false;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.manage_card_accounts);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Manage Card Accounts");
	        cardHolderName = (EditText) findViewById(R.id.card_holder_name_field);
	        cardNo = (EditText) findViewById(R.id.card_no_field);
	        securityCode = (EditText) findViewById(R.id.security_code_field);
	
  cardNo.setInputType(InputType.TYPE_CLASS_NUMBER);
  securityCode.setInputType(InputType.TYPE_CLASS_NUMBER);
  securityCode.setTransformationMethod(PasswordTransformationMethod.getInstance());
  securityCode.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
  
  final Spinner spinner = (Spinner) findViewById(R.id.expiry_month_spinner);
  spinner.setOnItemSelectedListener(new OnExpiryMonthSelectedListener());
  
try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.expirymths));
   monthsArray = new LipukaListItem[sources.length()];
JSONObject currentSource;
  for(int i = 0; i < sources.length(); i++){
  	currentSource = sources.getJSONObject(i);
  	LipukaListItem lipukaListItem = new LipukaListItem("", 
currentSource.getString("name"), currentSource.getString("value"));
  	monthsArray[i]= lipukaListItem;   	
  }
  }catch(Exception ex){
  	Log.d(Main.TAG, "creating months list error", ex);

	}
	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, monthsArray);
	spinner.setAdapter(adapter);
	
	final Spinner spinnerExpiryYear = (Spinner) findViewById(R.id.expiry_year_spinner);
	spinnerExpiryYear.setOnItemSelectedListener(new OnExpiryYearSelectedListener());
	  
	try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.expiryyrs));
	   yearsArray = new LipukaListItem[sources.length()];
	JSONObject currentSource;
	  for(int i = 0; i < sources.length(); i++){
	  	currentSource = sources.getJSONObject(i);
	  	LipukaListItem lipukaListItem = new LipukaListItem("", 
	currentSource.getString("name"), currentSource.getString("value"));
	  	yearsArray[i]= lipukaListItem;   	
	  }
	  }catch(Exception ex){
	  	Log.d(Main.TAG, "creating years list error", ex);

		}
		 ComboBoxAdapter adapterExpiryYear = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, yearsArray);
		spinnerExpiryYear.setAdapter(adapterExpiryYear);
	        	        	             
	        	   	submit = (Button) findViewById(R.id.submit);
	        	   	submit.setOnClickListener(this);

	        	    
	        	    
	        Button showOrHideAddCard = (Button) findViewById(R.id.show_or_hide_add_card);
	        showOrHideAddCard.setOnClickListener(this);
	        Button showOrHideEditCard = (Button) findViewById(R.id.show_or_hide_edit_card);
	        showOrHideEditCard.setOnClickListener(this);

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
	    	Log.d(Main.TAG, "creating manage card accounts error", ex);

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
	        if(lipukaApplication.getProfileID() == 0){
	        	Intent i = new Intent(this, StanChartHome.class);
	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);        	
	        }
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(ManageCardAccounts.class, true);	    
	    }
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(ManageCardAccounts.class, false);
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
	        			String cardHolderNameStr = cardHolderName.getText().toString();
		    			String cardNoStr = cardNo.getText().toString();	
		    			String securityCodeStr = securityCode.getText().toString();	

		    			if(cardHolderNameStr == null || cardHolderNameStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Card holder name is missing\n");
		    			}
		    			if(cardNoStr == null || cardNoStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Card number is missing\n");
		    			}
		    			if(securityCodeStr == null || securityCodeStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Security code is missing\n");
		    			}    			
		    				
		    				securityCode.setText("");
		    			if(valid){
		    	
		    			    				
		    				lipukaApplication.putPayload("card_no", cardNoStr);
		    				lipukaApplication.putPayload("cvv_code", securityCodeStr);
		    				lipukaApplication.putPayload("card_name", cardHolderNameStr);
		    				lipukaApplication.putPayload("expiry_date", "20"+selectedYear+"-"+selectedMonth+"-01");
		    				
		    			    				lipukaApplication.consumeService("70", new AddCardAccountHandler(lipukaApplication, this, cardNoStr, cardHolderNameStr,
		    			    						securityCodeStr, "20"+selectedYear+"-"+selectedMonth+"-01"));

		    		        			}else{
		    		        				lipukaApplication.setCurrentDialogTitle("Validation Error");
		    		        	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
		    		        	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
		    		        			}

	    		}if(arg0.getId() == R.id.edit){
    				JSONObject currentCard = (JSONObject)arg0.getTag();
    		    	Intent intent = new Intent(this, EditCardAccount.class);        
    		        intent.putExtra(CURRENT_CARD, currentCard.toString());
    		        startActivity(intent);	    		        
    			
    			}else if(arg0.getId() == R.id.cancel){
    				lipukaApplication.setCurrentDialogTitle("PIN");
		    	      lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
		    	showDialog(Main.DIALOG_PIN_ID);  
				JSONObject currentCard = (JSONObject)arg0.getTag();
lipukaApplication.clearPayloadObject();
		    	try{
		    		lipukaApplication.putPayload("account_id", currentCard.getString("account_id"));
    				deletedCardID = currentCard.getString("account_id");
	    			lipukaApplication.setServiceID("63");
		    		//amountStr = "You have successfully deleted "+currentCard.getString("alias")+" from your beneficiaries list. Thank you.";
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_add_card){
	    	    		LinearLayout  addCard = (LinearLayout) findViewById(R.id.add_card);
	    	    		Drawable img = null;
		if(addCard.isShown()){
			//myAccount.setVisibility(View.GONE);
			ExpandAnimation.collapse(addCard);
			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//myAccount.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(addCard);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  editCard = (LinearLayout) findViewById(R.id.edit_card);
		    	    		editCard.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideEditCard = (Button) findViewById(R.id.show_or_hide_edit_card);
	    	    	        showOrHideEditCard.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_edit_card){
		    	    		LinearLayout  editCard = (LinearLayout) findViewById(R.id.edit_card);
		    	    		Drawable img = null;
			if(editCard.isShown()){
				//anotherPerson.setVisibility(View.GONE);
				ExpandAnimation.collapse(editCard);
				img = getResources().getDrawable( R.drawable.show );
				((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	
    		}else{
if(!fetchedCards){
    lipukaApplication.clearPayloadObject();
	lipukaApplication.consumeService("64", new FetchCardAccountsHandler(lipukaApplication, this));
}else{
	//myAccount.setVisibility(View.VISIBLE);
	ExpandAnimation.expand(editCard);
	img = getResources().getDrawable( R.drawable.hide );
	LinearLayout  addCard = (LinearLayout) findViewById(R.id.add_card);
	addCard.setVisibility(View.GONE);
	Drawable img2 = getResources().getDrawable( R.drawable.show );
    Button showOrHideAddCard = (Button) findViewById(R.id.show_or_hide_add_card);
    showOrHideAddCard.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
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
	    		public class OnExpiryMonthSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedMonth = monthsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnExpiryYearSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedYear = yearsArray[pos].getText();
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

	   @Override
	   public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	   }
	   public void createList(JSONArray cards){
		   fetchedCards = true;
		   ListView listView = (ListView)findViewById(R.id.lipuka_list_view);
		    items = new ArrayList<JSONObject>();
  try{      
                 for(int i = 0; i < cards.length(); i++){
                 	items.add(cards.getJSONObject(i));
                 }
                 }catch(Exception ex){
           	    	Log.d(Main.TAG, "creating list error", ex);

             	}
   		ManageCardAccountsAdapter adapter = new ManageCardAccountsAdapter(this, items);
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
   	    
		LinearLayout  editCard = (LinearLayout) findViewById(R.id.edit_card);
		Drawable img = null;

			ExpandAnimation.expand(editCard);
			img = getResources().getDrawable( R.drawable.hide );
			LinearLayout  addCard = (LinearLayout) findViewById(R.id.add_card);
    		addCard.setVisibility(View.GONE);
    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	        Button showOrHideAddCard = (Button) findViewById(R.id.show_or_hide_add_card);
	        showOrHideAddCard.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

Button showOrHideEditCard = (Button) findViewById(R.id.show_or_hide_edit_card);

showOrHideEditCard.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

	   }
	   public void deleteCard(){
		   JSONObject cardToDelete = null;   
		   try{
		   		 for(JSONObject card: items){
	if(deletedCardID.equals(card.getString("account_id"))){
		cardToDelete = card;
			        	}	
		           }
		   		 }catch(Exception ex){
		   	    	Log.d(Main.TAG, "error", ex);

		     	} 
		   		items.remove(cardToDelete); 
		   		ListView listView = (ListView)findViewById(R.id.lipuka_list_view);
		   		((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
	   }
	   }