package com.neu.test.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.activity.DetctionActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.Device;
import com.neu.test.entity.JianChaItem;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.BottomBarLayout;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListItemCallBack;
import com.neu.test.net.callback.ListItemsCallBack;
import com.neu.test.util.BaseUrl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class CheckFragment extends Fragment {

    private static String TAG = "CheckFragment";

    private ListView lv_check;
    CheckAdapter checkAdapter;
    List<Task> tasks;
    BottomBarLayout mBottomBarLayout;
    private String taskType;

    public List<DetectionItem> testItem;

    public CheckFragment(){

    }

    public CheckFragment(List<Task> tasks, BottomBarLayout bottomBarLayout){
        this.tasks = tasks;
        mBottomBarLayout = bottomBarLayout;
    }

    /**
     * @date : 2020/2/22
     * @time : 10:05
     * @author : viki
     * @description : 传入任务类型的参数
     */

    public CheckFragment(List<Task> tasks, BottomBarLayout bottomBarLayout, String s){
        this.tasks = tasks;
        mBottomBarLayout = bottomBarLayout;
        taskType = s;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, null);
        lv_check = view.findViewById(R.id.lv_check);
        //准备BaseAdapter
        checkAdapter = new CheckAdapter();
        //设置Adapter显示列表
        lv_check.setAdapter(checkAdapter);
        lv_check.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task test = tasks.get(position);
                Log.e("CheckFargment"," DEVID  "+tasks.get(position).getDEVID());
                Log.e("CheckFargment"," TASKID  "+tasks.get(position).getTASKID());
                Log.e("CheckFargment"," DEVCLASS  "+tasks.get(position).getDEVCLASS());
                String s = test.getUSEUNITNAME();
                String DEVCLASS = test.getDEVCLASS();
                int taskPosition = position;
                getDetctionData(DEVCLASS,s,test);

//                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), DetctionActivity.class);
//                intent.putExtra("TITLE",s);
//                intent.putExtra("position",taskPosition);
//                intent.putExtra("taskType",taskType);
//                startActivity(intent);

            }
        });

//
//        lv_check.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.setHeaderTitle("确定删除该检查项吗");
//                menu.add(0, 0, 0, "删除此项");
//                menu.add(0, 1, 0, "取消删除");
//            }
//        });


        return view;
    }

    private void getDetctionData(String devclass, final String title, final Task task) {
        Map<String, String> map = new HashMap<>();
        map.put("DEVCLASS",devclass);
        map.put("UNITNAME",task.getUSEUNITNAME());
        map.put("TASKTYPE",task.getTASKTYPE());
        String url = BaseUrl.BaseUrl+"getCheckItems";
        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListItemsCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Toasty.warning(getActivity(),"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<JianChaItem>> response, int id) {
                if(response.getMessage().equals("获取检查项成功")) {
                    List<JianChaItem> items = response.getContent();
                    Intent intent = new Intent(getActivity(), DetctionActivity.class);
                    intent.putExtra("items", (Serializable) items);
                    intent.putExtra("userName", task.getLOGINNAME());
                    intent.putExtra("task", task);
                    intent.putExtra("tasktype",taskType);

                    Log.e(TAG," "+task.getTASKID());
                    intent.putExtra("TITLE", title);
                    startActivity(intent);
                }
            }
        });
    }


    class CheckAdapter extends BaseAdapter{


        //返回集合数据数量
        @Override
        public int getCount() {
            return tasks.size();
        }

        //返回指定下标的数据对象
        @Override
        public Object getItem(int position) {
            return tasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    //        "UNTITD":"",
    //                "DEVCLASS":""
    //

        /**
         * 返回指定下表对应的item的View对象
         * @param position 下标
         * @param convertView 可复用的缓存Item试图对象，前n+1个为null
         * @param parent ListView对象
         * @return
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //如果没有复用的
            if(convertView == null){
                //加载item的布局
                convertView = View.inflate(getActivity(), R.layout.fragment_listview_check, null);

            }

            //根据position设置对应数据
            //获得当前数据对象
            Task task = tasks.get(position);
            TextView tv_check_device = convertView.findViewById(R.id.tv_check_device);
            TextView tv_check_address = convertView.findViewById(R.id.tv_check_address);
            TextView tv_check_endtime = convertView.findViewById(R.id.tv_check_endtime);
            LinearLayout ll_check_bg = convertView.findViewById(R.id.ll_check_bg);
            int color = 0xFFFFFF00;
            if(position>15){
                color = 0xC606F600;
            }
            else{
                color = 0xC6F00000-(0x000F0000-0x00000F00)*position;

            }
            //ll_check_bg.setBackgroundColor(color);
            tv_check_device.setTextColor(color);
            tv_check_address.setTextColor(color);
            tv_check_endtime.setTextColor(color);

            tv_check_device.setText(task.getDEVID());
            tv_check_address.setText(task.getPLACE());
            tv_check_endtime.setText(task.getDEADLINE());
            return convertView;
        }
    }


}
