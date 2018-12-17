package com.bkjk.infra.router.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bkjk.infra.router.R;
import com.bkjk.infra.router_compiler.annotation.HenryAnnotation;
import com.bkjk.infra.router_compiler.annotation.demoprocessor.auto.Henry$$HENRY;

@HenryAnnotation(
        name = "Henry",
        text = "Hello! Welcome")
public class MainActivity extends AppCompatActivity {

    private TextView mShowTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShowTv = findViewById(R.id.show_text_tv);
    }

    public void showText(View view) {
        Henry$$HENRY henry$$HENRY = new Henry$$HENRY();
        String msg = henry$$HENRY.getMessage();
        if (!TextUtils.isEmpty(msg)) {
            mShowTv.setVisibility(View.VISIBLE);
            mShowTv.setText(msg);
        }
    }
}
