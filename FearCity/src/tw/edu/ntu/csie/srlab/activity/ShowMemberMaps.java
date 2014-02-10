package tw.edu.ntu.csie.srlab.activity;

 
import greendroid.app.GDMapActivity;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBarHost;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ItemAdapter;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.NormalActionBarItem;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionBar;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;
import greendroid.widget.item.Item;
import greendroid.widget.item.TextItem;
import greendroid.widget.item.ThumbnailItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 
 
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.packet.VCard;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.EmergencyCall;
import tw.edu.ntu.csie.srlab.HelloItemizedOverlay;
import tw.edu.ntu.csie.srlab.User;
import tw.edu.ntu.csie.srlab.QuickAction3D.ActionItem3D;
import tw.edu.ntu.csie.srlab.QuickAction3D.QuickAction3D;
import tw.edu.ntu.csie.srlab.adapter.GroupListItemAdapter;
import tw.edu.ntu.csie.srlab.aidl.ControlLocationService;
import tw.edu.ntu.csie.srlab.packet.IQEmergencyCall;
import tw.edu.ntu.csie.srlab.packet.IQQueryEmergencyRecordCall;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroup;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMember;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMemberLocation;
import tw.edu.ntu.csie.srlab.service.ConnectService;
import tw.edu.ntu.csie.srlab.service.SkyhookLocationService;


 
import com.cyrilmottier.android.greendroid.R;
import com.cyrilmottier.android.greendroid.R.string;
import com.google.android.maps.GeoPoint;
 
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
 
import android.content.BroadcastReceiver;
import android.content.ComponentName;
 
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.Toast;

public class ShowMemberMaps extends GDMapActivity{
	
	// debugging 
	private static final String TAG = "HelloGoogleMaps"; 
    private static final boolean D = true;
	
	private static final String SETTING_INFOS = "SETTING_info";
	private static final String SETTING_ACCOUNT = "SETTING_ACCOUNT";
	private static final String SETTING_PASSWORD = "SETTING_PASSWORD";
	private String account;
	private String password;
	
	//item's on the map have to put in overlay list 
	protected List<Overlay> mapOverlays;
	protected HelloItemizedOverlay itemizedoverlay;
	// map controller, set zoom value and set user's location in the map as center point in the app
	protected MapController mapController;
	
	
	// Intent request code
	private static final int REQUEST_XMPP_CONNECT = 1;
	private static final int REQUEST_XMPP_ROSTER = 2;
	private  String username;
	private ArrayList<User> groupmember;
	private Handler mHandler = new Handler();
	private Double Templongitude=0.0;
	private Double Templatitude=0.0;
	boolean SkyhookLocationServiceisStart=true;
    private Menu  menu;
	//Broadcast intent action
	 public static final String BroadcastReceive = "tw.edu.ntu.csie.srlab.lee.map.locationreport";
	
	// XMPP connection
	private XMPPConnection mConnetion;
    private QuickActionWidget mBar;
    final int QuickActionCreateGroup=0;
    final int QuickActionMyGroup=1;
    ControlLocationService svc=null;
    EditText editinput=null;
    boolean isHandOnupdated=false;
 	ArrayList<EmergencyCall> call=new ArrayList<EmergencyCall>();
     QuickAction3D quickAction ;
     GroupListItemAdapter adapter;
     SlidingDrawer drawer;
    /** Called when the activity is first created. */
    ServiceConnection con=new ServiceConnection(){

	  public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
		svc=ControlLocationService.Stub.asInterface(service);
	 
	  }

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

			svc=null;

		}

 

 
    };
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    
        setActionBarContentView(R.layout.main);

        //add sliderdraw
        View inflatedDrawerLayout = getLayoutInflater().inflate(R.layout.slideingofmain, null);
        int width = getWindow().getAttributes().width, height = getWindow().getAttributes().height;
        LayoutParams params = new LayoutParams(width, height);
        getWindow().addContentView(inflatedDrawerLayout, params);
       
    	mapSetup();

        mConnetion=((AppGlobalVariable) (this).getApplication()).getConnection();
       
 
        addActionBarItem(Type.Refresh, R.id.action_bar_refresh);
        addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
                .setDrawable(R.drawable.ic_title_export)
                .setContentDescription(R.string.gd_export), R.id.action_bar_export);
