package com.example.bowenhu.bookswap;

import android.database.DatabaseUtils;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    //test adding book and getting book
    @Test
    public void BookAddAndGet_isCorrect(){
        DataBaseUtil u = new DataBaseUtil();
        Book newBook1 = new Book("1","1","1","1","1");
        Book newBook2 = new Book("2","1","2","1","1");
        Book newBook3 = new Book("3","1","3","1","1");
        Book newBook4 = new Book("4","1","4","1","1");
        u.AddNewBook(newBook1);
        u.AddNewBook(newBook2);
        u.AddNewBook(newBook3);
        u.AddNewBook(newBook4);
        ArrayList testArray1 = u.getBooks(1);
        ArrayList testArray2 = u.getBooks(2);
        ArrayList testArray3 = u.getBooks(3);
        ArrayList testArray4 = u.getBooks(4);
        Book testBook1 = (Book) testArray1.get(0);
        Book testBook2 = (Book) testArray1.get(1);
        Book testBook3 = (Book) testArray1.get(2);
        Book testBook4 = (Book) testArray1.get(3);
        assertEquals("1",testBook1.getStatus());
        assertEquals("2",testBook2.getStatus());
        assertEquals("3",testBook3.getStatus());
        assertEquals("4",testBook4.getStatus());
    }

    //test adding User and getting User
    @Test
    public void AddAndGetUser_isCorrect(){
        DataBaseUtil u = new DataBaseUtil();
        User user = new User("1","1","1","1");
        u.addNewUser(user);
        User testUser = u.getOneUser("Bowen");
        assertEquals("1",testUser.getEmail());
    }

    //test changing status
    @Test
    public void chageStatus_isCorrect(){
        DataBaseUtil u = new DataBaseUtil();
        u.changeStatus(1);
        ArrayList testArray;
        testArray = u.getBooks(1);
        Book testbook = (Book) testArray.get(0);
        assertEquals("1",testbook.getStatus());
    }

    //test request adding
    @Test
    public void Request_isCorrect(){
        DataBaseUtil u = new DataBaseUtil();
        u.NewRequest();
        boolean testRequest = u.checkNewRequest("Bowen");
        assertEquals(true,testRequest);
    }
    
    
    //test borrow adding
    @Test
    public void Borrow_isCorrect(){
        DataBaseUtil u = new DataBaseUtil();
        u.NewBorrow();
        boolean testRequest = u.checkNewBorrow("Bowen");
        assertEquals(true,testRequest);
    }


}
