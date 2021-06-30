package com.codepath.apps.restclienttemplate.models;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public List<String> gettingListFromString(String str) {
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    @TypeConverter
    public String writingStringFromList(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}