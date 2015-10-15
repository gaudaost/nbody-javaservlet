package com.bredebasert.domain;

public class Point3 extends Point {
	public Point3(Point3 toCopy) {
		super(toCopy);
	}
	
	public Point3(float x,float y,float z) {
		super(new float[3]);
		point[0]=x;
		point[1]=y;
		point[2]=z;
	}
}
