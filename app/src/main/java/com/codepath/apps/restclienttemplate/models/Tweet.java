package com.codepath.apps.restclienttemplate.models;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "tweets")
@Parcel
public class Tweet {

    private String body;
    private String createdAt;
    @Embedded
    private User user;
    @PrimaryKey
    @NonNull
    private String id = "0";
    private int retweetCount;
    private int favoriteCount;
    private List<String> imageUrls;

    public Tweet() {
        //empty constructor required for Parcel
    }

    public Tweet(String body, String createdAt, User user, @NotNull String id, List<String> imageUrls, int retweetCount, int favoriteCount) {
        this.body = body;
        this.createdAt = createdAt;
        this.user = user;
        this.id = id;
        this.imageUrls = imageUrls;
        this.retweetCount = retweetCount;
        this.favoriteCount = favoriteCount;
    }

    public static Tweet fromJson(JSONObject json) throws JSONException {
        List<String> imageUrls = new ArrayList<>();
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
                json.getString("id_str"),
                imageUrls,
                json.getInt("retweet_count"),
                json.getInt("favorite_count")
        );
    }

    public static List<Tweet> fromJsonArray(JSONArray array) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
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

    @NotNull
    public String getId() {
        return id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
