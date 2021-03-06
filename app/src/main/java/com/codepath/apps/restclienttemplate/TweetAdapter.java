package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemTweetBinding;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.apps.restclienttemplate.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    interface onTweetClickListener {
        void onTweetClick(int position);

        void onProfileImageClick(int position);
    }

    private List<Tweet> tweets;
    private Context context;
    private onTweetClickListener clickListener;

    public TweetAdapter(List<Tweet> tweets, Context context, onTweetClickListener clickListener) {
        this.tweets = tweets;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tweet, parent, false);
        return new TweetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(tweets.get(position));
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemTweetBinding binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemTweetBinding.bind(itemView);
        }

        private void bind(Tweet tweet) {
            User user = tweet.getUser();
            Glide.with(context).load(user.getProfileImageUrl()).into(binding.profileImage);
            binding.userHandle.setText(user.getName());
            binding.userAt.setText(String.format("@%s", user.getScreenName()));
            binding.timeCreated.setText(DateUtils.getRelativeTimeAgo(tweet.getCreatedAt()));
            binding.tweetText.setText(tweet.getBody());

            if (!tweet.getImageUrls().isEmpty()) {
                binding.previewImage.setVisibility(View.VISIBLE);

                int radius = 80;
                int margin = 30;
                Glide.with(context)
                        .load(tweet.getImageUrls().get(0))
                        .centerCrop()
                        .transform(new RoundedCornersTransformation(radius, margin))
                        .into(binding.previewImage);
            } else {
                binding.previewImage.setVisibility(View.GONE);
            }

            binding.tweetItemRoot.setOnClickListener(view -> clickListener.onTweetClick(getAdapterPosition()));
            binding.profileImage.setOnClickListener(view -> clickListener.onProfileImageClick(getAdapterPosition()));

        }
    }
}


