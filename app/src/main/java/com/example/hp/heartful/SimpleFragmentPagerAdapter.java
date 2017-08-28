package com.example.hp.heartful;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by HP INDIA on 08-Apr-17.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    public SimpleFragmentPagerAdapter(Context context , FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.tab_one);
        } else if (position == 1) {
            return mContext.getString(R.string.tab_two);
        } else  {
            return mContext.getString(R.string.tab_three);
        }
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FragmentOne();
        } else if (position == 1){
            return new FragmentTwo();
        } else {
            return new FragmentThree();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
