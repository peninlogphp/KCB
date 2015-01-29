package lipuka.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class LipukaCheckBox extends CheckBox implements LipukaInputView{
	private String id;

	public LipukaCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public LipukaCheckBox(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		}
	public LipukaCheckBox(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		}
	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}

	public String getData() {
		if(isChecked()){
			return id+"=1";
		}else{
			return id+"=0";	
		}
	}

}
