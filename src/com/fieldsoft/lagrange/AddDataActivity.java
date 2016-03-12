package com.fieldsoft.lagrange;


import android.app.*;
import android.content.Intent;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.http.util.EncodingUtils;

import com.google.gson.Gson;

public class AddDataActivity extends Activity{
	
	private final static int REQUEST_CODE = 3; 
	public final static int RESULT_CODE = 2;


	//用于存放数据
	ArrayList<MyPoint> mData = new ArrayList<MyPoint>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adddata);
		
		//获取初始mData数据
		Intent intent = getIntent();
        Bundle bl = intent.getExtras();
        String str = bl.getString("mData");
        MyPoint[] Points = (new Gson()).fromJson(str, MyPoint[].class);
        this.mData.clear();
        for (int i = 0;i < Points.length;i ++){
        	this.mData.add(Points[i]);
        }
        this.ListViewRefresh();
	}
	
	/**
	 * 用于将mData填充到ListView的操作
	 */
	public void ListViewRefresh(){
		String[] StrArr = new String[this.mData.size()];
		for (int i = 0;i < this.mData.size();i ++){
			StrArr[i] = "X:" + this.mData.get(i).x + " Y:" + this.mData.get(i).y;
		}
		ListView mListView = (ListView)findViewById(R.id.listViewData);
		mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, StrArr));
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	public void Return(View srcView){
		this.finish();
	}
	
	public void Insert(View srcView){
		try{
			EditText EditX = (EditText) findViewById(R.id.editTextX);
			EditText EditY = (EditText) findViewById(R.id.editTextY);
			//判断该x是否已经被保存了，FX函数不允许一个x对应多个y值
			//获取x
			double X = Double.valueOf(EditX.getText().toString());
			for (int i = 0;i < this.mData.size();i ++){
				if (this.mData.get(i).x == X){
					Toast.makeText(this,"不允许一个X值对应多个Y！", Toast.LENGTH_LONG).show();
					return;
				}
			}
			this.mData.add(new MyPoint(X,Double.valueOf(EditY.getText().toString())));
			this.ListViewRefresh();
			EditX.setText("");
			EditY.setText("");
		}catch(Exception e){
			Toast.makeText(this,"输入数据非法！", Toast.LENGTH_LONG).show();
		}
	}

	public void AllSelect(View srcView){
		ListView mListView = (ListView)findViewById(R.id.listViewData);
		boolean DesValue;
		//判断是否全选 兼容api8
		boolean IsAllSelect = true;
		for (int i = 0;i < mListView.getCount();i ++){
			if (!mListView.isItemChecked(i)){
				IsAllSelect = false;
				break;
			}
		}
		if (IsAllSelect){
			DesValue = false;
		}
		else{
			DesValue = true;
		}
		for (int i = 0;i < mListView.getCount();i ++){
			mListView.setItemChecked(i,DesValue);
		}
	}
	
	public void Delete(View srcView){
		ListView mListView = (ListView)findViewById(R.id.listViewData);
		int dx = 0;
		for (int i = 0;i < mListView.getCount();i ++){
			if (mListView.isItemChecked(i)){
				this.mData.remove(i - dx);
				dx ++;
			}
		}
		this.ListViewRefresh();
	}
	
	public void Enter(View srcView){
		Intent intent = new Intent();
        intent.putExtra("mData", (new Gson()).toJson(this.mData)); 
        setResult(RESULT_CODE, intent);
        finish();
	}
	
	public void OpenFile(View srcView){
		Intent intent = new Intent(AddDataActivity.this, OpenFileDialogActivity.class);
		intent.putExtra("DefaultFilePath", Environment.getExternalStorageDirectory().getPath());
		intent.putExtra("DefaultFileName", "default.lag");
		intent.putExtra("Ext", ".lag");
		startActivityForResult(intent, AddDataActivity.REQUEST_CODE);
	}
	
	public void SaveFile(View srcView){
		Intent intent = new Intent(AddDataActivity.this, SaveFileDialogActivity.class);
		intent.putExtra("DefaultFilePath", Environment.getExternalStorageDirectory().getPath());
		intent.putExtra("DefaultFileName", "default.lag");
		intent.putExtra("Ext", ".lag");
		startActivityForResult(intent, AddDataActivity.REQUEST_CODE);
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
		//Toast.makeText(this,requestCode + "," + resultCode, Toast.LENGTH_LONG).show();
        if (requestCode == AddDataActivity.REQUEST_CODE)  
        {
            if (resultCode == SaveFileDialogActivity.RESULT_CODE)  
            {
            	//获取文件名
            	String FilePathName = data.getStringExtra("FilePathName");
                //保存
            	String Write_Str = (new Gson()).toJson(this.mData);
                try {
	            	FileOutputStream fout = new FileOutputStream(FilePathName);   
	                byte [] bytes = Write_Str.getBytes();
	                fout.write(bytes);   
					fout.close();
	                Toast.makeText(this,"保存到" + FilePathName, Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(this,"保存到" + FilePathName +"失败，可能是权限不够！", Toast.LENGTH_LONG).show();
				}
            }
            else if (resultCode == OpenFileDialogActivity.RESULT_CODE){
            	//获取文件名
            	String FilePathName = data.getStringExtra("FilePathName");
                //保存
            	String Read_Str;
                try {
                	FileInputStream fin = new FileInputStream(FilePathName);
                    int length = fin.available();
                    byte [] buffer = new byte[length];   
                    fin.read(buffer);    
                    Read_Str = EncodingUtils.getString(buffer, "UTF-8");
                    fin.close();
                    MyPoint[] Points = (new Gson()).fromJson(Read_Str, MyPoint[].class);
                    ArrayList<MyPoint> TempArr = new ArrayList<MyPoint>();
                    for (int i = 0;i < Points.length;i ++){
                    	TempArr.add(Points[i]);
                    }
                    this.mData = TempArr;
                    this.ListViewRefresh();
	                Toast.makeText(this,"读取成功！", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					Toast.makeText(this,"读取失败，可能是权限不够！", Toast.LENGTH_LONG).show();
				}
            }
        }  
    }

}
