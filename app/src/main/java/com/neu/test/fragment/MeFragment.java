package com.neu.test.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.allen.library.SuperTextView;
import com.neu.test.R;
import com.neu.test.activity.LawLearningActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.activity.MeAboutActivity;
import com.neu.test.activity.MeAccountAndSafeActivity;
import com.neu.test.activity.MeFeedbackActivity;
import com.neu.test.activity.MeInformActivity;
import com.neu.test.util.BaseUrl;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.io.File;

import es.dmoral.toasty.Toasty;

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

    int me;


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        init(view);
        initGroupListview(view);
        return view;
    }

    private void initGroupListview(View view) {

        int height = QMUIResHelper.getAttrDimen(getContext(), com.qmuiteam.qmui.R.attr.qmui_list_item_height);


        QMUICommonListItemView Item0 = groupListView.createItemView("个人信息");
        Item0.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item0.setTag(0);


//        QMUICommonListItemView itemWithChevron = mGroupListView.createItemView("Item 4");
//        itemWithChevron.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        QMUICommonListItemView Item1 = groupListView.createItemView("账号与安全");
        Item1.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item1.setTag(1);

        QMUICommonListItemView Item2 = groupListView.createItemView("法律法规学习");
        Item2.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item2.setTag(2);

        QMUICommonListItemView Item3 = groupListView.createItemView("意见反馈");
        Item3.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item3.setTag(3);

        QMUICommonListItemView Item4 = groupListView.createItemView("关于软件");
        Item4.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item4.setTag(4);

        QMUICommonListItemView Item5 = groupListView.createItemView("清空缓存");
        Item5.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item5.setTag(5);


        int size = QMUIDisplayHelper.dp2px(view.getContext(), 20);
        QMUIGroupListView.newSection(getContext())
                .setUseDefaultTitleIfNone(false) //默认标题内容
                .setUseTitleViewForSectionSpace(false) //取消标题显示
//                .setTitle("Section 2: 自定义右侧 View/红点/new 提示")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(Item0, this)
                .addItemView(Item1, this)
                .addItemView(Item2, this)
                .addItemView(Item3, this)
                .addItemView(Item4, this)
                .addItemView(Item5, this)
                //   .setOnlyShowStartEndSeparator(true)
                .addTo(groupListView);

    }

    private void init(View view) {
        groupListView = view.findViewById(R.id.me_fragment_ListView);
        nametext = view.findViewById(R.id.me_fragment_nametext);

        nametext.setText(LoginActivity.inputName);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        if(v instanceof QMUICommonListItemView){
            switch ((int)v.getTag()){
                case 0:
                    Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
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
                    String cachePath = BaseUrl.absolutePath+"/DCIM/"+ LoginActivity.inputName;
                    File cacheFile = new File(cachePath);
                    clearAppCache(cacheFile);
                    break;
            }
        }
    }

    private void clearAppCache(File cacheFile) {

        if (!cacheFile.exists()){
            Toasty.info(getContext(),"您没有缓存！",Toasty.LENGTH_SHORT).show();
        }else {
            for (File file : cacheFile.listFiles()) {
                if (file.isFile())
                    file.delete(); // 删除所有文件
                else if (file.isDirectory())
                    clearAppCache(file); // 递规的方式删除文件夹
            }
            cacheFile.delete();// 删除目录本身
            Toasty.info(getContext(),"缓存已清空！",Toasty.LENGTH_SHORT).show();
        }

    }
}
