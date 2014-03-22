package es.where2night.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.where2night.R;

import es.where2night.fragments.EventsFragment;
import es.where2night.fragments.localdetail.LocalDiscountListFragment;
import es.where2night.fragments.localdetail.LocalInfoFragment;

public class LocalViewActivity extends FragmentActivity implements OnClickListener, ActionBar.TabListener  {
   
    public static final String ID = "id";
	private Button btnIGo;
    private int lastIndex = 0;
    private String[] tabs = { "Info", "Eventos", "Listas", "Asistentes", "Gramola"}; //TODO modificar a @strings
    
    private Fragment[] fragments = new Fragment[]{ new LocalInfoFragment(),
    											   new EventsFragment(),
    											   new LocalDiscountListFragment()};
    
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.activity_local_view);
        
        final ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.logo7);
        actionBar.setTitle("Pacha");
        actionBar.setHomeButtonEnabled(true);
        
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        btnIGo = (Button) findViewById(R.id.btnIGo);
        
        btnIGo.setOnClickListener(this);
        
        
        for (int i = 0; i<tabs.length; i++)
        actionBar.addTab(
                actionBar.newTab()
                        .setText(tabs[i])
                        .setTabListener(this));
        
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
        	    .add(R.id.selectedTabFragment, fragments[0])
        	    .add(R.id.selectedTabFragment, fragments[1])
        	    .commit();	
        
        manager.beginTransaction().hide(fragments[1])
				        		  .commit();
        
        setContent(0);
        
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.local_view, menu);
		return true;
	}
    
    public void setContent(int index) {
	    Fragment toHide = null;
		Fragment toShow = null;
		
		toHide = fragments[lastIndex];
		toShow =  fragments[index];
		lastIndex = index;
		
		FragmentManager manager = getSupportFragmentManager();
		
		manager.beginTransaction()
				.hide(toHide)
				.show(toShow)
				.commit();
		
		if (index == 1) ((EventsFragment) toShow).fill();
    }
    

	@Override
	public void onClick(View v) {
		 if (v.getId() == btnIGo.getId()){
			 if (btnIGo.isSelected())
				 btnIGo.setSelected(false);
			 else
				 btnIGo.setSelected(true);
		 }
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		setContent(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
}
