package tw.edu.ntu.csie.srlab.activity;


import greendroid.app.GDListActivity;
import greendroid.widget.ItemAdapter;
import greendroid.widget.item.Item;
import greendroid.widget.item.ThumbnailItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import tw.edu.ntu.csie.srlab.AppGlobalVariable;
import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.R.id;
import tw.edu.ntu.csie.srlab.R.layout;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class XMPPRosterList extends GDListActivity {
	
	// Array adapter for the roster listview
    private ArrayAdapter<String> mRosterArrayAdapter;
	
    // debugging
    private static final boolean D = false;
    private static final String TAG = "XMPPRosterList";
    
    
    private XMPPConnection connection;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//setActionBarContentView(R.layout.roster_view);
        if(D) Log.d(TAG,"++++ ON CREATE +++++");
        // Eliminate title bar in the main activity
     
        
        // Initialize array adapters for show roster item
        
        List<Item> items = new ArrayList<Item>();

   
        this.connection = ((AppGlobalVariable)getApplication()).getConnection();
 
        	Roster roster = connection.getRoster();
         
        	Collection<RosterEntry> entries = roster.getEntries();
     
           for (RosterEntry entry:entries) {
                 
                Presence presence = roster.getPresence(entry.getUser());
                if(presence.getType() == Presence.Type.available){
                	items.add(new ThumbnailItem(entry.getName()   , "online", R.drawable.green_35));
                }else{
                	items.add(new ThumbnailItem(entry.getName()   , "offline", R.drawable.green_35));
                }
                Log.i("TAG","presence.getType()  "+presence.getType()  );
                Log.i("TAG","getUser() "+entry.getUser()  );
                Log.i("TAG","getName() "+entry.getName()  );
                Log.i("TAG","getType() "+entry.getType() );
            }
            ItemAdapter adapter = new ItemAdapter(this, items);
 
            setListAdapter(adapter);
         
	}
	
	@Override 
    protected void onStart() {
    	super.onStart();
    	if(D) Log.d(TAG,"++++ ON START +++++");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if(D) Log.d(TAG,"++++ ON RESUME +++++");
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	if(D) Log.d(TAG,"++++ ON PAUSE +++++");
    }
	
    @Override
    protected void onStop() {
    	if(D) Log.d(TAG, "+++ ON STOP +++");
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	if(D) Log.d(TAG, "+++ ON DESTORY +++");
        super.onDestroy();      
    }

 
    
 
    
    
}
