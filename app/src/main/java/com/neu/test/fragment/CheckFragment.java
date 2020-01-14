package com.neu.test.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import com.neu.test.R;
import com.neu.test.activity.DetctionActivity;
import com.neu.test.entity.Device;
import com.neu.test.layout.BottomBarLayout;

import java.util.List;

public class CheckFragment extends Fragment {

    private ListView lv_check;
    CheckAdapter checkAdapter;
    List<Device> dataDevice;
    BottomBarLayout mBottomBarLayout;
    Integer taskType;

    public CheckFragment(List<Device> dataDevice, BottomBarLayout bottomBarLayout,int taskType){
        this.dataDevice = dataDevice;
        mBottomBarLayout = bottomBarLayout;
        this.taskType = taskType;

    }

//    @Override
//    public boolean onContextItemSelected(@NonNull MenuItem item) {
//        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        int selectedPosition = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
//        switch(item.getItemId()){
//            case 0:
//                dataDevice.remove(selectedPosition);//选择行的位置
//                checkAdapter.notifyDataSetChanged();
//                lv_check.invalidate();
//
//                break;
//            case 1:
//                break;
//
//
//        }
//        return super.onContextItemSelected(item);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, null);
        lv_check = view.findViewById(R.id.lv_check);

        // mBottomBarLayout.setUnread(0, 20);//设置第一个页签的未读数为20
        //准备数据
        //getData();
        //准备BaseAdapter
        checkAdapter = new CheckAdapter();
        //设置Adapter显示列表
        lv_check.setAdapter(checkAdapter);
        lv_check.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Device test =dataDevice.get(position);
                String s = test.getDeviceName();
                int taskPosition = position;
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), DetctionActivity.class);
                intent.putExtra("TITLE",s);
                intent.putExtra("position",taskPosition);
                intent.putExtra("taskType",taskType);
                startActivity(intent);

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




    class CheckAdapter extends BaseAdapter{


        //返回集合数据数量
        @Override
        public int getCount() {
            return dataDevice.size();
        }

        //返回指定下标的数据对象
        @Override
        public Object getItem(int position) {
            return dataDevice.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

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
            Device device = dataDevice.get(position);
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

            tv_check_device.setText(device.getDeviceName());
            tv_check_address.setText(device.getDeviceAddress());
            tv_check_endtime.setText(device.getEndTime());
            return convertView;
        }
    }









}
