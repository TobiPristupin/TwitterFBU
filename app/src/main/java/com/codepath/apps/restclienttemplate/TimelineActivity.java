package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.restclienttemplate.databinding.ActivityTimelineBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    private ActivityTimelineBinding binding;
    private TwitterClient client;
    private TweetAdapter adapter;
    private List<Tweet> tweets;
    private static final int REQUEST_CODE = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        setSupportActionBar(binding.timelineToolbar);

        client = TwitterApplication.getRestClient(this);
        tweets = new ArrayList<>();

        initViews();
        populateHomeTimeline(0);
    }

    private void populateHomeTimeline(int page) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JsonHttpResponseHandler.JSON json) {
                tweets.clear();
                try {
                    tweets.addAll(Tweet.fromJsonArray(json.jsonArray));
                    binding.timelineProgressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    binding.swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                binding.timelineProgressBar.setVisibility(View.GONE);
                System.out.println(throwable.toString());
            }
        });

//        try {
//            JSONArray array = new JSONArray("[{\"created_at\":\"Tue Jun 29 16:30:00 +0000 2021\",\"id\":1409912042530775045,\"id_str\":\"1409912042530775045\",\"text\":\"Results of the DNA analysis have implications beyond the history of domestication and species naming. Modern interb… https:\\/\\/t.co\\/XqBWu2LDLk\",\"truncated\":true,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/XqBWu2LDLk\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/1409912042530775045\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/1…\",\"indices\":[117,140]}]},\"source\":\"<a href=\\\"http:\\/\\/www.socialnewsdesk.com\\\" rel=\\\"nofollow\\\">SocialNewsDesk<\\/a>\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":19402238,\"id_str\":\"19402238\",\"name\":\"Science News\",\"screen_name\":\"ScienceNews\",\"location\":\"Washington, DC\",\"description\":\"Covering the latest news in all fields of science. Tweets by @ThatMikeDenison and @wwrfd. Publisher @society4science. See also @SNStudents.\",\"url\":\"https:\\/\\/t.co\\/crx5P5xmsH\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"https:\\/\\/t.co\\/crx5P5xmsH\",\"expanded_url\":\"https:\\/\\/www.sciencenews.org\",\"display_url\":\"sciencenews.org\",\"indices\":[0,23]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":3896190,\"friends_count\":601,\"listed_count\":19629,\"created_at\":\"Fri Jan 23 16:56:32 +0000 2009\",\"favourites_count\":600,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":true,\"statuses_count\":59274,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"00476F\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/1210648820541018113\\/j4qqEa6F_normal.png\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/1210648820541018113\\/j4qqEa6F_normal.png\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/19402238\\/1403559093\",\"profile_link_color\":\"1F4363\",\"profile_sidebar_border_color\":\"CCCCCC\",\"profile_sidebar_fill_color\":\"E6E6E6\",\"profile_text_color\":\"00476F\",\"profile_use_background_image\":false,\"has_extended_profile\":false,\"default_profile\":false,\"default_profile_image\":false,\"following\":true,\"follow_request_sent\":false,\"notifications\":false,\"translator_type\":\"none\",\"withheld_in_countries\":[]},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":6,\"favorite_count\":20,\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"possibly_sensitive_appealable\":false,\"lang\":\"en\"},{\"created_at\":\"Tue Jun 29 16:04:21 +0000 2021\",\"id\":1409905588373438471,\"id_str\":\"1409905588373438471\",\"text\":\"RT @EURO2020: \uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F\uD83C\uDDE9\uD83C\uDDEA\uD83C\uDDF8\uD83C\uDDEA\uD83C\uDDFA\uD83C\uDDE6\\n\\nDrop a GIF \uD83D\uDCF1\uD83D\uDC47to reveal your team...\\n\\n#EURO2020\",\"truncated\":false,\"entities\":{\"hashtags\":[{\"text\":\"EURO2020\",\"indices\":[66,75]}],\"symbols\":[],\"user_mentions\":[{\"screen_name\":\"EURO2020\",\"name\":\"UEFA EURO 2020\",\"id\":1469402426,\"id_str\":\"1469402426\",\"indices\":[3,12]}],\"urls\":[]},\"source\":\"<a href=\\\"https:\\/\\/mobile.twitter.com\\\" rel=\\\"nofollow\\\">Twitter Web App<\\/a>\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":627673190,\"id_str\":\"627673190\",\"name\":\"UEFA Champions League\",\"screen_name\":\"ChampionsLeague\",\"location\":\"Nyon, Vaud\",\"description\":\"\uD83C\uDDEA\uD83C\uDDF8 @LigadeCampeones \\n\uD83C\uDDEF\uD83C\uDDF5 @UCLJapan \\nUnder-19 @UEFAYouthLeague\",\"url\":\"https:\\/\\/t.co\\/UayeXSxnTm\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"https:\\/\\/t.co\\/UayeXSxnTm\",\"expanded_url\":\"https:\\/\\/linktr.ee\\/UEFAChampionsLeague\",\"display_url\":\"linktr.ee\\/UEFAChampionsL…\",\"indices\":[0,23]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":35213367,\"friends_count\":583,\"listed_count\":23227,\"created_at\":\"Thu Jul 05 19:43:40 +0000 2012\",\"favourites_count\":3287,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":true,\"statuses_count\":75398,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":true,\"profile_background_color\":\"022330\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/1073607078109949957\\/74oimLX4_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/1073607078109949957\\/74oimLX4_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/627673190\\/1609932130\",\"profile_link_color\":\"1B95E0\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":false,\"default_profile_image\":false,\"following\":true,\"follow_request_sent\":false,\"notifications\":false,\"translator_type\":\"none\",\"withheld_in_countries\":[]},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweeted_status\":{\"created_at\":\"Tue Jun 29 14:11:09 +0000 2021\",\"id\":1409877098483879938,\"id_str\":\"1409877098483879938\",\"text\":\"\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F\uD83C\uDDE9\uD83C\uDDEA\uD83C\uDDF8\uD83C\uDDEA\uD83C\uDDFA\uD83C\uDDE6\\n\\nDrop a GIF \uD83D\uDCF1\uD83D\uDC47to reveal your team...\\n\\n#EURO2020\",\"truncated\":false,\"entities\":{\"hashtags\":[{\"text\":\"EURO2020\",\"indices\":[52,61]}],\"symbols\":[],\"user_mentions\":[],\"urls\":[]},\"source\":\"<a href=\\\"https:\\/\\/mobile.twitter.com\\\" rel=\\\"nofollow\\\">Twitter Web App<\\/a>\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":1469402426,\"id_str\":\"1469402426\",\"name\":\"UEFA EURO 2020\",\"screen_name\":\"EURO2020\",\"location\":\"\",\"description\":\"The official home of UEFA men's national team football on Twitter ⚽️ #EURO2020 #NationsLeague #WCQ\",\"url\":\"https:\\/\\/t.co\\/gD4AUen1Hd\",\"entities\":{\"url\":{\"urls\":[{\"url\":\"https:\\/\\/t.co\\/gD4AUen1Hd\",\"expanded_url\":\"http:\\/\\/euro2020.com\",\"display_url\":\"euro2020.com\",\"indices\":[0,23]}]},\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":2533314,\"friends_count\":515,\"listed_count\":5297,\"created_at\":\"Thu May 30 10:08:05 +0000 2013\",\"favourites_count\":1138,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"verified\":true,\"statuses_count\":30351,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/1403284352688136192\\/z31XwPuo_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/1403284352688136192\\/z31XwPuo_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/1469402426\\/1623055175\",\"profile_link_color\":\"1B95E0\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":false,\"default_profile_image\":false,\"following\":false,\"follow_request_sent\":false,\"notifications\":false,\"translator_type\":\"none\",\"withheld_in_countries\":[]},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":159,\"favorite_count\":2075,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},\"is_quote_status\":false,\"retweet_count\":159,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"}]");
//            tweets.addAll(Tweet.fromJsonArray(array));
//            binding.timelineProgressBar.setVisibility(View.GONE);
//            adapter.notifyDataSetChanged();
//            binding.swipeRefreshLayout.setRefreshing(false);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


    }

    private void initViews() {
        initRecyclerView();
        initComposeFab();
        initSwipeRefresh();
    }

    private void initRecyclerView() {
        binding.timelineProgressBar.setVisibility(View.VISIBLE);

        adapter = new TweetAdapter(tweets, this);
        binding.timelineRecyclerview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.timelineRecyclerview.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(binding.timelineRecyclerview.getContext(),
                layoutManager.getOrientation());
        binding.timelineRecyclerview.addItemDecoration(divider);
    }

    private void initSwipeRefresh(){
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeline(0);
            }
        });
    }

    private void initComposeFab(){
        binding.composeTweetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimelineActivity.this, ComposeTweetActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    private void logout(){
        client.clearAccessToken();
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Tweet newTweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
            tweets.add(0, newTweet);
            adapter.notifyItemInserted(0);
            binding.timelineRecyclerview.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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