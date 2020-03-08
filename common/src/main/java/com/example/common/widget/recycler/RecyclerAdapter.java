package com.example.common.widget.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener,View.OnLongClickListener, AdapterCallback<Data> {
    private final List<Data> mDataList;
    private AdapterListerner<Data>mListerner;

    /**
     * 构造函数模块
     */
    public RecyclerAdapter(){
        this(null);
    }

    public RecyclerAdapter(AdapterListerner<Data> listerner){
        this(new ArrayList<Data>(),listerner);
    }

    public RecyclerAdapter(List<Data> dataList,AdapterListerner<Data> listerner){
        this.mDataList = dataList;
        this.mListerner = listerner;

    }

    /**
     * 复写默认的布局类型返回
     * @param position 坐标
     * @return 类型，复写后返回XML的ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position,mDataList.get(position));
    }

    /**
     * 得到布局的类型坐标
     * @param position 坐标
     * @param data 当前数据
     * @return XML的id,用于创建ViewHolder
     */
    protected abstract int getItemViewType(int position,Data data);

    /**
     * 创建一个ViewHolder
     * @param parent RecyclerView
     * @param viewType 界面的类型,约定为XML布局的Id
     * @return ViewHolder
     */
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //将xml初始化为View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //将id为viewType初始化为一个root view
        View root = inflater.inflate(viewType,parent,false);
        //子类定义抽象函数，得到ViewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root,viewType);

        //设置View的Tag为ViewHolder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder,holder);

        //设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        //进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder,root);
        //绑定calback
        holder.callback = this;


        return holder;
    }

    /**
     * d当得到一个新的Viewholder
     * @param root 根布局
     * @param viewType 布局类型，即XML的ID
     * @return VIewHolder
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root,int viewType);

    /**
     * 绑定数据到Holder上
     * @param holder ViewHolder
     * @param position 坐标
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        //需要的数据
        Data data = mDataList.get(position);
        //触发绑定的方法
        holder.bind(data);
    }

    /**
     * 得到当前集合的数据量
     * @return 集合的数据量
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入一条数据并通知更新
     * @param data
     */
    public void add(Data data){
        mDataList.add(data);
        notifyItemInserted(mDataList.size()-1);
    }

    /**
     * 插入
     * @param dataList
     */
    public void add(Data... dataList){
        if(dataList!=null&&dataList.length>0){
            int startPos = mDataList.size();
            Collections.addAll(mDataList,dataList);
            notifyItemRangeInserted(startPos,dataList.length);
        }
    }

    public void add(Collection<Data> dataList){
        if(dataList!=null&&dataList.size()>0){
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos,dataList.size());
        }
    }

    /**
     * 替换为一个新的集合，其中包括清空
     * @param dataList 一个新的集合
     */
    public void replace(Collection<Data> dataList){
        mDataList.clear();
        if(dataList==null||dataList.size()==0){
            return;
        }
        mDataList.addAll(dataList);
    }

    /**
     * 删除操作
     */
    public void clear(){
        mDataList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v){
        ViewHolder viewHolder = (ViewHolder)v.getTag(R.id.tag_recycler_holder);
        if(this.mDataList!=null){
            //得到viewHolder当前适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            //回调方法
            this.mListerner.onItemClick(viewHolder,mDataList.get(pos));
        }
    }
    @Override
    public boolean onLongClick(View v){
        ViewHolder viewHolder = (ViewHolder)v.getTag(R.id.tag_recycler_holder);
        if(this.mDataList!=null){
            //得到viewHolder当前适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            //回调方法
            this.mListerner.onItemLongClick(viewHolder,mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器监听
     * @param adapterListerner
     */
    public void setListerner(AdapterListerner<Data> adapterListerner){
        this.mListerner = adapterListerner;
    }

    @Override
    public void update(Data data,ViewHolder<Data> holder){
        int pos = holder.getAdapterPosition();
        if(pos>=0){
            //进行数据移除与更新
             mDataList.remove(pos);
             mDataList.add(pos,data);
             //通知更新
             notifyItemChanged(pos);
        }
    }

    /**
     * 自定义监听器
     * @param <Data>
     */
    public interface AdapterListerner<Data>{
        //当cell点击时触发
        void onItemClick(RecyclerAdapter.ViewHolder holder,Data data);
        //当cell长按时触发
        void onItemLongClick(RecyclerAdapter.ViewHolder holder,Data data);
    }

    /**
     * 自定义的ViewHolder
     * @param <Data> 泛型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder{
        protected Data mData;
        private Unbinder unbinder;
        private AdapterCallback<Data> callback;

        public ViewHolder(View itemView){
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         * @param data 绑定的数据
         */
        void bind(Data data){
            this.mData = data;
            onBind(data);
        }

        /**
         * 用于绑定数据的回调，必须复写
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);

        /**
         * Holdetr自己对自己相应的Data进行更新
         * @param data
         */
        public void updataData(Data data){
            if(this.callback != null){
                this.callback.update(data,this);
            }
        }

    }
    public static abstract class  AdapterListernerImpl<Data> implements AdapterListerner<Data>{

        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }


}
