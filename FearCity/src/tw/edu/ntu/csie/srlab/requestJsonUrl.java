package tw.edu.ntu.csie.srlab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.util.Log;



public class requestJsonUrl {
	
	private ArrayList<overlayslist> li=new ArrayList<overlayslist>();
	public requestJsonUrl(String url){
	HttpPost request = new HttpPost(url);

      List <NameValuePair> params = new ArrayList <NameValuePair>(); 
//      params.add(new BasicNameValuePair("email",GOOGLE_ID )); 

      

	    String retSrc="";
		try {
			  /*發出HTTP request*/
      	//request.setEntity(new UrlEncodedFormEntity(" ", HTTP.UTF_8)); 
    
	        HttpResponse httpResponse = new DefaultHttpClient().execute(request);
	      
			retSrc = EntityUtils.toString(httpResponse.getEntity()).replace("while(1);", "");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     // 生成 JSON 對象
	     JSONObject result;
	     JSONObject data=null;
		  Log.v("@@@@@@@@@@@@@"+ retSrc,"!@#");
	    // list=new ArrayList<Patientlist>();
		try {
			result = new JSONObject( retSrc);
			   data=result.getJSONObject("overlays");
			   JSONArray poylines=data.getJSONArray("polylines");
			     for(int i=0;i<poylines.length();i++){
			    	 overlayslist patientlist=new overlayslist();
			    	 JSONObject tmp=poylines.getJSONObject(i);
			    	
			    	 if (!tmp.isNull("points")) {
//			    		 JSONObject patient=(JSONObject) tmp.get("points");
			    		 
			    		
			    		 patientlist.setpolylines( (String ) tmp.get("points"));
			   		  Log.v("@@@@@@@@@@@@@"+ patientlist.getpolylines(),"!@#");
//			    		 patientlist.setnumber( (String )patient.get("number"));
//			    		 
//			    		 JSONObject latest_stamp=(JSONObject) patient.get("latest_stamp");
//			    		 patientlist.settime( (String )latest_stamp.get("time"));
//					     patientlist.setstatus( (String )latest_stamp.get("status"));
//					     patientlist.setbed_number( (String )latest_stamp.get("bed_number"));
//					     patientlist.setid((int)tmp.getInt("id"));
//			    		// Log.v("!!!!!!!!!!!!!"," "+tmp.g.isNull("id"));
//					     if((boolean )tmp.get("care").equals("T")){
//					    	 patientlist.setcheckbox(true);
//					     }
					    
					     li.add(patientlist);
			    	 }	
			    	 
			     }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}
	  public  ArrayList<overlayslist> getlist (){
		return li;
	  
		  
	  }
}
