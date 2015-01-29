package kcb.android;

import greendroid.app.GDApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import kcb.android.R;
import lipuka.android.model.Bank;
import lipuka.android.model.BankAccount;
import lipuka.android.model.DbHelper;
import lipuka.android.model.LipukaActionListener;
import lipuka.android.model.LipukaDefaultActivity;
import lipuka.android.model.LipukaList;
import lipuka.android.model.Navigation;
import lipuka.android.model.Notifications;
import lipuka.android.model.ProfileXMLParser;
import lipuka.android.model.ServiceXMLParser;
import lipuka.android.model.Waiter;
import lipuka.android.model.database.AsyncDatabaseRead;
import lipuka.android.model.database.DatabaseParams;
import lipuka.android.model.database.DatabaseReadTask;
import lipuka.android.model.database.DatabaseWriteTask;
import lipuka.android.model.database.DatabaseWriteThread;
import lipuka.android.model.database.handlers.FetchBranchLocationsHandler;
import lipuka.android.model.database.handlers.FetchLariAtmLocationsHandler;
import lipuka.android.model.database.handlers.FetchMoneyGramLocationsHandler;
import lipuka.android.model.database.handlers.FetchOtherBanksAtmLocationsHandler;
import lipuka.android.model.database.handlers.FetchServicesHandler;
import lipuka.android.model.database.handlers.SaveBranchLocationsHandler;
import lipuka.android.model.database.handlers.SaveLariAtmLocationsHandler;
import lipuka.android.model.database.handlers.SaveAppDataHandler;
import lipuka.android.model.database.handlers.SaveGenreHandler;
import lipuka.android.model.database.handlers.SaveOtherBanksAtmLocationsHandler;
import lipuka.android.model.database.handlers.SaveProfileHandler;
import lipuka.android.model.database.handlers.SaveProfileUpdateHandler;
import lipuka.android.model.database.handlers.SaveProfileUpdateInBgHandler;
import lipuka.android.model.database.handlers.SavePublicKeyHandler;
import lipuka.android.model.database.handlers.SaveServiceHandler;
import lipuka.android.model.database.handlers.SaveServiceUpdateHandler;
import lipuka.android.model.database.handlers.SaveSongsHandler;
import lipuka.android.model.database.handlers.SetSubscribtionHandler;
import lipuka.android.model.responsehandlers.ActivationResponseHandler;
import lipuka.android.model.responsehandlers.ChangeOtpResponseHandler;
import lipuka.android.model.responsehandlers.CheckServiceXmlResponseHandler;
import lipuka.android.model.responsehandlers.ConsumeServiceHandler;
import lipuka.android.model.responsehandlers.DownloadProfileAfterSAResponseHandler;
import lipuka.android.model.responsehandlers.DownloadProfileResponseHandler;
import lipuka.android.model.responsehandlers.DownloadServiceXmlResponseHandler;
import lipuka.android.model.responsehandlers.FetchBranchLocationsResponseHandler;
import lipuka.android.model.responsehandlers.FetchLariAtmLocationsResponseHandler;
import lipuka.android.model.responsehandlers.FetchOtherBanksAtmLocationsResponseHandler;
import lipuka.android.model.responsehandlers.RemoteRequestResponseHandler;
import lipuka.android.model.responsehandlers.SelfActivateResponseHandler;
import lipuka.android.model.responsehandlers.SignInHandler;
import lipuka.android.model.responsehandlers.UpdateAppDataResponseHandler;
import lipuka.android.model.responsehandlers.UpdateProfileInBgResponseHandler;
import lipuka.android.model.responsehandlers.UpdateProfileResponseHandler;
import lipuka.android.model.responsehandlers.UpdatePublicKeyResponseHandler;
import lipuka.android.model.responsehandlers.WithdrawCashHandler;
import lipuka.android.model.sms.ActivateSMS;
import lipuka.android.model.synchronization.BackgroundTaskDone;
import lipuka.android.security.AppAuth;
import lipuka.android.security.AsymmetricEncryption;
import lipuka.android.security.SymmetricEncryption;
import lipuka.android.util.Base64;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.LipukaNavigateButton;
import lipuka.android.view.LipukaSubmitButton;
import lipuka.android.view.SignInDialog;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class LipukaApplication extends Application  { // 1
private static final String TAG = LipukaApplication.class.getSimpleName();
public static final int CLIENT_ID = 11;
private static final String URL = "https://multimediapayments.com:44001/index.php";

private String MSISDN;
//initialized to prevent funds transfer activity from crashing when it is restarted after 
//being idle for long. Find a better way of handling this
private String account = "11|126";
private boolean savedProfile = false, savedServiceXml = false;
private String serviceXML;
private String profileXML;
private byte currentActivityType;
private WebView webView;
private boolean isWebView;
private String currentDialogTitle;
private String currentDialogMsg;
private Main mainActivity;
private Activity currentActivity;

private LipukaSubmitButton lipukaSubmitButton;
private LipukaNavigateButton lipukaNavigateButton;
private LipukaActionListener lipukaActionListener;
private List<LipukaListItem> lipukaListItems;
private ServiceXMLParser serviceXMLParser;
private LipukaList lipukaList;
private Stack<Navigation> navigationStack = new Stack<Navigation>();
private String pin = null;
private String imsi = null;
private DbHelper dbHelper;
private AsyncHttpClient asyncHttpClient;
private AsyncDatabaseRead asyncDatabaseRead;
private DatabaseWriteThread databaseWriteThread;
private HashMap<Class<?>, Boolean> activityStates;
AsymmetricEncryption asymmetricEncryption;
private String serviceVersion = null;
private String currentURL = null;
int service;
int dialogType;
private DownloadProfileResponseHandler downloadProfileResponseHandler = new DownloadProfileResponseHandler(this);

private byte accountCount = 0;
Bank currentBank;
private Waiter waiter;  //Thread which controls idle time
private String serviceID = null;
private JSONObject commonParams, payloadObject, profileData;
int profileID;

@Override
public void onCreate() { // 3
super.onCreate();
//this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
//this.prefs.registerOnSharedPreferenceChangeListener(this);
Log.i(TAG, "onCreated");
this.asyncHttpClient = new AsyncHttpClient(); // 
readIMSI();
dbHelper = new DbHelper(this);
dbHelper.open();
asyncDatabaseRead = new AsyncDatabaseRead();
databaseWriteThread = new DatabaseWriteThread();
databaseWriteThread.start();
activityStates = new HashMap<Class<?>, Boolean>();
//loadRawBytes();
}

@Override
public void onTerminate() { // 4
super.onTerminate();
Log.i(TAG, "onTerminated");
}
/*@Override
public Class<?> getHomeActivityClass() {
    return EcobankHome.class;
}*/
public void initApp(Main mainActivity){
	 this.mainActivity = mainActivity;
	 serviceXMLParser = new ServiceXMLParser(mainActivity, this);
	 lipukaActionListener = new LipukaActionListener(this, mainActivity);
}

public void initHome(){
	loadProfileXml();
	 if(savedProfile){
		 parseMSISDN();
	 }
	 if(asymmetricEncryption == null){
asymmetricEncryption = new AsymmetricEncryption(loadPublicKey());
	SymmetricEncryption.init();
	 }
	 if(waiter == null){
		 waiter=new Waiter(this, 5*60*1000); //5 mins
       waiter.start();
	 }
	// putPayload("app_data_version", String.valueOf(loadAppDataVersion()));
	// consumeService("27", new UpdateAppDataResponseHandler(this, null));
	}

public void setActivityState(Class<?> klass, boolean visible) { 
	this.activityStates.put(klass, visible);
	}
public boolean isActivityVisible(Class<?> klass) { 
	return this.activityStates.get(klass);
	}
public Bank getCurrentBank() { 
	return this.currentBank;
	}
public void setCurrentBank(Bank currentBank) { 
		this.currentBank = currentBank;
		}
public String getServiceXML() { 
	return this.serviceXML;
	}
public void setServiceXML(String serviceXML) { 
		this.serviceXML = serviceXML;
		}
public boolean isSavedProfile() { 
	return this.savedProfile;
	}
public void setSavedProfile(boolean savedProfile) { 
	this.savedProfile = savedProfile;
	}
	public boolean isSavedServiceXml() { 
		return this.savedServiceXml;
		}
		public void setSavedServiceXml(boolean savedServiceXml) { 
			this.savedServiceXml = savedServiceXml;
			}
	public synchronized void openDb() { 
		this.dbHelper.open();
		}
	public synchronized void closeDb() { 
		this.dbHelper.close();
		}

	public void setCurrentActivity(Activity activity) { 
		currentActivity = activity;
		}
	public void setMainActivity(Main activity) { 
		mainActivity = activity;
		}
	public void setAccountsCount(byte accountCount) { 
		this.accountCount = accountCount;
		}
		public synchronized byte decrementAccountCount() { 
return --accountCount;
}
	
public synchronized String loadXML(int resourceId) {
    // The InputStream opens the resourceId and sends it to the buffer
    InputStream is = this.getResources().openRawResource(resourceId);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String readLine = null;

    try {
        // While the BufferedReader readLine is not null 
        while ((readLine = br.readLine()) != null) {
        	sb.append(readLine);
    }

    // Close the InputStream and BufferedReader
    is.close();
    br.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
    return sb.toString();
}
public String loadPublicKeyFromRes(){
	InputStream in = getResources().openRawResource(R.raw.rsa_public_key);
    String contents = new String();
    try {
        int c;
        while ((c = in.read()) != -1) {
            contents += (char) c;
        }
    } catch (IOException e) {
        System.err.println("Could not read RSA key resource.");
    }
    return contents;
}
public RSAPrivateKey loadRawBytes() {
	RSAPrivateKey privKey = null;
    try {
       /* InputStream is = this.getResources().openRawResource(R.raw.host);
    	BufferedInputStream bis = new BufferedInputStream(is);
    	
    	byte[] privKeyBytes = new byte[is.available()];
    	bis.read(privKeyBytes);
    	bis.close();*/
    	byte[] privKeyBytes = Base64.decode(kaw());
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    	KeySpec ks = new PKCS8EncodedKeySpec(privKeyBytes);
    	privKey = (RSAPrivateKey) keyFactory.generatePrivate(ks);
       
       	//Log.d(Main.TAG, "Private key: "+Base64.encodeBytes(privKeyBytes));

    } catch (IOException e) {
       	Log.d(Main.TAG, "Loading private key IO exception", e);
    }
    
    catch (Exception e) {
       	Log.d(Main.TAG, "Loading private key exception", e);
    }
    return privKey;
}
public boolean loadServiceXml(String account){
	savedServiceXml = false;
		Cursor cursor= dbHelper.fetchServiceXML(imsi, account);
	if (cursor != null && cursor.moveToFirst()){
		serviceXML = cursor.getString(cursor.getColumnIndex(DbHelper.C_SERVICE_XML));
		//serviceXml = loadXML(R.raw.servicexml);
savedServiceXml = true;
}else{
		//serviceXml = loadXML(R.raw.servicexml);
	}
	
	cursor.close();
	return savedServiceXml;
}
public void loadProfileXml(){
   	Log.d(Main.TAG, "Entered loadProfile");
try{
   	Log.d(Main.TAG, " b4 loadProfile");

		if (getPref()){

		Cursor cursor= dbHelper.fetchProfileXML(imsi);

	if (cursor != null && cursor.moveToFirst()){
		profileXML = cursor.getString(cursor.getColumnIndex(DbHelper.C_PROFILE_XML));

		/*try { 
			this.profileXML = PBEEncryption.decrypt(getPBEParam(), profileXML);
	       }catch (Exception e){
	       	Log.d(Main.TAG, "PBE Encryption Error: ", e);
	       }*/
	   	Log.d(Main.TAG, " activated");

	savedProfile = true;
	}else{
	   	Log.d(Main.TAG, "no profile xml");
	}	
	cursor.close();
	}
	else{
	   	Log.d(Main.TAG, "not activated");
	}
}catch (Exception e){
   	Log.d(Main.TAG, "loadProfile Error: ", e);
   }
	Log.d(Main.TAG, "left loadProfile");

}
public String loadPublicKey(){
try{
		Cursor cursor= dbHelper.fetchPublicKey();

	if (cursor != null && cursor.moveToFirst()){
		return cursor.getString(cursor.getColumnIndex(DbHelper.C_PUBLIC_KEY));
	}
	cursor.close();

}catch (Exception e){
   	Log.d(Main.TAG, "load public key error: ", e);
   }
return loadPublicKeyFromRes();
}
public int loadPublicKeyVersion(){
	try{
			Cursor cursor= dbHelper.fetchPublicKey();

		if (cursor != null && cursor.moveToFirst()){
			return cursor.getInt(cursor.getColumnIndex(DbHelper.C_PUBLIC_KEY_VERSION));
		}
		cursor.close();

	}catch (Exception e){
	   	Log.d(Main.TAG, "load public key version error: ", e);
	   }
	return 0;
	}
public String loadEftData(){
	try{
			Cursor cursor= dbHelper.fetchAppData();

		if (cursor != null && cursor.moveToFirst()){
			return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
		}
		cursor.close();

	}catch (Exception e){
	   	Log.d(Main.TAG, "load eft data error: ", e);
	   }
	return loadXML(R.raw.ecobank_eft_branches_temp);
	}
	public int loadAppDataVersion(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getInt(cursor.getColumnIndex(DbHelper.C_APP_DATA_VERSION));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load app data version error: ", e);
		   }
		return 0;
		}
	public String loadSourceAcounts(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.source_accounts);
		}
	public String loadSavedAcounts(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.saved_accounts);
		}
	public String loadCountries(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.countries);
		}
	public String loadSavedBills(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.saved_bills);
		}
	public String loadMerchants(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.merchants);
		}
	public String loadCurrencies(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.currencies);
		}
	public String loadAllCountries(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.all_countries);
		}
	public String loadSavedBeneficiaries(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.saved_beneficiaries);
		}
	public String loadMobileMoneyProviders(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.mobile_money_providers);
		}
	public String loadMobileNetworks(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.mobile_network_providers);
		}
	public String loadLocations(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.locations);
		}
	public String loadEftCurrencies(){
		try{
				Cursor cursor= dbHelper.fetchAppData();

			if (cursor != null && cursor.moveToFirst()){
				return cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			cursor.close();

		}catch (Exception e){
		   	Log.d(Main.TAG, "load eft data error: ", e);
		   }
		return loadXML(R.raw.eft_currencies);
		}
	public String loadSpinnerData(int spinnerData){
	
		return loadXML(spinnerData);
		}
	public String loadAppData(String name, int spinnerData){
		String data = null;
try{
				Cursor cursor= dbHelper.fetchAppData();
			if (cursor != null && cursor.moveToFirst()){
				data = cursor.getString(cursor.getColumnIndex(DbHelper.C_APP_DATA));
			}
			if (cursor != null){
				cursor.close();
			}
			if (data != null){
				JSONObject dataObject = new JSONObject(data);
				return dataObject.getJSONArray(name).toString();
			}

		}catch (Exception e){
		   	Log.d(Main.TAG, "load app data error: ", e);
		   }
		 switch(spinnerData){
			case R.raw.source_of_funds:
			case R.raw.purpose_of_funds:
				data = loadXML(spinnerData);
				break;
			case R.raw.all_countries:
				data = loadXML(R.raw.app_data);
				break;
			default:
			}
		return data;
		}
public Cursor loadChannels(){
		Cursor cursor= dbHelper.fetchSubscribtions();
	return cursor;
}
public String getMSISDN(){
	 return MSISDN;
 }
public void setMSISDN(String MSISDN){
 	 this.MSISDN = MSISDN;
    	Log.d(Main.TAG, "Set MSIDN: "+this.MSISDN);

  }
public String getProfileXML(){
	if(profileXML == null){
	initHome();	
	}
	 return profileXML;
}
public String getAccount(){
	 return this.account;
}
public void setAccount(String account){
	  this.account = account;
}
public boolean isWebView(){
 	 return isWebView;
  }
public void setIsWebView(boolean isWebView){
  	 this.isWebView = isWebView;
   }
   public WebView getWebView(){
     	 return webView;
      }
   public void setWebView(WebView webView){
   	 this.webView = webView;
    }
   public void setWebViewFlag(boolean isWebView){
	   	 this.isWebView = isWebView;
	    }
   public String getCurrentDialogTitle() { 
return this.currentDialogTitle;
	   }
   public void setCurrentDialogTitle(String currentDialogTitle) { // 5
this.currentDialogTitle = currentDialogTitle;
	   }
   public String getCurrentDialogMsg() { 
	   return this.currentDialogMsg;
	   	   }
	      public void setCurrentDialogMsg(String currentDialogMsg) { // 5
	   this.currentDialogMsg = currentDialogMsg;
	   	   }
	      public byte getCurrentActivityType(){
	      	 return currentActivityType;
	       }
	    public void setCurrentActivityType(byte currentActivityType){
	    	 this.currentActivityType = currentActivityType;
	     }
		      public Activity getActivity(){
			      	 return mainActivity;
			       }
		      public Activity getCurrentActivity(){
			      	 return currentActivity;
			       }
		      public int getDialogType(){
		    		 return this.dialogType;
		    	}	
		      public void setDialogType(int dialogType){
			    	 this.dialogType = dialogType;
			     }
			    public void showDialog(int dialogId){
			    	currentActivity.showDialog(dialogId);
			     }
			      public LipukaSubmitButton getLipukaSubmitButton(){
				      	 return lipukaSubmitButton;
				       }
				    public void setLipukaSubmitButton(LipukaSubmitButton lipukaSubmitButton){
				    	 this.lipukaSubmitButton = lipukaSubmitButton;
				     }
				      public LipukaNavigateButton getLipukaNavigateButton(){
					      	 return lipukaNavigateButton;
					       }
					    public void setLipukaNavigateButton(LipukaNavigateButton lipukaNavigateButton){
					    	 this.lipukaNavigateButton = lipukaNavigateButton;
					     }
				      public LipukaActionListener getLipukaActionListener(){
					      	 return lipukaActionListener;
					       }
				      public LipukaList getLipukaList(){
					      	 return lipukaList;
					       }
	
				      public void setLipukaList(LipukaList lipukaList){
					      	 this.lipukaList = lipukaList;
					       }
					     public List<LipukaListItem> getListItems(){
					    	 return lipukaListItems;
					     }
					     public void setListItems(List<LipukaListItem> lipukaListItems){
					    	 this.lipukaListItems = lipukaListItems;
					     }
					      public Stack<Navigation> getNavigationStack(){
						      	 return navigationStack;
						       }
					      public void pushNavigationStack(Navigation nav){
						      	 navigationStack.push(nav);
						       }
					      public void popNavigationStack(){
						      	 navigationStack.pop();
						       }
					      public Navigation peekNavigationStack(){
						      	 return navigationStack.peek();
						       }
					      public Navigation getNavigation(int i){
						      	 return navigationStack.get(i);
						       }
					      public void clearNavigationStack(){
						      	 navigationStack.clear();
						       }
					      public JSONObject getPayloadObject(){
						      	 return payloadObject;
						       }
					      public String getPayload(String name){
						      	 try {
									return payloadObject.getString(name);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Log.d(Main.TAG, "jsonError", e);
								}
								return "";
						       }
					      public void putPayload(String name, String value){
					    	  if(payloadObject == null){
					    		  payloadObject = new JSONObject();
					    	  }
						      	 try {
									payloadObject.put(name, value);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Log.d(Main.TAG, "jsonError", e);
								}
						       }
					      public void clearPayloadObject(){
					    	  payloadObject = new JSONObject();
						       }      
					      public String getPin() { 
						   	   return pin;
						   	   	   }
					      public void setPin(String pin) { 
				   	   this.pin = pin;
				   	   	   }
				     public void clearPIN() { 
					   	   this.pin = null;
					   	   	   }
				     public void setCurrentURL(String currentURL){
				   	  this.currentURL = currentURL;
				   }
				     public int getService() {
					 		return service;
					 	}
				     public void setService(int service){
					   	  this.service = service;
					   }
				     public String getServiceID() {
					 		return serviceID;
					 	}
				     public void setServiceID(String serviceID) {
					 		this.serviceID = serviceID;
					 	}
				     public int getProfileID() {
					 		return profileID;
					 	}
				     public void setProfileID(int profileID) {
					 		this.profileID = profileID;
					 	}
				     public JSONArray getProfileDataArray(String name){
					      	 try {
							return	profileData.getJSONArray(name);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								Log.d(Main.TAG, "jsonError", e);
							}
							return null;
					       }
				     public String getProfileData(String name){
				      	 try {
							return profileData.getString(name);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.d(Main.TAG, "jsonError", e);
						}
						return null;
				       }
				     public void setProfileData(JSONObject profileData){
				    	this.profileData = profileData;
					       }
				     public void putProfileData(String name, Object object){
				      	 try {
							profileData.put(name, object);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.d(Main.TAG, "jsonError", e);
						}
				       }
				    public void executeRemoteRequest(){
					     BackgroundTaskDone.setDone(false);
				    		RequestParams params = new RequestParams();
				    		String payload = getPayload()+generateRequestId();

				    		if(lipukaSubmitButton.getBearer() == LipukaSubmitButton.BEARER_HTTPS){
				    			byte[] plaintext = payload.getBytes();
					            byte[] ciphertext = null;
					            byte[] encryptedKey = null, encryptedIv = null;
					            encryptedKey = asymmetricEncryption.encrypt(SymmetricEncryption.aesKey);
					            encryptedIv = asymmetricEncryption.encrypt(SymmetricEncryption.aesInitV);

					            ciphertext = SymmetricEncryption.aesEncrypt(plaintext);
					    		params.put("param1", Base64.encodeBytes(encryptedKey));
					    		params.put("param2", Base64.encodeBytes(encryptedIv));

					            payload = Base64.encodeBytes(ciphertext);
					    	}
				    		params.put("param3", payload);
				    		params.put("param4", serviceVersion);

				    	if(lipukaSubmitButton.getBearer() == LipukaSubmitButton.BEARER_HTTP || lipukaSubmitButton.getBearer() == LipukaSubmitButton.BEARER_HTTPS){
				    		Log.d(Main.TAG, getPayload()); 
				    		Log.d(Main.TAG, lipukaSubmitButton.getAction()); 

				    		if(lipukaSubmitButton.getMethod() == LipukaSubmitButton.METHOD_POST){
				    			asyncHttpClient.post(lipukaSubmitButton.getAction(),
				params,
				new RemoteRequestResponseHandler(this, currentActivity));
	}else{
		asyncHttpClient.get(lipukaSubmitButton.getAction(),
					params,
					new RemoteRequestResponseHandler(this, currentActivity));			        		
	}
				    	}else{
			        		
			        	}
				     }
				    public void consumeService(){
					     BackgroundTaskDone.setDone(false);
				    		RequestParams params = new RequestParams();
				    		String payload = getPayload()+generateRequestId();
				    		Log.d(Main.TAG, payload); 

				    			byte[] plaintext = payload.getBytes();
					            byte[] ciphertext = null;
					            byte[] encryptedKey = null, encryptedIv = null;
					            encryptedKey = asymmetricEncryption.encrypt(SymmetricEncryption.aesKey);
					            encryptedIv = asymmetricEncryption.encrypt(SymmetricEncryption.aesInitV);

					            ciphertext = SymmetricEncryption.aesEncrypt(plaintext);
					    		params.put("param1", Base64.encodeBytes(encryptedKey));
					    		params.put("param2", Base64.encodeBytes(encryptedIv));

					            payload = Base64.encodeBytes(ciphertext);
				    		params.put("param3", payload);
				    		params.put("param4", serviceVersion);

				    		asyncHttpClient.post(currentURL,
				    				params,
				    				new ConsumeServiceHandler(this, currentActivity));

				     }
				    public void consumeService(String id, AsyncHttpResponseHandler responseHandler){
setServiceID(id);
				    	try{	//payload.append("|");
				    		if(commonParams == null){
				    			commonParams = new JSONObject();
				    			commonParams.put("api_key", "AP5J4RKDN56Y");
				    			commonParams.put("api_secret", "WHH57U8RIRKJJH");
				    		}
				    		if(responseHandler instanceof SignInHandler){
			    				commonParams.put("username", getPayloadObject().getString("username"));
				    			commonParams.put("password", getPayloadObject().getString("password"));		
			    			}	
				    		commonParams.put("service_id", id);
				    		commonParams.put("payload", getPayloadObject());

				    		asyncHttpClient.post(null, URL, 
				    				 new StringEntity(commonParams.toString()), "application/json",
				    				 responseHandler);
				    	}
				        catch (org.json.JSONException jsonError) {
				    	     Log.d(Main.TAG, "jsonError: ", jsonError);
				    } catch (UnsupportedEncodingException uee) {
				    	     Log.d(Main.TAG, "UnsupportedEncodingException: ", uee);
				    }	
		    	     Log.d(Main.TAG, "payload: "+ commonParams.toString());

				     }	
				    public void withdrawCash(){
					     BackgroundTaskDone.setDone(false);
				   		RequestParams params = new RequestParams();
				   		StringBuffer sb = new StringBuffer();
				 	   	  sb.append("AG_BANKING");
				 	   	  sb.append("|");
				 	   	  sb.append(MSISDN+"|");
				 	   	  sb.append(account+"|");
				 	   	  if(pin != null){
				 		    	  sb.append(pin+"|");
				 		    	  }
				 	   	  for(Navigation nav: navigationStack){
				 	   		  if(nav.getPayload() != null){
				 	   			  sb.append(nav.getPayload());
				 	   		  }
				 	   	  }
				 	   	  sb.append(generateRequestId());
				 	   	  sb.append("|");
				 	   	  sb.append(serviceID);

			    		String payload = sb.toString();
				   		Log.d(Main.TAG, payload); 

				   		byte[] plaintext = payload.getBytes();
					            byte[] ciphertext = null;
					            byte[] encryptedKey = null, encryptedIv = null;
					            encryptedKey = asymmetricEncryption.encrypt(SymmetricEncryption.aesKey);
					            encryptedIv = asymmetricEncryption.encrypt(SymmetricEncryption.aesInitV);

					            ciphertext = SymmetricEncryption.aesEncrypt(plaintext);
					    		params.put("101", Base64.encodeBytes(encryptedKey));
					    		params.put("102", Base64.encodeBytes(encryptedIv));

					            payload = Base64.encodeBytes(ciphertext);
				   		params.put("103", payload);
				   		params.put("104", serviceVersion);

				   		asyncHttpClient.post("http://zion.cellulant.com/ussdSyncAPI/appProxyScript.php",
				   				params,
				   				new WithdrawCashHandler(this, currentActivity));

				    }
				    public void parseRemoteActivity(String remoteActivity, boolean mainVisible){
serviceXMLParser.parseRemoteActivity(remoteActivity, mainVisible);
				     }
				    public void executeLocalRequest(String activity){

				        serviceXMLParser.parseLocalActivity(serviceXML, activity);
				     }
				    public void executeLocalRequest(int position){
			LipukaListItem listItem = lipukaListItems.get(position);

			executeLocalRequest(listItem.getValue());

			}
		public void parseMSISDN(){
			ProfileXMLParser profileXMLParser = new ProfileXMLParser(this);
			profileXMLParser.parseMSISDN(profileXML);
				    					     }
		public boolean parsePinStatus(int currentClientID){
			ProfileXMLParser profileXMLParser = new ProfileXMLParser(this);
			return profileXMLParser.parsePinStatus(profileXML, currentClientID);
				    					     }
		public List<String> parseNominations(){
			ProfileXMLParser profileXMLParser = new ProfileXMLParser(this);
			return profileXMLParser.parseNominations(profileXML, currentBank.getId());
				    					     }
		public List<LipukaListItem> parseEnrollments(){
			ProfileXMLParser profileXMLParser = new ProfileXMLParser(this);
			return profileXMLParser.parseEnrollments(profileXML, currentBank.getId());
			}
		public List<LipukaListItem> getCbsAccounts(){
			ArrayList<LipukaListItem> accounts = new ArrayList<LipukaListItem>();
			LipukaListItem lipukaListItem;
			for (BankAccount account: currentBank.getAccounts()){
				if(account.getType().equals("CBS")){
					lipukaListItem = new LipukaListItem();
					lipukaListItem.setText(account.getAlias());
					lipukaListItem.setValue(String.valueOf(account.getId()));

					accounts.add(lipukaListItem);
				}
			}
			return accounts;
				    					     }
		
		public List<LipukaListItem> getCustomerAccounts(){
			ArrayList<LipukaListItem> accounts = new ArrayList<LipukaListItem>();
			LipukaListItem lipukaListItem;
    		StringTokenizer tokens = new StringTokenizer(getAccount(), "|");
tokens.nextToken();
int currentAccountId = Integer.valueOf(tokens.nextToken());
			for (BankAccount account: currentBank.getAccounts()){
				if(account.getId() != currentAccountId){
					lipukaListItem = new LipukaListItem();
					lipukaListItem.setText(account.getAlias());
					lipukaListItem.setValue(String.valueOf(account.getId()));

					accounts.add(lipukaListItem);
				}
			}
			return accounts;
				    					     }
		
		public List<LipukaListItem> getOtherAccounts(){
			ArrayList<LipukaListItem> accounts = new ArrayList<LipukaListItem>();
			LipukaListItem lipukaListItem;
    		StringTokenizer tokens = new StringTokenizer(getAccount(), "|");
tokens.nextToken();
int currentAccountId = Integer.valueOf(tokens.nextToken());

ProfileXMLParser profileXMLParser = new ProfileXMLParser(this);
			
			for (BankAccount account: profileXMLParser.parseAccounts(getProfileXML())){
				if(account.getId() != currentAccountId){
					lipukaListItem = new LipukaListItem();
					lipukaListItem.setText(account.getAlias());
					lipukaListItem.setValue(account.getAlias());

					accounts.add(lipukaListItem);
				}
			}
			for (String nomination: parseNominations()){
					lipukaListItem = new LipukaListItem();
					lipukaListItem.setText(nomination);
					lipukaListItem.setValue(nomination);
					accounts.add(lipukaListItem);
			}
			return accounts;
				    					     }
		
	public void promptPIN(){
				        Intent i = new Intent(this, PINActivity.class);
				   mainActivity.startActivityForResult(i, Main.PROMPT_PIN);
;
				     }
				    public void handleResponse(String response){
setCurrentDialogTitle("Response");
setCurrentDialogMsg(response);
showDialog(Main.DIALOG_MSG_ID);
}
				    
				      public boolean handleBackButton(){
				    	  if (navigationStack.size() > 1){
					      	 navigationStack.pop();
					      	 Navigation nav = navigationStack.peek();
					      	 executeLocalRequest(nav.getActivity());
					      	 if (nav.getHashMap().size() > 0){
					     		LinearLayout linearLayout = (LinearLayout)mainActivity.findViewById(R.id.main_layout);

					      		 LipukaDefaultActivity.restoreData(linearLayout, nav.getHashMap());
 }
					      return true;
				    	  }else{
				    		  return false;
				    	  }
					       }
				      
				 /*public boolean restoreAfterActivityRestart(){
					// mainActivity
				    	  if (navigationStack.size() > 0){
					      	 Navigation nav = navigationStack.peek();
					      	 executeLocalRequest(nav.getActivity());
					      	 if (layout != null && nav.getHashMap().size() > 0){
					      		 LipukaDefaultActivity.restoreData(layout, nav.getHashMap());
 }
				    	  }
				    	  return true;
					       }*/
				 
				      public String getPayload(){
				    	  StringBuffer sb = new StringBuffer();
				    	  sb.append(MSISDN+"|");
				    	  sb.append(account+"|");
				    	  if(pin != null){
					    	  sb.append(pin+"|");
					    	  }
				    	  for(Navigation nav: navigationStack){
				    		  if(nav.getPayload() != null){
				    			  sb.append(nav.getPayload());
				    		  }
				    	  }
		
				    	  return sb.toString();
					       }
				      
		 public void showProgress(String msg){
			 currentActivity.showDialog(Main.DIALOG_PROGRESS_ID);
					//((MusicActivity) currentActivity).showLoader();
							     }
						    
						      
			public void dismissProgressDialog(){
				//((MusicActivity) currentActivity).dismissLoader();
				 currentActivity.dismissDialog(Main.DIALOG_PROGRESS_ID);
				 
			}
	public void readIMSI(){
		TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		imsi = tm.getSubscriberId();
		Log.d(Main.TAG, "imsi: "+imsi);

	}
	public String readIMEI(){
		TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		return tm.getDeviceId();

	}
	public String getIMSI(){
		return imsi;
	}
	private char[] getPBEParam(){
		String pbeParam = imsi;
		if (imsi.length() < 8) {
			StringBuilder sb = new StringBuilder();
int paddingLength = 8 - imsi.length();
		for (int i = 0; i < paddingLength; i++){
			sb.append("0");	
		}	
		pbeParam = pbeParam + sb.toString();
		}else if(imsi.length() > 8){
		pbeParam = imsi.substring(0, 8);	
		}
		return pbeParam.toCharArray();
	}
	public void activate(){
		RequestParams params = new RequestParams();
		//params.put("msisdn", MSISDN);
		params.put("imsi", imsi);
		
			//http://b-athletics.appspot.com/activate
		//http://10.100.100.23/lipuka/appActivation2.php
asyncHttpClient.post("http://10.100.100.23/lipuka/appActivation2.php",
				params,
				new ActivationResponseHandler(this, currentActivity));
        Log.d(Main.TAG, "Sent activation request");
	}
	public void activate2(){
		ActivateSMS activate = new ActivateSMS(this, currentActivity);
		activate.sendSMS("5500", "ECOTEST*"+imsi+"*"+readIMEI());
		
	}
	public void updateProfile(){
		ProfileXMLParser profileXMLParser = new ProfileXMLParser(this);
		profileXMLParser.parseMSISDN(profileXML);

		RequestParams params = new RequestParams();
		//params.put("msisdn", MSISDN);
		params.put("MSISDN", MSISDN);
		params.put("MESSAGE", "APPACT*"+imsi+"*"+readIMEI());
		
			//http://b-athletics.appspot.com/activate
		//http://10.100.100.23/lipuka/appActivation.php
asyncHttpClient.post("http://zion.cellulant.com/AndroidWallet/ECOBANK/profileUpdate.php",
				params,
				new UpdateProfileResponseHandler(this, currentActivity));
        Log.d(Main.TAG, "Sent update profile request. MSISDN is: "+MSISDN);
	}
	public void updateProfileInBg(){
		ProfileXMLParser profileXMLParser = new ProfileXMLParser(this);
		profileXMLParser.parseMSISDN(profileXML);

		RequestParams params = new RequestParams();
		//params.put("msisdn", MSISDN);
		params.put("MSISDN", MSISDN);
		params.put("MESSAGE", "APPACT*"+imsi+"*"+readIMEI());
		
			//http://b-athletics.appspot.com/activate
		//http://10.100.100.23/lipuka/appActivation.php
asyncHttpClient.post("http://zion.cellulant.com/AndroidWallet/ECOBANK/profileUpdate.php",
				params,
				new UpdateProfileInBgResponseHandler(this, currentActivity));
        Log.d(Main.TAG, "Sent update profile request. MSISDN is: "+MSISDN);
	}
	public void fetchProfile(JSONObject message){
		downloadProfileResponseHandler.setMessage(message);
try{		asyncHttpClient.get(message.getString("profile"), downloadProfileResponseHandler);
Log.d(Main.TAG, "profile link: "+message.getString("profile"));
}catch (org.json.JSONException jsonError) {
    Log.d(Main.TAG, "JSON Error", jsonError);
}
	     Log.d(Main.TAG, "Sent request to download profile");
	}
	public void fetchProfileAfterSA(String MSISDN){
		RequestParams params = new RequestParams();
		params.put("MSISDN", MSISDN);
			asyncHttpClient.post("http://10.100.100.23/lipuka/fetchprofile.php",
				params,
				new DownloadProfileAfterSAResponseHandler(this));

	     Log.d(Main.TAG, "Sent request to download profile");
	}
	public void fetchServiceXml(String url, String account){
		DownloadServiceXmlResponseHandler downloadServiceXmlResponseHandler = new DownloadServiceXmlResponseHandler(this);
	downloadServiceXmlResponseHandler.setAccount(account);
		asyncHttpClient.get(url,  downloadServiceXmlResponseHandler);
	     Log.d(Main.TAG, "Sent request to download service xml for account: "+account);

	}
	public void fetchLariAtmLocations(Activity activity) {

	try{	//payload.append("|");
		JSONObject params = new JSONObject();
   		params.put("api_key", "AP5J4RKDN56Y");
   		params.put("api_secret", "WHH57U8RIRKJJH");
   		params.put("service_id", "6");
   		params.put("username", "user1");
   		params.put("password", "user1");
		asyncHttpClient.post(null, URL, 
				 new StringEntity(params.toString()), "application/json",
				new FetchLariAtmLocationsResponseHandler(this, activity));
	}
    catch (org.json.JSONException jsonError) {
	     Log.d(Main.TAG, "jsonError: ", jsonError);
} catch (UnsupportedEncodingException uee) {
	     Log.d(Main.TAG, "UnsupportedEncodingException: ", uee);
}
	}
	public void fetchOtherBanksAtmLocations(Activity activity) {

		try{	//payload.append("|");
			JSONObject params = new JSONObject();
	   		params.put("api_key", "AP5J4RKDN56Y");
	   		params.put("bank_id", "2");
			asyncHttpClient.post(null, "http://69.195.198.55:8090/api/fetch_all_atms.php", 
					 new StringEntity(params.toString()), "application/json",
					new FetchOtherBanksAtmLocationsResponseHandler(this, activity));
		}
	    catch (org.json.JSONException jsonError) {
		     Log.d(Main.TAG, "jsonError: ", jsonError);
	} catch (UnsupportedEncodingException uee) {
		     Log.d(Main.TAG, "UnsupportedEncodingException: ", uee);
	}
	}
	public void fetchBranchLocations(Activity activity) {

		try{	//payload.append("|");
			JSONObject params = new JSONObject();
	   		params.put("api_key", "AP5J4RKDN56Y");
	   		params.put("bank_id", "1");
			asyncHttpClient.post(null, "http://69.195.198.55:8090/api/fetch_all_branches.php", 
					 new StringEntity(params.toString()), "application/json",
					new FetchBranchLocationsResponseHandler(this, activity));
		}
	    catch (org.json.JSONException jsonError) {
		     Log.d(Main.TAG, "jsonError: ", jsonError);
	} catch (UnsupportedEncodingException uee) {
		     Log.d(Main.TAG, "UnsupportedEncodingException: ", uee);
	}
	}
	public void fetchLariAtmLocationsLocally(Activity activity) {
		DatabaseParams dbParams = new DatabaseParams();

		DatabaseReadTask task = new DatabaseReadTask(dbHelper.getDatabase(),
				new FetchLariAtmLocationsHandler(this, activity), dbParams,
				DatabaseReadTask.FETCH_LARI_ATM_LOCATIONS);
		asyncDatabaseRead.submitTask(task);

	}
	public void fetchOtherBanksAtmLocationsLocally(Activity activity) {
		DatabaseParams dbParams = new DatabaseParams();

		DatabaseReadTask task = new DatabaseReadTask(dbHelper.getDatabase(),
				new FetchOtherBanksAtmLocationsHandler(this, activity), dbParams,
				DatabaseReadTask.FETCH_OTHER_BANKS_ATM_LOCATIONS);
		asyncDatabaseRead.submitTask(task);

	}
	public void fetchBranchLocationsLocally(Activity activity) {
		DatabaseParams dbParams = new DatabaseParams();

		DatabaseReadTask task = new DatabaseReadTask(dbHelper.getDatabase(),
				new FetchBranchLocationsHandler(this, activity), dbParams,
				DatabaseReadTask.FETCH_BRANCH_LOCATIONS);
		asyncDatabaseRead.submitTask(task);

	}
	public void fetchMoneyGramLocationsLocally(Activity activity) {
		DatabaseParams dbParams = new DatabaseParams();

		DatabaseReadTask task = new DatabaseReadTask(dbHelper.getDatabase(),
				new FetchMoneyGramLocationsHandler(this, activity), dbParams,
				DatabaseReadTask.FETCH_LARI_ATM_LOCATIONS);
		asyncDatabaseRead.submitTask(task);

	}
	public void checkServiceXml(String account, String version){
		RequestParams params = new RequestParams();
		params.put("account", account);
		params.put("version", version);

		asyncHttpClient.post("http://b-athletics.appspot.com/serviceupdate", params,
				new CheckServiceXmlResponseHandler(this, account));
	}
	
	public void changeOneTimePin(String oneTimePin, String newPin){
		RequestParams params = new RequestParams();

		StringBuffer payloadBuffer = new StringBuffer();
		
		payloadBuffer.append(MSISDN+"|");
		payloadBuffer.append(account+"|");
		payloadBuffer.append(oneTimePin+"|");
		payloadBuffer.append(newPin+"|");
		payloadBuffer.append(generateRequestId()+"|");

  	  
		String payload = payloadBuffer.toString();
		byte[] plaintext = payload.getBytes();
        byte[] ciphertext = null;
        byte[] encryptedKey = null, encryptedIv = null;
        encryptedKey = asymmetricEncryption.encrypt(SymmetricEncryption.aesKey);
        encryptedIv = asymmetricEncryption.encrypt(SymmetricEncryption.aesInitV);

        ciphertext = SymmetricEncryption.aesEncrypt(plaintext);
		params.put("param1", Base64.encodeBytes(encryptedKey));
		params.put("param2", Base64.encodeBytes(encryptedIv));

        payload = Base64.encodeBytes(ciphertext);
		params.put("param3", payload);

		asyncHttpClient.post("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/changeOtp.php", params,
				new ConsumeServiceHandler(this, currentActivity));
	}
	public void selfActivate(String payload){
		RequestParams params = new RequestParams();

  	  		byte[] plaintext = payload.getBytes();
        byte[] ciphertext = null;
        byte[] encryptedKey = null, encryptedIv = null;
        encryptedKey = asymmetricEncryption.encrypt(SymmetricEncryption.aesKey);
        encryptedIv = asymmetricEncryption.encrypt(SymmetricEncryption.aesInitV);

        ciphertext = SymmetricEncryption.aesEncrypt(plaintext);
		params.put("param1", Base64.encodeBytes(encryptedKey));
		params.put("param2", Base64.encodeBytes(encryptedIv));

        payload = Base64.encodeBytes(ciphertext);
		params.put("param3", payload);

		asyncHttpClient.post("http://10.100.100.23/lipuka/processorScripts/selfactivate.php", params,
				new SelfActivateResponseHandler(this, currentActivity));
	}
	public void updatePublicKey(Activity activity) {

		RequestParams params = new RequestParams();
		params.put("keyname", "ecobank");
   		params.put("version", String.valueOf(loadPublicKeyVersion()));

		asyncHttpClient.post("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/updatekey11.php", params,
				new UpdatePublicKeyResponseHandler(this, activity));
	}
	public void updateEftData(Activity activity) {

		RequestParams params = new RequestParams();
   		params.put("104", String.valueOf(loadAppDataVersion()));

		asyncHttpClient.post("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/updateEftData.php", params,
				new UpdateAppDataResponseHandler(this, activity));
	}
	public void fetchSongs(AsyncHttpResponseHandler handler, int index, String info){

		RequestParams params = new RequestParams();
		params.put("menu", "topsongs");
		//params.put("info", info);

		asyncHttpClient.get("http://lipuka.mobi/android",
				params, handler
				);
	}

	
	public void saveProfile(String profile, JSONObject message){
		DatabaseParams dbParams = new DatabaseParams();
				dbParams.addParam("imsi", imsi);
				/*try { 
					profile = PBEEncryption.encrypt(getPBEParam(), profile);
			       }catch (Exception e){
			       	Log.d(Main.TAG, "PBE Encryption Error: ", e);
			       }*/
				dbParams.addValue(DbHelper.C_IMSI, imsi);
		dbParams.addValue(DbHelper.C_PROFILE_XML, profile);

DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
		new SaveProfileHandler(this, message), dbParams,
		DatabaseWriteTask.INSERT_PROFILE_XML_ACTION);
databaseWriteThread.enqueueTask(task);

	}
	public void saveProfileUpdate(String profile, JSONObject accounts){
		DatabaseParams dbParams = new DatabaseParams();
				dbParams.addParam("imsi", imsi);
				/*try { 
					profile = PBEEncryption.encrypt(getPBEParam(), profile);
			       }catch (Exception e){
			       	Log.d(Main.TAG, "PBE Encryption Error: ", e);
			       }*/
		dbParams.addValue(DbHelper.C_PROFILE_XML, profile);
		JSONArray accountsArray = accounts.names();
		JSONArray batchValues = new JSONArray();
		JSONObject currentServiceXML = null;
		    // Log.d(Main.TAG, "Profile: "+ profile);

		int length = accountsArray.length();
		try {for(int i = 0; i < length; i++){
			currentServiceXML = new JSONObject();
			currentServiceXML.put("account", accountsArray.get(i));
			currentServiceXML.put("service_xml", accounts.get(accountsArray.get(i).toString()));
			currentServiceXML.put("timestamp", System.currentTimeMillis());
			currentServiceXML.put("current", 1);
		     Log.d(Main.TAG, accountsArray.get(i)+": "+ accounts.get(accountsArray.get(i).toString()));

			batchValues.put(currentServiceXML);		
		}
		}catch (org.json.JSONException jsonError) {
  		     Log.d(Main.TAG, "JSON error: ", jsonError);

       	}
		dbParams.setBatchValues(batchValues);
DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
		new SaveProfileUpdateHandler(this, currentActivity), dbParams,
		DatabaseWriteTask.UPDATE_PROFILE_XML_ACTION);
