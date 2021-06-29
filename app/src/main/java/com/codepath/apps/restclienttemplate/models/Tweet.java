package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public int id;
    public List<String> imageUrls;

    public Tweet(){

    }

    public Tweet(String body, String createdAt, User user, int id, List<String> imageUrls) {
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
        this.id = id;
        this.imageUrls = imageUrls;
    }

    public static Tweet fromJson(JSONObject json) throws JSONException {
        List<String> imageUrls = new ArrayList<>();
        JSONObject entities = json.optJSONObject("extended_entities");

        if (json.has("extended_entities") && json.getJSONObject("extended_entities").has("media")) {
            JSONArray mediaArray = json.getJSONObject("extended_entities").getJSONArray("media");
            for (int i = 0; i < mediaArray.length(); i++) {
                imageUrls.add(mediaArray.getJSONObject(i).getString("media_url_https"));
            }
        }

        return new Tweet(
                json.getString("text"),
                json.getString("created_at"),
                User.fromJson(json.getJSONObject("user")),
                json.getInt("id"),
                imageUrls
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
