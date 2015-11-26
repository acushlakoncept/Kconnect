package com.acushlakoncept.kconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class PasswordReset extends AppCompatActivity {

	protected EditText mEmailField;
	protected Button mResetButton;
	protected ProgressBar mProgressBar;
	private Toolbar mToolbar;
	protected TextView mInfoText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

		mEmailField = (EditText) findViewById(R.id.email);
		mResetButton = (Button) findViewById(R.id.reset_btn);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		mInfoText = (TextView)findViewById(R.id.info);


		mResetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mProgressBar.setVisibility(View.VISIBLE);

				String email = mEmailField.getText().toString();

				ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
					public void done(ParseException e) {
						if (e == null) {
							mProgressBar.setVisibility(View.INVISIBLE);
							// An email was successfully sent with reset instructions.
							mInfoText.setText(getString(R.string.info_reset_password_success));
						} else {
							// Something went wrong. Look at the ParseException to see what's up.
							mInfoText.setText(getString(R.string.info_reset_password_failure));
						}
					}
				});
			}
		});
	}

}
