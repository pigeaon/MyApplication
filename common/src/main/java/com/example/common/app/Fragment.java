package com.example.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class Fragment extends androidx.fragment.app.Fragment{
    protected View mRoot;
    protected Unbinder mRootUnBinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if(mRoot == null){
        int layId = getContentLayoutId();
        //初始化当前的根布局，但不在创建时添加到container
       View root = inflater.inflate(layId,container,false);
       initWidget(root);
       }else {
           if (mRoot.getParent() != null){
               ((ViewGroup)mRoot.getParent()).removeView(mRoot);
           }

       };
        return mRoot;
    }


    /**
     * 初始化相关参数
     * @param bundle 参数
     * @return 如果参数正确，返回True 否则返回False
     */
    protected boolean initArgs(Bundle bundle){
        return true;

    }

    /**
     * 初始化窗口
     */
    protected void initWindows(){

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //界面初始化完成后再进行数据初始化
        initData();
    }

    /**
     * 得到当前界面的资源ID
     * @return 资源ID
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(View root){
        mRootUnBinder = ButterKnife.bind(this,root);

    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    /**
     * 返回按键 触发时调用
     * @return 返回true表示已处理返回逻辑，Acivity不用自己finish
     * 返回False表示暂未处理，Activity自己走自己的逻辑
     */
    public boolean onBackPress(){
        return false;
    }
}
