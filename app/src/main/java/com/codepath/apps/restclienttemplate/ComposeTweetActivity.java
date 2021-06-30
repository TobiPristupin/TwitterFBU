package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.databinding.ActivityComposeTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TwitterApplication;
import com.codepath.apps.restclienttemplate.models.TwitterClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeTweetActivity extends AppCompatActivity {

    ActivityComposeTweetBinding binding;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComposeTweetBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        client = TwitterApplication.getRestClient(this);

        initToolbar();
        initEditText();
    }

    private void initToolbar(){
        setSupportActionBar(binding.composeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initEditText(){
        binding.composeTweetEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String length = editable.length() + "/280 characters";
                binding.characterCount.setText(length);
//                if (editable.length() > 280){
//                    binding.characterCount.setTextColor(Color.parseColor("#C70000"));
//                }
            }
        });
    }

    private void saveTweet(){
        String tweet = binding.composeTweetEdittext.getText().toString();
        if (tweet.length() == 0){
            Toast.makeText(this, "Cannot post empty tweet", Toast.LENGTH_SHORT).show();
            return;
        } else if (tweet.length() > 280){
            Toast.makeText(this, "Tweet exceeds 280 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        client.publishTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                System.out.println(json);
                try {
                    Tweet published = Tweet.fromJson(json.jsonObject);
                    Intent intent = new Intent();
                    intent.putExtra("tweet", Parcels.wrap(published));
                    setResult(RESULT_OK, intent);
                    ComposeTweetActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                System.out.println(throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                saveTweet();
                return true;
        }

        this.finish();
        return true;
    }
}