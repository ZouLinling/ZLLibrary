package com.zll.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Util {
	public Util() {
		// TODO Auto-generated constructor stub

	}

	public static boolean detectNetwork(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
			return false;
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable())
			return false;
		return true;
	}
	
	public static boolean isTopActivity(String packageName, Context context) 
	{
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
	    String currentPackageName = cn.getClassName();
	    if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(packageName))
	    {
	        return true ;
	    }
	 
	    return false ;
	}
	
	public static String getNetType(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null)
			return "";
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		int type = networkinfo.getSubtype();
		switch (type) {
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return "unknown";
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return "GPRS";
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return "EDGE";
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return "UMTS";
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return "CDMA: Either IS95A or IS95B";
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return "EVDO revision 0";
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return "EVDO revision A";
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return "1xRTT";
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return "HSDPA";
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return "HSUPA";
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return "HSPA";
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return "iDen";
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				return "EVDO revision B";
			default:
				return "unknown";
		}
	}
	
	public static String Md5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			return buf.toString().toUpperCase();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plainText.toUpperCase();
	}
	
	/**
	 * 返回当前时间的格式化字符串
	 * @param withMinutesAndSeconds
	 * @return
	 */
	public static String currentTimeString(boolean withMinutesAndSeconds) {
		if (withMinutesAndSeconds) {
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //格式化当前系统日期
			return dateFm.format(new java.util.Date());
		} else {
			SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日期
			return dateFm.format(new java.util.Date());
		}
	}
	
	/**
	 * 金额保留两位小数并加上人民币符号
	 * @param money
	 * @return
	 */
	public static String formatMoney(float money) {
		return String.format("￥%.2f", money);
	}

	/**
	 *  根据listviewitem的高度现实listview的高度
	 * @param listView listview item必须是LinearLayout
	 */
	public static void setListViewHeight(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();

		if(listAdapter == null) {

			return;

		}

		int totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {

			View listItem = listAdapter.getView(i, null, listView);

			listItem.measure(0, 0);

			totalHeight += listItem.getMeasuredHeight();

		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static boolean checkMobile(String mobile) {
		if (!TextUtils.isEmpty(mobile) && mobile.matches("^((\\+86)|(86))?((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[0-3,5-9]))\\d{8}$")) {
			return true;
		} else {
			return false;
		}
	}
}
