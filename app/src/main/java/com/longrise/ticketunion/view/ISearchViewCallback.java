package com.longrise.ticketunion.view;

import com.longrise.ticketunion.base.IBaseCallback;
import com.longrise.ticketunion.model.domain.SearchRecommend;
import com.longrise.ticketunion.model.domain.SearchResult;

import java.util.List;

public interface ISearchViewCallback extends IBaseCallback {

    /**
     * 搜索历史结果
     */
    void onHistoriesLoaded(List<String> histories);

    /**
     * 历史记录删除完成
     */
    void onHistoriesDeleted();

    /**
     * 搜索结果
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载更多内容成功
     */
    void onLoadedMoreSuccess(SearchResult result);

    /**
     * 没有更多内容了
     */
    void onLoadedMoreEmpty();

    /**
     * 获取更多内容加载失败
     */
    void onLoadedMoreError();

    /**
     * 获取推荐词成功
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);
}
