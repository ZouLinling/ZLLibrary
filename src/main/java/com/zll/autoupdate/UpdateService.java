package com.zll.autoupdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import com.zll.R;
import com.zll.logs.Log;
import com.zll.util.Util;


/**
 * 
 * @author zou
 * @see
 * 提供自动更新功能，需要准备条件如下：
 * 1. 在Manifest中增加service声明        
 *  <service
 *           android:name="com.zll.autoupdate.UpdateService"
 *           android:enabled="true" >
 *  </service>
 * 2. 在Manifest增加权限
 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
 * 3. 在启动该服务之前，需要先调用setUrl 设置xmlUrl以提供包含更新信息的xml文件地址
 * 否则无法继续
 *
 */
public class UpdateService extends Service {

	private static final String LogTag = UpdateService.class.getSimpleName();
	private boolean hasUpdate = false;
	private int currentVersion;
	private String currentVersionName;
	private String newVersionName;
	private ScheduledExecutorService executor;
	private ProgressDialog pBar;
	private String localFileName ="appliaction.apk";
	private int percent = 0;
	private boolean downloading = false;
	private File file;

	private String downloadUrl;
	private double downloadSize;
	private Context context;
	// whether to show dialog when is the newest version
	private boolean flag = false;
	
	private static String xmlUrl = null;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Log.e(LogTag, "Receive a message");
			if (msg.what == 1) {
				showUpdateDialog();
			} else if (msg.what == 2) {
				showNewestDialog();
			}
		};
	};

	private void downLoadNewVersion() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				download(downloadUrl);
			}
		});
		t.start();
	}

	private void showNewestDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.updatetips);
		builder.setMessage(getResources().getString(
				R.string.newestVersionMessage));
		builder.setPositiveButton(R.string.positive,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		AlertDialog newAlert = builder.create();
		newAlert.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		newAlert.show();
	}

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.updatetips);
		// builder.setIcon(R.drawable.icon);
		builder.setMessage(getResources().getString(R.string.current_version)
				+ currentVersionName + "\n"
				+ getResources().getString(R.string.newest_version)
				+ newVersionName);
		builder.setPositiveButton(R.string.update,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						pBar = new ProgressDialog(UpdateService.this);
						pBar.setTitle(getString(R.string.updating));
						pBar.setMessage(getString(R.string.updating_message));
						pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						pBar.getWindow().setType(
								WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
						pBar.show();
						downLoadNewVersion();
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(R.string.no_update,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						UpdateService.this.stopSelf();
					}
				});
		AlertDialog alert = builder.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//
		alert.show();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.e(LogTag, "onBind()");
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (intent != null) {
			if (downloading) {
				makeToast(R.string.downloading_tips);
			} else {
				context = this;
				Bundle bun = intent.getExtras();
				if (bun != null) {
					flag = bun.getBoolean("flag");
				} else {
					flag = false;
				}
				try {
					currentVersion = getPackageManager().getPackageInfo(
							context.getPackageName(), 0).versionCode;
					currentVersionName = getPackageManager().getPackageInfo(
							context.getPackageName(), 0).versionName;
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				executor = Executors.newSingleThreadScheduledExecutor();
				executor.execute(new Runnable() {

					@Override
					public void run() {
						if (Util.detectNetwork(context)) {
							if (xmlUrl != null) {
								checkSoftUpdate(xmlUrl);
								if (hasUpdate) {
									Message m = new Message();
									m.what = 1;
									mHandler.sendMessage(m);
								} else if (flag) {
									Message m = new Message();
									m.what = 2;
									mHandler.sendMessage(m);
								}
							} else {
								makeToast(R.string.setUrl);
							}
						} else {
							makeToast(R.string.no_network);
						}
					}
				});
			}
		} else {
			Log.e(LogTag, "UpdateService is start by null intent!!!");
			this.stopSelf();
		}
	}

	public void checkSoftUpdate(String xmlUrl) {
		try {
			
			URL url = new URL(xmlUrl);
			String line;
			BufferedReader in;

			StringBuffer sb = new StringBuffer();
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();
			String s = sb.toString();
			SAXHttp sax = new SAXHttp();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(sax);
			reader.parse(new InputSource(new StringReader(s))); // 解析结果在下面

		} catch (Exception e) {
			// listener.isUpdate(false);
			Log.e(LogTag, "CheckUpdateThread error " + e.getMessage());
			e.printStackTrace();
		}
	}

	public class SAXHttp extends DefaultHandler {
		public String tagName;
		public String version, versioncode, size, download, md5;

		@Override
		public void startElement(String url, String localName, String qName,
				Attributes stt) {
			this.tagName = localName;
		}

		@Override
		public void endElement(String url, String localName, String qName) {

		}

		@Override
		public void characters(char[] ch, int start, int length) {
			if (tagName.equals("version")) { // 这里的tag就是xml文件里面定义的各个字段
				version = new String(ch, start, length);
				if (Integer.valueOf(version) >= (currentVersion + 1)) {
					hasUpdate = true;
				}
			} else if (tagName.equals("versioncode")) {
				versioncode = new String(ch, start, length);
				newVersionName = versioncode;
			} else if (tagName.equals("md5")) {
				md5 = new String(ch, start, length);
			} else if (tagName.equals("size")) {
				size = new String(ch, start, length);
				if (hasUpdate) {
					downloadSize = Long.parseLong(size);
				}
			} else if (tagName.equals("downloadurl")) {
				download = new String(ch, start, length);
				if (hasUpdate && null != download && !"".equals(download)) {
					downloadUrl = download;
				}
			}
			tagName = "";
		}
	}

	public void download(String downloadUrl) {
		Log.e(LogTag, "download start");
		downloading = true;
		URL url;
		String localPath;
		file = null;
		InputStream in = null;
		OutputStream out = null;

		int line;
		try {
			localPath = context.getApplicationContext().getFilesDir() + "/"
					+ localFileName;
			Log.e(LogTag, "localPath:" + localPath);
			file = new File(localPath);
			if (file.exists()) {
				file.delete();
			} else {
				file.createNewFile();
			}

			Runtime.getRuntime().exec("chmod 777 " + localPath);
			url = new URL(downloadUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			in = conn.getInputStream();
			// out = new FileOutputStream(file);
			out = context
					.openFileOutput(localFileName, Context.MODE_WORLD_READABLE);
			byte[] b = new byte[1024 * 2];
			int percentNow = 0;
			while ((line = in.read(b)) != -1) {
				out.write(b, 0, line);
				long len = file.length();
				percentNow = (int) (len * 100 / downloadSize);
				if (percentNow != percent) {
					percent = percentNow;
					Log.e(LogTag, "setPercent = " + percent);
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							pBar.setMessage(getResources().getString(
									R.string.have_down)
									+ percent
									+ getResources()
											.getString(R.string.percent));
						}
					});
				}
			}
			out.flush();
			conn.disconnect();
			in.close();
			out.close();
			downloading = false;
			Log.e(LogTag, "download finish");
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					pBar.cancel();
					install();
				}
			});
			// listener.downloadFinish(localPath);
		} catch (Exception e) {
			downloading = false;
			if (file.exists()) {
				file.delete();
			}
			// listener.downloadError();
			Log.e(LogTag, "download error " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void install() {
		if (!file.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + file.toString()),
				"application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);

	}
	
//	public String getXmlUrl() {
//		WebClient webClient = B2CApplication.getInstance().getWebClient();
//		String xml = webClient.getUpdateXmlPath();
//		if (xml == null) {
//			B2CApplication.makeToast(webClient.getErrMsg());
//			return null;
//		}
//		try {
//			String result = new JSONObject(xml).getString("JSONString");
//			return result;
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			B2CApplication.makeToast(e.getMessage());
//			return null;
//		}
//	}
	
	public static void setXmlUrl(String url) {
		xmlUrl = url;
	}
	
	private void makeToast(int resId) {
		Toast.makeText(context, getText(resId), Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopSelf();
	}

}
