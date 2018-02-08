package com.aplex.webcan.util;

import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;

import com.aplex.webcan.fragment.LEDFragment;
import com.aplex.webcan.fragment.can.CanFragment;

public class FragmentFactory {
    //容器
	private static SparseArrayCompat<Fragment> mCaches = new SparseArrayCompat<Fragment>();

    public static Fragment getFragment(int position) {
        Fragment fragment = mCaches.get(position);

        //如果拿到了则返回
        if (fragment != null) {
            return fragment;
        }
        //没拿到就创建
        switch (position)
        {
            case 0:
                //只创建了一个界面
                fragment = new CanFragment();
                break;
            default:
                break;
        }

        // 存储
        mCaches.put(position, fragment);

        return fragment;
    }
}
