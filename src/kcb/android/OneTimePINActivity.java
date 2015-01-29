package kcb.android;

import greendroid.app.GDActivity;
import kcb.android.R;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.ProfileUpdateDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class OneTimePINActivity extends GDActivity implements OnClickListener{
    /** Called when the activity is first created. */
	public static final int DIALOG_MSG_ID = 0;
	public static final int DIALOG_ERROR_ID = 1;
	public static final String KEY_PIN = "PIN";
	
	LipukaApplication lipukaApplication;
	private EditText oneTimePinField;
	private EditText newPinField;
	private EditText confirmPinField;

	private Button pinSubmit;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.one_time_pin_layout);
        lipukaApplication = (LipukaApplication)getApplication();
        oneTimePinField = (EditText) findViewById(R.id.one_time_pin_field);
        oneTimePinField.setInputType(InputType.TYPE_CLASS_TEXT | 
        		InputType.TYPE_TEXT_VARIATION_PASSWORD);		
        InputFilter[] filterArray = new InputFilter[1];
 	   filterArray[0] = new InputFilter.LengthFilter(4);
 	  oneTimePinField.setFilters(filterArray);
 	  
 	 newPinField = (EditText) findViewById(R.id.new_pin_field);
 	newPinField.setInputType(InputType.TYPE_CLASS_TEXT | 
     		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
     
     confirmPinField = (EditText) findViewById(R.id.confirm_pin_field);
     confirmPinField.setInputType(InputType.TYPE_CLASS_TEXT | 
     		InputType.TYPE_TEXT_VARIATION_PASSWORD);	

        pinSubmit = (Button) findViewById(R.id.pin_submit);
        pinSubmit.setOnClickListener(this);
        
    }
	
    
    @Override
    protected void onStart() {
        super.onStart();
		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setActivityState(OneTimePINActivity.class, true);
	}
    @Override
    protected void onStop() {
        super.onStop();
    	lipukaApplication.setActivityState(OneTimePINActivity.class, false);
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
        case Main.DIALOG_PROFILE_UPDATE_MSG_ID:
        	ProfileUpdateDialog pud = new ProfileUpdateDialog(this);
        	pud.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	pud.setMessage(lipukaApplication.getCurrentDialogMsg()+"\nLipuka will restart when you click OK");
        	pud.setSuccessful(true);
dialog = pud;
        	break;
        case Main.DIALOG_PROFILE_UPDATE_ERROR_ID:
        	pud = new ProfileUpdateDialog(this);
        	pud.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	pud.setMessage(lipukaApplication.getCurrentDialogMsg());
        	pud.setSuccessful(false);
        	dialog = pud;
        	break;
        case Main.DIALOG_PROGRESS_ID:
        	//builder = new AlertDialog.Builder(this);

        	CustomProgressDialog pd = new CustomProgressDialog(this);
        	dialog = pd;

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
        case Main.DIALOG_PROFILE_UPDATE_MSG_ID:
        	ProfileUpdateDialog pud = (ProfileUpdateDialog)dialog;
        	pud.setTitle(lipukaApplication.getCurrentDialogTitle());
        	pud.setMessage(lipukaApplication.getCurrentDialogMsg()+"\nLipuka will restart when you click OK");
        	pud.setSuccessful(true);
        	break;
        case Main.DIALOG_PROFILE_UPDATE_ERROR_ID:
        	pud = (ProfileUpdateDialog)dialog;
        	pud.setTitle(lipukaApplication.getCurrentDialogTitle());
        	pud.setMessage(lipukaApplication.getCurrentDialogMsg());
        	pud.setSuccessful(false);

        	break;
        case Main.DIALOG_PROGRESS_ID:
        	CustomProgressDialog pd = (CustomProgressDialog)dialog;
ProgressBar pb = (ProgressBar)pd.findViewById(R.id.progressbar_default);
pb.setVisibility(View.GONE);
pb.setVisibility(View.VISIBLE);
        	break;
        default:
            dialog = null;
        }
    }

	public void onClick(View arg0) {
		
		 switch (arg0.getId()) {
		 case R.id.pin_submit:
				String oneTimePin = oneTimePinField.getText().toString();
				String newPin = newPinField.getText().toString();
				String confirmPin = confirmPinField.getText().toString();
			
					if(oneTimePin.length() <= 8 && oneTimePin.length() >= 4 && newPin.length() <=8 && newPin.length() >= 4 && confirmPin.length() <= 8 && confirmPin.length() >= 4){
						if(newPin.equals(confirmPin)){
							lipukaApplication.changeOneTimePin(oneTimePin, newPin);
						}else{
							lipukaApplication.setCurrentDialogTitle("Mismatch");
							lipukaApplication.setCurrentDialogMsg("New PIN values provided must match");	
							showDialog(DIALOG_ERROR_ID);
						}
					}else{
						lipukaApplication.setCurrentDialogTitle("Missing Values");
						lipukaApplication.setCurrentDialogMsg("Please fill all the fields provided, PIN should be between 4 and 8 characters long");

		showDialog(DIALOG_ERROR_ID);
					}	    
					break;
				
	        default:
	            return;
	        }		
	}
	
}