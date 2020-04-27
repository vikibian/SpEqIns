package com.neu.test.tree;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class TreeBaseAdapter<T> extends BaseAdapter {

	private int mPaddingConvertViewLeft;
	private List<TreeNode> mTreeNodeList;
	private List<TreeNode> mVisibleNodeList;

	private TreeListView.OnExpandListener mExpandListener;
	private TreeListView.OnCollapseListener mCollapseListener;
	private TreeListView.OnSelectListener mSelectListener;

	public TreeBaseAdapter(List<T> dataList, int paddingConvertViewLeft) {
		mPaddingConvertViewLeft = paddingConvertViewLeft;
		mVisibleNodeList = new ArrayList<TreeNode>();
		setNodeListOfClass(dataList);
	}

	@Override
	public int getCount() {
		return mVisibleNodeList.size();
	}

	@Override
	public Object getItem(int position) {
		return mVisibleNodeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TreeNode node = mVisibleNodeList.get(position);
		convertView = getConvertView(node, position, convertView, parent);
		int paddingRight = convertView.getPaddingRight();
		int paddingTop = convertView.getPaddingTop();
		int paddingBottom = convertView.getPaddingBottom();
		convertView.setPadding(node.getLevel() * mPaddingConvertViewLeft,
				paddingTop, paddingRight, paddingBottom);
		return convertView;
	}

	/**
	 * ���ò������б����convertView
	 * 
	 * @param node
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getConvertView(TreeNode node, int position,
                                      View convertView, ViewGroup parent);

	/**
	 * ��ȡָ���б�������ڵ����
	 * 
	 * @param position
	 *            �б�������
	 * @return ���ڵ����
	 */
	public TreeNode getNodeOfItem(int position) {
		return mVisibleNodeList.get(position);
	}

	/**
	 * �����б���
	 * 
	 * @param dataList
	 *            ����
	 */
	public void notifyDataSetChanged(List<T> dataList) {
		setNodeListOfClass(dataList);
		notifyDataSetChanged();
	}

	/**
	 * չ��ָ��id����
	 * 
	 * @param id
	 *            ��Id
	 * @return trueΪչ���ɹ���falseΪʧ��
	 */
	public boolean expand(int id) {
		boolean result = false;
		for (TreeNode node : mTreeNodeList) {
			List<TreeNode> resultList = expand(node, id);
			if (resultList.size() > 0) {
				result = true;
				setVisibleNodeList();
				notifyDataSetChanged();
				break;
			}
		}
		return result;
	}

	/**
	 * ����ָ��id����
	 * 
	 * @param id
	 *            ��Id
	 * @return trueΪ����ɹ���falseΪʧ��
	 */
	public boolean collapse(int id) {
		boolean result = false;
		for (TreeNode node : mTreeNodeList) {
			result = collapse(node, id);
			if (result == true) {
				setVisibleNodeList();
				notifyDataSetChanged();
				break;
			}
		}
		return result;
	}

	/**
	 * չ�����е���
	 */
	public void expandAll() {
		for (TreeNode node : mTreeNodeList)
			expand(node);
		setVisibleNodeList();
		notifyDataSetChanged();
	}

	/**
	 * �������е���
	 */
	public void collapseAll() {
		for (TreeNode node : mTreeNodeList)
			collapse(node);
		setVisibleNodeList();
		notifyDataSetChanged();
	}

	/**
	 * ѡ��ָ��id�����ڵ㣬������������ڵ�һ��ѡ��
	 * 
	 * @param id
	 *            ���ڵ�id
	 * @param isSelected
	 *            trueΪѡ�У�falseΪ��ѡ��
	 */
	public void select(int id, boolean isSelected) {
		for (TreeNode node : mTreeNodeList) {
			boolean flag = select(node, id, isSelected);
			if (flag == true) {
				notifyDataSetChanged();
				break;
			}
		}
	}

	/**
	 * ѡ�����е����ڵ�
	 * 
	 * @param isSelected
	 *            trueΪѡ�У�falseΪ��ѡ��
	 */
	public void selectAll(boolean isSelected) {
		for (TreeNode node : mTreeNodeList)
			select(node, isSelected);
		notifyDataSetChanged();
	}

	// **********************************************************************************
	// ��������

	/**
	 * ע��չ�������¼�����TreeListView��ע����
	 * 
	 * @param listener
	 */
	protected void setOnExpandListener(TreeListView.OnExpandListener listener) {
		mExpandListener = listener;
	}

	/**
	 * ע����������¼�����TreeListView��ע����
	 * 
	 * @param listener
	 */
	protected void setOnCollapseListener(TreeListView.OnCollapseListener listener) {
		mCollapseListener = listener;
	}

	/**
	 * ע��ѡ������¼�����TreeListView��ע����
	 * 
	 * @param listener
	 */
	protected void setOnSelectListener(TreeListView.OnSelectListener listener) {
		mSelectListener = listener;
	}

	// **********************************************************************************
	// ˽�з���

	/**
	 * ����ȫ�����ڵ�Ϳɼ����ڵ�
	 * 
	 * @param dataList
	 */
	private void setNodeListOfClass(List<T> dataList) {
		mTreeNodeList = convertTreeNode(dataList);
		setVisibleNodeList();
	}

	/**
	 * ���ÿɼ����ڵ�
	 */
	private void setVisibleNodeList() {
		mVisibleNodeList.clear();
		List<TreeNode> visibleList = getVisibleNoteList(mTreeNodeList);
		for (TreeNode node : visibleList)
			mVisibleNodeList.add(node);
	}

	/**
	 * ������ת������������ڵ�
	 * 
	 * @param dataList
	 *            ����
	 * @return ��������ڵ�
	 */
	private List<TreeNode> convertTreeNode(List<T> dataList) {
		try {

			List<TreeNode> nodeList = new ArrayList<TreeNode>();
			for (T t : dataList) {
				boolean isGetId = false;
				boolean isGetPid = false;
				boolean isGetGroup = false;
				int id = -1;
				int pId = -1;
				boolean isGroup = false;
				Class<? extends Object> clazz = t.getClass();
				Field[] declaredFields = clazz.getDeclaredFields();
				for (Field f : declaredFields) {
					if (f.getAnnotation(TreeNodeId.class) != null) {
						// java�����У�������һ����ĳ�Ա��������Ϊprivate������������ȡ����˽�г�Ա����ʱ����ʹ��setAccessible������
						// setAccessible(true)�����ǽ�����Ȩ�޸ĳ���public������ȡ��java��Ȩ�޿��Ƽ�顣
						// ���Լ�ʹ��public����accessible����Ĭ��Ҳ��false��
						f.setAccessible(true);
						id = f.getInt(t);
						isGetId = true;
					}
					if (f.getAnnotation(TreeNodePid.class) != null) {
						f.setAccessible(true);
						pId = f.getInt(t);
						isGetPid = true;
					}
					if (f.getAnnotation(TreeNodeGroup.class) != null) {
						f.setAccessible(true);
						isGroup = f.getBoolean(t);
						isGetGroup = true;
					}
					if (isGetId == true && isGetPid == true
							&& isGetGroup == true) {
						TreeNode node = new TreeNode<T>(id, pId, isGroup, t);
						nodeList.add(node);
						break;
					}
				}
			}
			List<TreeNode> rootList = getRootNodeList(nodeList);
			List<TreeNode> treeList = sortLevelOfTreeNode(rootList, nodeList);
			return treeList;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<TreeNode>();
		}
	}

	/**
	 * ��ȡ���ĸ��ڵ�
	 * 
	 * @param nodeList
	 *            ���ҵ����ڵ�
	 * @return
	 */
	private List<TreeNode> getRootNodeList(List<TreeNode> nodeList) {
		List<TreeNode> rootList = new ArrayList<TreeNode>();
		for (TreeNode node : nodeList) {
			int parentId = node.getParentId();
			if (parentId == TreeNode.ROOT_PARENT_ID)
				rootList.add(node);
		}
		return rootList;
	}

	/**
	 * ���õݹ鷽���������ҵ����ڵ��������������ڵ�
	 * 
	 * @param parentList
	 *            �����ڵ�
	 * @param nodeList
	 *            ���ҵ����ڵ�
	 * @return
	 */
	private List<TreeNode> sortLevelOfTreeNode(List<TreeNode> parentList,
                                             List<TreeNode> nodeList) {
		for (TreeNode parentNode : parentList) {
			int id = parentNode.getId();
			int level = parentNode.getLevel();
			nodeList.remove(parentNode);
			List<TreeNode> childNodeList = null;
			for (TreeNode node : nodeList) {
				int pId = node.getParentId();
				if (pId == id) {
					childNodeList = parentNode.getChildNodeList();
					if (childNodeList == null) {
						childNodeList = new ArrayList<TreeNode>();
						parentNode.setChildNodeList(childNodeList);
					}
					node.setLevel(level + 1);
					childNodeList.add(node);
				}
			}
			if (childNodeList != null)
				childNodeList = sortLevelOfTreeNode(childNodeList, nodeList);
		}
		return parentList;
	}

	/**
	 * ��ȡ�ɼ������ڵ�
	 * 
	 * @param treeList
	 *            ��������ڵ�
	 * @return
	 */
	private List<TreeNode> getVisibleNoteList(List<TreeNode> treeList) {
		List<TreeNode> visibleList = new ArrayList<TreeNode>();
		for (TreeNode node : treeList) {
			visibleList.add(node);
			List<TreeNode> sonList = filterVisibleNodeList(node);
			visibleList.addAll(sonList);
		}
		return visibleList;
	}

	/**
	 * ���˳��ɼ������ڵ�
	 * 
	 * @param node
	 * @return
	 */
	private List<TreeNode> filterVisibleNodeList(TreeNode node) {
		List<TreeNode> visibleList = new ArrayList<TreeNode>();
		if (node.isExpanded()) {
			List<TreeNode> childList = node.getChildNodeList();
			for (TreeNode child : childList) {
				visibleList.add(child);
				List<TreeNode> grandsonList = filterVisibleNodeList(child);
				visibleList.addAll(grandsonList);
			}
		}
		return visibleList;
	}

	/**
	 * չ��ָ��id����
	 * 
	 * @param node
	 * @param id
	 * @return
	 */
	private List<TreeNode> expand(TreeNode node, int id) {
		List<TreeNode> resultList = new ArrayList<TreeNode>();
		List<TreeNode> childList = node.getChildNodeList();
		if (childList != null) {
			if (node.getId() == id) {
				node.setExpanded(true);
				expandListener(node);
				resultList.addAll(childList);
			} else {
				for (TreeNode child : childList) {
					List<TreeNode> tempList = expand(child, id);
					if (tempList.size() > 0) {
						if (node.isExpanded() == false) {
							node.setExpanded(true);
							expandListener(node);
						}
						resultList.addAll(childList);
						break;
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * չ�����ڵ�
	 * 
	 * @param node
	 */
	private void expand(TreeNode node) {
		List<TreeNode> childList = node.getChildNodeList();
		if (childList != null) {
			node.setExpanded(true);
			expandListener(node);
			for (TreeNode child : childList)
				expand(child);
		}
	}

	/**
	 * ����ָ��id����
	 * 
	 * @param node
	 * @param id
	 * @return
	 */
	private boolean collapse(TreeNode node, int id) {
		boolean result = false;
		if (node.isExpanded()) {
			List<TreeNode> childList = node.getChildNodeList();
			if (node.getId() == id) {
				collapse(node);
				result = true;
			} else {
				for (TreeNode child : childList) {
					result = collapse(child, id);
					if (result == true)
						break;
				}
			}
		}
		return result;
	}

	/**
	 * �������ڵ�
	 * 
	 * @param node
	 */
	private void collapse(TreeNode node) {
		if (node.isExpanded()) {
			node.setExpanded(false);
			collapseListener(node);
			List<TreeNode> childList = node.getChildNodeList();
			for (TreeNode child : childList)
				collapse(child);
		}
	}

	/**
	 * ѡ��ָ��id�����ڵ㣬������������ڵ�һ��ѡ��
	 * 
	 * @param node
	 * @param id
	 * @param isSelected
	 *            trueΪѡ�У�falseΪ��ѡ��
	 * @return
	 */
	private boolean select(TreeNode node, int id, boolean isSelected) {
		boolean result = false;
		List<TreeNode> childList = node.getChildNodeList();
		if (node.getId() == id) {
			select(node, isSelected);
			result = true;
		} else {
			if (childList != null) {
				for (TreeNode child : childList) {
					result = select(child, id, isSelected);
					if (result == true)
						break;
				}
			}
		}
		return result;
	}

	/**
	 * ѡ�����ڵ㣬������������ڵ�һ��ѡ��
	 * 
	 * @param node
	 * @param isSelected
	 *            trueΪѡ�У�falseΪ��ѡ��
	 */
	private void select(TreeNode node, boolean isSelected) {
		if (node.isSelected() != isSelected) {
			node.setSelected(isSelected);
			selectListener(node);
		}
		List<TreeNode> childList = node.getChildNodeList();
		if (childList != null) {
			for (TreeNode child : childList)
				select(child, isSelected);
		}
	}

	/**
	 * �ص�չ����������
	 * 
	 * @param node
	 */
	private void expandListener(TreeNode node) {
		if (mExpandListener != null)
			mExpandListener.onExpand(node);
	}

	/**
	 * �ص������������
	 * 
	 * @param node
	 */
	private void collapseListener(TreeNode node) {
		if (mCollapseListener != null)
			mCollapseListener.onCollapse(node);
	}

	/**
	 * �ص�ѡ�м�������
	 * 
	 * @param node
	 */
	private void selectListener(TreeNode node) {
		if (mSelectListener != null)
			mSelectListener.onSelect(node);
	}

}
