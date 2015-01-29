package kcb.android;


import java.util.Calendar;
import java.util.StringTokenizer;

import kcb.android.R;
import lipuka.android.model.ActivityDateListener;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.EcobankDatePickerDialog;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.anim.LipukaAnim;

import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class FullStmt extends Activity implements OnClickListener{
   
Button submit;
EditText startDate;
EditText endDate;

RelativeLayout help;
ImageButton closeHelp;
String idTypeSelected;
byte idType;
LipukaApplication lipukaApplication;

ActivityDateListener activityDateListener;
EditText currentDateField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lipukaApplication = (LipukaApplication)getApplication();

        setContentView(R.layout.full_stmt);
       
        startDate = (EditText)findViewById(R.id.start_date_field);
        startDate.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        startDate.setOnTouchListener(new EcobankDateFieldListener());

        endDate = (EditText)findViewById(R.id.end_date_field);

        endDate.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        
             endDate.setOnTouchListener(new EcobankDateFieldListener());
             submit = (Button)findViewById(R.id.submit);
       submit.setOnClickListener(this);
       
       Button startDateButton = (Button)findViewById(R.id.start_date_button);
       startDateButton.setOnClickListener(this);
       
       Button endDateButton = (Button)findViewById(R.id.end_date_button);
       endDateButton.setOnClickListener(this);
       
       activityDateListener = new ActivityDateListener();
       
       Button helpButton = (Button)findViewById(R.id.help);
	    helpButton.setOnClickListener(this);
	    Button homeButton = (Button)findViewById(R.id.home_button);
	    homeButton.setOnClickListener(this);
	    
       help = (RelativeLayout) findViewById(R.id.help_layout);
       WebView myWebView = (WebView) findViewById(R.id.webview);
       WebSettings webSettings = myWebView.getSettings();
       webSettings.setJavaScriptEnabled(true);
   	myWebView.loadUrl("file:///android_asset/fullstmt.html");
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
		lipukaApplication.setActivityState(FullStmt.class, true);
		}
   
    @Override
    protected void onStop() {
        super.onStop();
		lipukaApplication.setActivityState(FullStmt.class, false);
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
    }*/

    		public void onClick(View arg0) {
    		if (submit == arg0){
    		    lipukaApplication.clearNavigationStack();

String startDateStr = startDate.getText().toString();

String endDateStr = endDate.getText().toString();
Calendar enteredStartDate = null;
Calendar enteredEndDate = null;
    			boolean valid = true;
    			StringBuffer errorBuffer = new StringBuffer();
    			
    			if(startDateStr == null || startDateStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter start date\n");
    			}
    			
    			if(endDateStr == null || endDateStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter end date\n");
    			}
    		try{	
    			if(valid){
	
	        	 enteredStartDate = Calendar.getInstance();
	        	 enteredEndDate = Calendar.getInstance();

	        	StringTokenizer tokens = new StringTokenizer(startDateStr, "-");
	        	int yr = Integer.parseInt(tokens.nextToken());
	        	int mth = Integer.parseInt(tokens.nextToken())-1;
	        	int day = Integer.parseInt(tokens.nextToken());
	        	
	        	enteredStartDate.set(yr, mth, day);
	        	
	        	tokens = new StringTokenizer(endDateStr, "-");
	        	yr = Integer.parseInt(tokens.nextToken());
	        	mth = Integer.parseInt(tokens.nextToken())-1;
	        	day = Integer.parseInt(tokens.nextToken());
	        	
	        	enteredEndDate.set(yr, mth, day);
	        	
	        	Calendar currentDate = Calendar.getInstance();
	        	
	        	if(enteredStartDate.after(enteredEndDate)){
	        		valid = false;
					errorBuffer.append("Start date should be before end date\n");	
	        	}
	        	
	        	if(currentDate.before(enteredStartDate)){
	        		valid = false;
					errorBuffer.append("Start date should not be in the future\n");	
	        	}
	        	
	        	if(currentDate.before(enteredEndDate)){
	        		valid = false;
					errorBuffer.append("End date should not be in the future\n");	
	        	}
	        	
    			}
    		}catch (NumberFormatException nfe){
    			valid = false;
				errorBuffer.append("Enter valid date values\n");			
    		}catch (Exception e){
    			valid = false;
				errorBuffer.append("Enter valid date values\n");		
    		}	
    			if(valid){
    		
    				StringBuffer payloadBuffer = new StringBuffer();
    				
    				payloadBuffer.append(enteredStartDate.get(Calendar.YEAR));
    				payloadBuffer.append(enteredStartDate.get(Calendar.MONTH));
    				payloadBuffer.append(enteredStartDate.get(Calendar.DAY_OF_MONTH));
    				payloadBuffer.append("|");
    				payloadBuffer.append(enteredEndDate.get(Calendar.YEAR));
    				payloadBuffer.append(enteredEndDate.get(Calendar.MONTH));
    				payloadBuffer.append(enteredEndDate.get(Calendar.DAY_OF_MONTH));
    				payloadBuffer.append("|");
    				
    	   			Navigation nav = new Navigation();
    			    nav.setPayload(payloadBuffer.toString());
    				lipukaApplication.pushNavigationStack(nav);
    				
    				lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/fullstmt11.php");
    				if(lipukaApplication.getPin() != null)	{
    					lipukaApplication.executeService();
    				}else{
    					lipukaApplication.setCurrentDialogTitle("PIN");
    				      lipukaApplication.setCurrentDialogMsg("Enter PIN");
    				showDialog(Main.DIALOG_PIN_ID);	
    				}

    			}else{
    				lipukaApplication.setCurrentDialogTitle("Validation Error");
    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    			}

    		}else if (arg0.getId() == R.id.start_date_button){
    			currentDateField = startDate;
	        	showDialog(Main.DATE_DIALOG_ID);
    	    }else if (arg0.getId() == R.id.end_date_button){
        			currentDateField = endDate;
    	        	showDialog(Main.DATE_DIALOG_ID);
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
    	        case Main.DATE_DIALOG_ID:
    	        	dialog = new EcobankDatePickerDialog(this);

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
    	        case Main.DATE_DIALOG_ID:

    	        //	EcobankDatePickerDialog dpd = (EcobankDatePickerDialog)dialog;
    	        
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
    	    
    	    public class MyOnItemSelectedListener implements OnItemSelectedListener {

    	        public void onItemSelected(AdapterView<?> parent,
    	            View view, int pos, long id) {
    	        	idTypeSelected = parent.getItemAtPosition(pos).toString();
    	        }
    	        public void onNothingSelected(AdapterView parent) {
    	          // Do nothing.
    	        }
    	    }
    	    
    	    public class EcobankDateFieldListener implements View.OnTouchListener{

                public EcobankDateFieldListener(){

                }

    		@Override
    		public boolean onTouch(View v, MotionEvent event) {
    			//Log.d(SalamaSureMain.TAG, "View ID: "+((LipukaEditText)v).getID()); 
    				EditText editText = (EditText)v;
    				currentDateField = editText;
    				activityDateListener.setEditText(editText);
    	        	showDialog(Main.DATE_DIALOG_ID);	
    			
    			return true;
    		}
        }
    	    
    		@Override
    	    public void onUserInteraction()
    	    {
    	        super.onUserInteraction();
    	        lipukaApplication.touch();
    	    }
    		
    		public void setDate(String date){
    			currentDateField.setText(date);		
    		}
    	    }
