package com.codepath.apps.restclienttemplate.models;

import androidx.room.ColumnInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class User {

    @ColumnInfo(name = "user_id")
    private String id;
    private String name;
    private String screenName;
    private String profileImageUrl;
    private String description;

    public User() {
        //empty constructor required for Parcel
    }

    public User(String name, String handle, String profileImageUrl, String id, String description) {
        this.name = name;
        this.screenName = handle;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
        this.id = id;
    }

    public static User fromJson(JSONObject json) throws JSONException {
        return new User(
                json.getString("name"),
                json.getString("screen_name"),
                json.getString("profile_image_url_https"),
                json.getString("id_str"),
                json.getString("description")
        );
    }

    public static List<User> fromJsonArray(JSONArray array) throws JSONException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            users.add(User.fromJson(array.getJSONObject(i)));
        }

        return users;
    }


    public String getId() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
