package com.neu.test.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.activity.CheckDetailsActivity;
import com.neu.test.activity.DetctionActivity;
import com.neu.test.activity.FragmentManagerActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.adapter.DropDownMenuAdapter;
import com.neu.test.adapter.ListViewAdapter1;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.DetectionItem1;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListDetectionResultCallBack;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.ListContent;
import com.neu.test.util.ResultBean;
import com.neu.test.util.SearchUtil;
import com.yyydjk.library.DropDownMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class ShowSearchedResultFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "ShowSearchedResult";
    private TextView textView_time;
    private TextView textView_deviceType;
    private TextView textView_qualify;
    private TextView textView_checked;
    private TextView textView_user;
    private TextView textView_address;
    private TextView textView_title;
    private Button bt_goback;

    private ListView listView;
    private ListViewAdapter1 listViewAdapter1;

    private CircleImageView circleImageView;

    private DropDownMenu dropDownMenu;
    private String headers[] = {"选择过滤项"};

    private DropDownMenuAdapter dropDownMenuAdapter;
    private List<View> popupViews = new ArrayList<>();
    private int posFlag=0;
    private List<DetectionItem1> listDatas;

    private List<Task> tasks;
    private Task task;
    private List<DetectionResult> listResult;
    private String taskID;
    private String devID;
    private SearchUtil searchUtil = new SearchUtil();

//    private List<DetectionItem1> listDatas_qualified = new ArrayList<>();
//    private List<DetectionItem1> listDatas_unqualified = new ArrayList<>();
    private List<DetectionResult> listDatas_qualified = new ArrayList<>();
    private List<DetectionResult> listDatas_unqualified = new ArrayList<>();
    private List<DetectionResult> listDatas_undecied = new ArrayList<>();


    public ShowSearchedResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_searched_result, container, false);
        task = (Task) getArguments().getSerializable("tasks");
        Log.e(TAG,"获取数据");
        Log.e(TAG,"获取数据"+task.toString());
        taskID = task.getTASKID();
        devID = task.getDEVID();

        listDatas = new ArrayList<DetectionItem1>();
        listDatas.add(new DetectionItem1("现场人员是否具有有效证件。","不合格"));
        listDatas.add(new DetectionItem1("是否有使用登记标志，并按规定固定在电梯的显著位置，是否在下次检验期限内。","合格"));
        listDatas.add(new DetectionItem1("安全注意事项和警示标志是否置于易于为乘客注意的显著位置。","合格"));
        listDatas.add(new DetectionItem1("电梯内设置的报警装置是否可靠，联系是否畅通。","不合格"));
        listDatas.add(new DetectionItem1("抽查呼层、楼层等显示信号系统功能是否有效，指示是否正确。","合格"));
        listDatas.add(new DetectionItem1("门防夹保护装置是否有效。","合格"));
        listDatas.add(new DetectionItem1("自动扶梯和自动人行道入口处急停开关是否有效。","合格"));
        listDatas.add(new DetectionItem1("限速器校验报告是否在有效期内。","合格"));
        listDatas.add(new DetectionItem1("是否有有效的维保合同，维保资质及人员资质是否满足要求。","合格"));
        listDatas.add(new DetectionItem1("是否有维保记录，并经安全管理人签字确认，维保周期是否符合规定。","合格"));

        initView(view);
        getTaskList();
        initContent();
//        initDropDownMenu();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: i "+ i);
                Log.d(TAG, "onItemClick: l "+ l);

                filiterResult();

                Toast.makeText(getContext(),"i "+i+" l "+l,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CheckDetailsActivity.class);
