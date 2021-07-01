package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetDao;
import com.codepath.apps.restclienttemplate.models.TweetDatabaseProvider;
import com.codepath.apps.restclienttemplate.models.TwitterApplication;
import com.codepath.apps.restclienttemplate.models.TwitterClient;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.DateUtils;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class DetailTweetActivity extends AppCompatActivity {

    private ActivityDetailTweetBinding binding;
    private TwitterClient client;
    private Tweet tweet;
    private TweetDao tweetDao;
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
        tweetDao = TweetDatabaseProvider.getInstance(this).tweetDao();

        initViews();
    }

    private void initViews() {
        User user = tweet.getUser();
        Glide.with(this).load(user.getProfileImageUrl()).into(binding.detailProfileImage);
        binding.detailUserHandle.setText(user.getName());
        binding.detailUserAt.setText(user.getScreenName());
        binding.detailCreatedAt.setText(DateUtils.getFormattedDate(tweet.getCreatedAt()));
        binding.detailText.setText(tweet.getBody());
        if (!tweet.getImageUrls().isEmpty()) {
            binding.detailPreviewImage.setVisibility(View.VISIBLE);
            int radius = 80;
            int margin = 30;
            Glide.with(this)
                    .load(tweet.getImageUrls().get(0))
                    .centerCrop()
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(binding.detailPreviewImage);
        }

        binding.detailRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        binding.detailFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));
        binding.favoriteButton.setOnClickListener(view -> likeTweet());
        binding.retweetButton.setOnClickListener(view -> retweet());
    }

    private void likeTweet() {
        client.likeTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                tweet.setFavoriteCount(tweet.getFavoriteCount() + 1);
                binding.detailFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));
                Toast.makeText(DetailTweetActivity.this, "Liked tweet", Toast.LENGTH_SHORT).show();
                tweetDao.update(tweet);
                Log.i(TAG, "Successfully liked");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                /* If liking a tweet fails, it may be bc the user liked it before,. In that case, we should
                 * unlike the tweet. This is a little bit hacky but it works for the scope of the project.
                 * I decided on this approach because keeping track of if the user already liked a tweet, and
                 * syncing that with the cache would be a lot of work.
                 * */
                int alreadyLikedErrorCode = 139;
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.has("errors") && json.getJSONArray("errors")
                            .getJSONObject(0).getInt("code") == alreadyLikedErrorCode) {
                        unlikeTweet();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.w(TAG, response);
            }
        });
    }

    private void unlikeTweet() {
        client.unlikeTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                tweet.setFavoriteCount(tweet.getFavoriteCount() - 1);
                binding.detailFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));
                Toast.makeText(DetailTweetActivity.this, "Unliked tweet", Toast.LENGTH_SHORT).show();
                tweetDao.update(tweet);
                Log.i(TAG, "Successfully unliked");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.w(TAG, response);
            }
        });
    }

    private void retweet() {
        client.retweetTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                tweet.setRetweetCount(tweet.getRetweetCount() + 1);
                binding.detailRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
                Toast.makeText(DetailTweetActivity.this, "Retweeted", Toast.LENGTH_SHORT).show();
                tweetDao.update(tweet);
                Log.i(TAG, "Successfully retweeted");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                /* If retweeting a tweet fails, it may be bc the user retweeted it before,. In that case, we should
                 * un-retweet the tweet. This is a little bit hacky but it works for the scope of the project.
                 * I decided on this approach because keeping track of if the user already retweeted a tweet, and
                 * syncing that with the cache would be a lot of work.
                 * */
                int alreadyRetweetedErrorCode = 327;
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.has("errors") && json.getJSONArray("errors")
                            .getJSONObject(0).getInt("code") == alreadyRetweetedErrorCode) {
                        unRetweet();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.w(TAG, response);
            }
        });
    }

    private void unRetweet() {
        client.unRetweetTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                tweet.setRetweetCount(tweet.getRetweetCount() - 1);
                binding.detailRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
                Toast.makeText(DetailTweetActivity.this, "Unretweeted", Toast.LENGTH_SHORT).show();
                tweetDao.update(tweet);
                Log.i(TAG, "Successfully unretweeted");
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.w(TAG, response);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}