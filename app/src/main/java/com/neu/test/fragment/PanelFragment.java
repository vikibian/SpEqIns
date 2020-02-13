package com.neu.test.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kelin.scrollablepanel.library.ScrollablePanel;
import com.neu.test.R;
import com.neu.test.util.ListContent;
import com.neu.test.util.ListName;
import com.neu.test.adapter.ScrollablePanel1Adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PanelFragment extends Fragment {


    public PanelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_panel, container, false);


        String[] listName={"时间","设备种类","合格情况","检查情况","人员","地址"};
        String[][] listContent={{"2019-10-02","电梯1","合格","已检查","admin","0"},
                {"2019-10-02","电梯2","合格","已检查","admin","1"},{"2019-10-02","电梯1","合格","已检查","admin","2"},
                {"2019-10-02","电梯1","合格","已检查","admin","1"},{"2019-10-02","电梯1","合格","已检查","admin"," "}};

        final ScrollablePanel scrollablePanel = view.findViewById(R.id.rearch_admin_scrollablepanel);
        final ScrollablePanel1Adapter scrollablePanel1Adapter = new ScrollablePanel1Adapter(this);

        initScrollablePanel(scrollablePanel,scrollablePanel1Adapter,listName,listContent);
        return view;
    }

    private void initScrollablePanel(ScrollablePanel scrollablePanel, ScrollablePanel1Adapter scrollablePanel1Adapter, String[] listName, String[][] listContent) {
        generateTable(scrollablePanel1Adapter,listName,listContent);
        scrollablePanel.setPanelAdapter(scrollablePanel1Adapter);
    }

    private void generateTable(ScrollablePanel1Adapter scrollablePanel1Adapter, String[] listName, String[][] listContent) {
        List<ListName> listItemName = new ArrayList<>();
        for(int i=0;i<listName.length;i++){
            ListName name =new ListName();
            name.setName(listName[i]);
            listItemName.add(name);
        }
        Log.d("SearchFragment: "," "+listItemName.toString());
        scrollablePanel1Adapter.setNameList(listItemName);

        List<List<ListContent>> listItemContent = new ArrayList<>();
        for(int i=0;i<listContent.length;i++){
            List<ListContent> listItemContent1 = new ArrayList<>();
            for(int j=0;j<listContent[i].length;j++){
                ListContent content = new ListContent();
                content.setContent(listContent[i][j]);
                listItemContent1.add(content);
            }
            listItemContent.add(listItemContent1);
        }
        scrollablePanel1Adapter.setContentList(listItemContent);
    }



}
