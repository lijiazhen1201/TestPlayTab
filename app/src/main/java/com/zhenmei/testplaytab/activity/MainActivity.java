package com.zhenmei.testplaytab.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.zhenmei.testplaytab.R;
import com.zhenmei.testplaytab.adapter.ViewPagerAdapter;
import com.zhenmei.testplaytab.fragment.TestFragment;
import com.zhenmei.testplaytab.view.SyncHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenmei on 16/2/15.
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    //tab的布局
    private RelativeLayout rlItem;
    //左右箭头
    private ImageView ivNavLeft;
    private ImageView ivNavRight;
    //自定义的HorizontalScrollView
    private SyncHorizontalScrollView mHsv;
    //HorizontalScrollView中的控件
    private RadioGroup rgNavContent;
    private ImageView ivNavContent;
    //主ViewPager
    private ViewPager vpMain;

    private void initView() {
        rlItem = (RelativeLayout) findViewById(R.id.rl_item);
        ivNavLeft = (ImageView) findViewById(R.id.iv_nav_left);
        ivNavRight = (ImageView) findViewById(R.id.iv_nav_right);
        mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);
        rgNavContent = (RadioGroup) findViewById(R.id.rg_nav_content);
        ivNavContent = (ImageView) findViewById(R.id.iv_nav_content);
        vpMain = (ViewPager) findViewById(R.id.vp_main);

        initData();
    }

    //tab的标题
    private String[] tabTitle = {"页面1", "页面2", "页面3", "页面4", "页面5", "页面6", "页面7", "页面8", "页面9", "页面10"};
    //一页显示的tab个数
    private int tabCount;

    private void initData() {
        //获取屏幕的高度和宽度
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int window_height = metric.heightPixels;
        int window_width = metric.widthPixels;
        //设置一页显示4个tab
        tabCount = window_width / 4;

        //初始化滑动下标的宽
        ViewGroup.LayoutParams params = ivNavContent.getLayoutParams();
        params.width = tabCount;
        ivNavContent.setLayoutParams(params);

        //添加tab栏
        mHsv.setSomeParam(rlItem, ivNavLeft, ivNavRight, this);

        //设置ViewPager
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < tabTitle.length; i++) {
            TestFragment test = new TestFragment(i + 1);
            list.add(test);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(fm, list);
        vpMain.setAdapter(adapter);

        initNavigationHSV();
    }

    //布局管理器
    private LayoutInflater inflater;

    private void initNavigationHSV() {
        //获取布局管理器
        inflater = LayoutInflater.from(this);
        //inflater=(LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);

        //清空RadioGroup
        rgNavContent.removeAllViews();
        //循环添加RadioButton
        for (int i = 0; i < tabTitle.length; i++) {
            RadioButton rb = (RadioButton) inflater.inflate(R.layout.sync_nav_radiogroup_item, null);
            rb.setId(i);
            rb.setText(tabTitle[i]);
            rb.setLayoutParams(new ViewGroup.LayoutParams(tabCount, ViewGroup.LayoutParams.MATCH_PARENT));
            rgNavContent.addView(rb);
        }

        setListener();
    }

    private int currentIndicatorLeft = 0;

    private void setListener() {

        vpMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (rgNavContent != null && rgNavContent.getChildCount() > position) {
                    ((RadioButton) rgNavContent.getChildAt(position)).performClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rgNavContent.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rgNavContent.getChildAt(checkedId) != null) {
                    TranslateAnimation animation = new TranslateAnimation(
                            currentIndicatorLeft,
                            ((RadioButton) rgNavContent.getChildAt(checkedId)).getLeft(), 0f, 0f);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setDuration(100);
                    animation.setFillAfter(true);

                    //执行位移动画
                    ivNavContent.startAnimation(animation);
                    //ViewPager 跟随一起 切换
                    vpMain.setCurrentItem(checkedId);

                    //记录当前 下标的距最左侧的 距离
                    currentIndicatorLeft = ((RadioButton) rgNavContent.getChildAt(checkedId)).getLeft();

                    mHsv.smoothScrollTo(
                            (checkedId > 1 ? ((RadioButton) rgNavContent.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rgNavContent.getChildAt(2)).getLeft(), 0);

                }
            }
        });

    }
}
