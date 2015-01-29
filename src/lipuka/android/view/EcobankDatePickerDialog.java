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

public class EcobankDatePickerDialog extends Dialog implements OnClickListener {
	Button okButton, dayOfMonthForward, monthForward, yearForward;
	Button cancelButton, dayOfMonthBackward, monthBackward, yearBackward;
	TextView title, message;
	EditText dayOfMonth, month, year;
	LipukaApplication lipukaApplication;
	Activity activity;
	String currentMonthStr, currentDayOfWeek;
	int currentDayOfMonth, currentMonth, currentYear;
	
	ArrayList<String> months = new ArrayList<String>(12);

	public EcobankDatePickerDialog(Context context) {
	super(context);
	activity = (Activity)context;
	lipukaApplication = (LipukaApplication)activity.getApplication();

	/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	/** Design the dialog in main.xml file */
	try{
		setContentView(R.layout.ecobank_date_picker_dialog);
	okButton = (Button) findViewById(R.id.ok_button);
	okButton.setOnClickListener(this);
	cancelButton = (Button) findViewById(R.id.cancel_button);
	cancelButton.setOnClickListener(this);
	dayOfMonthBackward = (Button) findViewById(R.id.day_of_month_backward);
	dayOfMonthBackward.setOnClickListener(this);
	dayOfMonthForward = (Button) findViewById(R.id.day_of_month_forward);
	dayOfMonthForward.setOnClickListener(this);
	monthBackward = (Button) findViewById(R.id.month_backward);
	monthBackward.setOnClickListener(this);
	monthForward = (Button) findViewById(R.id.month_forward);
	monthForward.setOnClickListener(this);
	yearBackward = (Button) findViewById(R.id.year_backward);
	yearBackward.setOnClickListener(this);
	yearForward = (Button) findViewById(R.id.year_forward);
	yearForward.setOnClickListener(this);
	
	title = (TextView) findViewById(R.id.title);
	message = (TextView) findViewById(R.id.message);
	dayOfMonth = (EditText) findViewById(R.id.day_of_month_field);
	dayOfMonth.setInputType(InputType.TYPE_CLASS_NUMBER);
	month = (EditText) findViewById(R.id.month_field);
	year = (EditText) findViewById(R.id.year_field);
	year.setInputType(InputType.TYPE_CLASS_NUMBER);
   InputFilter[] dayOfMonthFilterArray = new InputFilter[2];
   dayOfMonthFilterArray[0] = new InputFilter.LengthFilter(2);
   dayOfMonthFilterArray[1] = new DayOfMonthInputFilter();

   dayOfMonth.setFilters(dayOfMonthFilterArray);
   
   InputFilter[] monthFilterArray = new InputFilter[2];
   monthFilterArray[0] = new InputFilter.LengthFilter(3);
   monthFilterArray[1] = new MonthInputFilter();
   month.setFilters(monthFilterArray);
   
   InputFilter[] yearFilterArray = new InputFilter[2];
   yearFilterArray[0] = new InputFilter.LengthFilter(4);
   yearFilterArray[1] = new YearInputFilter();
year.setFilters(yearFilterArray);
   
   
   months.add("Jan");months.add("Feb");months.add("Mar");months.add("Apr");months.add("May");
   months.add("Jun");months.add("Jul");months.add("Aug");months.add("Sep");months.add("Oct");
   months.add("Nov");months.add("Dec");
   
   final Calendar c = Calendar.getInstance();
  
   currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
   setDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
   currentMonth = c.get(Calendar.MONTH);
   currentYear = c.get(Calendar.YEAR);

   dayOfMonth.setText(String.valueOf(currentDayOfMonth)); 	   

   setMonth(currentMonth);
	month.setText(currentMonthStr.substring(0, 3));

   year.setText(String.valueOf(currentYear));
   
	}  catch(Exception e){
		Log.d(Main.TAG, "creating ecobank date picker error", e);

   }
	}

