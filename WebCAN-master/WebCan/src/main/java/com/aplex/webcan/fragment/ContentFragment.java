package com.aplex.webcan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aplex.webcan.R;
import com.aplex.webcan.bean.CmdBean;
import com.aplex.webcan.bean.Constant;
import com.aplex.webcan.util.FragmentFactory;
import com.aplex.webcan.viewpagerindicator.TabPageIndicator;

//一个独立的fragment
public class ContentFragment extends Fragment{
	private LinearLayout mRootView;    //布局
	private TabPageIndicator mTabPageindicator;   //tab
	private ViewPager mViewPager;       //viewPager
	private String[] mViewPagerTabsData;   //显示为WebCAN的字符串,后面两个都屏蔽掉了
    private String TAG = "ContentFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    //在这里面起始啥都没干，因为我们代码的tab和viewpager添加了，但是基本没用
		initView(inflater);
		initLisetner();
		//这里干了点事，因为添加适配器了，适配器就会调用的界面
		initData();
		
		return mRootView;
	}

	private void initLisetner() {
		mViewPagerTabsData = getResources().getStringArray(R.array.viewpager_tabs);
	}

	private void initData() {
	    //适配器:在这里面启动了第一个界面
		FragmentPagerAdapter adapter = new TestAdapter(getActivity().getSupportFragmentManager());
		//将适配器加入到viewpager中
		mViewPager.setAdapter(adapter);
		//设置当页发生变化的时候的监听,啥都没有
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
        
        mTabPageindicator.setBackground(getResources().getDrawable(R.color.viewPagerTabBackground));
        //将viewpager加入到tab当中,没啥用
        mTabPageindicator.setViewPager(mViewPager);
	}

	private void initView(LayoutInflater inflater) {
	    //拿到ui_content根布局文件
		mRootView = (LinearLayout) inflater.inflate(R.layout.ui_content, null);
		//找到对应的tab和viewpager
		mTabPageindicator = (TabPageIndicator) mRootView.findViewById(R.id.Tabs);
		mViewPager = (ViewPager) mRootView.findViewById(R.id.main_vp);
	}

	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private CmdBean inflateCmdBean(int position) {
        CmdBean cmdBean = new CmdBean();
        switch (position) {
            case 0:
                cmdBean.CmdType = Constant.BUZZ_TYPE;
                cmdBean.isScoll = Constant.IS_SCROLL;
                break;

            case 1:
                cmdBean.CmdType = Constant.EEPROM_TYPE;
                cmdBean.isScoll = Constant.IS_SCROLL;
                break;
            default:
                break;
        }
        return cmdBean;
    }

    private class TestAdapter extends FragmentPagerAdapter {
        public TestAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        //在这个适配器里调用除了界面
        public Fragment getItem(int position) {
            //Log.d(TAG,"getItem:"+position);
            //为FragmentFactory类里面的静态方法getFragment
            return FragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            //Log.d(TAG,"getCount:"+mViewPagerTabsData.length);
            return mViewPagerTabsData.length;
        }

        @Override
        //显示在tab的标题
        public CharSequence getPageTitle(int position) {
            return mViewPagerTabsData[position % mViewPagerTabsData.length];
        }
    }
}
