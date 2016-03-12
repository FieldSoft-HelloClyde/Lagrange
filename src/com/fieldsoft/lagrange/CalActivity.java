package com.fieldsoft.lagrange;


import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.google.gson.Gson;

public class CalActivity extends Activity{
	
	public final static int RESULT_CODE = 6;

	//用于存放数据
	MyPoint CalPoint;
	
	Fxinterface mCalFun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cal);
		
		Intent intent = getIntent();
	    this.mCalFun = (Lagrange) getIntent().getSerializableExtra("mCalFun");
	}
	
	
	public void Return(View srcView){
		this.finish();
	}
	
	public void Cal(View srcView){
		try{
			EditText EditX = (EditText) findViewById(R.id.editTextX);
			TextView TextY = (TextView) findViewById(R.id.TextViewY);

			//获取x
			double X = Double.valueOf(EditX.getText().toString());
			//计算y
			double Y = this.mCalFun.Fx(X);
			this.CalPoint = new MyPoint(X,Y);
			TextY.setText(String.valueOf(Y));
		}catch(Exception e){
			Toast.makeText(this,"输入数据非法！", Toast.LENGTH_LONG).show();
		}
	}

	
	public void Enter(View srcView){
		Intent intent = new Intent();
        intent.putExtra("CalPoint", (new Gson()).toJson(this.CalPoint)); 
        setResult(this.RESULT_CODE, intent);
        finish();
	}
	

}
