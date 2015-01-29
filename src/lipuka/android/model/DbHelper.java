package lipuka.android.model;


import kcb.android.LipukaApplication;
import kcb.android.Main;

import org.json.JSONArray;






import lipuka.android.model.database.DatabaseResponse;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper { // 
	static final String DB_NAME = "lipuka.db"; // 
	public static final int DB_VERSION = 1; // 
	public  static final String TABLE_PROFILE_XML = "profile_xml"; // 
	public static final String TABLE_SERVICE_XML = "service_xml"; 
	public static final String TABLE_TIMESTAMPS = "timestamps"; //
	//public static final String TABLE_ACTIVATION = "activation"; //
	public static final String TABLE_TOP_TEN = "topsongs";  
	public static final String TABLE_LATEST_SONGS = "latest";  
	public static final String TABLE_KIKUYU = "kikuyu";  
	public static final String TABLE_GOSPEL = "gospel";  
	public static final String TABLE_KALENJIN = "kalenjin";  
	public static final String TABLE_POP = "pop";  
	public static final String TABLE_LUO = "luo";  
	public static final String TABLE_KAMBA = "kamba";  
	public static final String TABLE_SUBSCRIBTIONS = "subscribtions";  
	public  static final String TABLE_REQUEST_ID = "request_id"; // 
	public  static final String TABLE_PUBLIC_KEY = "public_key"; 
	public  static final String TABLE_APP_DATA = "eft_data"; 
	public  static final String TABLE_LARI_ATM_LOCATIONS = "lari_atm_locations";
	public  static final String TABLE_LARI_ATM_L = "lari_atm_l";
	public  static final String TABLE_OTHER_BANKS_ATM_LOCATIONS = "other_banks_atm_locations";
	public  static final String TABLE_OTHER_BANKS_ATM_L = "other_banks_atm_l";
	public  static final String TABLE_BRANCH_LOCATIONS = "branch_locations";
	public  static final String TABLE_BRANCH_L = "branch_l";
	
	public static final String C_ID = BaseColumns._ID;
	public static final String C_IMSI = "imsi";
	public static final String C_PROFILE_XML = "profile_xml";
	public static final String C_SERVICE_XML = "service_xml";
	public static final String C_ACCOUNT = "account";
	public static final String C_ACTIVATED = "activated";
	public static final String C_CURRENT = "current";

	public static final String C_TIMESTAMP = "timestamp";
	public static final String C_TABLE_NAME = "table_name";
	public static final String C_CONTENT_ID = "content_id";
	public static final String C_CONTENT_NAME = "content_name";
	public static final String C_ARTIST_NAME = "artist_name";
	public static final String C_ARTIST_ID = "artist_id";
	public static final String C_IMAGE_URL = "image_url";

	public static final String C_CHANNEL_ID = "channel_id";
	public static final String C_SUBSCRIBED = "subscribed";
	
	public static final String C_REQUEST_ID_VALUE = "request_id_value";
	public static final String C_PUBLIC_KEY = "public_key";
	public static final String C_PUBLIC_KEY_VERSION = "public_key_version";
	public static final String C_APP_DATA = "eft_data";
	public static final String C_APP_DATA_VERSION = "eft_data_version";
	public static final String C_JSON_DATA = "json_data";

  Context context;
  private SQLiteDatabase database;

  // Constructor
  public DbHelper(Context context) { // 
    super(context, DB_NAME, null, DB_VERSION);
    this.context = context;
  }

  // Called only once, first time the DB is created
  @Override
  public void onCreate(SQLiteDatabase db) {
    String sql = "create table " + TABLE_PROFILE_XML + " (" + C_ID + " integer primary key autoincrement, "
    + C_IMSI + " text, "+ C_PROFILE_XML + " text)"; // 

    db.execSQL(sql);  // 
    sql = "create table " + TABLE_SERVICE_XML + " (" + C_ID + " integer primary key autoincrement, "
    + C_IMSI + " text, "+ C_ACCOUNT + " text, "  + C_SERVICE_XML + " text, "+ C_TIMESTAMP+ " integer, " +
    		 C_CURRENT+ " integer)"; // 

    db.execSQL(sql); 
    
    /*sql = "create table " + TABLE_ACTIVATION + " (" + C_ID + " integer primary key autoincrement, "
    + C_IMSI + " text, "+ C_ACTIVATED+ " integer)"; // 

    db.execSQL(sql); */
    
    sql = "create table " + TABLE_TIMESTAMPS + " (" + C_ID + " integer primary key autoincrement, "
    + C_TABLE_NAME + " text, "+ C_TIMESTAMP+ " integer)"; 

    db.execSQL(sql);  

    sql = "create table " + TABLE_TOP_TEN + " (" + C_ID + " integer primary key autoincrement, "
    + C_CONTENT_ID + " integer, "+ C_CONTENT_NAME+ " text, "
    + C_ARTIST_NAME+ " text, "+ C_ARTIST_ID + " integer, "+ C_IMAGE_URL+ " text)"; 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_LATEST_SONGS + " (" + C_ID + " integer primary key autoincrement, "
    + C_CONTENT_ID + " integer, "+ C_CONTENT_NAME+ " text, "
    + C_ARTIST_NAME+ " text, "+ C_ARTIST_ID + " integer, "+ C_IMAGE_URL+ " text)"; 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_KIKUYU + " (" + C_ID + " integer primary key autoincrement, "
    + C_CONTENT_ID + " integer, "+ C_CONTENT_NAME+ " text, "
    + C_ARTIST_NAME+ " text, "+ C_ARTIST_ID + " integer, "+ C_IMAGE_URL+ " text)"; 

    db.execSQL(sql); 
    
    sql = "create table " + TABLE_GOSPEL + " (" + C_ID + " integer primary key autoincrement, "
    + C_CONTENT_ID + " integer, "+ C_CONTENT_NAME+ " text, "
    + C_ARTIST_NAME+ " text, "+ C_ARTIST_ID + " integer, "+ C_IMAGE_URL+ " text)"; 

    db.execSQL(sql); 
    
    sql = "create table " + TABLE_KALENJIN + " (" + C_ID + " integer primary key autoincrement, "
    + C_CONTENT_ID + " integer, "+ C_CONTENT_NAME+ " text, "
    + C_ARTIST_NAME+ " text, "+ C_ARTIST_ID + " integer, "+ C_IMAGE_URL+ " text)"; 

    db.execSQL(sql); 
    
    sql = "create table " + TABLE_POP + " (" + C_ID + " integer primary key autoincrement, "
    + C_CONTENT_ID + " integer, "+ C_CONTENT_NAME+ " text, "
    + C_ARTIST_NAME+ " text, "+ C_ARTIST_ID + " integer, "+ C_IMAGE_URL+ " text)"; 

    db.execSQL(sql); 
    
    sql = "create table " + TABLE_LUO + " (" + C_ID + " integer primary key autoincrement, "
    + C_CONTENT_ID + " integer, "+ C_CONTENT_NAME+ " text, "
    + C_ARTIST_NAME+ " text, "+ C_ARTIST_ID + " integer, "+ C_IMAGE_URL+ " text)"; 

    db.execSQL(sql); 
    
    sql = "create table " + TABLE_KAMBA + " (" + C_ID + " integer primary key autoincrement, "
    + C_CONTENT_ID + " integer, "+ C_CONTENT_NAME+ " text, "
    + C_ARTIST_NAME+ " text, "+ C_ARTIST_ID + " integer, "+ C_IMAGE_URL+ " text)"; 

    db.execSQL(sql); 

    sql = "create table " + TABLE_SUBSCRIBTIONS + " (" + C_ID + " integer primary key autoincrement, "
    + C_CHANNEL_ID + " integer, "+ C_SUBSCRIBED+ " integer)"; 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_REQUEST_ID + " (" + C_ID + " integer primary key autoincrement, "
    + C_REQUEST_ID_VALUE + " integer)"; 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_PUBLIC_KEY + " (" + C_ID + " integer primary key autoincrement, "
    + C_PUBLIC_KEY + " text, "+ C_PUBLIC_KEY_VERSION + " integer)"; // 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_APP_DATA + " (" + C_ID + " integer primary key autoincrement, "
    + C_APP_DATA + " text, "+ C_APP_DATA_VERSION + " integer)"; // 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_LARI_ATM_L + " (" + C_ID + " integer primary key autoincrement, "
    + C_TIMESTAMP+ " integer)"; 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_LARI_ATM_LOCATIONS + " (" + C_ID + " integer primary key autoincrement, "
    + C_JSON_DATA + " text)"; 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_OTHER_BANKS_ATM_L + " (" + C_ID + " integer primary key autoincrement, "
    + C_TIMESTAMP+ " integer)"; 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_OTHER_BANKS_ATM_LOCATIONS+ " (" + C_ID + " integer primary key autoincrement, "
    + C_JSON_DATA + " text)"; 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_BRANCH_L + " (" + C_ID + " integer primary key autoincrement, "
    + C_TIMESTAMP+ " integer)"; 

    db.execSQL(sql);
    
    sql = "create table " + TABLE_BRANCH_LOCATIONS + " (" + C_ID + " integer primary key autoincrement, "
    + C_JSON_DATA + " text)"; 

    db.execSQL(sql);
    Log.d(Main.TAG, "Created database");
  }
  public void open() {
		database = getWritableDatabase();
	}
  // Called whenever newVersion != oldVersion
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // 

	  Cursor profile = fetchAllProfileXML();
	  Cursor serviceXmlFiles = fetchAllServiceXML();
	  Cursor subscriptions = fetchAllSubscriptions();
	  
    db.execSQL("drop table if exists " + TABLE_PROFILE_XML); // drops the old database
    db.execSQL("drop table if exists " + TABLE_SERVICE_XML); // 
    db.execSQL("drop table if exists " + TABLE_TIMESTAMPS); 
    db.execSQL("drop table if exists " + TABLE_TOP_TEN); 
    db.execSQL("drop table if exists " + TABLE_LATEST_SONGS); 
    db.execSQL("drop table if exists " + TABLE_KIKUYU); 
    db.execSQL("drop table if exists " + TABLE_GOSPEL); 
    db.execSQL("drop table if exists " + TABLE_KALENJIN); 
    db.execSQL("drop table if exists " + TABLE_POP); 
    db.execSQL("drop table if exists " + TABLE_LUO); 
    db.execSQL("drop table if exists " + TABLE_KAMBA); 
    db.execSQL("drop table if exists " + TABLE_SUBSCRIBTIONS); 
    db.execSQL("drop table if exists " + TABLE_REQUEST_ID); 
 
    onCreate(db); // run onCreate to get new database
    
    upgradeDatabase(profile, serviceXmlFiles, subscriptions);
    
    Log.d(Main.TAG, "Updated database");

  }

  public SQLiteDatabase getDatabase(){
	  return database;
  }
	public long insertProfileXML(String imsi, String profileXML) {
		ContentValues values = createContentValues(imsi, profileXML);
		return database.insert(TABLE_PROFILE_XML, null, values);
	}
	public long insertServiceXML(String imsi, String serviceXML, long timestamp) {
		ContentValues values = createContentValues(imsi, serviceXML, timestamp);
		return database.insert(TABLE_SERVICE_XML, null, values);
	}

	public boolean updateProfileXML(String imsi, String profileXML) {
		ContentValues values = new ContentValues();
		values.put(C_PROFILE_XML, profileXML);

		return database.update(TABLE_PROFILE_XML, values, C_IMSI + "="
				+ imsi, null) > 0;
	}
	public boolean updateServiceXML(String imsi, String serviceXML, long timestamp) {
		ContentValues values = new ContentValues();
		values.put(C_SERVICE_XML, serviceXML);
		values.put(C_TIMESTAMP, String.valueOf(timestamp));

		return database.update(TABLE_SERVICE_XML, values, C_IMSI + "="
				+ imsi, null) > 0;
	}

	public boolean deleteProfileXML(String imsi) {
		return database.delete(TABLE_PROFILE_XML, C_IMSI + "="+ imsi, null) > 0;
	}
	public boolean deleteServiceXML(String imsi) {
		return database.delete(TABLE_SERVICE_XML, C_IMSI + "="+ imsi, null) > 0;
	}

	public Cursor fetchAllProfileXML() {
		return database.query(TABLE_PROFILE_XML, new String[] {
				C_IMSI, C_PROFILE_XML}, null, null, null,
				null, null);
	}
	public Cursor fetchAllServiceXML() {
		return database.query(DbHelper.TABLE_SERVICE_XML, new String[] {
				DbHelper.C_IMSI, DbHelper.C_ACCOUNT, DbHelper.C_SERVICE_XML, DbHelper.C_TIMESTAMP, DbHelper.C_CURRENT}, null, null, null,
				null, null);
	}
	
	public Cursor fetchAllSubscriptions() {
		return database.query(DbHelper.TABLE_SUBSCRIBTIONS, new String[] {
				DbHelper.C_CHANNEL_ID, DbHelper.C_SUBSCRIBED}, null, null, null,
				null, null);
	}

	public Cursor fetchAllRequestIDs() {
		return database.query(DbHelper.TABLE_REQUEST_ID, new String[] {
				DbHelper.C_REQUEST_ID_VALUE}, null, null, null,
				null, null);
	}
	
	public void upgradeDatabase(Cursor profile, Cursor serviceXmlFiles, Cursor subscriptions) {
			
boolean inserted = false;
	ContentValues values = new ContentValues();
	String sql = null;
	database.beginTransaction();
try {
	if (profile != null && profile.moveToFirst()){
	values.put(C_IMSI, profile.getString(profile.getColumnIndex(DbHelper.C_IMSI)));
	values.put(C_PROFILE_XML, profile.getString(profile.getColumnIndex(DbHelper.C_PROFILE_XML)));
	database.insert(TABLE_PROFILE_XML, null, values);
	
	}
	
	
	if (serviceXmlFiles != null){
	sql = "insert into "+TABLE_SERVICE_XML+
	" ("+C_IMSI+", "+C_ACCOUNT+", "+C_SERVICE_XML+", "+C_TIMESTAMP+
	", "+C_CURRENT+")"+
		" values (?, ?, ?, ?, ?)";
	
			SQLiteStatement insert = database.compileStatement(sql);

			while (serviceXmlFiles.moveToNext()) {
			insert.bindString(1, serviceXmlFiles.getString(serviceXmlFiles.getColumnIndex(DbHelper.C_IMSI)));
			insert.bindString(2, serviceXmlFiles.getString(serviceXmlFiles.getColumnIndex(DbHelper.C_ACCOUNT)));
			insert.bindString(3, serviceXmlFiles.getString(serviceXmlFiles.getColumnIndex(DbHelper.C_SERVICE_XML)));
			insert.bindLong(4, serviceXmlFiles.getLong(serviceXmlFiles.getColumnIndex(DbHelper.C_TIMESTAMP)));
			insert.bindLong(5, serviceXmlFiles.getLong(serviceXmlFiles.getColumnIndex(DbHelper.C_CURRENT)));
			insert.executeInsert();
			}
	}
	
	
	if (subscriptions != null){
	sql = "insert into "+TABLE_SUBSCRIBTIONS+
	" ("+C_CHANNEL_ID+", "+C_SUBSCRIBED+")"+
		" values (?, ?)";
	
			SQLiteStatement insert = database.compileStatement(sql);

			while (subscriptions.moveToNext()) {
			insert.bindLong(1, subscriptions.getLong(subscriptions.getColumnIndex(DbHelper.C_CHANNEL_ID)));
			insert.bindLong(2, subscriptions.getLong(subscriptions.getColumnIndex(DbHelper.C_SUBSCRIBED)));
			insert.executeInsert();
			}
	}
	
	database.setTransactionSuccessful();	

		}catch (Exception e) {
      	}
		 finally {
			database.endTransaction();
			profile.close();
			serviceXmlFiles.close();
			subscriptions.close();
		}
		

		}
	
	public Cursor fetchProfileXML(String imsi)  {
		Cursor mCursor = database.query(true, TABLE_PROFILE_XML, new String[] {
				C_IMSI, C_PROFILE_XML },
				C_IMSI + "=" + imsi, null, null, null, null, null);
		return mCursor;
	}
	public Cursor fetchServiceXML(String imsi, String account) {
		Cursor mCursor = database.query(true, TABLE_SERVICE_XML, new String[] {
				C_IMSI, C_SERVICE_XML, C_TIMESTAMP },
				C_IMSI + "= ?" + " AND "+C_ACCOUNT + "= ?", new String[] { imsi, account },
				null, null, null, null);
		return mCursor;
	}
	public Cursor fetchSubscribtions() {
		return database.query(DbHelper.TABLE_SUBSCRIBTIONS, new String[] {
				DbHelper.C_CHANNEL_ID, DbHelper.C_SUBSCRIBED}, null, null, null,
				null, null);
	}
	public void insertChannels(SQLiteDatabase db) {
			
	try{
		
		db.beginTransaction();
			String sql = "insert into "+TABLE_SUBSCRIBTIONS+
			" ("+C_CHANNEL_ID+", "+DbHelper.C_SUBSCRIBED+")"+
				" values (?, ?)";
					SQLiteStatement insert = db.compileStatement(sql);
					insert.bindLong(1, 505);
					insert.bindLong(2, 0);
					insert.executeInsert();
					insert.bindLong(1, 701);
					insert.bindLong(2, 0);
					insert.executeInsert();
					insert.bindLong(1, 15);
					insert.bindLong(2, 0);
					insert.executeInsert();
					insert.bindLong(1, 840);
					insert.bindLong(2, 0);
					insert.executeInsert();
					insert.bindLong(1, 16);
					insert.bindLong(2, 0);
					insert.executeInsert();
					insert.bindLong(1, 21);
					insert.bindLong(2, 0);
					insert.executeInsert();
					insert.bindLong(1, 31);
					insert.bindLong(2, 0);
					insert.executeInsert();
					insert.bindLong(1, 751);
					insert.bindLong(2, 0);
					insert.executeInsert();
					db.setTransactionSuccessful();
	
	}catch(Exception e){
	    Log.d(Main.TAG, "insert channels error: ", e);
	
	}
	finally {
		db.endTransaction();
	}
	}
	public Cursor fetchPublicKey()  {
		Cursor mCursor = database.query(true, TABLE_PUBLIC_KEY, new String[] {
				C_PUBLIC_KEY, C_PUBLIC_KEY_VERSION },
				null, null, null, null, null, null);
		return mCursor;
	}
	public Cursor fetchAppData()  {
		Cursor mCursor = database.query(true, TABLE_APP_DATA, new String[] {
				C_APP_DATA, C_APP_DATA_VERSION },
				null, null, null, null, null, null);
		return mCursor;
	}
	/*public boolean checkActivation(String imsi) throws SQLException {
		Cursor cursor = database.query(true, TABLE_ACTIVATION, new String[] {
				C_ACTIVATED },
				C_IMSI + "=" + imsi, null, null, null, null, null);
		 int activatedIndex = cursor.getColumnIndex(DbHelper.C_ACTIVATED);
		 long activated = 0;
     	if (cursor.moveToNext()){
     		 activated = cursor.getLong(activatedIndex);
    	   	Log.d(Main.TAG, "activated code"+activated);
         	}
    	cursor.close();


     	if (activated == 1){
return true;
     	}
     	
		return false;
	}*/

	public long getRequestId() {
		synchronized (DbHelper.class) {
			ContentValues values = new ContentValues();
			values.put(C_REQUEST_ID_VALUE, "1");
			return database.insert(DbHelper.TABLE_REQUEST_ID, null, values);
		}
		}
	private ContentValues createContentValues(String imsi, String profileXML) {
		ContentValues values = new ContentValues();
		values.put(C_IMSI, imsi);
		values.put(C_PROFILE_XML, profileXML);
		return values;
	}
	private ContentValues createContentValues(String imsi, String serviceXML,
			long timestamp) {
		ContentValues values = new ContentValues();
		values.put(C_IMSI, imsi);
		values.put(C_SERVICE_XML, serviceXML);
		values.put(C_TIMESTAMP, String.valueOf(timestamp));
		return values;
	}
	
	public void finalize() throws Throwable {
	   close();
	   LipukaApplication lipukaApplication = (LipukaApplication)context;
	   lipukaApplication.stopDatabseWriteThread();
	    super.finalize();
	}
}