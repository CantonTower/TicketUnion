package com.longrise.ticketunion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.longrise.ticketunion.model.domain.IBaseInfo;
import com.longrise.ticketunion.presenter.ITicketPagerPresenter;
import com.longrise.ticketunion.ui.activity.TicketActivity;

public class TicketUtil {

    public static void toTicketPage(Context context, IBaseInfo baseInfo) {
        String title = baseInfo.getTicketTitle();
        String ticketUrl = baseInfo.getTicketUrl();
        String photoUrl = baseInfo.getTicketPhoto();
        ITicketPagerPresenter ticketPagerPresenter = PresenterManager.getInstance().getTicketPagerPresenter();
        ticketPagerPresenter.getTicketData(title, ticketUrl, photoUrl);
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
