package lipuka.android.view;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LipukaRadioGroup extends RadioGroup implements LipukaInputView{

	private String id;

	public LipukaRadioGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}

	public String getData() {
		RadioButton currentRadioButton;
		int viewCount = getChildCount();
		for (int i = 0; i < viewCount; i++){
			currentRadioButton = (RadioButton)getChildAt(i);			
			if (currentRadioButton.isChecked()){
				return currentRadioButton.getText().toString();
			}
		}
		return null;
	}
}
