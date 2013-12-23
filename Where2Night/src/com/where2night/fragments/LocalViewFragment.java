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

package com.where2night.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.where2night.R;
import com.where2night.utilities.ObservableScrollView;

public class LocalViewFragment extends Fragment implements ObservableScrollView.Callbacks, OnClickListener {
    private LinearLayout mStickyView;
    private View mPlaceholderView;
    private ObservableScrollView mObservableScrollView;
    private Button btnLocalMap;
    private Button btnLocalEvents;
    private Button btnLocalLists;
    private Button btnLocalAttendees;
    private Button btnLocalJukebox;
    private Button anterior;

    public LocalViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_local_view, container, false);

        mObservableScrollView = (ObservableScrollView) rootView.findViewById(R.id.scroll_view);
        mObservableScrollView.setCallbacks(this);

        mStickyView = (LinearLayout) rootView.findViewById(R.id.sticky);
        mPlaceholderView = rootView.findViewById(R.id.placeholder);

        mObservableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        onScrollChanged(mObservableScrollView.getScrollY());
                    }
                });

        
        btnLocalMap = (Button)rootView.findViewById(R.id.btnLocalMap);
        btnLocalEvents = (Button)rootView.findViewById(R.id.btnLocalEvents);
        btnLocalLists = (Button)rootView.findViewById(R.id.btnLocalLists);
        btnLocalAttendees = (Button)rootView.findViewById(R.id.btnLocalAttendees);
        btnLocalJukebox = (Button)rootView.findViewById(R.id.btnLocalJukebox);
        
        anterior = btnLocalMap;
        
        btnLocalMap.setOnClickListener(this);
        btnLocalEvents.setOnClickListener(this);
        btnLocalLists.setOnClickListener(this);
        btnLocalAttendees.setOnClickListener(this);
        btnLocalJukebox.setOnClickListener(this);
        
        
        
        return rootView;
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
