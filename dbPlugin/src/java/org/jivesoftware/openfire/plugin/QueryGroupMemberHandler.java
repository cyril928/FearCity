package org.jivesoftware.openfire.plugin;


import java.util.Collection;
import java.util.Map;

import org.jivesoftware.openfire.group.DefaultGroupProvider;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.user.PresenceEventDispatcher;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;


public class QueryGroupMemberHandler extends IQHandler {

	// for Debug
	private static final boolean DEBUG = true;
	
	
	// field 
	private static final String moduleName = "queryGroupMember";
	private IQHandlerInfo info;
	private GroupManager gm = GroupManager.getInstance();
 
 
	
 

	
	
	public QueryGroupMemberHandler( ) {
		super(moduleName);
		info = new IQHandlerInfo("query", "srlab:group:iq:queryGroupMember");
 
	}



	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		
		// TODO Time tag implementation 
		final JID sender = packet.getFrom();
		
 
		
		if(DEBUG){
			System.out.println("openfire QueryGroupMemberHandler received packet: " + packet.toXML());
			System.out.println("packet from " + packet.getFrom());
 
 
		}
 
 
		if(DEBUG)System.out.println("result" +11);
		String GroupName=packet.getChildElement().elementText("GroupName");
		if(DEBUG)System.out.println(GroupName);
		//create result IQ 
		IQ result = QueryGroupMember(GroupName,packet);
 
		
		if(DEBUG){
			System.out.println("result " + result.toXML());
		}
		
		
		return result;
	}

	
	private IQ QueryGroupMember( String GroupName,IQ packet)    {
 
		
		
		DefaultGroupProvider groupProvider=new DefaultGroupProvider();
		//get groups name
		Collection<JID> users=null;
		try {
			users = groupProvider .getGroup(GroupName).getMembers();
		} catch (GroupNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IQ result = IQ.createResultIQ(packet);
 
		result.setChildElement("query", "srlab:group:iq:queryGroupMember");
		for(JID user : users) {  //System.out.println("group" + group);
				Element groupElement = DocumentHelper.createDocument().addElement("GroupMember");
				 
		 
				Element UserNameElement = DocumentHelper.createDocument().addElement("UserName");
				UserNameElement.addText(user.getNode());
				
				Element UserPresenceElement = DocumentHelper.createDocument().addElement("Presence");
				
				if(DEBUG)System.out.println("user "+user.toBareJID());
				 ClientSession clientsession=XMPPServer.getInstance().getSessionManager().getSession(new JID(user.toString()+"/Smack"));
					String UserStatus="online";
					if(clientsession==null){
						UserStatus="offline";
					}
		 
				if(DEBUG){
					
					 
							 
									//System.out.println(XMPPServer.getInstance().getSessionManager().getSession(user) );
									System.out.println(UserStatus);
		 
				}

				 
					UserPresenceElement.addText( UserStatus);
 
				 
				
				groupElement.add(UserNameElement);
				groupElement.add(UserPresenceElement);
		 ;
				 
				result.getChildElement().add(groupElement);
		}
		
		 
		return result;
	}

	
	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
