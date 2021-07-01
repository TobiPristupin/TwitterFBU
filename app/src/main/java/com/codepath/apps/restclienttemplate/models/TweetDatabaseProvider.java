package com.codepath.apps.restclienttemplate.models;

import android.content.Context;

import androidx.room.Room;

public class TweetDatabaseProvider {

    private static TweetDatabase db = null;

    private TweetDatabaseProvider(){}

    private static void initializeDb(Context context) {
        TweetDatabaseProvider.db = Room.databaseBuilder(context.getApplicationContext(), TweetDatabase.class, "db-name")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }

    public static TweetDatabase getInstance(Context context){
        if (db == null){
            initializeDb(context);
        }

        return db;
    }
}
