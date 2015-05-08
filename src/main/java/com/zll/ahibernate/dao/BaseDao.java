package com.zll.ahibernate.dao;

import java.util.List;
import java.util.Map;

import android.database.sqlite.SQLiteOpenHelper;

public interface BaseDao<T> {

	public SQLiteOpenHelper getDbHelper();

	/**
	 * 默认主键自増，调用insert(T,true);
	 * 
	 * @param entity
	 * @return
	 */
	public abstract long insert(T entity);

	/**
	 * flag为true是自动生成主键，为false时需要手动指定主键
	 * 
	 * @param entity
	 * @param flag
	 * @return
	 */
	public abstract long insert(T entity, boolean flag);

	public abstract void delete(int id);

	public abstract void delete(Integer... ids);

	public abstract void update(T entity, int idValue);
	
	public abstract void update(T entity);

	public abstract T get(int id);

	public abstract List<T> rawQuery(String sql, String[] selectionArgs);

	public abstract List<T> find();

	public abstract List<T> find(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit);

	public abstract boolean isExist(String sql, String[] selectionArgs);

	/**
	 * 将查询的结果保存为键值对map
	 * 
	 * @param sql
	 *            查询sql
	 * @param selectionArgs
	 *            查询参数
	 * @return 返回的map中key全部是小写
	 */
	public List<Map<String, String>> query2MapList(String sql,
			String[] selectionArgs);

	/**
	 * 执行sql代码
	 * 
	 * @param sql
	 * @param selectionArgs
	 */
	public void execSql(String sql, Object[] selectionArgs);

}