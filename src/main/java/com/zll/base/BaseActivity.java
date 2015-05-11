package com.zll.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zll.R;

public class BaseActivity extends ActionBarActivity {

	protected ActionBar myActionBar = null;
	
	protected TextView titleText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myActionBar = getSupportActionBar();
		myActionBar.setDisplayShowCustomEnabled(true);
		myActionBar.setDisplayShowTitleEnabled(false);
		View mCustomView = getLayoutInflater().inflate(R.layout.actionbar_custom_title, null);
		titleText = (TextView) mCustomView.findViewById(R.id.actionbar_title);
		myActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
		myActionBar.setCustomView(mCustomView, mP);
	}
	
}
