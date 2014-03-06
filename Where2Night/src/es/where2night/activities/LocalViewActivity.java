/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.where2night.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.where2night.R;

import es.where2night.fragments.LocalsFragment;
import es.where2night.fragments.MapFragment;
import es.where2night.utilities.ObservableScrollView;

public class LocalViewActivity extends FragmentActivity implements ObservableScrollView.Callbacks, OnClickListener {
   
	private LinearLayout mStickyView;
    private View mPlaceholderView;
    private ObservableScrollView mObservableScrollView;
    private Button btnLocalMap;
    private Button btnLocalEvents;
    private Button btnLocalLists;
    private Button btnLocalAttendees;
    private Button btnLocalJukebox;
    private Button anterior;
    private int lastIndex = 0;
    
    private Fragment[] fragments = new Fragment[]{ new MapFragment() };
    
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.activity_local_view);

        mObservableScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        mObservableScrollView.setCallbacks(this);

        mStickyView = (LinearLayout) findViewById(R.id.sticky);
        mPlaceholderView = findViewById(R.id.placeholder);

        mObservableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onScrollChanged(mObservableScrollView.getScrollY());
                    }
                });

        
        btnLocalMap = (Button)findViewById(R.id.btnLocalMap);
        btnLocalEvents = (Button)findViewById(R.id.btnLocalEvents);
        btnLocalLists = (Button)findViewById(R.id.btnLocalLists);
        btnLocalAttendees = (Button)findViewById(R.id.btnLocalAttendees);
        btnLocalJukebox = (Button)findViewById(R.id.btnLocalJukebox);
        
        anterior = btnLocalMap;
        
        btnLocalMap.setOnClickListener(this);
        btnLocalEvents.setOnClickListener(this);
        btnLocalLists.setOnClickListener(this);
        btnLocalAttendees.setOnClickListener(this);
        btnLocalJukebox.setOnClickListener(this);
        
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
        	    .add(R.id.selectedTabFragment, fragments[0])
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
    }
    

    @Override
    public void onScrollChanged(int scrollY) {
        mStickyView.setTranslationY(Math.max(mPlaceholderView.getTop(), scrollY));
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent() {
    }

	@Override
	public void onClick(View v) {
		anterior.setBackgroundResource(R.drawable.no_activo);
		if (v.getId() == btnLocalMap.getId()){
			btnLocalMap.setBackgroundResource(R.drawable.activo);
			anterior = btnLocalMap;
		} else if (v.getId() == btnLocalEvents.getId()){
			btnLocalEvents.setBackgroundResource(R.drawable.activo);
			anterior = btnLocalEvents;
		} else if (v.getId() == btnLocalLists.getId()){
			btnLocalLists.setBackgroundResource(R.drawable.activo);
			anterior = btnLocalLists;
		} else if (v.getId() == btnLocalAttendees.getId()){
			btnLocalAttendees.setBackgroundResource(R.drawable.activo);
			anterior = btnLocalAttendees;
		} else if (v.getId() == btnLocalJukebox.getId()){
			btnLocalJukebox.setBackgroundResource(R.drawable.activo);
			anterior = btnLocalJukebox;
		} 
	}
}
