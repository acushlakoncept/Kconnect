package com.acushlakoncept.kconnect;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SelectUsersActivity extends ListActivity {
	
	public static final String TAG = SelectUsersActivity.class.getSimpleName();
	
	protected ParseObject[] mUsers;	
	protected ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_users);
		
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		getAllUsers();
	}
	
	private void getAllUsers() {
		mProgressBar.setVisibility(View.VISIBLE);

		final Users users = new Users();

    	/*
    	 * Get ParseUsers using ParseUser.getQuery();
    	 */



		/*ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> userObjects, ParseException error) {
				mProgressBar.setVisibility(View.INVISIBLE);
				UsersAdapter adapter = new UsersAdapter(SelectUsersActivity.this, mUsers);
				setListAdapter(adapter);
			}
		});*/



		/*ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> userObjects, ParseException error) {
				mProgressBar.setVisibility(View.INVISIBLE);
				if (userObjects != null) {

					ArrayList<Users> userList = new ArrayList<>();

					for (int i = 0; i < userObjects.size(); i++) {

						users.setName(userObjects.get(i).getString("name"));
						users.setAddress(userObjects.get(i).getString("address"));
						users.setUsername(userObjects.get(i).getUsername());
						users.setPhone(userObjects.get(i).getString("phone"));
						userList.add(users);
					}

					UserListAdapter adapter = new UserListAdapter(getApplicationContext(),
							R.layout.list_users, userList);
					setListAdapter(adapter);
				}
			}
		});*/


		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> userObjects, ParseException error) {
				mProgressBar.setVisibility(View.INVISIBLE);
				if (userObjects != null) {
					ArrayList<HashMap<String, String>> articles = new ArrayList<HashMap<String, String>>();
					for (ParseObject result : userObjects) {
						HashMap<String, String> article = new HashMap<String, String>();
						article.put("name",
								result.getString("name"));
						article.put("username",
								result.getString("username"));
						articles.add(article);
					}
					SimpleAdapter adapter = new SimpleAdapter(
							getApplicationContext(), articles,
							android.R.layout.simple_list_item_2, new String[]{
							"name",
							"username"}, new int[]{
							android.R.id.text1, android.R.id.text2});
					setListAdapter(adapter);

				} else {
					Log.e("App", "Error: ");
				}
			}
		});

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		TextView name1 = (TextView) v.findViewById(android.R.id.text1);
		TextView username2 = (TextView) v.findViewById(android.R.id.text2);
		/*Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(urlLabel.getText().toString()));
		startActivity(intent);*/

		Intent intent = new Intent(getBaseContext(), FriendsActivity.class)
				.putExtra("Name", name1.getText().toString())
				.putExtra("Username", username2.getText().toString());
		startActivity(intent);
	}

	/*
	 * Helper method to remove the logged-in user from the list of all users
	 */
	private List<ParseObject> removeCurrentUser(List<ParseObject> objects) {
		ParseObject userToRemove = null;
		
		for (ParseObject user : objects) {
			if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
				userToRemove = user;
			}
		}
		
		if (userToRemove != null) {
			objects.remove(userToRemove);
		}

		return objects;
	}
}
