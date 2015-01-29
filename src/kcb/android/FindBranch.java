package kcb.android;

import greendroid.app.GDMapActivity;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kcb.android.LipukaApplication;


import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;



import kcb.android.R;
import lipuka.android.model.map.KMLHandler;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.LipukaMapOverlay;
import lipuka.android.view.adapter.ComboBoxAdapter;
import lipuka.android.view.map.GoogleParser;
import lipuka.android.view.map.LipukaItemizedOverlay;
import lipuka.android.view.map.MapScreen;
import lipuka.android.view.map.Parser;
import lipuka.android.view.map.Route;
import lipuka.android.view.map.RouteOverlay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.maps.TapControlledMapView;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;


public class FindBranch extends MapActivity implements LocationListener, com.readystatesoftware.maps.OnSingleTapListener, MapScreen, OnClickListener{

	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 0; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 120000; // in Milliseconds
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	LipukaApplication lipukaApplication;

	LocationManager locationManager;
	MapController mapController;
	GeoPoint currentPoint;
	private Location previousLocation;
	RouteOverlay routeOverlay;

	List<Overlay> mapOverlays;
	LipukaItemizedOverlay locationsItemizedOverlay, atmsItemizedOverlay, 
	currentPositionItemizedOverlay, hitItemizedOverlay;
	TapControlledMapView mapView;
	List<JSONObject> banks;
int selectedPosition;
JSONArray overlaysArray, nearMeOverlays, lariAtmOverlays, otherAtmsOverlay;
boolean firstTimeSelection = true;
LipukaListItem[] itemsArray;
boolean startedFirstTime = true;
Route route;
String[] autocompleteItemsArray;
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 try{setContentView(R.layout.find_branch);
 lipukaApplication = (LipukaApplication)getApplication();
	
        mapView = (TapControlledMapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    
	    mapOverlays = mapView.getOverlays();
	    
	    
	    mapView.setOnSingleTapListener(this);
	  /*  double latitudeFrom = -1.290098;
        double longitudeFrom = 36.820679;
        double latitudeTo = -1.261952;
        double longitudeTo = 36.758881;
        
        GeoPoint srcGeoPoint = 
        	new GeoPoint((int)(latitudeFrom * 1E6), (int)(longitudeFrom * 1E6));
        GeoPoint destGeoPoint = 
        	new GeoPoint((int)(latitudeTo * 1E6), (int)(longitudeTo * 1E6));
        */
        

       //drawPath(srcGeoPoint, destGeoPoint, mapView);

      //  mapView.getController().setZoom(15);
        
        mapController = mapView.getController();      
        	        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        	        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
        	    			MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, this);
        	    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
        	    			MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, this);        
	
        	    	currentPoint = getCurrentLocation();
        	    	
        	    	Drawable drawable = this.getResources().getDrawable(R.drawable.map);
        	    	currentPositionItemizedOverlay = new LipukaItemizedOverlay(drawable, mapView);
        		        OverlayItem overlayitem = new OverlayItem(currentPoint, "You are here", "");
        		    
        		        currentPositionItemizedOverlay.addOverlay(overlayitem);
        		    mapOverlays.add(currentPositionItemizedOverlay);
        		    
        	    		mapController.animateTo(currentPoint);
        	        mapController.setZoom(15); 
    
     AutoCompleteTextView locationField = (AutoCompleteTextView) findViewById(R.id.location_search_field);
        	        	