databaseWriteThread.enqueueTask(task);

	}
	public void saveProfileUpdateInBg(String profile, JSONObject accounts){
		DatabaseParams dbParams = new DatabaseParams();
				dbParams.addParam("imsi", imsi);
				/*try { 
					profile = PBEEncryption.encrypt(getPBEParam(), profile);
			       }catch (Exception e){
			       	Log.d(Main.TAG, "PBE Encryption Error: ", e);
			       }*/
		dbParams.addValue(DbHelper.C_PROFILE_XML, profile);
		JSONArray accountsArray = accounts.names();
		JSONArray batchValues = new JSONArray();
		JSONObject currentServiceXML = null;
		     //Log.d(Main.TAG, "Profile: "+ profile);

		int length = accountsArray.length();
		try {for(int i = 0; i < length; i++){
			currentServiceXML = new JSONObject();
			currentServiceXML.put("account", accountsArray.get(i));
			currentServiceXML.put("service_xml", accounts.get(accountsArray.get(i).toString()));
			currentServiceXML.put("timestamp", System.currentTimeMillis());
			currentServiceXML.put("current", 1);
		    // Log.d(Main.TAG, accountsArray.get(i)+": "+ accounts.get(accountsArray.get(i).toString()));

			batchValues.put(currentServiceXML);		
		}
		}catch (org.json.JSONException jsonError) {
  		     Log.d(Main.TAG, "JSON error: ", jsonError);

       	}
		dbParams.setBatchValues(batchValues);
DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
		new SaveProfileUpdateInBgHandler(this, currentActivity), dbParams,
		DatabaseWriteTask.UPDATE_PROFILE_XML_ACTION);
