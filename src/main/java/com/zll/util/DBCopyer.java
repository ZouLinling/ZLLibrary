package com.zll.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DBCopyer {

    private static DBCopyer instance = null;

    /**
     * 本类只提供数据库的拷贝以及获取相应数据库实例的功能，其余功能不提供，如需要，可以自行继承后扩展
     * @param context
     * @param dbResId 数据库文件的资源ID需要放在res/raw文件夹下
     */
    public static DBCopyer getInstance(Context context, int dbResId) {
        if (instance == null) {
            synchronized (DBCopyer.class) {
                if (instance == null) {
                    instance = new DBCopyer(context, dbResId);
                }
            }
        }
        return instance;
    }

	private final int BUFFER_SIZE = 400000;
	private String dbName = ""; // 保存的数据库文件名
	private String DB_PATH = ""; // 在手机里存放数据库的位置
	int dbResID;

	private SQLiteDatabase database;
	private Context context;

	private DBCopyer(Context context, int dbResId) {
		this(context,"",dbResId);
	}

	private DBCopyer(Context context,String dbName, int dbResId) {
        if (dbName == null || dbName.equals("")) {
            this.dbName = "mydb.db";
        } else {
            this.dbName = dbName;
        }
        this.context = context;
        DB_PATH = "/data"
                + Environment.getDataDirectory().getAbsolutePath() + "/"
                + context.getPackageName()+"/databases/";
        this.dbResID = dbResId;
	}

	/**
	 * 拷贝执行方法
	 */
	public void openDatabase() {
        if (isOpen()) {
            return;
        }
		String dbFile = DB_PATH + dbName;
		try {
			if (!(new File(dbFile).exists())) {// 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
				InputStream is = this.context.getResources().openRawResource(dbResID); // 欲导入的数据库
				FileOutputStream fos = new FileOutputStream(dbFile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
            this.database = SQLiteDatabase.openDatabase(dbFile, null,
                SQLiteDatabase.OPEN_READWRITE);
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
	}

	public void closeDatabase() {
		this.database.close();
	}
	
	public SQLiteDatabase getDatabase() {
		return this.database;
	}

    public boolean isOpen() {
        if (database != null) {
            return database.isOpen();
        }
        return false;
    }
}