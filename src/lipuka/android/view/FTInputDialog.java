package lipuka.android.view;

import kcb.android.EcobankMain;
import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FTInputDialog extends Dialog implements OnClickListener {
	Button okButton;
	Button cancelButton;
	TextView title;
	TextView message;
	EditText accountNo;
	EditText amount;

	LipukaApplication lipukaApplication;
	Activity activity;
	public FTInputDialog(Context context) {
	super(context);
	activity = (Activity)context;
	lipukaApplication = (LipukaApplication)activity.getApplication();
	
	/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	/** Design the dialog in main.xml file */
	setContentView(R.layout.ft_input_dialog);
	okButton = (Button) findViewById(R.id.ok_button);
	okButton.setOnClickListener(this);
	cancelButton = (Button) findViewById(R.id.cancel_button);
	cancelButton.setOnClickListener(this);
	title = (TextView) findViewById(R.id.title);
	message = (TextView) findViewById(R.id.message);
	accountNo = (EditText) findViewById(R.id.account_no_field);
	amount = (EditText) findViewById(R.id.amount_field);
	accountNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	/*InputFilter[] filterArray = new InputFilter[1];
	   filterArray[0] = new InputFilter.LengthFilter(4);
	   input.setFilters(filterArray);*/
	}

	@Override
	public void onClick(View v) {
	/** When OK Button is clicked, dismiss the dialog */
	if (v == okButton){
		String accountNoStr = accountNo.getText().toString();
		String amountStr = amount.getText().toString();

		    			boolean valid = true;
		    			StringBuffer errorBuffer = new StringBuffer();
		    			if(accountNoStr == null || accountNoStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Enter Account Number\n");
		    			}
		    			if(amountStr == null || amountStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Enter Amount\n");
		    			}
		if(valid){
			
			lipukaApplication.setCurrentDialogTitle("Funds Transfer");
		      lipukaApplication.setCurrentDialogMsg("Ksh. "+amountStr+" has been sent to "+accountNoStr+". Your new balance is Ksh. 45,000/-");
		      activity.showDialog(Main.DIALOG_MSG_ID);
		}else{
							message.setText(errorBuffer.toString());
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
		this.message.setText("");
		accountNo.setText("");
		amount.setText("");
		}
	}