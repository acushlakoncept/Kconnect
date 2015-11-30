package com.acushlakoncept.kconnect;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
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


		/*ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> userObjects, ParseException error) {
				mProgressBar.setVisibility(View.INVISIBLE);
				if (userObjects != null) {
					ArrayList<HashMap<String, String>> articles = new ArrayList<HashMap<String, String>>();
					ParseUser currentUser = ParseUser.getCurrentUser();
					//String currentUser = parseUser.getCurrentUser().toString();

					for (ParseObject result : userObjects) {
						HashMap<String, String> article = new HashMap<String, String>();

						if (result.getString("username").equals(currentUser.getUsername())){

							// Do nothing

						} else {
							article.put("name",
									result.getString("name"));
							article.put("username",
									result.getString("username"));
							articles.add(article);
						}

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
		});*/



		ParseQuery query = ParseUser.getQuery();
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				mProgressBar.setVisibility(View.INVISIBLE);

				if (e == null) {
					objects = removeCurrentUser(objects);
					mUsers = objects.toArray(new ParseObject[0]);

					// Get user relations
					ParseRelation userRelations = ParseUser.getCurrentUser().getRelation("UserRelation");
					userRelations.getQuery().findInBackground(new FindCallback<ParseObject>() {
						@Override
						public void done(List<ParseObject> results, ParseException e) {
							if (e == null) {
								UsersAdapter2 adapter = new UsersAdapter2(getApplicationContext(), mUsers, new ArrayList<ParseObject>(results));
								setListAdapter(adapter);
							}
							else {
								Log.e(TAG, "Exception caught!", e);
							}
						}
					});
				}
				else {
					// Something went wrong.
					Toast.makeText(SelectUsersActivity.this, "Sorry, there was an error getting users!", Toast.LENGTH_LONG).show();
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
