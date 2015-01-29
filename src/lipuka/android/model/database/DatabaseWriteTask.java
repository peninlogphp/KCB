package lipuka.android.model.database;

import kcb.android.Main;
import lipuka.android.model.DbHelper;


import org.json.JSONArray;






import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DatabaseWriteTask implements Runnable{ // 
	public static final byte INSERT_PROFILE_XML_ACTION = 0;
	public static final byte INSERT_SERVICE_XML_ACTION = 1;
	public static final byte UPDATE_PROFILE_XML_ACTION = 2;
	public static final byte UPDATE_SERVICE_XML_ACTION = 3;
	public static final byte DELETE_PROFILE_XML_ACTION = 4;
	public static final byte DELETE_SERVICE_XML_ACTION = 5;
	public static final byte SAVE_GENRE_ACTION = 6;
	public static final byte SET_ACTIVATED_ACTION = 7;
	public static final byte SET_SUBSCRIBTION_ACTION = 8;
	public static final byte SAVE_PUBLIC_KEY_ACTION = 9;
	public static final byte SAVE_APP_DATA_ACTION = 10;
	public static final byte SAVE_LARI_ATM_LOCATIONS_ACTION = 11;
	public static final byte SAVE_OTHER_BANKS_ATM_LOCATIONS_ACTION = 12;
	public static final byte SAVE_BRANCH_LOCATIONS_ACTION = 13;

    
  Context context;
  private SQLiteDatabase database;
  private AsyncDatabaseHandler asyncDatabaseHandler;
  private DatabaseParams params;
  private byte action;

  // Constructor
  public DatabaseWriteTask(SQLiteDatabase database,
		  AsyncDatabaseHandler asyncDatabaseHandler,
		  DatabaseParams params, byte action) { // 
    this.database = database;
    this.asyncDatabaseHandler = asyncDatabaseHandler;
    this.params = params;
    this.action = action;
  }

	public void insertProfileXML() {
	synchronized (DbHelper.class) {
		database.delete(DbHelper.TABLE_PROFILE_XML, 
				DbHelper.C_IMSI + "="+ params.getParam("imsi"), null);
		database.delete(DbHelper.TABLE_SERVICE_XML, 
				DbHelper.C_IMSI + "="+ params.getParam("imsi"), null);
		
		DatabaseResponse dr = new DatabaseResponse();
	if(database.insert(DbHelper.TABLE_PROFILE_XML, null, params.getValues()) != -1){
		dr.setStatus(DatabaseResponse.OK);
	     Log.d(Main.TAG, "inserted profile xml: ok");

	}else{
		dr.setStatus(DatabaseResponse.ERROR);
		dr.setMessage("Could not insert profile xml");	
	     Log.d(Main.TAG, "inserted profile xml: error");

	}
	asyncDatabaseHandler.sendResponseMessage(dr);
	}
	}
	public void insertServiceXML() {
		synchronized (DbHelper.class) {
		
	DatabaseResponse dr = new DatabaseResponse();
if(database.insert(DbHelper.TABLE_SERVICE_XML, null, params.getValues()) != -1){
	dr.setStatus(DatabaseResponse.OK);
    Log.d(Main.TAG, "inserted service xml: "+params.getValues().getAsString(DbHelper.C_ACCOUNT));

	}else{
		dr.setStatus(DatabaseResponse.ERROR);
		dr.setMessage("Could not insert service xml "+params.getValues().getAsString(DbHelper.C_ACCOUNT));
	     Log.d(Main.TAG, "inserted service xml: error");

	}
asyncDatabaseHandler.sendResponseMessage(dr);
}
	}

	public void updateProfileXML() {
		synchronized (DbHelper.class) {
			database.beginTransaction();
			DatabaseResponse dr = new DatabaseResponse();
			String imsi = params.getParam("imsi");
		try{
			database.delete(DbHelper.TABLE_SERVICE_XML, 
					DbHelper.C_IMSI + "="+ imsi, null);
			
			if(database.update(DbHelper.TABLE_PROFILE_XML, params.getValues(),
					DbHelper.C_IMSI + "="
					+ imsi, null) > 0){
				String sql = "insert into "+DbHelper.TABLE_SERVICE_XML+
				" ("+DbHelper.C_IMSI+", "+DbHelper.C_ACCOUNT+", "+DbHelper.C_SERVICE_XML+
				", "+DbHelper.C_TIMESTAMP+", "+DbHelper.C_CURRENT+")"+
					" values (?, ?, ?, ?, ?)";
						SQLiteStatement insert = database.compileStatement(sql);
				JSONArray batchValues = params.getbatchValues();
						for (int i = 0; i < batchValues.length(); i++) {
						insert.bindString(1, imsi);
						insert.bindString(2, batchValues.getJSONObject(i).getString("account"));
						insert.bindString(3, batchValues.getJSONObject(i).getString("service_xml"));
						insert.bindLong(4, batchValues.getJSONObject(i).getLong("timestamp"));
						insert.bindLong(5, batchValues.getJSONObject(i).getLong("current"));

						insert.executeInsert();
						}
						database.setTransactionSuccessful();
				dr.setStatus(DatabaseResponse.OK);
			}else{
				dr.setStatus(DatabaseResponse.ERROR);
				dr.setMessage("Could not update profile xml");		
			}
		}catch(Exception e){
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not update profile xml: "+e.getMessage());	
		}
		finally {
			database.endTransaction();
		}
		asyncDatabaseHandler.sendResponseMessage(dr);
		}
		}
	
	public void updateServiceXML() {
		synchronized (DbHelper.class) {
	DatabaseResponse dr = new DatabaseResponse();
		if(database.update(DbHelper.TABLE_SERVICE_XML, params.getValues(),
				DbHelper.C_IMSI + "="
				+ params.getParam(DbHelper.C_IMSI)+ " AND "+DbHelper.C_ACCOUNT + "='"+params.getParam(DbHelper.C_ACCOUNT)+"'", null) > 0){
			dr.setStatus(DatabaseResponse.OK);
		}
		else{
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not update service xml");		
		}
		asyncDatabaseHandler.sendResponseMessage(dr);
	}
	}
	public void setSubscribtion() {
		synchronized (DbHelper.class) {
	DatabaseResponse dr = new DatabaseResponse();
		if(database.update(DbHelper.TABLE_SUBSCRIBTIONS, params.getValues(),
				DbHelper.C_CHANNEL_ID + "="
				+ params.getParam(DbHelper.C_CHANNEL_ID), null) > 0){
			dr.setStatus(DatabaseResponse.OK);
		}
		else{
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not set service xml current");		
		}
		asyncDatabaseHandler.sendResponseMessage(dr);
	}
	}
	public void deleteProfileXML() {
		synchronized (DbHelper.class) {
	DatabaseResponse dr = new DatabaseResponse();

if(database.delete(DbHelper.TABLE_PROFILE_XML, 
				DbHelper.C_IMSI + "="+ params.getParam("imsi"), null) > 0){
	dr.setStatus(DatabaseResponse.OK);
}		else{
	dr.setStatus(DatabaseResponse.ERROR);
	dr.setMessage("Could not delete profile xml");		
}
asyncDatabaseHandler.sendResponseMessage(dr);
	}
	}
	public void deleteServiceXML() {
		synchronized (DbHelper.class) {
	DatabaseResponse dr = new DatabaseResponse();
	if(database.delete(DbHelper.TABLE_SERVICE_XML, 
				DbHelper.C_IMSI + "="+ params.getParam("imsi"), null) > 0){
		dr.setStatus(DatabaseResponse.OK);
	}else{
		dr.setStatus(DatabaseResponse.ERROR);
		dr.setMessage("Could not delete service xml");		
	}
	asyncDatabaseHandler.sendResponseMessage(dr);
	}
	}

	public void saveGenre() {
		DatabaseResponse dr = new DatabaseResponse();
		boolean savedGenre = false;
synchronized (DbHelper.class) {
			
boolean updatedTimestamp = false;
	ContentValues values = new ContentValues();
		values.put(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
	database.beginTransaction();
try {
		if(database.update(DbHelper.TABLE_TIMESTAMPS, values,
		DbHelper.C_TABLE_NAME + "='"
		+ params.getParam("table")+"'", null) > 0){
	updatedTimestamp =true;
	}
else {
	values.clear();
	values.put(DbHelper.C_TABLE_NAME, params.getParam("table")); 
	values.put(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
	if(database.insert(DbHelper.TABLE_TIMESTAMPS, null, values) != -1){
		updatedTimestamp =true;
	}	
}
	
if(updatedTimestamp){
	   Log.d(Main.TAG, "updatedTimestamp");	 	 
	database.delete(params.getParam("table"), null, null); 
		
	String sql = "insert into "+params.getParam("table")+
" ("+DbHelper.C_CONTENT_ID+", "+DbHelper.C_CONTENT_NAME+", "+DbHelper.C_ARTIST_NAME+", "+DbHelper.C_ARTIST_ID+
", "+DbHelper.C_IMAGE_URL+")"+
	" values (?, ?, ?, ?, ?)";
		SQLiteStatement insert = database.compileStatement(sql);
JSONArray batchValues = params.getbatchValues();
		for (int i = 0; i < batchValues.length(); i++) {
		insert.bindLong(1, batchValues.getJSONObject(i).getInt("contentID"));
		insert.bindString(2, batchValues.getJSONObject(i).getString("contentName"));
		insert.bindString(3, batchValues.getJSONObject(i).getString("artistName"));
		insert.bindLong(4, batchValues.getJSONObject(i).getInt("artistID"));
insert.bindString(5, batchValues.getJSONObject(i).getString("imageurl"));
		insert.executeInsert();
		}
		database.setTransactionSuccessful();	
		savedGenre = true;
			
	}
		}catch (org.json.JSONException jsonError) {
      	}
		 finally {
			database.endTransaction();
		}
		}
		if(savedGenre){
			dr.setStatus(DatabaseResponse.OK);		
		}else{
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not delete service xml");	
		}
		asyncDatabaseHandler.sendResponseMessage(dr);
		}
	
	public void savePublicKey() {
		synchronized (DbHelper.class) {
			database.delete(DbHelper.TABLE_PUBLIC_KEY, null, null);
			
			DatabaseResponse dr = new DatabaseResponse();
		if(database.insert(DbHelper.TABLE_PUBLIC_KEY, null, params.getValues()) != -1){
			dr.setStatus(DatabaseResponse.OK);
		     Log.d(Main.TAG, "saved public key");

		}else{
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not save public key");	
		     Log.d(Main.TAG, "failed to save public key");

		}
		asyncDatabaseHandler.sendResponseMessage(dr);
		}
		}
	public void saveAppData() {
		synchronized (DbHelper.class) {
			database.delete(DbHelper.TABLE_APP_DATA, null, null);
			
			DatabaseResponse dr = new DatabaseResponse();
		if(database.insert(DbHelper.TABLE_APP_DATA, null, params.getValues()) != -1){
			dr.setStatus(DatabaseResponse.OK);
		     Log.d(Main.TAG, "saved app data");

		}else{
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not save app data");	
		     Log.d(Main.TAG, "failed to save app data");

		}
		asyncDatabaseHandler.sendResponseMessage(dr);
		}
		}
	public void saveLariATMLocations() {
		DatabaseResponse dr = new DatabaseResponse();
		boolean saved = false;
synchronized (DbHelper.class) {
			
boolean updatedTimestamp = false;
	ContentValues values = new ContentValues();
		values.put(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
	database.beginTransaction();
try {
		if(database.update(DbHelper.TABLE_LARI_ATM_L, values,
		null, null) > 0){
	updatedTimestamp =true;
	}
else {
	values.clear();
	values.put(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
	if(database.insert(DbHelper.TABLE_LARI_ATM_L, null, values) != -1){
		updatedTimestamp =true;
	}	
}
	
if(updatedTimestamp){
	   Log.d(Main.TAG, "updatedTimestamp");	 	 
	database.delete(DbHelper.TABLE_LARI_ATM_LOCATIONS, null, null); 
	
	String sql = "insert into "+DbHelper.TABLE_LARI_ATM_LOCATIONS+
" ("+DbHelper.C_JSON_DATA+")"+
	" values (?)";
		SQLiteStatement insert = database.compileStatement(sql);
JSONArray batchValues = params.getBatchValues(DbHelper.TABLE_LARI_ATM_LOCATIONS);
		for (int i = 0; i < batchValues.length(); i++) {
		insert.bindString(1, batchValues.getJSONObject(i).toString());
		insert.executeInsert();
		}
		 
		database.setTransactionSuccessful();	
		saved = true;
			
	}
		}catch (org.json.JSONException jsonError) {
      	}
		 finally {
			database.endTransaction();
		}
		}
		if(saved){
			dr.setStatus(DatabaseResponse.OK);		
		}else{
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not save lari atm locations");	
		}
		asyncDatabaseHandler.sendResponseMessage(dr);
		}
	public void saveOtherBanksATMLocations() {
		DatabaseResponse dr = new DatabaseResponse();
		boolean saved = false;
synchronized (DbHelper.class) {
			
boolean updatedTimestamp = false;
	ContentValues values = new ContentValues();
		values.put(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
	database.beginTransaction();
try {
		if(database.update(DbHelper.TABLE_OTHER_BANKS_ATM_L, values,
		null, null) > 0){
	updatedTimestamp =true;
	}
else {
	values.clear();
	values.put(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
	if(database.insert(DbHelper.TABLE_OTHER_BANKS_ATM_L, null, values) != -1){
		updatedTimestamp =true;
	}	
}
	
if(updatedTimestamp){
	   Log.d(Main.TAG, "updatedTimestamp");	 	 
	database.delete(DbHelper.TABLE_OTHER_BANKS_ATM_LOCATIONS, null, null); 
	
	String sql = "insert into "+DbHelper.TABLE_OTHER_BANKS_ATM_LOCATIONS+
" ("+DbHelper.C_JSON_DATA+")"+
	" values (?)";
		SQLiteStatement insert = database.compileStatement(sql);
JSONArray batchValues = params.getBatchValues(DbHelper.TABLE_OTHER_BANKS_ATM_LOCATIONS);
		for (int i = 0; i < batchValues.length(); i++) {
		insert.bindString(1, batchValues.getJSONObject(i).toString());
		insert.executeInsert();
		}
		 
		database.setTransactionSuccessful();	
		saved = true;
			
	}
		}catch (org.json.JSONException jsonError) {
      	}
		 finally {
			database.endTransaction();
		}
		}
		if(saved){
			dr.setStatus(DatabaseResponse.OK);		
		}else{
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not save other banks atm locations");	
		}
		asyncDatabaseHandler.sendResponseMessage(dr);
		}
	public void saveBranchLocations() {
		DatabaseResponse dr = new DatabaseResponse();
		boolean saved = false;
synchronized (DbHelper.class) {
			
boolean updatedTimestamp = false;
	ContentValues values = new ContentValues();
		values.put(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
	database.beginTransaction();
try {
		if(database.update(DbHelper.TABLE_BRANCH_L, values,
		null, null) > 0){
	updatedTimestamp =true;
	}
else {
	values.clear();
	values.put(DbHelper.C_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
	if(database.insert(DbHelper.TABLE_BRANCH_L, null, values) != -1){
		updatedTimestamp =true;
	}	
}
	
if(updatedTimestamp){
	   Log.d(Main.TAG, "updatedTimestamp");	 	 
	database.delete(DbHelper.TABLE_BRANCH_LOCATIONS, null, null); 
	
	String sql = "insert into "+DbHelper.TABLE_BRANCH_LOCATIONS+
" ("+DbHelper.C_JSON_DATA+")"+
	" values (?)";
		SQLiteStatement insert = database.compileStatement(sql);
JSONArray batchValues = params.getBatchValues(DbHelper.TABLE_BRANCH_LOCATIONS);
		for (int i = 0; i < batchValues.length(); i++) {
		insert.bindString(1, batchValues.getJSONObject(i).toString());
		insert.executeInsert();
		}
		 
		database.setTransactionSuccessful();	
		saved = true;
			
	}
		}catch (org.json.JSONException jsonError) {
      	}
		 finally {
			database.endTransaction();
		}
		}
		if(saved){
			dr.setStatus(DatabaseResponse.OK);		
		}else{
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not save branch locations");	
		}
		asyncDatabaseHandler.sendResponseMessage(dr);
		}
	/*public void setActivation() {
		synchronized (DbHelper.class) {
			
			DatabaseResponse dr = new DatabaseResponse();
		if(database.insert(DbHelper.TABLE_ACTIVATION, null, params.getValues()) != -1){
			dr.setStatus(DatabaseResponse.OK);
		     Log.d(Main.TAG, "set activated: ok");

		}else{
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("Could not set activated");	
		     Log.d(Main.TAG, "set activated error");

		}
		asyncDatabaseHandler.sendResponseMessage(dr);
		}
		}*/

	@Override
	public void run() {
		
        switch(action) {
        case INSERT_PROFILE_XML_ACTION:
            insertProfileXML();
            break;
        case INSERT_SERVICE_XML_ACTION:
            insertServiceXML();
            break;
        case UPDATE_PROFILE_XML_ACTION:
            updateProfileXML();
            break;
        case UPDATE_SERVICE_XML_ACTION:
            updateServiceXML();
            break;
        case DELETE_PROFILE_XML_ACTION:
            deleteProfileXML();
            break;
        case DELETE_SERVICE_XML_ACTION:
            deleteServiceXML();
            break;
        case SAVE_GENRE_ACTION:
            saveGenre();
            break;
        case SET_ACTIVATED_ACTION:
            //setActivation();
            break;
        case SET_SUBSCRIBTION_ACTION:
        	setSubscribtion();
            break;
        case SAVE_PUBLIC_KEY_ACTION:
        	savePublicKey();
            break;
        case SAVE_APP_DATA_ACTION:
        	saveAppData();
            break;
        case SAVE_LARI_ATM_LOCATIONS_ACTION:
        	saveLariATMLocations();
            break;
        case SAVE_OTHER_BANKS_ATM_LOCATIONS_ACTION:
        	saveOtherBanksATMLocations();
            break;
        case SAVE_BRANCH_LOCATIONS_ACTION:
        	saveBranchLocations();
            break;
            default:
    }		
	}
}