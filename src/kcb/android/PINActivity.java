package kcb.android;

import greendroid.app.GDActivity;
import kcb.android.R;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class PINActivity extends GDActivity implements OnClickListener{
    /** Called when the activity is first created. */
	public static final int DIALOG_MSG_ID = 0;
	public static final int DIALOG_ERROR_ID = 1;
	public static final String KEY_PIN = "PIN";
	
	LipukaApplication lipukaApplication;
	private EditText pinField;
	private Button pinSubmit;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.pin_layout);
        lipukaApplication = (LipukaApplication)getApplication();
        pinField = (EditText) findViewById(R.id.pin_field);
        pinField.setInputType(InputType.TYPE_CLASS_TEXT | 
        		InputType.TYPE_TEXT_VARIATION_PASSWORD);		
       InputFilter[] filterArray = new InputFilter[1];
 	   filterArray[0] = new InputFilter.LengthFilter(8);
 	  pinField.setFilters(filterArray);

        pinSubmit = (Button) findViewById(R.id.pin_submit);
        pinSubmit.setOnClickListener(this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setActivityState(PINActivity.class, true);
	}

@Override
protected void onStop() {
    super.onStop();
	lipukaApplication.setActivityState(PINActivity.class, false);
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
	        default:
	            dialog = null;
	        }
	    }

	public void onClick(View arg0) {
		String txt = pinField.getText().toString();
	
			if(txt != null && txt.length() >= 4){
				Intent i = new Intent(this, Main.class);
			    i.putExtra(KEY_PIN, txt);
			            	   setResult(RESULT_OK, i);
				finish();
			}else{
				lipukaApplication.setCurrentDialogTitle("PIN Error");
	        	lipukaApplication.setCurrentDialogMsg(getString(R.string.pin_error));
showDialog(Main.DIALOG_ERROR_ID);
			}		
	}
	
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
  
    	Navigation nav = lipukaApplication.peekNavigationStack();
    	String act = nav.getActivity();
    	if (act != null){
    	if(!act.equals("main")){
       	 lipukaApplication.popNavigationStack();
    	}
    	}else{
          	 lipukaApplication.popNavigationStack();    		
    	}
    		 super.onBackPressed();
    	 
    }
}