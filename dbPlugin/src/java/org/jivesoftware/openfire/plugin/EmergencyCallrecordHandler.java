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
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.PacketError;


public class EmergencyCallrecordHandler extends IQHandler {

	// for Debug
	private static final boolean DEBUG = true;
	
	
	// field 
	private static final String moduleName = "EmergencyCallrecord";
	private IQHandlerInfo info;
	private GroupManager gm = GroupManager.getInstance();
	
	private boolean isRelease = false ;
	
	
	//Constructor
	public EmergencyCallrecordHandler() {
		super(moduleName);
		info = new IQHandlerInfo("query", "srlab:group:iq:EmergencyCallrecord");
	}

	
	
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		
		// TODO Time tag implementation 
		final JID sender = packet.getFrom();
		
		Element childEle = packet.getChildElement();
		
		if(DEBUG){
			System.out.println("openfire EmergencyCallrecordHandler received packet: " + packet.toXML());
			System.out.println("packet from " + packet.getFrom());
 
		}
		
		//use IQ info to create group 
 
		 
 

		System.out.println("sender "+sender);
		IQ result = IQ.createResultIQ(packet);
		result.setChildElement("query", "srlab:group:iq:EmergencyCallrecord");
		ArrayList rslist;
		if((rslist=EmergencyCallrecord(sender   ))==null){
			result.setError(PacketError.Condition.not_allowed);
			return result;
		}
		DefaultGroupProvider groupProvider=new DefaultGroupProvider();
		for(int i=0;i<rslist.size();i++){
			Map map=(Map) rslist.get(i);
			Element callElement = DocumentHelper.createDocument().addElement("call");
			try {
				if (groupProvider.getGroup((String) map.get("group")) ==null){
					continue;
				}
			} catch (GroupNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Element groupNameElement = DocumentHelper.createDocument().addElement("group");
			groupNameElement.addText((String) map.get("group"));
			System.out.println((String) map.get("group"));
			Element indexElement = DocumentHelper.createDocument().addElement("index");
			indexElement.addText((String) map.get("index"));
			Element senderElement = DocumentHelper.createDocument().addElement("sender");
			senderElement.addText((String) map.get("sender"));
			Element msgBodyElement = DocumentHelper.createDocument().addElement("msgBody");
			msgBodyElement.addText((String) map.get("msgBody"));
			Element timeElement = DocumentHelper.createDocument().addElement("time");
			timeElement.addText((String) map.get("time"));
			
			callElement.add(groupNameElement);
			callElement.add(indexElement);
			callElement.add(senderElement);
			callElement.add(msgBodyElement);
			callElement.add(timeElement);
			result.getChildElement().add(callElement);
		}
		
	
		 
		
		//create result IQ 
	
 

		if(DEBUG){
			System.out.println("result" + result.toXML());
		}
		
		
		return result;
	}

	
	private ArrayList  EmergencyCallrecord(JID  sender   )   {
		Collection<String> groups=null;
		DefaultGroupProvider groupProvider=new DefaultGroupProvider();
	 
		ArrayList<String>  groupnameList=new ArrayList<String> ()  ;
			groups = groupProvider.getGroupNames(sender);
			for(String groupName:groups){
				  
				Group group=null;
				try {
					group = groupProvider.getGroup(groupName);
					
				} catch (GroupNotFoundException e) {
					return null;
				}
				groupnameList.add(group.getName() );

//				int num=mysql .InsertEmergencyCallTable(group.getName(), sender.getNode(), msgBody,time);
			     
			}
			jdbcmysql mysql=new jdbcmysql();
 
			
		 
			return mysql .selectEmergencyCallrecord(   groupnameList );
	}

	
	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
