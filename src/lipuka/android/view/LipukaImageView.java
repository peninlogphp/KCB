package lipuka.android.view;

import greendroid.widget.AsyncImageView;
import android.content.Context;
import android.widget.ImageView;

public class LipukaImageView extends AsyncImageView{

	private String id;
	private String src;

	public LipukaImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
	/*public String getSrc(){
		return src;
	}
	public void setSrc(String src){
		this.src = src;
	}*/
}
