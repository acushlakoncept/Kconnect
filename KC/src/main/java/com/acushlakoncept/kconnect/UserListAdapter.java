package com.acushlakoncept.kconnect;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Acushla on 3/31/15.
 */
public class UserListAdapter extends ArrayAdapter<Users> {

    private Context context;
    private List<Users> usersList;

    private LruCache<String, Bitmap> imageCache;

    // create a field of the RequestQueue to be used by volley
    // so it persist through out the life time of the class
    //private RequestQueue queue;

    //private OkHttpClient client = new OkHttpClient();




        public UserListAdapter(Context context, int resources, List<Users> objects) {
        super(context, resources, objects);
        this.context = context;
        this.usersList = objects;

       /* final int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/8;
        imageCache = new LruCache<>(cacheSize);*/

        // instantiate the queue field and passing current context
        // so its associated with proper activity
        //queue = Volley.newRequestQueue(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_user, parent, false);

        // Display places name in the TextView widget
        final Users users = usersList.get(position);
        TextView name = (TextView)view.findViewById(R.id.user_name);
        TextView address = (TextView)view.findViewById(R.id.address);
        TextView email = (TextView)view.findViewById(R.id.email);
        TextView phone = (TextView)view.findViewById(R.id.phone);
        //TextView openNow = (TextView)view.findViewById(R.id.openNow);

        name.setText(users.getName());
        address.setText(users.getAddress());
        email.setText(users.getUsername());
        phone.setText(users.getPhone());
//        if(places.isOpenNow()){
//            openNow.setText("Open");
//        } else {
//            openNow.setText("Closed");
//        }

        //Display place photo in ImageView widget
        /*Bitmap bitmap = imageCache.get(users.getObjectId());
        // for volley
        final ImageView image = (ImageView) view.findViewById(R.id.user_image);
        if (bitmap != null){
            //For the volley, this commented line of code is refactored so it can
            // be called outside this block
            //ImageView image = (ImageView) view.findViewById(R.id.place_image);
            image.setImageBitmap(users.getBitmap());
        }
         else {
            // Retrieves image url
            //String imageUrl = places.getIconUrl();
            // declare instance of image request
            *//*ImageRequest request = new ImageRequest(imageUrl,
                    new Response.Listener<Bitmap>(){
                        @Override
                        public void onResponse(Bitmap arg0) {
                            image.setImageBitmap(arg0);
                            imageCache.put(places.getId(), arg0);
                        }
                    },
                    80, 80,
                    Bitmap.Config.ARGB_8888,

                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError arg0) {
                            Log.d("PlacesAdapter", arg0.getMessage());
                        }
                    }
                    );

            //adding request to queue
            queue.add(request);*//*

            *//*
                The line of code below is used by the ImageLoader AsyncTask
                Uncomment if using AsyncTask
             *//*
           *//* PlaceAndView container = new PlaceAndView();
            container.users = users;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);*//*

        }*/

        return view;
    }

    // Used with AsyncTask, since am using Volley, no need

    class PlaceAndView {
        public Users users;
        public View view;
        public Bitmap bitmap;
    }

    // Using the Volley Method does what the AsyncTask do
    // Uncomment to use

    /*private class ImageLoader extends AsyncTask<PlaceAndView, Void, PlaceAndView> {
        @Override
        protected PlaceAndView doInBackground(PlaceAndView... params) {

            PlaceAndView container = params[0];
            Users users = container.users;

            try {
                String imageUrl = users.getImageUrl();

                // Using OkHttpClient to fetch images
                HttpURLConnection con = client.open(new URL(imageUrl));
                InputStream in = con.getInputStream();

                //InputStream in = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                users.setBitmap(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(PlaceAndView result) {
            ImageView image = (ImageView) result.view.findViewById(R.id.user_image);
            image.setImageBitmap(result.bitmap);
            // saves image for future use
            //result.places.setBitmap(result.bitmap);

            imageCache.put(result.users.getObjectId(), result.bitmap);

        }
    }*/

}
