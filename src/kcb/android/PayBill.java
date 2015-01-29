package kcb.android;

import java.util.List;


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
import android.graphics.drawable.Drawable;
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

public class PayBill extends Activity implements OnClickListener{
	   
	Button selectBillSubmit, enterAccountSubmit;
	EditText accountNo;
	EditText selectBillAmount, enterAccountAmount;
	EditText name;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedEnrollment;
	String selectedMerchant, selectedAlias;

	String idTypeSelected, idTypeID, idTypePassport;

	LipukaApplication lipukaApplication;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();

	        setContentView(R.layout.pay_bill);
	        
	        RadioGroup accountsList = (RadioGroup)findViewById(R.id.accounts_list);
 
	        LayoutInflater inflater=(LayoutInflater)getSystemService
		      (Context.LAYOUT_INFLATER_SERVICE);    
	    	List<LipukaListItem> accounts = lipukaApplication.parseEnrollments();
	    	LipukaListItem lipukaListItem = null;
	    if(accounts != null && accounts.size() > 0){
	        OnClickListener radio_listener = new OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on clicks
	                RadioButton rb = (RadioButton) v;
	                LipukaListItem item = (LipukaListItem)rb.getTag();
	                selectedEnrollment = item.getValue();
	                selectedAlias = item.getText();
		    		
	            }
	        };
	    		for (LipukaListItem account: accounts){
	 RadioButton accountItem = (RadioButton)inflater.inflate(R.layout.list_radio_button, accountsList, false);
	 accountsList.addView(accountItem);
				    accountItem.setText(account.getText());
				    accountItem.setTag(account);
				    accountItem.setOnClickListener(radio_listener);
	    		}

	    	}
        
	        accountNo = (EditText) findViewById(R.id.account_no_field);
	        selectBillAmount = (EditText) findViewById(R.id.select_bill_amount_field);
	        enterAccountAmount = (EditText) findViewById(R.id.enter_account_amount_field);
	        name = (EditText) findViewById(R.id.name_field);
	        
	       // accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        selectBillAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        enterAccountAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        
	        OnClickListener radio_listener2 = new OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on clicks
	                RadioButton rb = (RadioButton) v;
	                selectedMerchant = rb.getText().toString();
	            }
	        };
	        
	        final RadioButton kplc = (RadioButton) findViewById(R.id.kplc);
	        final RadioButton dstv = (RadioButton) findViewById(R.id.dstv);
	       
	        kplc.setOnClickListener(radio_listener2);
	        dstv.setOnClickListener(radio_listener2);

	        selectBillSubmit = (Button) findViewById(R.id.select_bill_submit);
	        selectBillSubmit.setOnClickListener(this);
	        enterAccountSubmit = (Button) findViewById(R.id.enter_account_submit);
	        enterAccountSubmit.setOnClickListener(this);
	        
	        Button showOrHideSelectBillt = (Button) findViewById(R.id.show_or_hide_select_bill);
	        showOrHideSelectBillt.setOnClickListener(this);
	        Button showOrHideEnterAccount = (Button) findViewById(R.id.show_or_hide_enter_account);
	        showOrHideEnterAccount.setOnClickListener(this);

	        Button helpButton = (Button)findViewById(R.id.help);
		    helpButton.setOnClickListener(this);
		    Button homeButton = (Button)findViewById(R.id.home_button);
		    homeButton.setOnClickListener(this);
		   
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
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(PayBill.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(PayBill.class, false);
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
	    		if (selectBillSubmit == arg0){
	    		    lipukaApplication.clearNavigationStack();
	
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String amountStr = selectBillAmount.getText().toString();	

	    			if(selectedEnrollment == null || selectedEnrollment.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Select Bill\n");
	    			}
    				if(amountStr == null || amountStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Enter Amount\n");
	    			}
	    			    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    				payloadBuffer.append(selectedEnrollment+"|");
	    			    				payloadBuffer.append(selectedAlias+"|");
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

	    		}else if (enterAccountSubmit == arg0){
	    		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String accNoStr = accountNo.getText().toString();
	    			String amountStr = enterAccountAmount.getText().toString();	
	    			if(selectedMerchant == null || selectedMerchant.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Select merchant\n");
	    			}
	   				if(accNoStr == null || accNoStr.length() == 0){
			    				valid = false;
			    				errorBuffer.append("Enter Bill Number\n");
			    			}
			    			if(amountStr == null || amountStr.length() == 0){
			    				valid = false;
			    				errorBuffer.append("Enter Amount\n");
			    			}
	    			    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();

    			    	   				payloadBuffer.append("Other|");
    			    	   				payloadBuffer.append(selectedMerchant+"|");
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
	    	    	}else if (arg0.getId() == R.id.show_or_hide_select_bill){
	    	    		LinearLayout  selectBillBody = (LinearLayout) findViewById(R.id.select_bill_body);
	    	    		Drawable img = null;
		if(selectBillBody.isShown()){
			selectBillBody.setVisibility(View.GONE);
	    	    			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			selectBillBody.setVisibility(View.VISIBLE);
	    	    			img = getResources().getDrawable( R.drawable.hide );
	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_enter_account){
		    	    		LinearLayout  enterAccountBillBody = (LinearLayout) findViewById(R.id.enter_account_body);
		    	    		Drawable img = null;
			if(enterAccountBillBody.isShown()){
				enterAccountBillBody.setVisibility(View.GONE);
		    	    			img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			enterAccountBillBody.setVisibility(View.VISIBLE);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

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