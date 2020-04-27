package com.neu.test.tree;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TreeListView extends ListView {

	private TreeBaseAdapter mListAdapter;
	private ListItemClickListener mItemClickListener;
	private OnGroupItemClickListener mGroupItemClickListener;
	private OnChildItemClickListener mChildItemClickListener;
	private OnExpandClickListener mExpandClickListener;
	private OnCollapseClickListener mCollapseClickListener;

	public TreeListView(Context context) {
		this(context, null);
	}

	public TreeListView(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.listViewStyle);
	}

	public TreeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		registerListener();
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (adapter instanceof TreeBaseAdapter) {
			super.setAdapter(adapter);
			mListAdapter = (TreeBaseAdapter) adapter;
		} else
			throw new IllegalArgumentException(
					"the adapter must is TreeBaseAdapter");
	}

	@Override
	public void setOnItemClickListener(OnItemClickListener listener) {
		if (listener instanceof ListItemClickListener) {
			super.setOnItemClickListener(listener);
			mItemClickListener = (ListItemClickListener) listener;
		} else
			throw new IllegalArgumentException(
					"the listener must is ListItemClickListener of TreeListView");
	}


	public void setOnGroupItemClickListener(OnGroupItemClickListener listener) {
		mGroupItemClickListener = listener;
	}

	public void setOnChildItemClickListener(OnChildItemClickListener listener) {
		mChildItemClickListener = listener;
	}

	public void setOnExpandClickListener(OnExpandClickListener listener) {
		mExpandClickListener = listener;
	}

	public void setOnCollapseClickListener(OnCollapseClickListener listener) {
		mCollapseClickListener = listener;
	}

	public void setOnExpandListener(OnExpandListener listener) {
		mListAdapter.setOnExpandListener(listener);
	}

	public void setOnCollapseListener(OnCollapseListener listener) {
		mListAdapter.setOnCollapseListener(listener);
	}

	public void setOnSelectListener(OnSelectListener listener) {
		mListAdapter.setOnSelectListener(listener);
	}

	private void registerListener() {
		setOnItemClickListener(new ListItemClickListener());
	}

	private class ListItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			int headerViewCount = getHeaderViewsCount();
			if (position < headerViewCount)
				return;
			position = position - headerViewCount;
			if (position >= mListAdapter.getCount())
				return;

			TreeNode node = mListAdapter.getNodeOfItem(position);
			int nId = node.getId();
			boolean isGroup = node.isGroup();
			boolean isExpanded = node.isExpanded();
			if (isGroup) {
				if (mGroupItemClickListener != null)
					mGroupItemClickListener.onGroupClick(parent, view,
							position, node);
				if (isExpanded == false) {
					mListAdapter.expand(nId);
					if (mExpandClickListener != null)
						mExpandClickListener.onExpandClick(parent, view,
								position, node);
				} else {
					mListAdapter.collapse(nId);
					if (mCollapseClickListener != null)
						mCollapseClickListener.onCollapseClick(parent, view,
								position, node);
				}
			} else {
				if (mChildItemClickListener != null)
					mChildItemClickListener.onChildClick(parent, view,
							position, node);
			}
		}
	}

	public interface OnGroupItemClickListener {
		public void onGroupClick(AdapterView<?> parent, View view,
                                 int position, TreeNode node);
	}

	public interface OnChildItemClickListener {
		public void onChildClick(AdapterView<?> parent, View view,
                                 int position, TreeNode node);
	}

	public interface OnExpandClickListener {
		public void onExpandClick(AdapterView<?> parent, View view,
                                  int position, TreeNode node);
	}

	public interface OnCollapseClickListener {
		public void onCollapseClick(AdapterView<?> parent, View view,
                                    int position, TreeNode node);
	}

	public interface OnExpandListener {
		public void onExpand(TreeNode node);
	}


	public interface OnCollapseListener {
		public void onCollapse(TreeNode node);
	}

	public interface OnSelectListener {
		public void onSelect(TreeNode node);
	}

}
