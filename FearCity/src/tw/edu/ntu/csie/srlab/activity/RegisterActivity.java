package tw.edu.ntu.csie.srlab.activity;

import greendroid.app.GDActivity;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.packet.VCard;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.R.id;
import tw.edu.ntu.csie.srlab.R.layout;




import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends GDActivity {

	// debugging 
	private static final String TAG = "XMPPRegisterActivity"; 
	private static final boolean D = true;
	private static final String SETTING_INFOS = "SETTING_info";
	private static final String SETTING_ACCOUNT = "SETTING_ACCOUNT";
	private static final String SETTING_PASSWORD = "SETTING_PASSWORD";
	// XMPP connection
	private XMPPConnection connection;
	private IQ result;
	private String account;
	private String password;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.xmpp_register);
        if(D) Log.d(TAG,"++++ ON CREATE +++++");
 
        presentRegisterView();

       
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		

	}
	
	private void presentRegisterView(){
		 Button login = (Button) findViewById(R.id.register);
		 login.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
						
					 account = getViewText(R.id.register_id);
			    	 password = getViewText(R.id.register_password);
			    	String confirm_password = getViewText(R.id.confirm_password);
			    	String email = getViewText(R.id.register_email);
			    	String phone = getViewText(R.id.register_phone);
			    	
			    	if(!password.equals(confirm_password)){
						Toast.makeText(RegisterActivity.this, "Your password is differnt from your confirm one!", Toast.LENGTH_SHORT).show();
						setViewText(R.id.register_password,"");
						setViewText(R.id.confirm_password,"");
						return;
					}
			    	
			    	
			    	if (!formCheck("user id", account)) return;
			    	if (!formCheck("password", password)) return;
			    	if (!formCheck("email", email)) return;
			    	if (!formCheck("phone", phone)) return;
			    	
			    	setUserConnection(account,password,email,phone);
				}
				
			});
		 Button resume = (Button) findViewById(R.id.resume);
		 resume.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setViewText(R.id.register_id,"");
					setViewText(R.id.register_password,"");
					setViewText(R.id.confirm_password,"");
					setViewText(R.id.register_email,"");
					setViewText(R.id.register_phone,"");
				
					
				}
				
			});
	}
	

	

	
	private void setUserConnection(String account, String password,String email, String phone) {
	    connection = ((AppGlobalVariable) this.getApplication()).getConnection();
		if(D)Log.d("TAG",connection.getServiceName());
        Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(connection.getServiceName());
		reg.setUsername(account);
		reg.setPassword(password);
		//reg.addAttribute("phone", phone);
		reg.addAttribute("email", email);

		
		PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = connection.createPacketCollector(filter);
		connection.sendPacket(reg);

		result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
		// Stop queuing results
		collector.cancel();// 停止請求results（是否成功的結果）

		if (result == null) {
			Toast.makeText(getApplicationContext(), "SERVER NOT RESPONSE",
					Toast.LENGTH_SHORT).show();
		} else if (result.getType() == IQ.Type.ERROR) {
			if (result.getError().toString().equalsIgnoreCase(
					"conflict(409)")) {
				Toast.makeText(getApplicationContext(), "THIS ID ALREADY EXIST",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "REGISTER FAIL",
						Toast.LENGTH_SHORT).show();
			}
		} else if (result.getType() == IQ.Type.RESULT) {
		

    		Log.i("TAG", "Logged in as " + connection.getUser());

    		// Set the status to available (online)
    		Presence presence = new Presence(Presence.Type.available);
    		connection.sendPacket(presence);

    		//set the connection listener to deal with connection closing and reconnection events
    		connection.addConnectionListener(((AppGlobalVariable) (this).getApplication()).connectionListener);
			Toast.makeText(getApplicationContext(), "REGISTER SUCCESS",
					Toast.LENGTH_SHORT).show();
			

			
			/* new一個Intent物件，並指定class */
	          Intent intent = new Intent();
	        
	          
	          /* new一個Bundle物件，並將要傳遞的資料傳入 */
	          Bundle bundle = new Bundle();
	          bundle.putString("account", account);
		      bundle.putString("password",password);
		 
	          /* 將Bundle物件assign給Intent */
	          intent.putExtras(bundle);
	          setResult(99, intent);
	          finish();
 
		}
    		
    		
    		

		
	}


	
	private boolean formCheck(String type,String str){
		if(str.equals("")){
    		Toast.makeText(RegisterActivity.this, "Your " + type + " shouldn't be empty", Toast.LENGTH_SHORT).show();
    		return false;
    	}
		else
			return true;
	}
	
	
	private String getViewText(int id) {
        EditText widget = (EditText) this.findViewById(id);
        return widget.getText().toString();
    }
	
	private void setViewText(int id, String default_value) {
	    EditText widget = (EditText) this.findViewById(id);
	    widget.setText(default_value,TextView.BufferType.EDITABLE);
	}
	
}
