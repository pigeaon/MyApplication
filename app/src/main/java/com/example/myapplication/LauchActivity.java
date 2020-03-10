package com.example.myapplication;

import android.os.Bundle;

import com.example.common.app.Activity;
import com.example.myapplication.activities.SecondActivity;
import com.example.myapplication.frags.assist.PermissionFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class LauchActivity extends Activity {
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_lauch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PermissionFragment.haveAll(this,getSupportFragmentManager())){
           SecondActivity.show(this);
                   finish();
        }
    }
}
