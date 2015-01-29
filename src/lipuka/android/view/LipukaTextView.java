package lipuka.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class LipukaTextView extends TextView{

	private String id;
	private String lines;
	private String length;

	public LipukaTextView(Context context) {
		super(context);
		}
	
	public LipukaTextView(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		}
	public LipukaTextView(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		}
	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
	public String getLines(){
		return lines;
	}
	public void setLines(String lines){
		this.lines = lines;
	}
	public String getLength(){
		return length;
	}
	public void setLength(String length){
		this.length = length;
	}	
}
