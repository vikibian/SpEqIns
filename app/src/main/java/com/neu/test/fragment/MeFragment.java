package com.neu.test.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.allen.library.SuperTextView;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.neu.test.R;
import com.neu.test.activity.ChangeFontActivity;
import com.neu.test.activity.DraftTaskActivity;
import com.neu.test.activity.FragmentManagerActivity;
import com.neu.test.activity.LawLearningActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.activity.MeAboutActivity;
import com.neu.test.activity.MeAccountAndSafeActivity;
import com.neu.test.activity.MeFeedbackActivity;
import com.neu.test.activity.MeInformActivity;
import com.neu.test.layout.SimpleToolbar;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.CacheUtil;
import com.neu.test.util.ClickUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.io.File;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MeFragment";
    public static final int REQUEST_CODE_MEINFORM = 0;
    public static final int REQUEST_CODE_ACCOUNTANDSAFE = 1;
    public static final int REQUEST_CODE_FEEDBACK = 2;
    public static final int REQUEST_CODE_ABOUT = 3;
    public static final int REQUEST_CODE_LAW= 4;
    private TextView nametext;
    private QMUIGroupListView groupListView ;
    private QMUIGroupListView groupListView1 ;
    private QMUIGroupListView groupListView2 ;
    private QMUIGroupListView groupListView3 ;
    PromptDialog promptDialog;

    int me;


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        SimpleToolbar simple_toolbar = activity.findViewById(R.id.simple_toolbar);
        simple_toolbar.setVisibility(View.GONE);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        promptDialog = new PromptDialog(getActivity());
        init(view);
        initGroupListview(view);
        return view;
    }

    private void initGroupListview(View view)  {

        int height = QMUIResHelper.getAttrDimen(getContext(), com.qmuiteam.qmui.R.attr.qmui_list_item_height);
        int size = QMUIDisplayHelper.dp2px(view.getContext(), 20);

        QMUICommonListItemView Item0 = groupListView.createItemView("个人信息");
        Item0.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item0.setTag(0);


        QMUICommonListItemView Item1 = groupListView.createItemView("账号与安全");
        Item1.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item1.setTag(1);


        QMUICommonListItemView Item2 = groupListView1.createItemView("法律法规学习");
        Item2.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item2.setTag(2);

        QMUICommonListItemView Item3 = groupListView2.createItemView("意见反馈");
        Item3.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item3.setTag(3);

        QMUICommonListItemView Item4 = groupListView2.createItemView("关于软件");
        Item4.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item4.setTag(4);

//        QMUICommonListItemView Item5 = groupListView.createItemView("清空缓存");
//        Item5.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
//        Item5.setTag(5);

        String cachesize = "";
        cachesize = getCacheSize();

        QMUICommonListItemView Item5 = groupListView3.createItemView(
                null,
                "清空缓存",
                cachesize,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item5.setTag(5);

        QMUICommonListItemView Item6 = groupListView3.createItemView("字体大小");
        Item6.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item6.setTag(6);

        QMUICommonListItemView Item7 = groupListView3.createItemView("退出");
        Item7.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CUSTOM);
        Item7.setTag(7);

        QMUICommonListItemView Item8 = groupListView1.createItemView("任务起草");
        Item8.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item8.setTag(8);


        QMUIGroupListView.newSection(getContext())
                .setUseDefaultTitleIfNone(false) //默认标题内容
                .setUseTitleViewForSectionSpace(false) //取消标题显示
//                .setTitle("Section 2: 自定义右侧 View/红点/new 提示")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(Item0, this)
                .addItemView(Item1, this)
//                .addItemView(Item2, this)
//                .addItemView(Item8, this)
//                .addItemView(Item3, this)
//                .addItemView(Item4, this)
//                .addItemView(Item5, this)
//                .addItemView(Item6, this)
//                .addItemView(Item7, this)
                //   .setOnlyShowStartEndSeparator(true)
                .addTo(groupListView);

        QMUIGroupListView.newSection(getContext())
                .setUseDefaultTitleIfNone(false) //默认标题内容
                .setUseTitleViewForSectionSpace(false) //取消标题显示

                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(Item2, this)
                .addItemView(Item8, this)
                .addTo(groupListView1);

        QMUIGroupListView.newSection(getContext())
                .setUseDefaultTitleIfNone(false) //默认标题内容
                .setUseTitleViewForSectionSpace(false) //取消标题显示
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(Item3, this)
                .addItemView(Item4, this)
                .addTo(groupListView2);

        QMUIGroupListView.newSection(getContext())
                .setUseDefaultTitleIfNone(false) //默认标题内容
                .setUseTitleViewForSectionSpace(false) //取消标题显示
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(Item5, this)
                .addItemView(Item6, this)
                .addItemView(Item7, this)
                .addTo(groupListView3);

    }

    private void init(View view) {
        groupListView = view.findViewById(R.id.me_fragment_ListView);
        groupListView1 = view.findViewById(R.id.me_fragment_ListView1);
        groupListView2 = view.findViewById(R.id.me_fragment_ListView2);
        groupListView3 = view.findViewById(R.id.me_fragment_ListView3);

        nametext = view.findViewById(R.id.me_fragment_nametext);

        nametext.setText(LoginActivity.user.getUSERNAME());
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        if(v instanceof QMUICommonListItemView){
            if(ClickUtil.isFastClick()){
                Log.e("ttttt",true+"fast");
                return;
            }else{
                Log.e("ttttt",false+"notfast");
            }
            switch ((int)v.getTag()){
                case 0:
                    intent = new Intent(getActivity(), MeInformActivity.class);
                    getActivity().startActivityForResult(intent,REQUEST_CODE_MEINFORM);
                    //startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(getActivity(), MeAccountAndSafeActivity.class);
                    getActivity().startActivityForResult(intent,REQUEST_CODE_ACCOUNTANDSAFE);
                    break;
                case 2:
                    intent = new Intent(getActivity(), LawLearningActivity.class);
                    getActivity().startActivityForResult(intent,REQUEST_CODE_LAW);
                    break;
                case 3:
                    intent = new Intent(getActivity(), MeFeedbackActivity.class);
                    getActivity().startActivityForResult(intent,REQUEST_CODE_FEEDBACK);
                    break;
                case 4:
                    intent = new Intent(getActivity(), MeAboutActivity.class);
                    getActivity().startActivityForResult(intent,REQUEST_CODE_ABOUT);
                    break;
                case 5:
                    MessageDialog.build((AppCompatActivity) getContext())
                            .setStyle(DialogSettings.STYLE.STYLE_IOS)
                            .setTitle("提示")
                            .setMessage("该操作会清空该软件的存储文件！")
                            .setOkButton("确定", new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    showMultiChoiceDialog();
                                    return false;
                                }
                            })
                            .setCancelButton("取消", new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {

                                    return false;
                                }
                            })
                            .show();


                    break;
                case 6:
                    intent = new Intent(getActivity(), ChangeFontActivity.class);
                    startActivity(intent);
                    break;
                case 7:
                    MessageDialog.build((AppCompatActivity) getActivity())
                            .setStyle(DialogSettings.STYLE.STYLE_IOS)
                            .setTitle("提示")
                            .setMessage("你确定要退出当前登录的用户吗？")
                            .setOkButton("确定", new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isAuto",false);
                                    editor.commit();
                                    Intent intent;
                                    intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
                                    return false;
                                }
                            })
                            .setCancelButton("取消", new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    return false;
                                }
                            })
                            .show();
                    break;
                case 8:
                    intent = new Intent(getActivity(), DraftTaskActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    private void showMultiChoiceDialog(){
        String clearpath = BaseUrl.absolutePath+"/DCIM/"+ LoginActivity.inputName;
        final String[] items = new String[]{"清理图片缓存", "清理视频缓存", "清理PDF文件缓存"};
        QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(getActivity())
                .setCheckedItems(new int[]{0,1,2})
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.addAction("取消", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                dialog.dismiss();
            }
        });
        builder.addAction("确定", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                promptDialog.showLoading("正在清理缓存...");
                for (int i = 0; i < builder.getCheckedItemIndexes().length; i++) {
                    if (builder.getCheckedItemIndexes()[i] == 0){
                        String cachePath = clearpath+"/Photo";
                        File cacheFile = new File(cachePath);
                        clearAppCache(cacheFile);
                    }
                    if (builder.getCheckedItemIndexes()[i] == 1){
                        String cachePath = clearpath+"/Video";
                        File cacheFile = new File(cachePath);
                        clearAppCache(cacheFile);
                    }
                    if (builder.getCheckedItemIndexes()[i] == 2){
                        String cachePath = clearpath+"/PDFDownloads";
                        File cacheFile = new File(cachePath);
                        clearAppCache(cacheFile);
                    }
                }
                promptDialog.dismiss();
                dialog.dismiss();
                TipDialog.show((AppCompatActivity) getContext(),"缓存已经清除...", TipDialog.TYPE.SUCCESS);
                ((QMUICommonListItemView)groupListView3.findViewWithTag(5)).setDetailText(getCacheSize());
            }
        });
        int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
        builder.create(mCurrentDialogStyle).show();
    }

    private void clearAppCache(File cacheFile) {

        if (!cacheFile.exists()){
            //如果不存在则空执行
//            Toasty.info(getContext(),"不存在！",Toasty.LENGTH_SHORT).show();
        }else {
            for (File file : cacheFile.listFiles()) {
                if (file.isFile())
                    file.delete(); // 删除所有文件
                else if (file.isDirectory())
                    clearAppCache(file); // 递规的方式删除文件夹
            }
            cacheFile.delete();// 删除目录本身
        }

    }

    private String getCacheSize(){
        CacheUtil cacheUtil = new CacheUtil();
        String cachePath = BaseUrl.absolutePath+"/DCIM/"+ LoginActivity.inputName;
        File cacheFile = new File(cachePath);
        long cachesize = 0;
        try {
            cachesize = cacheUtil.getFileSizes(cacheFile);
            Log.e(TAG,"size: "+cachesize);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cacheUtil.FormetFileSize(cachesize);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((QMUICommonListItemView)groupListView3.findViewWithTag(5)).setDetailText(getCacheSize());
        promptDialog.dismissImmediately();
    }
}