//        addActionBarItem(Type.Locate, R.id.action_bar_locate);
        
        username = ((AppGlobalVariable) (this).getApplication()).getusername();
   
        getjson(username);

        ConnectServiceSetup();
        skyhookSetup();
        
        
        
        ActionItem3D CreateGroup = new ActionItem3D();
        CreateGroup.setTitle(getString(R.string.QuickActionCreateGroup));
        CreateGroup.setIcon(getResources().getDrawable(R.drawable .gd_action_bar_add));
        
        ActionItem3D MyGroup = new ActionItem3D();
        MyGroup.setTitle(getString(R.string.QuickActionMyGroup));
        MyGroup.setIcon( getResources().getDrawable(R.drawable .gd_action_bar_group));
		//create quickaction
        quickAction= new QuickAction3D(this);
      
		quickAction.addActionItem(CreateGroup);
		quickAction.addActionItem(MyGroup);
		
		quickAction.setOnActionItemClickListener(new QuickAction3D.OnActionItemClickListener() {			
			@Override
			public void onItemClick(int pos) {
	            switch (pos) {
	        	case QuickActionCreateGroup:
	 
	 
	 
			    	 
			    	startActivity(new Intent(ShowMemberMaps.this, CreateGroupActivity.class)); 	
			    	break;
	        		 
	        	case QuickActionMyGroup:
	        		startActivity(new Intent(ShowMemberMaps.this,GroupListActivity.class));
	        		break;
	        	default:
	        		break;
	            }
			}
		});
		Bundle bundle = this.getIntent().getExtras();
		 List<Item> items = new ArrayList<Item>();
		 ListView listview = (ListView) findViewById(R.id.list1);
		 
		 IQQueryEmergencyRecordCall iq=new IQQueryEmergencyRecordCall( );

	        IQ  result =   ((AppGlobalVariable) (ShowMemberMaps.this).getApplication()).requestBlocking(iq);
	        if( result.getType()!=IQ.Type.ERROR){
	        	  call=( (IQQueryEmergencyRecordCall)result ).getcall();
	        	for (int i=0;i<call.size();i++){	
	        		items.add(new ThumbnailItem(call.get(i).getsender() + " "+call.get(i) .getmsgBody() ,call.get(i) .gettime()  , R.drawable.green_35)); 
	        	}
	        	
	        }
	        drawer = (SlidingDrawer) this.findViewById(R.id.slidingDrawer1);
		if(bundle!=null){
			
			if(bundle.getString("EmergencyCall").equals("EmergencyCall")){
			NotificationManager notificationManager = (NotificationManager) this.getSystemService(  
			          android.content.Context.NOTIFICATION_SERVICE);  
			notificationManager.cancel(bundle.getInt("j"));
//			String sender=bundle.getString("sender"  );
//			String msgBody=bundle.getString("msgBody" );
//			String time=bundle.getString("time" );
			
			drawer.open();
			
			//items.add(new ThumbnailItem(sender + " "+msgBody ,time  , R.drawable.green_35));
			}
		}
         ItemAdapter adapter = new ItemAdapter(this, items);
    
    	 listview.setAdapter(adapter);
    	 listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				EmergencyCall callinfo= call.get(arg2);
				Intent intent = new Intent(ShowMemberMaps.this, UserInfoActivity.class);
				 Bundle bundletrack=new Bundle();
				 bundletrack.putString("index", callinfo.getindex());
				 bundletrack.putString("sender",  callinfo.getsender());
				 bundletrack.putString("GroupName",  callinfo.getGroupName());
				 bundletrack.putString("index", callinfo.getindex());
				 
			 
				 bundletrack.putDouble("me_latitude",Templatitude);
				 bundletrack.putDouble("me_longitude", Templongitude);
				 intent.putExtras(bundletrack);
		    	startActivity(intent); 	
			}
    		 
    	 });
      
   	 final VCard vCard = new VCard();
   	try {
		vCard.load(mConnetion);
	} catch (XMPPException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} 
		 //Log.i("TAG","vCard.getPhoneWork(\"mobile\") "+vCard.getPhoneWork("mobile"));
		 if(vCard.getPhoneWork("mobile")==null){
			 
			 editinput=new EditText(ShowMemberMaps.this);
					 new AlertDialog.Builder(ShowMemberMaps.this)
			   	      .setTitle("plz enter you phone number")
			   	      .setView(editinput)
			   	      .setPositiveButton(string.yes, new DialogInterface.OnClickListener() {
			   	        	public void onClick(DialogInterface dialog1, int id) {
			   	        	 
			   	        	 
							 vCard.setPhoneWork("mobile",editinput.getText().toString());
							 vCard.setEmailWork( 		 mConnetion.getAccountManager().getAccountAttribute("email")  );
							 
							 try {
									vCard.save(mConnetion);
								} catch (XMPPException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								dialog1.cancel();
			   	        	}
			   	        })
		  	        .show();

					
				  
					
		 }
		
		  
 
    
    }
  
    public void refreshEmergency(){
        Handler th1=new Handler();
        th1.post(new Runnable (){

			@Override
			public void run() {
//		        IQQueryGroup iq=new IQQueryGroup();
//
//		        IQ  result =  ((AppGlobalVariable) (GroupListActivity.this).getApplication()).requestBlocking(iq);
//				if(D) Log.d("TAG","result "+((IQQueryGroup) result)  );
//
////				if (result != null) {
////					if (result.getType() == IQ.Type.ERROR) {
////						showToast(result.getError().getCondition());
////						if(D) Log.d("TAG","result  ERROR");
////					}else{if(D) Log.d("TAG","result CORRECT");
////						return ((IQQueryGroup)result).getgroup();
////					}
////				}
////				if(D) Log.d("TAG","result "+ result .toXML());
//	    		 return ((IQQueryGroup) result)  .getgroup();   
//				mData = getData(); 
//				adapter = new EmergencyAdapter(ShowMemberMaps.this, mData);    
//				setListAdapter(adapter);
//				adapter.notifyDataSetChanged();
			}
        	
        });
    }
    void ConnectServiceSetup(){
    	
        Intent ser = new Intent(this, ConnectService.class);
    	startService(ser); 
    }
    public void getjson(String username){
//    	Map map = new HashMap();
//		map.put("username",username);
//		JSONObject str = new JSONObject(map);
//		Log.v(""+str.toString(),"***");
//    	HttpConnect httppost=new HttpConnect("http://taylor19882002.no-ip.org/FearCity/GetGps.php",str.toString());
//    	Log.v("www",httppost.getResponse() );
//    	if(!httppost.getResponse().equals("")){
//    	try {
//        	JSONObject ResponseJson=new JSONObject(new String(httppost.getResponse() ));
//        	Log.v("qqq",ResponseJson.toString());;
//			JSONArray the_json_array = ResponseJson.getJSONArray("groupmember");
//			 groupmember=new ArrayList<User>();
//			for (int i=0;i<the_json_array.length();i++){
//				User user=new User();
//				
//				JSONObject json=(JSONObject) the_json_array.get(i);
//				if (!username.equals(json.getString("username"))){
//					user.setusername(json.getString("username"));
//					user.setLatitude(json.getDouble("Latitude"));
//					user.setLongitude(json.getDouble("Longitude"));
//					user.setTimeStamp(json.getString("TimeStamp"));
//					groupmember.add(user);
//				}
//			}
//			
//		 
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//    	}
    	IQQueryGroupMemberLocation iq=new IQQueryGroupMemberLocation();

        IQ  result =  ((AppGlobalVariable) (ShowMemberMaps.this).getApplication()).requestBlocking(iq);
    	if (result != null) {
			if (result.getType() == IQ.Type.ERROR) {
				 
				ConnectService.showToast(this,result.getError().getCondition());
				 
					return ;
			} else{
				   
	            
				 groupmember=((IQQueryGroupMemberLocation)result).getusers();
	          
			}
    	} 
    }
    
    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
