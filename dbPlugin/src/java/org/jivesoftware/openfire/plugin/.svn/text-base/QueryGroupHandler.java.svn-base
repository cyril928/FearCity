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
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.vcard.VCardManager;
 
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;


public class QueryGroupHandler extends IQHandler {

	// for Debug
	private static final boolean DEBUG = true;
	
	
	// field 
	private static final String moduleName = "queryGroup";
	private IQHandlerInfo info;
	private GroupManager gm = GroupManager.getInstance();
	
 
	
	//Constructor
	public QueryGroupHandler() {
		super(moduleName);
		info = new IQHandlerInfo("query", "srlab:group:iq:queryGroup");
	}

	
	
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		
		// TODO Time tag implementation 
		final JID sender = packet.getFrom();
		
 
		
		if(DEBUG){
			//System.out.println("openfire QueryGroupHandler ");
			System.out.println("openfire QueryGroupHandler received packet: " + packet.toXML());
			System.out.println("packet from " + packet.getFrom());
 
 
		}
 
		boolean isJustName=(packet.getChildElement().element("isJustName").equals(true))?true:false;
		System.out.println("result" +11);
		
		//create result IQ 
		IQ result = QueryGroup(sender,packet,isJustName);
 
		
		if(DEBUG){
			System.out.println("result " + result.toXML());
		}
		
		
		return result;
	}

	
	private IQ QueryGroup( JID user,IQ packet,boolean isJustName)    {
 
		
		
		DefaultGroupProvider groupProvider=new DefaultGroupProvider();
		//get groups name
		Collection<String> groups=groupProvider .getGroupNames(user);
		
		IQ result = IQ.createResultIQ(packet);
		//CHECK VCARD
		VCardManager vcardm=new VCardManager();
		Element vcardele=vcardm.getVCard(user.getNode());
		System.out.println("vcard Element"+vcardele.asXML().toString());
		
		result.setChildElement("query", "srlab:group:iq:queryGroup");
		for(String group : groups) {  System.out.println("group" + group);
				Element groupElement = DocumentHelper.createDocument().addElement("Group");
				
			 
				Group agroup = null;
				try {
					agroup = groupProvider.getGroup(group.toString());
				} catch (GroupNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 
				
				
				Element groupNameElement = DocumentHelper.createDocument().addElement("GroupName");
				groupNameElement.addText(group);
				groupElement.add(groupNameElement);
		 

 
				if(!isJustName){
					Map<String,String> map=agroup.getProperties();
					Element groupDescriptionElement = DocumentHelper.createDocument().addElement("Description");
					groupDescriptionElement.addText(map.get("Description"));
					
					Element groupStartTimeElement = DocumentHelper.createDocument().addElement("StartTime");
					groupStartTimeElement.addText(map.get("StartTime"));
				
					Element groupEndTimeElement = DocumentHelper.createDocument().addElement("EndTime");
					groupEndTimeElement.addText(map.get("EndTime"));
					
					Element groupUploadlocElement = DocumentHelper.createDocument().addElement("Uploadloc");
					
//					if(vcardele.elementText(group.toString()+"_upload_link")==null){
//						groupUploadlocElement.addText(""+true);
//					}else {
						groupUploadlocElement.addText(vcardele.elementText(group.toString()+"_upload_link"));
//					}
					
				
					Element groupDownloadlocElement = DocumentHelper.createDocument().addElement("Downloadloc");
					
//					if(vcardele.elementText(group.toString()+"_download_link")==null){
//						groupDownloadlocElement.addText(""+true);
//					}else {
						groupDownloadlocElement.addText(vcardele.elementText(group.toString()+"_download_link"));
//					}
					
					groupElement.add(groupDescriptionElement);
					groupElement.add(groupStartTimeElement);
					groupElement.add(groupEndTimeElement);
					groupElement.add(groupUploadlocElement);
					groupElement.add(groupDownloadlocElement);
				}
				result.getChildElement().add(groupElement);
		}
		
		 
		return result;
	}

	
	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
