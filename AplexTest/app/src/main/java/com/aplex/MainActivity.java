package com.aplex;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * Created by Coder-pig on 2015/8/28 0028.
 */
public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    //UI Objects
    private TextView txt_topbar;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_gpio;
    private RadioButton rb_buzzer;
    private RadioButton rb_comSelect;
    private RadioButton rb_about;
    private ViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_gpio.setChecked(true);
    }

    private void bindViews() {
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_gpio = (RadioButton) findViewById(R.id.rb_gpio);
        rb_buzzer = (RadioButton) findViewById(R.id.rb_buzzer);
        rb_comSelect = (RadioButton) findViewById(R.id.rb_comSelect);
        rb_about = (RadioButton) findViewById(R.id.rb_about);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager) findViewById(R.id.vpager);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_gpio:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_buzzer:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_comSelect:
                vpager.setCurrentItem(PAGE_THREE);
                break;
            case R.id.rb_about:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_gpio.setChecked(true);
                    txt_topbar.setText(rb_gpio.getText().toString()+"测试");
                    break;
                case PAGE_TWO:
                    rb_buzzer.setChecked(true);
                    txt_topbar.setText(rb_buzzer.getText().toString()+"测试");
                    break;
                case PAGE_THREE:
                    rb_comSelect.setChecked(true);
                    txt_topbar.setText(rb_comSelect.getText().toString()+"测试");
                    break;
                case PAGE_FOUR:
                    rb_about.setChecked(true);
                    txt_topbar.setText(rb_about.getText().toString());
                    break;
            }
        }
    }
}
