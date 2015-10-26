package com.lipi.android.bazakonstrukcijskihsklopov;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lipi.android.bazakonstrukcijskihsklopov.baza.Constants;
import com.lipi.android.bazakonstrukcijskihsklopov.baza.MainActivity;
import com.lipi.android.bazakonstrukcijskihsklopov.baza.MyDB;
 

public class Locator extends android.support.v4.app.FragmentActivity implements OnInfoWindowClickListener,OnMapClickListener{
 
	public static Context mContext2;
	private List<Address> addressesIskani;
	private List<Address> addressesIsk;
	private double geolat;
	private double geolon;
	private Address addressIskani;
	public TextView tv;
	private Button   mButton;
	private EditText mEdit;
	private LocationManager mLocationManager;
	private GoogleMap mMap;
	private String mSBzelena;
	private String mSAzelena;
	private String mSBmodra;
	private String mSAmodra;
    private static final String KEY_BOTH = "use_both";
    private boolean mUseBoth;
    private Handler mHandler;
    private boolean mGeocoderAvailable;
    private MyDB dba;
    private static final int UPDATE_ADDRESS = 1;
    private static final int UPDATE_LATLNG = 2;
    private static final int TRENUTNI_ADDRESS = 3;
    
    private static final int TEN_SECONDS = 10000;
    private static final int TEN_METERS = 10;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.lokacija);
		//za overlay glede pozicije (poš.številka itd)
		this.deleteDatabase("datastorage");
		mContext2 = this;	
		tv = (TextView) findViewById(R.id.tv1);
	    mButton = (Button)findViewById(R.id.button1);
	    mEdit   = (EditText)findViewById(R.id.editText1);
	    mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1))
                .getMap();
	    mMap.setMyLocationEnabled(true);
	    mMap.setOnMapClickListener(this);
	    mMap.setOnInfoWindowClickListener(this);
	    
        if (savedInstanceState != null) {
            mUseBoth = savedInstanceState.getBoolean(KEY_BOTH);
        } else {
            mUseBoth = false;
        }
        
        // The isPresent() helper method is only available on Gingerbread or above.
        mGeocoderAvailable =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Geocoder.isPresent();
        // Handler for updating text fields on the UI like the lat/long and address.
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_ADDRESS:
                        tv.setText((String) msg.obj);
                        break;
                    case UPDATE_LATLNG:
                        tv.setText((String) msg.obj);
                        break;
                    case TRENUTNI_ADDRESS:
                    	tv.setText((String) msg.obj);
                        break;
                }
            }
        };

		
		
	    
	    mEdit.setOnKeyListener(new OnKeyListener() {
	        public boolean onKey(View v, int keyCode, KeyEvent event) {
	            // If the event is a key-down event on the "enter" button
	            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
	                (keyCode == KeyEvent.KEYCODE_ENTER)) {
	              // Perform action on key press
	            	getWantedLocation();
	              return true;
	            }
	            return false;
	        }
	    });
	    
	    mButton.setOnClickListener(
	            new View.OnClickListener()
	            {
	                public void onClick(View view)
	                {
	                	getWantedLocation();
	                }
	            });
	    mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	}

	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_BOTH, mUseBoth);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        setup();
    }
	@Override
    protected void onPause() {
        super.onPause();
        this.deleteDatabase("datastorage");
    }
    
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1))
                    .getMap();
            
            
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();

        // Check if the GPS setting and DATA CONECTIVITYis currently enabled on the device.
        // This verification should be done during onStart() because the system calls this method
        // when the user returns to the activity, which ensures the desired location provider is
        // enabled each time the activity resumes from the stopped state.
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        
        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            // call enableLocationSettings()
        	vklopiGPSinPODATKE();
        	
        }
        
        ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // && = "and"
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED && connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED){

        	vklopiGPSinPODATKE();
        	Toast.makeText(this, R.string.not_support_network, Toast.LENGTH_LONG).show();
        }	
        
}

    // Stop receiving location updates whenever the Activity becomes invisible.
    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(listener);
    }

    // Set up fine and/or coarse location providers depending on whether the fine provider or
    // both providers button is pressed.
    private void setup() {
        Location gpsLocation = null;
        Location networkLocation = null;
        mLocationManager.removeUpdates(listener);
        tv.setText(R.string.unknown);

        // Get fine location updates only.
        

            // Get coarse and fine location updates.
            // Request updates from both fine (gps) and coarse (network) providers.
            gpsLocation = requestUpdatesFromProvider(
                    LocationManager.GPS_PROVIDER, R.string.not_support_gps);

            networkLocation = requestUpdatesFromProvider(
                    LocationManager.NETWORK_PROVIDER, R.string.not_support_network);

            //Log.d(String.valueOf(getBetterLocation(gpsLocation, networkLocation)), "bla");
            
            // If both providers return last known locations, compare the two and use the better
            // one to update the UI.  If only one provider returns a location, use it.
            if (gpsLocation != null && networkLocation != null) {
                updateUILocation(getBetterLocation(gpsLocation, networkLocation));
                
                
            } else if (gpsLocation != null) {
                updateUILocation(gpsLocation);
                
            } else if (networkLocation != null) {
                updateUILocation(networkLocation);
                
                
            }
    
            
    }
    /**
     * Method to register location updates with a desired location provider.  If the requested
     * provider is not available on the device, the app displays a Toast with a message referenced
     * by a resource id.
     *
     * @param provider Name of the requested provider.
     * @param errorResId Resource id for the string message to be displayed if the provider does
     *                   not exist on the device.
     * @return A previously returned {@link android.location.Location} from the requested provider,
     *         if exists.
     */
    private Location requestUpdatesFromProvider(final String provider, final int errorResId) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, TEN_SECONDS, TEN_METERS, listener);
            location = mLocationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
        }
        return location;
    }

    public void useCoarseFineProviders(View v) {
        mUseBoth = true;
        setup();
    }
    
    private void doReverseGeocoding(Location location) {
        // Since the geocoding API is synchronous and may take a while.  You don't want to lock
        // up the UI thread.  Invoking reverse geocoding in an AsyncTask.
        (new ReverseGeocodingTask(this)).execute(new Location[] {location});
    }

    private void updateUILocation(Location location) {
        // We're sending the update to a handler which then updates the UI with the new
        // location.
    	
        Message.obtain(mHandler,
                UPDATE_LATLNG,
                location.getLatitude() + ", " + location.getLongitude()).sendToTarget();
                final CameraPosition moje =
                new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(18.5f)
                        .tilt(50)
                        .build();
    	mMap.animateCamera(CameraUpdateFactory.newCameraPosition(moje));

        // Bypass reverse-geocoding only if the Geocoder service is available on the device.
        if (mGeocoderAvailable) doReverseGeocoding(location);
    }

    private final LocationListener listener = new LocationListener() {

        public void onLocationChanged(Location location) {
            // A new location update is received.  Do something useful with it.  Update the UI with
            // the location update.
            updateUILocation(location);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    /** Determines whether one Location reading is better than the current Location fix.
      * Code taken from
      * http://developer.android.com/guide/topics/location/obtaining-user-location.html
      *
      * @param newLocation  The new Location that you want to evaluate
      * @param currentBestLocation  The current Location fix, to which you want to compare the new
      *        one
      * @return The better Location object based on recency and accuracy.
      */
    protected Location getBetterLocation(Location newLocation, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return newLocation;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved.
        if (isSignificantlyNewer) {
            return newLocation;
        // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return currentBestLocation;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return newLocation;
        } else if (isNewer && !isLessAccurate) {
            return newLocation;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return newLocation;
        }
        return currentBestLocation;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
          return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    // AsyncTask encapsulating the reverse-geocoding API.  Since the geocoder API is blocked,
    // we do not want to invoke it from the UI thread.
    private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {
        Context mContext;
        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected Void doInBackground(Location... params) {
        	
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            Location loc = params[0];
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                
            } catch (IOException e) {
                e.printStackTrace();
                // Update address field with the exception.
                Message.obtain(mHandler, UPDATE_ADDRESS, e.toString()).sendToTarget();
            }
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                // Format the first line of address (if available), city, and country name.
                String addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName());
                // Update address field on UI.
                Message.obtain(mHandler, UPDATE_ADDRESS, "Moja trenutna lokacija: " + addressText).sendToTarget();
                
                
            }
            return null;
        }
        
    }
    
		private void getWantedLocation() {
		//mapOverlays = mapView.getOverlays();
		//Drawable drawable = this.getResources().getDrawable(R.drawable.house4);
		//markerlayer = new MyMarkerLayer(drawable);
        String kraj=mEdit.getText().toString();
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        try {
		addressesIskani = gc.getFromLocationName(kraj + "Slovenia", 1);
		if(addressesIskani != null) {
		addressIskani = addressesIskani.get(0);
		geolat = addressIskani.getLatitude();
		geolon = addressIskani.getLongitude();
		mSBmodra = new String(addressesIskani.get(0).getAddressLine(0));
		mSAmodra = new String(addressesIskani.get(0).getAddressLine(1));
		}
		} catch(IOException e) {
			Toast.makeText(this, "Željenega naslova ni mogoèe dobiti!", Toast.LENGTH_SHORT).show();
		}		
		Marker modri = mMap.addMarker(new MarkerOptions().position(new LatLng(geolat,geolon))
				.title(mSBmodra)
				.snippet(mSAmodra)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.house4)));
		modri.showInfoWindow();
		Toast.makeText(this, "Dotaknite se oblaèka za urejanje lokacije", Toast.LENGTH_SHORT).show();
		
		//point = new GeoPoint((int) geolat,(int) geolon);
		//mc.animateTo(point);
		//mc.setZoom(17);
		//Address wantedAddr = addresses.get(0);
		//mSB = new String(addressIskani.getAddressLine(0));
		//mSA = new String(addressIskani.getAddressLine(1));
		//OverlayItem overlayitem = new OverlayItem(point, mSB, mSA);
		//markerlayer.addOverlayItem(overlayitem);
		//mapOverlays.add(markerlayer);
		final CameraPosition iskano =
                new CameraPosition.Builder().target(new LatLng(geolat, geolon))
                        .zoom(18.5f)
                        .tilt(50)
                        .build();
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(iskano));
		
		
	}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.layout.menu_inflater, menu);
		return true;
		}
		
	    @Override
	    public boolean onMenuItemSelected(int featureId, MenuItem item) {
	        int itemId = item.getItemId();
			if (itemId == R.id.menu_insert) {
				setup();
				pocistiMapo();
				return true;
			}
	       //????
	        return super.onMenuItemSelected(featureId, item);
	    }
	    
	protected boolean isRouteDisplayed() {
		return false;
	}
	
		
	
    /**
     * Dialog to prompt users to enable GPS on the device.
     */
    private void vklopiGPSinPODATKE() {
    	
    	AlertDialog.Builder dialog =
    				new AlertDialog.Builder(this);
    	dialog.setTitle("Zaznava lokacije");
		dialog.setMessage("Potreben je vklop GPS in podatkovne povezave");
		dialog.setPositiveButton("Vklopi GPS",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Toast.makeText(mContext2, "Vklopite ÿUporaba satelitov GPSÿ", Toast.LENGTH_LONG).show();
	            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            mContext2.startActivity(myIntent);
			}
		});

	dialog.setNeutralButton("Vklopi podatke",
			new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Toast.makeText(mContext2, "Vklopite ÿPodatki omogoèeniÿ", Toast.LENGTH_LONG).show();
	            Intent myoIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
	            ComponentName cName = new ComponentName("com.android.phone","com.android.phone.Settings");
	            myoIntent.setComponent(cName);
	            mContext2.startActivity(myoIntent);
			}
		});
		dialog.setNegativeButton("Preklièi",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		dialog.show();
    }



	public void onInfoWindowClick(final Marker marker) {
		// TODO Auto-generated method stub
		AlertDialog.Builder dialog =
				new AlertDialog.Builder(this);
		dialog.setTitle("Je željena lokacija pravilna?");
		dialog.setMessage(marker.getTitle() + ", " + marker.getSnippet());
		dialog.setPositiveButton("Da",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String Naselje = marker.getSnippet();
	            String Ulica = marker.getTitle();
	            String NaseljePostna = Naselje.substring(0,Naselje.indexOf(" "));
	            String NaseljeBesedilo = Naselje.substring(Naselje.indexOf(" ")+1,Naselje.length());
	            String stevilka = Ulica.substring(Ulica.trim().lastIndexOf(" ")+1,Ulica.trim().length());
	            String besedilo = Ulica.substring(0,Ulica.lastIndexOf(" ")+1);
	            Intent myIntent = new Intent(mContext2, MainActivity.class);
	            myIntent.putExtra(Constants.STAVBA_POSTNA, NaseljePostna);
	            myIntent.putExtra(Constants.STAVBA_NASELJE, NaseljeBesedilo);
	            myIntent.putExtra(Constants.STAVBA_ULICA, besedilo);
	            myIntent.putExtra(Constants.STAVBA_HISNA, stevilka);
	            mContext2.startActivity(myIntent);
	            
			}
		});
		dialog.setNegativeButton("Ne",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		dialog.show();
    }


	public void onMapClick(LatLng trenutni) {
		// TODO Auto-generated method stub
		Geocoder geocoderIsk = new Geocoder(this, Locale.getDefault());
		try {
		addressesIsk = geocoderIsk.getFromLocation(trenutni.latitude, trenutni.longitude, 1);
		
        } catch (IOException e) {
            e.printStackTrace();
            // Update address field with the exception.
            Message.obtain(mHandler, UPDATE_ADDRESS, e.toString()).sendToTarget();
        }
        if (addressesIsk != null && addressesIsk.size() > 0) {
            mSBzelena = new String(addressesIsk.get(0).getAddressLine(0));
    		mSAzelena = new String(addressesIsk.get(0).getAddressLine(1));		
        }	
		Marker zeleni = mMap.addMarker(new MarkerOptions().position(trenutni)
				.title(mSBzelena)
				.snippet(mSAzelena)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.house7)));
		zeleni.showInfoWindow();
		Toast.makeText(this, "Dotaknite se oblaèka za urejanje lokacije", Toast.LENGTH_LONG).show();
	}
	
	private void pocistiMapo(){
		mMap.clear();
	}
	
	}
