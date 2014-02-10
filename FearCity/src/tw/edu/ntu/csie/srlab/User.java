package tw.edu.ntu.csie.srlab;
 

public class User   {
	private String username,TimeStamp,Presence;
	private double Latitude;
	private double Longitude;
	public void  setusername(String username){
		this.username=username;
	}
	public String  getusername(){
		return username;
	}
	public void  setLatitude(double Latitude){
		this.Latitude=Latitude;
	}
	public double  getLatitude(){
		return Latitude;
	}
	public void  setLongitude(double Longitude){
		this.Longitude=Longitude;
	}
	public double  getLongitude(){
		return Longitude;
	}
	public void  setTimeStamp(String TimeStamp){
		this.TimeStamp=TimeStamp;
	}
	public String  getTimeStamp(){
		return TimeStamp;
	}
	public void  setPresence(String Presence){
		this.Presence=Presence;
	}
	public String  getPresence(){
		return Presence;
	}
 
}