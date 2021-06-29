package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private int id;
    private String name;
    private String screenName;
    private String profileImageUrl;

    public User(String name, String handle, String profileImageUrl, int id) {
        this.name = name;
        this.screenName = handle;
        this.profileImageUrl = profileImageUrl;
    }

    public static User fromJson(JSONObject json) throws JSONException {
        return new User(
                json.getString("name"),
                json.getString("screen_name"),
                json.getString("profile_image_url_https"),
                json.getInt("id")
        );
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
