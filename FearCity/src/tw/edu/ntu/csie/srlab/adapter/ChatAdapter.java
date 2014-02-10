package tw.edu.ntu.csie.srlab.adapter;

import java.util.ArrayList;

import tw.edu.ntu.csie.srlab.ChatMessage;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.R.drawable;
import tw.edu.ntu.csie.srlab.R.id;
import tw.edu.ntu.csie.srlab.R.layout;
import tw.edu.ntu.csie.srlab.service.ConnectService;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {
	private ArrayList<ChatMessage> msglist;
	private  LayoutInflater mInflater;
	private String username;
 
 
	public ChatAdapter(Context context,
			ArrayList<ChatMessage> msglist,String username) {
		this.msglist=msglist;
		this.mInflater=LayoutInflater.from(context);
		this.username = username;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return msglist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return msglist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		ChatMessage msg=msglist.get(position);
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.message_other, null);
			if(msg.getsender().equals(username)){
			convertView = mInflater.inflate(R.layout.message_me, null);
			}
			holder = new ViewHolder();
			holder.msgBody = (TextView) convertView.findViewById(R.id.msgBody);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.sender = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
			
	 
		}else {  
			 
            holder = (ViewHolder)convertView.getTag();    
       }
//		LinearLayout linLay = (LinearLayout) convertView.findViewById(R.id.backbubble);
	 
//		linLay.setBackgroundDrawable(mInflater.getContext().getResources().getDrawable(R.drawable.bubble_2));
	
		Log.d("TAG","msg.getsender()"+msg.getsender());
		Log.d("TAG","msg.getmsgBody()"+msg.getmsgBody());
		holder.msgBody.setText(msg.getmsgBody());
		holder.time.setText(msg.gettime());
		holder.sender.setText(msg.getsender());
		 
//			linLay.setBackgroundDrawable(mInflater.getContext().getResources().getDrawable(R.drawable.bubble_1));
		 


		
		return convertView;	
	}
	public final class ViewHolder{       
		public TextView msgBody;    
		public TextView sender;
		public TextView time;    
	}  
}
