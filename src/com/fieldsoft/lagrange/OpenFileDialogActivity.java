package com.fieldsoft.lagrange;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.Gson;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OpenFileDialogActivity extends Activity{
	
	String DefaultFilePath;
	String DefaultFileName;
	ArrayList<File> FileList = new ArrayList<File>();
	File FileNow;
	String Ext;
	
	public final static int RESULT_CODE = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openfiledialog);
		
		//获取参数
		Intent intent = getIntent();
		DefaultFilePath = intent.getStringExtra("DefaultFilePath");
		DefaultFileName = intent.getStringExtra("DefaultFileName");
		this.Ext = intent.getStringExtra("Ext");

		//Toast.makeText(this,DefaultFilePath + "," + DefaultFileName, Toast.LENGTH_LONG).show();
		//Toast.makeText(this,(new File(DefaultFilePath)).toString(), Toast.LENGTH_LONG).show();
		//
		
		this.FileNow = new File(DefaultFilePath);
		this.RefreshFileList();
		//
		TextView EditFileName = (TextView)findViewById(R.id.TextFileName);
		EditFileName.setText(DefaultFileName);
		//设置ListView单击事件
		ListView mListView = (ListView)findViewById(R.id.FileList);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (FileList.get(arg2).isDirectory()){
					FileNow = FileList.get(arg2);
					RefreshFileList();
				}
				else{
					TextView EditFileName = (TextView)findViewById(R.id.TextFileName);
					EditFileName.setText(FileList.get(arg2).getName());
				}
			}
			
		});
	}
	
	protected void RefreshFileList(){
		//将这些文件名加入listview
		this.FileList.clear();
		File[] TempFiles = this.FileNow.listFiles();
		if (TempFiles != null){
			for (int i = 0;i < TempFiles.length;i ++){
				if (TempFiles[i].isDirectory()){
					this.FileList.add(TempFiles[i]);
				}
				else{
					if (TempFiles[i].getName().endsWith(this.Ext)){
						this.FileList.add(TempFiles[i]);
					}
				}
			}
			//赋值给listView
			String[] TempStrArr = new String[this.FileList.size()];
			for (int i = 0;i < TempStrArr.length;i ++){
				TempStrArr[i] = this.FileList.get(i).isDirectory() ? "[" + this.FileList.get(i).getName() + "]" : this.FileList.get(i).getName();
			}
			ListView mListView = (ListView)findViewById(R.id.FileList);
			mListView.setAdapter(new ArrayAdapter<String>(this,
	                android.R.layout.simple_list_item_1, TempStrArr));
		}
		else{
			Toast.makeText(this,"权限不够！", Toast.LENGTH_LONG).show();
			if (this.FileNow.getParentFile() != null){
				this.FileNow = this.FileNow.getParentFile();
			}
			else{
				this.FileNow = new File(DefaultFilePath);
			}
			this.RefreshFileList();
		}
	}
	
	public void Return (View srcView){
		if (this.FileNow.getParentFile() != null){
			this.FileNow = this.FileNow.getParentFile();
			this.RefreshFileList();
		}
	}
	
	public void Cancel(View srcView){
		this.finish();
	}
	
	public void Enter (View srcView){
		Intent intent = new Intent();
		TextView EditFileName = (TextView)findViewById(R.id.TextFileName);
        intent.putExtra("FilePathName",this.FileNow.getAbsolutePath() + "/" + EditFileName.getText()); 
        setResult(OpenFileDialogActivity.RESULT_CODE, intent);
        this.finish();
	}
}
