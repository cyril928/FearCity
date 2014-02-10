package tw.edu.ntu.csie.srlab.activity;

import greendroid.app.GDActivity;
import greendroid.app.GDApplication;
import greendroid.util.Config;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBar.OnActionBarListener;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.R.id;
import tw.edu.ntu.csie.srlab.R.layout;
import tw.edu.ntu.csie.srlab.service.ConnectService;

import com.cyrilmottier.android.greendroid.R.string;

 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends GDActivity {
	
	private XMPPConnection connection;
	private static final String SETTING_INFOS = "SETTING_info";
	private static final String SETTING_ACCOUNT = "SETTING_ACCOUNT";
	private static final String SETTING_PASSWORD = "SETTING_PASSWORD";
	private static final String SETTING_autologin = "SETTING_autologin";
	private String account;
	private String password;
	private boolean autologin=false;
	final boolean D=true;
	
 
	private void storePrefs() 
	{

		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0);
		settings.edit()

			.putString(SETTING_ACCOUNT, account)
			.putString(SETTING_PASSWORD, password)
			.putBoolean(SETTING_autologin, autologin)
			.commit();

	}
	 private void restorePrefs() 
	{
			SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0);
			account = settings.getString(SETTING_ACCOUNT, "");
			password = settings.getString(SETTING_PASSWORD,"");
			autologin = settings.getBoolean(SETTING_autologin,false);
	  
	}
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);

		setActionBarContentView(R.layout.xmpp_login);
		this.getActionBar().setType(ActionBar.Type.Empty);
		this.getActionBar().setTitle("");
		TextView tv = (TextView) this.findViewById(R.id.gd_action_bar_title);
		//tv.setCompoundDrawablesWithIntrinsicBounds(this .getResources().getDrawable(R.drawable.greendroid_application_logo), null, null, null);
		
		 if (D)Log.i("TAG","getusername "+((AppGlobalVariable) (LoginActivity.this).getApplication()).getusername() );
		 if(((AppGlobalVariable) (LoginActivity.this).getApplication()).getusername().equals("")){
			 connection = ((AppGlobalVariable) (LoginActivity.this).getApplication()).connectionSetup();
		
		     	// Start a connection
		     	try {                                    
		     		connection.connect();
		     		Log.i("TAG", "[XMPPLoginActivity] Connected to " + connection.getHost());
		     	} catch (XMPPException ex) {
		     		Log.e("TAG", "[XMPPLoginActivity] Failed to connect to " + connection.getHost());
		     		Log.e("TAG", ex.toString());
		     		showToast("Connect Server Failed");
		     	}if(D)Log.i("TAG", "connection "+ connection);
				restorePrefs();
				 CheckBox widget = (CheckBox) this.findViewById(R.id.autologin);
				 widget.setChecked(autologin);
				
				 if((!account.equals(""))&&(widget.isChecked()||!(((AppGlobalVariable) (LoginActivity.this).getApplication()).getusername().equals("")))){
		    	// connection has been established and go to HelloGoogleMapView
					loginProduce();

		   	
		       }
				 presentLoginView();
		 }else{
				// go to HelloGoogleMapView
				Intent mapIntent = new Intent();
				mapIntent.setClass(LoginActivity.this, ShowMemberMaps.class);
		    	startActivity(mapIntent); 		
			    finish();
		 }
		
	       
	} 
	
	@Override
	protected void onStart() {
		super.onStart();
 	       // connection has not been established

	}
	
	private void presentLoginView(){
		 		
			setViewText(R.id.userid,account);
			setViewText(R.id.password,password);
		Button login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				account = getViewText(R.id.userid);
		    	password = getViewText(R.id.password);
		    	
		    	((AppGlobalVariable) (LoginActivity.this).getApplication()).setusername(account);
		    	
		    	if(account.equals("") || password.equals("")){
		    		showToast("Your username or password shouldn't be empty");
		    	}else {
//		    		storePrefs();
		    		// get a connection from the AppGlobalVariable
		    		loginProduce();
		    	}
			}
			
		});
		Button sign_up = (Button) findViewById(R.id.sign_up);
		sign_up.setOnClickListener(new OnClickListener () {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// go to register view
				Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
		    	startActivity(registerIntent); 	
			}
			
		});


		
    	EditText widget1 = (EditText) this.findViewById(R.id.userid);
    	
    	widget1.setText("roy" );
    	EditText widget2 = (EditText) this.findViewById(R.id.password);
    	widget2.setText("123" );
	}
	
	
	private void loginProduce(){
		
		// Login a XMPP server
		try {
			connection.login(account, password);
			Log.i("TAG", "Logged in as " + connection.getUser()); 
			
			
		  
		} catch (XMPPException ex) { new AlertDialog.Builder(this)
	      .setTitle(  "Login Fail")
	      .setMessage("May be ID or Pwd Error, \nIf you have any problem \nplease connect admin \nemail:taylor19882002@gmail.com")  
	      .setPositiveButton(string.yes , new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
	    	  
	      }) .show();		
			 
			 connection.disconnect();
			 try {
				connection.connect();
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ;
	 	}
		// Set the status to available (online)
		Presence presence = new Presence(Presence.Type.available);
		connection.sendPacket(presence);

		//set the connection listener to deal with connection closing and reconnection events
		connection.addConnectionListener(((AppGlobalVariable) (LoginActivity.this).getApplication()).connectionListener);
		// go to HelloGoogleMapView
		Intent mapIntent = new Intent();
		mapIntent.setClass(LoginActivity.this, ShowMemberMaps.class);
    	startActivity(mapIntent); 		
	    finish();
	}
	  @Override
	  protected void onActivityResult(int requestCode,int resultCode,Intent data)
	  {
	    switch (resultCode)
	    { 
	      case 99:
	        /* 回傳錯誤時以Dialog顯示 */
	        Bundle bundle = data.getExtras();
	        account = bundle.getString("account");
	        password = bundle.getString("password");
	        //Log.i("###################"+mylocationX,""+mylocationY);
	        loginProduce();
 
	        break;       
	      default: 
	        break; 
	     } 
	   } 
	private void showToast(String str){
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

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
