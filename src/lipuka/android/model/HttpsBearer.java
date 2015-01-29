package lipuka.android.model;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;

import kcb.android.Main;

import lipuka.android.util.FlushedInputStream;
import lipuka.android.util.Utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;






import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class HttpsBearer {
	
	private DefaultHttpClient client;	
	
	public HttpsBearer(){
		client = new DefaultHttpClient();	
			HttpParams httpParameters = client.getParams();
		// Set the timeout in milliseconds until a connection is established.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
       // ConnManagerParams.setTimeout(httpParameters, timeoutConnection);

	}
	public String makeGetRequest(String url) {
		
        HttpGet httpGet = null;
		try {
            Log.w(Main.TAG, "about to instantiate HttpGet"); 

	         httpGet = new HttpGet(url);

            Log.w(Main.TAG, "called makeGetRequest  "); 
	
			HttpResponse getResponse = client.execute(httpGet);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			
			if (statusCode != HttpStatus.SC_OK) { 
	            Log.w(Main.TAG, "Error " + statusCode + " for URL " + url); 
	            return null;
	        }
			
			HttpEntity responseEntity = getResponse.getEntity();
			
			if (responseEntity != null) {
				return EntityUtils.toString(responseEntity);
			}
			
		} 
		catch (ClientProtocolException e) {  
			httpGet.abort();
 e.printStackTrace();  
        }
		catch (SocketTimeoutException e) {
			httpGet.abort();
	        Log.w(Main.TAG, "Error for URL " + url, e);
		}
		catch (IOException e) {
			httpGet.abort();
	        Log.w(Main.TAG, "Error for URL " + url, e);
		}	
		catch (Exception e) {
			if(httpGet != null)
			httpGet.abort();
	        Log.w(Main.TAG, "Error for URL " + url, e);
		}
		return null;
		
	}
	
	public String makePostRequest(String url, List<NameValuePair> nameValuePairs) {
		
		   HttpPost httpPost = new HttpPost(url); 
		   httpPost.addHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");
		   httpPost.addHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
		   httpPost.addHeader("Pragma", "no-cache");

		try {
httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF8"));
			
			HttpResponse response = client.execute(httpPost);
			final int statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode != HttpStatus.SC_OK) { 
	            Log.d(Main.TAG, "Error " + statusCode + " for URL " + url); 
	            return null;
	        }
   	      Log.i(getClass().getSimpleName(), "Payload sent");

			HttpEntity responseEntity = response.getEntity();
			
			if (responseEntity != null) {
				return EntityUtils.toString(responseEntity);
			}
			
		} 
		catch (UnsupportedEncodingException e) {
	 		e.printStackTrace();
	 	}
		catch (ClientProtocolException e) {  
			httpPost.abort();
            e.printStackTrace();  
        }
		catch (SocketTimeoutException e) {
			httpPost.abort();
	        Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}
		catch (IOException e) {
			httpPost.abort();
	        Log.d(Main.TAG, "Error for URL " + url, e);
		}
		
		return null;
		
	}
	
	public InputStream retrieveStream(String url) {
		
		HttpGet getRequest = new HttpGet(url);
        
		try {
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			
			if (statusCode != HttpStatus.SC_OK) { 
	            Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
	            return null;
	        }

			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();
			
		} 
		catch (IOException e) {
			getRequest.abort();
	        Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}
		
		return null;
		
	}
	
	public Bitmap retrieveBitmap(String url) throws Exception {
		
		InputStream inputStream = null;
        try {
            inputStream = this.retrieveStream(url);
            final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
            return bitmap;
        } 
        finally {
            Utils.closeStreamQuietly(inputStream);
        }
		
	}

}
