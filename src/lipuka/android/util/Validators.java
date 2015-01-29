package lipuka.android.util;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators
{
	  //public static final String ALPHA_NUMERIC = "^[a-zA-Z0-9]*$";



        public static boolean isAlphaNumeric(String s){
            String pattern= "^[a-zA-Z0-9]*$";
            if(s.matches(pattern)){
                return true;
            }
            return false;   
    }
        public final static boolean isEmailAddress(CharSequence target) {
            if (target == null) {
                return false;
            } else {
                return false;

              //  return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            }
        }
        
        public static boolean isDate(int d, int m, int y) {
        	  m -= 1;
        	  Calendar c = Calendar.getInstance();
        	  c.setLenient(false);
        	  try {
        	    c.set(y, m, d);
        	    Date dt = c.getTime();
        	  } catch(IllegalArgumentException e) {
        	    return false;
        	  }
        	  return true;
        	}
}
