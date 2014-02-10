package tw.edu.ntu.csie.srlab.activity;

import java.util.Calendar;

import greendroid.app.GDActivity;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.VCard;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.R.id;
import tw.edu.ntu.csie.srlab.R.layout;
import tw.edu.ntu.csie.srlab.R.string;
import tw.edu.ntu.csie.srlab.packet.IQCreateGroup;

 
 
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGroupActivity extends GDActivity {
 
	private int mHourStart;
	private int mMinuteStart;
	private int mHourEnd;
	private int mMinuteEnd;
	private TextView start_time;
	private TextView end_time;
	private EditText group_name;
	private EditText group_desription;
	private String StartTime;
	private String EndTime;
	private XMPPConnection connection;
	private String username;
 	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.create_group);
        
        connection = ((AppGlobalVariable) (this).getApplication()).getConnection();
        username = ((AppGlobalVariable) (this).getApplication()).getusername();
        final String SERVER_IP= AppGlobalVariable .SERVER_IP;
        
        Button button_cancel = (Button) findViewById(R.id.btnCancel);
        button_cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        start_time = (TextView) findViewById(R.id.group_start_time);
        end_time = (TextView) findViewById(R.id.group_end_time);
        LinearLayout end_time_Layout = (LinearLayout) findViewById(R.id.group_end_time_Layout);
        LinearLayout Start_time_Layout = (LinearLayout) findViewById(R.id.group_start_time_Layout);
        group_name = (EditText) findViewById(R.id.group_name);
        group_desription = (EditText) findViewById(R.id.group_desription);
 
        end_time_Layout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass( CreateGroupActivity.this, TimeActivity.class);
				Bundle timefrom = new Bundle();
				timefrom.putString("StartOrEnd", "end");
				intent.putExtras(timefrom);
				startActivityForResult(intent,1); 		
				
			}
        	
        });
        Start_time_Layout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass( CreateGroupActivity.this, TimeActivity.class);
				Bundle timefrom = new Bundle();
				timefrom.putString("StartOrEnd", "start");
				intent.putExtras(timefrom);
				startActivityForResult(intent,1); 		
				
			}
        	
        });
        Button button_done = (Button) findViewById(R.id.btnSubmit);
        button_done.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if(!(StartTime .equals("")||EndTime.equals("")||group_name.getText().equals("")||group_desription.getText().equals(""))){
					IQCreateGroup iq=new IQCreateGroup(group_name.getText().toString(),
														group_desription.getText().toString(),
														StartTime ,
														EndTime);
					
					IQ result = ((AppGlobalVariable) (CreateGroupActivity.this).getApplication()).requestBlocking(iq);
					if (result != null) {
						if (result.getType() == IQ.Type.ERROR) {
							 
								showToast(result.getError().getCondition());
							 
								return ;
						} else {
							
							
							MultiUserChat muc=new MultiUserChat(connection,group_name.getText().toString()+"@conference."+SERVER_IP);
							 try {
								muc.join(username);
								// Get the the room's configuration form
								 Form form = muc.getConfigurationForm();
								  
								 // Create a new form to submit based on the original form
								 Form newForm = form.createAnswerForm();
								 
								 newForm.setAnswer("muc#roomconfig_persistentroom",true); // WORKS IF SET TO TRUE
							 
								 
								 // Send the completed form (with default values) to the server to configure the room
								 muc.sendConfigurationForm(newForm);
									VCard vCard = new VCard();
									   
			        				try {
										vCard.load(connection);

//										vCard.setProperty(group_name.getText().toString()+"_upload_link", ""+true );
										vCard.setField(group_name.getText().toString()+"_upload_link", ""+true);
//										vCard.setProperty(group_name.getText().toString()+"_download_link", ""+true );
										vCard.setField(group_name.getText().toString()+"_download_link", ""+true);
								
							
									vCard.save(connection);
									} catch (XMPPException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
//									Log.i("TAG","vCard.getProperty _upload_link "+ vCard.getProperty(group_name.getText().toString()+"_upload_link"));
//									Log.i("TAG","vCard.getProperty _download_link "+ vCard.getProperty(group_name.getText().toString()+"_download_link"));
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								showToast("MUC fail");
								return ;
							}
							
							
							 
						}
					}

        
 
				}else{
					showToast("Contents can't be null");
				}

				showToast("Success");
				finish();
			}
        	
        });
    }
	private void showToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	}

	  @Override
	  protected void onActivityResult(int requestCode,int resultCode,Intent data)
	  { 
	    switch (resultCode)
	    {   
	      case TimeActivity.TimeActivityResult:
	    	
	        Bundle bundle = data.getExtras();
	        if(bundle.getString("StartOrEnd").equals("start")){
	        	mHourStart=bundle.getInt("hour");
	        	mMinuteStart=bundle.getInt("min");
	        	StartTime=mHourStart+" : "+mMinuteStart;
	        	start_time.setText(getString(R.string.group_start_time)+" "+StartTime);
	        	Log.i("TAG",getString(R.string.group_start_time)+" "+StartTime);
	        	
	        }else  {
	        	mHourEnd=bundle.getInt("hour");
	        	mMinuteEnd=bundle.getInt("min");
	        	EndTime=mHourEnd+" : "+mMinuteEnd;
	        	end_time.setText(getString(R.string.group_end_time)+" "+EndTime);
	        	Log.i("TAG",getString(R.string.group_end_time)+" "+EndTime);
	        }
	       
   
	         
	        break;       
	      default: 
	        break; 
	     } 
	   } 
}