//                DetectionItem detectionItem = new DetectionItem(" "," ");
//                DetectionItem1 detectionItem = new DetectionItem1();
                DetectionResult detectionResult = new DetectionResult();
                if (posFlag == 1){
                    detectionResult = listDatas_qualified.get(i);
                }else if (posFlag == 2){
                    detectionResult = listDatas_unqualified.get(i);
                }else if(posFlag == 0){
                    detectionResult = listResult.get(i);
                }else if (posFlag == 3){
                    detectionResult = listDatas_undecied.get(i);
                }

                intent.putExtra("detectionResult",detectionResult);
                intent.putExtra("task",task);

                startActivity(intent);

            }
        });

        return view;
    }

    private void getTaskList() {
        String url;
        url = BaseUrl.BaseUrl+"selectItemResultServlet";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> map = new HashMap<>();
        map.put("taskID","123456785");//1affb4ca-1b34-4d99-9222-5ce1ed62afa5   taskID
//        map.put("taskID",taskID);//1affb4ca-1b34-4d99-9222-5ce1ed62afa5   taskID
        Log.e(TAG,"map: "+ map.toString());
        map.put("DEVID","五号电梯");//123456  devID
//        map.put("DEVID",devID);//123456  devID
        Log.e(TAG,"map: "+ map.toString());



        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListDetectionResultCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.toString());
                Log.e(TAG," 数据错误");
            }

            @Override
            public void onResponse(Result<List<DetectionResult>> reponse, int i) {
                if (reponse.getMessage().equals("获取成功")){
                    listResult  = reponse.getContent();
                    if (reponse.getMessage().length() == 0){
                        Log.e(TAG,"没有post数据");
                    }else {
                        //初始化listview应该在获取数据之后
                        if (listResult.size()!=0){
                            Log.e(TAG,"返回list的大小："+listResult.size());
                            initListViewAdapter1();
                            initDropDownMenu();
                        }
                    }
                }
            }
        });
    }

    private void filiterResult() {
        for (int i=0;i<listResult.size();i++){
            if (listResult.get(i).getSTATUS().equals(searchUtil.hegeText)
            ||listResult.get(i).getSTATUS().equals(searchUtil.hege)){
                listDatas_qualified.add(listResult.get(i));
            }
            if (listResult.get(i).getSTATUS().equals(searchUtil.nohegeText)
            ||listResult.get(i).getSTATUS().equals(searchUtil.nohege)){
                listDatas_unqualified.add(listResult.get(i));
            }
            if (listResult.get(i).getSTATUS().equals(searchUtil.undecidedText)
            ||listResult.get(i).getSTATUS().equals(searchUtil.undecided)){
                listDatas_undecied.add(listResult.get(i));
            }
        }
    }

    private void initDropDownMenu() {
        final ListView listView_dropdown = new ListView(getContext());
        dropDownMenuAdapter = new DropDownMenuAdapter(getContext(), Arrays.asList(searchUtil.choose));
        listView_dropdown.setDividerHeight(0);
        listView_dropdown.setAdapter(dropDownMenuAdapter);

        popupViews.add(listView_dropdown);


        listView_dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropDownMenuAdapter.setCheckItem(position);
                dropDownMenu.setTabText(searchUtil.choose[position]);
                Log.e(TAG," text: "+searchUtil.choose[position]);
                Log.e(TAG," text position: "+position);
                dropDownMenu.closeMenu();
                posFlag = position;


//                List<DetectionItem1> listQualifed = new ArrayList<>();
//                List<DetectionItem1> listunQualifed = new ArrayList<>();
                List<DetectionResult> listQualifed = new ArrayList<>();
                List<DetectionResult> listunQualifed = new ArrayList<>();
                List<DetectionResult> listundecied = new ArrayList<>();
                for(int i=0;i<listResult.size();i++){
                    if(listResult.get(i).getSTATUS().equals(searchUtil.hegeText)
                    ||listResult.get(i).getSTATUS().equals(searchUtil.hege)){
                        listQualifed.add(listResult.get(i));
                    }
                    else if(listResult.get(i).getSTATUS().equals(searchUtil.nohegeText)
                    ||listResult.get(i).getSTATUS().equals(searchUtil.nohege)){
                        listunQualifed.add(listResult.get(i));
                    }else if (listResult.get(i).getSTATUS().equals(searchUtil.undecidedText)
                    ||listResult.get(i).getSTATUS().equals(searchUtil.undecided)){
                        listundecied.add(listResult.get(i));
                    }
                }
                if(position==0){
                    refresh(listResult);
                }else if (position==1){
                    refresh(listQualifed);
                }else if (position==2){
                    refresh(listunQualifed);
                }else if (position == 3){
                    refresh(listundecied);
                }
            }
        });

        initDropDownMenuListView(listResult);
    }


    private void initContent() {
        if (task!=null){
            //时间
            textView_time.setText(task.getCHECKDATE());
            //增加下划线
            textView_time.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            Log.e(TAG,"接收数据显示："+task.getCHECKDATE());
            Log.e(TAG,"接收数据显示："+task.toString());

            //设备种类
            textView_deviceType.setText(searchUtil.getDevclassToType(task.getDEVCLASS()));
            textView_deviceType.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            //合格情况
            textView_qualify.setText(task.getRESULT());
            Log.e(TAG," initContent: "+task.getRESULT());
            textView_qualify.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            //是否已检查
//            textView_checked.setText(mResultBean.getIschecked());
//            textView_checked.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //检察人员
            textView_user.setText(task.getLOGINNAME());
            textView_user.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            //地址
            textView_address.setText(task.getPLACE());
            textView_address.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            textView_title.setText(task.getDEVID()+"检查情况");
        }

    }

    private void refresh(List<DetectionResult> listRes) {
        listViewAdapter1 = new ListViewAdapter1(getContext(),listRes,searchUtil.choose[posFlag]);
        listView.setAdapter(listViewAdapter1);
    }

    //对ListViewAdapter1进行适配
    private void initListViewAdapter1() {
//        DetctionActivity detctionActivity = new DetctionActivity();
//        detctionActivity.getData();
        //listViewAdapter1 = new ListViewAdapter1(getContext(),listDatas,choose[posFlag]);
        listViewAdapter1 = new ListViewAdapter1(getContext(),listResult,searchUtil.choose[posFlag]);
        listView.setAdapter(listViewAdapter1);

    }

    private void initDropDownMenuListView(List<DetectionResult> listResult) {
        ListView listView_1 = new ListView(getContext());
//        DetctionActivity detctionActivity = new DetctionActivity();
//        detctionActivity.getData();
//        ListViewAdapter1 listViewAdapter2 = new ListViewAdapter1(getContext(),listResult,searchUtil.choose[posFlag]);
//        listView_1.setAdapter(listViewAdapter2);
        dropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,listView_1);
    }

    private void initView(View view) {
        textView_title = view.findViewById(R.id.searched_result_title);
        textView_time = view.findViewById(R.id.searched_result_time);
        textView_deviceType = view.findViewById(R.id.searched_result_deviceType);
        textView_qualify = view.findViewById(R.id.searched_result_qualify);
//        textView_checked = view.findViewById(R.id.searched_result_checked);
        textView_user = view.findViewById(R.id.searched_result_user);
        textView_address = view.findViewById(R.id.searched_result_address);

        listView = view.findViewById(R.id.show_searched_result_listview);

        dropDownMenu = view.findViewById(R.id.dropdownmenu);

//        bt_goback.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.bt_goback:
//                getFragmentManager().popBackStack();
//                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG," onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG," onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG," onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG," onResume");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG," onpause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG," onstop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG," onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG," onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG," onDetach");
    }
}
