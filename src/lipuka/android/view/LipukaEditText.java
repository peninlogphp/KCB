package lipuka.android.view;


import java.util.HashMap;

import kcb.android.Main;







import android.content.Context;
import android.util.Log;
import android.widget.EditText;

public class LipukaEditText extends EditText implements LipukaInputView{
	
	private String id;
	private boolean required;
	private float max, min;
	
	public LipukaEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
	public boolean isRequired(){
		return required;
	}
	public void setRequired(boolean required){
		this.required = required;
	}
	public float getMax(){
		return max;
	}
	public void setMax(float max){
		this.max = max;
	}
	public float getMin(){
		return min;
	}
	public void setMin(float min){
		this.min = min;
	}
	public String getData() {

		return getText().toString();
	}
	public void saveData(HashMap<String, String> map) {
		Log.d(Main.TAG, "Saved data: "+getText().toString()+"for "+id);

		map.put(id, getText().toString());
	}
	public void restoreData(HashMap<String, String> map) {
  		Log.d(Main.TAG, "restored data: "+map.get(id));

		setText(map.get(id));
	}
	
}
