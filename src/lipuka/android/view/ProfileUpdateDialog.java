package lipuka.android.view;

import kcb.android.EcobankHome;
import kcb.android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/** Class Must extends with Dialog */
/** Implement onClickListener to dismiss dialog when OK Button is pressed */
public class ProfileUpdateDialog extends Dialog implements OnClickListener {
Button okButton;
TextView title;
TextView message;
Context context;
boolean successful;
public ProfileUpdateDialog(Context context) {
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

if (successful){
	Intent i = new Intent(context, EcobankHome.class);
	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	((EcobankHome)context).setSetCurrentActivityNull(false);
	context.startActivity(i);
}

/*Activity activity = (Activity)context;
LipukaApplication lipukaApplication = (LipukaApplication)activity.getApplication();

if (activity.getClass().equals(OneTimePINActivity.class)){
  
	}*/
	
}

public void setCustomTitle(String title) {
this.title.setText(title);
}
public void setMessage(String message) {
	this.message.setText(message);
	}
public void setSuccessful(boolean successful) {
	this.successful = successful;
	}
}
