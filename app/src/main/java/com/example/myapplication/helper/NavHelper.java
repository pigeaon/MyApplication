package com.example.myapplication.helper;

import android.content.Context;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



/**
 * 解决对Fragment的调度与重用问题
 * 达到最优的Fragment切换
 */
public class NavHelper<T> {
    //所有Tab集合
    private final SparseArray<Tab<T>>tabs = new SparseArray();
    private final Context context;
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final OnTabChangedListener<T> listener;
    //当前的一个选中的Tab
    private Tab<T> currentTab;

    public NavHelper(Context context,
                     int containerId,
                     FragmentManager fragmentManager,
                     OnTabChangedListener listener) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.listener = listener;
    }



    /**
     * 执行点击菜单操作
     * @param menuId 菜单Id
     * @return 是否能够处理这一点击
     */
    public boolean performClickMenu(int menuId){
        //集合中寻找相应的菜单Tab
        Tab<T> tab = tabs.get(menuId);
        if(tab!=null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行真实的Tab选择操作
     * @param tab
     */
    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;
        if(currentTab!=null){
            oldTab = currentTab;
            if(oldTab==tab){
                //如果说当前的Tab就是点击的Tab
                //则不做处理
                notifyTabReselect(tab);
                return;
            }
        }
        //赋值并调用切换方法；
        currentTab = tab;
        doTabChanged(currentTab,oldTab);
    }

    private void doTabChanged(Tab<T> newTab,Tab<T> oldTab){
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if(oldTab!=null){
            if(oldTab.fragment!=null){
                //从界面中移除 依然存在
                ft.detach(oldTab.fragment);
            }
        }
        if(newTab!=null){
            if(newTab.fragment==null){
                Fragment fragment = Fragment.instantiate(context,newTab.clx.getName());
                //首次缓存
                newTab.fragment = fragment;
                //提交到FragmentManger
                ft.add(containerId,fragment,newTab.clx.getName());
            }else{
                //从提交到fragmentmanger缓存空间重新加载
                ft.attach(newTab.fragment);
            }
        }
        //提交事务
        ft.commit();
        //通知回调
        notifyTabSelect(newTab,oldTab);
    }

    /**
     * 回调监听器
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    private void notifyTabSelect(Tab<T> newTab,Tab<T> oldTab){
        if(listener!=null){
            listener.onTabChanged(newTab,oldTab);
        }
    }

    private void notifyTabReselect(Tab<T> tab){
        //TODO 二次点击Tab所做的操作

    }

    /**
     * 所有Tab基础属性（可以理解为各个fragment）
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T>{
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        //        Fragment对应的class信息
        public Class<?> clx;
        //额外的字段，用户自己设定需要用的
        public T extra;
        //内部缓存对应的fragment
        //包内权限Private，外部无法使用
        private Fragment fragment;
    }

    /**
     * 定义事件完成后的回调接口
     * @param <T>
     */
    public interface OnTabChangedListener<T>{
        void onTabChanged(Tab<T> newTab,Tab<T> oldTab);
    }

    /**
     * 流水式添加
     * 添加Tab
     * @param menuId Tab对应Id
     * @param tab
     */
    public NavHelper<T>  add(int menuId,Tab<T> tab){
        tabs.put(menuId,tab);
        return this;
    }

    /**
     * 获取当前显示的Tab
     * @return 当前的Tab
     */
    public Tab<T> getCurrentTab(){
        return currentTab;
    }
}
