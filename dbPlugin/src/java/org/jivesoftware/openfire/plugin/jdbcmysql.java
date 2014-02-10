
package org.jivesoftware.openfire.plugin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class jdbcmysql {
	  private Connection con = null; //Database objects 
	  
	  private Statement stat = null; 
	 
	  private ResultSet rs= null, subrs= null; 
	  
	  private PreparedStatement pst = null; 
	  

	  
	     
	//	"Update MyTable set SomeColumn=192 where OtherColumn=12";
 
	  
	  private String selectSQLhead = "Select * from `usergps` where `username`='",
	  selectSQLtail="' ORDER BY `TimeStamp` DESC LIMIT 1"; 
	  
	  private String indexofselectSQLhead = "Select index from `usergps` where `username`='";
	  

	  public jdbcmysql() 
	  { 
	    try { 
	      Class.forName("com.mysql.jdbc.Driver"); 
 
	      con = DriverManager.getConnection( 
	      "jdbc:mysql://localhost/openfire?useUnicode=true&characterEncoding=UTF8", 
	      "root","test12345"); 
	     
 
	    } 
	    catch(ClassNotFoundException e) 
	    { 
	      System.out.println("DriverClassNotFound :"+e.toString()); 
	    } 
	    catch(SQLException x) { 
	      System.out.println("Exception :"+x.toString()); 
	    } 
	    
	  } 
	  public void  InsertEmergencyCallTable(ArrayList<String> groupnameList , String username,String msgBody,String time) 
	  {  
	  
	    try 
	    { 
	    	  stat = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  
	                  ResultSet.CONCUR_UPDATABLE); 
	    	  rs = stat.executeQuery(selectSQLhead+username+selectSQLtail); 
	
	      
	      String GPSindex=null;
	      while(rs.next()) 
	      { 
	    	  GPSindex=rs.getString("index");
	    	 
	      } /*
	      System.out.println("GPSindex "+GPSindex);
	      time=time.substring(0,time.length()-4);
	      System.out.println("time "+time);*/
	   
	      String sql = "INSERT `emergencycall`(`group`,`GPSindex`,`username`,`msgBody`,`TimeStamp`) VALUES(?,?,?,?,?)";  
	      PreparedStatement prest = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);  
	      for(int x = 0; x <    groupnameList.size(); x++){  
	         prest.setString(1, groupnameList.get(x));  
	         prest.setString(2, GPSindex);  
	         prest.setString(3, username);  
	         prest.setString(4, msgBody);  
	         prest.setString(5, time);  
	         prest.addBatch();  
	      }  
	      prest.executeBatch();  
 
	       
	       
	      
  
	     
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("InsertDB Exception :" + e.toString()); 
	    } 
		 finally 
			{ 
			      Close(); 
			} 
		     

	      
	  } 
	  public ArrayList   selectEmergencyCallrecord( ArrayList<String> MatchUserGroup ){
		  ArrayList  indexnum=new ArrayList<String>();
		    try {
		   String str="";
		      stat = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  
	                  ResultSet.CONCUR_UPDATABLE); 
		   for (int i=0;i<MatchUserGroup.size();i++){
			   
			   str+=" `group`='"+MatchUserGroup.get(i)+"' ";
			   if( i!=MatchUserGroup.size()-1){
				   str+="or";
			   }
		   }System.out.print("Select * from `emergencycall` where"+str+"ORDER BY `index` DESC ");
				rs = stat.executeQuery("Select * from `emergencycall` where   "+str+" ORDER BY `index` DESC LIMIT 10");
				 while(rs.next()) 
			      { 	String index=rs.getString("index");
					 
						 Map map=new HashMap();
						 map.put("group",  rs.getString("group") );
						 map.put("index",  rs.getString("index") );
						 map.put("sender",  rs.getString("username") );
						 map.put("msgBody",  rs.getString("msgBody") );
						 map.put("time",  rs.getString("TimeStamp") );
			    	     indexnum.add( map );
			    	  //System.out.println("index "+rs.getString("index"));
			       
			      }
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			      System.out.println("SelectDB Exception :" + e.toString()); 
			} 
			 finally 
			{ 
			      Close(); 
			} 
		     
		      return indexnum;
		      
	  }
	  public Map  SelectTable(String username) 
	  {  
		Map map=new HashMap();
	    try 
	    {

	      
	      stat = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  
                  ResultSet.CONCUR_UPDATABLE); 
	      
	      rs = stat.executeQuery(selectSQLhead+username+selectSQLtail); 
	    
	      while(rs.next()) 
	      { 
	    	  map.put("Location",rs.getString("Latitude")+","+rs.getString("Longitude"));
	    	  map.put("TimeStamp",rs.getString("TimeStamp") );
	    	  map.put("UserName",rs.getString("username") );
	       
	        
//	        rs.updateInt("IsPushData", 0);
//	        rs.updateRow(); 
	        
 
	        
	       
	        
	      } 

	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("SelectDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    } 
	    return map;
	  } 
	  public ArrayList<Map>  SelectEmergencyCallTable(int  index) 
	  {  
	 
		Map mapLocation=null;
		ArrayList <Map> maplist=new ArrayList <Map>();
	    try 
	    {

	      
	      stat = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,  
                  ResultSet.CONCUR_UPDATABLE); 
	      
	      rs = stat.executeQuery("Select * from `EmergencyCall` where `index`='"+index+"'"); 
	      String  GPSindex="";
	 
	      while(rs.next()) 
	      { 
//	    	  map.put("index",rs.getString("index"));
//	    	  map.put("group",rs.getString("group"));
//	    	  map.put("GPSindex",rs.getString("GPSindex") );
//	    	  map.put("username",rs.getString("username") );
//	    	  map.put("msgBody",rs.getString("msgBody"));
//	    	  map.put("TimeStamp",rs.getString("TimeStamp"));
	    	  GPSindex=rs. getString("GPSindex");
	    	  System.out.println(GPSindex); 
	      } 
	      
	      rs= stat.executeQuery("Select * from `usergps` where `index`<= '"+GPSindex+"' ORDER BY `index` DESC LIMIT 20"); 
          
          while(rs.next()) 
	      {   mapLocation=new HashMap();
        	  mapLocation.put("index" ,rs.getString("index")  );
        	  mapLocation.put("TimeStamp" , rs.getString("TimeStamp"));
          	  mapLocation.put("Location" ,rs.getString("Latitude")+","+rs.getString("Longitude"));
          	  maplist.add(mapLocation);
          	System.out.println(rs.getString("index")); 
	      }
        
	      
	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("SelectDB Exception :" + e.toString()); 
	    } 
	    finally 
	    { 
	      Close(); 
	    } 
	    return maplist;
	  } 
	  private void Close() 
	  { 
	    try 
	    { 

	      if(stat!=null) 
	      { 
	        stat.close(); 
	        stat = null; 
	      } 

	    } 
	    catch(SQLException e) 
	    { 
	      System.out.println("Close Exception :" + e.toString()); 
	    } 
	  } 
	  
}
