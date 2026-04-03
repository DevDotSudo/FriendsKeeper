package com.example.magalona_fk;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Friend.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract FriendDao friendDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "friends_keeper_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Note: For production, use background threads
                    .build();
        }
        return instance;
    }
}
