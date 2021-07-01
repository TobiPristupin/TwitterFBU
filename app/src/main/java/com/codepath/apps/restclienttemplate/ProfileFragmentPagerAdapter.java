package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.models.User;

import org.jetbrains.annotations.NotNull;

public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 2;
    private final String tabTitles[] = new String[]{"Followers", "Following"};
    private User user;

    public ProfileFragmentPagerAdapter(FragmentManager fm, User user) {
        super(fm);
        this.user = user;
    }


    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FollowersFragment.newInstance(user, FollowersFragment.State.SHOW_FOLLOWERS);
        } else if (position == 1) {
            return FollowersFragment.newInstance(user, FollowersFragment.State.SHOW_FOLLOWING);
        }

        throw new RuntimeException("Unreachable");
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
