package com.acushlakoncept.kconnect;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by macbook on 11/22/15.
 */
public class SignUpActivity extends AppCompatActivity {

    protected String mAction;

    protected EditText mEmailField;
    protected EditText mPasswordField;
    protected EditText mNameField;
    protected EditText mPhoneField;
    protected EditText mAddressField;
    protected Button mButton;
    protected ProgressBar mProgressBar;

    private static int RESULT_LOAD_CAMERA_IMAGE = 2;
    private static int RESULT_LOAD_GALLERY_IMAGE = 1;
    private String mCurrentPhotoPath;
    private ImageView imgPhoto;
    private Button btnUploadImage;
    private File cameraImageFile;
    private TextView mTextView;
    private Toolbar mToolbar;

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == RESULT_LOAD_GALLERY_IMAGE && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();

            } else if (requestCode == RESULT_LOAD_CAMERA_IMAGE) {
                mCurrentPhotoPath = cameraImageFile.getAbsolutePath();
            }

            File image = new File(mCurrentPhotoPath);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            imgPhoto.setImageBitmap(bitmap);
        }
    }

    private File createImageFile () throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File folder = new File(storageDir.getAbsolutePath() + "/KCFolder");

        if (!folder.exists()) {
            folder.mkdir();
        }

        cameraImageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                folder      /* directory */
        );

        return cameraImageFile;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        imgPhoto = (ImageView)findViewById(R.id.imgPhoto);
        imgPhoto.setOnClickListener(chooseImageListener);

        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        mNameField = (EditText)findViewById(R.id.name);
        mPhoneField = (EditText)findViewById(R.id.phone);
        mAddressField = (EditText) findViewById(R.id.address);
        mButton = (Button) findViewById(R.id.signup_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.signup_progress);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressBar.setVisibility(View.VISIBLE);


                String username = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();
                String name = mNameField.getText().toString();
                String phone = mPhoneField.getText().toString();
                String address = mAddressField.getText().toString();

                /*
					 * Sign up using ParseUser
					 */
                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.put("name", name);
                user.put("phone", phone);
                user.put("address", address);


                byte[] image = null;

                try {
                    image = readInFile(mCurrentPhotoPath);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }

                if (image != null) {

                    // Create the ParseFile
                    ParseFile file = new ParseFile(username + ".png", image);
                    // Upload the image into Parse Cloud
                    file.saveInBackground();
                    // Create a New Class called "ImageUpload" in Parse
                    ParseObject imgupload = new ParseObject("ImageUpload");
                    // Create a column named "ImageName" and set the string
                    imgupload.put("ImageName", username);
                    // Create a column named "ImageFile" and insert the image
                    imgupload.put("ImageFile", file);
                    // Create the class and the columns
                    imgupload.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            Toast.makeText(getBaseContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                        }
                    });
                } else { Toast.makeText(getBaseContext(), "Image cannot be loaded at the moment", Toast.LENGTH_LONG).show();}

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if (e == null) {
                            // Hooray! Let them use the app now.
                            startActivity(new Intent(
                                    SignUpActivity.this,
                                    MainActivity.class));
                        } else {
                            // Sign up didn't succeed. Look at the
                            // ParseException to figure out what went wrong
                            Toast.makeText(SignUpActivity.this,
                                    "Sign up failed! Try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }

    View.OnClickListener chooseImageListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogChooseFrom();
        }
    };


    private void dialogChooseFrom(){

        final CharSequence[] items={"From Gallery","From Camera"};

        AlertDialog.Builder chooseDialog =new AlertDialog.Builder(this);
        chooseDialog.setTitle("Pick your choice").setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(items[which].equals("From Gallery")){

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_GALLERY_IMAGE);

                } else {

                    try {

                        File photoFile = createImageFile();
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, RESULT_LOAD_CAMERA_IMAGE);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        chooseDialog.show();
    }

    private byte[] readInFile(String path) throws IOException {

        byte[] data = null;
        File file = new File(path);
        InputStream input_stream = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        data = new byte[16384]; // 16K
        int bytes_read;

        while ((bytes_read = input_stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytes_read);
        }

        input_stream.close();
        return buffer.toByteArray();
    }
}
