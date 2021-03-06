package com.example.bookswap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 *
 * This class is for DataBase
 * developers can use it easily
 * they can add book and get book from database
 * they can check book status and other info
 *
 */
public class DataBaseUtil {

    private String BookKey;
    private String BookTitle;
    private String BookDes;
    private String BookISBN;
    private String BookStatus;
    private String BookAuthor;
    private String userName;

    private String getPassword;
    private String getEmail;
    private String getPhone;
    private String getAddress;


    private ArrayList<String> bookUniKeyList = new ArrayList<String>();
    private Book aBook;
    public ArrayList<Book> bookArray = new ArrayList<Book>();
    private DatabaseReference BookDatabase;
    private DatabaseReference UserDatabase;
    private DatabaseReference ALlData;

    /**
     * backgroud empty constructor
     */
    public DataBaseUtil(){
        UserDatabase = FirebaseDatabase.getInstance().getReference("User");
        BookDatabase = FirebaseDatabase.getInstance().getReference("Book");
        ALlData = FirebaseDatabase.getInstance().getReference();
    }

    /**
     *  @param name backgroud string for backgroud user name
     *
     */
    public DataBaseUtil(String name){
        UserDatabase = FirebaseDatabase.getInstance().getReference("User");
        BookDatabase = FirebaseDatabase.getInstance().getReference("Book");
        ALlData = FirebaseDatabase.getInstance().getReference();
        this.userName = name;
    }

    /**
     * interface for get Book info
     */
    public interface getNewBook{
        void getNewBook(Book aBook);
    }

