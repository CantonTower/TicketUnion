package com.longrise.ticketunion.view;

import com.longrise.ticketunion.base.IBaseCallback;
import com.longrise.ticketunion.model.domain.SellContent;

public interface ISellPageCallback extends IBaseCallback {

    /**
     * 特惠内容加载完成
     */
    void onContentCallback(SellContent result);

    /**
     * 记载更多的结果
     */
    void onLoadMoreContentCallback(SellContent moreResult);

    /**
     * 加载更多失败
     */
    void onMoreLoadedError();

    /**
     * 没有更多内容
     */
    void onMoreLoadedEmpty();
}
