package com.neu.test.util;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.kelin.scrollablepanel.library.PanelAdapter;
import com.neu.test.R;
import com.neu.test.fragment.PanelFragment;
import com.neu.test.fragment.ShowSearchedResultFragment;

import java.util.ArrayList;
import java.util.List;

public class ScrollablePanel1Adapter extends PanelAdapter {
    private static final int ITEM_TITLE = 0;
    private static final int ITEM_NAME = 1;
    private static final int ITEM_ROW = 2;
    private static final int ITEM_CONTENT = 3;
    private PanelFragment panelFragment;

    private List<ListName> listnames = new ArrayList<>();
    private List<List<ListContent>> listcontents = new ArrayList<>();

    public ScrollablePanel1Adapter(PanelFragment panelFragment) {
        this.panelFragment = panelFragment;
    }


    @Override
    public int getRowCount() {
        return listcontents.size()+1;

    }

    @Override
    public int getColumnCount() {
        return listnames.size()+1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        int itemType = getItemViewType(row,column);
        Log.d("Adpater ","  itemType:"+itemType);
        switch (itemType){
            case ITEM_NAME:
                setItemName(column, (ItemNameHolder) holder);
                break;
            case ITEM_ROW:
                setItemRow(row,(ItemRowHolder) holder);
                break;
            case ITEM_CONTENT:
                setItemContent(row,column, (ItemContentHolder) holder);
                break;
            case ITEM_TITLE:
                setItemTitle(row,column,(ItemTitleHolder) holder);
                break;
            default:
                setItemContent(row,column,(ItemContentHolder)holder);
        }

    }


    private void setItemTitle(int row, int column, ItemTitleHolder holder) {
        if(row == 0 && column==0){
            holder.itemTitleTextview.setText("条目");
        }
    }

    private void setItemName(int column, ItemNameHolder holder) {
        ListName name = listnames.get(column-1);
        if(name != null&&column>0){
            holder.itemNameTextview.setText(name.getName());
        }
    }

    private void setItemRow(final int row, ItemRowHolder holder) {
        holder.itemRowTextView.setText(String.valueOf(row));
        holder.itemRowTextView.setClickable(true);
        holder.itemRowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickToJump(row,v);
            }
        });
    }

    private void setItemContent(final int row,final int column, ItemContentHolder holder) {
        final ListContent content = listcontents.get(row-1).get(column-1);
        if(content!=null){
            holder.itemContentTextview.setText(content.getContent());
            holder.itemContentTextview.setClickable(true);
            holder.itemContentTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickToJump(row,v);
                }
            });
        }
    }

    private static class ItemTitleHolder extends RecyclerView.ViewHolder{
        public TextView itemTitleTextview;

        public ItemTitleHolder( View itemView) {
            super(itemView);
            this.itemTitleTextview = itemView.findViewById(R.id.list_item_title);
        }
    }

    private static class ItemNameHolder extends RecyclerView.ViewHolder{
        public TextView itemNameTextview;

        public ItemNameHolder(@NonNull View itemView) {
            super(itemView);
            this.itemNameTextview = itemView.findViewById(R.id.list_item_name);
        }
    }

    private static class ItemRowHolder extends RecyclerView.ViewHolder{
        public TextView itemRowTextView;

        public ItemRowHolder(@NonNull View itemView) {
            super(itemView);
            this.itemRowTextView = itemView.findViewById(R.id.list_item_row);
        }
    }

    private static class ItemContentHolder extends RecyclerView.ViewHolder{
        public TextView itemContentTextview;

        public ItemContentHolder(@NonNull View itemView) {
            super(itemView);
            this.itemContentTextview = itemView.findViewById(R.id.list_item_content);
        }
    }

    public int getItemViewType(int row, int column) {
        if(row ==0 && column ==0){
            Log.d("Adpapter: "," title row"+row+"  column"+column);
            return ITEM_TITLE;
        }
        if(row == 0){//
            Log.d("Adpapter: "," name row"+row+"  column"+column);
            return ITEM_NAME;
        }
        if (column==0){
            return ITEM_ROW;
        }
        return ITEM_CONTENT;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Adpater ","  itemType:"+viewType);
        switch (viewType){
            case ITEM_TITLE:
                return new ItemTitleHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_title,parent,false));
            case ITEM_NAME:
                return new ItemNameHolder(LayoutInflater.from(parent.getContext())
                         .inflate(R.layout.list_item_name,parent,false));
            case ITEM_ROW:
                return new ItemRowHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_row,parent,false));
            case ITEM_CONTENT:
                return new ItemContentHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_content,parent,false));
            default:
                break;
        }
        return new ItemContentHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_content,parent,false));
    }

    public void setNameList(List<ListName> listnames){
        this.listnames = listnames;
    }

    public void setContentList(List<List<ListContent>> listcontents){
        this.listcontents = listcontents;
    }

    private void clickToJump(int row,View v) {
        //Toast.makeText(v.getContext(),listcontents.get(row-1).toString()+" 被点击了",Toast.LENGTH_LONG).show();

        ResultBean resultBean = new ResultBean();

        resultBean.setTime(listcontents.get(row-1).get(0).getContent());
        resultBean.setDeviceType(listcontents.get(row-1).get(1).getContent());
        resultBean.setQualify(listcontents.get(row-1).get(2).getContent());
        resultBean.setIschecked(listcontents.get(row-1).get(3).getContent());
        resultBean.setUser(listcontents.get(row-1).get(4).getContent());


        Bundle bundle = new Bundle();
        bundle.putSerializable("result",resultBean);
        FragmentTransaction transaction = panelFragment.getFragmentManager().beginTransaction();
        ShowSearchedResultFragment showSearchedResultFragment = new ShowSearchedResultFragment();
        showSearchedResultFragment.setArguments(bundle);
        transaction.replace(R.id.fl_content,showSearchedResultFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();

    }

}
