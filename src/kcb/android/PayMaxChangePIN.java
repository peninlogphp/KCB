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
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
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

public class PayMaxChangePIN extends Activity implements OnClickListener{
	   
	Button submit;
	EditText currentPIN;
	EditText newPIN;
	EditText confirmPIN;
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
	        setContentView(R.layout.change_pin);	    
	        
	        TextView title = (TextView)findViewById(R.id.title);
	        title.setText("Change Mobile PIN");
	        currentPIN = (EditText) findViewById(R.id.current_pin_field);
	        
	        newPIN = (EditText) findViewById(R.id.new_pin_field);
	        confirmPIN = (EditText) findViewById(R.id.confirm_pin_field);	        
     
	        currentPIN.setInputType(InputType.TYPE_CLASS_TEXT | 
	          		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
	        newPIN.setInputType(InputType.TYPE_CLASS_TEXT | 
	          		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
	          confirmPIN.setInputType(InputType.TYPE_CLASS_TEXT | 
		          		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
	          
	          currentPIN.setInputType(InputType.TYPE_CLASS_NUMBER);
	          currentPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());
	          currentPIN.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	          newPIN.setInputType(InputType.TYPE_CLASS_NUMBER);
	          newPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());
	          newPIN.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	          confirmPIN.setInputType(InputType.TYPE_CLASS_NUMBER);
	          confirmPIN.setTransformationMethod(PasswordTransformationMethod.getInstance());
	          confirmPIN.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	          InputFilter[] filterArray = new InputFilter[1];
	   	   filterArray[0] = new InputFilter.LengthFilter(4);
	   	currentPIN.setFilters(filterArray);
	   	newPIN.setFilters(filterArray);
	   	confirmPIN.setFilters(filterArray);

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
		    	myWebView.loadUrl("file:///android_asset/withdrawcash.html");
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
			lipukaApplication.setActivityState(PayMaxChangePIN.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(PayMaxChangePIN.class, false);
	    }
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       // MenuInflater inflater = getMenuInflater();
	        //inflater.inflate(R.menu.help_menu, menu);
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
	    			String currentPINStr = currentPIN.getText().toString();
String newPINStr = newPIN.getText().toString();	
String confirmPINStr = confirmPIN.getText().toString();		    			
	    			
	    				if(currentPINStr == null || currentPINStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Current PIN is missing\n");
		    			}	
	    				if(newPINStr == null || newPINStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("New PIN is missing\n");
		    			}
	    				if(confirmPINStr == null || confirmPINStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Please confirm PIN\n");
		    			} 	
	    				if(newPINStr.length() > 0 && confirmPINStr.length() > 0){
	    					if(!newPINStr.equals(confirmPINStr)){
			    				valid = false;
			    				errorBuffer.append("New PIN values entered do not match\n");
			    			}
		    			}
	    				currentPIN.setText("");
	    				newPIN.setText("");
	    				confirmPIN.setText("");

   			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
    			    				
    			    	   				payloadBuffer.append(currentPINStr+"|");
    			    	   				payloadBuffer.append(newPINStr+"|");
    			    	   				
	    			    	   			Navigation nav = new Navigation();	
	    			    	   			nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				lipukaApplication.setCurrentDialogTitle("Response");
	    			    	        	lipukaApplication.setCurrentDialogMsg("Dear Mali, you have successfully changed PIN for card 6464646468746632. Thank you");
	    			    	            lipukaApplication.showDialog(Main.DIALOG_MSG_ID);
	    			    			
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}

	    		}else if(arg0.getId() ==  R.id.help){
					//help.setVisibility(View.VISIBLE);
			        //help.startAnimation(LipukaAnim.inFromRightAnimation());
	    		}else if(arg0.getId() ==  R.id.home_button){
				 /*Intent i = new Intent(this, PaymaxHome.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);*/
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
	    	        	cd.setActivityShowing(CustomDialog.CHANGE_PIN);
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
	    	        	cd.setActivityShowing(CustomDialog.CHANGE_PIN);
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