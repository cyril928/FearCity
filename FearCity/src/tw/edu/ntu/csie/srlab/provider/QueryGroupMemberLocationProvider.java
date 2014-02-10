package tw.edu.ntu.csie.srlab.provider;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

 
import tw.edu.ntu.csie.srlab.User;
 
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMember;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMemberLocation;

import android.util.Log;


public class  QueryGroupMemberLocationProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser arg0) throws Exception {
 

		boolean done = false;

		IQQueryGroupMemberLocation iq=new IQQueryGroupMemberLocation(); 
		Log.d("TAG", "QueryGroupProvider was been used.");
		ArrayList<User>  users=new ArrayList<User>();
		User user = null;
		while (!done) {
			int eventType = arg0.next();	
			if (eventType == XmlPullParser.START_TAG) {
//				Log.d("TAG", "arg0.nextText()"+arg0.nextText());
				if (arg0.getName().equals("User")) {
					
					user=new User(); 
					 
				} if (arg0.getName().equals("UserName")) {
					
					user.setusername(arg0.nextText());
					
				}  if (arg0.getName().equals("TimeStamp")) {
					
					user.setTimeStamp(arg0.nextText());
					
				} if (arg0.getName().equals("Location")) {
					String[] str=arg0.nextText().split(",");
					user.setLatitude(Double.parseDouble(str[0]));
					user.setLongitude(Double.parseDouble(str[1]));
					
				} 
				
			} else if (eventType == XmlPullParser.END_TAG) {
				if (arg0.getName().equals("query")) {
					done = true;
					iq.setusers(users);
				}else if(arg0.getName().equals("User")){
					users.add(user);
				}
				         
			}
		}

		Log.d("TAG", "provider creates iq: " + iq.toXML());

		return iq;
	}

}
