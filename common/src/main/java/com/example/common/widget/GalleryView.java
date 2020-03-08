package com.example.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.common.R;
import com.example.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * TODO: document your custom view class.
 */
public class GalleryView extends RecyclerView {
    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IMAGE_SIZE =3; //最大的选中图片数量
    private static final int MIN_IMAGE_FILE_SIZE =10*1024; //最大的选中图片数量
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private Adapter mAdapter = new Adapter() ;
    private SelectChangeListener mListener;
    //较少遍历，长度变更情况多 使用LinkedList
    private List<Image> mSelectedImages = new LinkedList<>();

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(),4));
        setAdapter(mAdapter);
        mAdapter.setListerner(new RecyclerAdapter.AdapterListernerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
            //如果cell点击被允许，更新响应状态
                if(onItemSeclectCLick(image)){
                    holder.updataData(image);
                }
            }
        });
    }

    /**
     * 初始化方法
     * @param loaderManager loader管理器
     * @return 返回LOADER_ID 可销毁
     */
    public int setup(LoaderManager loaderManager,SelectChangeListener listener){
        mListener = listener;
        loaderManager.initLoader(LOADER_ID,null,mLoaderCallback);
        return LOADER_ID;
    }

    /**
     * cell实现的具体逻辑
     * @param image
     * @return true表示数据进行了更改，需要刷新
     */
    private boolean onItemSeclectCLick(Image image){
        boolean notifyRefresh;
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
            image.isSelect = false;
            notifyRefresh = true;
        }else{
            if(mSelectedImages.size()>=MAX_IMAGE_SIZE){
                //得到填充文字
                notifyRefresh = false;
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str,MAX_IMAGE_SIZE);
                Toast.makeText(getContext(),
                        str,Toast.LENGTH_SHORT).show();
            }else{
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }

        if (notifyRefresh){
            notifySelectChanged();
        }

        return notifyRefresh;
    }

    public String[] getSelectedPath(){
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image image :mSelectedImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    /**
     * 清空所选
     */
    public void clear(){
        for (Image image : mSelectedImages) {//m .for快捷键
            //一定进行，否则可能上面操作已调用
            image.isSelect = false;
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 通知外部监听者 数据选中改变
     */
    private void notifySelectChanged(){
        //得到监听者，并判断有无 进行数量变化回调
        SelectChangeListener listener =mListener;
        if(listener!=null){
            listener.onSelectCountChange(mSelectedImages.size());
        }
    }

    private void updateSource(List<Image> images){
        mAdapter.replace(images);
    }

    /**
     * 用于实际的数据加载的LoaderCallback
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor>{
        private final String[] IMAGE_POROJECTION = new String[]{
                MediaStore.Images.Media._ID,//id
                MediaStore.Images.Media.DATA,//图片路径
                MediaStore.Images.Media.DATE_ADDED//图片创建时间

    };
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            //创建一个loader
            if(id==LOADER_ID){
                //如果是本身ID则初始化
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        IMAGE_POROJECTION[2]+" DESC");//逆序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            //loader创建完成
            List<Image> images = new ArrayList<>();
            //判断是否有数据
            if(data!=null){
                int count = data.getCount();
                if(count>0){
                    //移动到游标开始
                    data.moveToFirst();
                    //得到对应的列的INdex坐标
                    int indexId = data.getColumnIndexOrThrow(IMAGE_POROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_POROJECTION[1]);
                    int indexData = data.getColumnIndexOrThrow(IMAGE_POROJECTION[2]);

                    do{
                        //循环读取，直到下一数据为空
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long dataTime = data.getLong(indexData);
                        File file = new File(path);
                        if(!file.exists()||file.length() < MIN_IMAGE_FILE_SIZE){
                            //如果没有图片或图片太小，跳过
                            continue;
                        }
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.data = dataTime;
                        images.add(image);
                    }while(data.moveToNext());

                }
            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            //loader销毁或重建
            updateSource(null);
        }
    }

    /**
     * 内部的数据结构 图片单元
     */
    private static class Image{
        int id;//数据id
        String path;//图片路径
        long data;//图片创建日期
        boolean isSelect;//是否被选中

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Image image = (Image) o;
            return Objects.equals(path, image.path);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public int hashCode() {
            return Objects.hash(path);
        }
    }


    /**
     * 适配器
     */
    private class Adapter extends RecyclerAdapter<Image>{
        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_gallery;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

    }

    /**
     * cell对应的holder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image>{
        private ImageView mPic;
        private View mShade;
        private CheckBox mSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            mPic = (ImageView)itemView.findViewById(R.id.im_image);
            mShade = itemView.findViewById(R.id.view_shade);
            mSelected = (CheckBox) itemView.findViewById(R.id.cb_select);

        }

        @Override
        protected void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)//加载路径
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存，直接从原图加载
                    .centerCrop()//居中剪切
                    .placeholder(R.color.gray)//默认颜色
                    .into(mPic);

            mShade.setVisibility(image.isSelect?VISIBLE:INVISIBLE);
            mSelected.setChecked(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }
    }

    public interface SelectChangeListener{
        void onSelectCountChange(int count);
    }

}
