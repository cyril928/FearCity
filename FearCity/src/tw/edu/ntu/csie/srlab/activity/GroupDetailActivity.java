package tw.edu.ntu.csie.srlab.activity;
 
 
import org.jivesoftware.smack.packet.IQ;

import tw.edu.ntu.csie.srlab.packet.IQQueryGroup;
import tw.edu.ntu.csie.srlab.packet.IQQueryGroupMember;

import com.cyrilmottier.android.greendroid.R;
 

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import greendroid.app.ActionBarActivity;
 
import greendroid.app.GDTabActivity;

public class GroupDetailActivity extends GDTabActivity {
 
 
 
    
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
//		this.setTitle("Screen 2");
		
		this.getContentView().setBackgroundColor(android.R.color.white);
		final String GroupInfoText =  getString(R.string.GroupInfo);
		final Intent GroupInfoIntent = new Intent(this, GroupInfoActivity.class);
			        
		GroupInfoIntent.putExtra(GroupListActivity.GROUP_START_TIME, getIntent().getStringExtra(GroupListActivity.GROUP_START_TIME));
		GroupInfoIntent.putExtra(GroupListActivity.GROUP_GROUP_NAME, getIntent().getStringExtra(GroupListActivity.GROUP_GROUP_NAME));
		GroupInfoIntent.putExtra(GroupListActivity.GROUP_DESRIPTION, getIntent().getStringExtra(GroupListActivity.GROUP_DESRIPTION));
		GroupInfoIntent.putExtra(GroupListActivity.GROUP_END_TIME, getIntent().getStringExtra(GroupListActivity.GROUP_END_TIME));

		GroupInfoIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
		addTab(GroupInfoText, GroupInfoText, GroupInfoIntent);
			        
		final String GroupUserText =  getString(R.string.GroupUser);
		final Intent GroupUserIntent = new Intent(this, GroupUserActivity.class);
		GroupUserIntent.putExtra(GroupListActivity.GROUP_GROUP_NAME, getIntent().getStringExtra(GroupListActivity.GROUP_GROUP_NAME));
		GroupUserIntent.putExtra(ActionBarActivity.GD_ACTION_BAR_VISIBILITY, View.GONE);
		addTab(GroupUserText, GroupUserText, GroupUserIntent);
			
      
        
    }
    


 
 
}
