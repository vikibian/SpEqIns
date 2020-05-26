package com.neu.test.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.neu.test.R;
import com.neu.test.entity.CheckLists;
import com.neu.test.entity.DetectionResult;
import com.neu.test.layout.MyTextView;
import com.neu.test.tree.TreeBaseAdapter;
import com.neu.test.tree.TreeListView;
import com.neu.test.tree.TreeNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class TreeListActivity2 extends AppCompatActivity {

  private TreeListView mTreeListView = null;
  private MyTreeBaseAdapter mTreeListAdapter = null;
  private List<CheckLists> myCheckLists = new ArrayList<>();
  private List<CheckLists> subCheckLists = new ArrayList<>();
  private List<TreeNode> treeNodeList = new ArrayList<>();
  private Toolbar mToolbar;
  private TextView textView_toolbar;
  private Button btn_submit;
  private Intent intent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tree_list);

    findView();
    initToolbar();
    init();
    registerListener();
  }

  private void initToolbar() {
    mToolbar = findViewById(R.id.tree_toolbar);
    mToolbar.setTitle(" ");
    mToolbar.setTitleTextColor(Color.WHITE);

    textView_toolbar = findViewById(R.id.tree_textview);
    textView_toolbar.setText("选择任务项");
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
  }
  private void findView() {
    mTreeListView = (TreeListView) findViewById(R.id.treeListView);
    btn_submit = findViewById(R.id.btn_submitItems);
  }

  private void init() {
    intent = getIntent();
    myCheckLists = (List<CheckLists>) intent.getSerializableExtra("CheckLists");

    mTreeListAdapter = new MyTreeBaseAdapter(TreeListActivity2.this,myCheckLists);
    mTreeListView.setAdapter(mTreeListAdapter);
    //TreeMethod();
  }

  private void registerListener() {
    btn_submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(subCheckLists.size() == 0){
          Toasty.warning(TreeListActivity2.this,"未选择检查项").show();
        }
        intent.putExtra("CList", (Serializable) subCheckLists);
        setResult(RESULT_OK,intent);
        finish();
      }
    });
    // mTreeListView.setOnGroupItemClickListener(new
    // GroupItemClickListener());
    // mTreeListView.setOnChildItemClickListener(new
    // ChildItemClickListener());
    // mTreeListView.setOnExpandClickListener(new ExpandClickListener());
    // mTreeListView.setOnCollapseClickListener(new
    // CollapseClickListener());
    mTreeListView.setOnExpandListener(new ExpandListener());
    mTreeListView.setOnCollapseListener(new CollapseListener());
    mTreeListView.setOnSelectListener(new SelectListener());
  }
  /*
    private void TreeMethod() {
      Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          UserBean bean = new UserBean(0x51121, 0x5111, true,
            "五号1121");
          mDataList.add(bean);
          bean = new UserBean(0x51122, 0x5111, false, "五号1122");
          mDataList.add(bean);

          notifyDataSetChanged(mDataList);
        }
      }, 6000);
    }
  */
  private void expand(int groupId) {
    mTreeListAdapter.expand(groupId);
  }

  private void collapse(int groupId) {
    mTreeListAdapter.collapse(groupId);
  }

  private void expandAll() {
    mTreeListAdapter.expandAll();
  }

  private void collapseAll() {
    mTreeListAdapter.collapseAll();
  }

  private void selectAll(boolean flag) {
    mTreeListAdapter.selectAll(flag);
  }

  private void notifyDataSetChanged(List<CheckLists> dataList) {
    mTreeListAdapter.notifyDataSetChanged(dataList);
  }

  private class MyTreeBaseAdapter<T> extends TreeBaseAdapter<T> {

    private LayoutInflater inflater = null;

    public MyTreeBaseAdapter(Context context, List<T> dataList) {
      super(dataList, 30);
      inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
      public ImageView imageView = null;
      public MyTextView textView = null;
      public CheckBox checkBox = null;
      public ImageView tree_more;
    }

    @Override
    public View getConvertView(final TreeNode node, int position,
                               View convertView, ViewGroup parent) {
      ViewHolder holder = null;
      if (convertView == null) {
        holder = new ViewHolder();
        convertView = inflater
          .inflate(R.layout.treelistview_item, null);
        holder.imageView = (ImageView) convertView
          .findViewById(R.id.imageView);
        holder.textView = (MyTextView) convertView
          .findViewById(R.id.textView);
        holder.checkBox = (CheckBox) convertView
          .findViewById(R.id.checkBox);
        convertView.setTag(holder);
        holder.tree_more = convertView.findViewById(R.id.tree_more);
      } else
        holder = (ViewHolder) convertView.getTag();

      boolean isGroup = node.isGroup();
      CheckLists checkLists = (CheckLists) node.getSelfData();
      if (isGroup == true) {
        holder.tree_more.setVisibility(View.GONE);
        holder.imageView.setImageResource(R.mipmap.jiahao);
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        holder.textView.setTextColor(Color.rgb(0, 0, 0xb0));
      } else {
        holder.tree_more.setVisibility(View.VISIBLE);
        holder.imageView.setImageResource(R.mipmap.jian);
        holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        holder.textView.setTextColor(Color.BLACK);

      }
      holder.textView.setText(checkLists.getMONITORCONTENT());

      holder.checkBox.setChecked(node.isSelected());
      holder.checkBox.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mTreeListAdapter.select(node.getId(), !node.isSelected());
        }
      });

      holder.tree_more.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Dialog dlg = new Dialog(TreeListActivity2.this,R.style.FullScreen);
          View textEntryView = View.inflate(TreeListActivity2.this,R.layout.show_law_and_other, null);
          LinearLayout linearLayout = textEntryView.findViewById(R.id.xiangmuID);
          linearLayout.setVisibility(View.GONE);
          TextView tv_paichaxize = textEntryView.findViewById(R.id.tv_paichaxize);
          TextView tv_laws = textEntryView.findViewById(R.id.tv_laws);
          Button btn_cancel = textEntryView.findViewById(R.id.btn_cancel);
          btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dlg.dismiss();
            }
          });
          tv_paichaxize.setText(myCheckLists.get(position).getMONITORCONTENT());
          tv_laws.setText(myCheckLists.get(position).getLAW());
          dlg.setContentView(textEntryView);
          dlg.show();
        }
      });

      return convertView;
    }

  }

  private class GroupItemClickListener implements TreeListView.OnGroupItemClickListener {
    @Override
    public void onGroupClick(AdapterView<?> parent, View view,
                             int position, TreeNode node) {
      /*UserBean bean = (UserBean) node.getSelfData();
      toast("GroupItemClickListener" + bean.getName());*/
    }

  }

  private class ChildItemClickListener implements TreeListView.OnChildItemClickListener {
    @Override
    public void onChildClick(AdapterView<?> parent, View view,
                             int position, TreeNode node) {

    }
  }

  private class ExpandClickListener implements TreeListView.OnExpandClickListener {
    @Override
    public void onExpandClick(AdapterView<?> parent, View view,
                              int position, TreeNode node) {

    }
  }

  private class CollapseClickListener implements TreeListView.OnCollapseClickListener {
    @Override
    public void onCollapseClick(AdapterView<?> parent, View view,
                                int position, TreeNode node) {


    }
  }

  private class ExpandListener implements TreeListView.OnExpandListener {
    @Override
    public void onExpand(TreeNode node) {

    }
  }

  private class CollapseListener implements TreeListView.OnCollapseListener {
    @Override
    public void onCollapse(TreeNode node) {

    }
  }

  private class SelectListener implements TreeListView.OnSelectListener {
    @Override
    public void onSelect(TreeNode node) {
      if(node.isGroup()){
        if(!treeNodeList.contains(node)){
          treeNodeList.add(node);
        }
      }else{
        if(node.isSelected()){
          if (!subCheckLists.contains(node.getSelfData())) {
            subCheckLists.add((CheckLists) node.getSelfData());
          }
        }else{
          if (subCheckLists.contains(node.getSelfData())) {
            subCheckLists.remove(node.getSelfData());
          }
        }
        int pId = node.getParentId();
        for (int i = 0; i < treeNodeList.size(); i++) {
          if (treeNodeList.get(i).getId() == pId) {
            List<TreeNode> list = treeNodeList.get(i).getChildNodeList();
            if (list.size() == subCheckLists.size())
              treeNodeList.get(i).setSelected(true);
            else
              treeNodeList.get(i).setSelected(false);
          }
        }
      }
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){
      case android.R.id.home:
        askForIsSureToFinish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    askForIsSureToFinish();
  }

  public void askForIsSureToFinish(){
    MessageDialog.build(TreeListActivity2.this)
      .setStyle(DialogSettings.STYLE.STYLE_IOS)
      .setTitle("提示")
      .setMessage("确认退出当前界面吗")
      .setOkButton("确定", new OnDialogButtonClickListener() {
        @Override
        public boolean onClick(BaseDialog baseDialog, View v) {
          finish();
          return false;
        }
      })
      .setCancelButton("取消", new OnDialogButtonClickListener() {
        @Override
        public boolean onClick(BaseDialog baseDialog, View v) {
          return false;
        }
      })
      .show();
  }
}

