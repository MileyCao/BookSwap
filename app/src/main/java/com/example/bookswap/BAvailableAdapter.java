package com.example.bookswap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BAvailableAdapter extends ArrayAdapter<Book> {
    private ArrayList<Book> ava_booklist;
    public BAvailableAdapter(Context context, ArrayList<Book> ava_books) {
        super(context,R.layout.element_available2, ava_books);
        this.ava_booklist = ava_books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BAvailableAdapter.ViewHolder holder = null;

        if (convertView == null) {
            holder = new BAvailableAdapter.ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_available2, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.BAB_title_textview);
            holder.author = (TextView) convertView.findViewById(R.id.BAB_author_textview);

            holder.bookcover = (ImageView) convertView.findViewById(R.id.BAB_bookCover_imageview);
            convertView.setTag(holder);
        }
        else {
            holder = (BAvailableAdapter.ViewHolder) convertView.getTag();
        }
        Book element = ava_booklist.get(position);

        holder.title.setText("Title: "+(String)element.getTitle());
        holder.author.setText("Author: "+(String)element.getAuthor());

        //holder.bookcover.setImageBitmap(element.getImage());
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View customView = inflater.inflate(R.layout.element_available2, parent, false);
//
        return convertView;
    }

    public static class ViewHolder {
        public TextView title;
        public TextView author;

        public ImageView bookcover;
    }
}
