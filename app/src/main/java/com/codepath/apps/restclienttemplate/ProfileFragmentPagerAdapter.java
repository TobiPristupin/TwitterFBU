package com.codepath.apps.restclienttemplate;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.models.User;

import org.jetbrains.annotations.NotNull;

public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Followers", "Following"};
    private Context context;
    private User user;

    public ProfileFragmentPagerAdapter(FragmentManager fm, Context context, User user) {
        super(fm);
        this.context = context;
        this.user = user;
    }


    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return FollowersFragment.newInstance(user, FollowersFragment.State.SHOW_FOLLOWERS);
        } else if (position == 1){
            return FollowersFragment.newInstance(user, FollowersFragment.State.SHOW_FOLLOWING);
        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
