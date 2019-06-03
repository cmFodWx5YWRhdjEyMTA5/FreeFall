package com.develop.loginov.freefall.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Page> pageList;


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        pageList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return pageList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageList.get(position).getTitle();
    }

    public void addPage(Fragment fragment, String title) {
        pageList.add(new Page(fragment, title));
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public void clearPages() {
        pageList = new ArrayList<>();
        pageList.clear();
        notifyDataSetChanged();
    }

    class Page {
        private Fragment fragment;
        private String title;

        Page(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public String getTitle() {
            return title;
        }


        @Override
        public String toString() {
            return title;
        }
    }
}


