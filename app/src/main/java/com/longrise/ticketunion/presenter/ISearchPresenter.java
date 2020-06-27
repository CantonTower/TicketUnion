package com.longrise.ticketunion.presenter;

import com.longrise.ticketunion.base.IBasePresenter;
import com.longrise.ticketunion.view.ISearchViewCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchViewCallback> {

    /**
     * 获取搜索历史内容
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 搜索
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void research();

    /**
     * 获取更多搜索结果
     */
    void loaderMore();

    /**
     * 获取推荐词
     */
    void getReCommendWords();
}
