package com.bkjk.infra.home_module.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bkjk.infra.home_module.R;

public class HomeIndexActivity extends AppCompatActivity {

    private TextView mHomeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_index_activity_layout);

        initView();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mHomeTv = findViewById(R.id.home_index_tv);
    }

    /**
     * 加载数据
     */
    private void initData() {

    }
}
