package com.longrise.ticketunion.view;

import com.longrise.ticketunion.base.IBaseCallback;
import com.longrise.ticketunion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {

    /**
     * 请求返回的分类页面的数据
     *
     * @param contents
     */
    void onContentCallback(List<HomePagerContent.DataBean> contents);

    /**
     * 更多数据
     */
    void onLoadMoreCallback(List<HomePagerContent.DataBean> contents);

    void onLoadMoreError();

    void onLoadMoreEmpty();

    /**
     * 轮播图
     */
    void onLooperListCallback(List<HomePagerContent.DataBean> contents);

    /**
     * 获取Id
     */
    int getCategoryId();
}
