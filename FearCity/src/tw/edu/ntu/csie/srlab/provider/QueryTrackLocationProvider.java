package tw.edu.ntu.csie.srlab.provider;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

 
 
import tw.edu.ntu.csie.srlab.TrackPoint;
import tw.edu.ntu.csie.srlab.User;
 
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMember;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMemberLocation;
import tw.edu.ntu.csie.srlab.packet.IQQueryTrackLocation;

import android.util.Log;


public class  QueryTrackLocationProvider implements IQProvider {
 
	@Override
	public IQ parseIQ(XmlPullParser arg0) throws Exception {
 

		boolean done = false;

		IQQueryTrackLocation iq=new IQQueryTrackLocation(); 
		Log.d("TAG", "QueryGroupProvider was been used.");
		ArrayList<TrackPoint>  TrackPointlist=new ArrayList<TrackPoint>();
		TrackPoint trackpoint = null;
		while (!done) {
			int eventType = arg0.next();	
			if (eventType == XmlPullParser.START_TAG) {
//				Log.d("TAG", "arg0.nextText()"+arg0.nextText());
				if (arg0.getName().equals("TrackPoint")) {
					
					trackpoint=new TrackPoint(); 
					 
				}else if (arg0.getName().equals("index")) {
					
					trackpoint.setindex(Integer.parseInt(arg0.nextText()));
					
				}else  if (arg0.getName().equals("Location")) {
					String[] str=arg0.nextText().split(",");
	 
					trackpoint.setLatitude( Double.parseDouble(str[0]));
					trackpoint.setLongitude( Double.parseDouble(str[1]));
				}else if (arg0.getName().equals("TimeStamp")) {
					
					trackpoint.setTimeStamp(arg0.nextText());
					
				} 
				
			} else if (eventType == XmlPullParser.END_TAG) {
				if (arg0.getName().equals("query")) {
					done = true;
					iq.setTrackPointlist(TrackPointlist);
				}else if(arg0.getName().equals("TrackPoint")){
					TrackPointlist.add(trackpoint);
				}
				         
			}
		}

		Log.d("TAG", "provider creates iq: " + iq.toXML());

		return iq;
	}

}
