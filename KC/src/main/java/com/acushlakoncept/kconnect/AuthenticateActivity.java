package com.acushlakoncept.kconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class AuthenticateActivity extends AppCompatActivity {

	protected String mAction;

	protected EditText mEmailField;
	protected EditText mPasswordField;
	protected Button mButton;
	protected ProgressBar mProgressBar;
	protected Button mForgetPasswordBtn;
	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticate);

		mEmailField = (EditText) findViewById(R.id.editText1);
		mPasswordField = (EditText) findViewById(R.id.editText2);
		mButton = (Button) findViewById(R.id.button_signup_signin);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mForgetPasswordBtn = (Button)findViewById(R.id.forget_password);


		Bundle bundle = getIntent().getExtras();
		mAction = bundle.getString(LoginOrSignUpActivity.TYPE);

		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mProgressBar.setVisibility(View.VISIBLE);

				String username = mEmailField.getText().toString();
				String password = mPasswordField.getText().toString();

				if (mAction.equals(LoginOrSignUpActivity.SIGNUP)) {

					/*
					 * Sign up using ParseUser
					 */
					ParseUser user = new ParseUser();
					user.setUsername(username);
					user.setPassword(password);

					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							mProgressBar.setVisibility(View.INVISIBLE);
							if (e == null) {
								// Hooray! Let them use the app now.
								startActivity(new Intent(
										AuthenticateActivity.this,
										MainActivity.class));
							} else {
								// Sign up didn't succeed. Look at the
								// ParseException to figure out what went wrong
								Toast.makeText(AuthenticateActivity.this,
										"Sign up failed! Try again.",
										Toast.LENGTH_LONG).show();
							}
						}
					});
				} else {
                    mButton.setText(getResources().getString(R.string.login_button_label));
					/*
					 * Login using ParseUser
					 */
					ParseUser.logInInBackground(username, password,
							new LogInCallback() {
								public void done(ParseUser user,
												 ParseException e) {
									mProgressBar.setVisibility(View.INVISIBLE);
									if (user != null) {
										// Hooray! The user is logged in.
										startActivity(new Intent(
												AuthenticateActivity.this,
												MainActivity.class));
									} else {
										// Login failed. Look at the
										// ParseException to see what happened.
										Toast.makeText(
												AuthenticateActivity.this,
												"Login failed! Try again.",
												Toast.LENGTH_LONG).show();
									}
								}
							});
				}
			}
		});

		mForgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// Start the password reset activity
				startActivity(new Intent(
						AuthenticateActivity.this, PasswordReset.class
				));
			}
		});
	}

}
