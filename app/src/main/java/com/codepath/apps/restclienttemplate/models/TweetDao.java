package com.codepath.apps.restclienttemplate.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TweetDao {

    @Query("SELECT * FROM tweets")
    List<Tweet> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Tweet> tweets);

    @Update
    void update(Tweet tweet);

    @Query("DELETE FROM tweets")
    public void deleteAll();

    @Query("SELECT COUNT(id) FROM tweets")
    int getNumberOfRows();
}
