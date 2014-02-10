package tw.edu.ntu.csie.srlab.service;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

 
import com.cyrilmottier.android.greendroid.R.string;
 
import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.ChatMessage;
import tw.edu.ntu.csie.srlab.GroupList;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.SMSReceiver;
import tw.edu.ntu.csie.srlab.activity.ChatFormActivity;
import tw.edu.ntu.csie.srlab.activity.EmergencycallActivity;
import tw.edu.ntu.csie.srlab.activity.GroupListActivity;
import tw.edu.ntu.csie.srlab.activity.LoginActivity;
import tw.edu.ntu.csie.srlab.activity.ShowMemberMaps;
import tw.edu.ntu.csie.srlab.aidl.ControlLocationService;
import tw.edu.ntu.csie.srlab.aidl.EmergencycallService;
import tw.edu.ntu.csie.srlab.packet.IQCreateGroup;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroup;
import tw.edu.ntu.csie.srlab.provider.QueryGroupProvider;
 
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class ConnectService extends Service  {
 
	  

		// debugging 
 
		private static final boolean D = true;
 
		private Handler mHandler = new Handler();
		private MultiUserChat muc;
		private String username;
		private static int muci=0;
		private  int j=0;
		private XMPPConnection connection;
		private Map<String,String> MUCFromisOpenList;
//		Map<String,MultiUserChat> MUClist;
		final String SERVER_IP= AppGlobalVariable .SERVER_IP;
		private String MUCconference;
		private  ArrayList<GroupList> group;
		private  Map  ChatMessageQueuee;
//	    private final EmergencycallService.Stub mBinder=new EmergencycallService.Stub(){
//
//
//			@Override
//			public void  SendEmergencycall(String msg)
//					throws RemoteException {
//				for(int i=0;i<group.size();i++){
//					 muc=new MultiUserChat(connection,group.get(i).getgroupname()+ MUCconference);
//					
//					try {
//						muc.sendMessage(msg);
//					 
//					} catch (XMPPException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				 
//				}
//			}
//
//		 
//			
//		};
		  @Override
		  public void onCreate()
		  {  
			
		        username = ((AppGlobalVariable) (ConnectService.this).getApplication()).getusername();
		        MUCconference =AppGlobalVariable.conference;
		        MUCFromisOpenList=((AppGlobalVariable) (ConnectService.this).getApplication()).getMUCFromisOpenList();
		        ChatMessageQueuee= ((AppGlobalVariable) (ConnectService.this).getApplication()).getChatMessageQueuee();
//		         MUClist=((AppGlobalVariable) (ConnectService.this).getApplication()).getMUClist();
		        
		        connection = ((AppGlobalVariable) (ConnectService.this).getApplication()).getConnection();
				IQQueryGroup iq=new IQQueryGroup();
				iq.setisJustName(true);
		        IQ  result =   requestBlocking(iq);
				if(D) Log.d("TAG","result "+((IQQueryGroup) result)  );
				 group=((IQQueryGroup) result) .getgroup();
				for(int i=0;i<group.size();i++){
					 muc=new MultiUserChat(connection,group.get(i).getgroupname()+ MUCconference);
					
					try {
						muc.join(username);
						muc.addMessageListener(new MUCListener());
					} catch (XMPPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//MUCFromisOpenList.put(group.get(i).getgroupname(),group.get(i).getgroupname());
//					MUClist.put(group.get(i).getgroupname(), muc);
				}
				
				MultiUserChat.addInvitationListener(connection, new InvitationListener() {
			          
						//(Connection conn, String room, String inviter, String reason, String password,Message)
									@Override
									public void invitationReceived(Connection arg0, final String arg1,final String arg2,   final String arg3, final String arg4,   Message arg5) {
								     	  
					 
													mHandler.post(new Runnable(){

														@Override
														public void run() {
															String msg=((arg2+" invite you to join group "+arg1).replace(MUCconference, "")).replace("@"+SERVER_IP, "");
															  Notification notification = new Notification(R.drawable.f,  msg, System.currentTimeMillis()); 
															  notification.defaults |= Notification.DEFAULT_ALL;
														 
															  Intent intent= new Intent(ConnectService.this,SMSReceiver.class);
															  intent.setAction("actionstring" + System.currentTimeMillis()); 
															  //intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
															  Bundle bundle = new Bundle();
															  bundle.putString("room", arg1 );
															  bundle.putString("inviter", arg2 );
															  bundle.putString("reason", arg3);
															  bundle.putInt("i",muci);
														      intent.putExtras(bundle);
															 // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
															 PendingIntent contentIntent = PendingIntent.getActivity(ConnectService.this, 0,intent , 0);  
															 
															  notification.setLatestEventInfo(ConnectService.this, msg , arg3, contentIntent);  
															    
															  NotificationManager notificationManager = (NotificationManager) ConnectService.this.getSystemService(  
															          android.content.Context.NOTIFICATION_SERVICE);  
															  Log.d("TAG",""+muci);
															  notificationManager.notify(muci++, notification); 
															  
															  
										  
												    	        												
														}
														
													});
													
										
												 
											 
							
									}
							      });

				connection.addPacketListener(new IQListener() ,new PacketTypeFilter(IQ.class));
 
				 IntentFilter filter = new IntentFilter();
		         filter.addAction(Intent.ACTION_SCREEN_ON);
		       
//		         filter.addAction("android.intent.action.ACTION_SCREEN_ON");
		         registerReceiver(ScreenONReceiver, filter);
		       

		    
		}

		  private class MUCListener implements PacketListener {

			@Override
			public void processPacket(final Packet arg0) {
				mHandler.post(new Runnable(){

					@Override
					public void run() {
						if(arg0 instanceof Message) {
							Log.d("TAG","REC service MUC "+((Message)arg0).toXML());
							//filter start string "the anyxxxroom"
							if (   ((Message)arg0).getFrom().indexOf("/") !=-1){
								if(((Message) arg0).getSubject()!=null&&((Message)arg0).getSubject().equals("EmergencyCall")){
									Message message = (Message) arg0;
						 

									if (message.getBody() != null) {
										DelayInformation inf = (DelayInformation) message.getExtension(
												"x", "jabber:x:delay");
										 
										if (inf != null) {
											return;
										}  
									}
									int endlocation=((Message)arg0).getFrom().indexOf( "/" );
									String sender=((Message)arg0) .getFrom().substring(endlocation+1, ((Message)arg0) .getFrom().length() ); 
									String msgBody=((Message)arg0).getBody();
									Calendar c = Calendar.getInstance();
									 String time =  ChatFormActivity.getMonth(c)+ " , "+c.get(Calendar.DAY_OF_MONTH)+", Time "+c.get(Calendar.HOUR_OF_DAY)+" : "+c.get(Calendar.MINUTE);
									 Notification notification = new Notification(R.drawable.f,"EmergencyCall"  , System.currentTimeMillis()); 
									 notification.defaults |= Notification.DEFAULT_ALL;
									 
									 Intent intent= new Intent(ConnectService.this,ShowMemberMaps.class);
									 intent.setAction("actionstring" + System.currentTimeMillis()); 
//									 intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
									 Bundle bundle = new Bundle();
									 bundle.putString("time", time );
									 bundle.putString("sender", sender );
									 bundle.putString("msgBody",msgBody );
									 bundle.putString("EmergencyCall","EmergencyCall");

									 bundle.putInt("j",j);
									 intent.putExtras(bundle);
																			 // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								    PendingIntent contentIntent = PendingIntent.getActivity(ConnectService.this, 0,intent , 0);  
																			 
									notification.setLatestEventInfo(ConnectService.this, sender ,   msgBody, contentIntent);  
																			    
									NotificationManager notificationManager = (NotificationManager) ConnectService.this.getSystemService(  
																			          android.content.Context.NOTIFICATION_SERVICE);  
									Log.d("TAG",""+j );
									notificationManager.notify(j , notification); 
								}else{
							
									int endlocation=((Message)arg0).getFrom().indexOf( MUCconference );
									String room=((Message)arg0) .getFrom().substring(0,endlocation );
									String msgBody=((Message)arg0).getBody() ;
									endlocation=((Message)arg0).getFrom().indexOf( "/" );
									Calendar c = Calendar.getInstance();
									String time =  ChatFormActivity.getMonth(c)+ " , "+c.get(Calendar.DAY_OF_MONTH)+", Time "+c.get(Calendar.HOUR_OF_DAY)+" : "+c.get(Calendar.MINUTE);
									String sender=((Message)arg0) .getFrom().substring(endlocation+1, ((Message)arg0) .getFrom().length() ); 	
									Log.d("TAG","REC service MUC "+sender);
									Log.d("TAG","REC service MUCFromisOpenList.get( room ) "+MUCFromisOpenList.get( room ));
									if (MUCFromisOpenList.get( room )==null){
										
										 Notification notification = new Notification(R.drawable.f,room+ " Message" , System.currentTimeMillis()); 
	//									  notification.defaults |= Notification.DEFAULT_ALL;
									 
										  Intent intent= new Intent(ConnectService.this,ChatFormActivity.class);
										  intent.setAction("actionstring" + System.currentTimeMillis()); 
										  //intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
										  Bundle bundle = new Bundle();
										  bundle.putBoolean("isServiceSend",true);
										  bundle.putString("room", room );
//										  bundle.putString("time", time );
//										  bundle.putString("sender", sender );
//										  bundle.putString("msgBody", msgBody);
										  int  ChatMessagesize = 0;
										  if( ChatMessageQueuee.get(room)!=null){
											
											 ArrayList<ChatMessage> tmpChatMessageList=  (ArrayList<ChatMessage>) ChatMessageQueuee.get(room) ;
											 tmpChatMessageList .add(new ChatMessage(  msgBody,  time ,  sender));
											  
											 ChatMessagesize=tmpChatMessageList.size();
											 
										  }else{
				 
												 ArrayList<ChatMessage> tmpChatMessageList=new ArrayList<ChatMessage>();
												 tmpChatMessageList .add(new ChatMessage(  msgBody,  time ,  sender));
												 ChatMessageQueuee.put(room, tmpChatMessageList);
												 ChatMessageQueuee.put(room+"KEY", j);
											   
										  }
										  //bundle.putInt("j", (Integer) ChatMessageQueuee.put(room+"KEY", j));
									      intent.putExtras(bundle);
										 // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										 PendingIntent contentIntent = PendingIntent.getActivity(ConnectService.this, 0,intent , 0);  
										 if(ChatMessagesize==0){
											 notification.setLatestEventInfo(ConnectService.this, room+ " Message"  , sender +" : "+msgBody, contentIntent);  

										 }else{
											 notification.setLatestEventInfo(ConnectService.this, room+ " Message "+"("+ChatMessagesize+")" , sender +" : "+msgBody, contentIntent);  

										 }
										  NotificationManager notificationManager = (NotificationManager) ConnectService.this.getSystemService(  
										          android.content.Context.NOTIFICATION_SERVICE);  
										 
										  if(ChatMessagesize==0){
											  notificationManager.notify(j++, notification);
									 
										  }else{
											  notificationManager.notify((Integer) ChatMessageQueuee.get(room+"KEY" ), notification);
											   
										  }
									}
								}
							}
						}
					}
					
				}); 
			}
			  
		  }
		  
		  private class IQListener implements PacketListener {
	 

				@Override
				public void processPacket(Packet arg0) {
					 if (D)Log.i("TAG","REC : "+arg0.toXML());
			
				}
			}
		 
			private IQ requestBlocking(IQ request) {
				PacketCollector collector = connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
				connection.sendPacket(request);
				IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
				collector.cancel();
				return response;
			}
	 
  
			public static  void showToast(Context context,String str){
				Toast.makeText(context , str, Toast.LENGTH_SHORT).show();

			}
			public  BroadcastReceiver  ScreenONReceiver = new BroadcastReceiver() {
			 	
		    	@Override
		    	public void onReceive(Context context, Intent intent) {
		    		 if (D)Log.i("TAG","ScreenOffReceiver : ");
		    		 
		    			Intent screenintent=new Intent();
		    			screenintent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
		    			screenintent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
		    			screenintent.setClass(ConnectService.this ,EmergencycallActivity.class);
		    			
		    			startActivity(screenintent);
		    		 
		    		
		    	}
			};
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	  public void onDestroy()
	  {	
			NotificationManager notificationManager = (NotificationManager) this.getSystemService(  
			          android.content.Context.NOTIFICATION_SERVICE);  
			notificationManager.cancelAll();
		  unregisterReceiver(ScreenONReceiver);
	    super.onDestroy();
	  }
}
