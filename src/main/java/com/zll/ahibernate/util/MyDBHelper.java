package com.zll.ahibernate.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * @author zou
 * 继承该类
 * public class DBHelper extends MyDBHelper {

	private static final String DBNAME = "B2C.db"; //dbname
	private static final int DBVERSION = 1;
	private static final Class<?>[] clazz = { BrowseRecord.class,
			ShopCart.class,ScanRecord.class};

	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}
	}
 *
 */
public class MyDBHelper extends SQLiteOpenHelper {
	private Class<?>[] modelClasses;

	public MyDBHelper(Context context, String databaseName,
			SQLiteDatabase.CursorFactory factory, int databaseVersion,
			Class<?>[] modelClasses) {
		super(context, databaseName, factory, databaseVersion);
		this.modelClasses = modelClasses;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TableHelper.createTablesByClasses(db, this.modelClasses);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		TableHelper.dropTablesByClasses(db, this.modelClasses);
		onCreate(db);
	}
}