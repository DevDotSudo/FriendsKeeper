package com.example.magalona_fk;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FriendDao {
    @Insert
    void insert(Friend friend);

    @Update
    void update(Friend friend);

    @Delete
    void delete(Friend friend);

    @Query("SELECT * FROM friends WHERE userId = :userId")
    List<Friend> getFriendsByUser(int userId);

    @Query("SELECT * FROM friends WHERE id = :friendId")
    Friend getFriendById(int friendId);
}
