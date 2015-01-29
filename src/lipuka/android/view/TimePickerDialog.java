package lipuka.android.view;


import java.util.ArrayList;
import java.util.Calendar;

import kcb.android.AccountStatement;
import kcb.android.DateCaptureActivity;
import kcb.android.EcobankMain;
import kcb.android.FullStmt;
import kcb.android.LipukaApplication;
import kcb.android.Main;






import kcb.android.R;
import lipuka.android.model.Navigation;
import lipuka.android.util.Validators;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TimePickerDialog extends Dialog implements OnClickListener {
	Button okButton, hourForward, minForward, amPm;
	Button cancelButton, hourBackward, minBackward;
	TextView title, message;
	EditText hour, min;
	LipukaApplication lipukaApplication;
	Activity activity;
	String currentMinStr, amPmStr;
	int currentHour, currentMin;
	
	ArrayList<String> months = new ArrayList<String>(12);

	public TimePickerDialog(Context context) {
	super(context);
	activity = (Activity)context;
	lipukaApplication = (LipukaApplication)activity.getApplication();

	/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	/** Design the dialog in main.xml file */
	try{
		setContentView(R.layout.time_picker);
	okButton = (Button) findViewById(R.id.ok_button);
	okButton.setOnClickListener(this);
	cancelButton = (Button) findViewById(R.id.cancel_button);
	cancelButton.setOnClickListener(this);
	hourBackward = (Button) findViewById(R.id.hour_backward);
	hourBackward.setOnClickListener(this);
	hourForward = (Button) findViewById(R.id.hour_forward);
	hourForward.setOnClickListener(this);
	minBackward = (Button) findViewById(R.id.min_backward);
	minBackward.setOnClickListener(this);
	minForward = (Button) findViewById(R.id.min_forward);
	minForward.setOnClickListener(this);
	
	amPm = (Button) findViewById(R.id.am_pm);
	amPm.setOnClickListener(this);
	
	title = (TextView) findViewById(R.id.title);
	message = (TextView) findViewById(R.id.message);
	hour = (EditText) findViewById(R.id.hour_field);
	hour.setInputType(InputType.TYPE_CLASS_NUMBER);
	min = (EditText) findViewById(R.id.min_field);
	min.setInputType(InputType.TYPE_CLASS_NUMBER);
	
   InputFilter[] hourFilterArray = new InputFilter[2];
   hourFilterArray[0] = new InputFilter.LengthFilter(2);
   hourFilterArray[1] = new HourInputFilter();

   hour.setFilters(hourFilterArray);
   
   InputFilter[] minFilterArray = new InputFilter[2];
   minFilterArray[0] = new InputFilter.LengthFilter(2);
   minFilterArray[1] = new MinInputFilter();
   min.setFilters(minFilterArray);
   
   final Calendar c = Calendar.getInstance();
  
   currentHour = c.get(Calendar.HOUR);
   currentMin = c.get(Calendar.MINUTE);
if(currentHour == 0){
	currentHour = 12;
}
   hour.setText(String.valueOf(currentHour)); 	   

   setMin(currentMin);
	min.setText(currentMinStr);
	
	   setAmPm(c.get(Calendar.AM_PM));
amPm.setText(amPmStr);
   
	}  catch(Exception e){
		Log.d(Main.TAG, "creating time picker error", e);

   }
	}

	@Override
	public void onClick(View v) {
	/** When OK Button is clicked, dismiss the dialog */
	if (v == okButton){
		String hourStr = hour.getText().toString();
		String minStr = min.getText().toString();
		
		boolean valid = true;
		message.setText("");

if(hourStr.length() == 0 || hourStr.equals("0")){
valid = false;
}
        if(minStr.length() < 1){
valid = false;
}
        if(validateHour("1", hourStr) == null){
        	valid = false;	
        }
if(validateMin("1", minStr) == null){
	valid = false;	
}
		    			
		    			if(valid){
((DateCaptureActivity)activity).setDate(currentHour+":"+currentMinStr+" "+amPmStr);	
		    			}else{
		    				message.setText("Please enter a valid time");
							return;				
		    			}
		/*if(inputStr != null && inputStr.length() > 0){
			Navigation nav = new Navigation();
		    nav.setPayload(inputStr+"|");
			lipukaApplication.pushNavigationStack(nav);
			lipukaApplication.executeService();
						}else{
							
						}	*/

	dismiss();
}else if (v == cancelButton){

	dismiss();
	}else if (v.getId() == R.id.hour_forward){
increaseHour();
		}else if (v.getId() == R.id.hour_backward){
decreaseHour();
		}else if (v.getId() == R.id.min_forward){
increaseMin();
		}else if (v.getId() == R.id.min_backward){
decreaseMin();
		}else if (v.getId() == R.id.am_pm){
toggleAmPm();
		}
	}

	public void setCustomTitle(String title) {
	this.title.setText(title);
	}

	private void setMin(int min){
		if(min < 10){
	currentMinStr = "0"+min;
		}else{
	currentMinStr = String.valueOf(min);
		}
	}
	private void setAmPm(int amPm){
		switch(amPm){
		case Calendar.AM:
	amPmStr = "AM";
			break;
		case Calendar.PM:
			amPmStr = "PM";
		break;
		default:
		}
	}
	
	class HourInputFilter implements InputFilter{
	    @Override
	    public CharSequence filter(CharSequence source, int start, int end,
	            Spanned dest, int dstart, int dend) {

            StringBuilder newValue = new StringBuilder();
	    	if(source.length() > 0){
            for (int i = 0; i < dstart; i++) { 
                char currentChar = dest.charAt(i);
                newValue.append(currentChar);   
            }
            newValue.append(source);   
            for (int i = dstart; i < dend; i++) { 
                char currentChar = dest.charAt(i);
                newValue.append(currentChar);   
            }
	    	}else{
	    		return "";
	    	}
	    	String mth = validateHour(source.toString(), newValue.toString());

	          if (source instanceof SpannableStringBuilder) {
	            SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder)source;
	            if(mth != null){
	        	source = mth;   
	           } else{
                   sourceAsSpannableBuilder.delete(end - 1, end);        	   
	           }
	            return source;
	        } else {
	            StringBuilder filteredStringBuilder = new StringBuilder();
	            if(mth != null){
		        	Log.d(Main.TAG, "hour is valid");
	            	return mth;   
		           } else{		        	 
		        	   for (int i = 0; i < end-1; i++) { 
			                char currentChar = source.charAt(i);
		                    filteredStringBuilder.append(currentChar);   
			            }
		           }
	            return filteredStringBuilder.toString();
	        }
	    }
	}
	private String validateHour(String source, String input){
	if(source.length() == 0){
		return null;
	}
	/*Log.d(Main.TAG, "source: "+source);
	Log.d(Main.TAG, "source length: "+source.length());

	String input = dayOfMonth.getText().toString();
input += source;
Log.d(Main.TAG, "input: "+input);
Log.d(Main.TAG, "input length: "+input.length());*/
int enteredHour = 0; 
try{
	enteredHour = Integer.parseInt(input);	
}catch(NumberFormatException ex){
	return null;
}
if(enteredHour >= 1 && enteredHour <= 12){
/*if (input.length() == 1){
	return "0"+source;	
	}*/
	currentHour = enteredHour;
	setTimeText();

	return source;
}
else{
	return null;
}
	}
	
	class MinInputFilter implements InputFilter{
	    @Override
	    public CharSequence filter(CharSequence source, int start, int end,
	            Spanned dest, int dstart, int dend) {

            StringBuilder newValue = new StringBuilder();
	    	if(source.length() > 0){
            for (int i = 0; i < dstart; i++) { 
                char currentChar = dest.charAt(i);
                newValue.append(currentChar);   
            }
            newValue.append(source);   
            for (int i = dstart; i < dend; i++) { 
                char currentChar = dest.charAt(i);
                newValue.append(currentChar);   
            }
	    	}else{
	    		return "";
	    	}
	    	String mth = validateMin(source.toString(), newValue.toString());

	          if (source instanceof SpannableStringBuilder) {
	            SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder)source;
	            if(mth != null){
	        	source = mth;   
	           } else{
                   sourceAsSpannableBuilder.delete(end - 1, end);        	   
	           }
	            return source;
	        } else {
	            StringBuilder filteredStringBuilder = new StringBuilder();
	            if(mth != null){
		        	Log.d(Main.TAG, "min is valid");
	            	return mth;   
		           } else{		        	 
		        	   for (int i = 0; i < end-1; i++) { 
			                char currentChar = source.charAt(i);
		                    filteredStringBuilder.append(currentChar);   
			            }
		           }
	            return filteredStringBuilder.toString();
	        }
	    }
	}
	private String validateMin(String source, String input){
	if(source.length() == 0){
		return null;
	}

int enteredMin = 0; 
try{
	enteredMin = Integer.parseInt(input);	
}catch(NumberFormatException ex){
	return null;
}
if(enteredMin >= 0 && enteredMin <= 59){

	currentMin = enteredMin;
	   setMin(currentMin);

	   setTimeText();

	return source;
}
else{
	return null;
}
	}
	
	private void increaseHour(){
		currentHour = ++currentHour % 13;
		if(currentHour == 0){
			currentHour = 1;
			}
		   hour.setText(String.valueOf(currentHour)); 	   
	}
	private void decreaseHour(){
		currentHour = --currentHour % 13;
		if(currentHour == 0){
			currentHour = 12;
			}
		   hour.setText(String.valueOf(currentHour)); 	
	}
	private void increaseMin(){
		currentMin = ++currentMin % 60;		
		   setMin(currentMin);
			min.setText(currentMinStr);
			}
	private void decreaseMin(){
		currentMin = --currentMin % 60;		
		if(currentMin == -1){
			currentMin = 59;
			}
		   setMin(currentMin);
			min.setText(currentMinStr);
		}
	private void toggleAmPm(){
		if(amPmStr.equals("AM")){
			amPmStr = "PM";
			}else{
				amPmStr = "AM";				
			}
		amPm.setText(amPmStr);
		setTimeText();
	}
	
	private void setTimeText(){	
		   StringBuilder longDate = new StringBuilder();
		   longDate.append(currentHour);
		   longDate.append(":");
		   longDate.append(currentMinStr);
		   longDate.append(" ");
		   longDate.append(amPmStr);
		   title.setText(longDate.toString());

	}
	public void resetToCurrentTime(){
		final Calendar c = Calendar.getInstance();
		  
		   currentHour = c.get(Calendar.HOUR);
		   currentMin = c.get(Calendar.MINUTE);
		   if(currentHour == 0){
				currentHour = 12;
			}
		   hour.setText(String.valueOf(currentHour)); 	   

		   setMin(currentMin);
			min.setText(currentMinStr);
			
			   setAmPm(c.get(Calendar.AM_PM));
		amPm.setText(amPmStr);
	}
	}