        	        locationField.setOnItemClickListener(new AdapterView.OnItemClickListener() { 

        	            @Override
        	            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        	                    long arg3) {

        	          		try{   
        	          			JSONObject hit = searchLocation();

        	          	int latitude = (int) (hit.getDouble("latitude") * 1E6);
        	            int longitude = (int) (hit.getDouble("longitude") * 1E6);
        	                	   GeoPoint point = new GeoPoint(latitude, longitude);
        	               	    OverlayItem overlayitem = new OverlayItem(point, hit.getString("bank_name")+", "+
        	               	    		hit.getString("street_address")+", "+hit.getString("city"), 
        	               	    		"Tap to view route");
        	        	        mapController.animateTo(point); 	
        	        	        if(!mapOverlays.isEmpty()){
        	        				for(int i = 0; i < mapOverlays.size(); i++){
        	        					mapOverlays.remove(i);
        	        				}
        	        				}
        	        	    	Drawable drawable = FindBranch.this.getResources().getDrawable(R.drawable.map_red);
        	        	    	hitItemizedOverlay = new LipukaItemizedOverlay(drawable, mapView);        	        			    
        	        	    	hitItemizedOverlay.addOverlay(overlayitem);
    	        			    mapOverlays.add(locationsItemizedOverlay);
    	        			    mapOverlays.add(currentPositionItemizedOverlay);
    mapOverlays.add(hitItemizedOverlay);
        	        				mapView.invalidate();
        	        	}catch(Exception ex){
        	            	Log.d(Main.TAG, "setting location error", ex);

        	        	}
        	        	
        	            }
        	            });
        	        
        	        Button helpButton = (Button)findViewById(R.id.help);
        		    helpButton.setOnClickListener(this);
        		    Button homeButton = (Button)findViewById(R.id.home_button);
        		    homeButton.setOnClickListener(this);
        		    Button signOutButton = (Button)findViewById(R.id.sign_out);
        		    signOutButton.setOnClickListener(this);
        		    
        	        lipukaApplication.setCurrentActivity(this);
 }  catch(Exception e){
		Log.d(Main.TAG, "creating find branch error", e);

}
    }
  
    @Override
    protected void onStart() {
        super.onStart();
		lipukaApplication.setCurrentActivity(this);
		lipukaApplication.setActivityState(FindBranch.class, true);
		if(startedFirstTime){
        	lipukaApplication.fetchBranchLocationsLocally(FindBranch.this);
        	startedFirstTime = false;
        	}
		  Button signOutButton = (Button)findViewById(R.id.sign_out);

			if(lipukaApplication.getProfileID() == 0){
				 signOutButton.setVisibility(View.GONE);
				    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
				    dividerTwo.setVisibility(View.GONE);
				}else{
					 signOutButton.setVisibility(View.VISIBLE);
					    LinearLayout dividerTwo = (LinearLayout) findViewById(R.id.divider_two);
					    dividerTwo.setVisibility(View.VISIBLE);			
				}
		}
    
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
				MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, this);
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
    			MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, this);
    	
	}
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}
	@Override
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(this);
		lipukaApplication.setActivityState(FindBranch.class, false);
	}
	
    public void drawPath(final GeoPoint src, final GeoPoint dest, MapView mapView) {
    	Log.d(Main.TAG, "dest location: "+dest.getLatitudeE6()+", "+dest.getLongitudeE6());
    	Log.d(Main.TAG, "currentPoint location: "+currentPoint.getLatitudeE6()+", "+currentPoint.getLongitudeE6());
	if(dest.getLatitudeE6() == currentPoint.getLatitudeE6() && 
    			dest.getLongitudeE6() == currentPoint.getLongitudeE6()){
    		return;
    	}
	
	Location location1 = new Location("");
    location1.setLatitude(src.getLatitudeE6()/1.0E6);
    location1.setLongitude(src.getLongitudeE6()/1.0E6);

    Location location2 = new Location("");
    location2.setLatitude(dest.getLatitudeE6()/1.0E6);
    location2.setLongitude(dest.getLongitudeE6()/1.0E6);

    float distanceInMeters = location1.distanceTo(location2);
    if(distanceInMeters > 1000000.0f){
    	Toast.makeText(FindBranch.this, "Sorry, the distance from where you are to the destination is too large. Please try another location.",
    			Toast.LENGTH_LONG).show();  
    	return;
    }
    
    	Log.d(Main.TAG, "src location: "+src.getLatitudeE6()+", "+src.getLongitudeE6());
   
	new Thread() {
			 
            @Override

            public void run() {

                    double fromLat = -1.290098, fromLon = 36.820679, 
                    toLat = -1.302111, toLon = 36.828060;

  route = directions(new GeoPoint(src.getLatitudeE6(),src.getLongitudeE6()),
		 new GeoPoint(dest.getLatitudeE6(),dest.getLongitudeE6()));

if(route != null){
                    mHandler.sendEmptyMessage(1);
}else{
    mHandler.sendEmptyMessage(0);	
}
            }

    }.start();
	   lipukaApplication.showProgress("Sending request");


    	/*try {
    		//Parse KML
			URL url = new URL(strUrl.toString());
			
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			SAXParser parser = saxFactory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			
			KMLHandler kmlHandler = new KMLHandler();
			reader.setContentHandler(kmlHandler);
			
			InputSource inputSource = new InputSource(url.openStream());
			reader.parse(inputSource);

			String path = kmlHandler.getPathCoordinates();
			//Draw path
			if(path != null) {
				RouteOverlay routeOverlay = new RouteOverlay();
				
				String pairs[] = path.split(" ");
				
				for (String pair : pairs) {
					String coordinates[] = pair.split(",");
					GeoPoint geoPoint = new GeoPoint(
							(int) (Double.parseDouble(coordinates[1]) * 1E6),
							(int) (Double.parseDouble(coordinates[0]) * 1E6));
					routeOverlay.addGeoPoint(geoPoint);
				}
				
				mapView.getOverlays().add(routeOverlay);
				mapView.invalidate();
				routeOverlays.add(routeOverlay);

			}
		} catch (Exception e) {
			Log.w(Main.TAG, "RoutePath exception", e);
            Toast.makeText(FindAtmBranch.this, "Oops! Route could not be determined", Toast.LENGTH_LONG).show();

		}*/
    }
    
    public GeoPoint getCurrentLocation(){
    	
    	
    	previousLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	GeoPoint currentPoint = new GeoPoint(24500145,54374139);
	    if(previousLocation != null){    
currentPoint = new GeoPoint((int) (previousLocation.getLatitude() * 1E6), 
(int) (previousLocation.getLongitude() * 1E6));

}else{
	previousLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    if(previousLocation != null){    
currentPoint = new GeoPoint((int) (previousLocation.getLatitude() * 1E6), 
			(int) (previousLocation.getLongitude() * 1E6));
    }
}
        //mapController.animateTo(currentPoint);
	    return currentPoint;
    }
    public GeoPoint getCurrentPoint(){
        //mapController.animateTo(currentPoint);
	    return currentPoint;
    }
    public void goToCurrentPoint(){
	        mapController.animateTo(currentPoint); 	
    }
	public void onLocationChanged(Location location) {
		if(isBetterLocation(location, previousLocation)){
			previousLocation =	location;
			Log.i(Main.TAG, String.valueOf(location.getLatitude()));
			Log.i(Main.TAG, String.valueOf(location.getLongitude()));
			
			currentPoint = new GeoPoint((int) (location.getLatitude() * 1E6), 
					(int) (location.getLongitude() * 1E6));
	       // mapController.animateTo(currentPoint);
	        	    	
			currentPositionItemizedOverlay.hideAllBalloons();
mapOverlays.remove(currentPositionItemizedOverlay);
    	Drawable drawable = this.getResources().getDrawable(R.drawable.map);
	    	currentPositionItemizedOverlay = new LipukaItemizedOverlay(drawable, mapView);
		        OverlayItem overlayitem = new OverlayItem(currentPoint, "You are here", "");
		    
		        currentPositionItemizedOverlay.addOverlay(overlayitem);
		    mapOverlays.add(currentPositionItemizedOverlay);
			mapView.invalidate();

			Log.i(Main.TAG, "location changed");	

		}else{
			Log.i(Main.TAG, "new location not better");	
		}		
			}
	
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null ) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}

	@Override
	public boolean onSingleTap(MotionEvent e) {

	try{	if(locationsItemizedOverlay != null){
		locationsItemizedOverlay.hideAllBalloons();
		}
		if(atmsItemizedOverlay != null){
			atmsItemizedOverlay.hideAllBalloons();
			}
		currentPositionItemizedOverlay.hideAllBalloons();
		
		mapOverlays.remove(routeOverlay);
		if(hitItemizedOverlay != null){
			hitItemizedOverlay.hideAllBalloons();
			//mapOverlays.remove(hitItemizedOverlay);
			}

		mapView.invalidate();
	}catch(Exception ex){
	     Log.d(Main.TAG, "Error: ", ex);
	
	}
		return true;
	}
	
	public void addOverlays(JSONArray overlaysArray) {
		this.nearMeOverlays = overlaysArray;

		if(!mapOverlays.isEmpty()){
			for(int i = 0; i < mapOverlays.size(); i++){
				mapOverlays.remove(i);
			}
			}
		mapOverlays.remove(locationsItemizedOverlay);
		
		int total = overlaysArray.length();
		JSONObject currentOverlayItem;
		Drawable locationImg = this.getResources().getDrawable(R.drawable.atm);
		locationsItemizedOverlay = new LipukaItemizedOverlay(locationImg, mapView);
	    
	    	    
      try{

    	  for (int i = 0; i < total ; i++){
        	   currentOverlayItem = overlaysArray.getJSONObject(i);
        	int latitude = (int) (currentOverlayItem.getDouble("latitude") * 1E6);
        	int longitude = (int) (currentOverlayItem.getDouble("longitude") * 1E6);
        	   GeoPoint point = new GeoPoint(latitude, longitude);
       	    OverlayItem overlayitem = new OverlayItem(point, currentOverlayItem.getString("bank_name")+", "+
       	    		currentOverlayItem.getString("street_address")+", "+currentOverlayItem.getString("city"), 
       	    		"Tap to view route");
       	 locationsItemizedOverlay.addOverlay(overlayitem);  	
           }
	}
    catch (org.json.JSONException jsonError) {
	     Log.d(Main.TAG, "jsonError: ", jsonError);
      } 
    if(locationsItemizedOverlay.size() > 0){
    mapOverlays.add(locationsItemizedOverlay);
    }
	
	mapOverlays.remove(currentPositionItemizedOverlay);
Drawable drawable = this.getResources().getDrawable(R.drawable.map);
	currentPositionItemizedOverlay = new LipukaItemizedOverlay(drawable, mapView);
        OverlayItem overlayitem = new OverlayItem(currentPoint, "You are here", "");
    
        try { currentPositionItemizedOverlay.addOverlay(overlayitem);
        mapOverlays.add(currentPositionItemizedOverlay);
          }catch(Exception ex){
        	  Log.d(Main.TAG, "Add current position error", ex);
          }
	mapView.invalidate();
	setAutoCompleteData(overlaysArray);
	}
	
	protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
        case Main.DIALOG_MSG_ID:
        	CustomDialog cd = new CustomDialog(this);
        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
        	dialog = cd;
        	break;
        case Main.DIALOG_ERROR_ID:
        	cd = new CustomDialog(this);
        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
        	dialog = cd;
        	break;
        case Main.DIALOG_PROGRESS_ID:
        	//builder = new AlertDialog.Builder(this);

        	CustomProgressDialog pd = new CustomProgressDialog(this);
        	dialog = pd;

        	break;
        default:
            dialog = null;
        }
        return dialog;
    }
    protected  void onPrepareDialog(int id, Dialog dialog){
    	//AlertDialog ad = (AlertDialog) dialog;
    	switch(id) {
        case Main.DIALOG_MSG_ID:
        	CustomDialog cd = (CustomDialog)dialog;
        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
        	break;
        case Main.DIALOG_ERROR_ID:
          	cd = (CustomDialog)dialog;
        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
        	break;
        case Main.DIALOG_PROGRESS_ID:
        	CustomProgressDialog pd = (CustomProgressDialog)dialog;
ProgressBar pb = (ProgressBar)pd.findViewById(R.id.progressbar_default);
pb.setVisibility(View.GONE);
pb.setVisibility(View.VISIBLE);
        	break;
        default:
            dialog = null;
        }
    }

	Handler mHandler = new Handler() {
		  public void handleMessage(android.os.Message msg) {
			  if (FindBranch.this == lipukaApplication.getCurrentActivity()){
		 		 	lipukaApplication.dismissProgressDialog();
		         }
 if (FindBranch.this == null || !lipukaApplication.isActivityVisible(FindBranch.this.getClass())){
	return;
	            }
			  if(msg.what == 1){		
if(routeOverlay != null){		
	mapOverlays.remove(routeOverlay);
}
			routeOverlay = new RouteOverlay(route, Color.BLUE);
			  mapView.getOverlays().add(routeOverlay);
			  mapView.invalidate();
		  }else{
Toast.makeText(FindBranch.this, "Sorry, route could not be determined. Please check your Internet connection.",
		Toast.LENGTH_LONG).show();  
		  }
		  };
		 };
		 private Route directions(final GeoPoint start, final GeoPoint dest) {
			    Parser parser;
			    //https://developers.google.com/maps/documentation/directions/#JSON <- get api
			    String jsonURL = "http://maps.googleapis.com/maps/api/directions/json?";
			    final StringBuffer sBuf = new StringBuffer(jsonURL);
			    sBuf.append("origin=");
			    sBuf.append(start.getLatitudeE6()/1E6);
			    sBuf.append(',');
			    sBuf.append(start.getLongitudeE6()/1E6);
			    sBuf.append("&destination=");
			    sBuf.append(dest.getLatitudeE6()/1E6);
			    sBuf.append(',');
			    sBuf.append(dest.getLongitudeE6()/1E6);
			    sBuf.append("&sensor=true&mode=driving");
			    parser = new GoogleParser(sBuf.toString());
			    Route r =  null;
			    try{
			    r = parser.parse();
			    }catch(Exception e){
					Log.d(Main.TAG, "getting directions error", e);  	
			    }
			    return r;
			}
	    	public void setAutoCompleteData(JSONArray data){
	    		try{   
		    	 autocompleteItemsArray = new String[data.length()];
		      JSONObject overlay;
		        for(int i = 0; i < data.length(); i++){
		        	overlay = data.getJSONObject(i);
		        	autocompleteItemsArray[i] = overlay.getString("bank_name")+", "+
		        	overlay.getString("street_address")+
       	    		", "+overlay.getString("city");   	
		        }

AutoCompleteTextView locationsView = (AutoCompleteTextView) findViewById(R.id.location_search_field);
	
ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autocompleteItemsArray);

locationsView.setAdapter(adapter);
	    	}catch(Exception ex){
		    	Log.d(Main.TAG, "setting locations error", ex);
	
	    	}
	    	}
	    	
	 		public void onClick(View arg0) {
		    	 if(arg0.getId() ==  R.id.help){
						//help.setVisibility(View.VISIBLE);
				       // help.startAnimation(LipukaAnim.inFromRightAnimation());
		    		}else if(arg0.getId() ==  R.id.home_button){
					 Intent i = new Intent(this, StanChartHome.class);
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
		    		}/*else if (closeHelp == arg0){
		    			help.startAnimation(LipukaAnim.outToRightAnimation());
		    	    	help.setVisibility(View.GONE);
		    	    	}*/else if (R.id.sign_out == arg0.getId()){
		    	    		lipukaApplication.setProfileID(0);
		    	    		Intent i = new Intent(this, StanChartHome.class);
		    	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    	    		startActivity(i);
			    	    	}
		    			}
	   		private JSONObject searchLocation(){
    			AutoCompleteTextView locationsView = (AutoCompleteTextView) findViewById(R.id.location_search_field);
		 String param = locationsView.getText().toString();
		 int i = 0;
			JSONObject hit = null;
try{
for(String location: autocompleteItemsArray){
    	if(location.equals(param)){
				hit = nearMeOverlays.getJSONObject(i);		
    	}			
    	i++;
    	}
    		}catch(Exception ex){
		    	Log.d(Main.TAG, "searching locations error", ex);
	    	}
    return hit; 			
    		}
}
