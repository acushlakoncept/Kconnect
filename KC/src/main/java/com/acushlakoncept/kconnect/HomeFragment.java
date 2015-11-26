package com.acushlakoncept.kconnect;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseImageView;
import com.parse.ParseUser;

/**
 * Created by Acushla on 8/3/15.
 */
public class HomeFragment extends Fragment {

    TextView mLabel;
    String uname;
    ImageView mProfileImg;
    ParseImageView imgProfile;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mLabel = (TextView)rootView.findViewById(R.id.label);
       // mProfileImg = (ImageView)rootView.findViewById(R.id.profile_img);
        imgProfile = (ParseImageView) rootView.findViewById(R.id.nav_profile_img);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            uname = currentUser.getUsername();

            mLabel.setText("Welcome " + currentUser.getString("name") + "\n You are logged in as " +
                    currentUser.getUsername());

            /*ParseObject object = new ParseObject("ImageUpload");
            ParseFile image = object.getParseFile("ImageFile");
            //final ParseImageView imgProfile = (ParseImageView)rootView.findViewById(R.id.imgProfile);
            imgProfile.setParseFile(image);
            imgProfile.loadInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    // The image is loaded and displayed!
                    int oldHeight = imgProfile.getHeight();
                    int oldWidth = imgProfile.getWidth();
                    Log.v("LOG!!!!!!", "imageView height = " + oldHeight);      // DISPLAYS 90 px
                    Log.v("LOG!!!!!!", "imageView width = " + oldWidth);        // DISPLAYS 90 px
                }
            });*/

/*
            // Locate the class table named "ImageUpload" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "ImageUpload");

            // Locate the objectId from the class
            query.getInBackground("oamwwmYYDf",
                    new GetCallback<ParseObject>() {

                        public void done(ParseObject object,
                                         ParseException e) {
                            // TODO Auto-generated method stub

                            // Locate the column named "ImageName" and set
                            // the string
                            ParseFile fileObject = (ParseFile) object
                                    .get("ImageFile");
                            fileObject
                                    .getDataInBackground(new GetDataCallback() {

                                        public void done(byte[] data,
                                                         ParseException e) {
                                            if (e == null) {
                                                Log.d("test",
                                                        "We've got data in data.");
                                                // Decode the Byte[] into
                                                // Bitmap
                                                Bitmap bmp = BitmapFactory
                                                        .decodeByteArray(
                                                                data, 0,
                                                                data.length);

                                                // Get the ImageView from
                                                // main.xml
                                               // ImageView image = (ImageView) findViewById(R.id.image);

                                                // Set the Bitmap into the
                                                // ImageView
                                                imgProfile.setImageBitmap(bmp);

                                                // Close progress dialog
                                               // progressDialog.dismiss();

                                            } else {
                                                Log.d("test",
                                                        "There was a problem downloading the data.");
                                            }
                                        }
                                    });
                        }
                    });
*/


        } else {
            // show the signup or login screen
        }

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
