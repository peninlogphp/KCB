package lipuka.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class LipukaRadioButton extends RadioButton {

	private String id;

	public LipukaRadioButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public LipukaRadioButton(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		}
	public LipukaRadioButton(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		}
	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}

}
