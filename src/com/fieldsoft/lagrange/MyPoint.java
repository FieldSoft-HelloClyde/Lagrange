package com.fieldsoft.lagrange;

import java.io.Serializable;

public class MyPoint implements Serializable {
	private static final long serialVersionUID = 3387946629163553458L;
	public double x;
	public double y;
	public MyPoint(double x,double y){
		this.x = x;
		this.y = y;
	}
}
