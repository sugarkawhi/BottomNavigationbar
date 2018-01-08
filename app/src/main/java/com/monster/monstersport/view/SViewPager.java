package com.monster.monstersport.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SViewPager extends ViewPager {

	private boolean canScroll;

	public SViewPager(Context context) {
		super(context);
		canScroll = false;
	}

	public SViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		canScroll = false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (canScroll) {
			try {
				return super.onInterceptTouchEvent(ev);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (canScroll) {
			return super.onTouchEvent(event);
		}
		return false;
	}


	//去除页面切换时的滑动翻页效果
	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {

		super.setCurrentItem(item, smoothScroll);
	}

	@Override
	public void setCurrentItem(int item) {

		super.setCurrentItem(item, false);
	}

	public void toggleLock() {
		canScroll = !canScroll;
	}

	public void setCanScroll(boolean canScroll) {
		this.canScroll = canScroll;
	}

	public boolean isCanScroll() {
		return canScroll;
	}

}
