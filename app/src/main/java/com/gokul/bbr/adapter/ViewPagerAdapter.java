package com.gokul.bbr.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.gokul.bbr.fragment.AudioFragment;
import com.gokul.bbr.fragment.PlayListFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new AudioFragment();
        }
        return new PlayListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
