package com.aplex;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.aplex.Fragment.AboutFragment;
import com.aplex.Fragment.BuzzFragment;
import com.aplex.Fragment.ComSelectFragment;
import com.aplex.Fragment.GPIOFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2015/8/31 0031.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> listFragment;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);

        listFragment = new ArrayList<Fragment>();
        listFragment.add(new GPIOFragment());
        listFragment.add(new BuzzFragment());
        listFragment.add(new ComSelectFragment());
        listFragment.add(new AboutFragment());

    }

    @Override
    public int getCount() {
//        return PAGER_COUNT;
        return listFragment.size();
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

}

