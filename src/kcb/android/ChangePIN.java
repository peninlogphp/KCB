package kcb.android;

import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import kcb.android.R;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.ProfileUpdateDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.anim.LipukaAnim;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ChangePIN extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	public static final int DIALOG_MSG_ID = 0;
	public static final int DIALOG_ERROR_ID = 1;
	public static final String KEY_PIN = "PIN";
	
	LipukaApplication lipukaApplication;
	private EditText oneTimePinField;
	private EditText newPinField;
	private EditText confirmPinField;
	private CheckBox otp;

	private Button pinSubmit;
	RelativeLayout help;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.change_pin_layout);
        lipukaApplication = (LipukaApplication)getApplication();
        oneTimePinField = (EditText) findViewById(R.id.one_time_pin_field);
        oneTimePinField.setInputType(InputType.TYPE_CLASS_TEXT | 
        		InputType.TYPE_TEXT_VARIATION_PASSWORD);		
        InputFilter[] filterArray = new InputFilter[1];
 	   filterArray[0] = new InputFilter.LengthFilter(8);
 	  oneTimePinField.setFilters(filterArray);
 	  
 	 newPinField = (EditText) findViewById(R.id.new_pin_field);
 	newPinField.setInputType(InputType.TYPE_CLASS_TEXT | 
     		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
 	newPinField.setFilters(filterArray);

     confirmPinField = (EditText) findViewById(R.id.confirm_pin_field);
     confirmPinField.setInputType(InputType.TYPE_CLASS_TEXT | 
     		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
     confirmPinField.setFilters(filterArray);

     oneTimePinField.setInputType(InputType.TYPE_CLASS_NUMBER);
     oneTimePinField.setTransformationMethod(PasswordTransformationMethod.getInstance());
     oneTimePinField.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
     newPinField.setInputType(InputType.TYPE_CLASS_NUMBER);
     newPinField.setTransformationMethod(PasswordTransformationMethod.getInstance());
     newPinField.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
     confirmPinField.setInputType(InputType.TYPE_CLASS_NUMBER);
     confirmPinField.setTransformationMethod(PasswordTransformationMethod.getInstance());
     confirmPinField.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
     
        pinSubmit = (Button) findViewById(R.id.pin_submit);
        pinSubmit.setOnClickListener(this);
        
        /*addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
                .setDrawable(new ActionBarDrawable(this, R.drawable.gd_action_bar_help)), R.id.action_bar_view_help);        
	 	*/
        Button helpButton = (Button)findViewById(R.id.help);
	    helpButton.setOnClickListener(this);
	    Button homeButton = (Button)findViewById(R.id.home_button);
	    homeButton.setOnClickListener(this);
        help = (RelativeLayout) findViewById(R.id.help_layout);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    	myWebView.loadUrl("file:///android_asset/changepin.html");
    	myWebView.setBackgroundColor(0);

        ImageButton close = (ImageButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help.startAnimation(LipukaAnim.outToRightAnimation());
            	help.setVisibility(View.GONE);
            }
        });
        
    }
	
    
    @Override
    protected void onStart() {
        super.onStart();
		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setActivityState(ChangePIN.class, true);
	}
    @Override
    protected void onStop() {
        super.onStop();
    	lipukaApplication.setActivityState(ChangePIN.class, false);
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
//main.setVisibility(View.GONE);

                //startActivity(new Intent(Home.this, OneTimePINActivity.class));
            	 break;          

            default:
                return super.onHandleActionBarItemClick(item, position);
        }
        return true;
    }*/
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
        case Main.DIALOG_SERVICE_RESPONSE_ID:
        	ResponseDialog rd = (ResponseDialog)dialog;
        	rd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	rd.setMessage(lipukaApplication.getCurrentDialogMsg());
        	break;
        default:
            dialog = null;
        }
    }

	public void onClick(View arg0) {
		
		 switch (arg0.getId()) {
		 case R.id.pin_submit:
			    lipukaApplication.clearNavigationStack();

				String oneTimePin = oneTimePinField.getText().toString();
				String newPin = newPinField.getText().toString();
				String confirmPin = confirmPinField.getText().toString();
			
					if(oneTimePin.length() <= 8 && oneTimePin.length() >= 4 && newPin.length() <=8 && newPin.length() >= 4 && confirmPin.length() <= 8 && confirmPin.length() >= 4){
						if(newPin.equals(confirmPin)){
							oneTimePinField.setText("");
							newPinField.setText("");
							confirmPinField.setText("");

							StringBuffer payloadBuffer = new StringBuffer();		    				
		    				payloadBuffer.append(oneTimePin+"|");
		    				payloadBuffer.append(newPin+"|");
		    				payloadBuffer.append(confirmPin+"|");
		    				
		    	   			Navigation nav = new Navigation();
		    			    nav.setPayload(payloadBuffer.toString());
		    				lipukaApplication.pushNavigationStack(nav);
		    				
		    				lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/change_pin.php");
		    				//lipukaApplication.setCurrentURL("http://10.100.100.23/lipuka/processorScripts/change_pin.php");
lipukaApplication.executeService();			
						
							
						}else{
							lipukaApplication.setCurrentDialogTitle("Mismatch");
							lipukaApplication.setCurrentDialogMsg("New PIN values provided must match");	
							showDialog(DIALOG_ERROR_ID);
							newPinField.setText("");
							confirmPinField.setText("");
						}
					}else{
						lipukaApplication.setCurrentDialogTitle("Missing Values");
						lipukaApplication.setCurrentDialogMsg("Please fill all the fields provided, PIN should be between 4 and 8 characters long");

		showDialog(DIALOG_ERROR_ID);
					}	    
					break;
		 case R.id.help:
				help.setVisibility(View.VISIBLE);
		        help.startAnimation(LipukaAnim.inFromRightAnimation());
			break;
		 case R.id.home_button:
			 Intent i = new Intent(this, EcobankHome.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				break;
	        default:
	            return;
	        }		
	}
	@Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
        lipukaApplication.touch();
    }
	
}