package es.where2night.fragments.friendProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.where2night.R;

import es.where2night.activities.FriendViewActivty;

public class FriendInfoFragment extends Fragment implements OnClickListener {
	
	private NetworkImageView imgFriend;
	private TextView txtNameAndSurnameFriend;
	private TextView txtNumberOfFriends;
	private TextView txtBirthdayFriend;
	private TextView txtMusicFriendLike;
	private TextView txtDescriptionFriend;
	private ProgressBar pgFriendView;
	
	private Button btnAddAsFriend;
	private int friends = 0;
	
	String friendId;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_info, container, false);

		friendId = getArguments().getString(FriendViewActivty.ID);
		
		imgFriend = (NetworkImageView) view.findViewById(R.id.imgFriend);
		txtNameAndSurnameFriend = (TextView) view.findViewById(R.id.txtNameAndSurnameFriend);
		txtNumberOfFriends = (TextView) view.findViewById(R.id.txtNumberOfFriends);
		txtBirthdayFriend = (TextView) view.findViewById(R.id.txtBirthdayFriend);
		txtMusicFriendLike = (TextView) view.findViewById(R.id.txtMusicFriendLike);
		txtDescriptionFriend = (TextView) view.findViewById(R.id.txtDescriptionFriend);
		pgFriendView = (ProgressBar) view.findViewById(R.id.pgFriendView);
		btnAddAsFriend = (Button) view.findViewById(R.id.btnAddAsFriend);
        btnAddAsFriend.setOnClickListener(this);
        
        //fillData();
        
		return view;
	}
	
	@Override
	public void onClick(View v) {
		 if (v.getId() == btnAddAsFriend.getId()){
			 if (btnAddAsFriend.isSelected()){
			 	friends--;
			 }
			 else{
				 btnAddAsFriend.setSelected(true);
				 friends++;
			 }
		 }
	}

}
