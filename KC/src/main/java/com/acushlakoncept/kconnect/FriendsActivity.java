package com.acushlakoncept.kconnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class FriendsActivity extends Activity {

	public static final String TAG = FriendsActivity.class.getSimpleName();

	protected ProgressBar mProgressBar;
    private Toolbar mToolbar;
	String name, username;
	TextView mName, mAddress, mPhone, mEmail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_detail);

		//for push notifications
       /* ParseAnalytics.trackAppOpened(getIntent());
        PushService.setDefaultPushCallback(this, FriendsActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();*/

		mName = (TextView) findViewById(R.id.name);
		mAddress = (TextView) findViewById(R.id.address);
		mPhone = (TextView) findViewById(R.id.phone);
		mEmail = (TextView) findViewById(R.id.email);

		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			name = (extras.getString("Name")).toString();
			username = extras.getString("Username").toString();
		} else {
			// set default value for now

		}

		if (name != null) {
			mName.setText(name);
		}
		if (username != null) {
			mEmail.setText(username);

			ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
			userQuery.whereEqualTo("username",username);
			userQuery.findInBackground(new FindCallback<ParseUser>() {
				public void done(List<ParseUser> results, ParseException e) {
					// results has the list of users with a hometown team with a losing record
					for (int j = 0; j < results.size(); j++) {
						String testObjectID = results.get(j).getObjectId();
						Log.d("score testObjectID", testObjectID.toString());

						mPhone.setText(results.get(j).getString("phone"));
						mAddress.setText(results.get(j).getString("address"));
					}


				}
			});

		}
	}




	@Override
	public void onResume() {
		super.onResume();
		//getLatestPosts();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			/*case R.id.addButton:
				startActivity(new Intent(this, AddLinkActivity.class));
				return true;
			case R.id.followButton:
				//startActivity(new Intent(this, SelectUsersActivity.class));
				return true;*/
			case R.id.logoutButton:
			/*
			 * Log current user out using ParseUser.logOut()
			 */
				ParseUser.logOut();
				Intent intent = new Intent(this, LoginOrSignUpActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
            case R.id.refreshButton:
                //Action to refresh list of URLs added
                //getLatestPosts();
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}