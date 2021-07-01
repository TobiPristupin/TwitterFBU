package com.codepath.apps.restclienttemplate.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Tweet.class}, version = 5)
@TypeConverters({Converters.class})
public abstract class TweetDatabase extends RoomDatabase {
    public abstract TweetDao tweetDao();
}
