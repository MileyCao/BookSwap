package com.example.bookswap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RequestedBooks extends AppCompatActivity {

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_books);
    }

    /**
     * load date and display a list
     */
    protected void onStart() {

        super.onStart();
        //todo display requested book list
        //todo: onclick listener: once select a book
            //todo: once clicked start activity: list user requested
    }
}
