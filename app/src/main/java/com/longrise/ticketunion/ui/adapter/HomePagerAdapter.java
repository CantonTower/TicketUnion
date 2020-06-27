package com.longrise.ticketunion.ui.adapter;

import com.longrise.ticketunion.model.domain.Categorys;
import com.longrise.ticketunion.ui.fragment.HomePagerFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private List<Categorys.DataBean> mCategoryList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mCategoryList.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return HomePagerFragment.newInstance(mCategoryList.get(position));
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    public void setCategories(Categorys categorys) {
        mCategoryList.clear();
        List<Categorys.DataBean> data = categorys.getData();
        mCategoryList.addAll(data);
        notifyDataSetChanged();
    }
}
