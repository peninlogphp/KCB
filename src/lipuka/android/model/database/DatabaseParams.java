package lipuka.android.model.database;

import java.util.HashMap;

import org.json.JSONArray;

import android.content.ContentValues;

public class DatabaseParams {

	private HashMap<String, String> params;
	private ContentValues values;
	private JSONArray batchValues;
	private HashMap<String, JSONArray> batchValuesParams;

	public DatabaseParams(){
		params = new HashMap<String, String>();	
		values = new ContentValues();
		batchValuesParams = new HashMap<String, JSONArray>();	
	}
	public void clearParams(){
    	this.params.clear();
    }
	public void clearValues(){
    	this.values.clear();
    }
	public void addParam(String key, String value){
    	this.params.put(key, value);
    } 
public String getParam(String key){
    	return params.get(key);
    }
public void addValue(String key, String value){
	this.values.put(key, value);
} 
public ContentValues getValues(){
	return values;
}
public JSONArray getbatchValues(){
	return batchValues;
}
public void setBatchValues(JSONArray songs){
	this.batchValues = songs;
} 
public JSONArray getBatchValues(String key){
	return batchValuesParams.get(key);
}
public void putBatchValues(String key, JSONArray songs){
	this.batchValuesParams.put(key, songs);
} 
}
