package kcb.android;

import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;

import java.util.ArrayList;
import java.util.List;

import kcb.android.R;
import lipuka.android.data.HomeItem;
import lipuka.android.model.Bank;
import lipuka.android.model.BankAccount;
import lipuka.android.view.ChequeNumberDialog;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.FTInputDialog;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.SingleInputDialog;
import lipuka.android.view.adapter.AccountsListAdapter;
import lipuka.android.view.adapter.EcobankMainAdapter;
import lipuka.android.view.anim.LipukaAnim;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class EcobankMain extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{
	
	public static final  String TAG = "DeftUI";
	private ScrollView main;
	
	LipukaApplication lipukaApplication;

	Button activate, register, info;

	EditText phoneNo;
	AccountsListAdapter adapter;
	RelativeLayout help, about;
	ImageButton closeAbout;;
	//TextView accountName;
	//ImageView bankIcon;
	String helpFile;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        lipukaApplication = (LipukaApplication)getApplication();
        lipukaApplication.initHome();
lipukaApplication.setServiceVersion("1");
  /*      if(lipukaApplication.isSavedProfile()){ 
  	    	ProfileXMLParser profileXMLParser = new ProfileXMLParser(lipukaApplication);
   			createList(profileXMLParser.parseAccounts(lipukaApplication.getProfileXML()));
   			helpFile = "file:///android_asset/bankaccounts.html";
}else{
    setActionBarContentView(R.layout.ecobank_home);
    main = (ScrollView)findViewById(R.id.ScrollView01);
    activate = (Button)findViewById(R.id.activate);
    activate.setOnClickListener(this);
    register = (Button)findViewById(R.id.register);
    register.setOnClickListener(this);
    info = (Button)findViewById(R.id.info);
    info.setOnClickListener(this);
		helpFile = "file:///android_asset/activate.html";
}
*/
        
		//start temp
        setContentView(R.layout.ecobank_main);
       

        ListView listView = (ListView) findViewById(R.id.lipuka_list_view);
        List<HomeItem> homeItemList = new ArrayList<HomeItem>();
        homeItemList.add(new HomeItem(0, "", "Balance", 0, true));
        homeItemList.add(new HomeItem(0, "", "Mini Statement", 0, true));
        homeItemList.add(new HomeItem(0, "", "Airtime Topup", 0, true));
        homeItemList.add(new HomeItem(0, "", "Transfer Funds", 0, true));
        homeItemList.add(new HomeItem(0, "", "Transfer to M-pesa", 0, true));
        homeItemList.add(new HomeItem(0, "", "Pay Bills", 0, true));
        homeItemList.add(new HomeItem(0, "", "Withdraw Cash", 0, true));
        homeItemList.add(new HomeItem(0, "", "Cheque Book Requests", 0, true));
        homeItemList.add(new HomeItem(0, "", "Full Statement", 0, true));
        homeItemList.add(new HomeItem(0, "", "Forex Rates", 0, true));
        homeItemList.add(new HomeItem(0, "", "Stop Cheque", 0, true));       
        homeItemList.add(new HomeItem(0, "", "Change PIN", 0, true));
        //homeItemList.add(new HomeItem(0, "", "Contact Us", 0, true));
        homeItemList.add(new HomeItem(0, "", "Settings", 0, true));

        //listView.setAdapter(new EcobankMainAdapter(this, homeItemList));
        listView.setOnItemClickListener(this);
        
       // accountName = (TextView) findViewById(R.id.account_name);
        //bankIcon = (ImageView) findViewById(R.id.bank_icon);
        helpFile = "file:///android_asset/main.html";
        
		
		//end temp
      /*  addActionBarItem(getActionBar()
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
        
        about = (RelativeLayout) findViewById(R.id.about_layout);
        WebView aboutWebView = (WebView) findViewById(R.id.about_webview);
        WebSettings aboutWebSettings = aboutWebView.getSettings();
        aboutWebSettings.setJavaScriptEnabled(true);
        aboutWebView.loadUrl("file:///android_asset/about.html");
        aboutWebView.setBackgroundColor(0);

        closeAbout = (ImageButton) findViewById(R.id.close_about);
        closeAbout.setOnClickListener(this); 
        
        lipukaApplication.setCurrentActivity(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setActivityState(EcobankMain.class, true);
		String accountNameStr = lipukaApplication.getPref("accountName");
		if(accountNameStr != null){
	       //accountName.setText(lipukaApplication.getPref("accountName"));	
	        setTitle(lipukaApplication.getPref("accountName"));
		}else{
	        //accountName.setText("Set up an account");		
			setTitle("Set up an account");
		}
	}

@Override
protected void onStop() {
    super.onStop();
	lipukaApplication.setActivityState(EcobankMain.class, false);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.about:
        	about.setVisibility(View.VISIBLE);
        	about.startAnimation(LipukaAnim.inFromRightAnimation());   
        	return true;
        case R.id.help:
        	help.setVisibility(View.VISIBLE);
        	help.startAnimation(LipukaAnim.inFromRightAnimation());   
        	return true;
        case R.id.reset_pin:
            lipukaApplication.clearPIN();   
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    } 
	@Override
	public void onAttachedToWindow() {
	    super.onAttachedToWindow();
	    Window window = getWindow();
	    window.setFormat(PixelFormat.RGBA_8888);
	}	
	
	
   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        }*/
	
   /* @Override
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

@Override
public void onClick(View arg0) {
	if (activate == arg0){
    	lipukaApplication.activate2();
	}
	else if (register == arg0){
	
		Intent i = new Intent(EcobankMain.this, SelfActivation.class);
 		  startActivity(i);
	}else if(arg0.getId() ==  R.id.help){
		help.setVisibility(View.VISIBLE);
        help.startAnimation(LipukaAnim.inFromRightAnimation());
	}else if(arg0.getId() ==  R.id.home_button){
	 Intent i = new Intent(this, EcobankHome.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}
	else if (closeAbout == arg0){
		about.startAnimation(LipukaAnim.outToRightAnimation());
		about.setVisibility(View.GONE);
    	}
	
		}

public void createList(ArrayList<BankAccount> items){
	 
	  setContentView(R.layout.accounts_list);

		LinearLayout accountsList = (LinearLayout)findViewById(R.id.accounts_list);
		AccountButtonListener accountButtonListener = new AccountButtonListener();
		for(BankAccount item: items){
			LinearLayout accountButtonLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.account_button, null);	
Button button = (Button)accountButtonLayout.getChildAt(0);
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
			button.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );
			accountsList.addView(accountButtonLayout);
		}

		Log.d(Main.TAG, "created accounts list");

		
}

@Override
public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	lipukaApplication.setService(position);	

switch(position){
case 0:
	lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/balance11.php");
	if(lipukaApplication.getPin() != null)	{
		lipukaApplication.executeService();
	}else{
		lipukaApplication.setCurrentDialogTitle("PIN");
	      lipukaApplication.setCurrentDialogMsg("Enter PIN");
	showDialog(Main.DIALOG_PIN_ID);	
	}
    lipukaApplication.clearNavigationStack();

		break;
case 1:
	lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/ministmt11.php");
	if(lipukaApplication.getPin() != null)	{
		lipukaApplication.executeService();
	}else{
		lipukaApplication.setCurrentDialogTitle("PIN");
	      lipukaApplication.setCurrentDialogMsg("Enter PIN");
	showDialog(Main.DIALOG_PIN_ID);	
	}
    lipukaApplication.clearNavigationStack();

	break;
	
case 2:
	lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/topup11.php");
	Intent i = new Intent(EcobankMain.this, Topup.class);
    startActivity(i);	
    lipukaApplication.clearNavigationStack();
		break;
	case 3:
		lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/transferfunds11.php");
		//lipukaApplication.setCurrentURL("http://10.100.100.23/lipuka/processorScripts/transferfunds11.php");
		i = new Intent(EcobankMain.this, TransferFunds.class);
	    startActivity(i);
	    lipukaApplication.clearNavigationStack();
	    break;
	case 4:
		lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/mpesaft11.php");
		lipukaApplication.setCurrentDialogTitle("Account to M-pesa Transfer");
	    lipukaApplication.setCurrentDialogMsg("Enter amount to transfer to your m-pesa number");
	showDialog(Main.DIALOG_SINGLE_INPUT_ID);
	lipukaApplication.setPin(null);
    lipukaApplication.clearNavigationStack();
		//i = new Intent(EcobankMain.this, AutoCompleteTest.class);
	    //startActivity(i);
		break;
	case 5:
		lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/paybill11.php");
	i = new Intent(EcobankMain.this, PayBill.class);
	    startActivity(i);	
	    lipukaApplication.clearNavigationStack();
		break;
	case 6:
	i = new Intent(EcobankMain.this, WithdrawCash.class);
	    startActivity(i);	
	    lipukaApplication.clearNavigationStack();
		break;
		case 7:
		lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/chequebk11.php");
		i = new Intent(EcobankMain.this, ChequeBookRequest.class);
	    startActivity(i);	
	    lipukaApplication.clearNavigationStack();
		break;
	case 8:
		i = new Intent(EcobankMain.this, FullStmt.class);
	    startActivity(i);
	    lipukaApplication.clearNavigationStack();
		break;
	case 9:
		i = new Intent(EcobankMain.this, Forex.class);
	    startActivity(i);
	    lipukaApplication.clearNavigationStack();
		break;
	case 10:
		lipukaApplication.setCurrentURL("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/stopcheque11.php");
		lipukaApplication.setCurrentDialogTitle("Stop Cheque");
	    lipukaApplication.setCurrentDialogMsg("Enter cheque number");
	showDialog(Main.DIALOG_CHEQUE_NUMBER_ID);
	lipukaApplication.setPin(null);
lipukaApplication.clearNavigationStack();
		break;
	case 11:
i = new Intent(EcobankMain.this, ChangePIN.class);
	     startActivity(i);
	     lipukaApplication.clearNavigationStack();
		break;
	case 12:
		i = new Intent(EcobankMain.this, Accounts.class);
	     startActivity(i);
		break;
		default:
			
	} 

	/*lipukaApplication.setCurrentBank(new Bank("Ecobank", 10));

	BankAccount account = (BankAccount)adapter.getItem(position);	

	lipukaApplication.setAccount(10+  "|"+account.getId() + "|"+account.getAlias());
	
	if(!lipukaApplication.parsePinStatus(10)){ 
		if(!lipukaApplication.getOTP()){ 
    		
    		Intent i = new Intent(LipukaHome.this, OneTimePINActivity.class);
    	     startActivity(i);
return;
}
}
	
	if(lipukaApplication.loadServiceXml(10+"_"+account.getType())){
		Intent i = new Intent(LipukaHome.this, Main.class);
		  startActivity(i);
	}else{
		Toast toast = Toast.makeText(LipukaHome.this, "Service not available", Toast.LENGTH_LONG);
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
    case Main.DIALOG_SINGLE_INPUT_ID:
    	SingleInputDialog sid = new SingleInputDialog(this);
    	sid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	sid.setMessage(lipukaApplication.getCurrentDialogMsg());
    	dialog = sid;
    	break;
    case Main.DIALOG_FT_INPUT_ID:
    	FTInputDialog ftid = new FTInputDialog(this);
    	ftid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	ftid.setMessage(lipukaApplication.getCurrentDialogMsg());
    	dialog = ftid;
    	break;
    case Main.DIALOG_CHEQUE_NUMBER_ID:
    	ChequeNumberDialog cnd = new ChequeNumberDialog(this);
    	cnd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	cnd.setMessage(lipukaApplication.getCurrentDialogMsg());
    	dialog = cnd;
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
    case Main.DIALOG_SINGLE_INPUT_ID:
    	SingleInputDialog sid = (SingleInputDialog)dialog;
    	sid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	sid.setMessage(lipukaApplication.getCurrentDialogMsg());
    	break;
    case Main.DIALOG_FT_INPUT_ID:
    	FTInputDialog ftid = (FTInputDialog)dialog;
    	ftid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	ftid.setMessage(lipukaApplication.getCurrentDialogMsg());
    	break;
    case Main.DIALOG_CHEQUE_NUMBER_ID:
    	ChequeNumberDialog cnd = (ChequeNumberDialog)dialog;
    	cnd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
    	cnd.setMessage(lipukaApplication.getCurrentDialogMsg());
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

		lipukaApplication.setCurrentBank(new Bank("Ecobank", 10));

		BankAccount account = (BankAccount)arg0.getTag();	

		lipukaApplication.setAccount(10+  "|"+account.getId() + "|"+account.getAlias());
		
		if(!lipukaApplication.parsePinStatus(10)){ 
			if(!lipukaApplication.getOTP()){ 
	    		
	    		Intent i = new Intent(EcobankMain.this, OneTimePINActivity.class);
	    	     startActivity(i);
	return;
	}
	}
		
		if(lipukaApplication.loadServiceXml(10+"_"+account.getType())){
			Intent i = new Intent(EcobankMain.this, Main.class);
			  startActivity(i);
		}else{
			Toast toast = Toast.makeText(EcobankMain.this, "Service not available", Toast.LENGTH_LONG);
			toast.show();
		}
		
			}
}

@Override
public void onUserInteraction()
{
    super.onUserInteraction();
    lipukaApplication.touch();
}
}