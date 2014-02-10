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
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.vcard.VCardManager;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;


public class QueryGroupMemberLocationHandler extends IQHandler {

	// for Debug
	private static final boolean DEBUG = true;
	
	
	// field 
	private static final String moduleName = "queryGroupMemberLocation";
	private IQHandlerInfo info;
	private GroupManager gm = GroupManager.getInstance();
	
 
	
	//Constructor
	public QueryGroupMemberLocationHandler() {
		super(moduleName);
		info = new IQHandlerInfo("query", "srlab:group:iq:queryGroupMemberLocation");
	}

	
	
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		
		// TODO Time tag implementation 
		final JID sender = packet.getFrom();
		
 
		
		if(DEBUG){
			System.out.println("openfire GroupMemberLocationHandler received packet: " + packet.toXML());
			System.out.println("packet from " + packet.getFrom());
 
 
		}
 
	 
		
		//create result IQ 
		IQ result = QueryLocation(sender,packet);
 
		
		if(DEBUG){
			System.out.println("result " + result.toXML());
		}
		
		
		return result;
	}

	//username>group>group member>session>location
	private IQ QueryLocation( JID jid,IQ packet )    {
 
		
		
		DefaultGroupProvider groupProvider=new DefaultGroupProvider();
		//get groups name
		Collection<String> groups=groupProvider .getGroupNames(jid);
		
		IQ result = IQ.createResultIQ(packet);

		result.setChildElement("query", "srlab:group:iq:queryGroupMemberLocation");
		String username=packet.getChildElement().elementText("UserName");
		if(username!=null){		 
			try {
				if( XMPPServer.getInstance().getUserManager().getUser(username)!=null){
				jdbcmysql mysql=new jdbcmysql();
				Map map=mysql.SelectTable(username);
				Element groupElement = DocumentHelper.createDocument().addElement("User");
				Element groupusernameElement = DocumentHelper.createDocument().addElement("UserName");
				groupusernameElement.addText(map.get("UserName").toString());
				System.out.println("UserName " + map.get("UserName").toString());
				Element groupTimeStampElement = DocumentHelper.createDocument().addElement("TimeStamp");
				groupTimeStampElement.addText(map.get("TimeStamp").toString());
				System.out.println("TimeStamp " + map.get("TimeStamp").toString());
				Element groupLocationElement = DocumentHelper.createDocument().addElement("Location");
				groupLocationElement.addText(map.get("Location").toString());
				System.out.println("Location " + map.get("Location").toString());
				groupElement.add(groupusernameElement);
				groupElement.add(groupTimeStampElement);
				groupElement.add(groupLocationElement);
				result.getChildElement().add(groupElement);
				}
			} catch (UserNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			
		VCardManager vcardm = new VCardManager();
		
		for(String group : groups) {  
			
			System.out.println("group" + group);
		
		Element tempele = vcardm.getVCard(jid.getNode());
 
			//CHECK WHICH GROUP NEED TO DOWNLOAD GROUP MEMEBER LOCATION
			if((tempele.elementText(group.toString()+"_download_link").equals(""+true))) {
				
				Collection<JID> users=null;
				try {
					users = groupProvider .getGroup(group).getMembers();
				} catch (GroupNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(JID user : users) {
					//CHECK WHICH MEMEBER SETTING FOR OTHER USER DOWNLOAD HIMSELF LOCATION
					Element vcardele=vcardm.getVCard(user.getNode());
 
					if(!(user.equals(jid))  &&	 (vcardele.elementText(group.toString()+"_upload_link").equals(""+true)) ){
						
				
						ClientSession clientsession=XMPPServer.getInstance().getSessionManager().getSession(new JID(user.toString()+"/Smack"));
					 
					if(clientsession!=null){
						Element groupElement = DocumentHelper.createDocument().addElement("User");

						jdbcmysql mysql=new jdbcmysql();
						Map map=mysql.SelectTable(user.getNode());
						
						Element groupusernameElement = DocumentHelper.createDocument().addElement("UserName");
						groupusernameElement.addText(map.get("UserName").toString());
						System.out.println("UserName " + map.get("UserName").toString());
						Element groupTimeStampElement = DocumentHelper.createDocument().addElement("TimeStamp");
						groupTimeStampElement.addText(map.get("TimeStamp").toString());
						System.out.println("TimeStamp " + map.get("TimeStamp").toString());
						Element groupLocationElement = DocumentHelper.createDocument().addElement("Location");
						groupLocationElement.addText(map.get("Location").toString());
						System.out.println("Location " + map.get("Location").toString());
						groupElement.add(groupusernameElement);
						groupElement.add(groupTimeStampElement);
						groupElement.add(groupLocationElement);
						result.getChildElement().add(groupElement);
					}
 
				}
				}
				
			}//	end of if(tempele.elementText(group.toString()+"_download_link").equals(""+true)) {
		}
	}
		
		 
		return result;
	}

	
	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
