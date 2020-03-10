package com.example.myapplication.frags.assist;


import android.Manifest;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.app.Application;
import com.example.myapplication.R;
import com.example.myapplication.media.GalleryFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionFragment extends BottomSheetDialogFragment
implements EasyPermissions.PermissionCallbacks{
    //权限回调标识
    private static final int RC = 0x0100;

    public PermissionFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //官方操作
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取布局中的控件
        View root= inflater.inflate(R.layout.fragment_permission, container, false);

        //按钮绑定
        root.findViewById(R.id.btn_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //进行权限申请
                        requestPerm();
                    }
                });
        return root;
    }

    /**
     * 刷新布局中图片的状态
     * @param root
     */
    private void refreshState(View root){
        if(root == null){
            return;
        }

        Context context = getContext();
        root.findViewById(R.id.im_state_permission_network)
                .setVisibility(haveNetworkPerms(context)?View.VISIBLE:View.GONE);

        root.findViewById(R.id.im_state_permission_read)
                .setVisibility(haveReadPerm(context)?View.VISIBLE:View.GONE);

        root.findViewById(R.id.im_state_permission_write)
                .setVisibility(haveWritePerm(context)?View.VISIBLE:View.GONE);

        root.findViewById(R.id.im_state_permission_record_audio)
                .setVisibility(haveAudioRecordPerm(context)?View.VISIBLE:View.GONE);
    }

    /**
     * 获取是否有网络权限
     * @param context
     * @return
     */
    private static boolean haveNetworkPerms(Context context){
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    /**
     * 获取是否有读取权限
     * @param context
     * @return
     */
    private static boolean haveReadPerm(Context context){
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    /**
     * 获取是否有写入权限
     * @param context
     * @return
     */
    private static boolean haveWritePerm(Context context){
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    /**
     * 获取是否有录音权限
     * @param context
     * @return
     */
    private static boolean haveAudioRecordPerm(Context context){
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };
        return EasyPermissions.hasPermissions(context,perms);
    }

    /**
     * 显示权限申请界面
     * @param manager
     */
    private static void show(FragmentManager manager){
        new PermissionFragment()
                .show(manager,PermissionFragment.class.getName());
    }

    /**
     * 检查是否具有权限
     * @param context
     * @param manager
     * @return 是否有权限
     */
    public static boolean haveAll(Context context,FragmentManager manager){
        //检查是否具有所有权限
        boolean haveAll = haveNetworkPerms(context)
                &&haveReadPerm(context)
                &&haveWritePerm(context)
                &&haveAudioRecordPerm(context);
        //若没有，则显示权限申请界面
        if(!haveAll){
            show(manager);
        }
        return haveAll;
    }

    @AfterPermissionGranted(RC)
    private void requestPerm(){
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        if (EasyPermissions.hasPermissions(getContext(),perms)){
            Application.showToast(R.string.label_permission_ok);
            //Fragment中调用getView得到根布局，前提是在onCreateView方法之后
            refreshState(getView());
        }else {
            EasyPermissions.requestPermissions(this,getString(R.string.title_assist_permissions),RC,perms);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //如果有权限未申请成功，则弹出弹出框，用户点击后去到设置界面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show();
        }
    }

    /**
     * 权限申请的时候回调的方法，在这个方法中把对应权限申请状态交给EasuPermisson框架
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //传递对应参数，并且告知接受权限的处理者是自己
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
}
