package lipuka.android.model;


import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;


public class ActivityDateListener implements DatePickerDialog.OnDateSetListener{

	EditText editText;
int mYear, mMonth, mDay;  
        public ActivityDateListener(){
        	super();
        }

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        updateEditText();		
	}
	 private void updateEditText() {
		 int mth = mMonth + 1;
		 editText.setText(
	            new StringBuilder().append(mYear).append("-")
	                    .append(mth).append("-")
	                    .append(mDay)
	                    );
		 
		 
	    }
	 public void setEditText(EditText editText) {
		 this.editText = editText;
	 }
}
