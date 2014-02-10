package tw.edu.ntu.csie.srlab.activity;

import greendroid.app.GDListActivity;
import greendroid.app.GDTabActivity;
import greendroid.util.Config;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBarHost;
import greendroid.widget.QuickAction;
import greendroid.widget.QuickActionBar;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.ActionBar.OnActionBarListener;
import greendroid.widget.ActionBar.Type;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.VCard;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.GroupList;
import tw.edu.ntu.csie.srlab.adapter.GroupListItemAdapter;
import tw.edu.ntu.csie.srlab.packet.IQLeaveGroup;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroup;

 
import com.cyrilmottier.android.greendroid.R;


import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class GroupListActivity extends GDListActivity {
	   
		//for debugging 
		private static final String TAG = "GroupClockListView"; 
		private static final boolean D = true;
	    private ActionBarHost mActionBarHost;
		private ArrayList<GroupList> mData= new ArrayList<GroupList>();  
 	    private QuickActionWidget mBar;		
	    final int QuickActionGroupDetail=0;

	    final int groupchat=QuickActionGroupDetail+1;
	    final int download=groupchat+1;
	    final int upload=download+1;
	    final int QuickActionLeaveGroup=upload+1;
		private XMPPConnection connection;
	     GroupListItemAdapter adapter;
		public static final String GROUP_START_TIME = "start_time";
		public static final String GROUP_END_TIME = "end_time";	
		public static final String GROUP_DESRIPTION = "desription";
		public static final String GROUP_GROUP_NAME = "groupname";
		public static final String GROUP_GROUP_MEMBER_NAME = "groupmenbername";
		int NowPosition=0;
		final String MUCconference =AppGlobalVariable.conference;
		private XMPPConnection mConnetion;
		private ImageView mMoreIv = null;
		VCard vCard = new VCard();
		@Override   
		public void onCreate(Bundle savedInstanceState) {    
			if(D) Log.d(TAG,"===ON CREATE====");
			
			super.onCreate(savedInstanceState); 
	        connection = ((AppGlobalVariable) (this).getApplication()).getConnection();
			final String SERVER_IP= AppGlobalVariable .SERVER_IP;
	      

			 setContentView(createLayout());
		        mConnetion=((AppGlobalVariable) (this).getApplication()).getConnection();
			 prepareQuickActionBar();
	           adapter = new GroupListItemAdapter(this, mData);    
		        setListAdapter(adapter);
	        
		}   
		 
	    public void onShowBar(View v) {
	        mBar.show(v);
	    }
	    private void prepareQuickActionBar() {
	        mBar = new QuickActionBar(this); 
	      
	        mBar.addQuickAction(new QuickAction(this, R.drawable.sign_up, R.string.GroupInfo));
	        mBar.addQuickAction(new QuickAction(this, R.drawable.chat, R.string.groupchat));
	        
	        mBar.addQuickAction(new QuickAction(this, R.drawable.download, R.string.DownloadLoc));
	        mBar.addQuickAction(new QuickAction(this, R.drawable.upload, R.string.UploadLoc));
	        mBar.addQuickAction(new QuickAction(this, R.drawable.gd_action_bar_export, R.string.group_leave));

	        mBar.setOnQuickActionClickListener(mActionListener);
		    mBar.setOnDismissListener(new PopupWindow.OnDismissListener() {			
				@Override
				public void onDismiss() {
					mMoreIv.setImageResource(R.drawable .ic_list_more);
				}
			});
	    }   
	    private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
	        public void onQuickActionClicked(QuickActionWidget widget, int position) {
	            Toast.makeText(GroupListActivity.this, "Item " + position + " clicked", Toast.LENGTH_SHORT).show();
	            
	           
	        	GroupList   map=  mData.get(NowPosition);
			  
		 
				 
	            switch (position) {
	        	case QuickActionGroupDetail:
	        		
	        	
	        		
	        		Intent detailIntent = new Intent(GroupListActivity.this, GroupDetailActivity.class);
	        		detailIntent.putExtra(GroupListActivity.GROUP_START_TIME ,map.getstart_time());
	        		detailIntent.putExtra(GroupListActivity.GROUP_END_TIME ,map.getend_time()); 	
	        		detailIntent.putExtra(GroupListActivity.GROUP_DESRIPTION ,map.getdesription());
	        		detailIntent.putExtra(GroupListActivity.GROUP_GROUP_NAME ,map.getgroupname());
	        		detailIntent.putExtra(GroupListActivity.GROUP_GROUP_MEMBER_NAME ,map.getgroupmenbername());
			    	startActivity(detailIntent); 	
			    	break;
			    	

	        	case groupchat:
	        		Intent groupchatIntent = new Intent(GroupListActivity.this, ChatFormActivity.class);
	        		 Bundle bundle = new Bundle();
	        		bundle.putString("room",map.getgroupname());
	        		 bundle.putBoolean("isServiceSend",false);
	        		 groupchatIntent.putExtras(bundle);
	        		 startActivity(groupchatIntent);
	        		break;

	        	case download:
		        	 
	        		
	        		  
	        		 
	        			try {	
	        				vCard.load(mConnetion);
	        
	        				//Log.d("TAG","map.getdownload_link() "+vCard.getProperty(map.getgroupname()+"_download_link"  ));
//	        				if(vCard.getField(map.getgroupname()+"_download_link"  )!=null){
//	        					vCard.setField(map.getgroupname()+"_download_link", ""+true );
//	        				}
						if(map.getdownload_link()   ){
							vCard.setField(map.getgroupname()+"_download_link", ""+false );
							map.setdownload_link(false);
						}else{
							vCard.setField(map.getgroupname()+"_download_link", ""+true );
							map.setdownload_link(true);
						}
					
				
						vCard.save(connection);
						
					} catch (XMPPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					localrefreshList();
	        		break;
	        	case upload:
		        	 
	        		 
	        		  
        			try {	
        				vCard.load(mConnetion);
        
				//Log.d("TAG","map.getupload_link() "+vCard.getProperty(map.getgroupname()+"_upload_link"  ));
//				if(vCard.getField(map.getgroupname()+"_upload_link"  )!=null){
//					vCard.setField(map.getgroupname()+"_upload_link", ""+true );
//				}
					if(map.getupload_link()  ){
						vCard.setField(map.getgroupname()+"_upload_link", ""+false );
						map.setupload_link(false);
					}else{
						vCard.setField(map.getgroupname()+"_upload_link", ""+true );
						map.setupload_link(true);
					}
				
			
					vCard.save(connection);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				localrefreshList();

        		break;
	        	case QuickActionLeaveGroup:
	        		leaveGroup(map.getgroupname());
	        		try {
						vCard.load(mConnetion);
					 
					} catch (XMPPException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	        		
	        		break;
	            }
	        }
	    };
	    public void leaveGroup(String groupname){
	    	
	    	IQLeaveGroup iq=new IQLeaveGroup(groupname);

	    	IQ result = ((AppGlobalVariable) (GroupListActivity.this).getApplication()).requestBlocking(iq);
	    	if (result != null) {
				if (result.getType() == IQ.Type.ERROR) {
					try {
						showToast(result.getError().getMessage());
						throw new  Exception(result.getError().getMessage());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					
					try {
					MultiUserChat muc=new MultiUserChat(connection,groupname+MUCconference );
					
						muc.leave() ;
 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						showToast("MUC fail");
						return ;
					}
					
					refreshList();
					 
				}
			}
	    	showToast("Leave");
	    }
		private void showToast(String str){
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

		}
	    private static class MyQuickAction extends QuickAction {
	        
	        private static final ColorFilter BLACK_CF = new LightingColorFilter(Color.BLACK, Color.BLACK);

	        public MyQuickAction(Context ctx, int drawableId, int titleId) {
	            super(ctx, buildDrawable(ctx, drawableId), titleId);
	        }
	        
	        private static Drawable buildDrawable(Context ctx, int drawableId) {
	            Drawable d = ctx.getResources().getDrawable(drawableId);
	            d.setColorFilter(BLACK_CF);
	            return d;
	        }
	        
	    }
	    
	    public void refreshList(){
	        Handler th1=new Handler();
	        th1.post(new Runnable (){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mData = getData(); 
					adapter = new GroupListItemAdapter(GroupListActivity.this, mData);    
					setListAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
	        	
	        });
	    }
	    public void  localrefreshList(){ 
	    	adapter = new GroupListItemAdapter(GroupListActivity.this, mData);    
			setListAdapter(adapter);
			adapter.notifyDataSetChanged();
	    }
		@Override
		public void onStart(){
			if(D) Log.d(TAG,"===ON START====");
			
			super.onStart();
			// put the following code in the onstart(), because it will reload the new result when user push back from the group setting view 
	        
			
			refreshList();

	        
		}
		
		@Override
		public void onStop(){
			if(D) Log.d(TAG,"===ON STOP====");
			super.onStop();
			mData.clear();
			
 

		}
		
		private ArrayList<GroupList> getData() {    
			
			if(D) Log.d(TAG,"===getData()====");
			
	      
//	        String []strarry= {"1","2"};
	        IQQueryGroup iq=new IQQueryGroup();

	        IQ  result =  ((AppGlobalVariable) (GroupListActivity.this).getApplication()).requestBlocking(iq);
			if(D) Log.d("TAG","result "+((IQQueryGroup) result)  );

//			if (result != null) {
//				if (result.getType() == IQ.Type.ERROR) {
//					showToast(result.getError().getCondition());
//					if(D) Log.d("TAG","result  ERROR");
//				}else{if(D) Log.d("TAG","result CORRECT");
//					return ((IQQueryGroup)result).getgroup();
//				}
//			}
//			if(D) Log.d("TAG","result "+ result .toXML());
    		 return ((IQQueryGroup) result)  .getgroup();    
	        
	     }   
	    // the item in the list are pushed 
	    @Override   
	    protected void onListItemClick(ListView l, View v, int position, long id) {    
	    	onShowBar(v);
	    	if(D) Log.d(TAG,"onListItemClick()");
	     
	    	NowPosition=position;
	    	
			mMoreIv = (ImageView) v.findViewById(R.id.i_more);
			mMoreIv.setImageResource(R.drawable.ic_list_more_selected);
	     

	    }  

 

	    public int createLayout() {
 
 
	                return R.layout.groupclock_list;
	        
	    }
	    public ArrayList<GroupList> getmData(){
			return (ArrayList<GroupList>) mData;
	    	
	    }
}
