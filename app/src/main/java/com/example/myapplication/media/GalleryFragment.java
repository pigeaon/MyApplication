package com.example.myapplication.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.common.tools.UiTool;
import com.example.common.widget.GalleryView;
import com.example.myapplication.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.qiujuer.genius.ui.Ui;

/**
 * 图片选择
 */
public class GalleryFragment extends BottomSheetDialogFragment
implements GalleryView.SelectChangeListener {
    private GalleryView mGallery;
    //疑问，此时setlistener尚未调用，何时对于mListener进行了调用
    private OnSelectedListener mListener;

    public GalleryFragment() {
        // Required empty public constructor

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //官方操作
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取galleryView
        View root= inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = (GalleryView)root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onSelectCountChange(int count) {
        if(count>0){
            dismiss();
            if(mListener!=null){
                //得到选中图片路径
                String[] paths = mGallery.getSelectedPath();
                //返回第一张
                mListener.onSelectedImage(paths[0]);
                //取消和唤起者之间的引用，加快内存回收
                mListener = null;
            }
        }
    }

    /**
     * 设置事件监听并返回自己
     * @param listener OnSelectedListener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener){
        mListener = listener;
        return this;
    }

    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener{
        void onSelectedImage(String path);
    }

    private static class TransStatusBottomSheetDialog extends BottomSheetDialog{

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if(window == null)
                return;

            //得到屏幕高度
            int screenHeight = UiTool.getScreenHeight(getOwnerActivity());
            //得到状态栏高度
            int statusHeight = UiTool.getStatusBarHeight(getOwnerActivity());


            //二者相减即为本布局高度
            int diaglogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT
                    ,diaglogHeight<=0?ViewGroup.LayoutParams.MATCH_PARENT:diaglogHeight);
        }
    }
}
