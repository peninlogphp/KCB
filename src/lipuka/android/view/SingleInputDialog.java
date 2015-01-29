package lipuka.android.view;

import kcb.android.EcobankMain;
import kcb.android.LipukaApplication;
import kcb.android.R;
import lipuka.android.model.Navigation;
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

public class SingleInputDialog extends Dialog implements OnClickListener {
	Button okButton;
	Button cancelButton;
	TextView title;
	TextView message;
	EditText input;
	LipukaApplication lipukaApplication;
	EcobankMain lipukaHome;
	public SingleInputDialog(Context context) {
	super(context);
	lipukaHome = (EcobankMain)context;
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
	input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
   /*InputFilter[] filterArray = new InputFilter[1];
	   filterArray[0] = new InputFilter.LengthFilter(4);
	   input.setFilters(filterArray);*/
	}

	@Override
	public void onClick(View v) {
	/** When OK Button is clicked, dismiss the dialog */
	if (v == okButton){
		String inputStr = input.getText().toString();
		if(inputStr != null && inputStr.length() > 0){
			Navigation nav = new Navigation();
		    nav.setPayload(inputStr+"|");
			lipukaApplication.pushNavigationStack(nav);
			lipukaApplication.executeService();
						}else{
							message.setText("Please enter amount");
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