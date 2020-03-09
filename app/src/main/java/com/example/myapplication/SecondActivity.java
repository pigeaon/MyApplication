package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.common.app.Activity;
import com.example.common.widget.PortraitView;
import com.example.myapplication.activities.AccountActivity;
import com.example.myapplication.frags.main.ActiveFragment;
import com.example.myapplication.frags.main.ContactFragment;
import com.example.myapplication.frags.main.GroupFragment;
import com.example.myapplication.helper.NavHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.OnClick;

public class SecondActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener ,
                NavHelper.OnTabChangedListener<Integer>{
    private static final String TAG = "SecondActivity";
    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigaton)
    BottomNavigationView mNavigationView;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelp;
    @Override
    protected void initWidget() {
        super.initWidget();
        mNavigationView.setOnNavigationItemSelectedListener(this);
        //初始化底部辅助工具类
       mNavHelp = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(),this);
        mNavHelp.add(R.id.action_home,new NavHelper.Tab<>(ActiveFragment.class,R.string.title_home))
                .add(R.id.action_group,new NavHelper.Tab<>(GroupFragment.class,R.string.title_group))
                .add(R.id.action_contact,new NavHelper.Tab<Integer>(ContactFragment.class,R.string.title_contact ));
        //添加对底部栏的监听


        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();

        Menu menu = mNavigationView.getMenu();
        Log.d(TAG, "initData: ");
        menu.performIdentifierAction(R.id.action_home,0);

    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick(){

    }

    @OnClick(R.id.btn_action)
    void onActionClick(){
        AccountActivity.show(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    boolean isFirst = true;

    /**
     * 底部导航栏点击时触发
     * @param item
     * @return true代表能够处理点击，点击完成
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //转接事件流到工具类
        return mNavHelp.performClickMenu((item.getItemId()));
    }

    /**
     * NavHelper处理后回调的方法
     * @param newTab 新Tab
     * @param oldTab 旧Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //从额外字段中资源ID
        mTitle.setText(newTab.extra);

        //对浮动按钮进行隐藏与显示的动画
        float transY = 0;
        float rotation = 0;
        if(newTab.extra.equals(R.string.title_home)){
            //主界面时隐藏
            transY = Ui.dipToPx(getResources(),76);
        }else{
            //默认为0则显示
            if(newTab.extra.equals(R.string.title_group)){
                //群
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            }else{
                //联系人
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }

        //开始动画
        //旋转/Y轴位移，弹性差值器，时间
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .setDuration(480)
                .start();
    }
}
