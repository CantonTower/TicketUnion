package com.longrise.ticketunion;

import android.os.Bundle;
import android.util.Log;

import com.longrise.ticketunion.ui.custom.TextFlowLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Test extends AppCompatActivity {
    private TextFlowLayout mTextFlowLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    private void initView() {
        mTextFlowLayout = findViewById(R.id.text_flow_layout);
        List<String> textList = new ArrayList<>();
        textList.add("毛衣");
        textList.add("键盘");
        textList.add("笔记本电脑");
        textList.add("机械键盘");
        textList.add("毛衣");
        textList.add("笔记本电脑");
        textList.add("毛衣");
        textList.add("机械键盘");
        textList.add("毛衣");
        textList.add("键盘");
        textList.add("笔记本电脑");
        textList.add("机械键盘");
        textList.add("毛衣");
        textList.add("笔记本电脑");
        textList.add("毛衣");
        textList.add("机械键盘");
        textList.add("毛衣");
        textList.add("键盘");
        textList.add("笔记本电脑");
        textList.add("机械键盘");
        textList.add("毛衣");
        textList.add("笔记本电脑");
        textList.add("毛衣");
        textList.add("机械键盘");
        mTextFlowLayout.setTextList(textList);
        mTextFlowLayout.setOnFlowTextItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowTextItemClick(String itemStr) {
                Log.i("main", "点击=" + itemStr);
            }
        });
    }




}
