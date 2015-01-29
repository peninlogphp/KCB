package lipuka.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class LipukaWebView extends WebView{

	private String id;
		private String url;

	public LipukaWebView(Context context) {
		super(context);
	    getSettings().setJavaScriptEnabled(true);
	}
	public LipukaWebView(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		}
	public LipukaWebView(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		}
public String getID(){
	return id;
}
public void setID(String id){
	this.id = id;
}
public String getURL(){
	return url;
}
public void setURL(String url){
	this.url = url;
}
public void load(){
	loadUrl(url);
}	
}
