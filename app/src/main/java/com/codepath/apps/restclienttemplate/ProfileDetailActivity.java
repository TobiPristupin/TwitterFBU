package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.databinding.ActivityProfileDetailBinding;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class ProfileDetailActivity extends AppCompatActivity {

    ActivityProfileDetailBinding binding;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("profile"));

        setSupportActionBar(binding.profileDetailToolbar);
        getSupportActionBar().setTitle(user.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.pager.setAdapter(new ProfileFragmentPagerAdapter(getSupportFragmentManager(), user));
        binding.tabLayout.setupWithViewPager(binding.pager);
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