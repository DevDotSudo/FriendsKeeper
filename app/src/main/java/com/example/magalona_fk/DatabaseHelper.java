package com.example.magalona_fk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FriendsKeeper.db";
    private static final int DATABASE_VERSION = 1;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "username";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Friends table
    private static final String TABLE_FRIENDS = "friends";
    private static final String COLUMN_FRIEND_ID = "id";
    private static final String COLUMN_FRIEND_NAME = "name";
    private static final String COLUMN_FRIEND_EMAIL = "email";
    private static final String COLUMN_FRIEND_PHONE = "phone";
    private static final String COLUMN_FRIEND_BIRTHDAY = "birthday";
    private static final String COLUMN_FRIEND_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS + "("
                + COLUMN_FRIEND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FRIEND_NAME + " TEXT,"
                + COLUMN_FRIEND_EMAIL + " TEXT,"
                + COLUMN_FRIEND_PHONE + " TEXT,"
                + COLUMN_FRIEND_BIRTHDAY + " TEXT,"
                + COLUMN_FRIEND_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_FRIENDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        onCreate(db);
    }

    // User operations
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, username);
        values.put(COLUMN_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    // Friend operations
    public boolean addFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FRIEND_NAME, friend.getName());
        values.put(COLUMN_FRIEND_EMAIL, friend.getEmail());
        values.put(COLUMN_FRIEND_PHONE, friend.getPhone());
        values.put(COLUMN_FRIEND_BIRTHDAY, friend.getBirthday());
        values.put(COLUMN_FRIEND_STATUS, friend.getStatus());
        long result = db.insert(TABLE_FRIENDS, null, values);
        return result != -1;
    }

    public List<Friend> getAllFriends() {
        List<Friend> friendList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FRIENDS, null);

        if (cursor.moveToFirst()) {
            do {
                Friend friend = new Friend();
                friend.setId(cursor.getInt(0));
                friend.setName(cursor.getString(1));
                friend.setEmail(cursor.getString(2));
                friend.setPhone(cursor.getString(3));
                friend.setBirthday(cursor.getString(4));
                friend.setStatus(cursor.getString(5));
                friendList.add(friend);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return friendList;
    }

    public void deleteFriend(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIENDS, COLUMN_FRIEND_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
