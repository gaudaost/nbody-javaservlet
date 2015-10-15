package com.bredebasert.domain;

import java.util.Vector;

public class Octree {
	private Point3[] bounds;
	private Point3 center;
	private Octree[] children;
	private Vector<Point3> points;
	private Vector<Integer> indices;
	private Vector<Float> masses;
	private Point3 cm;
	private float mass;
	private boolean leaf;
	private float size;

	private void insertPoint(Point3 point, float mass, int index) {
		int i;
		Point3[] boundsNew = new Point3[2];
		if (point.get(2) > center.get(2)) {
			boundsNew[0] = new Point3(center);
			boundsNew[1] = new Point3(bounds[1]);
			if (point.get(0) > center.get(0) && point.get(1) > center.get(1)) {
				i = 0;
			} else if (point.get(0) <= center.get(0) && point.get(1) > center.get(1)) {
				i = 1;
				boundsNew[0].set(0, bounds[0].get(0));
				boundsNew[1].set(0, center.get(0));
			} else if (point.get(0) <= center.get(0) && point.get(1) <= center.get(1)) {
				i = 2;
				boundsNew[0].set(0, bounds[0].get(0));
				boundsNew[1].set(0, center.get(0));
				boundsNew[0].set(1, bounds[0].get(1));
				boundsNew[1].set(1, center.get(1));
			} else {
				i = 3;
				boundsNew[0].set(1, bounds[0].get(1));
				boundsNew[1].set(1, center.get(1));
			}
		} else {
			boundsNew[0] = new Point3(bounds[0]);
			boundsNew[1] = new Point3(center);
			if (point.get(0) > center.get(0) && point.get(1) > center.get(1)) {
				i = 4;
				boundsNew[1].set(0, bounds[1].get(0));
				boundsNew[0].set(0, center.get(0));
				boundsNew[1].set(1, bounds[1].get(1));
				boundsNew[0].set(1, center.get(1));
			} else if (point.get(0) <= center.get(0) && point.get(1) > center.get(1)) {
				i = 5;
				boundsNew[1].set(1, bounds[1].get(1));
				boundsNew[0].set(1, center.get(1));
			} else if (point.get(0) <= center.get(0) && point.get(1) <= center.get(1)) {
				i = 6;
			} else {
				i = 7;
				boundsNew[1].set(0, bounds[1].get(0));
				boundsNew[0].set(0, center.get(0));
			}
		}
		if (children[i] == null) {
			children[i] = new Octree(boundsNew, size / 2);
		}
		children[i].insert(point, mass, index);
	}

	public Octree(Point3[] bounds, float size) {
		this.size = size;
		this.bounds = bounds;
		children = new Octree[8];
		for (int i = 0; i < 8; ++i) {
			children[i] = null;
		}
		center = new Point3(bounds[0]);
		points=new Vector<Point3>();
		indices=new Vector<Integer>();
		masses=new Vector<Float>();
		center.add(bounds[1]).divideByConstant(2);
		cm = new Point3(0, 0, 0);
		mass = 0;
		leaf = true;
	}

	public void computeCenterOfMass() {
		if (leaf) {
			mass = masses.get(0);
			cm = new Point3(points.get(0));
			return;
		}
		mass = 0;
		for (int i = 0; i < points.size(); ++i) {
			mass += masses.get(i);
			Point3 copyOfPoint=new Point3(points.get(i));
			cm.add(copyOfPoint.timesConstant(masses.get(i)));
		}
		cm.divideByConstant(mass);
		for (int i = 0; i < 8; ++i) {
			if (children[i] != null) {
				children[i].computeCenterOfMass();
			}
		}
	}

	public void insert(Point3 point, float mass, int index) {
		points.add(point);
		masses.add(mass);
		indices.add(index);
		if (points.size() > 1) {
			leaf = false;
			if (points.size() == 2) {
				insertPoint(points.get(0), masses.get(0), indices.get(0));
			}
			insertPoint(point, mass, index);
		}
	}

	public Point3 computeAcc(Point3 pos, int index) {
		if (leaf && indices.get(0) == index) {
			return new Point3(0, 0, 0);
		}
		Point3 dist = new Point3(cm);
		dist.subtract(pos);
		float distNorm = dist.norm() + 0.1f;
		float thresh = 1;
		if (leaf || size / distNorm < thresh) {
			return (Point3) dist.timesConstant(mass / ((float) Math.pow(distNorm, 3)));
		}
		Point3 acc = new Point3(0, 0, 0);
		for (int i = 0; i < 8; ++i) {
			if (children[i] != null) {
				acc.add(children[i].computeAcc(pos, index));
			}
		}
		return acc;
	}
}
