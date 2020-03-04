package com.example.common.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.common.R2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class Activity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化前初始化的数据
        initWindows();
        if(initArgs(getIntent().getExtras())){
            //得到界面id并进行设置，如果只是getContentLayoutId只是单纯获取。
            int layId = getContentLayoutId();
            setContentView(layId);
            initData();
            initWidget();
        }else{
            finish();
        }

    }

    /**
     * 初始化窗口
     */
    protected void initWindows(){

    }

    /**
     * 初始化相关参数
     * @param bundle 参数（bundle系统定义的数据，可以将之上传到全局变量onSavedInstance，以供调用）
     * @return 如果参数正确，返回True 否则返回False
     */
    protected boolean initArgs(Bundle bundle){
        return true;

    }

    /**
     * 得到当前界面的资源ID
     * @return 资源ID
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(){
        ButterKnife.bind(this);

    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时，finish当前界面
        //页面退出时应销毁
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //多个fragment
        //得到当前Activity所有的Fragment
       List<Fragment> framents = getSupportFragmentManager().getFragments();
        if (framents!=null&&framents.size()>0){
            for(Fragment fragment:framents){
                if(fragment instanceof com.example.common.app.Fragment){
                    //是否来自当前的Fragment
                    if(((com.example.common.app.Fragment) fragment).onBackPress()){
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
    }
}
