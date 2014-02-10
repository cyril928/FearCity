package org.jivesoftware.openfire.plugin;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.muc.ForbiddenException;
import org.jivesoftware.openfire.muc.HistoryStrategy;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.jivesoftware.openfire.muc.spi.LocalMUCRoom;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketError;
import org.xmpp.packet.PacketExtension;


public class EmergencyCallHandler extends IQHandler {

	// for Debug
	private static final boolean DEBUG = true;
	
	
	// field 
	private static final String moduleName = "EmergencyCall";
	private IQHandlerInfo info;
 
	
	private boolean isRelease = false ;
	
	
	//Constructor
	public EmergencyCallHandler() {
		super(moduleName);
		info = new IQHandlerInfo("query", "srlab:group:iq:EmergencyCall");
	}

	
	
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		
		// TODO Time tag implementation 
		final JID sender = packet.getFrom();
		
		Element childEle = packet.getChildElement();
		
		if(DEBUG){
			System.out.println("openfire EmergencyCallHandler received packet: " + packet.toXML());
			System.out.println("packet from " + packet.getFrom());
 
		}
		
		//use IQ info to create group 
 
		 
		String msgBody = childEle.elementText("msgBody");
		 

		System.out.println("sender "+sender);
		isRelease=EmergencyCall(sender,msgBody);
		System.out.println("isRelease "+isRelease);  
		
		
		
		
		//create result IQ 
		IQ result = IQ.createResultIQ(packet);
		if (!isRelease){
			result.setError(PacketError.Condition.not_allowed);
		}
 

		if(DEBUG){
			System.out.println("result" + result.toXML());
		}
		
		
		return result;
	}

	
	private boolean EmergencyCall(JID  sender , String msgBody)   {
		Collection<String> groups=null;
		DefaultGroupProvider groupProvider=new DefaultGroupProvider();
		ArrayList<String> groupnameList=new ArrayList<String>();

			jdbcmysql mysql=new jdbcmysql();
			groups = groupProvider.getGroupNames(sender);
			for(String groupName:groups){
				  
				Group group=null;
				try {
					group = groupProvider.getGroup(groupName);
				} catch (GroupNotFoundException e) {
					return false;
				}
 
			
				groupnameList.add(group.getName());


 
				
			}
			Date today=new Date();
		    String time=new  Timestamp(today.getTime() ).toString();
		    mysql .InsertEmergencyCallTable(groupnameList, sender.getNode(), msgBody,time);
			if(DEBUG){System.out.println("indexnum " + groupnameList.size()); }

		    MultiUserChatService  mucservice=XMPPServer.getInstance().getMultiUserChatManager().getMultiUserChatService("conference");
			//mucservice.getHistoryStrategy().setType(HistoryStrategy.Type.none); 
			if(DEBUG){System.out.println("MultiUserChatService " + mucservice); }
		    if(groupnameList.size()>0&&mucservice!=null){
		    	/*for(MUCRoom MUCRoomname:mucservice.getChatRooms()){
		    		if(DEBUG){System.out.println("MUCRoomname " + MUCRoomname.getName()); }
		    	}*/
		    	for(int i=0 ;i<groupnameList.size() ;i++){
					MUCRoom mucroom=mucservice.getChatRoom(groupnameList.get(i)); 
					// if(mucroom!=null){		if(DEBUG){System.out.println("mucroom!=null "+mucroom.getName()); }
					Message msg=new Message();
		 
					msg.setSubject("EmergencyCall");
					msg.setFrom(mucroom.getJID()+"/"+sender.getNode());
					msg.setBody(msgBody);
					 msg.setType(Message.Type.groupchat);
 					//msg.addExtension(new PacketExtension("index", ""+indexnum.get(i)));
					
					if(DEBUG){System.out.println("msg.toXML() " + msg.toXML()); }

 					mucroom.send(msg); 
 					
		    	}
		    	return true;
		    }
		 
		     
			
		 
		    return false;
	}

	
	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
