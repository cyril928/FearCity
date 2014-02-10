package tw.edu.ntu.csie.srlab.activity;

import java.util.ArrayList;

import greendroid.app.GDActivity;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.VCard;

import com.google.android.maps.GeoPoint;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.User;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMemberLocation;
import tw.edu.ntu.csie.srlab.service.ConnectService;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

 
 
 

public  class UserInfoActivity extends GDActivity {
    public static final String EXTRA_COLOR = "com.cyrilmottier.android.gdcatalog.TabbedActionBarActivity$FakeActivity.extraColor";
    public static final String EXTRA_TEXT = "com.cyrilmottier.android.gdcatalog.TabbedActionBarActivity$FakeActivity.extraText";
	private XMPPConnection connection;
	private boolean Debug=true;
	private Double Templongitude;
	private Double Templatitude;
	 String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        
        connection = ((AppGlobalVariable) (UserInfoActivity.this).getApplication()).getConnection();
        final String MUCconference=AppGlobalVariable.conference;
       final String SERVER_IP=  AppGlobalVariable .SERVER_IP;
        if (intent != null) {
            setActionBarContentView(R.layout.user_detail);

//            TextView textView = (TextView) findViewById(R.id.group_name);
//            textView.setText(intent.getStringExtra(EXTRA_TEXT));
//            textView.setBackgroundColor(intent.getIntExtra(EXTRA_COLOR, Color.WHITE));
            
            
            
            final Bundle bundle=this.getIntent().getExtras();

            Templatitude= bundle.getDouble("me_latitude" ) ;
            Templongitude=bundle.getDouble("me_longitude" );
            final TextView user_name = (TextView) findViewById(R.id.user_name);
            TextView user_phone = (TextView) findViewById(R.id.user_phone);
            TextView user_email = (TextView) findViewById(R.id.user_email);
            VCard vCard = new VCard();
            try {
				//vCard.load(connection);
            	Log.i("TAG", "  sender  "+ bundle.getString("sender")+"@"+SERVER_IP);
				vCard.load(connection, bundle.getString("sender")+"@"+SERVER_IP);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // load own VCard
			
            user_name.setText( bundle.getString("sender"));
             number=vCard.getPhoneWork("mobile");
            user_phone.setText(number  );
            user_email.setText(  vCard.getEmailWork());
            
            user_phone.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
		            Intent myIntenDial = new 
					Intent
					(
							"android.intent.action.VIEW",Uri.parse("tel:"+number)
					);
					startActivity(myIntenDial);
					
				}
            	
            });

            Button route = (Button) findViewById(R.id.route);
 
    		 
            route.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
		 
					Intent  TrackingIntent = new Intent(UserInfoActivity.this, TrackingmapActicity.class);
					TrackingIntent.putExtras(bundle);
					startActivity(TrackingIntent);
				}
            	
            });
            Button direct = (Button) findViewById(R.id.direction);
            
   		 
            direct.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
			          Intent intent = new Intent();  
			          intent.setAction(android.content.Intent.ACTION_VIEW); 
			      	IQQueryGroupMemberLocation iq=new IQQueryGroupMemberLocation();
			      	  ArrayList<User> groupmember=null;
			        IQ  result =  ((AppGlobalVariable) (UserInfoActivity.this).getApplication()).requestBlocking(iq);
			    	if (result != null) {
						if (result.getType() == IQ.Type.ERROR) {
							 
							//ConnectService.showToast(this,result.getError().getCondition());
							 
								return ;
						} else{
							   
				            
							 groupmember=((IQQueryGroupMemberLocation)result).getusers();
				          
						}
			    	} 
			          /* 傳入路徑規劃所需要的地標位址 */ 
			          intent.setData 
			          ( 
			            Uri.parse("http://maps.google.com/maps?f=d&saddr="+ 
			            		 Templatitude +","+ Templongitude + 
			            "&daddr="+ groupmember.get(0).getLatitude() +","+ groupmember.get(0).getLongitude() + 
			            "&hl=tw") 
			          ); 
			          startActivity(intent); 
//					Intent  TrackingIntent = new Intent(UserInfoActivity.this, TrackingmapActicity.class);
//					TrackingIntent.putExtras(bundle);
//					startActivity(TrackingIntent);
				}
            	
            });
            
        }
    }
	  
}