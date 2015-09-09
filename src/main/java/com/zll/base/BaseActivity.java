package com.zll.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zll.R;
import com.zll.util.ProgressBarUtil;

import java.lang.reflect.Field;

public abstract class BaseActivity extends ActionBarActivity {
	protected ActionBar myActionBar = null;
	
	protected TextView titleText = null;

	protected View mCustomView = null;

	protected Button rightButton = null;

	protected TextView rightText = null;

	protected abstract void initView();

	protected abstract void initData();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myActionBar = getSupportActionBar();
		if (myActionBar != null) {
			myActionBar.setTitle("");
			myActionBar.setDisplayShowCustomEnabled(true);
			myActionBar.setDisplayShowTitleEnabled(false);
			myActionBar.setDisplayHomeAsUpEnabled(false);
		}

		mCustomView = getLayoutInflater().inflate(R.layout.title, null);
		titleText = (TextView) mCustomView.findViewById(R.id.titleText);
		rightButton = (Button) mCustomView.findViewById(R.id.titleRightButton);
		rightText = (TextView) mCustomView.findViewById(R.id.titleRightText);
		// Calculate ActionBar height
		TypedValue tv = new TypedValue();
		if (this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, this.getResources().getDisplayMetrics());
			myActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, actionBarHeight));
		} else {
			myActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 100));
		}

		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		myActionBar.setCustomView(mCustomView, mP);
	}
}
