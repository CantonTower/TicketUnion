package com.longrise.ticketunion.presenter;

import com.longrise.ticketunion.base.IBasePresenter;
import com.longrise.ticketunion.view.ISellPageCallback;

public interface ISellPagePresenter extends IBasePresenter<ISellPageCallback> {

    /**
     * 加载特惠内容
     */
    void getSellContent();

    /**
     * 重新加载特惠内容
     */
    void reloadContent();

    /**
     * 加载更多特惠内容
     */
    void loadMoreContent();
}
