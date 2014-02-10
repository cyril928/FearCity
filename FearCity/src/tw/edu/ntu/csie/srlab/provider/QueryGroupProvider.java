package tw.edu.ntu.csie.srlab.provider;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import tw.edu.ntu.csie.srlab.GroupList;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroup;

import android.util.Log;


public class QueryGroupProvider implements IQProvider {

	@Override
	public IQ parseIQ(XmlPullParser arg0) throws Exception {
 

		boolean done = false;

		IQQueryGroup iq=new IQQueryGroup(); 
		Log.d("TAG", "QueryGroupProvider was been used.");
		ArrayList<GroupList>  groups=new ArrayList<GroupList>();
		GroupList group = null;
		while (!done) {
			int eventType = arg0.next();	
			if (eventType == XmlPullParser.START_TAG) {
//				Log.d("TAG", "arg0.nextText()"+arg0.nextText());
				if (arg0.getName().equals("Group")) {
					
					 group=new GroupList(); 
					 
				}else if (arg0.getName().equals("GroupName")) {
					
					group.setgroupname(arg0.nextText());
					
				}else if (arg0.getName().equals("Description")) {
					
					group.setdesription(arg0.nextText());
					
				}else if (arg0.getName().equals("StartTime")) {
					
					group.setstart_time(arg0.nextText());
					
				}else if (arg0.getName().equals("EndTime")) {
					
					group.setend_time(arg0.nextText());
					
				}else if (arg0.getName().equals("Uploadloc")) {
					
					group.setupload_link((arg0.nextText().equals("true"))?true:false);
					
				}else if (arg0.getName().equals("Downloadloc")) {
					
					group.setdownload_link((arg0.nextText().equals("true"))?true:false);
					
					
				} 
				
			} else if (eventType == XmlPullParser.END_TAG) {
				if (arg0.getName().equals("query")) {
					done = true;
					iq.setgroup(groups);
				}else if(arg0.getName().equals("Group")){
					  groups.add(group);
				}
				         
			}
		}

		Log.d("TAG", "provider creates iq: " + iq.toXML());

		return iq;
	}

}
