package com.bkjk.infra.login_module.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bkjk.infra.login_module.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mLoginBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mLoginBt = findViewById(R.id.login_in_bt);
        mLoginBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.login_in_bt) {
            // 模拟直接按成功跳转,跳转到Home页

        }
    }
}
