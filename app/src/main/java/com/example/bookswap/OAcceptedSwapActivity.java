package com.example.bookswap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * the activity for owner to confirm swap with the borrower
 */
public class OAcceptedSwapActivity extends AppCompatActivity {
    private TextView time;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private TextView date;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView comment;
    private Button confirm;
    private Swap swapclass = new Swap();
    private Book swapingBook;
    private TextView bookinfo;
    private Button locationBut;
    private DataBaseUtil u;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oaccepted_swap);

        /**
         * hwo to change actionbar title
         * resource:https://stackoverflow.com/questions/3438276/how-to-change-the-text-on-the-action-bar
         */
        getSupportActionBar().setTitle("Owner Confirm Accept");

        setContentView(R.layout.activity_oaccepted_swap);
        time = (TextView) findViewById(R.id.time_text);
        date = (TextView) findViewById(R.id.date_text);
        bookinfo = (TextView) findViewById(R.id.bookInfo);
        comment = (TextView) findViewById(R.id.comment_text);
        confirm = (Button) findViewById(R.id.confirm);
        locationBut = (Button) findViewById(R.id.locationButton);

        Intent intent = getIntent();
//        Log.d("viewbook",)
        swapingBook = intent.getParcelableExtra("book");
        Log.d("viewbook2",swapingBook.getUnikey());
        String infoDisplay = swapingBook.getTitle() + " by " + swapingBook.getAuthor();
        infoDisplay = infoDisplay.substring(0, Math.min(infoDisplay.length(), 40));
        bookinfo.setText(infoDisplay);

        bookinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OAcceptedSwapActivity.this, ViewBookActivity.class);
                intent.putExtra("book", swapingBook);
                startActivity(intent);
            }
        });


        u = new DataBaseUtil("Bowen");
        u.getSwap(swapingBook, new DataBaseUtil.getSwapInfo() {
            @Override
            public void getSwapInfo(Swap swap) {
                swapclass = swap;
                date.setText(swap.getDate());
                time.setText(swap.getTime());
                comment.setText(swap.getComment());
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                u.changeSwapStatus(swapingBook,"Owner",true);
                showNormalDialog();
                timer();

            }
        });



        locationBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OAcceptedSwapActivity.this, MapViewActivity.class);
                LatLng point = swapclass.getLocation();
                if (point == null){
                    Toast.makeText(getApplicationContext(), "Fatal error, improper location", LENGTH_SHORT).show();
                } else {
                    intent.putExtra("point", point);
                    startActivity(intent);
                }
            }
        });


    }

    /**
     * build timer to make handler can auto
     * check do swapingbook is swap or not
     */
    public void timer(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                u.checkSwapStatus(swapingBook, new DataBaseUtil.swapStatus() {
                    @Override
                    public void getStatus(boolean value) {
                        if(value){
                            u.changeStatus(swapingBook,"Borrowed");
                            u.deleteSwap(swapingBook);
                            u.changeSwapStatus(swapingBook,"Return",false);
                            handler.removeCallbacksAndMessages(null);
                            finish();
                        }
                    }
                });


                handler.postDelayed(this,1000);}
        }, 1000);  //the time is in miliseconds

    }




    /**
     * make a dialog
     * resourse:https://www.cnblogs.com/gzdaijie/p/5222191.html
     */
    private void showNormalDialog(){

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(OAcceptedSwapActivity.this);
        normalDialog.setTitle("Wait for Owner confiem");
        normalDialog.setMessage("Waiting..");
        normalDialog.setPositiveButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        u.changeSwapStatus(swapingBook,"Borrower",false);
                        handler.removeCallbacksAndMessages(null);
                    }
                });

        normalDialog.show();
    }
}
