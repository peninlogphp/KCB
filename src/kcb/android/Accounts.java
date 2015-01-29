package kcb.android;

import kcb.android.R;
import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;


import java.util.ArrayList;

import kcb.android.EcobankHome.AccountButtonListener;






import lipuka.android.model.Bank;
import lipuka.android.model.BankAccount;
import lipuka.android.model.ProfileXMLParser;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.ProfileUpdateDialog;
import lipuka.android.view.adapter.AccountsListAdapter;
import lipuka.android.view.anim.LipukaAnim;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class Accounts extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
	
	public static final  String TAG = "DeftUI";
	private ScrollView main;
	
	LipukaApplication lipukaApplication;

	Button activate, register, info, update, helpButton;

	EditText phoneNo;
	AccountsListAdapter adapter;
	RelativeLayout help;
	String helpFile;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      try{  lipukaApplication = (LipukaApplication)getApplication();
        
  	    	ProfileXMLParser profileXMLParser = new ProfileXMLParser(lipukaApplication);
  	    	
   			createList(profileXMLParser.parseAccounts(lipukaApplication.getProfileXML()));
   			helpFile = "file:///android_asset/bankaccounts.html";
   			
   		/* addActionBarItem(getActionBar()
                 .newActionBarItem(NormalActionBarItem.class)
                 .setDrawable(new ActionBarDrawable(this, R.drawable.gd_action_bar_refresh)), R.id.action_bar_refresh);        


      
        addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
                .setDrawable(new ActionBarDrawable(this, R.drawable.gd_action_bar_help)), R.id.action_bar_view_help);        
	*/ 	
        
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
        
        lipukaApplication.setCurrentActivity(this);
	}  catch(Exception e){
		Log.d(Main.TAG, "creating ecobank home error", e);

   }
    }

    @Override
    protected void onStart() {
        super.onStart();
		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setActivityState(Accounts.class, true);
	}

@Override
protected void onStop() {
    super.onStop();
	lipukaApplication.setActivityState(Accounts.class, false);
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
	
 /*   @Override
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
	if (activate == arg0){
    	lipukaApplication.activate2();
	}
	else if (register == arg0){
	
		Intent i = new Intent(Accounts.this, SelfActivation.class);
 		  startActivity(i);
	}
	else if (helpButton == arg0){		
		help.setVisibility(View.VISIBLE);
        help.startAnimation(LipukaAnim.inFromRightAnimation());
	}
	else if (update == arg0){		
		lipukaApplication.updateProfile();
	}
		}

public void createList(ArrayList<BankAccount> items){
	 
	  setContentView(R.layout.accounts_list);


		LinearLayout accountsList = (LinearLayout)findViewById(R.id.accounts_list);
		AccountButtonListener accountButtonListener = new AccountButtonListener();
		for(BankAccount item: items){
			LinearLayout accountButtonLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.account_button, null);	
Button button = (Button)accountButtonLayout.findViewById(R.id.account_button);
			button.setOnClickListener(accountButtonListener);
			button.setTag(item);
			button.setText(item.getAlias());
			Drawable img = null;
			if(item.getType().equals("CBS")){
				img = getResources().getDrawable( R.drawable.current_account_icon );
			}else if(item.getType().equals("CREDIT")){
				img = getResources().getDrawable( R.drawable.saving_account_icon );
			}else if(item.getType().equals("DEBIT")){
				img = getResources().getDrawable( R.drawable.card_icon);
			}
			Drawable listItemArrow = getResources().getDrawable( R.drawable.listitem_arrow );

			button.setCompoundDrawablesWithIntrinsicBounds( img, null, listItemArrow, null );
			accountsList.addView(accountButtonLayout);
		}

		update = (Button)findViewById(R.id.update);
		update.setOnClickListener(this);
	    helpButton = (Button)findViewById(R.id.help);
	    helpButton.setOnClickListener(this);
		Log.d(Main.TAG, "created accounts list");
		
}

@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

	lipukaApplication.setCurrentBank(new Bank("Ecobank", LipukaApplication.CLIENT_ID));

	BankAccount account = (BankAccount)adapter.getItem(position);	

	lipukaApplication.setAccount(LipukaApplication.CLIENT_ID+  "|"+account.getId() + "|"+account.getAlias());
	
	if(!lipukaApplication.parsePinStatus(LipukaApplication.CLIENT_ID)){ 
		if(!lipukaApplication.getOTP()){ 
    		
    		Intent i = new Intent(Accounts.this, OneTimePINActivity.class);
    	     startActivity(i);
return;
}
}
	Log.d(Main.TAG, "account type: "+account.getType()); 

	if(lipukaApplication.loadServiceXml(LipukaApplication.CLIENT_ID+"_"+account.getType())){
		Intent i = new Intent(Accounts.this, Main.class);
		  startActivity(i);
	}else{
		Toast toast = Toast.makeText(Accounts.this, "Service not available", Toast.LENGTH_LONG);
		toast.show();
	}
    return;	
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
		Intent i = new Intent(Accounts.this, EcobankMain.class);
		  startActivity(i);
		
			}
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.bank_accounts_menu, menu);
    return true;
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
    case R.id.update_profile:
        lipukaApplication.updateProfile();
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
}