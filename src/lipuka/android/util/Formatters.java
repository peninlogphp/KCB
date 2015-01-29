package lipuka.android.util;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatters
{
	 private static final DecimalFormat myFormatter = new DecimalFormat("###,###,###.##");



        public static String formatAmount(Double d){
        
            return myFormatter.format(d);   
    }
        
}
