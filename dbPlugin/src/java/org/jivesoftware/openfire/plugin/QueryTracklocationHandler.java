package org.jivesoftware.openfire.plugin;


import java.util.ArrayList;
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
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;


public class QueryTracklocationHandler extends IQHandler {

	// for Debug
	private static final boolean DEBUG = true;
	
	
	// field 
	private static final String moduleName = "queryTracklocation";
	private IQHandlerInfo info;
	private GroupManager gm = GroupManager.getInstance();
	
 
	
	//Constructor
	public QueryTracklocationHandler() {
		super(moduleName);
		info = new IQHandlerInfo("query", "srlab:group:iq:queryTracklocation");
	}

	
	
	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		
		// TODO Time tag implementation 
		final JID sender = packet.getFrom();
		
 
		
		if(DEBUG){
			System.out.println("openfire QueryTracklocationHandler received packet: " + packet.toXML());
			System.out.println("packet from " + packet.getFrom());
 
 
		}
 
		int  index=Integer.parseInt(packet.getChildElement().elementText("index"));
		//create result IQ 
		IQ result = queryTracklocation(index,packet );
 
		
		if(DEBUG){
			System.out.println("result " + result.toXML());
		}
		
		
		return result;
	}

	
	private IQ queryTracklocation( int  index,IQ packet )    {
 
		
		
		DefaultGroupProvider groupProvider=new DefaultGroupProvider();
		//get groups name
		jdbcmysql mysql=new jdbcmysql();
		ArrayList <Map> maplist=mysql.SelectEmergencyCallTable(index);
		IQ result = IQ.createResultIQ(packet);
 
		result.setChildElement("query", "srlab:group:iq:queryTracklocation");
		int k =0;
		

	    	  for(int i =0;i<maplist.size();i++){
	    		  Element TrackElement=  DocumentHelper.createDocument().addElement(  "TrackPoint");
	    		 
	    		   Element indexElement=  DocumentHelper.createDocument().addElement(  "index"  );
	    		   indexElement.setText((String) maplist.get(i).get("index"));
	    		   Element locationElement=  DocumentHelper.createDocument().addElement(  "Location");
	    		   locationElement.setText((String) maplist.get(i).get("Location"));
	    		   Element TimeStampElement=  DocumentHelper.createDocument().addElement(  "TimeStamp");
	    		   TimeStampElement.setText((String) maplist.get(i).get("TimeStamp"));
	    		   
	    		   TrackElement.add(indexElement);
	    		   TrackElement.add(locationElement);
	    		   TrackElement.add(TimeStampElement);
	    		   result.getChildElement().add(TrackElement);
	    		   
	           }
	    	   
	    	
		 
		
		 
		return result;
	}

	
	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

}