databaseWriteThread.enqueueTask(task);

	}
	public void savePublicKey(String publicKey, int publicKeyVersion){
		DatabaseParams dbParams = new DatabaseParams();
				
				dbParams.addValue(DbHelper.C_PUBLIC_KEY, publicKey);
		dbParams.addValue(DbHelper.C_PUBLIC_KEY_VERSION, String.valueOf(publicKeyVersion));

DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
		new SavePublicKeyHandler(this), dbParams,
		DatabaseWriteTask.SAVE_PUBLIC_KEY_ACTION);
databaseWriteThread.enqueueTask(task);

	}
	public void saveAppData(String appData, int appDataVersion){
		DatabaseParams dbParams = new DatabaseParams();
				
				dbParams.addValue(DbHelper.C_APP_DATA, appData);
		dbParams.addValue(DbHelper.C_APP_DATA_VERSION, String.valueOf(appDataVersion));

DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
		new SaveAppDataHandler(this), dbParams,
		DatabaseWriteTask.SAVE_APP_DATA_ACTION);
databaseWriteThread.enqueueTask(task);

	}
	public void saveServiceXml(String serviceXml, String account){
		DatabaseParams dbParams = new DatabaseParams();
		dbParams.addValue(DbHelper.C_IMSI, imsi);
		dbParams.addValue(DbHelper.C_ACCOUNT, account);
dbParams.addValue(DbHelper.C_SERVICE_XML, serviceXml);
dbParams.addValue(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
new SaveServiceHandler(this), dbParams,
DatabaseWriteTask.INSERT_SERVICE_XML_ACTION);
databaseWriteThread.enqueueTask(task);
	}
	public void saveServiceXmlUpdate(String serviceXml, String account){
		DatabaseParams dbParams = new DatabaseParams();
		dbParams.addParam(DbHelper.C_IMSI, imsi);
		dbParams.addParam(DbHelper.C_ACCOUNT, account);
dbParams.addValue(DbHelper.C_SERVICE_XML, serviceXml);
dbParams.addValue(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
new SaveServiceUpdateHandler(this), dbParams,
DatabaseWriteTask.UPDATE_SERVICE_XML_ACTION);
databaseWriteThread.enqueueTask(task);
	}
	public void saveLariAtmLocations(JSONArray locationsArray) {
		DatabaseParams dbParams = new DatabaseParams();
		dbParams.putBatchValues(DbHelper.TABLE_LARI_ATM_LOCATIONS, locationsArray);

		DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
				new SaveLariAtmLocationsHandler(this), dbParams,
				DatabaseWriteTask.SAVE_LARI_ATM_LOCATIONS_ACTION);
		databaseWriteThread.enqueueTask(task);
	}
	public void saveOtherBanksLocations(JSONArray locationsArray) {
		DatabaseParams dbParams = new DatabaseParams();
		dbParams.putBatchValues(DbHelper.TABLE_OTHER_BANKS_ATM_LOCATIONS, locationsArray);

		DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
				new SaveOtherBanksAtmLocationsHandler(this), dbParams,
				DatabaseWriteTask.SAVE_OTHER_BANKS_ATM_LOCATIONS_ACTION);
		databaseWriteThread.enqueueTask(task);
	}
	public void saveBranchLocations(JSONArray locationsArray) {
		DatabaseParams dbParams = new DatabaseParams();
		dbParams.putBatchValues(DbHelper.TABLE_BRANCH_LOCATIONS, locationsArray);

		DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
				new SaveBranchLocationsHandler(this), dbParams,
				DatabaseWriteTask.SAVE_BRANCH_LOCATIONS_ACTION);
		databaseWriteThread.enqueueTask(task);
	}
	public void fetchServices(){
        Log.d(Main.TAG, "fetching services");

DatabaseReadTask task = new DatabaseReadTask(dbHelper.getDatabase(),
		new FetchServicesHandler(this),
		null,
		DatabaseReadTask.FETCH_ALL_SERVICE_XML_ACTION);
asyncDatabaseRead.submitTask(task);

	}
	
	public void setSubscribtion(int channelId, int subscribed){
		DatabaseParams dbParams = new DatabaseParams();
				dbParams.addParam(DbHelper.C_CHANNEL_ID, String.valueOf(channelId));
				dbParams.addValue(DbHelper.C_SUBSCRIBED, String.valueOf(subscribed));

DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
		new SetSubscribtionHandler(this), dbParams,
		DatabaseWriteTask.SET_SUBSCRIBTION_ACTION);