    /**
     * trans string to bitmap
     * @param encodedString
     * @return
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    /**
     *  get book info
     *  this function is for Owner
     *  It can get all owner Book
     *  And it can be filtered by status in the activity
     *  @param callBack backgroud interface for
     */
    public void testAllInfoBook__3(final getNewBook callBack){
        ALlData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> allBookkey = new ArrayList<>();
                ArrayList<Book> allBook = new ArrayList<>();
                for (DataSnapshot bookKeys: dataSnapshot.child("User").child(userName).child("Book").getChildren()){
                    String key = bookKeys.getKey();
                    allBookkey.add(key);
                    Book book = new Book();
                    book.setDescription(dataSnapshot.child("Book").child(key).child("Description").getValue(String.class));
                    book.setStatus(dataSnapshot.child("Book").child(key).child("Status").getValue(String.class));
                    book.setTitle(dataSnapshot.child("Book").child(key).child("Title").getValue(String.class));
                    book.setOwner(dataSnapshot.child("Book").child(key).child("Owner").getValue(String.class));
                    book.setISBN(dataSnapshot.child("Book").child(key).child("ISBN").getValue(String.class));
                    book.setAuthor(dataSnapshot.child("Book").child(key).child("Author").getValue(String.class));
                    book.setImageUrl(dataSnapshot.child("Book").child(key).child("Photo").getValue(String.class));
                    book.setUnikey(dataSnapshot.child("Book").child(key).child("UniKey").getValue(String.class));
                    callBack.getNewBook(book);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    /**
     *
     * @param book the book you are checking
     * @param status
     */
    public void changeStatus(Book book, String status){
        BookDatabase.child(book.getUnikey()).child("Status").setValue(status);
    }

    /**
     *  Adding backgroud new book to database
     * @param book add this book to database
     */
    public void addNewBook(Book book){
        if (book.getUnikey() == null) {
            UUID number = UUID.randomUUID();
            String BookKey = number.toString();
            this.BookKey = BookKey;
        }
        else{
            this.BookKey = book.getUnikey();
        }
        BookName(book.getTitle());
        bookOwner();
        BookDescription(book.getDescription());
        BookISBN(book.getISBN());
        //BookPhoto(book.getUnencodedImage());
        BookStatus();
        OwnerBook(userName,book.getTitle());
        BookUniKey();
        bookAuthor(book.getAuthor());
        //BookDatabase.child(BookKey).child("Title").setValue(book.getTitle());
        BookDescription(book.getDescription());
    }


    private void bookOwner(){
        BookDatabase.child(BookKey).child("Owner").setValue(userName);
    }

    /**
     * save all book information to Firebase
     * save the bookname
     * @param BookName backgroud bookName for
     */
    private void BookName(String BookName) {
        BookDatabase.child(BookKey).child("Title").setValue(BookName);
    }




    /**
     * set bookOwner to the database
     * @param name
     */
    private void BookOwner(String name) {
        BookDatabase.child(BookKey).child("Owner").setValue(name);
    }

    /**
     *
     * set book Borrower
     * @param name
     */
    private void BookBorrower(String name) {
        BookDatabase.child(BookKey).child("Borrower").child(name).setValue(name);
    }

    /**
     *
     *  set book Des
     * @param description
     */
    private void BookDescription(String description){
        BookDatabase.child(BookKey).child("Description").setValue(description);
    }

    /**
     *  set Book ISBN
     * @param ISBN give backgroud ISBN to database
     */
    private void BookISBN(String ISBN){
        BookDatabase.child(BookKey).child("ISBN").setValue(ISBN);
    }

    /**
     * set bookStatus to Available
     */
    private void BookStatus(){
        BookDatabase.child(BookKey).child("Status").setValue("Available");
    }

    /**
     * set backgroud image to the user
     * @param book
     */
    private void BookPhoto(Book book){

        //TODO
//        FireStorage fStore = new FireStorage();
        //fStore.addImageUri(book, );
        //BookDatabase.child(BookKey).child("").setValue(image);
    }

    /**
     * set backgroud book unikey to backgroud book
     */
    private void BookUniKey(){
        BookDatabase.child(BookKey).child("UniKey").setValue(BookKey);
    }

    private void bookAuthor(String author){
        BookDatabase.child(BookKey).child("Author").setValue(author);
    }

    /**
     *
     * set this book to user
     * @param name
     * @param BookName
     */
    private void OwnerBook(String name,String BookName){
        UserDatabase.child(name).child("Book").child(BookKey).child("Title").setValue(BookName);
    }

    // save BorrowerBook to user info
    // private  void borrowerBook(String name, String BookName){};




    /** This part is for user
     *  save user to the database
     *  addPassword TODO
     */
    public void addNewUser (User user){
        UserDatabase.child(user.getName()).child("Password").setValue(user.getPassword());
        UserDatabase.child(user.getName()).child("Address").setValue(user.getAddress());
        UserDatabase.child(user.getName()).child("Email").setValue(user.getEmail());
        UserDatabase.child(user.getName()).child("Phone").setValue(user.getPhone_number());
        changeNotificationStatus(userName,"Borrow","False");
        changeNotificationStatus(userName,"Request","False");
    }


    /**
     * the interface for User
     */
    public interface getUserInfo{
        void getNewUser(User user,List<Review> commentList);
    }


    /**
     * get the User and their commentList
     * @param callBack
     */
    public void getOwnerUser(final String part,final getUserInfo callBack){
        UserDatabase.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Review> commentList = new ArrayList<>();
                getPassword = (String) dataSnapshot.child("Password").getValue(String.class);
                getEmail = (String) dataSnapshot.child("Email").getValue(String.class);
                getAddress = (String) dataSnapshot.child("Address").getValue(String.class);
                getPhone = (String) dataSnapshot.child("Phone").getValue(String.class);
                User user = new User(userName,getPhone,getEmail,getAddress,getPassword);
                if (dataSnapshot.hasChild("Review")) {
                    for (DataSnapshot review : dataSnapshot.child("Review").child(part).getChildren()) {
                        String rating = review.getValue(String.class);
                        String comment = review.getKey();
                        Review oneReview = new Review(comment, rating);
                        commentList.add(oneReview);
                        callBack.getNewUser(user, commentList);
                    }
                } else{
                    callBack.getNewUser(user, commentList);
                }
                //callBack.getNewUser(user, commentList);
                //callBack.getNewUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }


    /**
     *
     * delete the book
     * @param book the book which user want to delete
     *
     */
    public void deleteBook(Book book){
        BookDatabase.child(book.getUnikey()).removeValue();
        UserDatabase.child(userName).child("Book").child(book.getUnikey()).removeValue();
    }



    /**
     * backgroud intereface for getting data
     */
    public interface getStatus{
        void getStatus(String value);
    }

    /**
     *
     * @param key   backgroud book key for get
     * @param status
     */
    public void changeStatus(String key, String status){
        BookDatabase.child(key).child("Status").setValue(status);
    }




    /**
     * Yifu part
     * backgroud. get borrower list
     * 2. accept_icon or decline backgroud user
     */

    public interface getBorrowerList{
        void getBorrower(String value);
    }

    /**
     * get all borrower of one book.
     * @param book
     * @param callBack
     */
    public void getBookBorrower(Book book,final getBorrowerList callBack){
        BookDatabase.child(book.getUnikey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot borrower: dataSnapshot.child("Borrower").getChildren()){
                    String borrowerName = borrower.getKey();
                    callBack.getBorrower(borrowerName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * acccept backgroud user and delete others
     * @param BorrowerName
     * @param book
     */
    public void acceptAndDeleteOther(String BorrowerName,Book book){

        BookDatabase.child(book.getUnikey()).child("Borrower").removeValue();
        BookDatabase.child(book.getUnikey()).child("Borrower").child(BorrowerName).setValue(BorrowerName);
        BookDatabase.child(book.getUnikey()).child("FinalBorrower").setValue(BorrowerName);
        BookDatabase.child(book.getUnikey()).child("Status").setValue("Accepted");

    }

    public void declineUser(String BorrowerName,final Book book){
        BookDatabase.child(book.getUnikey()).child("Borrower").child(BorrowerName).removeValue();
        BookDatabase.child(book.getUnikey()).child("Borrower").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    BookDatabase.child(book.getUnikey()).child("Status").setValue("Available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

//    public void declineUser(String BorrowerName,Book book){
//        BookDatabase.child(book.getUnikey()).child("Borrower").child(BorrowerName).removeValue();
//    }



    /**
     *   this function is for Owner
     *   check all the book which is filter by status
     * @param callBack
     * @param status
     */
    public void getBookOwner(final String status, final getNewBook callBack){
        ALlData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bookKey: dataSnapshot.child("User").child(userName).child("Book").getChildren()){
                    String key = bookKey.getKey();
                    if (dataSnapshot.child("Book").child(key).child("Status").getValue(String.class).equals(status) && dataSnapshot.child("Book").child(key).child("Borrower").hasChildren()){
                        //for(DataSnapshot bookborrower: dataSnapshot.child("Book").child(key).child("Borrower").getChildren())
                        //if (userName.equals(bookborrower.getValue(String.class))) {
                        //allBookkey.add(key);
                        Book book = new Book();
                        book.setDescription(dataSnapshot.child("Book").child(key).child("Description").getValue(String.class));
                        book.setStatus(dataSnapshot.child("Book").child(key).child("Status").getValue(String.class));
                        book.setTitle(dataSnapshot.child("Book").child(key).child("Title").getValue(String.class));
                        book.setAuthor(dataSnapshot.child("Book").child(key).child("author").getValue(String.class));
                        book.setISBN(dataSnapshot.child("Book").child(key).child("ISBN").getValue(String.class));
                        book.setOwner(dataSnapshot.child("Book").child(key).child("Owner").getValue(String.class));
                        book.setImageUrl(dataSnapshot.child("Book").child(key).child("Photo").getValue(String.class));
                        book.setUnikey(dataSnapshot.child("Book").child(key).child("UniKey").getValue(String.class));
                        callBack.getNewBook(book);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

    }


    //Yifu part finish

    //Yifu part finish







    /**
     * Chaoran Part
     * add backgroud username to the selected book's borrower list
     */
    public interface addBorrowerSucceed{
        void addNewBorrower(boolean value);
    }



    /**
     * for Chaoran part
     * add backgroud new borrower to that book borrower list
     * @param book
     */
    public void addNewBorrow(final Book book, final addBorrowerSucceed callBack) {
        BookDatabase.child(book.getUnikey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("Status").getValue(String.class);
                if (status.equals("Available")||status.equals("Requested")){
                    BookDatabase.child(book.getUnikey()).child("Borrower").child(userName).setValue(userName);
                    callBack.addNewBorrower(true);
                } else {
                    callBack.addNewBorrower(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Dset the request to true so that the book
     * backgroud new request Notification
     * @param user
     */
    public void NewRequestNotification(User user){
        UserDatabase.child(user.getName()).child("request").child("True");
    }

    /**
     * check the user Accept status
     * if it is true, user can be notified
     * @param callBack
     */
    public void checkBorrowNotification(final getStatus callBack){
        UserDatabase.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status;
                status = dataSnapshot.child("User").child(userName).child("Borrow").getValue(String.class);
                callBack.getStatus(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * set user borrow to true
     * add backgroud new borrow notification
     * @param user
     */
    public void newBorrowNotification(User user){

        UserDatabase.child(user.getName()).child("Borrow").setValue("True");

    }

    /**
     * check the user request status
     * if it is true, user can be notified
     * @param callBack
     */
    public void checkNotification(final String part,final getStatus callBack){
//        TODO implement stub
//        return true;
        UserDatabase.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status;
                status = dataSnapshot.child("Notification").child(part).getValue(String.class);
                callBack.getStatus(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * change status
     * @param part
     * @param status
     */
    public void changeNotificationStatus(String name,String part, String status){
        UserDatabase.child(name).child("Notification").child(part).setValue(status);
    }

    /**
     *   this function is for Borrower
     *   it will get all Borrower book
     *   and it can be filtered by the status
     * @param callBack
     */
    public void getBorrowerBook(final getNewBook callBack){
        BookDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot books: dataSnapshot.getChildren()){
                    if (books.hasChild("Borrower")) {
                        if (books.child("Borrower").hasChild(userName)) {
                            Book book = new Book();
                            book.setDescription(books.child("Description").getValue(String.class));
                            book.setStatus(books.child("Status").getValue(String.class));
                            book.setTitle(books.child("Title").getValue(String.class));
                            book.setISBN(books.child("ISBN").getValue(String.class));
                            book.setOwner(books.child("Owner").getValue(String.class));
                            book.setAuthor(books.child("Author").getValue(String.class));
                            book.setImageUrl(books.child("Photo").getValue(String.class));
                            book.setUnikey(books.child("UniKey").getValue(String.class));
                            callBack.getNewBook(book);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * get owner Book
     * it can be filtered by the status
     * @param callBack
     */
    public void getOwnerBook(final getNewBook callBack){
        ALlData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bookKeys: dataSnapshot.child("User").child(userName).child("Book").getChildren()){
                    String key = bookKeys.getKey();
                    Book book = new Book();
                    book.setDescription(dataSnapshot.child("Book").child(key).child("Description").getValue(String.class));
                    book.setStatus(dataSnapshot.child("Book").child(key).child("Status").getValue(String.class));
                    book.setTitle(dataSnapshot.child("Book").child(key).child("Title").getValue(String.class));
                    book.setOwner(dataSnapshot.child("Book").child(key).child("Owner").getValue(String.class));
                    book.setAuthor(dataSnapshot.child("Book").child(key).child("Author").getValue(String.class));
                    book.setImageUrl(dataSnapshot.child("Book").child(key).child("Photo").getValue(String.class));
                    book.setISBN(dataSnapshot.child("Book").child(key).child("ISBN").getValue(String.class));
                    book.setUnikey(dataSnapshot.child("Book").child(key).child("UniKey").getValue(String.class));

                    callBack.getNewBook(book);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

    }



    //chaoRan part finish


    /**
     * Swap part
     * add location, date, comment
     */


    public void swapInfo(Book book, Swap swap){
        addLocation(swap);
        addDate(book, swap);
        addComment(book, swap);
        addTime(book, swap);
        changeSwapStatus(book,"Borrower",false);
        changeSwapStatus(book,"Owner",false);
        changeSwapStatus(book,"Return",false);
    }

    /**
     * add book swap date to firebase
     * @param book
     * @param swap
     */
    private void addDate(Book book, Swap swap){
        BookDatabase.child(swap.getBook().getUnikey()).child("Swap").child("Date").setValue(swap.getDate());
    }

    /**
     * add book swap comment to firebase
     * @param book
     * @param swap
     */
    private void addComment(Book book, Swap swap){
        BookDatabase.child(swap.getBook().getUnikey()).child("Swap").child("Comment").setValue(swap.getComment());
    }

    /**
     * adds a time to a swap activity in database
     * @param book book of swap activity
     * @param swap swap object
     */
    private void addTime(Book book, Swap swap){
        BookDatabase.child(swap.getBook().getUnikey()).child("Swap").child("Time").setValue(swap.getTime());
    }

    /**
     * adds a meeting location of a swap activity into database
     * @param swap swap object
     */
    private void addLocation(Swap swap){
        BookDatabase.child(swap.getBook().getUnikey()).child("Swap").child("Location").setValue(swap.getLocation());
    }


    /**
     * get bookSwap info
     */

    interface getSwapInfo{
        void getSwapInfo(Swap swap);
    }

    /**
     *
     * @param book book to retrieve swap information for
     * @param callBack firebase callback
     */
    public void getSwap(Book book,final getSwapInfo callBack){
        BookDatabase.child(book.getUnikey()).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Swap swap = new Swap();
                swap.setComment(dataSnapshot.child("Swap").child("Comment").getValue(String.class));
                swap.setDate(dataSnapshot.child("Swap").child("Date").getValue(String.class));
                swap.setTime(dataSnapshot.child("Swap").child("Time").getValue(String.class));
                swap.setBorrower(dataSnapshot.child("FinalBorrower").getValue(String.class));
                swap.setOwner(dataSnapshot.child("Owner").getValue(String.class));
                if (dataSnapshot.child("Swap").hasChild("Location")) {
                    double latitude = dataSnapshot.child("Swap").child(("Location")).child("latitude").getValue(double.class);
                    double longitude = dataSnapshot.child("Swap").child(("Location")).child("longitude").getValue(double.class);
                    LatLng point = new LatLng(latitude,longitude);
                    swap.setLocation(point);
                }
                callBack.getSwapInfo(swap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }

        });
    }

    /**
     * change swap status after the user clicking "Swap" button
     * person can be : Owner, Borrower, Return
     * @param book
     * @param person
     * @param status
     */
    public void changeSwapStatus(Book book,String person, boolean status) {
        BookDatabase.child(book.getUnikey()).child("Swap").child(person).setValue(status);
    }

    public interface swapStatus{
        void getStatus(boolean value);
    }


    /**
     * Checks the status of a book for 2 person confirmation
     * @param book book to check status of
     * @param callBack callback object from firebase
     */
    public void checkSwapStatus(final Book book, final swapStatus callBack){
        BookDatabase.child(book.getUnikey()).child("Swap").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Borrower")) {
                    boolean Borrow = dataSnapshot.child("Borrower").getValue(boolean.class);
                    boolean Owner = dataSnapshot.child("Owner").getValue(boolean.class);
                    if (Borrow && Owner) {
                        callBack.getStatus(true);
                    } else {
                        callBack.getStatus(false);
                    }
                } else{
                    callBack.getStatus(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }


    public interface returnStatus{
        void getReturnStatus(Boolean value);
    }

    /**
     * gets the status of a returned book, since the borrower can have the book for extended periods of time
     * @param book book to check status of
     * @param callBack firebase callback
     */
    public void getReturnstatus(final Book book, final returnStatus callBack){
        BookDatabase.child(book.getUnikey()).child("Swap").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean returnStatus = dataSnapshot.child("Return").getValue(Boolean.class);
                callBack.getReturnStatus(returnStatus);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void deleteSwap(Book book){
        BookDatabase.child(book.getUnikey()).child("Swap").removeValue();
    }


    //finish swap part
    /**
     * Cao, search part
     *
     */


    /**
     * use getNewBook interface
     * pass the search string and return the related book
     * @param searchString
     * @param callBack
     */
    public void searchBook(final String searchString, final getNewBook callBack){
        BookDatabase.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot bookKey: dataSnapshot.getChildren()){
                    Book book = new Book();
                    book.setDescription(dataSnapshot.child(bookKey.getKey()).child("Description").getValue(String.class));
                    book.setStatus(dataSnapshot.child(bookKey.getKey()).child("Status").getValue(String.class));
                    book.setTitle(dataSnapshot.child(bookKey.getKey()).child("Title").getValue(String.class));
                    book.setOwner(dataSnapshot.child(bookKey.getKey()).child("Owner").getValue(String.class));
                    book.setAuthor(dataSnapshot.child(bookKey.getKey()).child("Author").getValue(String.class));
                    book.setISBN(dataSnapshot.child(bookKey.getKey()).child("ISBN").getValue(String.class));
                    book.setImageUrl(dataSnapshot.child(bookKey.getKey()).child("Photo").getValue(String.class));
                    book.setUnikey(dataSnapshot.child(bookKey.getKey()).child("UniKey").getValue(String.class));
                    book.setOwner(dataSnapshot.child(bookKey.getKey()).child("Owner").getValue(String.class));
                    if (dataSnapshot.child(bookKey.getKey()).child("Title").getValue(String.class).contains(searchString)){
                        callBack.getNewBook(book);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }


    //Cao, finish

    /**
     * Bo, user search
     */
    interface getMatchedUser{
        void getMatchedUser(User user);
    }

    public void searchUser(final String searchString, final getMatchedUser callBack){
        UserDatabase.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user: dataSnapshot.getChildren()){
                    User matchedUser = new User();
                    matchedUser.setAddress(user.child("Address").getValue(String.class));
                    matchedUser.setEmail(user.child("Email").getValue(String.class));
                    matchedUser.setName(user.getKey());
                    matchedUser.setPhone_number(user.child("Phone").getValue(String.class));
                    if (matchedUser.getImage()!=null){
                        matchedUser.setImage(user.child("Photo").getValue(Bitmap.class));
                    }
                    if (matchedUser.getName().contains(searchString)){
                        callBack.getMatchedUser(matchedUser);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }



    /**
     * Login part
     * connect email and user name
     */



    public void connectUserAndEmail(String email,String name){
        ALlData.child("UserEmail").child(email).setValue(name);
    }

    public interface getName{
        void getName(String value);
    }

    public void getNameByEmail(final String email,final getName callBack){
        ALlData.child("UserEmail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name;
                name = dataSnapshot.child(email).getValue(String.class);
                callBack.getName(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
    // Login finished


    // user Comment
    public void addOwnerReview(String name, Review review){
        UserDatabase.child(name).child("Review").child("Owner").child(review.getComment()).setValue(review.getRating());
    }

    public void addBorrowerReview(String name, Review review){
        UserDatabase.child(name).child("Review").child("Borrower").child(review.getComment()).setValue(review.getRating());
    }
}