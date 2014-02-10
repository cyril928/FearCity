package tw.edu.ntu.csie.srlab.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.HttpConnect;

import tw.edu.ntu.csie.srlab.aidl.ControlLocationService;


import com.skyhookwireless.wps.IPLocation;
import com.skyhookwireless.wps.IPLocationCallback;
import com.skyhookwireless.wps.WPSAuthentication;
import com.skyhookwireless.wps.WPSContinuation;
import com.skyhookwireless.wps.WPSLocation;
import com.skyhookwireless.wps.WPSLocationCallback;
import com.skyhookwireless.wps.WPSPeriodicLocationCallback;
import com.skyhookwireless.wps.WPSReturnCode;
import com.skyhookwireless.wps.WPSStreetAddressLookup;
import com.skyhookwireless.wps.XPS;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

public class SkyhookLocationService extends Service {
	
	// debugging 
	private static final String TAG = "SkyhookLocationService"; 
    private static final boolean D = true;
	
	private static final String SERVER_URL_KEY = "Server URL";

	private static final String _username = "Lee"; 
	private static final String _realm = "csie.ntu.edu.tw";
	private String _localFilePath = null;
	private static final long _period = 60000;
	private static final int _iterations = 0;
	private int _desiredXpsAccuracy = XPS.EXACT_ACCURACY;
	private String _tilingPath = null;
	private long _maxDataSizePerSession = 0;
	private long _maxDataSizeTotal = 0;
	private String _serverUrl = "SERVER_URL_KEY";
	private WPSStreetAddressLookup _streetAddressLookup = WPSStreetAddressLookup.WPS_FULL_STREET_ADDRESS_LOOKUP;
	private XPS _xps;
	private WPSAuthentication auth;
	private MyLocationCallback _callback;
	private TextView _tv = null;
	private Handler _handler;
	
	//Broadcast intent action
	public static final String BroadcastAction = "tw.edu.ntu.csie.srlab.lee.map.locationreport";
	
	//Broadcast message type
	public static final String MESSAGE = "MESSAGE";
	public static final int TYPE_DONE = 0;
	public static final int TYPE_ERROR = 1;
	public static final int TYPE_LOCATION = 2;
	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	public static final String LONGITUDE = "LONGITUDE";
	public static final String LATITUDE = "LATITUDE";
	private  String username;
	private boolean isSend_=true;
 
	private final ControlLocationService.Stub mBinder=new ControlLocationService.Stub(){


		@Override
		public void SetSendLocationtoActivity(boolean isSend)
				throws RemoteException {
			 isSend_=isSend;
			
		}

	 
		
	};
    	
	@Override
	public void onCreate() {

		_xps = new XPS(this);
		auth = new WPSAuthentication(_username, _realm);
		_callback = new MyLocationCallback();

		username = ((AppGlobalVariable) (this).getApplication()).getusername();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(D) Log.d(TAG,"Start the service!");
		
		_xps.getXPSLocation(auth,
                // note we convert _period to seconds
                (int) (_period / 1000),
                _desiredXpsAccuracy,
                _callback);
		
		_xps.getPeriodicLocation(auth,
                 _streetAddressLookup,
                 _period,
                 _iterations,
                 _callback);
		 
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	private class MyLocationCallback
    implements IPLocationCallback,
               WPSLocationCallback,
               WPSPeriodicLocationCallback {
		public void done()
		{
			if(D) Log.d(TAG,"WPS is ready for further requests");
			
//			 tell the UI thread to re-enable the buttons
//			_handler.sendMessage(_handler.obtainMessage(DONE_MESSAGE));
			Intent broadcastIntent = new Intent(BroadcastAction);
			broadcastIntent.putExtra(MESSAGE,TYPE_DONE);
			sendBroadcast(broadcastIntent);
		}

		public WPSContinuation handleError(WPSReturnCode error)
		{
			if(D) Log.d(TAG,((WPSReturnCode) error).name());
			
			// send a message to display the error
			//_handler.sendMessage(_handler.obtainMessage(ERROR_MESSAGE,error));
			
			Intent broadcastIntent = new Intent(BroadcastAction);
			broadcastIntent.putExtra(MESSAGE,TYPE_ERROR);			
			String error_message = ((WPSReturnCode) error).name();
			broadcastIntent.putExtra(ERROR_MESSAGE,error_message);			
			sendBroadcast(broadcastIntent);
			
			// return WPS_STOP if the user pressed the Stop button
			//if (! _stop)
			return WPSContinuation.WPS_CONTINUE;
			//else
				//return WPSContinuation.WPS_STOP;
		}

		public void handleIPLocation(IPLocation location)
		{
			if(D) Log.d(TAG,location.toString());
						
			// send a message to display the location
			//_handler.sendMessage(_handler.obtainMessage(LOCATION_MESSAGE,location));
			broadcastgps(location.getLatitude(),location.getLongitude());
			
		}

		public void handleWPSLocation(WPSLocation location)
		{
			if(D) Log.d(TAG,location.toString());
			
			// send a message to display the location
			//_handler.sendMessage(_handler.obtainMessage(LOCATION_MESSAGE,location));
			broadcastgps(location.getLatitude(),location.getLongitude());
		}

		public WPSContinuation handleWPSPeriodicLocation(WPSLocation location)
		{
			if(D) Log.d(TAG,location.toString());
			
			//_handler.sendMessage(_handler.obtainMessage(LOCATION_MESSAGE,location));
			broadcastgps(location.getLatitude(),location.getLongitude());
	
						
			// return WPS_STOP if the user pressed the Stop button
			//if (! _stop)			
			return WPSContinuation.WPS_CONTINUE;
			//else
				//return WPSContinuation.WPS_STOP;
		}
	}
	public void  broadcastgps(Double Latitude,Double Longitude){
		if(isSend_){
			Intent broadcastIntent = new Intent(BroadcastAction);	
			broadcastIntent.putExtra(MESSAGE,TYPE_LOCATION);		
			broadcastIntent.putExtra(LONGITUDE,Longitude);	
			broadcastIntent.putExtra(LATITUDE,Latitude);
			sendBroadcast(broadcastIntent);
			if(D)Log.i("TAG","isSend_1");
	       

		} postGps(Latitude,Longitude,username);

	}

	public void postGps(Double Latitude,Double Longitude,String username){
		

		if(!username.equals("")){
			Map map = new HashMap();
			map.put("username",username);
			map.put("Latitude",Latitude);
			map.put("Longitude",Longitude);
		
				 
			
			JSONObject str = new JSONObject(map);
			if(D)Log.i("TAG",str.toString());
			HttpConnect postgps=new HttpConnect("http://taylor19882002.no-ip.org/FearCity/StoreGps.php",str.toString());
			if(D)Log.i("TAG",postgps.getResponse());
		}
	}

	@Override 
	public void onDestroy() {
		if(D) Log.d(TAG,"Stop the service!");
		_xps.abort();
	}
	
}
