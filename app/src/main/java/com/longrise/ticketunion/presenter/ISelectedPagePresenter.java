package com.longrise.ticketunion.presenter;

import com.longrise.ticketunion.base.IBasePresenter;
import com.longrise.ticketunion.model.domain.SelectedPageCategory;
import com.longrise.ticketunion.view.ISelectedPageCallback;

public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback> {

    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取内容
     */
    void getContentByCategory(SelectedPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reloadContent();
}