	@Override
	public void onClick(View v) {
	/** When OK Button is clicked, dismiss the dialog */
	if (v == okButton){
		String dayOfMonthStr = dayOfMonth.getText().toString();
		String monthStr = month.getText().toString();
		String yearStr = year.getText().toString();
		
		boolean valid = true;
		message.setText("");

if(dayOfMonthStr.length() == 0 || dayOfMonthStr.equals("0")){
valid = false;
}
        if(monthStr.length() < 3){
valid = false;
}
        if(yearStr.length() == 0 || yearStr.equals("0")){
        	valid = false;
        	}

		    			
		    			if(valid){
		    				int actualMonth = currentMonth + 1;
((DateCaptureActivity)activity).setDate(currentYear+"-"+actualMonth+"-"+currentDayOfMonth);	
		    			}else{
		    				message.setText("Please enter a valid date");
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
	}else if (v.getId() == R.id.day_of_month_forward){
increaseDayOfMonth();
		}else if (v.getId() == R.id.day_of_month_backward){
decreaseDayOfMonth();
		}else if (v.getId() == R.id.month_forward){
increaseMonth();
		}else if (v.getId() == R.id.month_backward){
decreaseMonth();
		}else if (v.getId() == R.id.year_forward){
increaseYear();
		}else if (v.getId() == R.id.year_backward){
decreaseYear();
		}
	}

	public void setCustomTitle(String title) {
	this.title.setText(title);
	}

	private void setMonth(int mth){
		switch(mth){
		case 0:
	currentMonthStr = "January";
			break;
		case 1:
	currentMonthStr = "February";
		break;
		case 2:
	currentMonthStr = "March";
		break;
		case 3:
	currentMonthStr = "April";
		break;
		case 4:
	currentMonthStr = "May";
		break;
		case 5:
	currentMonthStr = "June";
		break;
		case 6:
	currentMonthStr = "July";
		break;
		case 7:
	currentMonthStr = "August";
		break;
		case 8:
	currentMonthStr = "September";
		break;
		case 9:
	currentMonthStr = "October";
		break;
		case 10:
	currentMonthStr = "November";
		break;
		case 11:
	currentMonthStr = "December";
		break;
		default:
		}
	}
	private void setDayOfWeek(int dayOfWeek){
		switch(dayOfWeek){
		case Calendar.SUNDAY:
	currentDayOfWeek = "Sunday";
			break;
		case Calendar.MONDAY:
			currentDayOfWeek = "Monday";
		break;
		case Calendar.TUESDAY:
			currentDayOfWeek = "Tuesday";
		break;
		case Calendar.WEDNESDAY:
			currentDayOfWeek = "Wednesday";
		break;
		case Calendar.THURSDAY:
			currentDayOfWeek = "Thursday";
		break;
		case Calendar.FRIDAY:
			currentDayOfWeek = "Friday";
		break;
		case Calendar.SATURDAY:
			currentDayOfWeek = "Saturday";
		break;
		default:
		}
	}
	
	class MonthInputFilter implements InputFilter{
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
	          String mth = validateMonth(source.toString(), newValue.toString());

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
		        	Log.d(Main.TAG, "date is valid");
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
	private String validateMonth(String source, String input){
	int i = 1;
	if(source.length() == 0){
		return null;
	}
	/*	Log.d(Main.TAG, "source: "+source);
	Log.d(Main.TAG, "source length: "+source.length());

	String input = month.getText().toString();
input += source;
Log.d(Main.TAG, "input: "+input);
Log.d(Main.TAG, "input length: "+input.length());
*/
if(input.length() == 3){
		for(String mth: months){
			if(mth.toLowerCase().equals(input.toLowerCase())){
				Log.d(Main.TAG, "found Nov match");
			if(Validators.isDate(currentDayOfMonth, i, currentYear)){
				currentMonth = i - 1;
				   setMonth(currentMonth);
				   setLongDate();
					return source;
		        }	
			}
			i++;
		}
	}else if (input.length() == 2){
		for(String mth: months){
			String firstTwo = mth.substring(0, 2);
			if(firstTwo.toLowerCase().equals(input.toLowerCase())){
	        	Log.d(Main.TAG, "firstTwo: "+firstTwo);
				return source;	
			}
			i++;
		}
	}else if (input.length() == 1){
		for(String mth: months){
			String first = mth.substring(0, 1);
			if(first.toLowerCase().equals(input.toLowerCase())){
				return first;	
			}
			i++;
		}
	}
	return null;
	}
	
	class DayOfMonthInputFilter implements InputFilter{
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
	    	String mth = validateDayOfMonth(source.toString(), newValue.toString());

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
		        	Log.d(Main.TAG, "date is valid");
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
	private String validateDayOfMonth(String source, String input){
	int i = 1;
	if(source.length() == 0){
		return null;
	}
	/*Log.d(Main.TAG, "source: "+source);
	Log.d(Main.TAG, "source length: "+source.length());

	String input = dayOfMonth.getText().toString();
input += source;
Log.d(Main.TAG, "input: "+input);
Log.d(Main.TAG, "input length: "+input.length());*/
int enteredDayOfMonth = 0; 
try{
	enteredDayOfMonth = Integer.parseInt(input);	
}catch(NumberFormatException ex){
	return null;
}
if(Validators.isDate(enteredDayOfMonth, currentMonth + 1, currentYear)){
/*if (input.length() == 1){
	return "0"+source;	
	}*/
	currentDayOfMonth = enteredDayOfMonth;
	setLongDate();

	return source;
}
else{
	return null;
}
	}
	
	class YearInputFilter implements InputFilter{
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
	    	String yr = validateYear(source.toString(), newValue.toString());

	          if (source instanceof SpannableStringBuilder) {
	            SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder)source;
	            if(yr != null){
	        	source = yr;   
	           } else{
                   sourceAsSpannableBuilder.delete(end - 1, end);        	   
	           }
	            return source;
	        } else {
	            StringBuilder filteredStringBuilder = new StringBuilder();
	            if(yr != null){
		        	Log.d(Main.TAG, "date is valid");
	            	return yr;   
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
	private String validateYear(String source, String input){
int enteredYear = 0; 
try{
	enteredYear = Integer.parseInt(input);	
}catch(NumberFormatException ex){
	return null;
}
if(Validators.isDate(currentDayOfMonth, currentMonth + 1, enteredYear)){
currentYear = enteredYear;
setLongDate();
	return source;
}
else{
	return null;
}
	}
	
	private void increaseDayOfMonth(){
		if(Validators.isDate(currentDayOfMonth+1, currentMonth + 1, currentYear)){
			currentDayOfMonth += 1;
			   dayOfMonth.setText(String.valueOf(currentDayOfMonth)); 	   
			}
	}
	private void decreaseDayOfMonth(){
		if(Validators.isDate(currentDayOfMonth-1, currentMonth + 1, currentYear)){
			currentDayOfMonth -= 1;
			   dayOfMonth.setText(String.valueOf(currentDayOfMonth)); 	   
			}
	}
	private void increaseMonth(){
		if(currentMonth < 11){
			currentMonth += 1;
			   month.setText(months.get(currentMonth)); 			
		}
		}
	private void decreaseMonth(){
		if(currentMonth > 0){
			currentMonth -= 1;
			   month.setText(months.get(currentMonth)); 			
		}
		}
	private void increaseYear(){
		if(Validators.isDate(currentDayOfMonth, currentMonth + 1, currentYear+1)){
			currentYear += 1;
			   year.setText(String.valueOf(currentYear)); 	   
			}
	}
	private void decreaseYear(){
		if(Validators.isDate(currentDayOfMonth, currentMonth + 1, currentYear-1)){
			currentYear -= 1;
			   year.setText(String.valueOf(currentYear)); 	   
			}
	}
	private void setLongDate(){
		final Calendar c = Calendar.getInstance();
		  c.set(currentYear, currentMonth, currentDayOfMonth);
		   setDayOfWeek(c.get(Calendar.DAY_OF_WEEK));	
		   StringBuilder longDate = new StringBuilder();
		   longDate.append(currentDayOfWeek);
		   longDate.append(" - ");
		   longDate.append(currentDayOfMonth);
		   longDate.append(" ");
		   longDate.append(currentMonthStr);
		   longDate.append(" - ");
		   longDate.append(currentYear);
		   title.setText(longDate.toString());

	}
	public void resetToCurrentDate(){
		   final Calendar c = Calendar.getInstance();
		   
		   currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		   setDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
		   currentMonth = c.get(Calendar.MONTH);
		   currentYear = c.get(Calendar.YEAR);

		   dayOfMonth.setText(String.valueOf(currentDayOfMonth)); 	   

		   setMonth(currentMonth);
			month.setText(currentMonthStr.substring(0, 3));

		   year.setText(String.valueOf(currentYear));
	}
	}