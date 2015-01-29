package kcb.android;

import java.util.List;


import kcb.android.R;
import lipuka.android.model.MsisdnRegex;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
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
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import android.widget.TextView;

public class Topup extends Activity implements OnClickListener{
	   
	Button submit;
	AutoCompleteTextView mobileNo;
	EditText amount;
	CheckBox ownNumber;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedNetwork;
	String selectedAmount;

	String idTypeSelected, idTypeID, idTypePassport;

	LipukaApplication lipukaApplication;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.topup);

	        OnClickListener radio_listener = new OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on clicks
	                RadioButton rb = (RadioButton) v;
	                selectedNetwork = rb.getText().toString().toLowerCase();
	            }
	        };
	    
	        
	        mobileNo = (AutoCompleteTextView) findViewById(R.id.mobile_no_field);
	        amount = (EditText) findViewById(R.id.amount_field);
	        ownNumber = (CheckBox) findViewById(R.id.own_number);
	        final LinearLayout networksLayout = (LinearLayout) findViewById(R.id.networks_layout);
	       
	        final TextView specifyLabel = (TextView) findViewById(R.id.specify_label);
	        final LinearLayout mobileNoLayout = (LinearLayout) findViewById(R.id.mobile_no_layout);

	        Cursor c = lipukaApplication.getContactsAutoCompleteCursor();
	          ContactsAutoCompleteCursorAdapter adapter = new ContactsAutoCompleteCursorAdapter(this, c);
	          mobileNo.setAdapter(adapter);
	          mobileNo.setHint("name/number");
	          
	          amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        
	        final RadioButton safaricom = (RadioButton) findViewById(R.id.safaricom);
	       // safaricom.setChecked(true);
	       // selectedNetwork = "safaricom";
	        // final RadioButton yu = (RadioButton) findViewById(R.id.yu);
	       final RadioButton airtel = (RadioButton) findViewById(R.id.airtel);
	        
	        safaricom.setOnClickListener(radio_listener);
	       // yu.setOnClickListener(radio_listener);
	       airtel.setOnClickListener(radio_listener);
	        
	        OnClickListener ownNumberListener = new OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on clicks
	            	if(ownNumber.isChecked()){
	            		mobileNo.setEnabled(false);
	            		specifyLabel.setVisibility(View.GONE);
	            		mobileNoLayout.setVisibility(View.GONE);
	        	    	networksLayout.setVisibility(View.GONE);
	            		}
	            	else{
	            		mobileNo.setEnabled(true);
	            		specifyLabel.setVisibility(View.VISIBLE);
	            		mobileNoLayout.setVisibility(View.VISIBLE);
	            		networksLayout.setVisibility(View.VISIBLE);	            		
	            	}
}
	        };
	        ownNumber.setOnClickListener(ownNumberListener);
	        
	        OnClickListener radio_listener2 = new OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on clicks
	                RadioButton rb = (RadioButton) v;
	                if(rb.getId() == R.id.specify_amount){
	                	selectedAmount = null;
		                amount.setEnabled(true);
	                }else{
		                selectedAmount = rb.getText().toString();
		                amount.setText("");
		                amount.setEnabled(false);

	                }
	            }
	        };
	        
	        RadioButton fifty = (RadioButton) findViewById(R.id.fifty);
	        RadioButton hundred = (RadioButton) findViewById(R.id.hundred);
	        RadioButton twohundred = (RadioButton) findViewById(R.id.twohundred);
	        RadioButton fivehundred = (RadioButton) findViewById(R.id.fivehundred);
	        RadioButton thousand = (RadioButton) findViewById(R.id.thousand);
	        RadioButton specifyAmount = (RadioButton) findViewById(R.id.specify_amount);
	        
	        fifty.setOnClickListener(radio_listener2);
	        hundred.setOnClickListener(radio_listener2);
	        twohundred.setOnClickListener(radio_listener2);
	        fivehundred.setOnClickListener(radio_listener2);
	        thousand.setOnClickListener(radio_listener2);
	        specifyAmount.setOnClickListener(radio_listener2);

	        submit = (Button) findViewById(R.id.submit);
	        submit.setOnClickListener(this);
	        
	        Button helpButton = (Button)findViewById(R.id.help);
		    helpButton.setOnClickListener(this);
		    Button homeButton = (Button)findViewById(R.id.home_button);
		    homeButton.setOnClickListener(this);
		   
			 help = (RelativeLayout) findViewById(R.id.help_layout);
		        WebView myWebView = (WebView) findViewById(R.id.webview);
		        WebSettings webSettings = myWebView.getSettings();
		        webSettings.setJavaScriptEnabled(true);
		    	myWebView.loadUrl("file:///android_asset/topup.html");
		    	myWebView.setBackgroundColor(0);

		        closeHelp = (ImageButton) findViewById(R.id.close);
		        closeHelp.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                help.startAnimation(LipukaAnim.outToRightAnimation());
		            	help.setVisibility(View.GONE);
		            }
		        });  
			lipukaApplication.setCurrentActivity(this);
}  catch(Exception e){
	Log.d(Main.TAG, "creating topup error", e);

} 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(Topup.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(Topup.class, false);
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
	    			String mobileNoStr = mobileNo.getText().toString();
	    			String amountStr = amount.getText().toString();	
	    			
	    			if((selectedAmount == null || selectedAmount.length() == 0) &&
	    					(amountStr == null || amountStr.length() == 0)){
	    				valid = false;
	    				errorBuffer.append("Select or enter amount\n");
	    			}else if((selectedAmount != null && selectedAmount.length() > 0) &&
	    					(amountStr != null && amountStr.length() > 0)){
	    				valid = false;
	    				errorBuffer.append("Select or enter amount, not both\n");
	    			}
	    			
	    			if(!ownNumber.isChecked()){
	    				if(mobileNoStr == null || mobileNoStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Select mobile number\n");
		    			}	
	    				if(mobileNoStr != null && mobileNoStr.length() > 0){
	    					mobileNoStr = lipukaApplication.ensureCountryCode(mobileNoStr);
if(!MsisdnRegex.isValidMsisdn(mobileNoStr)){
		    				valid = false;
		    				errorBuffer.append("Enter valid mobile number\n");
		    			}	
	    				}
	    				if(selectedNetwork == null || selectedNetwork.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Select mobile network\n");
		    			}
	    			}
	    			    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
    			    				
	    				    			if(ownNumber.isChecked()){
	    				    				payloadBuffer.append(lipukaApplication.getMSISDN()+"|");	    				
	    				    				payloadBuffer.append(selectedAmount+"|");	    				
	    				    				payloadBuffer.append(amountStr+"|");	    				
	    				    			}else{
	    				    				payloadBuffer.append("Other|");
	    			    	   				payloadBuffer.append(selectedNetwork+"|");
	    			    	   				payloadBuffer.append(mobileNoStr+"|");
	    			    	   				payloadBuffer.append(selectedAmount+"|");
		    			    				payloadBuffer.append(amountStr+"|");	
	    				    			}
    			    	   				
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
	    	    }