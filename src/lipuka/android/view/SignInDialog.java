package lipuka.android.view;

import kcb.android.AccountStatementMiniNfull;
import kcb.android.AgencyBanking;
import kcb.android.AirtimeTopup;
import kcb.android.CreditCardPayments;
import kcb.android.EcobankMain;
import kcb.android.ForgotCredentials;
import kcb.android.FundsTransfer;
import kcb.android.Insurance;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.ManageBeneficiaries;
import kcb.android.ManageTransactions;
import kcb.android.ManageTransactionsList;
import kcb.android.Mkodi;
import kcb.android.MobileMoneyTransfer;
import kcb.android.MoneyGram;
import kcb.android.MyAccount;
import kcb.android.PartialSignInActivity;
import kcb.android.PayBills;
import kcb.android.PaymaxHome;
import kcb.android.PrepaidCards;
import kcb.android.ReceiveMoney;
import kcb.android.ResponseActivity;
import kcb.android.SalaryAdvance;
import kcb.android.SendMoney;
import kcb.android.ShowSignOutActivity;
import kcb.android.SignInUp;
import kcb.android.TransactionHistory;
import kcb.android.WuEditProfile;
import lipuka.android.model.Navigation;
import lipuka.android.model.responsehandlers.SignInHandler;
import lipuka.android.model.responsehandlers.SignUpHandler;
import kcb.android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignInDialog extends Dialog implements OnClickListener {
	public static final byte BALANCE = 1;
	public static final byte PAY_BILL = 2;
	public static final byte FUNDS_TRANSFER = 3;
	public static final byte M_MONEY_TRANSFER = 4;
	public static final byte AIRTIME_TOP_UP = 5;
	public static final byte INSURANCE = 6;
	public static final byte MONEYGRAM = 7;
	public static final byte LOCATOR = 8;
	public static final byte CHEQUE_B00K_REQUEST = 9;
	public static final byte INVESTMENTS_N_CREDIT = 10;
	public static final byte CREDIT_CARD_PAYMENTS = 11;
	public static final byte MY_ACCOUNT = 12;
	public static final byte AGENCY_BANKING = 13;
	public static final byte M_KODI = 14;
	public static final byte SALARY_ADVANCE = 15;
	public static final byte PREPAID_CARDS = 16;
	public static final byte SEND_MONEY = 17;
	public static final byte RECEIVE_MONEY = 18;
	public static final byte MANAGE_TRANSACTIONS = 19;
	public static final byte TRANSACTION_HISTORY = 20;
	public static final byte MANAGE_BENEFICIARIES = 21;
	public static final byte WU_EDIT_PROFILE = 22;	
	public static final byte ACCOUNT_STATEMENT = 23;	
	public static final byte MG_SEND_MONEY = 24;
	public static final byte MG_RECEIVE_MONEY = 25;
	public static final byte MG_TRANSACTION_HISTORY = 26;
	public static final byte MG_MANAGE_BENEFICIARIES = 27;
	public static final byte MG_EDIT_PROFILE = 28;
	public static final byte AIRTIME_TOPUP = 29;

public static final byte NOTHING = 31;

	private byte action = 0;

	Button okButton;
	Button cancelButton;
	TextView title;
	TextView message;
	EditText username, password;
	LipukaApplication lipukaApplication;
Activity lipukaHome;
	public SignInDialog(Context context) {
	super(context);
	lipukaHome = (Activity)context;
	lipukaApplication = (LipukaApplication)lipukaHome.getApplication();
	
	/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	/** Design the dialog in main.xml file */
	setContentView(R.layout.sign_in_dialog);
	okButton = (Button) findViewById(R.id.ok_button);
	okButton.setOnClickListener(this);
	cancelButton = (Button) findViewById(R.id.cancel_button);
	cancelButton.setOnClickListener(this);
	title = (TextView) findViewById(R.id.title);
	message = (TextView) findViewById(R.id.message);
	username = (EditText) findViewById(R.id.username_field);
	password = (EditText) findViewById(R.id.password_field);
	//password.setInputType(InputType.TYPE_CLASS_NUMBER);
	password.setTransformationMethod(PasswordTransformationMethod.getInstance());
	password.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	
	 TextView forgotCredentialsLink = (TextView) findViewById(R.id.forgot_credentials_link);
     SpannableString mySpannableString = new SpannableString("Forgot username/password?");
 			 mySpannableString.setSpan(new UnderlineSpan(), 0, mySpannableString.length(), 0);
 			forgotCredentialsLink.setText(mySpannableString);
 			forgotCredentialsLink.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
	/** When OK Button is clicked, dismiss the dialog */
		message.setText("");
		message.setVisibility(View.GONE);

	if (v == okButton){
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
		    lipukaApplication.clearPayloadObject();

		    				lipukaApplication.putPayload("username", usernameStr);
		    				lipukaApplication.putPayload("password", passwordStr);
		    				lipukaApplication.consumeService("26", new SignInHandler(lipukaApplication, lipukaHome, action));
		    				
		    			

		    			}else{
		    				message.setText(errorBuffer.toString());
		    				message.setVisibility(View.VISIBLE);
							return;
		    			}	

	dismiss();
}else if (v == cancelButton){

	dismiss();
	}else if (v.getId() == R.id.forgot_credentials_link){
		dismiss();
		Intent i = new Intent(lipukaHome, ForgotCredentials.class);
		lipukaHome.startActivity(i);
		}
	}

	public void setCustomTitle(String title) {
	this.title.setText(title);
	}
	public void setMessage(String message) {
		this.message.setText("");
		this.message.setVisibility(View.GONE);
		username.setText("");
		password.setText("");
		}
	public void setAction(byte action) {
		this.action = action;
		}
	}