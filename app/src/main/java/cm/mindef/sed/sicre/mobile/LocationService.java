package cm.mindef.sed.sicre.mobile;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import cm.mindef.sed.sicre.mobile.utils.Constant;

public class LocationService extends Service implements LocationListener{

    private final IBinder locationBinder = new LocationBinder();

    public LocationService() {
    }

    @Override
    public void onCreate() {
        //create the service
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        Log.e("Location Location", Constant.latitudeNetwork + ", " + Constant.longitudeNetwork);
        return locationBinder;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("Location Location", location.getLatitude() + ", " + location.getLongitude());
        Constant.latitudeNetwork = location.getLatitude();
        Constant.longitudeNetwork = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class LocationBinder extends Binder {
        public LocationService getService() {
            return LocationService.this;
        }
    }
}
