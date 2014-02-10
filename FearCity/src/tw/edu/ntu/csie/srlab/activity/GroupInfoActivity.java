package tw.edu.ntu.csie.srlab.activity;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.service.ConnectService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cyrilmottier.android.greendroid.R;
import com.cyrilmottier.android.greendroid.R.string;

public  class GroupInfoActivity extends Activity {
    public static final String EXTRA_COLOR = "com.cyrilmottier.android.gdcatalog.TabbedActionBarActivity$FakeActivity.extraColor";
    public static final String EXTRA_TEXT = "com.cyrilmottier.android.gdcatalog.TabbedActionBarActivity$FakeActivity.extraText";
	private XMPPConnection connection;
	private boolean Debug=true;
	private EditText editview =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        connection = ((AppGlobalVariable) (GroupInfoActivity.this).getApplication()).getConnection();
        final String MUCconference=AppGlobalVariable.conference;
       final String SERVER_IP=  AppGlobalVariable .SERVER_IP;
        if (intent != null) {
            setContentView(R.layout.group_detail);

//            TextView textView = (TextView) findViewById(R.id.group_name);
//            textView.setText(intent.getStringExtra(EXTRA_TEXT));
//            textView.setBackgroundColor(intent.getIntExtra(EXTRA_COLOR, Color.WHITE));
            final TextView group_name = (TextView) findViewById(R.id.group_name);
            TextView group_desription = (TextView) findViewById(R.id.group_desription);
            TextView group_start_time = (TextView) findViewById(R.id.group_start_time);
            TextView group_end_time = (TextView) findViewById(R.id.group_end_time);
            group_name.setText(intent.getStringExtra(GroupListActivity.GROUP_GROUP_NAME));
            group_desription.setText(intent.getStringExtra(GroupListActivity.GROUP_DESRIPTION));
            group_start_time.setText(intent.getStringExtra(GroupListActivity.GROUP_START_TIME));
            group_end_time.setText(intent.getStringExtra(GroupListActivity.GROUP_END_TIME));
            Button inv = (Button) findViewById(R.id.invite);

    		 
            inv.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					editview= new   EditText(GroupInfoActivity.this);
					 new AlertDialog.Builder(GroupInfoActivity.this)
		    	      .setTitle("invitation")
		    	      .setMessage("who you want to invite?")  
		    	      .setView(editview)
		    	      .setPositiveButton(string.yes , new DialogInterface.OnClickListener() {
		    	        	public void onClick(DialogInterface dialog, int id) {
		    	        		
		    	        		MultiUserChat muc=new MultiUserChat(connection,group_name.getText().toString()+ MUCconference);
		    	        		
		    	        		if (Debug)Log.d("TAG",editview.getText().toString()+"@"+SERVER_IP);
		    	        		
								muc.invite(editview.getText().toString()+"@"+SERVER_IP, "Did you want to join group "+group_name.getText().toString());
								dialog.dismiss();
		    	        	}
		    	        })
		    	        .setNegativeButton(string.no, new DialogInterface.OnClickListener() {
		    	        	public void onClick(DialogInterface dialog, int id) {
		    	        		dialog.dismiss();
		    	        	}
		    	        })
		    	        .show();	
					
				}
            	
            });
            
            
        }
    }

}