package com.zll.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public abstract class BaseAdapter<B, H> extends android.widget.BaseAdapter {
	protected Context mContext;
	protected LayoutInflater mInflater;

	protected List<B> mBeanList = new ArrayList<B>();

	public BaseAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public BaseAdapter(Context context, ListView listView) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initList(listView);
	}

	public BaseAdapter(Context context, ListView listView, List<B> list) {
		this(context);
		if (null == listView) {
			throw new NullPointerException("listview is null");
		}
		if (null == list) {
			throw new NullPointerException("list data is null");
		}
		mBeanList.addAll(list);
		initList(listView);
	}
	
	private void initList(ListView listView){
		if(listView == null){
			return;
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				onDataItemClick(position, mBeanList.get(position));
			}
		});
	}

	@Override
	public int getCount() {
		return mBeanList.size();
	}

	@Override
	public B getItem(int position) {
		return mBeanList.get(position);
	}

	public void removeItem(int position) {
		if (position >= mBeanList.size()) {
			return;
		}
		mBeanList.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		H holder = null;
		if (view == null) {
			view = createItem(position);
			holder = initHolder(view);
			view.setTag(holder);
		} else {
			holder = (H) view.getTag();
		}

		setViewContent(holder, getItem(position), position);
		return view;
	}

	public final List<B> getAllList() {
		return mBeanList;
	}

	/**
	 * 创建ListItem
	 */
	protected abstract View createItem(int position);

	/**
	 * 初始化ViewHolder
	 */
	protected abstract H initHolder(View view);

	/**
	 * 设置ListItem的内容
	 */
	protected abstract void setViewContent(H holder, B bean, int position);

	
	protected void onDataItemClick(int position, B bean) {

	}
}