databaseWriteThread.enqueueTask(task);

	}
	public void checkActivation(){
		if(decrementAccountCount() == 0){
			/*DatabaseParams dbParams = new DatabaseParams();
			dbParams.addValue(DbHelper.C_IMSI, imsi);
			dbParams.addValue(DbHelper.C_ACTIVATED, String.valueOf(1));

	DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
	new SetActivatedHandler(this), dbParams,
	DatabaseWriteTask.SET_ACTIVATED_ACTION);
	databaseWriteThread.enqueueTask(task);*/
			putPref(true);
		     Log.d(Main.TAG, "set activated: ok");
		     createActivationNotification("Activation successful"); 	 

	}

	}

	public void saveGenre(String table, JSONArray songs){
		DatabaseParams dbParams = new DatabaseParams();
		dbParams.addParam("table", table);
dbParams.setBatchValues(songs);

DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
new SaveGenreHandler(this), dbParams,
DatabaseWriteTask.SAVE_GENRE_ACTION);
databaseWriteThread.enqueueTask(task);
	}

	public void saveSongs(String table, JSONArray songs){
		DatabaseParams dbParams = new DatabaseParams();
		dbParams.addParam("table", table);
dbParams.setBatchValues(songs);

DatabaseWriteTask task = new DatabaseWriteTask(dbHelper.getDatabase(),
new SaveSongsHandler(this), dbParams,
DatabaseWriteTask.SAVE_GENRE_ACTION);
databaseWriteThread.enqueueTask(task);
	}
    public void goToHome()  {
        executeLocalRequest("main");
        clearNavigationStack();
        Navigation nav = new Navigation();
        nav.setActivity("main");
        pushNavigationStack(nav);   
        }
    public void stopDatabseWriteThread()  {
        databaseWriteThread.requestStop();  
        }
    public void setServiceVersion(String version)  {
    	byte[] encryptedVersion = asymmetricEncryption.encrypt(version.getBytes());
    	serviceVersion = Base64.encodeBytes(encryptedVersion);
    }
    public void parseServiceVersion()  {
        serviceXMLParser.parseServiceVersion(serviceXML);

    }
    public String getServiceVersion(String serviceXML)  {
       return serviceXMLParser.parseServiceVersionForUpdate(serviceXML);

    }
    public void createActivationNotification(String msg)  {
    	String ns = Context.NOTIFICATION_SERVICE;
    	NotificationManager mNotificationManager = 
    		(NotificationManager) getSystemService(ns);  
    	int icon = R.drawable.lipuka_icon;
    	CharSequence tickerText = msg;
    	long when = System.currentTimeMillis();
    	Notification notification = new Notification(icon, tickerText, when);
    	
    	Context context = getApplicationContext();
    	CharSequence contentTitle = "PayMax Activation";
    	CharSequence contentText = msg+"\nPress this notification to go to PayMax";
    	Intent notificationIntent = new Intent(this, PaymaxHome.class);
    	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    	PendingIntent contentIntent = 
    		PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    	notification.setLatestEventInfo(context, contentTitle, 
    			contentText, contentIntent);
    	notification.flags |= Notification.FLAG_AUTO_CANCEL;
    	notification.defaults |= Notification.DEFAULT_SOUND;
    	mNotificationManager.notify(R.id.notification_activation, notification);
        }
    
    public void showResponseNotification(String msg)  {
    	String ns = Context.NOTIFICATION_SERVICE;
    	NotificationManager mNotificationManager = 
    		(NotificationManager) getSystemService(ns);  
    	int icon = R.drawable.lipuka_icon;
    	CharSequence tickerText = "KCB Message";
    	long when = System.currentTimeMillis();
    	Notification notification = new Notification(icon, tickerText, when);
    	
    	Context context = getApplicationContext();
    	CharSequence contentTitle = "KCB Message";
    	//CharSequence contentText = "If Lipuka is running, press this message to restart it";
    	Intent notificationIntent = new Intent(this, Main.class);
    	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    	PendingIntent contentIntent = 
    		PendingIntent.getActivity(this, 0, notificationIntent, 0);

    	notification.setLatestEventInfo(context, contentTitle, 
    			msg, contentIntent);
    	notification.flags |= Notification.FLAG_AUTO_CANCEL;
    	notification.defaults |= Notification.DEFAULT_SOUND;
    	mNotificationManager.notify(R.id.notification_response, notification);
        }
    
    public void showProfileUpdateNotification(String msg)  {
    	String ns = Context.NOTIFICATION_SERVICE;
Notifications notifications = new Notifications(
    			(NotificationManager) getSystemService(ns), this, EcobankHome.class);
  
notifications.showResponseNotification(msg, R.id.update_profile_response);
        }
    public void showChangeOTPNotification(String msg, Class<?> klass)  {
    	String ns = Context.NOTIFICATION_SERVICE;
Notifications notifications = new Notifications(
    			(NotificationManager) getSystemService(ns), this, klass);
  
notifications.showResponseNotification(msg, R.id.change_otp_response);
        }
    public void showGenericNotification(String msg, Class<?> klass, int id)  {
    	String ns = Context.NOTIFICATION_SERVICE;
Notifications notifications = new Notifications(
    			(NotificationManager) getSystemService(ns), this, klass);
  
notifications.showResponseNotification(msg, id);
        }
 
    
    public void putPref(boolean activated) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
    	Editor editor = prefs.edit();
    	editor.putBoolean(imsi+"activated", activated);
    	editor.commit();
        //prefs.registerOnSharedPreferenceChangeListener(this);   // 
}
    
	public void putPref(String key, String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
    public boolean getPref() {
    	readIMSI();
        // Setup preferences
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
    	return prefs.getBoolean(imsi+"activated", false);
        //prefs.registerOnSharedPreferenceChangeListener(this);   // 
}
    
	public String getPref(String key) {
		readIMSI();
		// Setup preferences
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getString(key, null);
		// prefs.registerOnSharedPreferenceChangeListener(this); //
	}
    public void putNumber(String number) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
    	Editor editor = prefs.edit();
    	editor.putString(imsi+"number", number);
    	editor.commit();
}
    public String getNumber() {
        // Setup preferences
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
    	return prefs.getString(imsi+"number", "");
        //prefs.registerOnSharedPreferenceChangeListener(this);   // 
}
    public void putOTP(String clientPrefix, boolean otp) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
    	Editor editor = prefs.edit();
    	editor.putBoolean(imsi+clientPrefix+"otp", otp);
    	editor.commit();
}
    public boolean getOTP() {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
    	return prefs.getBoolean(imsi+currentBank.getId()+"_otp", false);
}
    public synchronized long generateRequestId() { 
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
    	Editor editor = prefs.edit();

    	Long id = prefs.getLong("request_id", 0);

    	if(id == Long.MAX_VALUE){
    		id = 0L;
    	}
		editor.putLong("request_id", id+1);
    	editor.commit();

    	return id+1;
		}
    public String generateDownloadURL(String contentId, String type) {
    	byte[] authToken = "authToken".getBytes();
        byte[] authTokenCiphertext = null;
    	StringBuilder sb = new StringBuilder();
            
        try {
        	 AppAuth appAuth = new AppAuth(this);      		
        	 authTokenCiphertext = appAuth.encrypt(authToken);

            String encodedAuthTokenCiphertext = Base64.encodeBytes(authTokenCiphertext);
            
    	sb.append(MSISDN);
    	sb.append("|");
    	sb.append(contentId);
    	sb.append("|");
    	sb.append(type);
    	sb.append("|");
    	sb.append(encodedAuthTokenCiphertext);
    	sb.append("|");
    	sb.append(generateRequestId());

    	  byte[] plaintext = sb.toString().getBytes();
          byte[] ciphertext = null;
          byte[] encryptedKey = null, encryptedIv = null;
        		
       encryptedKey = asymmetricEncryption.encrypt(SymmetricEncryption.aesKey);
       encryptedIv = asymmetricEncryption.encrypt(SymmetricEncryption.aesInitV);

       ciphertext = SymmetricEncryption.aesEncrypt(plaintext);
       
       String base64EncodedCiphertext = Base64.encodeBytes(ciphertext);	
       
       sb.setLength(0);
 		sb.append("http://lipuka.mobi/android/?menu=download&param1=");
 		sb.append(URLEncoder.encode(Base64.encodeBytes(encryptedKey), "utf-8"));
 		sb.append("&param2=");
 		sb.append(URLEncoder.encode(Base64.encodeBytes(encryptedIv), "utf-8"));
 		sb.append("&param3=");
 		sb.append(URLEncoder.encode(base64EncodedCiphertext, "utf-8"));
               }catch (Exception e){
           		Log.d(Main.TAG, e.getMessage());
          }
          		Log.d(Main.TAG, sb.toString());

        		return sb.toString();
}
    private String kaw(){
    	return "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANQqFlg49+LxWZO12EPh3RWEr7n4asXdwY+qjklhcRh35GQ8YQ2FFcUge8IlvxxptZ2Wajk9uw1Eu7ugg3tg7CFFwGID4OsZfI3kbe3Lhe4LJ3LihCFfp1abfd0XMFyg7iBMgY6W+DYoCRjakfptSKpUUCtV3bzyQeHRJ3jKMlYlAgMBAAECgYEApW8D6VrSf5UuAe2DarsNhx/lXh+EMjFOItYdCL8ATtH9tuMV0lL5vLItjh+cu2z5/p0wyt2Fozz7Hbx1iZjzxToTdeEtS1yy8Q3hx53XhqhHwcLTFGxXRyNSxYiQwRmVLt/nmyPtUGv/xk7uWcMmCNOEk6u63ZKLP1ujEY4yusECQQD1FJ5lruZ3oBH/ZzxYlWigowqOpeeak1cbALb99TQdT9lhGjtQO19c1qOrcu+NDQll5GVcbdT0GC5XMzdZmrM5AkEA3Z4HLyB96hkXWRqp9N9dpqPlyOr73T7xXzISIjeDUPW7bVcE1BiQF4Me4BYxK/V5d9EkNAQ+XwEQVGZtnjfeTQJBAON7avcZG+THmE0H5vSWYhJQo7j6d3p77qsqw7AADIp0lmJSNeHn6kFDHZJDUeULx8Bi+k6Lx9F2LHVWHbc3J8kCQGYiHRd39u97DsqOwkLK6gMRR6XXvalSB7Uigz7nrIoUJYOXde81PrrHP+Rv9ctXftpcNT4PsprgB6GN7vjasIUCQQCIgCFQKW2PQrNBlqPTwt6WwMCCEGv+4lJfoXQMjxgYzXi/VZ2PdJBriAiLFQ8q4KZa9l4aSq2BDhSyBsI7o0DX";
    }
    public String ensureCountryCode(String msisdn){
        try{
            int i = msisdn.indexOf("7");
         StringBuffer sb = new StringBuffer("254");
         sb.append(msisdn.substring(i));
         return sb.toString();
        }catch (Exception e){
        return null;
        }
                       }
    
    public ArrayList<LipukaListItem> getContacts() {
        Log.d("START","Getting all Contacts");
        ArrayList<LipukaListItem> arrContacts = new ArrayList<LipukaListItem>();
        LipukaListItem phoneContactInfo=null;     
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone._ID}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            String contactNumber= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  
            String contactName =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            int phoneContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));


            phoneContactInfo = new LipukaListItem();
           // phoneContactInfo.setPhoneContactID(phoneContactID);             
            phoneContactInfo.setText(contactName);                   
            phoneContactInfo.setValue(contactNumber); 
            if (phoneContactInfo != null)
            {
                arrContacts.add(phoneContactInfo);
            }
            phoneContactInfo = null; 
            cursor.moveToNext();
        }       
        cursor.close();
        cursor = null;
        Log.d("END","Got all Contacts");
        return arrContacts;
    }
	public Cursor getContactsAutoCompleteCursor() {
		Log.d("START", "Getting all Contacts");
		
		Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

		Cursor cursor = getContentResolver().query(
				uri,
				new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
						ContactsContract.CommonDataKinds.Phone._ID }, null,
						   null,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

		Log.d("END", "Got all Contacts");
		return cursor;
	}
    public void executeService() {
    	
    	switch(service){
    case 0:
    	consumeService();		
    		break;
    case 1:
		consumeService();		   		   	
    	break;  	
    case 2:
		consumeService();		   		
    		break;
    	case 3:
    		consumeService();		
    		break;
    	case 4:
    		if(getPin() != null)	{
    			consumeService();		
    		}else{
    			setCurrentDialogTitle("PIN");
    		      setCurrentDialogMsg("Enter PIN");
    		currentActivity.showDialog(Main.DIALOG_PIN_ID);	
    		}
    		break;
    	case 5:
    		consumeService();		   		
    		break;
    	case 6:
    		withdrawCash();		   		    		
    		break;
    	case 7:
    		consumeService();		   		    		
    		break;
    	case 8:
    		consumeService();		   		    		
    		break;
    	case 9:
    		consumeService();		   		    				
    		break;
    	case 10:
    		if(getPin() != null)	{
    			consumeService();		
    		}else{
    			setCurrentDialogTitle("PIN");
    		      setCurrentDialogMsg("Enter PIN");
    		currentActivity.showDialog(Main.DIALOG_PIN_ID);	
    		}  
    		break;
    	case 11: 
    		consumeService();		
	break;
    	case 12:
    		break;
    	case 13:

    		break;
    	case 14:

    		break;
    		default:
    			
    	} 

    	}
    public void touch()
    {
    	 if(waiter == null){
    		 waiter=new Waiter(this, 5*60*1000); //5 mins
           waiter.start();
    	 }
        waiter.touch();
    }
}

