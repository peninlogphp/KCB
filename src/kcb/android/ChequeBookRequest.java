package kcb.android;

import kcb.android.R;
import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.anim.LipukaAnim;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

public class ChequeBookRequest extends Activity implements OnClickListener{
	   
	Button submit;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedNoOfLeaves;

	String idTypeSelected, idTypeID, idTypePassport;

	LipukaApplication lipukaApplication;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();

	        setContentView(R.layout.cheque_book_request);

	        OnClickListener radio_listener2 = new OnClickListener() {
	            public void onClick(View v) {
	                // Perform action on clicks
	                RadioButton rb = (RadioButton) v;
	                selectedNoOfLeaves = rb.getText().toString();
	            }
	        };
	        
	        RadioButton twentyfive = (RadioButton) findViewById(R.id.twentyfive);
	        RadioButton fifty = (RadioButton) findViewById(R.id.fifty);
	        RadioButton hundred = (RadioButton) findViewById(R.id.hundred);
	        twentyfive.setOnClickListener(radio_listener2);
	        fifty.setOnClickListener(radio_listener2);
	        hundred.setOnClickListener(radio_listener2);

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
		    	myWebView.loadUrl("file:///android_asset/chequebookrequest.html");
		    	myWebView.setBackgroundColor(0);

		        closeHelp = (ImageButton) findViewById(R.id.close);
		        closeHelp.setOnClickListener(this); 
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(ChequeBookRequest.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(ChequeBookRequest.class, false);
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
	  /*  @Override
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
	    }
*/
	    		public void onClick(View arg0) {
	    		 if (submit == arg0){
	    			    lipukaApplication.clearNavigationStack();
	
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();	
	    			if(selectedNoOfLeaves == null || selectedNoOfLeaves.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Select number of leaves\n");
	    			}
	    			    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();

    			    	   				payloadBuffer.append(selectedNoOfLeaves+"|");
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