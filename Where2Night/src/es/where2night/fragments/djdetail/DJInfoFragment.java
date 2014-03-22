package es.where2night.fragments.djdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.where2night.R;

public class DJInfoFragment extends Fragment implements OnClickListener{

	private Button btnFollowMe;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dj_info, container, false);

		btnFollowMe = (Button) view.findViewById(R.id.btnFollowMe);
        btnFollowMe.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		 if (v.getId() == btnFollowMe.getId()){
			 if (btnFollowMe.isSelected())
				 btnFollowMe.setSelected(false);
			 else
				 btnFollowMe.setSelected(true);
		 }
	}
}