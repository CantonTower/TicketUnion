package com.longrise.ticketunion.view;

import com.longrise.ticketunion.base.IBaseCallback;
import com.longrise.ticketunion.model.domain.Categorys;

public interface IHomeCallback extends IBaseCallback {

    void onCategoryCallback(Categorys categorys);
}
