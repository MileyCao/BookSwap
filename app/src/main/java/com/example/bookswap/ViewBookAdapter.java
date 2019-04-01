package com.example.bookswap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewBookAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> availableList;

    /**
     * standard constructor from parent
     * @param context Android context
     * @param resource Android resource (used in super only)
     * @param objects ArrayList of our objects
     */
    public ViewBookAdapter(Context context, int resource, ArrayList<Book> objects) {
        super(context, resource, objects);

        this.availableList = objects;
    }

    /**
     * Method used to create the view for each individual item
     * @param position index of the item in the list
     * @param convertView view object for method to convert, is later returned
     * @param parent parent activity
     * @return A view object used by Android to display our individual item in list view
     */

    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){ // check if given view is null, if it is we inflate
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_available, null);
        }
        // extract our recording from the list
        Log.d("POSITION", Integer.toString(position));
        Book element = availableList.get(position);


        TextView title = (TextView) convertView.findViewById(R.id.vTitle);
        TextView author = (TextView) convertView.findViewById(R.id.vAuthor);
        TextView status = (TextView) convertView.findViewById(R.id.vStatus);
        TextView description = (TextView) convertView.findViewById(R.id.vdescription);
        //TextView owner = (TextView) convertView.findViewById(R.id.owner);
        ImageView bookcover = (ImageView)convertView.findViewById(R.id.bookCover);
        title.setText(element.getTitle());
        author.setText(element.getAuthor());
        if (element.getImageUrl()!= null){
            Picasso.get()
                    .load(element.getImageUrl())
                    .into(bookcover);
        }

        return convertView;
    }

}
