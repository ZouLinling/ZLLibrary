package com.zll.bitmap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


public class BitmapCache {
	private LruCache<String, Bitmap> mBitmapCache;

	// static private final String TAG = BitmapCache.class.getCanonicalName();
	public BitmapCache() {
		mBitmapCache = new LruCache<String, Bitmap>(1024 * 1024 * 3) { // by default use 3mb as a limit for the in memory Lrucache
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in bytes rather than number of items.
				int byteCount = 0;
//				if (Build.VERSION.SDK_INT < 12) {
//					byteCount = bitmap.getRowBytes() * bitmap.getHeight();
//				} else {
//					byteCount = bitmap.getByteCount();
//				}
				byteCount = bitmap.getRowBytes() * bitmap.getHeight();
				return byteCount;
			}
		};
	}

	public void addBitmap(String url, Bitmap b) {
		mBitmapCache.put(url, b);
	}

	public Bitmap getBitmap(String url) {
		if (url == null) {
			return null;
		}
		return mBitmapCache.get(url);
	}
}
