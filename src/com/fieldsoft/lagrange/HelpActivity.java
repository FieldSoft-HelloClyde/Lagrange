package com.fieldsoft.lagrange;


import android.app.*;
import android.os.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.*;

public class HelpActivity extends Activity{

	ImageView[] HelpImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		//创建4个ImageView
		HelpImageView = new ImageView[4];
		HelpImageView[0] = new ImageView(this);
		HelpImageView[1] = new ImageView(this);
		HelpImageView[2] = new ImageView(this);
		HelpImageView[3] = new ImageView(this);
		HelpImageView[0].setImageResource(R.drawable.img001);
		HelpImageView[1].setImageResource(R.drawable.img002);
		HelpImageView[2].setImageResource(R.drawable.img003);
		HelpImageView[3].setImageResource(R.drawable.img004);
		
		PagerAdapter mPagerAdapter = new PagerAdapter(){

            @Override
            //获取当前窗体界面数
            public int getCount() {
                return HelpImageView.length;
            }

            @Override
            //判断是否由对象生成界面
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0==arg1;
            }
            //是从ViewGroup中移出当前View
             public void destroyItem(View arg0, int arg1, Object arg2) {  
                    ((ViewPager) arg0).removeView(HelpImageView[arg1]);  
                }  
            
            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(View arg0, int arg1){
                ((ViewPager)arg0).addView(HelpImageView[arg1]);
                return HelpImageView[arg1];
            }
        };
        
        //绑定适配器
        ViewPager mViewPage = (ViewPager)findViewById(R.id.viewPager);
        mViewPage.setAdapter(mPagerAdapter);
	}

	public void Return(View srcView){
		this.finish();
	}
}
