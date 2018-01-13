package com.project.me.ntnu.cadcamtrashcreatorcatcher.UI;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.project.me.ntnu.cadcamtrashcreatorcatcher.IMainActivityAction;

/**
 * Created by SPLAB_Note on 2018/1/13.
 */

public class PagerAdapterOption extends FragmentPagerAdapter implements IPagerAdapterOption {

    private IMainActivityAction iMainActivityAction = null;

    private FragmentOptionData fragmentOptionData = null;

    public PagerAdapterOption(IMainActivityAction iMainActivityAction, FragmentManager fm) {
        super(fm);
        this.iMainActivityAction = iMainActivityAction;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragmentOptionData = new FragmentOptionData();
                if(iMainActivityAction!=null)
                    fragmentOptionData.iMainActivityAction = iMainActivityAction;
                fragment = fragmentOptionData;
                break;
            case 1:
                fragment = new FragmentOptionPictureDisplay();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public void refreshEmailData(String myName, String myPassword, String masterName) {
        if(fragmentOptionData==null)
            return;
        fragmentOptionData.setUserMailName(myName);
        fragmentOptionData.setUserMailPassword(myPassword);
        fragmentOptionData.setMasterMailName(masterName);
    }
}
