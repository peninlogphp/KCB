package lipuka.android.model;


import kcb.android.Main;
import android.app.DatePickerDialog;
import android.widget.DatePicker;


public class LipukaDateListener implements DatePickerDialog.OnDateSetListener{

	Main activity;
int mYear, mMonth, mDay;  
        public LipukaDateListener(Main activity){
        	super();
        this.activity = activity;
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
		 activity.getCurrentDateEditText().setText(
	            new StringBuilder().append(mYear)
	                    .append(mth  < 10 ? "0"+mth : mth)
	                    .append(mDay  < 10 ? "0"+mDay : mDay)
	                    );
		 
		 
	    }
}
