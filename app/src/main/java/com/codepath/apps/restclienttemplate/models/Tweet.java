package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {

    private String body;
    private String createdAt;
    private User user;
    private int id;

    public Tweet(String body, String createdAt, User user, int id) {
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
        this.id = id;
    }

    public static Tweet fromJson(JSONObject json) throws JSONException {
        return new Tweet(
                json.getString("text"),
                json.getString("created_at"),
                User.fromJson(json.getJSONObject("user")),
                json.getInt("id")
        );
    }

    public static List<Tweet> fromJsonArray(JSONArray array) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < array.length(); i++){
            tweets.add(Tweet.fromJson(array.getJSONObject(i)));
        }
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }
}
