package tw.edu.ntu.csie.srlab;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
 

public class HelloItemizedOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	private View viewBubble;
	private ViewGroup parentBubble;
	private LayoutInflater mInflater;   
 
 
	private MapView map=null;
	public HelloItemizedOverlay(Drawable defaultMarker, Context context,MapView map) {
		 super(boundCenterBottom(defaultMarker));
 
		 mContext = context;
		 this.map=map;
 
		 
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}

	public void deleteOverlay(int index) {
		mOverlays.remove(index);
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		 
		 return mOverlays.get(i);
	}
     


 
	@Override
	public int size() {
	 
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
 
 
	  addBubble(item);
	  return true;
	}

	//the main function about bubble
	private void addBubble(OverlayItem point) {

	//if there isn't any bubble on the screen enter
	       if (viewBubble == null) {

	               parentBubble = (ViewGroup) map.getParent();
	               this.mInflater = LayoutInflater.from(mContext);
	              //We inflate the view with the bubble
	               viewBubble = mInflater.inflate(R.layout.bubble,
	                               parentBubble, false);

	               //to position the bubble over the map. The -128 and -150 are the offset.
	               MapView.LayoutParams mvlp = new MapView.LayoutParams(
	                               MapView.LayoutParams.WRAP_CONTENT,
	                               MapView.LayoutParams.WRAP_CONTENT, point.getPoint(), -58 ,  -80,
	                               MapView.LayoutParams.LEFT);

	               FrameLayout f = (FrameLayout) viewBubble;
	//Fill the text
	               ((TextView) f.findViewById(R.id.bubbleText))
	                               .setText(point.getTitle());
	               ((TextView) f.findViewById(R.id.time))
                   .setText(point.getSnippet());
	               ImageView bb=((ImageView) f.findViewById(R.id.close));
	                bb.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							  map.removeView(viewBubble);
	                          viewBubble = null;
						}
	                	
	                });
	             
	//And the event.
	               viewBubble.setOnClickListener(new OnClickListener() {


	                       //When we touch the bubble it is removed. And make null viewBubble to reuse it.
	                       @Override
	                       public void onClick(View v) {
//	                    	   showToast("qqqq");

	                       }
	               });
	               //As you see, add in the map.
	               map.addView(viewBubble, mvlp);
	       }			

	}
    public boolean draw(Canvas canvas,MapView mapview,boolean shadow, long when)
    { 
 
    	super.draw(canvas, mapview, false); 
    	return true; 
    }
}
