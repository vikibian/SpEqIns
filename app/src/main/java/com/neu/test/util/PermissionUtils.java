package com.neu.test.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.ArraySet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.Size;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.barteksc.pdfviewer.util.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by Viki on 2020/3/22
 * system login name : lg
 * created time : 14:33
 * email : 710256138@qq.com
 */
public class PermissionUtils extends Activity {

    private static final int SDK_PERMISSION_REQUEST = 127;
    public static final int GOTO_SEETING_CODE = 152;
    private AppCompatActivity activity1;
    private Context context1;
    public static AlertDialog dialog;
    private String[] permss = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public String[] permssName = {
            "位置信息","位置信息","麦克风","网络","设备信息","相机","存储","存储"
    };
    public static Map<String,String> permissionMap = new HashMap<>();

    public PermissionUtils(AppCompatActivity activity, Context context, @Nullable String[] stringpermission,@Nullable String[] chineseName) {
        if(stringpermission != null && chineseName != null){
            this.permss = stringpermission;
            this.permssName = chineseName;
        }

        this.activity1 = activity;
        this.context1 = context;
        initMap(permss,permssName);
    }
    public void initMap(String[] permissions,String[] permissionChineseName){
        permissionMap.clear();
        for(int i =0 ;i < permissions.length; i++){
            permissionMap.put(permissions[i],permissionChineseName[i]);
        }
    }


    public boolean canGoNextstep(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        else{
            ArrayList<String> permissions = new ArrayList<String>();
            for (String perm : permss) {
                if (ContextCompat.checkSelfPermission(context1, perm) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(perm);
                }
            }
            if (permissions.size() > 0) {
                return false;
            }else{
                return true;
            }
        }
    }

    public void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            for (String perm : permss) {
                if (ContextCompat.checkSelfPermission(context1, perm) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(perm);
                }
            }
            if (permissions.size() > 0) {
                String[] pString = new String[permissions.size()];
                ActivityCompat.requestPermissions(activity1, permissions.toArray(pString),SDK_PERMISSION_REQUEST);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //授予的权限。
        boolean show = true;
        List<String> granted = new ArrayList<>();
        //拒绝的权限
        List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }
        List<String> deniedPersistant = new ArrayList<>();
        for (String deniedPermission : denied) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity1, deniedPermission)) {
                deniedPersistant.add(deniedPermission);
                Log.e("message","deniedPermission ppp");
            }
            else{
                show = false;
                Log.e("message","deniedPermission dodo");
                ActivityCompat.requestPermissions(activity1, denied.toArray(new String[denied.size()]),SDK_PERMISSION_REQUEST);
            }
        }
        if(show){
            if(deniedPersistant.size()>0){
                Set<String> mySet = new ArraySet<>();
                for(String deniedC : deniedPersistant){
                    mySet.add(permissionMap.get(deniedC));
                }
                String content = "";
                for(String my : mySet){
                    content = content + my + ",";
                }
                content = content.substring(0,content.length()-1);
                showDialogGoToAppSettting(activity1,content);
            }
        }
    }


    public static boolean permissionPermanentlyDenied(Activity activity, @NonNull String perms) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, perms)) {
            showDialogGoToAppSettting(activity,perms);
            return true;
        }
        return false;
    }

    public static void showDialogGoToAppSettting(final Activity activity,String permission) {
        dialog = new AlertDialog.Builder(activity)
                .setMessage("去设置界面开启("+permission+")权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting(activity);
                    }
                }).setCancelable(false).show();
    }

    public static void showDialogGoToAppSettting(final Activity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage("去设置界面开启权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting(activity);
                    }
                }).setCancelable(false).show();
    }


    /**
     * 跳转到应用设置界面
     */
    public static void goToAppSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, GOTO_SEETING_CODE);
        dialog.dismiss();
    }

    public static void showPermissionReason(final int requestCode, final Activity activity, final String[] permission, String s) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(s)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission(activity, requestCode, permission);
                    }
                })
                .setCancelable(false).show();
    }





















    /**
     * 判断是否有权限
     *
     * @param context
     * @param perms
     * @return
     */
    public static boolean hasPermissions(@NonNull Context context, @Size(min = 1) @NonNull String... perms) {
        //判断SDK版本
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (context == null) {
            throw new IllegalArgumentException("Can't check permissions for null context");
        }

        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {

                return false;
            }
        }
        return true;
    }


    /**
     * 申请权限
     */
    public static void requestPermission(@NonNull Activity activity, int requestCode, String[] permissions) {

        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        String[] permissionsArray = permissionList.toArray(new String[permissionList.size()]);//将List转为数组
        if (permissionList.isEmpty()) {
            //不可能为空
        } else {
            ActivityCompat.requestPermissions(activity, permissionsArray, requestCode);
            //返回结果onRequestPermissionsResult
        }
    }


    /**
     * 申请权限的回调
     *
     * @param requestCode  请求权限时传入的请求码，用于区别是哪一次请求的
     * @param permissions  所请求的所有权限的数组
     * @param grantResults 权限授予结果，和 permissions 数组参数中的权限一一对应，元素值为两种情况，如下:
     *                     授予: PackageManager.PERMISSION_GRANTED
     *                     拒绝: PackageManager.PERMISSION_DENIED
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                  @NonNull int[] grantResults, @NonNull PermissionCallbacks callBack) {
        //授予的权限。
        List<String> granted = new ArrayList<>();

        //拒绝的权限
        List<String> denied = new ArrayList<>();


        for (int i = 0; i < permissions.length; i++) {
            String perm = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                denied.add(perm);
            }
        }

        if (null != callBack) {
            if (denied.isEmpty()) {
                callBack.onPermissionsAllGranted(requestCode, granted, denied.isEmpty());
            }

            if (!denied.isEmpty()) {
                callBack.onPermissionsDenied(requestCode, denied);
            }
        }
    }

    /**
     * 用户是否拒绝权限，并检查“不要提醒”。
     *
     * @param activity
     * @param perms
     * @return
     */
    public static boolean somePermissionPermanentlyDenied(Activity activity, @NonNull List<String> perms) {
        for (String deniedPermission : perms) {
            if (permissionPermanentlyDenied(activity, deniedPermission)) {
                return true;
            }
        }

        return false;
    }



    public interface PermissionCallbacks {

        /**
         * @param isAllGranted 是否全部同意
         */
        void onPermissionsAllGranted(int requestCode, List<String> perms, boolean isAllGranted);

        /**
         */
        void onPermissionsDenied(int requestCode, List<String> perms);

    }

}
