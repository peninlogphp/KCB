package lipuka.android.model.database;

import kcb.android.Main;
import lipuka.android.model.DbHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseReadTask implements Runnable{ // 
	
	public static final byte FETCH_ALL_PROFILE_XML_ACTION = 0;
	public static final byte FETCH_ALL_SERVICE_XML_ACTION = 1;
	public static final byte FETCH_PROFILE_XML_ACTION = 2;
	public static final byte FETCH_SERVICE_XML_ACTION = 3;
	public static final byte CHECK_ACTIVATION_ACTION = 4;
	public static final byte FETCH_GENRE_ACTION = 5;
	public static final byte FETCH_LARI_ATM_LOCATIONS = 6;
	public static final byte FETCH_OTHER_BANKS_ATM_LOCATIONS = 7;
	public static final byte FETCH_BRANCH_LOCATIONS = 8;
	private final long ONE_WEEK = 7*24*60*60*1000;

  Context context;
  private SQLiteDatabase database;
  private AsyncDatabaseHandler asyncDatabaseHandler;
  private DatabaseParams params;
  private byte action;

  // Constructor
  public DatabaseReadTask(SQLiteDatabase database,
		  AsyncDatabaseHandler asyncDatabaseHandler,
		  DatabaseParams params, byte action) { // 
    this.database = database;
    this.asyncDatabaseHandler = asyncDatabaseHandler;
    this.params = params;
    this.action = action;
  }
	public void fetchAllProfileXML() {
		DatabaseResponse dr = new DatabaseResponse();

		dr.setCursor(database.query(DbHelper.TABLE_PROFILE_XML, new String[] {
				DbHelper.C_IMSI, DbHelper.C_PROFILE_XML}, null, null, null,
				null, null));
		asyncDatabaseHandler.sendResponseMessage(dr);
	}
	public void fetchAllServiceXML() {
		DatabaseResponse dr = new DatabaseResponse();

		try{dr.setCursor(database.query(DbHelper.TABLE_SERVICE_XML, new String[] {
				DbHelper.C_IMSI, DbHelper.C_ACCOUNT, DbHelper.C_SERVICE_XML, DbHelper.C_TIMESTAMP}, null, null, null,
				null, null));
		dr.setStatus(DatabaseResponse.OK);

		}catch(Exception e){
			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage(e.getMessage());

		     Log.d(Main.TAG, "fetch all services error: ", e);
		}	
		asyncDatabaseHandler.sendResponseMessage(dr);

	}

	public void fetchProfileXML() {
		DatabaseResponse dr = new DatabaseResponse();

		Cursor mCursor = database.query(true, DbHelper.TABLE_PROFILE_XML, new String[] {
				DbHelper.C_IMSI, DbHelper.C_PROFILE_XML },
	DbHelper.C_IMSI + "=" + params.getParam("imsi"), null, null, null, null, null);
		dr.setCursor(mCursor);
		asyncDatabaseHandler.sendResponseMessage(dr);
	}
	public void fetchServiceXML()  {
		DatabaseResponse dr = new DatabaseResponse();

		Cursor mCursor = database.query(true, DbHelper.TABLE_SERVICE_XML, new String[] {
				DbHelper.C_IMSI, DbHelper.C_SERVICE_XML, DbHelper.C_TIMESTAMP },
	DbHelper.C_IMSI + "=" + params.getParam("imsi"), null, null, null, null, null);
		dr.setCursor(mCursor);
		asyncDatabaseHandler.sendResponseMessage(dr);
	}
	public void checkActivation() {
		DatabaseResponse dr = new DatabaseResponse();

		Cursor profileCursor = database.query(true, DbHelper.TABLE_PROFILE_XML, new String[] {
				DbHelper.C_IMSI, DbHelper.C_PROFILE_XML },
	DbHelper.C_IMSI + "=" + params.getParam("imsi"), null, null, null, null, null);
		Cursor serviceXmlCursor = database.query(true, DbHelper.TABLE_SERVICE_XML, new String[] {
				DbHelper.C_IMSI, DbHelper.C_SERVICE_XML, DbHelper.C_TIMESTAMP },
	DbHelper.C_IMSI + "=" + params.getParam("imsi"), null, null, null, null, null);
		if(profileCursor.moveToFirst() && serviceXmlCursor.moveToFirst()){
			dr.setStatus(DatabaseResponse.OK);
		     Log.d(Main.TAG, "checked activation: ok");

		}else{
			dr.setStatus(DatabaseResponse.ERROR);
		     Log.d(Main.TAG, "checked activation: error");

		}
		profileCursor.close();
		serviceXmlCursor.close();
		asyncDatabaseHandler.sendResponseMessage(dr);


	}
	public void fetchGenre() {
		asyncDatabaseHandler.sendStartMessage();
		DatabaseResponse dr = new DatabaseResponse();

	try{	Cursor timestampsCursor = database.query(true, DbHelper.TABLE_TIMESTAMPS, new String[] {
				DbHelper.C_TIMESTAMP },
	DbHelper.C_TABLE_NAME + "='" + params.getParam("table")+"'", null, null, null, null, null);

	     if(timestampsCursor.moveToFirst()){
long timestamp = timestampsCursor.getLong(timestampsCursor.getColumnIndex(DbHelper.C_TIMESTAMP));

if (System.currentTimeMillis() - timestamp <= (24*60*60*1000)){
	Cursor songs = database.query(params.getParam("table"), new String[] {
DbHelper.C_CONTENT_ID, DbHelper.C_CONTENT_NAME, DbHelper.C_ARTIST_NAME, DbHelper.C_ARTIST_ID, DbHelper.C_IMAGE_URL },
null, null, null, null, null);
	   Log.d(Main.TAG, "got good songs ");	 	 
	   timestampsCursor.close();
	dr.setStatus(DatabaseResponse.OK);	
	dr.setCursor(songs);	
}else{
	dr.setStatus(DatabaseResponse.ERROR);
	dr.setMessage("Local copy has expired");
	   Log.d(Main.TAG, "Local copy has expired");	 	 

}
		}else{

			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("No songs saved locally yet");
			   Log.d(Main.TAG, "No songs saved locally yet");	 	 

		}
	}catch (Exception e){
		dr.setStatus(DatabaseResponse.ERROR);
		dr.setMessage(e.getMessage());
		   Log.d(Main.TAG, "Error: "+e.getMessage(), e);	 	 
	}
		asyncDatabaseHandler.sendResponseMessage(dr);
	}
	public void fetchLariAtmLocations() {
		asyncDatabaseHandler.sendStartMessage();
		DatabaseResponse dr = new DatabaseResponse();

	try{	Cursor timestampsCursor = database.query(true, DbHelper.TABLE_LARI_ATM_L, new String[] {
				DbHelper.C_TIMESTAMP },
	null, null, null, null, null, null);

	     if(timestampsCursor.moveToFirst()){
long timestamp = timestampsCursor.getLong(timestampsCursor.getColumnIndex(DbHelper.C_TIMESTAMP));

if (System.currentTimeMillis() - timestamp <= ONE_WEEK){
	Cursor songs = database.query(true, DbHelper.TABLE_LARI_ATM_LOCATIONS, new String[] {
			DbHelper.C_JSON_DATA },
			null, null, null, null, null, null);
	   Log.d(Main.TAG, "got good lari atm locations");	 	 
	   timestampsCursor.close();
	dr.setStatus(DatabaseResponse.OK);	
	dr.setCursor(songs);	
}else{
	dr.setStatus(DatabaseResponse.ERROR);
	dr.setMessage("lari atm locations local copy has expired");
	   Log.d(Main.TAG, "lari atm locations local copy has expired");	 	 

}
		}else{

			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("No lari atm locations saved locally yet");
			   Log.d(Main.TAG, "No lari atm locations songs saved locally yet");	 	 

		}
	}catch (Exception e){
		dr.setStatus(DatabaseResponse.ERROR);
		dr.setMessage(e.getMessage());
		   Log.d(Main.TAG, "Error: "+e.getMessage(), e);	 	 
	}
		asyncDatabaseHandler.sendResponseMessage(dr);
	}
	public void fetchOtherBanksAtmLocations() {
		asyncDatabaseHandler.sendStartMessage();
		DatabaseResponse dr = new DatabaseResponse();

	try{	Cursor timestampsCursor = database.query(true, DbHelper.TABLE_OTHER_BANKS_ATM_L, new String[] {
				DbHelper.C_TIMESTAMP },
	null, null, null, null, null, null);

	     if(timestampsCursor.moveToFirst()){
long timestamp = timestampsCursor.getLong(timestampsCursor.getColumnIndex(DbHelper.C_TIMESTAMP));

if (System.currentTimeMillis() - timestamp <= ONE_WEEK){
	Cursor songs = database.query(true, DbHelper.TABLE_OTHER_BANKS_ATM_LOCATIONS, new String[] {
			DbHelper.C_JSON_DATA },
			null, null, null, null, null, null);
	   Log.d(Main.TAG, "got good other banks atm locations");	 	 
	   timestampsCursor.close();
	dr.setStatus(DatabaseResponse.OK);	
	dr.setCursor(songs);	
}else{
	dr.setStatus(DatabaseResponse.ERROR);
	dr.setMessage("other banks atm locations local copy has expired");
	   Log.d(Main.TAG, "other banks atm locations local copy has expired");	 	 

}
		}else{

			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("No other banks atm locations saved locally yet");
			   Log.d(Main.TAG, "No other banks atm locations songs saved locally yet");	 	 

		}
	}catch (Exception e){
		dr.setStatus(DatabaseResponse.ERROR);
		dr.setMessage(e.getMessage());
		   Log.d(Main.TAG, "Error: "+e.getMessage(), e);	 	 
	}
		asyncDatabaseHandler.sendResponseMessage(dr);
	}
	public void fetchBranchLocations() {
		asyncDatabaseHandler.sendStartMessage();
		DatabaseResponse dr = new DatabaseResponse();

	try{	Cursor timestampsCursor = database.query(true, DbHelper.TABLE_BRANCH_L, new String[] {
				DbHelper.C_TIMESTAMP },
	null, null, null, null, null, null);

	     if(timestampsCursor.moveToFirst()){
long timestamp = timestampsCursor.getLong(timestampsCursor.getColumnIndex(DbHelper.C_TIMESTAMP));

if (System.currentTimeMillis() - timestamp <= ONE_WEEK){
	Cursor songs = database.query(true, DbHelper.TABLE_BRANCH_LOCATIONS, new String[] {
			DbHelper.C_JSON_DATA },
			null, null, null, null, null, null);
	   Log.d(Main.TAG, "got good branch locations");	 	 
	   timestampsCursor.close();
	dr.setStatus(DatabaseResponse.OK);	
	dr.setCursor(songs);	
}else{
	dr.setStatus(DatabaseResponse.ERROR);
	dr.setMessage("branch locations local copy has expired");
	   Log.d(Main.TAG, "branch locations local copy has expired");	 	 

}
		}else{

			dr.setStatus(DatabaseResponse.ERROR);
			dr.setMessage("No branch locations saved locally yet");
			   Log.d(Main.TAG, "No branch locations songs saved locally yet");	 	 

		}
	}catch (Exception e){
		dr.setStatus(DatabaseResponse.ERROR);
		dr.setMessage(e.getMessage());
		   Log.d(Main.TAG, "Error: "+e.getMessage(), e);	 	 
	}
		asyncDatabaseHandler.sendResponseMessage(dr);
	}
	private ContentValues createContentValues(String imsi, String profileXML) {
		ContentValues values = new ContentValues();
		values.put(DbHelper.C_IMSI, imsi);
		values.put(DbHelper.C_PROFILE_XML, profileXML);
		return values;
	}


	@Override
	public void run() {
        switch(action) {
        
        case FETCH_ALL_PROFILE_XML_ACTION:
            fetchAllProfileXML();
            break;
        case FETCH_ALL_SERVICE_XML_ACTION:
            fetchAllServiceXML();
            break;
        case FETCH_PROFILE_XML_ACTION:
            fetchProfileXML();
            break;
        case FETCH_SERVICE_XML_ACTION:
            fetchServiceXML();
            break;
        case CHECK_ACTIVATION_ACTION:
            checkActivation();
            break;
        case FETCH_GENRE_ACTION:
            fetchGenre();
            break;
        case FETCH_LARI_ATM_LOCATIONS:
            fetchLariAtmLocations();
            break;
        case FETCH_OTHER_BANKS_ATM_LOCATIONS:
            fetchOtherBanksAtmLocations();
            break;
        case FETCH_BRANCH_LOCATIONS:
            fetchBranchLocations();
            break;
            default:
    }		
	}
}