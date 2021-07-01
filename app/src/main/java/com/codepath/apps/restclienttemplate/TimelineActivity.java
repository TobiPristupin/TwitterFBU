package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.TweetDao;
import com.codepath.apps.restclienttemplate.models.TweetDatabaseProvider;
import com.codepath.apps.restclienttemplate.models.TwitterApplication;
import com.codepath.apps.restclienttemplate.models.TwitterClient;
import com.codepath.apps.restclienttemplate.utils.EndlessRecyclerViewScrollListener;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/*
 * TimelineActivity aims to minimize API calls. API calls are only done when the app is first started,
 * when the user refreshes the timeline by pulling down, or when the user scrolls down enough and new
 * tweets have to be fetched. After every API call, tweets are stored in the Room database. The UI
 * is then populated from the database.\
 *   */
public class TimelineActivity extends AppCompatActivity {

    private ActivityTimelineBinding binding;
    private TwitterClient client;
    private TweetAdapter adapter;
    private List<Tweet> tweets;
    private TweetDao tweetDao;
    private static final int REQUEST_CODE = 999; //used when starting an activity for result
    private BigInteger minTweetId = null; //Smallest id of all the tweets currently loaded
    private static final String TAG = "TimelineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        setSupportActionBar(binding.timelineToolbar);

        client = TwitterApplication.getRestClient(this);
        tweets = new ArrayList<>();
        tweetDao = TweetDatabaseProvider.getInstance(this).tweetDao();

        initViews();
        fetchFromApiToCacheAndPopulateTimeline(null);
    }

    //onResume is called when returning from another activity such as DetailTweetActivity
    @Override
    protected void onResume() {
        super.onResume();
        /*
        Since another activity such as DetailTweetActivity might have modified the cached tweets,
        we update the timeline with the new results from the cache
        */
        populateHomeTimelineFromCache();
    }

    /* Fetches tweets from API, displays them on the timeline, and caches the results in room db
     * TODO: break down into multiple methods, this is too long and has too many responsibilities.
     *
     * @param minTweetId: Return tweets with an id less than (that is, older than) this id. If null,
     * return newest tweets.
     *  */
    private void fetchFromApiToCacheAndPopulateTimeline(String minTweetId) {
        client.getHomeTimeline(minTweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                try {
                    Log.i(TAG, "Fetched from api");
                    tweetDao.insertAll(Tweet.fromJsonArray(json.jsonArray));
                    populateHomeTimelineFromCache();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.w(TAG, throwable);
            }
        });
    }

    private void populateHomeTimelineFromCache() {
        binding.timelineProgressBar.setVisibility(View.GONE);
        binding.swipeRefreshLayout.setRefreshing(false);
        tweets.clear();
        tweets.addAll(tweetDao.getAll());
        adapter.notifyDataSetChanged();
        updateMinTweetId();
    }

    private void updateMinTweetId() {
        BigInteger smallest = new BigInteger("0");
        for (Tweet t : tweets) {
            smallest = smallest.max(new BigInteger(t.getId()));
        }
        minTweetId = smallest;
    }

    private void initViews() {
        initRecyclerView();
        initComposeFab();
        initSwipeRefresh();
    }

    private void initRecyclerView() {
        binding.timelineProgressBar.setVisibility(View.VISIBLE);

        adapter = new TweetAdapter(tweets, this, new TweetAdapter.onTweetClickListener() {
            @Override
            public void onTweetClick(int position) {
                TimelineActivity.this.onTweetClick(position);
            }

            @Override
            public void onProfileImageClick(int position) {
                TimelineActivity.this.onProfileImageClick(position);
            }
        });
        binding.timelineRecyclerview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.timelineRecyclerview.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(binding.timelineRecyclerview.getContext(),
                layoutManager.getOrientation());
        binding.timelineRecyclerview.addItemDecoration(divider);

        binding.timelineRecyclerview.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                System.out.println("fetching more!!");
                fetchFromApiToCacheAndPopulateTimeline(minTweetId.subtract(new BigInteger("1")).toString());
            }
        });
    }

    private void onTweetClick(int position) {
        Tweet tweet = tweets.get(position);
        Intent i = new Intent(TimelineActivity.this, DetailTweetActivity.class);
        i.putExtra("tweet", Parcels.wrap(tweet));
        startActivity(i);
    }

    private void onProfileImageClick(int position) {
        Intent i = new Intent(TimelineActivity.this, ProfileDetailActivity.class);
        i.putExtra("profile", Parcels.wrap(tweets.get(position).getUser()));
        startActivity(i);
    }

    private void initSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchFromApiToCacheAndPopulateTimeline(null);
        });
    }

    private void initComposeFab() {
        binding.composeTweetFab.setOnClickListener(view -> {
            Intent i = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
            startActivityForResult(i, REQUEST_CODE);
        });
    }

    private void logout() {
        client.clearAccessToken();
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this makes sure the Back button won't work
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // same as above
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Tweet newTweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0, newTweet);
            adapter.notifyItemInserted(0);
            binding.timelineRecyclerview.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline_menu, menu);
        return true;
    }
}