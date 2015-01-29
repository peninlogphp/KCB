package lipuka.android.view;

import kcb.android.EcobankMain;
import kcb.android.LipukaApplication;
import kcb.android.MgEditProfile;
import kcb.android.ResponseActivity;
import kcb.android.WuEditProfile;
import lipuka.android.data.Constants;
import lipuka.android.model.responsehandlers.ConsumeServiceHandler;
import lipuka.android.model.responsehandlers.DeleteBeneficiaryHandler;
import lipuka.android.model.responsehandlers.DeleteCardAccountHandler;
import lipuka.android.model.responsehandlers.DeleteCardHandler;
import lipuka.android.model.responsehandlers.DeleteCardNumberHandler;
import lipuka.android.model.responsehandlers.FetchBalanceHandler;
import lipuka.android.model.responsehandlers.FetchFtTxnsHandler;
import lipuka.android.model.responsehandlers.SignUpHandler;
import lipuka.android.model.responsehandlers.WuEditProfileHandler;
import kcb.android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PinInputDialog extends Dialog implements OnClickListener {
	Button okButton;
	Button cancelButton;
	TextView title;
	TextView message;
	EditText input;
	LipukaApplication lipukaApplication;
Activity lipukaHome;
	public PinInputDialog(Context context) {
	super(context);
	lipukaHome = (Activity)context;
	lipukaApplication = (LipukaApplication)lipukaHome.getApplication();
	
	/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	/** Design the dialog in main.xml file */
	setContentView(R.layout.pin_input_dialog);
	okButton = (Button) findViewById(R.id.ok_button);
	okButton.setOnClickListener(this);
	cancelButton = (Button) findViewById(R.id.cancel_button);
	cancelButton.setOnClickListener(this);
	title = (TextView) findViewById(R.id.title);
	message = (TextView) findViewById(R.id.message);
	input = (EditText) findViewById(R.id.pin_field);
	input.setInputType(InputType.TYPE_CLASS_TEXT | 
    		InputType.TYPE_TEXT_VARIATION_PASSWORD);		
   InputFilter[] filterArray = new InputFilter[1];
	   filterArray[0] = new InputFilter.LengthFilter(4);
	   input.setFilters(filterArray);
	   input.setInputType(InputType.TYPE_CLASS_NUMBER);
	   input.setTransformationMethod(PasswordTransformationMethod.getInstance());
	   input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
	}

	@Override
	public void onClick(View v) {
	/** When OK Button is clicked, dismiss the dialog */
	if (v == okButton){
		String inputStr = input.getText().toString();
		if(inputStr != null && inputStr.length() > 0){
			lipukaApplication.setPin(inputStr);
			//lipukaApplication.executeService();
			lipukaApplication.putPayload("pin", inputStr);
			//lipukaApplication.putPayload("profile_id", String.valueOf(lipukaApplication.getProfileID()));
			int serviceID = Integer.valueOf(lipukaApplication.getServiceID());
			switch(serviceID){
			case Constants.DELETE_BENEFICIARY:	
				lipukaApplication.consumeService(lipukaApplication.getServiceID(), new DeleteBeneficiaryHandler(lipukaApplication, lipukaHome));
				break;
			case Constants.FETCH_BALANCE:	
				lipukaApplication.consumeService(lipukaApplication.getServiceID(), new FetchBalanceHandler(lipukaApplication, lipukaHome));
				break;
			case Constants.DELETE_CARD:	
				lipukaApplication.consumeService(lipukaApplication.getServiceID(), new DeleteCardHandler(lipukaApplication, lipukaHome));
				break;
			case Constants.FETCH_FT_TXNS:	
				lipukaApplication.consumeService(lipukaApplication.getServiceID(), new FetchFtTxnsHandler(lipukaApplication, lipukaHome));
				break;
			case Constants.WU_EDIT_PROFILE:	
				if(lipukaHome instanceof WuEditProfile){
					WuEditProfile wuEditProfile = (WuEditProfile)lipukaHome;
				lipukaApplication.consumeService(lipukaApplication.getServiceID(), new WuEditProfileHandler(lipukaApplication, lipukaHome, wuEditProfile.getGoldCardNo()));
				}else{
					MgEditProfile mgEditProfile = (MgEditProfile)lipukaHome;
					lipukaApplication.consumeService(lipukaApplication.getServiceID(), new WuEditProfileHandler(lipukaApplication, lipukaHome, mgEditProfile.getCustomerCardNo()));
					
				}
				break;
			case Constants.DELETE_CARD_ACCOUNT:	
				lipukaApplication.consumeService(lipukaApplication.getServiceID(), new DeleteCardAccountHandler(lipukaApplication, lipukaHome));
				break;
			case Constants.DELETE_CARD_NUMBER:	
				lipukaApplication.consumeService(lipukaApplication.getServiceID(), new DeleteCardNumberHandler(lipukaApplication, lipukaHome));
				break;
				default:
				lipukaApplication.consumeService(lipukaApplication.getServiceID(), new ConsumeServiceHandler(lipukaApplication, lipukaHome));
			}	
						}else{
						//	message.setText("Please enter PIN");
							return;
						}	

	dismiss();
}else if (v == cancelButton){

	dismiss();
	}
	}

	public void setCustomTitle(String title) {
	this.title.setText(title);
	}
	public void setMessage(String message) {
		this.message.setText(message);
		input.setText("");
		}
	}