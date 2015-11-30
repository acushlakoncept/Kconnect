package com.acushlakoncept.kconnect;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by Acushla on 11/27/15.
 */
public class ChatListAdapter extends ArrayAdapter<Message> {
    private String mUserId;

    public ChatListAdapter(Context context, String userId, List<Message> messages) {
        super(context, 0, messages);
        this.mUserId = userId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.chat_item, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.imageLeft = (ImageView)convertView.findViewById(R.id.ivProfileLeft);
            holder.imageRight = (ImageView)convertView.findViewById(R.id.ivProfileRight);
            holder.body = (TextView)convertView.findViewById(R.id.tvBody);
            convertView.setTag(holder);
        }
        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder)convertView.getTag();
        final boolean isMe = message.getUserId().equals(mUserId);
        // Show-hide image based on the logged-in user.
        // Display the profile image to the right for our user, left for other users.
        if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        final ImageView profileView = isMe ? holder.imageRight : holder.imageLeft;
        Picasso.with(getContext()).load(getProfileUrl(message.getUserId())).into(profileView);
        holder.body.setText(message.getBody());
        return convertView;
    }

    // Create a gravatar image based on the hash value obtained from userId
    public static String getProfileUrl(final String userId) {
        /*String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        String hash = MD5Util.md5Hex(userId);

        //return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
        return "http://www.gravatar.com/avatar/"+hash;
    }

    public static class MD5Util {
        public static String hex(byte[] array) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i]
                        & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        }

        public static String md5Hex(String message) {
            try {
                MessageDigest md =
                        MessageDigest.getInstance("MD5");
                return hex(md.digest(message.getBytes("CP1252")));
            } catch (NoSuchAlgorithmException e) {
            } catch (UnsupportedEncodingException e) {
            }
            return null;
        }
    }

    final class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView body;
    }

}
