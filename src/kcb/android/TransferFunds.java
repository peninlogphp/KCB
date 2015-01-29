package kcb.android;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import lipuka.android.view.adapter.BranchesAdapter;
import lipuka.android.view.adapter.ComboBoxAdapter;

import kcb.android.R;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
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

public class TransferFunds extends Activity implements OnClickListener{
	   
	Button submit;
	EditText accountNo;
	EditText amount;
	EditText name;
	CheckBox specifyAccount;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedPolicy;
	String idTypeSelected, idTypeID, idTypePassport;

	LipukaApplication lipukaApplication;
	ViewFlipper flipper;
	GestureDetector gestureDetector;
	int selected;
	LinearLayout internalFT, externalFT;
	TextView internalFTText, externalFTText;
	
	EditText fname;
	EditText lname;
	EditText destAccount;
	EditText amountEft;
	Button submitEft;
	int selectedBankPosition;
	LipukaListItem[] itemsArray;
	int bankCode, branchCode = -1;
    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();

	try{        setContentView(R.layout.transfer_funds_main);
	        
	        TextView selectAccountLabel = (TextView)findViewById(R.id.policy_label);
    		//final LinearLayout merchantsLinearLayout = (LinearLayout)findViewById(R.id.merchants_linear_layout); 
    		final TextView accountNoLabel = (TextView)findViewById(R.id.account_no_label);
    		final TextView nameLabel = (TextView)findViewById(R.id.name_label); 
    		specifyAccount = (CheckBox)findViewById(R.id.specify_account_number); 
    		
	        RadioGroup accountsList = (RadioGroup)findViewById(R.id.accounts_list);

	        LayoutInflater inflater=(LayoutInflater)getSystemService
		      (Context.LAYOUT_INFLATER_SERVICE);    
	    	List<LipukaListItem> accounts = lipukaApplication.getOtherAccounts();
	    	LipukaListItem lipukaListItem = null;
	    if(accounts != null && accounts.size() > 0){
	        OnClickListener radio_listener = new OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on clicks
	                RadioButton rb = (RadioButton) v;
	                LipukaListItem item = (LipukaListItem)rb.getTag();
	                selectedPolicy = item.getValue();

		    		accountNo.setVisibility(View.GONE);
		    		name.setVisibility(View.GONE);
		    		//merchantsLinearLayout.setVisibility(View.GONE);
		    		accountNoLabel.setVisibility(View.GONE);
		    		nameLabel.setVisibility(View.GONE);
		    		specifyAccount.setChecked(false);
	            }
	        };
	    		for (LipukaListItem account: accounts){
	 RadioButton accountItem = (RadioButton)inflater.inflate(R.layout.list_radio_button, accountsList, false);
	 accountsList.addView(accountItem);
				    accountItem.setText(account.getText());
				    accountItem.setTag(account);
				    accountItem.setOnClickListener(radio_listener);
	    		}

	    	}else{
	    		selectAccountLabel.setVisibility(View.GONE);
	    		specifyAccount.setVisibility(View.GONE);
	    	}
	    
	    OnClickListener checkbox_listener = new OnClickListener() {
            public void onClick(View v) {

if(specifyAccount.isChecked()){
    accountNo.setVisibility(View.VISIBLE);
	name.setVisibility(View.VISIBLE);
	//merchantsLinearLayout.setVisibility(View.VISIBLE);
	accountNoLabel.setVisibility(View.VISIBLE);
	nameLabel.setVisibility(View.VISIBLE);	
}else{
	accountNo.setVisibility(View.GONE);
	name.setVisibility(View.GONE);
	//merchantsLinearLayout.setVisibility(View.GONE);
	accountNoLabel.setVisibility(View.GONE);
	nameLabel.setVisibility(View.GONE);	
}
            }
        };
        
        specifyAccount.setOnClickListener(checkbox_listener);
		
	        accountNo = (EditText) findViewById(R.id.account_no_field);
	        amount = (EditText) findViewById(R.id.amount_field);
	        name = (EditText) findViewById(R.id.name_field);
	        
	        accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		

	        submit = (Button) findViewById(R.id.submit);
	        submit.setOnClickListener(this);
	      // Log.d(SalamaSureMain.TAG, "jst after buttons");

	        gestureDetector = new GestureDetector(this, new MyGestureDetector());
	        flipper = (ViewFlipper) findViewById(R.id.flipper);

	        flipper.setOnTouchListener(new OnTouchListener() {

	     	   public boolean onTouch(View v, MotionEvent event) {

	     	    if (gestureDetector.onTouchEvent(event)) {

	     	     return true;

	     	    } else {
	 				//Log.i(SalamaSureMain.TAG, "onTouch() returned false ");

	     	     return false;
	     	    }
	     	   }
	     	});
	        
	        internalFT = (LinearLayout)findViewById(R.id.internal_ft);
	        externalFT = (LinearLayout)findViewById(R.id.external_ft);
	        internalFTText = (TextView)findViewById(R.id.internal_ft_text);
	        externalFTText = (TextView)findViewById(R.id.external_ft_text);
	        internalFTText.setOnClickListener(this);
	        externalFTText.setOnClickListener(this);

	       initEFT();
	        
	       Button helpButton = (Button)findViewById(R.id.help);
		    helpButton.setOnClickListener(this);
		    Button homeButton = (Button)findViewById(R.id.home_button);
		    homeButton.setOnClickListener(this);
		   
			 help = (RelativeLayout) findViewById(R.id.help_layout);
		        WebView myWebView = (WebView) findViewById(R.id.webview);
		        WebSettings webSettings = myWebView.getSettings();
		        webSettings.setJavaScriptEnabled(true);
		    	myWebView.loadUrl("file:///android_asset/fundstransfer.html");
		    	myWebView.setBackgroundColor(0);

		        closeHelp = (ImageButton) findViewById(R.id.close);
		        closeHelp.setOnClickListener(this); 
			lipukaApplication.setCurrentActivity(this);
	    }catch(Exception ex){
	    	Log.d(Main.TAG, "transfer funds error", ex);
	    } 	
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(TransferFunds.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(TransferFunds.class, false);
	    }
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.help_menu, menu);
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
	   /* @Override
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
		
		    		if (!accountNo.isShown()){
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String amountStr = amount.getText().toString();	

    				if(selectedPolicy == null || selectedPolicy.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Select recipient\n");
	    			}
    				if(amountStr == null || amountStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Enter Amount\n");
	    			}
	    			    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    				payloadBuffer.append(selectedPolicy+"|");
    			    	   				payloadBuffer.append(amountStr+"|");
    			    	   				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.setCurrentDialogTitle("PIN");
	    			    			      lipukaApplication.setCurrentDialogMsg("Enter PIN");
	    			    			showDialog(Main.DIALOG_PIN_ID);			


	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}
		    		}else{
		    			
		    			boolean valid = true;
		    			StringBuffer errorBuffer = new StringBuffer();
		    			String accNoStr = accountNo.getText().toString();
		    			String amountStr = amount.getText().toString();	
		     			if(accNoStr == null || accNoStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Enter Account Number\n");
		    			}
		    			if(amountStr == null || amountStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Enter Amount\n");
		    			}
		    			    			
		    			    			if(valid){
		    			    				StringBuffer payloadBuffer = new StringBuffer();
		    			    				payloadBuffer.append("Other|");
	    			    	   				payloadBuffer.append(accNoStr+"|");
		    			    				payloadBuffer.append(amountStr+"|");		
	    			    	   				payloadBuffer.append(name.getText().toString()+"|");
	    			    	   			    				
		    			    	   			Navigation nav = new Navigation();
		    			    			    nav.setPayload(payloadBuffer.toString());
		    			    				lipukaApplication.pushNavigationStack(nav);
		    			    				lipukaApplication.setPin(null);
		    			    				lipukaApplication.setCurrentDialogTitle("PIN");
		    			    			      lipukaApplication.setCurrentDialogMsg("Enter PIN");
		    			    			showDialog(Main.DIALOG_PIN_ID);			


		    			    			}else{
		    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
		    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
		    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
		    			    			}
		    			
		    		}
	    		}else if (submitEft == arg0){
	    		    lipukaApplication.clearNavigationStack();
	    			lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/e_funds_transfer.php");

		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String fnameStr = fname.getText().toString();
                    String lnameStr = lname.getText().toString();
                    String destAccountStr = destAccount.getText().toString();
                    String amountStr = amountEft.getText().toString();


 if(fnameStr == null || fnameStr.length() == 0){
	 errorBuffer.append("Enter first name\n");
valid = false;
 }
                    if(lnameStr == null || lnameStr.length() == 0){
                    	errorBuffer.append("Enter last name\n");
valid = false;
 }
                      if(bankCode == 0){
                    	  errorBuffer.append("select bank\n");
valid = false;
 }

            if(branchCode == -1){
            	errorBuffer.append("select branch\n");
valid = false;
 }
                    if(destAccountStr == null || destAccountStr.length() == 0){
                    	errorBuffer.append("Enter destination account\n");
valid = false;
 }
                    if(amountStr == null || amountStr.length() == 0){
                    	errorBuffer.append("Enter amount\n");
valid = false;
          }

	    			    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    				payloadBuffer.append(fnameStr);
	    			    				payloadBuffer.append("|");
	    			    				payloadBuffer.append(lnameStr);
	    			    				payloadBuffer.append("|");
	    			    				payloadBuffer.append(bankCode);
	    			    				payloadBuffer.append("|");
	    			    				payloadBuffer.append(branchCode);
	    			    				payloadBuffer.append("|");
	    			    				payloadBuffer.append(destAccountStr);
	    			    				payloadBuffer.append("|");
	    			    				payloadBuffer.append(amountStr);
	    			    				payloadBuffer.append("|");
    			    	   				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.setCurrentDialogTitle("PIN");
	    			    			      lipukaApplication.setCurrentDialogMsg("Enter PIN");
	    			    			showDialog(Main.DIALOG_PIN_ID);			


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
				help.setVisibility(View.VISIBLE);
		        help.startAnimation(LipukaAnim.inFromRightAnimation());
    		}else if(arg0.getId() ==  R.id.home_button){
			 Intent i = new Intent(this, EcobankHome.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
    		}else if (closeHelp == arg0){
	    			help.startAnimation(LipukaAnim.outToRightAnimation());
	    	    	help.setVisibility(View.GONE);
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
	        	  internalFTText.setBackgroundDrawable(res.getDrawable(R.drawable.ft_btn_selected ));
	        	  externalFTText.setBackgroundDrawable(res.getDrawable(R.drawable.ft_btn ));
	        	         break;
	        	     case 1:
	        	    	  internalFTText.setBackgroundDrawable(res.getDrawable(R.drawable.ft_btn ));
	        	    	  externalFTText.setBackgroundDrawable(res.getDrawable(R.drawable.ft_btn_selected ));
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
	    	
	    	private void initEFT(){
	    		fname = (EditText) findViewById(R.id.fname_field);
		        lname = (EditText) findViewById(R.id.lname_field);
		        
		        Spinner spinner = (Spinner) findViewById(R.id.bank_spinner);
		        spinner.setOnItemSelectedListener(new OnBankSelectedListener());
		        
		  try{      JSONObject eftData = new JSONObject(lipukaApplication.loadEftData());
		        JSONArray banks = eftData.getJSONArray("banks");
		         itemsArray = new LipukaListItem[banks.length()];
		      JSONObject currentBank;
		        for(int i = 0; i < banks.length(); i++){
		        	currentBank = banks.getJSONObject(i);
		        	LipukaListItem lipukaListItem = new LipukaListItem("", 
		      currentBank.getString("bank_name"), currentBank.getString("bank_code"));
		        	itemsArray[i]= lipukaListItem;   	
		        }
		   	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, itemsArray);
		   	spinner.setAdapter(adapter);
		   	
AutoCompleteTextView branches = (AutoCompleteTextView) findViewById(R.id.branch_field);
branches.setHint("branch name");
	
branches.setOnItemClickListener(new AdapterView.OnItemClickListener() { 

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
            long arg3) {

  		try{   
			JSONObject eftData = new JSONObject(lipukaApplication.loadEftData());
        JSONArray banks = eftData.getJSONArray("banks");
        JSONObject selectedBank = banks.getJSONObject(selectedBankPosition);
        JSONArray branches = selectedBank.getJSONArray("branches");
        JSONObject selectedBranch = branches.getJSONObject(arg2);
branchCode  = selectedBranch.getInt("branch_code"); 
	}catch(Exception ex){
    	Log.d(Main.TAG, "setting branches error", ex);

	}
	
    }
    });
	    	}catch(Exception ex){
		    	Log.d(Main.TAG, "creating external transfer funds error", ex);
	
	    	}
		        destAccount = (EditText) findViewById(R.id.destination_account_field);
		        amountEft = (EditText) findViewById(R.id.amount_eft_field);
		        
		        amountEft.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		

		        submitEft = (Button) findViewById(R.id.submit_eft);
		        submitEft.setOnClickListener(this);
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
	    			JSONObject eftData = new JSONObject(lipukaApplication.loadEftData());
		        JSONArray banks = eftData.getJSONArray("banks");
		        JSONObject selectedBank = banks.getJSONObject(selectedBankPosition);
		        JSONArray branches = selectedBank.getJSONArray("branches");

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