package com.longrise.ticketunion.view;

import com.longrise.ticketunion.base.IBaseCallback;
import com.longrise.ticketunion.model.domain.SelectedContent;
import com.longrise.ticketunion.model.domain.SelectedPageCategory;

public interface ISelectedPageCallback extends IBaseCallback {

    /**
     * 分类内容结果
     * @param categories
     */
    void onCategoriesCallback(SelectedPageCategory categories);

    /**
     * 内容
     * @param content
     */
    void onContentCallback(SelectedContent content);
}
