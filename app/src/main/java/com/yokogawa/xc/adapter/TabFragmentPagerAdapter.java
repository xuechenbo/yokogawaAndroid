package com.yokogawa.xc.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author liuchao
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mFragmentManager;
    private List<Fragment> mList;


    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        //显示第几个页面
        return mList.get(position);
    }

    @Override
    public int getCount() {
        //有几个页面
        return mList.size();
    }


    @Override
    public long getItemId(int position) {
        return mList.get(position).hashCode();

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
