package com.codepath.apps.restclienttemplate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.databinding.ItemAccountBinding;
import com.codepath.apps.restclienttemplate.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private List<User> users;
    private Context context;

    public ProfileAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account, parent, false);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemAccountBinding binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = ItemAccountBinding.bind(itemView);
        }

        private void bind(User user) {
            Glide.with(context).load(user.getProfileImageUrl()).into(binding.accountProfileImage);
            binding.accountDescription.setText(user.getDescription());
            binding.accountUserAt.setText(String.format("@%s", user.getScreenName()));
            binding.accountUserHandle.setText(user.getName());
        }
    }
}


