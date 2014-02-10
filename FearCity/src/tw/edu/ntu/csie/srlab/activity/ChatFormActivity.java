package tw.edu.ntu.csie.srlab.activity;

import greendroid.app.GDListActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.ChatMessage;
import tw.edu.ntu.csie.srlab.adapter.ChatAdapter;
import tw.edu.ntu.csie.srlab.service.ConnectService;

import com.cyrilmottier.android.greendroid.R;

 

 
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
 
 
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
 
import android.widget.Button;
import android.widget.EditText;
 
 

 
public class ChatFormActivity extends GDListActivity {
	/** Called when the activity is first created. */


 
    private Button mSendButton;
	 
 
    private EditText view ;
 
 
	private ArrayList<ChatMessage> ChatMessageList=new ArrayList<ChatMessage>();
	private Handler mHandler = new Handler();
	private XMPPConnection connection;
	private String username;
	private ChatAdapter adapter;
	private MultiUserChat muc;
	private String room;
	private Map<String, String> MUCFromisOpenList;
	private Calendar c = Calendar.getInstance();
	private  Map  ChatMessageQueuee;
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(createLayout());
		 
		Bundle bundle = this.getIntent().getExtras();
		room=bundle.getString("room");
		if(bundle.getBoolean("isServiceSend") ){
			 ChatMessageQueuee= ((AppGlobalVariable) (ChatFormActivity.this).getApplication()).getChatMessageQueuee();
			 
			NotificationManager notificationManager = (NotificationManager) this.getSystemService(  
			          android.content.Context.NOTIFICATION_SERVICE);  
			notificationManager.cancel((Integer) ChatMessageQueuee.get(room+"KEY"));
//			String sender=bundle.getString("sender"  );
//			String msgBody=bundle.getString("msgBody" );
//			String time=bundle.getString("time" );
 
			ChatMessageList= (ArrayList<ChatMessage>) ChatMessageQueuee.get(room );
			ChatMessageQueuee.remove(room);
			ChatMessageQueuee.remove(room+"KEY");
		} 
		final String MUCconference =AppGlobalVariable.conference;
		connection = ((AppGlobalVariable) (ChatFormActivity.this).getApplication()).getConnection();
		username=((AppGlobalVariable) (ChatFormActivity.this).getApplication()).getusername();
		
	    view = (EditText) findViewById(R.id.edit_text_out);
	    
	    
	    adapter=new ChatAdapter(ChatFormActivity.this,ChatMessageList,username);   
	    setListAdapter(adapter);
	    
	    Log.d("TAG","room+ MUCconference "+room+ MUCconference);
	    
	    muc=new MultiUserChat(connection,room+MUCconference);
//	    muc=((AppGlobalVariable) (ChatFormActivity.this).getApplication()).getMUClist().get();
 
	  
			Log.d("TAG","getMembers "+muc.getRoom());
		 
		 
	    muc.addMessageListener(new PacketListener(){

			@Override
			public void processPacket(final Packet arg0) {
				// TODO Auto-generated method stub
				mHandler.post(new Runnable(){
				
					@Override
					public void run() {	 
						Message	msg=((Message)arg0) ;
						Log.d("TAG","REC MUC "+room+" "+msg.toXML());
						refrech(((Message)arg0).getBody(),((Message)arg0).getFrom().substring(((Message)arg0).getFrom().indexOf("/")+1).toString(),username,((Message)arg0).getProperty("time").toString());
//						refrech(msg.getBody(),username.toString(),username);

					}
				});
			}
	    	
	    });
	    
	    mSendButton = (Button) findViewById(R.id.button_send);
	    mSendButton.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	    		mHandler.post(new Runnable(){
	    			@Override
					public void run() {
	    				String time =  getMonth(c ) + " , "+c.get(Calendar.DAY_OF_MONTH)+" , "+c.get(Calendar.HOUR_OF_DAY)+", Time "+c.get(Calendar.MINUTE);
 
	    				Message	msg=new Message(muc.getRoom());
	    				msg.setProperty("time",time);
	    				msg.setBody(view.getText().toString());
	    				msg.setType(Message.Type.groupchat);
			    		try {
							muc.sendMessage( msg );
						} catch (XMPPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} Log.d("TAG","SEND MUC "+room+" "+msg.toXML());
						 view.setText("");
	    		//refrech( view.getText().toString(),  username,  username);
	    			} 
	    		});
	    	}
	    });
	        
 


	    MUCFromisOpenList = ((AppGlobalVariable) (ChatFormActivity.this).getApplication()).getMUCFromisOpenList();
	    MUCFromisOpenList.put(room, room);

	}
	public void refrech(String msgBody,String  sender,String  username,String time){
		Log.d("TAG","msgBody"+msgBody+"sender"+sender);
		ChatMessage msgtemp=new ChatMessage();
		msgtemp.setmsgBody(msgBody);
		msgtemp.setsender(sender);
		msgtemp.settime(time);
		ChatMessageList.add(msgtemp);
		adapter=new ChatAdapter(ChatFormActivity.this,ChatMessageList,username);    
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
//	public void onDestroy(){
//		
//		 super.onDestroy();
//  	}
	public void onStop(){
		 MUCFromisOpenList.remove(room) ;
		 super.onStop();
 	}
    public int createLayout() {
    	 
    	 
        return R.layout.chatform;

    }
    public static String getMonth(Calendar rightNow) {
        String chineseMonth = null;
        
        switch(rightNow.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                chineseMonth = "JANUARY";
                break;
            case Calendar.FEBRUARY:
                chineseMonth = "FEBRUARY";
                break;
            case Calendar.MARCH:
                chineseMonth = "MARCH";
                break;
            case Calendar.APRIL:
                chineseMonth = "APRIL";
                break;
            case Calendar.MAY:
                chineseMonth = "MAY";
                break;
            case Calendar.JUNE:
                chineseMonth = "JUNE";
                break;
            case Calendar.JULY:
                chineseMonth = "JULY";
                break;
            case Calendar.AUGUST:
                chineseMonth = "AUGUST";
                break;
            case Calendar.SEPTEMBER:
                chineseMonth = "SEPTEMBER";
                break;
            case Calendar.OCTOBER:
                chineseMonth = "OCTOBER";
                break;
            case Calendar.NOVEMBER:
                chineseMonth = "NOVEMBER";
                break;
            case Calendar.DECEMBER:
                chineseMonth = "DECEMBER";
                break;                
        }
        
        return chineseMonth;
    }
}
