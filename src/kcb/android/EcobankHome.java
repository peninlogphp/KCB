package kcb.android;

import kcb.android.R;
import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

import java.util.ArrayList;

import lipuka.android.model.Bank;
import lipuka.android.model.BankAccount;
import lipuka.android.model.Navigation;
import lipuka.android.model.ProfileXMLParser;
import lipuka.android.model.synchronization.BackgroundTaskDone;
import lipuka.android.util.Validators;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ProfileUpdateDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.adapter.AccountsListAdapter;
import lipuka.android.view.adapter.ForexListAdapter;
import lipuka.android.view.adapter.LanguageListAdapter;
import lipuka.android.view.anim.LipukaAnim;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class EcobankHome extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
	
	public static final  String TAG = "DeftUI";
	private ScrollView main;
	
	LipukaApplication lipukaApplication;

	Button signIn, signUp;
	EditText username;
	EditText password;
	EditText cardNumber;
	EditText authenticationCode;
	RelativeLayout help;
	ImageButton closeHelp;
	LanguageListAdapter adapter;
	String helpFile;
	boolean setCurrentActivityNull = true;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

      try{  lipukaApplication = (LipukaApplication)getApplication();
        lipukaApplication.initHome();
        
        if(lipukaApplication.getPref("language") != null){ 
  	    	
   			createSignInUp();
   			
}else{
    setContentView(R.layout.ecobank_home);
    TextView title = (TextView)findViewById(R.id.title);
    title.setText("Select Language");
    ListView listView = (ListView)findViewById(R.id.lipuka_list_view);
	
	ArrayList<LipukaListItem> items= new ArrayList<LipukaListItem>();
	items.add(new LipukaListItem("0", "English", "USD"));
	items.add(new LipukaListItem("0", "Arabic", "EUR"));
	items.add(new LipukaListItem("0", "Hindi", "GBP"));
	items.add(new LipukaListItem("0", "Tamil", "ZAR"));
	items.add(new LipukaListItem("0", "Malayalam", "TZS"));
	items.add(new LipukaListItem("0", "Urdu", "TZS"));
	items.add(new LipukaListItem("0", "Bengali", "TZS"));
	items.add(new LipukaListItem("0", "Filipino - Tagalog", "TZS"));
	items.add(new LipukaListItem("0", "Chinese", "TZS"));
	items.add(new LipukaListItem("0", "Sinhala", "TZS"));
	items.add(new LipukaListItem("0", "Indonesia", "TZS"));
	items.add(new LipukaListItem("0", "French", "TZS"));
	items.add(new LipukaListItem("0", "Swahili", "TZS"));
	items.add(new LipukaListItem("0", "Russian", "TZS"));
	items.add(new LipukaListItem("0", "Thai", "TZS"));
	items.add(new LipukaListItem("0", "Indonesian", "TZS"));     
	items.add(new LipukaListItem("0", "Nepali", "TZS"));      

	adapter = new LanguageListAdapter(this, items);
	listView.setAdapter(adapter);
	Log.d(Main.TAG, "created listview");

	AnimationSet set = new AnimationSet(true);

    Animation animation = new AlphaAnimation(0.0f, 1.0f);
    animation.setDuration(50);
    set.addAnimation(animation);

    animation = new TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
        Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
    );
    animation.setDuration(100);
    set.addAnimation(animation);

    LayoutAnimationController controller =
            new LayoutAnimationController(set, 0.5f);
    listView.setLayoutAnimation(controller);
    listView.setOnItemClickListener(this);
    
	Button helpButton = (Button)findViewById(R.id.help);
    helpButton.setOnClickListener(this);
    Button homeButton = (Button)findViewById(R.id.home_button);
    homeButton.setOnClickListener(this);
    Button signOutButton = (Button)findViewById(R.id.sign_out);
    signOutButton.setOnClickListener(this);
    
    homeButton.setVisibility(View.GONE);
    signOutButton.setVisibility(View.GONE);
    LinearLayout dividerOne = (LinearLayout) findViewById(R.id.divider_one);
    dividerOne.setVisibility(View.GONE);
    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
    dividerTwo.setVisibility(View.GONE);
    
    helpFile = "file:///android_asset/activate.html";
	    
        help = (RelativeLayout) findViewById(R.id.help_layout);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    	myWebView.loadUrl(helpFile);
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
        
        lipukaApplication.setCurrentActivity(this);
	}  catch(Exception e){
		Log.d(Main.TAG, "creating ecobank home error", e);

   }
    }

    @Override
    protected void onStart() {
        super.onStart();
		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setActivityState(EcobankHome.class, true);
}

