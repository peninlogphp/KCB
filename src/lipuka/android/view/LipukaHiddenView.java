package lipuka.android.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class LipukaHiddenView extends TextView implements LipukaInputView{

	private String id;
	private String name;
	private String value;

	public LipukaHiddenView(Context context) {
		super(context);
setVisibility(View.GONE);
setWidth(LayoutParams.WRAP_CONTENT);
setHeight(LayoutParams.WRAP_CONTENT);
}

	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getValue(){
		return value;
	}
	public void setValue(String value){
		this.value = value;
	}

	public String getData() {
		return value;
	}
		
}
