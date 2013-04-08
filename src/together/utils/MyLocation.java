package together.utils;

import android.location.Location;
import android.location.LocationManager;

public class MyLocation {
	
	private static LocationManager locationManager;
	
	public MyLocation(LocationManager locationManager) {
		MyLocation.locationManager = locationManager;
	}
	
	public Location getLoaction() {
		String provider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER).getName();
		Location location = locationManager.getLastKnownLocation(provider);
		return location;
	}
}
