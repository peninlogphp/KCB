package lipuka.android.model;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LipukaWebViewClient extends WebViewClient {
	
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        /**
        when someone clicks on a lick determine whether 
        it s remote or a local link n then act accordingly*/
    	//view.loadUrl(url);
        return true;
    }

}
