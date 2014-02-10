package tw.edu.ntu.csie.srlab;

import greendroid.app.GDMapActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
 


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

/*繼承itemizedoverlay*/
public class MyOverlay extends ItemizedOverlay <OverlayItem>
{
 
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private GeoPoint myCurrentLocation,p;
	private ArrayList<TrackPoint>  geoPointlist;
	private int position;
	private int[] Index;
	private Paint	innerPaint, borderPaint, textPaint;
	private MapView vMap ;
	private GDMapActivity mapActivity;
	public MyOverlay(Drawable defaultMarker,MapView mapView,GDMapActivity mapActivity) 
	{
		
		super(boundCenterBottom(defaultMarker));
		this.vMap=mapView;
		this.mapActivity=mapActivity;
	}
	 
 

    public boolean draw(Canvas canvas,MapView mapview,boolean shadow, long when)
    { 
    	  Projection projection = mapview.getProjection();
    	  

      	for(int i=mOverlays.size()-1;i>=0;i--){
      		
      		
      	
      		  Point startPoint = new Point();
      			  projection.toPixels(mOverlays.get(i).getPoint(), startPoint );
      		

      		
      		int INFO_WINDOW_WIDTH = 35;
  			int INFO_WINDOW_HEIGHT = 25;
  		
  			RectF infoWindowRect = new RectF(0,0,INFO_WINDOW_WIDTH,INFO_WINDOW_HEIGHT);				
  			int infoWindowOffsetX =startPoint.x-INFO_WINDOW_WIDTH/2;
  			int infoWindowOffsetY = startPoint.y-INFO_WINDOW_HEIGHT-50;
  			infoWindowRect.offset(infoWindowOffsetX,infoWindowOffsetY);

  			//  Draw inner info window
  			canvas.drawRoundRect(infoWindowRect, 5, 5, getInnerPaint());
  			
  			//  Draw border for info window
  			canvas.drawRoundRect(infoWindowRect, 5, 5, getBorderPaint());
  				
  			//  Draw the MapLocation's name
  			int TEXT_OFFSET_X = 10;
  			int TEXT_OFFSET_Y = 15;
  			canvas.drawText(mOverlays.get(i).getTitle(),infoWindowOffsetX+TEXT_OFFSET_X,infoWindowOffsetY+TEXT_OFFSET_Y,getTextPaint());

      	}
    	super.draw(canvas, mapview, false); 
    	return true; 
    }
    

	
	public Paint getTextPaint() {
		if ( textPaint == null) {
			textPaint = new Paint();
			textPaint.setARGB(255, 255, 255, 255);
			textPaint.setAntiAlias(true);
			textPaint.setTextSize(16);
		}
		return textPaint;
	}
	
	public Paint getInnerPaint() {
		if ( innerPaint == null) {
			innerPaint = new Paint();
			innerPaint.setARGB(225, 75, 75, 75); //gray
			innerPaint.setAntiAlias(true);
		}
		return innerPaint;
	}

	public Paint getBorderPaint() {
		if ( borderPaint == null) {
			borderPaint = new Paint();
			borderPaint.setARGB(255, 255, 255, 255);
			borderPaint.setAntiAlias(true);
			borderPaint.setStyle(Style.STROKE);
			borderPaint.setStrokeWidth(2);
		}
		return borderPaint;
	}
	public void addOverlay(OverlayItem overlay) 
	{
	    mOverlays.add(overlay);
	    populate();
	}
	@Override
	public int size() 
	{
	  return mOverlays.size();
	}
	@Override
	protected OverlayItem createItem(int i) 
	{
	  return mOverlays.get(i);
	}

	
 

	/*按下座標時會做的動作*/
	public boolean onTap(int i)
	{
		
 
    	//Location.distanceBetween(p.getLatitudeE6()/1E6, p.getLongitudeE6()/1E6, myCurrentLocation.getLatitudeE6()/1E6, myCurrentLocation.getLongitudeE6()/1E6, distance);
		return true;

	}


 

}