//            case R.id.action_bar_locate:
//
//                break;

            case R.id.action_bar_refresh:
                final LoaderActionBarItem loaderItem = (LoaderActionBarItem) item;
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        loaderItem.setLoading(false);
                    
                        	getjson(username);
                       
                        	putItem(Templongitude,Templatitude);
                         
                        
                    }
                }, 2000);
//                Toast.makeText(this, R.string.refresh_pressed, Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_bar_export:
            	quickAction.show(item.getItemView());
//                Toast.makeText(this, R.string.custom_drawable, Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }
 
 
    
 
    // In order to show the over item on the map, use the simple configuration here
    private void mapSetup(){
    	
    	   // set default value of mapview
        MapView mapView = (MapView) findViewById(R.id.mapview);
//        mapView.setBuiltInZoomControls(true);
//        mapView.setStreetView(true);
//        mapView.setSatellite(true);
        
        
        // Zoom the mapview to scale 17
        mapController = mapView.getController();
        mapController.setZoom(17);
        
        mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_maps_indicator_current_position);
        
        
        // Item can put on the map
        itemizedoverlay = new HelloItemizedOverlay(drawable, this,mapView);
    }
    
    // setup and start the skyhook service 
    private void skyhookSetup(){
	
    	 IntentFilter filter = new IntentFilter();
         filter.addAction(BroadcastReceive);
         registerReceiver(LocationReceiver, filter);
         
         Intent Skyhookintent = new Intent(this,SkyhookLocationService.class);
         startService(Skyhookintent);
         bindService(new Intent(this, SkyhookLocationService.class), con, Context.BIND_AUTO_CREATE);
    }
    
    protected boolean isRouteDisplayed() {
        return false;
    }
    public void  onSart(){
    	putItem(Templongitude,Templatitude);
    	super.onStart();
    }
    //put item on the map
    protected void putItem(Double longitude, Double latitude){
    	GeoPoint point;
    	OverlayItem overlayitem;
    	Log.d(TAG,"==========="+itemizedoverlay.size()+"==============");
    	mapOverlays.clear();
        if (Templongitude!=0&&Templatitude!=0){
	    	if(itemizedoverlay.size()>0)
	    		itemizedoverlay.deleteOverlay(0);
	    	int y = (int) (longitude*1E6);
	    	int x = (int) (latitude*1E6);   
	    	
	
	    	  point = new GeoPoint(x,y);  
	          overlayitem = new OverlayItem(point, "Hello "+username, "Ur in Here!");
	        itemizedoverlay.addOverlay(overlayitem);
	    	mapOverlays.add(itemizedoverlay);
	        mapController.setCenter(point);
        }
        if(groupmember!=null){
	        for(int i=0; i<groupmember.size();i++){
	        	if(groupmember.get(i).getusername().equals(username))
	        	{
	        		continue;
	        	}
	        	 point = new GeoPoint((int)(groupmember.get(i).getLatitude()*1E6),(int)(groupmember.get(i).getLongitude()*1E6));  
	             overlayitem = new OverlayItem(point, groupmember.get(i).getusername(), groupmember.get(i).getTimeStamp());
	        	itemizedoverlay.addOverlay(overlayitem);
	        }
        }
    	mapOverlays.add(itemizedoverlay);
    	try {
			svc.SetSendLocationtoActivity(false);
			if(D)Log.i("TAG","isSend_3");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /* create a menu for setup connections */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if(D) Log.d("TAG","=== MENU CREATE ====");
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        this.menu=menu;
        return true;
    }
 
    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.logout:

    		logout();
    		
    		return true;

    	case R.id.roster_list:
  
    			Intent listIntent = new Intent(this, XMPPRosterList.class);
    			startActivity (listIntent );
 
    		
    	case R.id.updateMyLocation:
    		if(SkyhookLocationServiceisStart){
        		try {
    				svc.SetSendLocationtoActivity(true);
    				 
    			} catch (RemoteException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			isHandOnupdated=true;
    			Handler mHandler = new Handler();
    			 mHandler.postDelayed((new Runnable(){

    				@Override
    				public void run() {
    					if(D)Log.i("TAG","isSend_4");
    					 
    						if(isHandOnupdated){
    							Toast.makeText(ShowMemberMaps.this, "may be need more time to update my location", Toast.LENGTH_SHORT).show();
    							isHandOnupdated=false;
    						}else{
    							
    							Toast.makeText(ShowMemberMaps.this, "already update my location", Toast.LENGTH_SHORT).show();
    						}
    						
    	 
    				}
    				 
    			 }), 8000);
    		}else{
				Toast.makeText(ShowMemberMaps.this, "please start SkyhookLocationServiceis first", Toast.LENGTH_SHORT).show();

    		}

    		return true;
    	case R.id.stop_locationing:
    		if(SkyhookLocationServiceisStart){
    			Intent intent = new Intent(this,SkyhookLocationService.class);
    			  this.unbindService(con) ;
        		if(stopService(intent)) {		
        			 Toast.makeText(this, "You have stop the skyhook locationing service!", Toast.LENGTH_LONG).show();
        		}
        		menu.getItem(3).setTitle(R.string.start_locationing);
        		SkyhookLocationServiceisStart=false;
    		}else{
    			if(D) Toast.makeText(this, "skyhook locationing service start!", Toast.LENGTH_LONG).show();
    			SkyhookLocationServiceisStart=true;
 
    			menu.getItem(3).setTitle(R.string.stop_locationing);
    	    
    			skyhookSetup();
    		}
    		
    		return true;
    	case R.id.clear_account:
			 new AlertDialog.Builder(this)
   	      .setTitle(R.string. clear_account)
   	      .setMessage(R.string.clear_account_message)  
   	      .setPositiveButton(string.yes , new DialogInterface.OnClickListener() {
   	        	public void onClick(DialogInterface dialog, int id) {
   	        		account="";
   	        		password="";
   	        		storePrefs() ;
   	        		logout();
   	        	}
   	        })
   	        .setNegativeButton(string.no, new DialogInterface.OnClickListener() {
   	        	public void onClick(DialogInterface dialog, int id) {
   	        	}
   	        })
   	        .show();	
    		
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}	
    }
    public void logout(){
        mConnetion.disconnect();
        mConnetion=null;
    	Intent ser = new Intent(this,ConnectService.class);
    	stopService(ser); 	
    	Intent Skyhookintent = new Intent(this,SkyhookLocationService.class);
    	stopService(Skyhookintent);
    	
    	((AppGlobalVariable)getApplication()).setusername("");
    	finish();
    	 Log.d("TAG", "logout");
    }

	private void storePrefs() 
	{

		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0);
		settings.edit()

			.putString(SETTING_ACCOUNT, account)
			.putString(SETTING_PASSWORD, password)
			.commit();

	}
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	 Log.d(TAG, "onActivityResult " + resultCode);
    	 switch (requestCode) {
    	 case REQUEST_XMPP_CONNECT:
    		 if(resultCode == Activity.RESULT_OK) {
    			 Toast.makeText(this, "The xmpp connection is set", Toast.LENGTH_LONG).show();
    		 }  	 
    		 break;
    	 case REQUEST_XMPP_ROSTER:
    		 if(resultCode == Activity.RESULT_OK) {
    			 Toast.makeText(this, "You have read your friend list!", Toast.LENGTH_SHORT).show();
    		 }
    		 break;
    	 }
    }
    
    public final BroadcastReceiver LocationReceiver = new BroadcastReceiver() {
    	 	
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		
    		if(D) Log.d(TAG,"Location Broadcast received!");
    		
    		int message = intent.getIntExtra(SkyhookLocationService.MESSAGE,-1);
    		
    		switch (message) {
    		
    		case SkyhookLocationService.TYPE_DONE:
    			if (D)Toast.makeText(ShowMemberMaps.this, "WPS is ready for further requests!", Toast.LENGTH_SHORT).show();
    			break;
    		
    		case SkyhookLocationService.TYPE_ERROR:
    			String error_message = intent.getStringExtra(SkyhookLocationService.ERROR_MESSAGE);
    			if (D)Toast.makeText(ShowMemberMaps.this,error_message, Toast.LENGTH_SHORT).show();
    			break;
    			
    		case SkyhookLocationService.TYPE_LOCATION:
    			 Templongitude = intent.getDoubleExtra(SkyhookLocationService.LONGITUDE,0.0);
    			 Templatitude = intent.getDoubleExtra(SkyhookLocationService.LATITUDE,0.0);
    			 
    			 if(isHandOnupdated){
    				 isHandOnupdated=false;
    			 }
    			 
    			if(Templongitude!=0 && Templatitude!=0){
    				putItem(Templongitude,Templatitude);
    			}
    			if (D)Toast.makeText(ShowMemberMaps.this,"("+Templongitude+":"+Templatitude+")", Toast.LENGTH_SHORT).show();
    			if(D)Log.i("TAG","isSend_2");
    			break;
    			
    		default:
        		return;	
    		}
    	}
    };
	  public void onStop()
	  {    
 
		Log.i("TAG","onStop");
	    super.onStop();
	  }
	  public void onDestroy()
	  {    
		  unregisterReceiver(LocationReceiver);
		  unbindService(con);
		Log.i("TAG","onDestroy");
	    super.onDestroy();
	  }
	  public boolean onKeyDown(int keyCode, KeyEvent event)  {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
//	            if (getApplicationInfo().targetSdkVersion
//	                    >= Build.VERSION_CODES.ECLAIR) {
//	                event.startTracking();
//	            } else {
//	                onBackPressed();
//	            }
	            if (drawer.isOpened()) {
	            	drawer.close();
	            } else {
	                onBackPressed();
	            }
	           
	        } 
	        return super.onKeyUp(keyCode, event);  
	  }
    /* Called by XMPPConnectDialog when a connection is established with the XMPP server
     */
   // public void setConnection(XMPPConnection connection) {
        
//    public void setPacketListener(){
//    	//this.connection = connection;
//        this.connection = ((AppGlobalVariable)getApplication()).getConnection();
//    	if (connection != null) {
//            // Add a packet listener to get messages sent to us, it's asynchronous approach 
//            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
//            connection.addPacketListener(new PacketListener() {
//                public void processPacket(Packet packet) {
//                    Message message = null;             	
//                	if(packet instanceof Message)
//                    	message = (Message) packet;
//                    if (message.getBody() != null) {
//                        
//                        // Add the incoming message to the list view
//                        mHandler.post(new Runnable() {
//                            public void run() {
//                                
//                            }
//                        });
//                    }
//                }
//            }, filter);
//        }
//    }      
}