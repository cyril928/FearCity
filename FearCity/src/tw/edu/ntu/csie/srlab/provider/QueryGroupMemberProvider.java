package tw.edu.ntu.csie.srlab.provider;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

 
import tw.edu.ntu.csie.srlab.User;
 
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMember;

import android.util.Log;


public class QueryGroupMemberProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser arg0) throws Exception {
 

		boolean done = false;

		IQQueryGroupMember iq=new IQQueryGroupMember(); 
		Log.d("TAG", "QueryGroupProvider was been used.");
		ArrayList<User>  users=new ArrayList<User>();
		User user = null; 
		while (!done) {
			int eventType = arg0.next();	
			if (eventType == XmlPullParser.START_TAG) {		
//				Log.d("TAG", "arg0.nextText()"+arg0.nextText());
				if (arg0.getName().equals("GroupMember")) {
			
					user=new User(); 
				
				} else if (arg0.getName().equals("UserName")) {
				
					user.setusername(arg0.nextText());
					
				}else if (arg0.getName().equals("Presence")) {
					
					user.setPresence(arg0.nextText());
					
				} 
				
			} else if (eventType == XmlPullParser.END_TAG) {
				if (arg0.getName().equals("query")) {
					done = true;
					iq.setusers(users);
				}else if(arg0.getName().equals("GroupMember")){
					users.add(user);
				}
				         
			}
		}

		Log.d("TAG", "users.size " +users.size());

		return iq;
	}

}
