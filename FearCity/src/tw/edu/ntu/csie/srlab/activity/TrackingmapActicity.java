package tw.edu.ntu.csie.srlab.activity;

import greendroid.app.GDMapActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.item.ThumbnailItem; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;

 
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.Convert;
import tw.edu.ntu.csie.srlab.HelloItemizedOverlay;
import tw.edu.ntu.csie.srlab.LineItemizedOverlay;
import tw.edu.ntu.csie.srlab.MyOverlay;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.R.id;
import tw.edu.ntu.csie.srlab.R.layout;
import tw.edu.ntu.csie.srlab.TrackPoint;
import tw.edu.ntu.csie.srlab.packet.IQQueryEmergencyRecordCall;
import tw.edu.ntu.csie.srlab.packet.IQQueryTrackLocation;
import tw.edu.ntu.csie.srlab.overlayslist;
import tw.edu.ntu.csie.srlab.requestJsonUrl;




import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TrackingmapActicity extends  GDMapActivity {
	private MapView mapView;
	 
	private Handler mHandler = new Handler();
	private List<Overlay>  mapOverlays ;
	private MyOverlay itemizedOverlay ;
	private  MapController mapController;
	private   ArrayList<TrackPoint> GeoPointlist=new ArrayList<TrackPoint>();
	private   int point =0;
	private   Button Prev,Next;
	private  Bundle bundle;
	private boolean route_line=true;
	  
	private LineItemizedOverlay lineOverlay;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.trackingmap);
		  
		    bundle=this.getIntent().getExtras();
		  mapView = (MapView) findViewById(R.id.mapview);
		  mapView.setBuiltInZoomControls(true);    
		  Drawable  drawable = this.getResources().getDrawable(R.drawable.ic_maps_indicator_current_position);
		  itemizedOverlay = new MyOverlay(drawable,mapView,this);
		  mapOverlays = mapView.getOverlays();
		  mapController = mapView.getController();
		  //mapController.setCenter(new GeoPoint((int)(22.201968*1E6),(int)(113.541555*1E6)));
		  mapController.setZoom(17);
		  
			  
		      mapOverlays.add(itemizedOverlay);
		          Prev=  (Button) findViewById(R.id.Prev); 
 
		      Prev.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.i("TAG","point "+point);
					 if( point  !=GeoPointlist.size()-1){
						 mapController.animateTo(new GeoPoint((int)( GeoPointlist.get(++point).getLatitude()*1E6 ), 
									(int)( GeoPointlist.get( point).getLongitude()*1E6 )) );
					 }
					
				}
		    	  
		      });
		          Next=  (Button) findViewById(R.id.Next); 
		      Next.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						Log.i("TAG","point "+point);
						 if( point !=0){
							 mapController.animateTo(new GeoPoint((int)( GeoPointlist.get(--point).getLatitude()*1E6 ), 
										(int)( GeoPointlist.get( point).getLongitude()*1E6 )) );
						 } 
						
						
					}
			    	  
			      });
		      addActionBarItem(Type.Export, R.id.action_bar_export);
		 }
    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
 
            case R.id.action_bar_export:
            	if(route_line){
            		route_line=false;
            		  mapOverlays.remove(lineOverlay);
            	}else{
            		route_line=true;
            	    mapOverlays.remove(itemizedOverlay);
            		  mapOverlays.add(lineOverlay);
            		    mapOverlays.add(itemizedOverlay);
            	}
         	 
                 break;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }
	protected void onStart (){

			mHandler.post(new Runnable(){

				@Override
				public void run() {
			
			   
			   
			   IQQueryTrackLocation iq=new IQQueryTrackLocation(bundle.getString("index") );

			        IQ  result =   ((AppGlobalVariable) (TrackingmapActicity.this).getApplication()).requestBlocking(iq);
			        if( result.getType()!=IQ.Type.ERROR){
			        	GeoPointlist=( (IQQueryTrackLocation)result ).getTrackPointlist();
			        	 
			        	
			        }
			if(! (GeoPointlist.size()>0)){
		
				return;
			}		
			point= GeoPointlist.size()-1;
			Log.d("TAG","GeoPointlist.size() "+GeoPointlist.size());
		   String start=GeoPointlist.get(GeoPointlist.size()-1).getLatitude()+","+GeoPointlist.get(GeoPointlist.size()-1).getLongitude();
		   String dest=GeoPointlist.get(GeoPointlist.size()-2).getLatitude()+","+GeoPointlist.get(GeoPointlist.size()-2).getLongitude();
		   mapController.setCenter(new GeoPoint((int)(GeoPointlist.get(GeoPointlist.size()-1).getLatitude()*1E6),(int)(GeoPointlist.get(GeoPointlist.size()-1).getLongitude()*1E6)));
			StringBuffer mid = new StringBuffer(); 
		    for (int i=GeoPointlist.size()-3;i>=0;i--){
		 
			 
			   mid.append("+to:"+GeoPointlist.get(i).getLatitude()+","+GeoPointlist.get(i).getLongitude());
		   } 
			Log.d("TAG","mid "+mid.length()); 
		   String url="http://maps.google.com/maps?saddr="+start+"&daddr="+dest+mid.toString()+"&vpsrc=0&dirflg=w&mra=ltm&t=m&z=15&output=json";
		  Log.i("TAG",url);
		   requestJsonUrl test1=new requestJsonUrl(url);
		   ArrayList<overlayslist>  li=test1.getlist();
		   ArrayList<GeoPoint> polylines=new ArrayList<GeoPoint>();
		   
		   int [] test1PolylinesFirst=new int [li.size()];
		   int k=0;
		   for (int i=0;i<li.size();i++){
 
			
			   ArrayList myPoints=(ArrayList)decodePolyline(li.get(i).getpolylines());
			   test1PolylinesFirst[i]=(i!=0)?test1PolylinesFirst[i-1]:0+myPoints.size();
			
				   polylines.addAll(myPoints);
				  
			   
			   
		   }

		     lineOverlay = new LineItemizedOverlay(polylines);        
		   mapOverlays.add(lineOverlay);
	
		   for(int i=GeoPointlist.size()-1;i>=0;i--)
			  {
				  
				  GeoPoint gp = new GeoPoint((int)( GeoPointlist.get(i).getLatitude()*1E6 ),(int)( GeoPointlist.get(i).getLongitude()*1E6 ));
				  OverlayItem overlayitem = new OverlayItem(gp,""+(GeoPointlist.size()-i), GeoPointlist.get( i).getTimeStamp() );
				  itemizedOverlay.addOverlay(overlayitem);
			  }
		      mapOverlays.add(itemizedOverlay);
				}
			});
			
			super.onStart();
		}
	  
		 @Override
		 protected boolean isRouteDisplayed() {
		  return false;
		 }
		 
		 
		 public static List<GeoPoint> decodePolyline(String poly) {
		        int len = poly.length();
		        int index = 0;
		        int lat = 0;
		        int lng = 0;
		        List<GeoPoint> decoded = new ArrayList<GeoPoint>();

		        while (index < len) {
		            int b;
		            int shift = 0;
		            int result = 0;
		            do {
		                b = poly.charAt(index++) - 63;
		                result |= (b & 0x1f) << shift;
		                shift += 5;
		            } while (b >= 0x20);
		            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		            lat += dlat;

		            shift = 0;
		            result = 0;
		            do {
		                b = poly.charAt(index++) - 63;
		                result |= (b & 0x1f) << shift;
		                shift += 5;
		            } while (b >= 0x20);
		            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		            lng += dlng;
		            
		            decoded.add(new GeoPoint(
		                    Convert.asMicroDegrees(lat / 1E5), Convert.asMicroDegrees(lng / 1E5)));

		        }
		        return decoded;
		    }
		}


	
 
