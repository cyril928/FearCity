package tw.edu.ntu.csie.srlab;

 

	import java.util.ArrayList;
	import java.util.Iterator;

	import android.graphics.Canvas;
	import android.graphics.Color;
	import android.graphics.Paint;
	import android.graphics.Path;
	import android.graphics.Point;

	import com.google.android.maps.GeoPoint;
	import com.google.android.maps.MapView;
	import com.google.android.maps.Overlay;
	import com.google.android.maps.OverlayItem;
	import com.google.android.maps.Projection;

	public class LineItemizedOverlay extends Overlay {

	    private ArrayList<GeoPoint> mOverlays = new ArrayList<GeoPoint>();
	    private int colour;
	    private static final int ALPHA = 120;
	    private static final float STROKE = 4.5f;
	    private final Path path;
	    private final Point p;
	    private final Paint paint;


	    public LineItemizedOverlay(ArrayList<GeoPoint> mOverlays) {
	        this.mOverlays = mOverlays;
	        path = new Path();
	        p = new Point();
	        paint = new Paint();
	    }

	    @Override
	    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	        super.draw(canvas, mapView, shadow);

	        paint.setColor(colour);
	        paint.setAlpha(ALPHA);
	        paint.setAntiAlias(true);
	        paint.setStrokeWidth(STROKE);
	        paint.setStyle(Paint.Style.STROKE);

	        redrawPath(mapView);
	        canvas.drawPath(path, paint);
	    }

	    private void redrawPath(final MapView mv) {
	        final Projection prj = mv.getProjection();
	        path.rewind();
	        final Iterator<GeoPoint> it = mOverlays.iterator();
	        prj.toPixels(it.next(), p);
	        path.moveTo(p.x, p.y);
	        while (it.hasNext()) {
	            prj.toPixels(it.next(), p);
	            path.lineTo(p.x, p.y);
	        }
	        path.setLastPoint(p.x, p.y);
	    }

	}

	
