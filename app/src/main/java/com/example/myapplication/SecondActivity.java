package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.common.app.Activity;

import butterknife.BindView;

public class SecondActivity extends Activity {
    @BindView(R.id.txt_test)
    TextView m;

    @Override
    protected void initWidget() {
        super.initWidget();
        m.setText("test");
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }
}
