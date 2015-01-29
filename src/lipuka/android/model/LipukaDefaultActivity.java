package lipuka.android.model;


import java.util.HashMap;

import kcb.android.Main;







import lipuka.android.view.LipukaEditText;
import lipuka.android.view.LipukaInputView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class LipukaDefaultActivity {
	
	public static boolean validationError = false;
	public static StringBuffer errorMsgs = new StringBuffer();
	public static StringBuffer payload = new StringBuffer();
	public static View currentView;

	public static String getData(LinearLayout layout){
		errorMsgs.setLength(0);
		payload.setLength(0);
		collectData(layout);
		return payload.toString();
		
	}
	public static void collectData(LinearLayout layout){
		
		int	viewCount = layout.getChildCount();
		Log.d(Main.TAG, "viewCount: "+viewCount);

		for (int i = 0; i < viewCount; i++){
			currentView = layout.getChildAt(i);
			
			if (currentView instanceof LipukaInputView){
if (currentView instanceof LipukaEditText){
	LipukaEditText let = (LipukaEditText)currentView;
	if (let.isRequired()){
		if (let.getData() == null || let.getData().length() == 0){
			validationError = true;
			errorMsgs.append("Please enter value for "+let.getID());
		return;	
		}	
	}
	
	if (let.getMax() > 0.0f){
		if (let.getData() != null && let.getData().length() != 0){
			float val = Float.valueOf(let.getData());
			if(val > let.getMax()){
				validationError = true;
				errorMsgs.append("Maximum value for "+let.getID()+" is "+let.getMax());
			return;	
			}	
		}	
	}
	
	if (let.getMin() > 0.0f){
		if (let.getData() != null && let.getData().length() != 0){
			float val = Float.valueOf(let.getData());
			if(val < let.getMin()){
				validationError = true;
				errorMsgs.append("Minimum value for "+let.getID()+" is "+let.getMin());
			return;	
			}	
		}	
	}
	
				}
					
payload.append(((LipukaInputView) currentView).getData());	
payload.append("|");
Log.d(Main.TAG, "got data: "+((LipukaInputView) currentView).getData()+" for "+currentView.getId());
Log.d(Main.TAG, "payload: "+payload);
		
			}else if(currentView instanceof LinearLayout){
				LinearLayout linearLayout = (LinearLayout)currentView;
				collectData(linearLayout);
			}
		}
		return;
		
	}
	public static void saveData(LinearLayout layout, HashMap<String, String> map){
		View currentView;
		int viewCount = layout.getChildCount();
		for (int i = 0; i < viewCount; i++){
			currentView = layout.getChildAt(i);
			
if (currentView instanceof LipukaEditText){
	LipukaEditText let = (LipukaEditText)currentView;
let.saveData(map);

				}else if(currentView instanceof LinearLayout){
					LinearLayout linearLayout = (LinearLayout)currentView;
					saveData(linearLayout, map);
				}
		}		
	}
	public static void restoreData(LinearLayout layout, HashMap<String, String> map){
		View currentView;
		int viewCount = layout.getChildCount();
		for (int i = 0; i < viewCount; i++){
			currentView = layout.getChildAt(i);
			
if (currentView instanceof LipukaEditText){
	LipukaEditText let = (LipukaEditText)currentView;
let.restoreData(map);

				}else if(currentView instanceof LinearLayout){
					LinearLayout linearLayout = (LinearLayout)currentView;
					restoreData(linearLayout, map);
				}
		}		
	}
	public static void reset(){
		validationError = false;
		errorMsgs.setLength(0);		
	}
	public static boolean isValidationError(){
		return validationError;		
	}
	public static void setValidationError(boolean ve){
		validationError = ve;		
	}
	public static String getErrorMsgs(){
		return errorMsgs.toString();		
	}
}
