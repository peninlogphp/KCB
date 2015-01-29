package lipuka.android.view;

import kcb.android.CreateAccount;
import kcb.android.Main;
import kcb.android.PaymaxHome;
import kcb.android.ResetPasswordOTP;
import kcb.android.RetrieveUsernameOTP;
import kcb.android.SignInUp;
import lipuka.android.controller.ControllerState;
import kcb.android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;

/** Class Must extends with Dialog */
/** Implement onClickListener to dismiss dialog when OK Button is pressed */
public class CustomDialog extends Dialog implements OnClickListener {
	public static final byte RETRIEVE_USERNAME_OTP = 1;
	public static final byte RESET_PASSWORD_OTP = 2;
	public static final byte RETRIEVE_USERNAME = 3;
	public static final byte RESET_PASSWORD = 4;
	public static final byte CARD_ACTIVATION = 5;
	public static final byte CARD_LOCK = 6;
	public static final byte CHANGE_PIN = 7;
	public static final byte EDIT_PROFILE = 8;

	Button okButton;
TextView title;
TextView message;
Context context;
private byte activityShowing;
public CustomDialog(Context context) {
super(context);
this.context = context;
/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
requestWindowFeature(Window.FEATURE_NO_TITLE);
/** Design the dialog in main.xml file */
setContentView(R.layout.custom_dialog);
okButton = (Button) findViewById(R.id.ok_button);
okButton.setOnClickListener(this);
title = (TextView) findViewById(R.id.title);
message = (TextView) findViewById(R.id.message);

}

@Override
public void onClick(View v) {
/** When OK Button is clicked, dismiss the dialog */
if (v == okButton)
dismiss();
switch(activityShowing) {
case RETRIEVE_USERNAME_OTP:
case RESET_PASSWORD_OTP:
	Intent i = new Intent(context, SignInUp.class);
	context.startActivity(i);
	break;
case RETRIEVE_USERNAME:
	i = new Intent(context, RetrieveUsernameOTP.class);
	context.startActivity(i);
	break;
case RESET_PASSWORD:
	i = new Intent(context, ResetPasswordOTP.class);
	context.startActivity(i);
	break;
case CARD_ACTIVATION:
	i = new Intent(context, PaymaxHome.class);
	context.startActivity(i);
	break;
case CARD_LOCK:
	((Activity)context).finish();
	break;
case CHANGE_PIN:
	((Activity)context).finish();
	break;
case EDIT_PROFILE:
	((Activity)context).finish();
	break;
default:
}
}

public void setCustomTitle(String title) {
this.title.setText(title);
}
public void setMessage(String message) {
	this.message.setText(message);
	}
public void setActivityShowing(byte activityShowing) {
	this.activityShowing = activityShowing;
	}
}
