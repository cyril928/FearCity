package tw.edu.ntu.csie.srlab;
import java.util.Calendar;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.VCard;

 
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.packet.IQLeaveGroup;
import tw.edu.ntu.csie.srlab.packet.IQJoinGroup;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SMSReceiver extends Activity{
	  

 
 
	private Handler mHandler = new Handler();
	final boolean debug=true;
		private XMPPConnection connection;
        final String SERVER_IP= AppGlobalVariable .SERVER_IP;
        final String  MUCconference=AppGlobalVariable.conference;
        private  VCard vCard = new VCard();
	  @Override
	  public void onCreate(Bundle savedInstanceState) 
	  {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.smsreceiver);
			
		
		  final String username = ((AppGlobalVariable) (SMSReceiver.this).getApplication()).getusername();

	        connection = ((AppGlobalVariable) (SMSReceiver.this).getApplication()).getConnection();
	    
		  Bundle bundle = this.getIntent().getExtras();
				 
				
				  NotificationManager notificationManager = (NotificationManager) this.getSystemService(  
				          android.content.Context.NOTIFICATION_SERVICE);  
				  notificationManager.cancel(bundle.getInt("i"));
	 
				 final String room=bundle.getString("room"  );
				 String inviter=bundle.getString("inviter"  );
				 String reason=bundle.getString("reason" );
 
					
		    	LayoutInflater factory = LayoutInflater.from(SMSReceiver.this);
				 Button btn1;
				 Button btn2;
			  final View textEntryView = factory.inflate(R.layout.dialog_alert, null);
		        final Dialog addr=new Dialog(SMSReceiver.this,R.style.Dialog);
		       
		        addr.addContentView(textEntryView, new LayoutParams(
		                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		        //addr.setCancelable(true);
		        TextView texttitle = (TextView) textEntryView.findViewById(R.id.title);
		        TextView textmessage = (TextView) textEntryView.findViewById(R.id.message);
		        ImageView image = (ImageView) textEntryView.findViewById(R.id.widget55);
		        image.setImageDrawable(SMSReceiver.this.getResources().getDrawable(R.drawable.alert_48));
		        final EditText edtInput ;
		        
		        String title=((inviter+" invite you to join group "+room).replace(MUCconference, "")).replace("@"+SERVER_IP, "");
		        
		        texttitle.setText(title);
		        textmessage.setText(reason);
				btn1 = (Button) textEntryView.findViewById(R.id.positiveButton);
				btn1.setText(android.R.string.yes);
				btn2 = (Button) textEntryView.findViewById(R.id.negativeButton);
		
				btn1.setOnClickListener(new View.OnClickListener(){

					public void onClick(View v) {if (debug)Log.d("TAG","JOIN "+room);
						MultiUserChat muc=new MultiUserChat(connection,room );
							try {	
//								Calendar c = Calendar.getInstance();
//								int curHours = c.get(Calendar.HOUR_OF_DAY);
//								int curMinutes = c.get(Calendar.MINUTE);
//								if (   curHours)
								muc.join(username);
								JoinGroup(room );
								   
								 addvcard(  room.replace(MUCconference, ""));

								
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    
//						 Intent intent = new Intent();
	    				 
//						 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	    			    	intent.setClass(SMSReceiver.this,main.class);
//	    			    	startActivity(intent);
	    			    	addr.cancel();
						 SMSReceiver.this.finish();
						
						
					}


		        });
				btn2.setOnClickListener(new View.OnClickListener(){

					
					public void onClick(View v) {
						addr.cancel();
						SMSReceiver.this.finish();
					}


		        });
		        addr.show();
		        
				
		  
	  }
	  public void addvcard(String room){
			try {
				vCard.load(connection);
//				vCard.setProperty(group_name.getText().toString()+"_upload_link", ""+true );
				vCard.setField(room+"_upload_link", ""+true);
//				vCard.setProperty(group_name.getText().toString()+"_download_link", ""+true );
				vCard.setField(room+"_download_link", ""+true);
				//SmackConfiguration.setPacketReplyTimeout(2000);
				vCard.save(connection);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	  }
	  public  void JoinGroup(String groupname ){
		  IQJoinGroup iq=new IQJoinGroup(groupname .replace(MUCconference, ""));

	    	IQ result = ((AppGlobalVariable) (SMSReceiver.this).getApplication()).requestBlocking(iq);
	    	if (result != null) {
				if (result.getType() == IQ.Type.ERROR) {
					try {
						showToast(result.getError().getMessage());
						throw new  Exception(result.getError().getMessage());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
				
					
				
			

 
					
					
				}
	    	}
	  }
		private void showToast(String str){
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

		}
}
