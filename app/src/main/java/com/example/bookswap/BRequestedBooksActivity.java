package com.example.bookswap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BRequestedBooksActivity extends AppCompatActivity {
    private ListView requestedbooks;
    private ArrayList<Book> req_book = new ArrayList<Book>();
    private ArrayAdapter<Book> adapter;
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_books);
        requestedbooks = (ListView) findViewById(R.id.BRB_listview);
        Intent intent = getIntent();
    }

    /**
     * load date and display a list
     */
    protected void onStart() {

        super.onStart();
        //todo display requested book list




        adapter = new BRequestedBooksAdapter(this, req_book);

        DataBaseUtil u;
        u = new DataBaseUtil("Bowen");
        u.getBorrowerBok(new DataBaseUtil.getNewBook(){
            @Override
            public void getNewBook(Book a){
                if(true) {
                    req_book.add(a);
                }
                requestedbooks.setAdapter(adapter);
            }
        });
        Book abook = new Book("asdfhaskdjfhak", "adsfa", "fasdfasdf", "asdjfhakjdfhlaksdfhlkahjdsfhakldsfhaksdjfhskdajlfhaskdljfhlaskjdfa", "baba");
        req_book.add(abook);
        requestedbooks.setAdapter(adapter);
        //todo: onclick listener: once select a book
            //todo: once clicked start activity: list user requested
    }
}
