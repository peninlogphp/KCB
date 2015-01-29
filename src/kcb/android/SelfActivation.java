package kcb.android;


import java.util.Calendar;

import kcb.android.R;
import lipuka.android.model.ActivityDateListener;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.anim.LipukaAnim;

import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
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

public class SelfActivation extends GDActivity implements OnClickListener{
   
Button register;
EditText accNo;
EditText idNo;
EditText transactionAmt;
EditText transactionDate;
EditText surname;
EditText middleName;
EditText firstName;
EditText mobileNo;
EditText dateOfBirth;

RelativeLayout help;
ImageButton closeHelp;
String idTypeSelected;
byte idType;
LipukaApplication lipukaApplication;

ActivityDateListener activityDateListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lipukaApplication = (LipukaApplication)getApplication();

        setActionBarContentView(R.layout.self_activation);
       
        accNo = (EditText)findViewById(R.id.accountno_field);
        accNo.setInputType(InputType.TYPE_CLASS_NUMBER);

        idNo = (EditText)findViewById(R.id.id_field);
        
        Spinner spinner = (Spinner) findViewById(R.id.id_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.id_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
 
        transactionAmt = (EditText)findViewById(R.id.transaction_amount_field);
        transactionAmt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        transactionDate = (EditText)findViewById(R.id.transaction_date_field);
        transactionDate.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        transactionDate.setOnTouchListener(new EcobankDateFieldListener());
        surname = (EditText)findViewById(R.id.surname_field);
        middleName = (EditText)findViewById(R.id.middlename_field);

        firstName = (EditText)findViewById(R.id.firstname_field);
        mobileNo = (EditText)findViewById(R.id.mobileno_field);
        mobileNo.setInputType(InputType.TYPE_CLASS_PHONE);

        dateOfBirth = (EditText)findViewById(R.id.dateOfBirth_field);

        dateOfBirth.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        
             dateOfBirth.setOnTouchListener(new EcobankDateFieldListener());
             register = (Button)findViewById(R.id.register);
       register.setOnClickListener(this);
       
       activityDateListener = new ActivityDateListener();
       
      // Log.d(SalamaSureMain.TAG, "jst after buttons");

       addActionBarItem(getActionBar()
               .newActionBarItem(NormalActionBarItem.class)
               .setDrawable(new ActionBarDrawable(this, R.drawable.gd_action_bar_help)), R.id.action_bar_view_help);        
	
       help = (RelativeLayout) findViewById(R.id.help_layout);
       WebView myWebView = (WebView) findViewById(R.id.webview);
       WebSettings webSettings = myWebView.getSettings();
       webSettings.setJavaScriptEnabled(true);
   	myWebView.loadUrl("file:///android_asset/self_activation.html");
   	myWebView.setBackgroundColor(0);

       closeHelp = (ImageButton) findViewById(R.id.close);
       closeHelp.setOnClickListener(this); 
       
		lipukaApplication.setCurrentActivity(this);
	 	
    }
  
    @Override
    protected void onStart() {
        super.onStart();
		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setActivityState(SelfActivation.class, true);
		}
   
    @Override
    protected void onStop() {
        super.onStop();
		lipukaApplication.setActivityState(SelfActivation.class, false);
    }
    @Override
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

    		public void onClick(View arg0) {
    		if (register == arg0){
    			
    			String accNoStr = accNo.getText().toString();
String idNoStr = idNo.getText().toString();
String transactionAmtStr = transactionAmt.getText().toString();
String transactionDateStr = transactionDate.getText().toString();
String surnameStr = surname.getText().toString();
String firstNameStr = firstName.getText().toString();
String mobileNoStr = mobileNo.getText().toString();

String dateOfBirthStr = dateOfBirth.getText().toString();

    			boolean valid = true;
    			StringBuffer errorBuffer = new StringBuffer();
    			if(accNoStr == null || accNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter Account Number\n");
    			}
    			if(idNoStr == null || idNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter ID/Passport Number\n");
    			}
    			if(transactionAmtStr == null || transactionAmtStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter your last transaction amount\n");
    			}
    			if(transactionDateStr == null || transactionDateStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter your last transaction date\n");
    			}
    			if(surnameStr == null || surnameStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter surname\n");
    			}
    			
    			if(firstNameStr == null || firstNameStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter first name\n");
    			}
    			if(mobileNoStr == null || mobileNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter mobile number\n");
    			}
    			if(dateOfBirthStr == null || dateOfBirthStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Enter date of birth\n");
    			}
    			
                if(mobileNoStr != null && mobileNoStr.length() > 0){
                    mobileNoStr = lipukaApplication.ensureCountryCode(mobileNoStr);
 if(mobileNoStr == null){
	 errorBuffer.append("Enter a valid mobile number\n");
valid = false;
}
}
    			if(valid){
    				if(idTypeSelected.equals("National ID number")){
    					idType = 1;
    				}else if(idTypeSelected.equals("Passport number")){
    					idType = 2;    					
    				}
    				StringBuffer payloadBuffer = new StringBuffer();
    				
    				payloadBuffer.append(accNoStr+"|");
    				payloadBuffer.append(idNoStr+"|");
    				payloadBuffer.append(idType+"|");
    				payloadBuffer.append(transactionAmtStr+"|");
    				payloadBuffer.append(transactionDateStr+"|");
    				payloadBuffer.append(surnameStr+"|");
    				payloadBuffer.append(middleName.getText().toString()+"|");
    				payloadBuffer.append(firstNameStr+"|");
    				payloadBuffer.append(mobileNoStr+"|");
    				payloadBuffer.append(dateOfBirthStr+"|");
    				payloadBuffer.append(lipukaApplication.generateRequestId()+"|");
    				
                	lipukaApplication.selfActivate(payloadBuffer.toString());


    			}else{
    				lipukaApplication.setCurrentDialogTitle("Validation Error");
    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    			}

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
    	        	 final Calendar c = Calendar.getInstance();
    	             int mYear = c.get(Calendar.YEAR);
    	             int mMonth = c.get(Calendar.MONTH);
    	             int mDay = c.get(Calendar.DAY_OF_MONTH);
    	        	dialog = new DatePickerDialog(this, activityDateListener, mYear, mMonth, mDay);

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

    	        	DatePickerDialog dpd = (DatePickerDialog)dialog;
    	        	final Calendar c = Calendar.getInstance();
    	            int mYear = c.get(Calendar.YEAR);
    	            int mMonth = c.get(Calendar.MONTH);
    	            int mDay = c.get(Calendar.DAY_OF_MONTH);
    	            dpd.updateDate(mYear, mMonth, mDay);
    	        
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
    				activityDateListener.setEditText(editText);
    	        	showDialog(Main.DATE_DIALOG_ID);	
    			
    			return true;
    		}
        }
    	    }