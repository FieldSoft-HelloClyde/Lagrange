package com.fieldsoft.lagrange;

import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

import com.fieldsoft.lagrange.Comment.DisplayView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends ActionBarActivity {

	private final static int REQUEST_CODE=1; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DisplayView mDisplayView = (DisplayView)findViewById(R.id.DisplayView);
		Lagrange testLa = new Lagrange();
		testLa.SetData();
		mDisplayView.SetData(testLa.mData);
		mDisplayView.SetFx(testLa);
		
		SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE); 
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true); 
		Editor editor = sharedPreferences.edit(); 
		if (isFirstRun) 
		{
			Intent intent = new Intent(MainActivity.this, HelpActivity.class);
			startActivity(intent);
			editor.putBoolean("isFirstRun", false); 
			editor.commit(); 
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.MenuAddData) {
			Intent intent = new Intent(MainActivity.this, AddDataActivity.class);
			//传输数据
			Bundle bl = new Bundle();
			DisplayView mDisplayView = (DisplayView)findViewById(R.id.DisplayView);
			bl.putString("mData", (new Gson()).toJson(mDisplayView.getData()));
			intent.putExtras(bl);
			startActivityForResult(intent, REQUEST_CODE); 
			return true;
		}
		else if (id == R.id.MenuExit) {
			//this.finish();
			System.exit(0);
			return true;
		}
		else if (id == R.id.MenuContact) {
			//this.finish();
			Intent intent = new Intent();       
	        intent.setAction("android.intent.action.VIEW");   
	        Uri content_url = Uri.parse("http://www.helloclyde.com.cn/");
	        intent.setData(content_url); 
	        startActivity(intent);
			return true;
		}
		else if (id == R.id.MenuCal){
			Intent intent = new Intent(MainActivity.this, CalActivity.class);
			//传输数据
			DisplayView mDisplayView = (DisplayView)findViewById(R.id.DisplayView);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("mCalFun", (Lagrange)mDisplayView.getmCalFun());
			intent.putExtras(mBundle); 
			startActivityForResult(intent, this.REQUEST_CODE);
			return true;
		}
		else if (id == R.id.MenuHelp){
			Intent intent = new Intent(MainActivity.this, HelpActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
		//Toast.makeText(this,requestCode + "," + resultCode, Toast.LENGTH_LONG).show();
        if (requestCode==REQUEST_CODE)  
        {
            if (resultCode==AddDataActivity.RESULT_CODE)  
            {
                Bundle bundle=data.getExtras();
                String str=bundle.getString("mData");
                DisplayView mDisplayView = (DisplayView)findViewById(R.id.DisplayView);
                ArrayList<MyPoint> TempArr = new ArrayList<MyPoint>();
                MyPoint[] Points = (new Gson()).fromJson(str, MyPoint[].class);
                for (int i = 0;i < Points.length;i ++){
                	TempArr.add(Points[i]);
                }
                
                Lagrange testLa = new Lagrange();
        		testLa.SetData(TempArr);
        		mDisplayView.SetData(testLa.mData);
        		mDisplayView.SetFx(testLa);
                mDisplayView.SetData(testLa.mData);
                Toast.makeText(this,"数据已经更新！", Toast.LENGTH_LONG).show();
            }
            else if (resultCode == CalActivity.RESULT_CODE){
            	Bundle bundle=data.getExtras();
                String str=bundle.getString("CalPoint");
                DisplayView mDisplayView = (DisplayView)findViewById(R.id.DisplayView);
                MyPoint CalPoint = (new Gson()).fromJson(str, MyPoint.class);

        		mDisplayView.setCalPoint(CalPoint);
                Toast.makeText(this,"数据已经更新！", Toast.LENGTH_LONG).show();
            }
        }  
    } 
}
