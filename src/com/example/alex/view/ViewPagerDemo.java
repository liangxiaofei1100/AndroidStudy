package com.example.alex.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.R;

public class ViewPagerDemo extends Activity {
	private static final String TAG = "ViewPagerDemo";
	private View mPage1, mPage2, mPage3;
	private ViewPager mViewPager;
	private PagerTitleStrip pagerTitleStrip;
	private PagerTabStrip pagerTabStrip;
	private List<View> mPageList;
	private List<String> mTitleList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_demo);
		initView();
	}

	private void initView() {
		//view pager
		mViewPager = (ViewPager) findViewById(R.id.viewpager_demo_viewpager);
		pagerTabStrip = (PagerTabStrip) findViewById(R.id.viewpager_demo_pagertab);
		pagerTabStrip.setTabIndicatorColor(Color.BLUE);
		pagerTabStrip.setDrawFullUnderline(false);
		pagerTabStrip.setBackgroundColor(Color.GRAY);
		pagerTabStrip.setTextSpacing(50);

		// page list
		LayoutInflater inflater = getLayoutInflater();
		mPage1 = inflater.inflate(R.layout.viewpager_demo_page1, null);
		mPage2 = inflater.inflate(R.layout.viewpager_demo_page2, null);
		mPage3 = inflater.inflate(R.layout.viewpager_demo_page3, null);
		mPageList = new ArrayList<View>();
		mPageList.add(mPage1);
		mPageList.add(mPage2);
		mPageList.add(mPage3);
		
		// title list
		mTitleList = new ArrayList<String>();// 每个页面的Title数据
		mTitleList.add("page1");
		mTitleList.add("page2");
		mTitleList.add("page3");
		
		PagerAdapterDemo adapter = new PagerAdapterDemo(mPageList, mTitleList);
		mViewPager.setAdapter(adapter);
	}

	class PagerAdapterDemo extends PagerAdapter {
		private List<View> mPages;
		private List<String> mTitles;

		public PagerAdapterDemo(List<View> pages, List<String> titles) {
			Log.d(TAG, "pages size: " + pages.size());
			for (View view : pages) {
				Log.d(TAG, "Page: " + view);
			}
			mPages = pages;
			mTitles = titles;
		}

		@Override
		public int getCount() {
			return mPages.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mPages.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTitles.get(position);
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mPages.get(position), 0);
			return mPages.get(position);
		}

	}
}
