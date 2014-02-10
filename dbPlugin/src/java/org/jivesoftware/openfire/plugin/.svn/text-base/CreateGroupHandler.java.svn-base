package org.jivesoftware.openfire.plugin;


import java.util.Map;

import org.jivesoftware.openfire.group.DefaultGroupProvider;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.group.GroupManager;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.vcard.VCardManager;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;


public class CreateGroupHandler extends IQHandler {

	// for Debug
	private static final boolean DEBUG = true;
	
	
	// field 
	private static final String moduleName = "createGroup";
	private IQHandlerInfo info;
	private GroupManager gm = GroupManager.getInstance();
	
	private boolean iscreate = false ;
	
	
	//Constructor
	public CreateGroupHandler() {
		super(moduleName);
		info = new IQHandlerInfo("query", "srlab:group:iq:createGroup");
	}

	
	
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		
		// TODO Time tag implementation 
		final JID sender = packet.getFrom();
		
		Element childEle = packet.getChildElement();
		
		if(DEBUG){
			System.out.println("openfire IQCreateGroupHandler received packet: " + packet.toXML());
			System.out.println("packet from " + packet.getFrom());
			System.out.println(childEle.elementText("GroupName"));
			System.out.println(childEle.elementText("Description"));
			System.out.println(childEle.elementText("StartTime"));
			System.out.println(childEle.elementText("EndTime"));
		}
		
		//use IQ info to create group 
		String groupName = childEle.elementText("GroupName");
		String description = childEle.elementText("Description");
		String startTime = childEle.elementText("StartTime");
		String endTime = childEle.elementText("EndTime");

		System.out.println("sender "+sender);
		//try to create group 
		try {
			iscreate=creatGroup(groupName,description,startTime,endTime,sender);
			System.out.println("iscreate "+iscreate);
		} catch (GroupAlreadyExistsException e) {
			e.printStackTrace();
		 
			IQ result = IQ.createResultIQ(packet);
			result.setError(PacketError.Condition.bad_request);
			 
			return result;
		}  
		
		
		
		
		//create result IQ 
		IQ result = IQ.createResultIQ(packet);
		if (!iscreate){
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

	
	private boolean creatGroup(String igroupName,String description, String istartTime, String iendTime,JID user) throws GroupAlreadyExistsException  {
		Group group=null;
		DefaultGroupProvider groupProvider=new DefaultGroupProvider();
	
			group = groupProvider.createGroup(igroupName);
			if (group==null){
				return false;
			}	 
			
			Map<String,String> gMap = group.getProperties();
			gMap.put("Description",description);
			gMap.put("StartTime",istartTime);
			gMap.put("EndTime", iendTime);
//			VCardManager vcardm=new VCardManager();
//			 
//			Element upload_link = DocumentHelper.createDocument().addElement(igroupName+"_upload_link");
//			upload_link.addText(""+true);
//			Element download_link = DocumentHelper.createDocument().addElement(igroupName+"_download_link");
//			download_link.addText(""+true);
//			Element vcardele=vcardm.getVCard(user.getNode());
//			vcardele.add(upload_link);
//			vcardele.add(download_link); 
//			try {
//				vcardm.setVCard(user.getNode(), vcardele);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			//gMap.put("Uploadloc", "1");
			//gMap.put("Downloadloc", "1");
			groupProvider.addMember(igroupName, user , false);
			return true;
	}

	
	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
