package tw.edu.ntu.csie.srlab.activity;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.cyrilmottier.android.greendroid.R.string;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.aidl.ControlLocationService;
import tw.edu.ntu.csie.srlab.aidl.EmergencycallService;
import tw.edu.ntu.csie.srlab.packet.IQEmergencyCall;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroup;
import tw.edu.ntu.csie.srlab.service.ConnectService;
import tw.edu.ntu.csie.srlab.service.SkyhookLocationService;
 
 
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EmergencycallActivity extends Activity {
	
	private Handler handler = new Handler();
	private EmergencycallService svc=null;
	private final int CALL_START=0;
	private final int SHARE_LOCATION_START=CALL_START+1;
	private final int SHARE_LOCATION_AND_CALL_START=SHARE_LOCATION_START+1;
	private int postive;
	private ProgressDialog  myDialog ;
	 private static   int second=3;
 
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.emergency_call);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
   			       
   		KeyguardManager Keylockmgr = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
   		Keylockmgr.newKeyguardLock("").disableKeyguard();
        Button help = (Button) findViewById(R.id.button_send);
        Button share_location = (Button) findViewById(R.id.share_location);

//        cancel.setOnClickListener();
        Button call = (Button) findViewById(R.id.call);
        help.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		second=3;
	    		postive=SHARE_LOCATION_AND_CALL_START;
	    		handler.post( updateTimer );

	    	}
	    });
        share_location.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		second=3;
	    		postive=SHARE_LOCATION_START;
	    		handler.post( updateTimer );

	    	}
	    });
        call.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		second=3;
	    		postive=CALL_START;
	    		handler.post( updateTimer );

	    	}
	    });
       
 		
        
	}
    private Runnable updateTimer = new Runnable() {
        public void run( ) {
          
            
 
           
            if(second==0){
            	if(myDialog.isShowing()){
            	switch(postive){
            	case CALL_START:
            		call("123123123");
            		break;
            	case SHARE_LOCATION_START:
            		ShareLocation();
            		break;
            	case SHARE_LOCATION_AND_CALL_START:
            		ShareLocation();
            		call("123123123");
            		break;
            	default:
            		break;
            	}
            	myDialog.dismiss();
            	}
            	postive=-1;
            } else if (second==3){
            	
  		      myDialog = ProgressDialog.show
              (
            	EmergencycallActivity.this,
                "Timer",
                "after "+second+" second start",
                true
              );
  		      myDialog.setCancelable(true); 
  		      handler.postDelayed(this, 1000);
  		    
  		      
            }else if (second>=0){
            	 
            	handler.postDelayed(this, 1000);
             
            }
            second--;
        }
    };
    public void ShareLocation(){
		IQEmergencyCall iq=new IQEmergencyCall("HELP");

        IQ  result =   ((AppGlobalVariable) (EmergencycallActivity.this).getApplication()).requestBlocking(iq);
        String str="ERROR";
        
        if(result.getType()!= IQ.Type.ERROR){
        	str="Release Succes";

        } 
        new AlertDialog.Builder(EmergencycallActivity.this)
	      .setTitle(str)

	      .setPositiveButton(string.yes, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int id) {
	        		
	        		 
					dialog.cancel();
	        	}
	        })
	        
	        .show();	
    }
    public void call(String Numb ){
    	Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+Numb));
    	startActivity(intent);
    }
 
}
