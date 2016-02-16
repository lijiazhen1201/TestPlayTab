# TestPlayTab

##  标题栏和下方的ViewPager实现联动

**自定义HorizontalScrollView**

	public class SyncHorizontalScrollView extends HorizontalScrollView {
	
	    private View view;
	    private ImageView leftImage;
	    private ImageView rightImage;
	    private int windowWitdh = 0;
	    private Activity mContext;
	
	    public void setSomeParam(View view, ImageView leftImage, ImageView rightImage, Activity mContext) {
	        this.mContext = mContext;
	        this.view = view;
	        this.leftImage = leftImage;
	        this.rightImage = rightImage;
	        DisplayMetrics dm = new DisplayMetrics();
	        this.mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
	        windowWitdh = dm.widthPixels;
	    }
	
	    public SyncHorizontalScrollView(Context context) {
	        super(context);
	        // TODO Auto-generated constructor stub
	    }
	
	    public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        // TODO Auto-generated constructor stub
	    }
	
	    // 显示和隐藏左右两边的箭头
	    public void showAndHideArrow() {
	        if (!mContext.isFinishing() && view != null) {
	            this.measure(0, 0);
	            if (windowWitdh >= this.getMeasuredWidth()) {
	                leftImage.setVisibility(View.GONE);
	                rightImage.setVisibility(View.GONE);
	            } else {
	                if (this.getLeft() == 0) {
	                    leftImage.setVisibility(View.GONE);
	                    rightImage.setVisibility(View.VISIBLE);
	                } else if (this.getRight() == this.getMeasuredWidth() - windowWitdh) {
	                    leftImage.setVisibility(View.VISIBLE);
	                    rightImage.setVisibility(View.GONE);
	                } else {
	                    leftImage.setVisibility(View.VISIBLE);
	                    rightImage.setVisibility(View.VISIBLE);
	                }
	            }
	        }
	    }
	
	    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	        super.onScrollChanged(l, t, oldl, oldt);
	        if (!mContext.isFinishing() && view != null && rightImage != null && leftImage != null) {
	            if (view.getWidth() <= windowWitdh) {
	                leftImage.setVisibility(View.GONE);
	                rightImage.setVisibility(View.GONE);
	            } else {
	                if (l == 0) {
	                    leftImage.setVisibility(View.GONE);
	                    rightImage.setVisibility(View.VISIBLE);
	                } else if (view.getWidth() - l == windowWitdh) {
	                    leftImage.setVisibility(View.VISIBLE);
	                    rightImage.setVisibility(View.GONE);
	                } else {
	                    leftImage.setVisibility(View.VISIBLE);
	                    rightImage.setVisibility(View.VISIBLE);
	                }
	            }
	        }
	    }
	}
	
布局文件中添加该控件

    <RelativeLayout
        android:id="@+id/rl_tab"
        android:layout_width="match_parent"
        android:layout_height="50dp">

        <com.zhenmei.testplaytab.view.SyncHorizontalScrollView
            android:id="@+id/mHsv"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:fadingEdge="none"
            android:scrollbars="none">

            <RelativeLayout
                android:id="@+id/rl_item"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_gravity="top">

                <RadioGroup
                    android:id="@+id/rg_nav_content"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_alignParentTop="true"
                    android:orientation="horizontal" />

                <ImageView
                    android:id="@+id/iv_nav_content"
                    android:layout_width="1dp"
                    android:layout_height="1dp"
                    android:layout_alignParentBottom="true"
                    android:background="@android:color/holo_blue_light" />
            </RelativeLayout>
        </com.zhenmei.testplaytab.view.SyncHorizontalScrollView>

        <ImageView
            android:id="@+id/iv_nav_left"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentLeft="true"
            android:layout_centerVertical="true"
            android:src="@mipmap/iv_navagation_scroll_left"
            android:visibility="gone" />

        <ImageView
            android:id="@+id/iv_nav_right"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_centerVertical="true"
            android:src="@mipmap/iv_navagation_scroll_right"
            android:visibility="visible" />
    </RelativeLayout>
    
    
设置各个控件

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
        
添加tab内的按钮

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
        
设置ViewPager和RadioGroup的监听，实现联动

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