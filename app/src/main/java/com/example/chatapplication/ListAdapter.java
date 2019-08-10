package com.example.chatapplication;


import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;


import org.w3c.dom.Text;

import java.util.ArrayList;


public class ListAdapter extends ArrayAdapter<ListViewDetails> {

    private Context mContext;
    int mResource;

    //Constructor will take in custom list and array of ListViewDetails objects
    public ListAdapter(Context context, int resource, ArrayList<ListViewDetails> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    //responsible for getting the view and attaching it to the listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //For each item downloaded, get the title and picture URL
        String name = getItem(position).getContactName();
        String lastMessage = getItem(position).getLastMessage();
        String username = getItem(position).getContactUsername();
        //String picture = getItem(position).getContactPicture();

        //Initiate a new object for each set of title and picture
        ListViewDetails details = new ListViewDetails(username, name, lastMessage);

        //Inflate the view
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        //Get the views from list_view_layout
        TextView personName = (TextView) convertView.findViewById(R.id.personName);
        TextView personChat = (TextView) convertView.findViewById(R.id.personChat);
        //ImageView personImage = (ImageView) convertView.findViewById(R.id.personImg);

        //Setup
        personName.setText(name);
        personChat.setText(lastMessage);
        //Picasso.get().load(picture).into(personImage);

        return convertView;
    }
}

