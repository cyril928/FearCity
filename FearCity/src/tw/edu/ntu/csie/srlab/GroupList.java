package tw.edu.ntu.csie.srlab;

public class GroupList {
	private String groupname,desription,start_time,end_time;
	private String [] groupmenbername;
	private boolean upload_link,download_link;
 
	public void  setgroupname(String groupname){
		this.groupname=groupname;
	}
	public String  getgroupname(){
		return groupname;
	}
	public void  setstart_time(String  start_time){
		this.start_time=start_time;
	}
	public String  getstart_time(){
		return start_time;
	}
	public void  setend_time(String end_time){
		this.end_time=end_time;
	}
	public String  getend_time(){
		return end_time;
	}
	public void  setupload_link(boolean upload_link){
		this.upload_link=upload_link;
	}
	public boolean  getupload_link(){
		return upload_link;
	}
	public void  setdownload_link(boolean download_link){
		this.download_link=download_link;
	}
	public boolean  getdownload_link(){
		return download_link;
	}
	public void  setdesription(String desription){
		this.desription=desription;
	}
	public String  getdesription(){
		return desription;
	}
	public void  setgroupmenbername(String[] groupmenbername){
		this.groupmenbername=groupmenbername;
	}
	public String[]  getgroupmenbername(){
		return groupmenbername;
	}
	
}