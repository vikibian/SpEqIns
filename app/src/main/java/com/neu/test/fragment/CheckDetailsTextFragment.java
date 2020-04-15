package com.neu.test.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.neu.test.R;
import com.neu.test.activity.LoginActivity;
import com.neu.test.entity.DetectionItem1;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Task;
import com.neu.test.util.SearchUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckDetailsTextFragment extends Fragment  {
    private static String TAG = "CheckDetailsTextFragment";

    private String content;
    private TextView textView_inform;
    private TextView textView_suggestion_title;
    private DetectionItem1 mDetectionItem;
    private DetectionResult detectionResult;
    private Task task;
    private TextView textView_time;
    private TextView textView_deviceType;
    private TextView textView_qualify;
    private TextView textView_item_qualify_condition;
    private TextView textView_user;
    private TextView textView_address;
    private ExpandableTextView expandableTextView_content;
    private ExpandableTextView expandableTextView_law;

    private TextView textView_nohege_yinhuanlevel;
    private TextView textView_nohege_changway;
    private View include_nohege;

    private View include_rectify;
    private TextView textView_rectify_changway;
    private TextView textView_rectify_changaction;
    private TextView textView_rectify_changefinishtime;
    private TextView textView_rectify_changresult;

    private ImageButton law_collapse;
    private TextView law_title;
    private TextView law_content;
    private boolean law_isExpand =false;

    private ImageButton checkcontent_collapse;
    private TextView checkcontent_title;
    private TextView checkcontent_content;
    private boolean checkcontent_isExpand =false;

    private SearchUtil searchUtil = new SearchUtil();


    public CheckDetailsTextFragment() {
        // Required empty public constructor
    }

    public CheckDetailsTextFragment(DetectionItem1 detectionItem) {
        this.mDetectionItem = detectionItem;
    }

    public CheckDetailsTextFragment(DetectionResult detectionResult, Task task) {
        this.detectionResult = detectionResult;
        this.task = task;
        Log.e(TAG,"检验task："+task.toString());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_details_text, container, false);

        initFragment(view);

        return view;
    }

    private void initFragment(View view) {
        textView_suggestion_title = view.findViewById(R.id.check_detail_suggestion_title);
        textView_inform = view.findViewById(R.id.check_detail_suggestion);
        textView_inform.setText(detectionResult.getSUGGESTION());
        Log.e(TAG," 法律法规："+detectionResult.getCHECKCONTENT());
        Log.e(TAG," 法律法规："+detectionResult.getLAW());


        textView_time = view.findViewById(R.id.check_detail_time);
        textView_time.setText(task.getCHECKDATE());

        textView_deviceType = view.findViewById(R.id.check_detail_deviceType);
        Log.e(TAG," task.getDevclass: "+task.getDEVCLASS());
        Log.e(TAG," searchUtil.getTypeToDevclass: "+searchUtil.getDevclassToType(task.getDEVCLASS().toString()));
        Log.e(TAG," searchUtil: "+new Gson().toJson(searchUtil.typeToDevclass));
        textView_deviceType.setText(searchUtil.getDevclassToType(task.getDEVCLASS().toString()));

        textView_qualify = view.findViewById(R.id.check_detail_qualify);

//        textView_qualify.setText(task.getRESULT());
        textView_qualify.setText(searchUtil.getHelpMapForResult().get(task.getRESULT()));

//        textView_checked = view.findViewById(R.id.check_detail_checked);
        //textView_checked.setText(resultBean.getIschecked());

        textView_user = view.findViewById(R.id.check_detail_user);
        textView_user.setText(LoginActivity.user.getUSERNAME());

        textView_address = view.findViewById(R.id.check_detail_address);
        textView_address.setText(task.getPLACE());

        textView_item_qualify_condition = view.findViewById(R.id.check_detail_item_qualify);
        textView_item_qualify_condition.setText(searchUtil.getHelpMapForResult().get(detectionResult.getSTATUS().trim()));

        law_title = view.findViewById(R.id.check_detail_law).findViewById(R.id.check_detail_myexpandview_title);
        law_content = view.findViewById(R.id.check_detail_law).findViewById(R.id.check_detail_myexpandview_content);
        law_collapse = view.findViewById(R.id.check_detail_law).findViewById(R.id.check_detail_myexpandview_imagebutton);
        law_title.setText("法律法规：");
        law_content.setText(detectionResult.getLAW());
        law_collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!law_isExpand){
                    law_collapse.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_grey_600_24dp));
                    law_content.setVisibility(View.VISIBLE);
                    law_isExpand = true;
                }else if (law_isExpand){
                    law_collapse.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_grey_600_24dp));
                    law_content.setVisibility(View.GONE);
                    law_isExpand = false;
                }
            }
        });

        checkcontent_title = view.findViewById(R.id.check_detail_checkcontent).findViewById(R.id.check_detail_myexpandview_title);
        checkcontent_content = view.findViewById(R.id.check_detail_checkcontent).findViewById(R.id.check_detail_myexpandview_content);
        checkcontent_collapse = view.findViewById(R.id.check_detail_checkcontent).findViewById(R.id.check_detail_myexpandview_imagebutton);
        checkcontent_title.setText("检查内容：");
        checkcontent_content.setText(detectionResult.getCHECKCONTENT());
        checkcontent_collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkcontent_isExpand){
                    checkcontent_collapse.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_grey_600_24dp));
                    checkcontent_content.setVisibility(View.VISIBLE);
                    checkcontent_isExpand = true;
                }else if (checkcontent_isExpand){
                    checkcontent_collapse.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_grey_600_24dp));
                    checkcontent_content.setVisibility(View.GONE);
                    checkcontent_isExpand = false;
                }
            }
        });



        Log.e(TAG," status: "+detectionResult.getSTATUS());
        include_nohege = view.findViewById(R.id.check_detail_nohege_include);
        textView_nohege_yinhuanlevel = view.findViewById(R.id.check_detail_nohege_include)
                .findViewById(R.id.check_detail_nohege_yinhuanlevel);
        textView_nohege_changway = view.findViewById(R.id.check_detail_nohege_include)
                .findViewById(R.id.check_detail_nohege_changway);
        if (detectionResult.getSTATUS().equals(searchUtil.nohege)){
            textView_suggestion_title.setText("不合格情况描述：");
            include_nohege.setVisibility(View.VISIBLE);
            textView_nohege_yinhuanlevel.setText(detectionResult.getYINHUANLEVEL());
            textView_nohege_changway.setText(detectionResult.getCHANGEDWAY());
        }

        include_rectify = view.findViewById(R.id.check_detail_rectify_include);
        textView_rectify_changway = view.findViewById(R.id.check_detail_rectify_include)
                .findViewById(R.id.check_detail_rectify_changway);
        textView_rectify_changaction = view.findViewById(R.id.check_detail_rectify_include)
                .findViewById(R.id.check_detail_rectify_changaction);
        textView_rectify_changefinishtime = view.findViewById(R.id.check_detail_rectify_include)
                .findViewById(R.id.check_detail_rectify_changfinishtime);
        textView_rectify_changresult = view.findViewById(R.id.check_detail_rectify_include)
                .findViewById(R.id.check_detail_rectify_changresult);
        if (detectionResult.getSTATUS().equals(searchUtil.recifyQualify)){
            include_rectify.setVisibility(View.VISIBLE);
            textView_suggestion_title.setText("整改情况描述：");
            textView_rectify_changway.setText(detectionResult.getCHANGEDWAY());
            textView_rectify_changaction.setText(detectionResult.getCHANGEDACTION());
            textView_rectify_changefinishtime.setText(detectionResult.getCHANGEDFINISHTIME());
            textView_rectify_changresult.setText(detectionResult.getCHANGEDRESULT());
        }




    }


}
