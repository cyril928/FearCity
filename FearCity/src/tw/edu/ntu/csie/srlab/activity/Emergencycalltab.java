package tw.edu.ntu.csie.srlab.activity;
 
 
import org.jivesoftware.smack.packet.IQ;

import tw.edu.ntu.csie.srlab.R;
 
 

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import greendroid.app.ActionBarActivity;
 
import greendroid.app.GDTabActivity;

public class Emergencycalltab extends GDTabActivity {
 
 
 
    
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
//		this.setTitle("Screen 2");
		Bundle bundle=this.getIntent().getExtras();

		final String  InfoText =  getString(R.string.UserInfo);
		final Intent  InfoIntent = new Intent(this, UserInfoActivity.class);
		InfoIntent.putExtras(bundle);
		addTab(InfoText, InfoText, InfoIntent);
			        
		final String TrackingText =  getString(R.string.UserTracking);
		final Intent  TrackingIntent = new Intent(this, TrackingmapActicity.class);
		TrackingIntent.putExtras(bundle);
		addTab(TrackingText, TrackingText, TrackingIntent);
 
			
        
    }
    


 
 
}
