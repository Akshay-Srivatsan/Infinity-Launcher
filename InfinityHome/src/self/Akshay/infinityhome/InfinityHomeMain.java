package self.Akshay.infinityhome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class InfinityHomeMain extends Activity {
	//String s;
	GridView gv = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infinity_home_main);
		gv = (GridView)findViewById(R.id.appGrid);

	}
	
	ArrayList<ApplicationInfo> mApplications;
	AppAdapter adapter=null;
	PackageManager pm=null;
	@Override
	public void onStart() {
		super.onStart();


		pm=getPackageManager();

        Intent main=new Intent(Intent.ACTION_MAIN, null);
            
        main.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> launchables=pm.queryIntentActivities(main, 0);
        
        Collections.sort(launchables,
                         new ResolveInfo.DisplayNameComparator(pm)); 
        
        adapter=new AppAdapter(pm, launchables);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(l);
        

    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId())  {
	    	case R.id.resetDefault:
	    			pm.clearPackagePreferredActivities("self.Akshay.infinityhome");
	    			return true;
	    	default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	OnItemClickListener l = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> adapV, View v, int p,
				long id) {
			ResolveInfo r = adapter.getItem(p);
		    ActivityInfo activity = r.activityInfo;
		    ComponentName name = new ComponentName(activity.applicationInfo.packageName,
		                                         activity.name);
		    Intent i=new Intent(Intent.ACTION_MAIN);
		    
		    i.addCategory(Intent.CATEGORY_LAUNCHER);
		    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
		                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		    i.setComponent(name);
		    
		    startActivity(i);    

			
		}
		
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.infinity_home_main, menu);
		return true;
	}
	
	  class AppAdapter extends ArrayAdapter<ResolveInfo> {
		    private PackageManager pm=null;
		    
		    AppAdapter(PackageManager pm, List<ResolveInfo> apps) {
		      super(InfinityHomeMain.this, R.layout.grid, apps);
		      this.pm=pm;
		    }
		    
		    @Override
		    public View getView(int position, View convertView,
		                          ViewGroup parent) {
		      if (convertView==null) {
		        convertView=newView(parent);
		      }
		      
		      bindView(position, convertView);
		      
		      return(convertView);
		    }
		    
		    private View newView(ViewGroup parent) {
		      return(getLayoutInflater().inflate(R.layout.grid, parent, false));
		    }
		    
		    private void bindView(int position, View cell) {
		      TextView label=(TextView)cell.findViewById(R.id.label);
		      
		      label.setText(getItem(position).loadLabel(pm));
		      
		      ImageView icon=(ImageView)cell.findViewById(R.id.icon);
		      
		      icon.setImageDrawable(getItem(position).loadIcon(pm));
		    }
		  }
	  
	  
	  
	  public void goHome() {

			Intent intent = new Intent("android.intent.action.MAIN");
		    intent.setComponent(ComponentName.unflattenFromString("com.android.launcher"));
		    intent.addCategory("android.intent.category.HOME");
		    startActivity(intent);
	  }


}
