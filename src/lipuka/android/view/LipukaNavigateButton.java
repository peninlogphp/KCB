package lipuka.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class LipukaNavigateButton extends Button implements Navigate{

	private String activity;
	private boolean save;
	public LipukaNavigateButton(Context context) {
		super(context);
	}
	public LipukaNavigateButton(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		}
	public LipukaNavigateButton(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		}
	public String getActivity() {
		// TODO Auto-generated method stub
		return activity;
	}

	public void setActivity(String activity) {
this.activity = activity;		
	}

	public boolean isSave() {
		return save;
	}

	public void setSave(boolean save) {
		this.save = save;		
	}

	
}
