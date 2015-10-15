package com.bredebasert.domain;

public abstract class Point {
	protected float[] point;
	
	public Point(float[] point) {
		this.point=point;
	}
	
	public Point(Point toCopy) {
		//This constructor serves to obtain a copy of a point
		point=toCopy.getPoint().clone();
	}
	
	public Point add(Point p) {
		for (int i = 0; i < point.length; i++) {
			point[i]=point[i]+p.get(i);
		}
		return this;
	}
	
	public Point subtract(Point p) {
		for (int i = 0; i < point.length; i++) {
			point[i]-=p.get(i);
		}
		return this;
	}
	
	public Point timesConstant(float c) {
		for (int i = 0; i < point.length; i++) {
			point[i]*=c;
		}
		return this;
	}
	
	public Point divideByConstant(float c) {
		for (int i = 0; i < point.length; i++) {
			point[i]/=c;
		}
		return this;
	}
	
	public float get(int i) {
		return point[i];
	}
	
	public void set(int i, float val) {
		point[i]=val;
	}
	
	public float[] getPoint() {
		return point;
	}
	
	public float norm() {
		float norm=0;
		for (int i = 0; i < point.length; i++) {
			norm+=(float)Math.pow(point[i],2);
		}
		return (float)Math.sqrt(norm);
	}
	
	public void print() {
		String output="[";
		for (int i = 0; i < (point.length-1); i++) {
			output+=point[i]+",";
		}
		output+=point[point.length-1]+"]";
		System.out.println(output);
	}
}
