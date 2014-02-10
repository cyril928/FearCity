package org.jivesoftware.openfire.plugin;


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
import org.jivesoftware.openfire.muc.MUCRole;
import org.jivesoftware.openfire.muc.MUCRoom;
import org.jivesoftware.openfire.muc.MultiUserChatService;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.vcard.VCardManager;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;


public class LeaveGroupHandler extends IQHandler {

	// for Debug
	private static final boolean DEBUG = true;
	 MultiUserChatService  mucservice=XMPPServer.getInstance().getMultiUserChatManager().getMultiUserChatService("conference");
	
	// field 
	private static final String moduleName = "leaveGroup";
	private IQHandlerInfo info;
	private GroupManager gm = GroupManager.getInstance();
	
	private boolean isleave = false ;
	
	
	//Constructor
	public LeaveGroupHandler() {
		super(moduleName);
		info = new IQHandlerInfo("query", "srlab:group:iq:leaveGroup");
	}

	
	
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		
		// TODO Time tag implementation 
		final JID sender = packet.getFrom();
		
		Element childEle = packet.getChildElement();
		
		if(DEBUG){
			System.out.println("openfire IQLeaveGroupHandler received packet: " + packet.toXML());
			System.out.println("packet from " + packet.getFrom());
			System.out.println(childEle.elementText("GroupName"));
 
		}
		
		//use IQ info to leave group 
		String groupName = childEle.elementText("GroupName");
 

		
		//try to leave group 
 
			try {
				isleave =leaveGroup(groupName,sender );
			} catch (GroupNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				IQ result = IQ.createResultIQ(packet);
				result.setError(PacketError.Condition.bad_request);
				return result;
			 
			}
		 
	   
		
		
		
		
		//create result IQ 
		IQ result = IQ.createResultIQ(packet);
		if (!isleave){
			result.setError(PacketError.Condition.not_allowed);
		}
//		Document document1 = DocumentHelper.createDocument();
//		Document document2 = DocumentHelper.createDocument();
//		
//		Element groupNameElement = document1.addElement("requestGroupName");
//		groupNameElement.addText(groupName);
//		
//		Element groupExistElement = document2.addElement("state");
//		if (!groupExist)
//			groupExistElement.addText("create success");
//		else
//			groupExistElement.addText("group has exist!!");
//	
//		
//	
//		result.setChildElement("query", "srlab:group:iq:createGroup");
//		result.getChildElement().add(groupNameElement);
//		result.getChildElement().add(groupExistElement);

		if(DEBUG){
			System.out.println("result" + result.toXML());
		}
		
		
		return result;
	}

	
	private boolean leaveGroup(String igroupName,JID user) throws  GroupNotFoundException  {
 
		 
		
			DefaultGroupProvider groupProvider=new DefaultGroupProvider();
			if (groupProvider.getGroup(igroupName).isUser(user)){
				groupProvider.deleteMember(igroupName, user);
				MUCRoom mucroom=mucservice.getChatRoom(igroupName); 
				try {
					mucroom.leaveRoom(mucroom.getOccupant(user.getNode()) );
				} catch (UserNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
					if(groupProvider.getGroup(igroupName).getMembers().size()==0){
						groupProvider.deleteGroup(igroupName);
						mucservice.removeChatRoom(igroupName);
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
