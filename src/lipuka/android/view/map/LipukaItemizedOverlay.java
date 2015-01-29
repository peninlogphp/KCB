package lipuka.android.view.map;


import java.util.ArrayList;

import kcb.android.FindAtm;





import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;


public class LipukaItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private MapScreen c;
	MapView mapView;
	public LipukaItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		c = (MapScreen)mapView.getContext();
		this.mapView = mapView;
	}

	public void addOverlay(OverlayItem overlay) {
	    m_overlays.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		  double latitudeTo = -1.261952;
	        double longitudeTo = 36.758881;
	        
	        GeoPoint destGeoPoint = 
	        	new GeoPoint((int)(latitudeTo * 1E6), (int)(longitudeTo * 1E6));

		c.drawPath(c.getCurrentPoint(), item.getPoint(), mapView);
		c.goToCurrentPoint();
		//Toast.makeText(c, "onBalloonTap for overlay index " + index,
			//	Toast.LENGTH_LONG).show();
		return true;
	}
	
}
