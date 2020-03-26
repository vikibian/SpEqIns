package com.neu.test.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private SuperTextView STVinform;
    private SuperTextView STVaccount;
    private SuperTextView STVfeedback;
    private SuperTextView STVabout;
    private SuperTextView STVlaw;
    private SuperTextView STVclearcache;

    int me;


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        init(view);

        return view;
    }

    private void init(View view) {
        STVinform = view.findViewById(R.id.STV_inform);
        STVaccount = view.findViewById(R.id.STV_account);
        STVfeedback = view.findViewById(R.id.STV_feedback);
        STVabout = view.findViewById(R.id.STV_about);
        STVlaw = view.findViewById(R.id.STV_law);
        STVclearcache = view.findViewById(R.id.STV_clearCache);


        STVinform.setOnClickListener(this);
        STVaccount.setOnClickListener(this);
        STVfeedback.setOnClickListener(this);
        STVabout.setOnClickListener(this);
        STVlaw.setOnClickListener(this);
        STVclearcache.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.STV_inform:
                intent = new Intent(getActivity(), MeInformActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE_MEINFORM);
                //startActivity(intent);
                break;
            case R.id.STV_account:
                intent = new Intent(getActivity(), MeAccountAndSafeActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE_ACCOUNTANDSAFE);
                break;
            case R.id.STV_law:
                intent = new Intent(getActivity(), LawLearningActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE_LAW);
                break;
            case R.id.STV_feedback:
                intent = new Intent(getActivity(), MeFeedbackActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE_FEEDBACK);
                break;
            case R.id.STV_about:
                intent = new Intent(getActivity(), MeAboutActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE_ABOUT);
                break;
            case R.id.STV_clearCache:
                String cachePath = BaseUrl.absolutePath+"/DCIM/"+ LoginActivity.inputName;
                File cacheFile = new File(cachePath);
                clearAppCache(cacheFile);
                break;
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
