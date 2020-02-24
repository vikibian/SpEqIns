package com.neu.test.fragment;

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

import androidx.fragment.app.Fragment;

import com.neu.test.R;
import com.neu.test.activity.CheckDetailsActivity;
import com.neu.test.activity.DetctionActivity;
import com.neu.test.adapter.DropDownMenuAdapter;
import com.neu.test.adapter.ListViewAdapter1;
import com.neu.test.entity.DetectionItem;
import com.neu.test.util.ListContent;
import com.neu.test.util.ResultBean;
import com.yyydjk.library.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

    private List<ListContent> resultListContent ;
    private ResultBean mResultBean ;
    private CircleImageView circleImageView;

    private DropDownMenu dropDownMenu;
    private String headers[] = {"选择过滤项"};
    public String choose[] = {"全部","合格","不合格"};
    private DropDownMenuAdapter dropDownMenuAdapter;
    private List<View> popupViews = new ArrayList<>();
    private int posFlag=0;
    private List<DetectionItem> listDatas;


    private List<DetectionItem> listDatas_qualified = new ArrayList<>();
    private List<DetectionItem> listDatas_unqualified = new ArrayList<>();



    public ShowSearchedResultFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_searched_result, container, false);
        mResultBean = (ResultBean) getArguments().getSerializable("result");

//        listDatas = new ArrayList<DetectionItem>();
//        listDatas.add(new DetectionItem("现场人员是否具有有效证件。","不合格"));
//        listDatas.add(new DetectionItem("是否有使用登记标志，并按规定固定在电梯的显著位置，是否在下次检验期限内。","合格"));
//        listDatas.add(new DetectionItem("安全注意事项和警示标志是否置于易于为乘客注意的显著位置。","合格"));
//        listDatas.add(new DetectionItem("电梯内设置的报警装置是否可靠，联系是否畅通。","不合格"));
//        listDatas.add(new DetectionItem("抽查呼层、楼层等显示信号系统功能是否有效，指示是否正确。","合格"));
//        listDatas.add(new DetectionItem("门防夹保护装置是否有效。","合格"));
//        listDatas.add(new DetectionItem("自动扶梯和自动人行道入口处急停开关是否有效。","合格"));
//        listDatas.add(new DetectionItem("限速器校验报告是否在有效期内。","合格"));
//        listDatas.add(new DetectionItem("是否有有效的维保合同，维保资质及人员资质是否满足要求。","合格"));
//        listDatas.add(new DetectionItem("是否有维保记录，并经安全管理人签字确认，维保周期是否符合规定。","合格"));

        initView(view);
        initContent();
        initDropDownMenu();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: i "+ i);
                Log.d(TAG, "onItemClick: l "+ l);

                filiterResult();

                Toast.makeText(getContext(),"i "+i+" l "+l,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CheckDetailsActivity.class);
//                DetectionItem detectionItem = new DetectionItem(" "," ");
                DetectionItem detectionItem = new DetectionItem();
                if (posFlag == 1){
                    detectionItem = listDatas_qualified.get(i);
                }else if (posFlag == 2){
                    detectionItem = listDatas_unqualified.get(i);
                }else if(posFlag == 0){
                    detectionItem = listDatas.get(i);
                }

                intent.putExtra("result",detectionItem);
                intent.putExtra("result_resultBean",mResultBean);
                startActivity(intent);

            }
        });



        return view;
    }

    private void filiterResult() {
        for (int i=0;i<listDatas.size();i++){
            if (listDatas.get(i).getResultStatus().equals("合格")){
                listDatas_qualified.add(listDatas.get(i));
            }
            if (listDatas.get(i).getResultStatus().equals("不合格")){
                listDatas_unqualified.add(listDatas.get(i));
            }
        }
    }

    private void initDropDownMenu() {
        final ListView listView_dropdown = new ListView(getContext());
        dropDownMenuAdapter = new DropDownMenuAdapter(getContext(), Arrays.asList(choose));
        listView_dropdown.setDividerHeight(0);
        listView_dropdown.setAdapter(dropDownMenuAdapter);

        popupViews.add(listView_dropdown);


        listView_dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dropDownMenuAdapter.setCheckItem(position);
                dropDownMenu.setTabText(choose[position]);
                dropDownMenu.closeMenu();
                posFlag = position;


                List<DetectionItem> listQualifed = new ArrayList<>();
                List<DetectionItem> listunQualifed = new ArrayList<>();
                for(int i=0;i<listDatas.size();i++){
                    if(listDatas.get(i).getResultStatus().equals("合格")){
                        listQualifed.add(listDatas.get(i));
                    }
                    else if(listDatas.get(i).getResultStatus().equals("不合格")){
                        listunQualifed.add(listDatas.get(i));
                    }
                }
                if(position==0){
                    refresh(listDatas);
                }else if (position==1){
                    refresh(listQualifed);
                }else if (position==2){
                    refresh(listunQualifed);
                }
            }
        });

        initDropDownMenuListView(listDatas);
    }

    private void refresh(List<DetectionItem> listDatas) {
        listViewAdapter1 = new ListViewAdapter1(getContext(),listDatas,choose[posFlag]);
        listView.setAdapter(listViewAdapter1);
    }

    private void initContent() {
        if (mResultBean!=null){
            int i=0;
            //时间
            textView_time.setText(mResultBean.getTime());
            //增加下划线
            textView_time.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //设备种类
            textView_deviceType.setText(mResultBean.getDeviceType());
            textView_deviceType.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //合格情况
            textView_qualify.setText(mResultBean.getQualify());
            textView_qualify.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //是否已检查
            textView_checked.setText(mResultBean.getIschecked());
            textView_checked.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //检察人员
            textView_user.setText(mResultBean.getUser());
            textView_user.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //地址

            textView_address.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            textView_title.setText(mResultBean.getDeviceType()+"检查情况");
        }

        initListViewAdapter1();

    }

    //对ListViewAdapter1进行适配
    private void initListViewAdapter1() {
        DetctionActivity detctionActivity = new DetctionActivity();
        detctionActivity.getData();
        listViewAdapter1 = new ListViewAdapter1(getContext(),listDatas,choose[posFlag]);
        listView.setAdapter(listViewAdapter1);

    }

    private void initDropDownMenuListView(List<DetectionItem> listData) {
        ListView listView_1 = new ListView(getContext());
        DetctionActivity detctionActivity = new DetctionActivity();
        detctionActivity.getData();
//        ListViewAdapter1 listViewAdapter2 = new ListViewAdapter1(getContext(),listData,choose[posFlag]);
//        listView_1.setAdapter(listViewAdapter2);
        dropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,listView_1);
    }

    private void initView(View view) {
        textView_title = view.findViewById(R.id.searched_result_title);
        textView_time = view.findViewById(R.id.searched_result_time);
        textView_deviceType = view.findViewById(R.id.searched_result_deviceType);
        textView_qualify = view.findViewById(R.id.searched_result_qualify);
        textView_checked = view.findViewById(R.id.searched_result_checked);
        textView_user = view.findViewById(R.id.searched_result_user);
        textView_address = view.findViewById(R.id.searched_result_address);
//        resultToolbarTextview = view.findViewById(R.id.reslut_toolbar_textview);
//        bt_goback = view.findViewById(R.id.bt_goback);
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
}
