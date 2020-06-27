package com.longrise.ticketunion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.longrise.ticketunion.R;
import com.longrise.ticketunion.base.BaseActivity;
import com.longrise.ticketunion.model.domain.TicketResult;
import com.longrise.ticketunion.presenter.ITicketPagerPresenter;
import com.longrise.ticketunion.ui.custom.LoadingView;
import com.longrise.ticketunion.utils.PresenterManager;
import com.longrise.ticketunion.utils.UrlUtils;
import com.longrise.ticketunion.view.ITicketPagerCallback;

public class TicketActivity extends BaseActivity implements ITicketPagerCallback{
    private ITicketPagerPresenter mTicketPresenter;
    private ImageView mivTicketPhoto;
    private EditText metTicketCode;
    private TextView mtvTicketCopyOrOpen;
    private ImageView mivTicketBack;
    private LoadingView mPhotoLoadingView;
    private TextView mtvPhotoLoadRetry;
    private boolean mHasTaobaoApp;

    @Override
    protected void initView() {
        mivTicketPhoto = findViewById(R.id.iv_ticket_photo);
        metTicketCode = findViewById(R.id.et_ticket_code);
        mtvTicketCopyOrOpen = findViewById(R.id.tv_ticket_copy_or_open);
        mivTicketBack = findViewById(R.id.iv_ticket_back);
        mPhotoLoadingView = findViewById(R.id.ticket_photo_loading);
        mtvPhotoLoadRetry = findViewById(R.id.tv_ticket_load_retry);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void initEvent() {
        mivTicketBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mtvTicketCopyOrOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 复制淘口令
                String ticketCode = metTicketCode.getText().toString().trim();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 复制到粘贴板
                ClipData clipData = ClipData.newPlainText("sob_taobao_ticket_code", ticketCode);
                cm.setPrimaryClip(clipData);
                if (mHasTaobaoApp) {
                    // 打开淘宝
                    Intent taobaoIntent = new Intent();
                    ComponentName componentName = new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);
                } else {
                    Toast.makeText(TicketActivity.this, "已经复制，粘贴分享，或打开淘宝", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void initPresenter() {
        mTicketPresenter = PresenterManager.getInstance().getTicketPagerPresenter();
        mTicketPresenter.registerViewCallback(this);

        // 检查是否有安装淘宝app
        // 淘宝的包名：com.taobao.taobao
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaobaoApp = packageInfo != null;
        } catch (Exception e) {
            e.printStackTrace();
            mHasTaobaoApp = false;
        }
        mtvTicketCopyOrOpen.setText(mHasTaobaoApp ? "打开淘宝领劵" : "复制淘口令");
    }

    @Override
    protected void release() {
        if (mTicketPresenter != null) {
            mTicketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onTicketCallback(String photoUrl, TicketResult ticketResult) {
        if (mPhotoLoadingView != null) {
            mPhotoLoadingView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(photoUrl)) {
            String url = UrlUtils.getPhotoPath(photoUrl);
            Glide.with(this).load(url).into(mivTicketPhoto);
        }
        if (ticketResult != null && ticketResult.getData().getTbk_tpwd_create_response() != null) {
            metTicketCode.setText(ticketResult.getData().getTbk_tpwd_create_response().getData().getModel());
        }
    }

    @Override
    public void onLoading() {
        if (mtvPhotoLoadRetry != null) {
            mtvPhotoLoadRetry.setVisibility(View.GONE);
        }
        if (mPhotoLoadingView != null) {
            mPhotoLoadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError() {
        if (mPhotoLoadingView != null) {
            mPhotoLoadingView.setVisibility(View.GONE);
        }
        if (mtvPhotoLoadRetry != null) {
            mtvPhotoLoadRetry.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEmpty() {

    }
}
