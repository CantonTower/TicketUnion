package com.longrise.ticketunion.utils;

import com.longrise.ticketunion.presenter.ICategoryPagerPresenter;
import com.longrise.ticketunion.presenter.IHomePresenter;
import com.longrise.ticketunion.presenter.ISearchPresenter;
import com.longrise.ticketunion.presenter.ISelectedPagePresenter;
import com.longrise.ticketunion.presenter.ISellPagePresenter;
import com.longrise.ticketunion.presenter.ITicketPagerPresenter;
import com.longrise.ticketunion.presenter.impl.CategoryPagerPresenterImpl;
import com.longrise.ticketunion.presenter.impl.HomePresenterImpl;
import com.longrise.ticketunion.presenter.impl.SearchPresenterImpl;
import com.longrise.ticketunion.presenter.impl.SelectedPagePresenterImpl;
import com.longrise.ticketunion.presenter.impl.SellPagePresenter;
import com.longrise.ticketunion.presenter.impl.TicketPagerPresenterImpl;

public class PresenterManager {
    private static final PresenterManager ourInstance = new PresenterManager();
    private final IHomePresenter mHomePresenter;
    private final ICategoryPagerPresenter mCategoryPagerPresenter;
    private final ITicketPagerPresenter mTicketPagerPresenter;
    private final ISelectedPagePresenter mSelectedPagePresenter;
    private final ISellPagePresenter mSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;

    public static PresenterManager getInstance() {
        return ourInstance;
    }

    private PresenterManager() {
        mHomePresenter = new HomePresenterImpl();
        mCategoryPagerPresenter = new CategoryPagerPresenterImpl();
        mTicketPagerPresenter = new TicketPagerPresenterImpl();
        mSelectedPagePresenter = new SelectedPagePresenterImpl();
        mSellPagePresenter = new SellPagePresenter();
        mSearchPresenter = new SearchPresenterImpl();
    }

    public IHomePresenter getHomePresenter() {
        return mHomePresenter;
    }

    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return mCategoryPagerPresenter;
    }

    public ITicketPagerPresenter getTicketPagerPresenter() {
        return mTicketPagerPresenter;
    }

    public ISelectedPagePresenter getSelectedPagePresenter() {
        return mSelectedPagePresenter;
    }

    public ISellPagePresenter getSellPagePresenter() {
        return mSellPagePresenter;
    }

    public ISearchPresenter getSearchPresenter() {
        return mSearchPresenter;
    }
}
