package com.longrise.ticketunion.presenter;

import com.longrise.ticketunion.base.IBasePresenter;
import com.longrise.ticketunion.view.ICategoryPagerCallback;

/**
 * 获取分类页面数据的接口
 */
public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    /**
     * 根据分类Id获取内容
     */
    void getContentByCategoryId(int categoryId);

    void loadMore(int categoryId);

    void reload(int categoryId);
}
