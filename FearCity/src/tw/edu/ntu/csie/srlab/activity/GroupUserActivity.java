package tw.edu.ntu.csie.srlab.activity;

import greendroid.app.GDListActivity;
import greendroid.widget.ItemAdapter;
import greendroid.widget.item.DescriptionItem;
import greendroid.widget.item.DrawableItem;
import greendroid.widget.item.Item;
import greendroid.widget.item.ProgressItem;
import greendroid.widget.item.SeparatorItem;
import greendroid.widget.item.TextItem;
import greendroid.widget.item.ThumbnailItem;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.User;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMember;

import android.R.color;
import android.R.id;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyrilmottier.android.greendroid.R;

public  class GroupUserActivity  extends GDListActivity {
    public static final String EXTRA_COLOR = "com.cyrilmottier.android.gdcatalog.TabbedActionBarActivity$FakeActivity.extraColor";
    public static final String EXTRA_TEXT = "com.cyrilmottier.android.gdcatalog.TabbedActionBarActivity$FakeActivity.extraText";
    

  
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Intent intent = getIntent();
//            FrameLayout tv = (FrameLayout) this.findViewById(R.id.gd_action_bar_content_view);
//            tv.setBackgroundColor(android.R.color.white);
 //            if (intent != null) {
//                setContentView(R.layout.group_detail);
//
//                TextView textView = (TextView) findViewById(R.id.group_name);
//                textView.setText(intent.getStringExtra(EXTRA_TEXT));
//                textView.setBackgroundColor(intent.getIntExtra(EXTRA_COLOR, Color.WHITE));
//            }
            List<Item> items = new ArrayList<Item>();
    		IQQueryGroupMember iq=new IQQueryGroupMember(intent.getStringExtra(GroupListActivity.GROUP_GROUP_NAME));

            IQ  result =  ((AppGlobalVariable) (GroupUserActivity.this).getApplication()).requestBlocking(iq);
        	if (result != null) {
    			if (result.getType() == IQ.Type.ERROR) {
    				 
    					showToast(result.getError().getCondition());
    				 
    					return ;
    			} else{
    				 ArrayList<User>  users=((IQQueryGroupMember)result).getusers();
    				 if(users==null){
    					 return ;
    				 }
    				 Log.i("TAG","TAG"+ users.toString());
    				 for (int i=0;i<users.size();i++){
    					 items.add(new ThumbnailItem(users.get(i).getusername(), users.get(i).getPresence(), R.drawable.green_35));
    				 }
    	            
    	            
    	              ItemAdapter adapter = new ItemAdapter(this, items);
    	     
    	            setListAdapter(adapter);
    	            
    	             
    			}
        	} 
    			
            
            
        

            
             
            
            
         
 
        }
    	private void showToast(String str){
    		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    	}
}