@Override
protected void onStop() {
    super.onStop();
	if(setCurrentActivityNull){
lipukaApplication.setActivityState(EcobankHome.class, false);
	}
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onDestroy() {
	    super.onDestroy();
		lipukaApplication.clearPIN();
		if(setCurrentActivityNull){
		lipukaApplication.setCurrentActivity(null);
		}
}	
	
	@Override
	public void onAttachedToWindow() {
	    super.onAttachedToWindow();
	    Window window = getWindow();
	    window.setFormat(PixelFormat.RGBA_8888);
	}	
	
	
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
            case R.id.action_bar_refresh:
                lipukaApplication.updateProfile();
 break; 
            default:
                return super.onHandleActionBarItemClick(item, position);
        }
        return true;
    }*/

@Override
public void onClick(View arg0) {
	if (signIn == arg0){
	     lipukaApplication.clearNavigationStack();

boolean valid = true;
	StringBuffer errorBuffer = new StringBuffer();
	String usernameStr = username.getText().toString();	
	String passwordStr = password.getText().toString();	

	if(usernameStr == null || usernameStr.length() == 0){
		valid = false;
errorBuffer.append("Username is missing\n");
	}
		if(passwordStr == null || passwordStr.length() == 0){
		valid = false;
errorBuffer.append("Password is missing\n");
	}	    			
	password.setText("");
	if(valid){
	    				StringBuffer payloadBuffer = new StringBuffer();
   	   				payloadBuffer.append(usernameStr+"|");	    				
	    				payloadBuffer.append(passwordStr+"|");
	    	   			Navigation nav = new Navigation();
	    			    nav.setPayload(payloadBuffer.toString());
	    				lipukaApplication.pushNavigationStack(nav);
	    				lipukaApplication.setPin(null);
	    				
Intent i = new Intent(EcobankHome.this, PaymaxHome.class);
		    			  startActivity(i);
		    		    	//lipukaApplication.putPref("signout", "signout");

	    			}else{
	    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			}	

}else if (signUp == arg0){
	
  lipukaApplication.clearNavigationStack();
	
	boolean valid = true;
	StringBuffer errorBuffer = new StringBuffer();
	String cardNumberStr = cardNumber.getText().toString();
	String authenticationCodeStr = authenticationCode.getText().toString();	

	if(cardNumberStr.length() < 16 || cardNumberStr.length() > 16){
		valid = false;
errorBuffer.append("Please enter a card number with 16 digits.\n");
	}
			if(authenticationCodeStr == null || authenticationCodeStr.length() == 0){
				valid = false;
				errorBuffer.append("Authentication code is missing.\n");
			}
			authenticationCode.setText("");

	    			if(valid){
	    				StringBuffer payloadBuffer = new StringBuffer();

   	   				payloadBuffer.append(cardNumberStr+"|");
	    				payloadBuffer.append(authenticationCodeStr+"|");		
   	   				    				
	    	   			Navigation nav = new Navigation();
	    			    nav.setPayload(payloadBuffer.toString());
	    				lipukaApplication.pushNavigationStack(nav);
	    				lipukaApplication.setPin(null);
	    				Intent i = new Intent(EcobankHome.this, CreateAccount.class);
		    			  startActivity(i);

	    			}else{
	    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			}	

}else if(arg0.getId() ==  R.id.forgot_password_link){
	Intent i = new Intent(this, ForgottenUsernamePassword.class);
	startActivity(i);
	}else if(arg0.getId() ==  R.id.help){
	//help.setVisibility(View.VISIBLE);
 //  help.startAnimation(LipukaAnim.inFromRightAnimation());
}else if(arg0.getId() ==  R.id.home_button){
/* Intent i = new Intent(this, PaymaxHome.class);
	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	startActivity(i);*/
}else if (closeHelp == arg0){
	help.startAnimation(LipukaAnim.outToRightAnimation());
	help.setVisibility(View.GONE);
	}else if (arg0.getId() == R.id.show_or_hide_sign_in){
		FrameLayout  signInLayout = (FrameLayout) findViewById(R.id.sign_in_layout);
		Drawable img = null;
if(signInLayout.isShown()){
signInLayout.setVisibility(View.GONE);
			img = getResources().getDrawable( R.drawable.show );
		}else{
			signInLayout.setVisibility(View.VISIBLE);
			img = getResources().getDrawable( R.drawable.hide );
    		FrameLayout  signUpLayout = (FrameLayout) findViewById(R.id.sign_up_layout);
    		signUpLayout.setVisibility(View.GONE);
    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	        Button showOrHideSignUp = (Button) findViewById(R.id.show_or_hide_sign_up);
	        showOrHideSignUp.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		
		}
((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

   	}else if (arg0.getId() == R.id.show_or_hide_sign_up){
   		FrameLayout  signUpLayout = (FrameLayout) findViewById(R.id.sign_up_layout);
   		Drawable img = null;
if(signUpLayout.isShown()){
signUpLayout.setVisibility(View.GONE);
   			img = getResources().getDrawable( R.drawable.show );
   		}else{
   			signUpLayout.setVisibility(View.VISIBLE);
   			img = getResources().getDrawable( R.drawable.hide );
    		FrameLayout  signInLayout = (FrameLayout) findViewById(R.id.sign_in_layout);
    		signInLayout.setVisibility(View.GONE);
    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	        Button showOrHideSignIn = (Button) findViewById(R.id.show_or_hide_sign_in);
	        showOrHideSignIn.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		
   		}
((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

	    	}
	
		}

public void createSignInUp(){
	 
	setContentView(R.layout.sign_in_up);
    
	username = (EditText) findViewById(R.id.username_field);
		        
		        password = (EditText) findViewById(R.id.password_field);
		       
		          password.setInputType(InputType.TYPE_CLASS_TEXT | 
		          		InputType.TYPE_TEXT_VARIATION_PASSWORD);	
		          
		          TextView forgotPasswordLink = (TextView) findViewById(R.id.forgot_password_link);
	     SpannableString mySpannableString = new SpannableString("Forgot username or password?");
	 			 mySpannableString.setSpan(new UnderlineSpan(), 0, mySpannableString.length(), 0);
	 			forgotPasswordLink.setText(mySpannableString);
	 			forgotPasswordLink.setOnClickListener(this);
		 
	 			 cardNumber = (EditText) findViewById(R.id.card_number_field);
			        
			        authenticationCode = (EditText) findViewById(R.id.authentication_code_field);
			        
			        cardNumber.setInputType(InputType.TYPE_CLASS_NUMBER);		
		     
			          authenticationCode.setInputType(InputType.TYPE_CLASS_TEXT | 
			          		InputType.TYPE_TEXT_VARIATION_PASSWORD);		      

		          
		        signIn = (Button) findViewById(R.id.sign_in);
		        signIn.setOnClickListener(this);
		        signUp = (Button) findViewById(R.id.sign_up);
		        signUp.setOnClickListener(this);
		        
		        Button showOrHideSignIn = (Button) findViewById(R.id.show_or_hide_sign_in);
		        showOrHideSignIn.setOnClickListener(this);
		        Button showOrHideSignUp = (Button) findViewById(R.id.show_or_hide_sign_up);
		        showOrHideSignUp.setOnClickListener(this);	      
		   	
		   	Button helpButton = (Button)findViewById(R.id.help);
			    helpButton.setOnClickListener(this);
			    Button homeButton = (Button)findViewById(R.id.home_button);
			    homeButton.setOnClickListener(this);
			    Button signOutButton = (Button)findViewById(R.id.sign_out);
			    signOutButton.setOnClickListener(this);
			    
			    homeButton.setVisibility(View.GONE);
			    signOutButton.setVisibility(View.GONE);
			    LinearLayout dividerOne = (LinearLayout) findViewById(R.id.divider_one);
			    dividerOne.setVisibility(View.GONE);
			    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
			    dividerTwo.setVisibility(View.GONE);
			    
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
}

@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	Intent i = new Intent(EcobankHome.this, SignInUp.class);
	  startActivity(i);
	  LipukaListItem item = (LipukaListItem)adapter.getItem(position);
	  lipukaApplication.putPref("language", item.getValue());
	/*lipukaApplication.setCurrentBank(new Bank("Ecobank", LipukaApplication.CLIENT_ID));

	BankAccount account = (BankAccount)adapter.getItem(position);	

	lipukaApplication.setAccount(LipukaApplication.CLIENT_ID+  "|"+account.getId() + "|"+account.getAlias());
	
	if(!lipukaApplication.parsePinStatus(LipukaApplication.CLIENT_ID)){ 
		if(!lipukaApplication.getOTP()){ 
    		
    		Intent i = new Intent(EcobankHome.this, OneTimePINActivity.class);
    	     startActivity(i);
return;
}
}
	Log.d(Main.TAG, "account type: "+account.getType()); 

	if(lipukaApplication.loadServiceXml(LipukaApplication.CLIENT_ID+"_"+account.getType())){
		Intent i = new Intent(EcobankHome.this, Main.class);
		  startActivity(i);
	}else{
		Toast toast = Toast.makeText(EcobankHome.this, "Service not available", Toast.LENGTH_LONG);
		toast.show();
	}
    return;	*/
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
    	pud.setMessage(lipukaApplication.getCurrentDialogMsg()+"\nThe application will restart when you click OK");
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
    case Main.DIALOG_CONFIRM_ID:
    	ConfirmationDialog cfd = new ConfirmationDialog(this);
    	cfd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	cfd.setMessage(lipukaApplication.getCurrentDialogMsg());
    	dialog = cfd;
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
    case Main.DIALOG_PROFILE_UPDATE_MSG_ID:
    	ProfileUpdateDialog pud = (ProfileUpdateDialog)dialog;
    	pud.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	pud.setMessage(lipukaApplication.getCurrentDialogMsg()+"\nThe application will restart when you click OK");
    	pud.setSuccessful(true);
    	break;
    case Main.DIALOG_PROFILE_UPDATE_ERROR_ID:
    	pud = (ProfileUpdateDialog)dialog;
    	pud.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	pud.setMessage(lipukaApplication.getCurrentDialogMsg());
    	pud.setSuccessful(false);

    	break;
    case Main.DIALOG_PROGRESS_ID:
    	CustomProgressDialog pd = (CustomProgressDialog)dialog;
ProgressBar pb = (ProgressBar)pd.findViewById(R.id.progressbar_default);
pb.setVisibility(View.GONE);
pb.setVisibility(View.VISIBLE);
    	break;
    case Main.DIALOG_CONFIRM_ID:
    	ConfirmationDialog cfd = (ConfirmationDialog)dialog;
    	cfd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	cfd.setMessage(lipukaApplication.getCurrentDialogMsg());
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

class AccountButtonListener implements View.OnClickListener{
	
	@Override
	public void onClick(View arg0) {

		lipukaApplication.setCurrentBank(new Bank("Ecobank", LipukaApplication.CLIENT_ID));

		BankAccount account = (BankAccount)arg0.getTag();	

		lipukaApplication.setAccount(LipukaApplication.CLIENT_ID+  "|"+account.getId() + "|"+account.getAlias());
		
		lipukaApplication.putPref("accountName", account.getAlias());
		/*if(!lipukaApplication.parsePinStatus(LipukaApplication.CLIENT_ID)){ 
			if(!lipukaApplication.getOTP()){ 
	    		
	    		Intent i = new Intent(EcobankHome.this, OneTimePINActivity.class);
	    	     startActivity(i);
	return;
	}
	}*/
	
		/*if(lipukaApplication.loadServiceXml(LipukaApplication.CLIENT_ID+"_"+account.getType())){
			Intent i = new Intent(EcobankHome.this, Main.class);
			  startActivity(i);
		}else{
			Toast toast = Toast.makeText(EcobankHome.this, "Service not available", Toast.LENGTH_LONG);
			toast.show();
		}*/
		Intent i = new Intent(EcobankHome.this, EcobankMain.class);
		  startActivity(i);
		
			}
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    //MenuInflater inflater = getMenuInflater();
   // inflater.inflate(R.menu.bank_accounts_menu, menu);
    return true;
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
    case R.id.update_profile:
    	if(lipukaApplication.isSavedProfile()){
    		lipukaApplication.updateProfile();
    	}else{
            Toast.makeText(this, "Please activate the application first", Toast.LENGTH_LONG).show();
    	}
      	return true;
    case R.id.help:
    	help.setVisibility(View.VISIBLE);
    	help.startAnimation(LipukaAnim.inFromRightAnimation());   
    	return true;
    default:
        return super.onOptionsItemSelected(item);
    }
} 
@Override
public void onUserInteraction()
{
    super.onUserInteraction();
    lipukaApplication.touch();
}

public class ConfirmationDialog extends Dialog implements OnClickListener {
	Button yesButton;
	Button noButton;
	TextView title;
	TextView message;
	public ConfirmationDialog(Context context) {
	super(context);

	/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	/** Design the dialog in main.xml file */
	setContentView(R.layout.confirmation_dialog);
	yesButton = (Button) findViewById(R.id.yes_button);
	yesButton.setOnClickListener(this);
	noButton = (Button) findViewById(R.id.no_button);
	noButton.setOnClickListener(this);
	title = (TextView) findViewById(R.id.title);
	message = (TextView) findViewById(R.id.message);

	}

	@Override
	public void onClick(View v) {
	/** When OK Button is clicked, dismiss the dialog */
	if (v == yesButton){
		lipukaApplication.setProfileID(0);
	dismiss();
}else if (v == noButton){
	dismiss();
	}
	finish();

	}

	public void setCustomTitle(String title) {
	this.title.setText(title);
	}
	public void setMessage(String message) {
		this.message.setText(message);
		}
	}
public void setSetCurrentActivityNull(boolean setCurrentActivityNull) {
	this.setCurrentActivityNull = setCurrentActivityNull;
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
	if(lipukaApplication.getProfileID() != 0){
	lipukaApplication.setCurrentDialogTitle("Sign Out");
   	lipukaApplication.setCurrentDialogMsg("Would you like to sign out?");
	lipukaApplication.showDialog(Main.DIALOG_CONFIRM_ID);
	}else{
		super.onBackPressed();
	}
}
}