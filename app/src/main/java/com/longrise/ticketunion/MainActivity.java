package com.longrise.ticketunion;

import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.longrise.ticketunion.base.BaseActivity;
import com.longrise.ticketunion.base.BaseFragment;
import com.longrise.ticketunion.ui.fragment.HomeFragment;
import com.longrise.ticketunion.ui.fragment.SellFragment;
import com.longrise.ticketunion.ui.fragment.SearchFragment;
import com.longrise.ticketunion.ui.fragment.SelectedFragment;
import com.longrise.ticketunion.view.IMainActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        IMainActivity {
    private BottomNavigationView mMainNavigationView;
    private HomeFragment mHomeFragment;
    private SellFragment mRedPacketFragment;
    private SearchFragment mSearchFragment;
    private SelectedFragment mSelectedFragment;
    private FragmentManager mFm;
    private BaseFragment mLastShowFragment;

    private final String TAG = "MainActivity";

    protected void initView() {
        mMainNavigationView = findViewById(R.id.main_navigation_bar);
        mMainNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void initEvent() {
        initFragments();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initPresenter() {

    }

    public void switch2SearchFragment() {
        mMainNavigationView.setSelectedItemId(R.id.search);
    }

    private void initFragments() {
        mFm = getSupportFragmentManager();
        mHomeFragment = new HomeFragment();
        mRedPacketFragment = new SellFragment();
        mSearchFragment = new SearchFragment();
        mSelectedFragment = new SelectedFragment();
        switchFragment(mHomeFragment);
    }

    private void switchFragment(BaseFragment targetFragment) {
        // 如果上一个fragment根当前要切换的fragment是同一个，那么不需要切换
        if (targetFragment == mLastShowFragment) {
            return;
        }
        FragmentTransaction ft = mFm.beginTransaction();
        if (targetFragment.isAdded()) {
            ft.show(targetFragment);
        } else {
            ft.add(R.id.main_page_container, targetFragment);
        }
        if (mLastShowFragment != null) {
            ft.hide(mLastShowFragment);
        }
        mLastShowFragment = targetFragment;
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home:
                switchFragment(mHomeFragment);
                break;
            case R.id.selected:
                switchFragment(mSelectedFragment);
                break;
            case R.id.red_packet:
                switchFragment(mRedPacketFragment);
                break;
            case R.id.search:
                switchFragment(mSearchFragment);
                break;
        }
        return true;
    }
}
