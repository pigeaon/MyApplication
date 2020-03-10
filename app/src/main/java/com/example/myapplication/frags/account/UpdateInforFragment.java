package com.example.myapplication.frags.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bumptech.glide.Glide;
import com.example.common.app.Application;
import com.example.common.app.Fragment;
import com.example.common.widget.PortraitView;
import com.example.factory.Factory;
import com.example.factory.net.UploadHelper;
import com.example.myapplication.R;
import com.example.myapplication.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInforFragment extends Fragment {
    private static final String TAG = "UpdateInforFragment";
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    public UpdateInforFragment() {
        // Required empty public constructor
    }



    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_infor;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick(){
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        //图片设置JPEG格式
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置压缩图片的精度
                        options.setCompressionQuality(96);
                        //得到图片的缓存地址
                        File dPath = Application.getPortraitTmpFile();
                        //进行切割
                        UCrop.of(Uri.fromFile(new File(path)),Uri.fromFile(dPath))//新头像缓存于缓存文件夹
                                .withAspectRatio(1,1)//图片宽高比1：1
                                .withMaxResultSize(520,520)//最大尺寸为520*520
                                .withOptions(options)//以上设置的相关参数
                                .start(getActivity());//
                    }
                }).show(getChildFragmentManager(),
                GalleryFragment.class.getName());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //收到从Activity中传递过来的值，然后取出图片进行加载
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            //通过UCrop得到对应的Uri
            final Uri resultUri = UCrop.getOutput(data);
            if(resultUri!=null){
                loadPortrait(resultUri);
            }


        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 加载Uri到当前的头像中
     * @param uri
     */
    private void loadPortrait(Uri uri){
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);
        String localPath = uri.getPath();
        Log.e(TAG, "loadPortrait: "+localPath );
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = UploadHelper.uploadPortraits(localPath);
                Log.e(TAG, "run: "+url );
            }
        });

    }
}
