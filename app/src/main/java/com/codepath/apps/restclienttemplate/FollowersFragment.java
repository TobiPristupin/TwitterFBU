package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.apps.restclienttemplate.databinding.FragmentFollowersBinding;
import com.codepath.apps.restclienttemplate.models.TwitterApplication;
import com.codepath.apps.restclienttemplate.models.TwitterClient;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class FollowersFragment extends Fragment {

    public enum State {
        SHOW_FOLLOWING, SHOW_FOLLOWERS
    }

    private State state = null;
    private User user;
    private FragmentFollowersBinding binding;
    private List<User> users;
    private ProfileAdapter adapter;
    private TwitterClient client;
    private static final String TAG = "FollowersFragment";

    public FollowersFragment() {
        // Required empty public constructor
    }


    public static FollowersFragment newInstance(User user, State state) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putSerializable("state", state);
        args.putParcelable("profile", Parcels.wrap(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            state = (State) bundle.getSerializable("state");
            user = (User) Parcels.unwrap(bundle.getParcelable("profile"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFollowersBinding.inflate(getLayoutInflater());

        users = new ArrayList<>();
        client = TwitterApplication.getRestClient(getContext());

        initRecyclerView();
        fetchAccountsFromApi();

        return binding.getRoot();
    }

    private void initRecyclerView() {
        adapter = new ProfileAdapter(users, getContext());
        binding.accountsRecyclerview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.accountsRecyclerview.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(binding.accountsRecyclerview.getContext(),
                layoutManager.getOrientation());
        binding.accountsRecyclerview.addItemDecoration(divider);
    }

    private void fetchAccountsFromApi() {
        if (state == State.SHOW_FOLLOWERS) {
            fetchFollowers();
        } else if (state == State.SHOW_FOLLOWING) {
            fetchFollowing();
        }
    }

    private void fetchFollowers() {
        client.getFollowers(user, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Fetched followers");
                try {
                    users.clear();
                    users.addAll(User.fromJsonArray(json.jsonObject.getJSONArray("users")));
                    adapter.notifyDataSetChanged();
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

    private void fetchFollowing() {
        client.getFollowing(user, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Fetched following");
                try {
                    users.clear();
                    users.addAll(User.fromJsonArray(json.jsonObject.getJSONArray("users")));
                    adapter.notifyDataSetChanged();
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

}