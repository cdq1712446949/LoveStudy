package com.fedming.bottomnavigationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.fedming.bottomnavigationdemo.fourfragment.DynamicFragment;
import com.fedming.bottomnavigationdemo.fourfragment.HomeFragment;
import com.fedming.bottomnavigationdemo.fourfragment.MyFragment;
import com.fedming.bottomnavigationdemo.fourfragment.ShareFragment;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by bruce on 2016/11/1.
 * HomeActivity 主界面
 */

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private long mExitTime;
    private boolean isLogin=false;
    private App app;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        Log.i("---test-->","onCreat");
        Intent intent=getIntent();
        isLogin=intent.getBooleanExtra("islogin",isLogin);
        if (isLogin){
            Log.i("-----isLogin--->","true");
        }else {
            Log.i("-----isLogin--->","false");
        }

        app=new App();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_news:
                                viewPager.setCurrentItem(0);
                                setTitle("首页");
                                break;
                            case R.id.item_lib:
                                viewPager.setCurrentItem(1);
                                setTitle("分享");
                                break;
                            case R.id.item_find:
                                viewPager.setCurrentItem(2);
                                setTitle("问题动态");
                                break;
                            case R.id.item_more:
                                viewPager.setCurrentItem(3);
                                setTitle("个人中心");
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Log.i("int ",""+position);
                switch (position) {
                    case 0:
                        setTitle("首页");
                        break;
                    case 1:
                        setTitle("分享");
                        break;
                    case 2:
                        setTitle("问题动态");
                        break;
                    case 3:
                        setTitle("个人中心");
                        break;
                }
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //禁止ViewPager滑动
//        viewPager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new ShareFragment(isLogin));
        adapter.addFragment(new DynamicFragment());
        adapter.addFragment(new MyFragment(isLogin));
        viewPager.setAdapter(adapter);
    }

}