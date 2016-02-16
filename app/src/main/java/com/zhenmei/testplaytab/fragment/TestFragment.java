package com.zhenmei.testplaytab.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhenmei.testplaytab.R;

/**
 * Created by zhenmei on 16/2/15.
 */
public class TestFragment extends Fragment {

    private View v;
    private TextView tvFragment;
    private int index;

    public TestFragment(int index) {
        this.index = index;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_test, null);
        tvFragment = (TextView) v.findViewById(R.id.tv_fragment);
        tvFragment.setText("TestFragment" + index);
        return v;
    }
}
