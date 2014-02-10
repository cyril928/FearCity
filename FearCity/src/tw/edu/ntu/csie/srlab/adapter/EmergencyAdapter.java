package tw.edu.ntu.csie.srlab.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tw.edu.ntu.csie.srlab.GroupList;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.R.id;
import tw.edu.ntu.csie.srlab.R.layout;
import tw.edu.ntu.csie.srlab.activity.GroupListActivity;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


// implement the customize adapter for the groupclockListactivity 

public class EmergencyAdapter extends BaseAdapter {
	
	//for debugging 
	private static final String TAG = "EmergencyAdapter"; 
	private static final boolean D = true;
	
	private LayoutInflater mInflater;   
	private List<GroupList> groupList;
	//item's id which is used for find the item in the groupinfo db
 
	
	// in order to access db groupinfo.db, we need to get the GroupClockListView's openhelper
	private GroupListActivity groupClockList_Context;
	
	public EmergencyAdapter(Context context, List<GroupList> it ) {
		// TODO Auto-generated constructor stub
		this.groupClockList_Context = (GroupListActivity) context;
		this.mInflater = LayoutInflater.from(context);
		groupList = it;
	}

 

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return groupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	// adapter get the each item in the List<map> and display 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.groupclock_item, null);
			holder = new ViewHolder();
			holder.group_name = (TextView) convertView.findViewById(R.id.group_name);

			holder.start_time = (TextView) convertView.findViewById(R.id.start_time);
			holder.end_time = (TextView) convertView.findViewById(R.id.end_time);
			holder.upload_link = (TextView)convertView.findViewById(R.id.upload_link);
			holder.download_link = (TextView)convertView.findViewById(R.id.download_link);
			convertView.setTag(holder);
			
			if(D) Log.d(TAG,"================convertView.setTag() success=======");
		}
		else {  
			if(D) Log.d(TAG,"================convertView.getTag() success=======");
             holder = (ViewHolder)convertView.getTag();    
        }
		//item's id which is used for find the item in the groupinfo db
 
		holder.start_time.setText( groupList.get(position).getstart_time());
		holder.end_time.setText( groupList.get(position).getend_time());
		holder.group_name.setText( groupList.get(position).getgroupname());
		
//		if(D) Log.d(TAG,(String)groupList.get(position).get("start_time")+"->"+(String)groupList.get(position).get("end_time"));
		
 
 
 
		if(D) Log.d(TAG,"=============return convertView success=======");
		return convertView;	
	}

	//create view group for the any view components in the listview's each item
	public final class ViewHolder{
		public TextView group_name;    
		public TextView start_time;    
		public TextView end_time;    
		public TextView upload_link;
		public TextView download_link;    
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return groupList.size();
	}    
	
}
