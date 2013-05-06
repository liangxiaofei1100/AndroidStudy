package com.example.alex.view;

import java.util.ArrayList;
import java.util.List;

import com.example.alex.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ViewPagerFragmentDemo extends FragmentActivity {
	List<Fragment> fragmentList = new ArrayList<Fragment>();
	List<String> titleList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.viewpager_demo);

		initView();
	}

	private void initView() {
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_demo_viewpager);

		ViewPageFragment fragment1 = new ViewPageFragment();
		fragment1.setLayout(R.layout.viewpager_demo_page1);
		fragmentList.add(fragment1);

		ViewPageFragment fragment2 = new ViewPageFragment();
		fragment2.setLayout(R.layout.viewpager_demo_page2);
		fragmentList.add(fragment2);

		ViewPageFragment fragment3 = new ViewPageFragment();
		fragment3.setLayout(R.layout.viewpager_demo_page3);
		fragmentList.add(fragment3);

		titleList.add("page1");
		titleList.add("page2");
		titleList.add("page3");

		MyPageAdapter adapter = new MyPageAdapter(getSupportFragmentManager(),
				fragmentList, titleList);
		viewPager.setAdapter(adapter);

	}

	class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> mFragmentList;
		private List<String> mTiltleList;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragmentList,
				List<String> titleList) {
			super(fm);
			mFragmentList = fragmentList;
			mTiltleList = titleList;
		}

		@Override
		public Fragment getItem(int arg0) {
			return (mFragmentList == null || mFragmentList.size() == 0) ? null
					: mFragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return mFragmentList == null ? 0 : mFragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return (mTiltleList == null || mTiltleList.size() <= position) ? ""
					: mTiltleList.get(position);
		}
	}
}
