package com.zll.phone;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class PhoneManager {

	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (null == imei)
			return "";
		else
			return imei;
	}

	public static String getSIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);
		String simei = tm.getSubscriberId();
		if (null == simei)
			return "";
		else
			return simei;
	}

	public static String getPhoneNum(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);
		String num = tm.getLine1Number();
		if (null == num)
			return "";
		else
			return num;
	}

	public static String getPhoneProvider(Context context) {
		String simei = getSIMEI(context);
		// IMSI号前�?�?60是国家，紧接�?���?�?0 02是中国移动，01是中国联通，03是中国电信�?
		if (simei.startsWith("4600001")) {
			return "中国联�?";
		} else if (simei.startsWith("4600002")) {
			return "中国移动";
		} else if (simei.startsWith("4600003")) {
			return "中国电信";
		} else {
			return "";
		}
	}

	public static boolean isNetWorkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = null;
		if (null != cm) {
			net = cm.getActiveNetworkInfo();
			if (net != null && net.isAvailable())
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
	
	public static boolean isWifiAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = null;
		if (null != cm) {
			net = cm.getActiveNetworkInfo();
			if (net != null && net.isAvailable() && net.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
}
