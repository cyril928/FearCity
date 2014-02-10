package tw.edu.ntu.csie.srlab.packet;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.IQ;

 
import tw.edu.ntu.csie.srlab.TrackPoint;
 

 

public class IQQueryTrackLocation extends IQ {
	public static String NAME = "query";
	private String index ;
	public static String NAMESPACE = "srlab:group:iq:queryTracklocation";
	

	private ArrayList<TrackPoint> TrackPointlist =null;
	
	public void  setTrackPointlist(ArrayList<TrackPoint>  TrackPointlist){
		this.TrackPointlist=TrackPointlist;
	}
	public ArrayList<TrackPoint>   getTrackPointlist( ){
		return TrackPointlist;
	}
	public IQQueryTrackLocation(String index){
		this.index=index;
		
	}
 
 
	public IQQueryTrackLocation() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getChildElementXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<" + NAME + " xmlns=\"" + NAMESPACE + "\">");
		buffer.append("<index>"+index+"</index>"); 
//		buffer.append("<Description>"+GroupName+"</Description>");  
//		buffer.append("<StartTime>"+StartTime+"</StartTime>");  
//		buffer.append("<EndTime>"+EndTime+"</EndTime>");  
		buffer.append("</" + NAME + ">");
		return buffer.toString();
	}
}
