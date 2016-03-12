package com.fieldsoft.lagrange;

import java.io.Serializable;
import java.util.ArrayList;

public class Lagrange implements Fxinterface, Serializable{
	
	private static final long serialVersionUID = 6200226947290477095L;
	public ArrayList<MyPoint> mData;
	@Override
	public double Fx(double x) {
		double addResult = 0;
		int k = 0;
		for (k = 0;k < this.mData.size();k ++){
			double mulResult = 1;
			int j = 0;
			for (j = 0;j < this.mData.size();j ++){
				if (j == k) continue;
				mulResult *= (x - this.mData.get(j).x) / (this.mData.get(k).x - this.mData.get(j).x);
			}
			addResult += this.mData.get(k).y * mulResult;
		}
		return addResult;
	}
	
	public void SetData(){
		this.mData = new ArrayList<MyPoint>();
		double x[] = {0,10,20,30,40,50,60,70,80,90,100,110,120};
		double y[] = {5,1,7.5,3,4.5,8.8,15.5,6.5,-5,-10,-2,4.5,7};
		for (int i = 0;i < x.length;i ++){
			this.mData.add(new MyPoint(x[i],y[i]));
		}
	}
	
	public void SetData(ArrayList<MyPoint> mData){
		this.mData = mData;
	}
}
