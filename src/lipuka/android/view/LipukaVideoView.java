package lipuka.android.view;

import android.content.Context;
import android.widget.VideoView;

public class LipukaVideoView extends VideoView{

	private String id;
	private String src;
	private boolean autostart;

	public LipukaVideoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public String getID(){
		return id;
	}
	public void setID(String id){
		this.id = id;
	}
	public String getSrc(){
		return src;
	}
	public void setSrc(String src){
		this.src = src;
	}
	public boolean isAutostart(){
		return autostart;
	}
	public void setAutostart(boolean autostart){
		this.autostart = autostart;
	}
}
