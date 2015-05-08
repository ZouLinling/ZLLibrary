package com.zll.logs;

public class Log {
	
	/**
	 * 默认是开启log，可以使用该方法来关闭
	 */
	public static void setDeubg(boolean debug) {
		DEBUG = debug;
	}
	
	
	private static boolean DEBUG = true;
	
	public static void setDebugMode(boolean debug) {
		DEBUG = debug;
	}
	
	public static void d(String tag, String msg) {
		if(DEBUG) {
			android.util.Log.d(tag, msg);
		}
	}
	
	public static void d(String tag, String msg, Throwable t) {
		if(DEBUG) {
			android.util.Log.d(tag, msg, t);
		}
	}
	
	public static void e(String tag, String msg){
		if(DEBUG) {
			android.util.Log.e(tag, msg);
		}
	}
	
	public static void e(String tag, String msg, Exception e){
		if(DEBUG) {
			android.util.Log.w(tag, msg, e);
		}
	}
	
	public static void w(String tag, String msg){
		if(DEBUG) {
			android.util.Log.w(tag, msg);
		}
	}
	
	public static void w(String tag, String msg, Exception e){
		if(DEBUG) {
			android.util.Log.w(tag, msg, e);
		}
	}
	
	public static void i(String tag, String msg){
		if(DEBUG) {
			android.util.Log.i(tag, msg);
		}
	}
	
	public static void v(String tag, String msg) {
		if(DEBUG) {
			android.util.Log.v(tag, msg);
		}
	}
}
