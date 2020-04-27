package com.neu.test.tree;

import java.util.List;

public class TreeNode<T> {

	public static final int ROOT_PARENT_ID = -1;
	private int id = 0;
	private int parentId = 0;
	private int level = 0;
	private boolean isGroup = false;
	private boolean isExpanded = false;
	private boolean isSelected = false;
	private T selfData = null;
	private List<TreeNode> childNodeList = null;

	public TreeNode(int id, int pId, boolean isGroup, T t) {
		this.id = id;
		parentId = pId;
		this.isGroup = isGroup;
		selfData = t;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isGroup() {
		return isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public T getSelfData() {
		return selfData;
	}

	public void setSelfData(T selfData) {
		this.selfData = selfData;
	}

	public List<TreeNode> getChildNodeList() {
		return childNodeList;
	}

	public void setChildNodeList(List<TreeNode> childNodeList) {
		this.childNodeList = childNodeList;
	}

}
