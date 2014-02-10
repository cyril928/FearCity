package tw.edu.ntu.csie.srlab;

public class TrackPoint  
	{

	private double Latitude;
	private double Longitude;
	private String  TimeStamp;
	private int index;
		public double getLatitude(){
			return Latitude;
		}
		public double getLongitude(){
			return Longitude;
		}
		public void setLatitude(double Latitude){
			this.Latitude= Latitude;
		}
		public void setLongitude(double Longitude){
			this.Longitude= Longitude;
		}
		
		public String getTimeStamp(){
			return TimeStamp;
		}
		public void setTimeStamp(String TimeStamp){
			this.TimeStamp= TimeStamp;
		}
		
		
		public int  getindex(){
			return index;
		}
		public void setindex(int index){
			this.index= index;
		}
		
	}