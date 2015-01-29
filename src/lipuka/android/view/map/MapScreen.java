package lipuka.android.view.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public interface MapScreen {
    public void drawPath(final GeoPoint src, final GeoPoint dest, MapView mapView);
    public void goToCurrentPoint();
    public GeoPoint getCurrentPoint();
}
