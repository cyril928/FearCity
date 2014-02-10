
package org.jivesoftware.openfire.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.PacketDeliverer;
import org.jivesoftware.openfire.PacketException;
import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.group.DefaultGroupProvider;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupAlreadyExistsException;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.group.JDBCGroupProvider;
import org.jivesoftware.openfire.handler.IQHandler;
import org.jivesoftware.openfire.muc.MUCRole;
import org.jivesoftware.openfire.muc.spi.LocalMUCRole;
import org.jivesoftware.openfire.muc.spi.LocalMUCRoom;
import org.jivesoftware.openfire.session.ClientSession;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketError;
import org.xmpp.packet.Presence;

/**
 * Broadcast service plugin. It accepts messages and broadcasts them out to
 * groups of connected users. The address <tt>all@[serviceName].[server]</tt> is
 * reserved for sending a broadcast message to all connected users. Otherwise,
 * broadcast messages can be sent to groups of users by using addresses
 * in the form <tt>[group]@[serviceName].[server]</tt>.
 *
 * @author Matt Tucker
 */
public class dbPlugin implements Plugin{  
 
    @Override  
    public void destroyPlugin() {  
    }  
  
    @Override  
    public void initializePlugin(PluginManager manager, File pluginDirectory) {  
        XMPPServer service = XMPPServer.getInstance(); 
        service.getIQRouter().addHandler(new CreateGroupHandler());  
        service.getIQRouter().addHandler(new QueryGroupHandler());  
        service.getIQRouter().addHandler(new QueryGroupMemberHandler( ));  
        service.getIQRouter().addHandler(new JoinGroupHandler() );  
        service.getIQRouter().addHandler(new LeaveGroupHandler() );  
        service.getIQRouter().addHandler(new QueryGroupMemberLocationHandler() );  
        service.getIQRouter().addHandler(new EmergencyCallHandler() );  
        service.getIQRouter().addHandler(new EmergencyCallrecordHandler() );  
        service.getIQRouter().addHandler(new QueryTracklocationHandler() );  
        
//        LocalMUCRoom a1=new LocalMUCRoom();a1.leaveRoom(MUCRole.Role.participant);
    }  
 
} 