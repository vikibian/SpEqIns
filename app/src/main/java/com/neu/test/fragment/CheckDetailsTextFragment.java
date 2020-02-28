package com.neu.test.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neu.test.R;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.DetectionItem1;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Task;
import com.neu.test.util.ResultBean;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckDetailsTextFragment extends Fragment {

    private String content;
    private TextView textView_inform;
    private DetectionItem1 mDetectionItem;
    private DetectionResult detectionResult;
    private Task task;
    private TextView textView_time;
    private TextView textView_deviceType;
    private TextView textView_qualify;
    private TextView textView_checked;
    private TextView textView_user;
    private TextView textView_address;


    public CheckDetailsTextFragment() {
        // Required empty public constructor
    }

    public CheckDetailsTextFragment(DetectionItem1 detectionItem) {
        this.mDetectionItem = detectionItem;
    }

    public CheckDetailsTextFragment(DetectionResult detectionResult, Task task) {
        this.detectionResult = detectionResult;
        this.task = task;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_details_text, container, false);

        initFragment(view);

        return view;
    }

    private void initFragment(View view) {
        textView_inform = view.findViewById(R.id.check_detail_text_detailInform);
        textView_inform.setText(detectionResult.getCHECKCONTENT());
        //textView_inform.setText(mDetectionItem.getCHECKCONTENT());


        textView_time = view.findViewById(R.id.check_detail_time);
        textView_time.setText(task.getCHECKDATE());

        textView_deviceType = view.findViewById(R.id.check_detail_deviceType);
        textView_deviceType.setText(task.getDEVCLASS());

        textView_qualify = view.findViewById(R.id.check_detail_qualify);
        textView_qualify.setText(task.getRESULT());

//        textView_checked = view.findViewById(R.id.check_detail_checked);
        //textView_checked.setText(resultBean.getIschecked());

        textView_user = view.findViewById(R.id.check_detail_user);
        textView_user.setText(task.getLOGINNAME());

        textView_address = view.findViewById(R.id.check_detail_address);
        textView_address.setText(task.getPLACE());


    }

}
