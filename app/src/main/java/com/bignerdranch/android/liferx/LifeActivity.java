package com.bignerdranch.android.liferx;

import android.support.v4.app.Fragment;

public class LifeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LifeFragment();
    }

}
