package tw.edu.ntu.csie.srlab;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import greendroid.app.GDApplication;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
 
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.packet.VCard;
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
 

import tw.edu.ntu.csie.srlab.activity.LoginActivity;
import tw.edu.ntu.csie.srlab.activity.ShowMemberMaps;
import tw.edu.ntu.csie.srlab.provider.QueryEmergencyRecordCallProvider;
import tw.edu.ntu.csie.srlab.provider.QueryGroupMemberLocationProvider;
import tw.edu.ntu.csie.srlab.provider.QueryGroupMemberProvider;
import tw.edu.ntu.csie.srlab.provider.QueryGroupProvider;
import tw.edu.ntu.csie.srlab.provider.QueryTrackLocationProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

 
 

public class AppGlobalVariable extends GDApplication {

	// debugging 
	private static final String TAG = "AppGlobalVariable"; 
	private static final boolean D = true;
	public static final String  conference="@conference.taylor19882002.no-ip.org";
	public  XMPPConnection connection ;
	public static final String SERVER_IP = "taylor19882002.no-ip.org";
 
	private String username="";
	private  Map<String,String> MUCFromisOpenList=  new   HashMap<String,String> ();
	private Context context;
	private  Map  ChatMessageQueuee=new HashMap  ();
 
	// for whole application's xmpp connection
	
	public Map  getChatMessageQueuee() {
		return this.ChatMessageQueuee;	
	}
	public XMPPConnection getConnection() {
		return this.connection;	
	}
 
	
	public Map<String,String> getMUCFromisOpenList() {
		return this.MUCFromisOpenList;	
	}
	public void setMUCFromisOpenList(Map<String,String> MUCFromisOpenList) {
		this.MUCFromisOpenList=MUCFromisOpenList;	
	}
	
	public String getusername() {
		return username;	
	}
	public void setusername(String username) {
		this.username=username;	
	}
	
	// for whole application's current running context
	public Context getContext() {
		return this.context;	
	}
	
	public void setContext(Context context) {
		this.context = context;
	}

	public ConnectionListener connectionListener = new ConnectionListener(){
		
		@Override
		public void connectionClosed() {
			// TODO Auto-generated method stub
			if(D) Log.d(TAG,"++++ connectionClosed +++++");
 
			
			// back to the login view
			//((Activity) context).finish();
		}

		@Override
		public void connectionClosedOnError(Exception e) {
			// TODO Auto-generated method stub
			if(D) Log.d(TAG,"++++ connectionClosedOnError +++++");
			
 
//			Toast.makeText(AppGlobalVariable.this, e.toString(), Toast.LENGTH_SHORT).show();
			// back to the login view
			Intent loginIntent = new Intent(AppGlobalVariable.this, LoginActivity.class);
	    	startActivity(loginIntent); 
		}

		@Override
		public void reconnectingIn(int seconds) {
			// TODO Auto-generated method stub
 		}

		@Override
		public void reconnectionFailed(Exception e) {
			// TODO Auto-generated method stub
			if(D) Log.d(TAG,"++++ reconnectionFailed +++++");
			
 
 			// back to the login view
			Intent loginIntent = new Intent(AppGlobalVariable.this, LoginActivity.class);
	    	startActivity(loginIntent); 
		}

		@Override
		public void reconnectionSuccessful() {
			// TODO Auto-generated method stub
			
		}

 
		
	};
	
	public IQ requestBlocking(IQ request) {
		PacketCollector collector = connection.createPacketCollector(new PacketIDFilter(request.getPacketID()));
		connection.sendPacket(request);
		IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		collector.cancel();
		return response;
	}
	public XMPPConnection connectionSetup(){

		final ConnectionConfiguration connectionConfig = new ConnectionConfiguration(
				SERVER_IP, 5222, "");
		connection = new XMPPConnection(connectionConfig);
	      XMPPConnection.DEBUG_ENABLED = true;  
		ProviderManager pm = ProviderManager.getInstance();
		configure(pm);
		ProviderManager.getInstance().addIQProvider(  "query", "srlab:group:iq:queryGroup", new QueryGroupProvider());
		ProviderManager.getInstance().addIQProvider(  "query", "srlab:group:iq:queryGroupMember", new QueryGroupMemberProvider());
		ProviderManager.getInstance().addIQProvider(  "query", "srlab:group:iq:queryGroupMemberLocation", new QueryGroupMemberLocationProvider());
		ProviderManager.getInstance().addIQProvider(  "query", "srlab:group:iq:EmergencyCallrecord", new QueryEmergencyRecordCallProvider());
		ProviderManager.getInstance().addIQProvider(  "query", "srlab:group:iq:queryTracklocation", new QueryTrackLocationProvider());
 
    	return connection;
	}
	

    @Override
    public Class<?> getHomeActivityClass() {
        return ShowMemberMaps.class;
    }
    @Override
    public Intent getMainApplicationIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_url)));
    }
	public static void configure(ProviderManager pm) {

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time", Class
					.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());

		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());

		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());

		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());

		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version", Class
					.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());

		// Last Activity
		pm
				.addIQProvider("query", "jabber:iq:last",
						new LastActivity.Provider());

		// User Search
		pm
				.addIQProvider("query", "jabber:iq:search",
						new UserSearch.Provider());

		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());

		//pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
//				new BytestreamsProvider());

		// pm.addIQProvider("open", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Open());
		//
		// pm.addIQProvider("close", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Close());
		//
		// pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Data());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());

		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}
}
