package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.DateUtils;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class DetailTweetActivity extends AppCompatActivity {

    ActivityDetailTweetBinding binding;
    TwitterClient client;
    Tweet tweet;
    private static final String TAG = "DetailTweetActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailTweetBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        setSupportActionBar(binding.detailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        client = TwitterApplication.getRestClient(this);

        initViews();
    }

    private void initViews(){
        User user = tweet.getUser();
        Glide.with(this).load(user.getProfileImageUrl()).into(binding.detailProfileImage);
        binding.detailUserHandle.setText(user.getName());
        binding.detailUserAt.setText(user.getScreenName());
        binding.detailCreatedAt.setText(DateUtils.getFormattedDate(tweet.getCreatedAt()));
        binding.detailText.setText(tweet.getBody());
        if (!tweet.imageUrls.isEmpty()){
            binding.detailPreviewImage.setVisibility(View.VISIBLE);

            int radius = 80;
            int margin = 30;
            Glide.with(this)
                    .load(tweet.imageUrls.get(0))
                    .centerCrop()
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(binding.detailPreviewImage);
        }

        binding.detailRetweetCount.setText(String.valueOf(tweet.retweetCount));
        binding.detailFavoriteCount.setText(String.valueOf(tweet.favoriteCount));

        binding.favoriteButton.setOnClickListener(view -> likeTweet());
    }

    private void likeTweet(){
        client.likeTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Successfully liked");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.w(TAG, response);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}