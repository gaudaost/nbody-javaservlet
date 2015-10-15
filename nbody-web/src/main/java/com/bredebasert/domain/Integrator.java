package com.bredebasert.domain;

import java.util.Vector;

public class Integrator {
	private Vector<Point3> points;
	private Vector<Point3> pointsOld;
	private Vector<Point3> vel;
	private Vector<Float> radii;
	private Vector<Float> masses;
	private float dt;
	private boolean firstIteration;

	private float computeBoundsAndSize(Point3[] bounds) {
		float min = points.get(0).get(0);
		float max = min;
		for (int i = 0; i < points.size(); i++) {
			for (int j = 0; j < 3; j++) {
				if (points.get(i).get(j) < min) {
					min = points.get(i).get(j);
				}
				if (points.get(i).get(j) > max) {
					max = points.get(i).get(j);
				}
			}
		}
		bounds[0] = new Point3(min, min, min);
		bounds[1] = new Point3(max, max, max);
		return max - min;
	}

	private void oneStep() {
		Point3[] bounds = new Point3[2];
		float size = computeBoundsAndSize(bounds);
		Octree tree = new Octree(bounds, size);
		for (int i = 0; i < points.size(); i++) {
			tree.insert(points.get(i), masses.get(i), i);
		}
		tree.computeCenterOfMass();
		Vector<Point3> acc = new Vector<Point3>();
		for (int i = 0; i < points.size(); i++) {
			acc.add(tree.computeAcc(points.get(i), i));
		}
		updatePositions(acc);
	}

	private void updatePositions(Vector<Point3> acc) {
		for (int i = 0; i < points.size(); i++) {
			if (firstIteration) {
				pointsOld.add(new Point3(points.get(i)));
				points.get(i).add(vel.get(i).timesConstant(dt))
						.add(acc.get(i).timesConstant(0.5f * (float) Math.pow(dt, 2)));
			} else {
				Point3 tmp = new Point3(points.get(i));
				points.get(i).add(points.get(i)).subtract(pointsOld.get(i))
						.add(acc.get(i).timesConstant((float) Math.pow(dt, 2)));
				pointsOld.set(i, tmp);
			}
		}
		if (firstIteration) {
			firstIteration = false;
		}
	}

	public Integrator(Vector<Point3> points, Vector<Point3> vel, Vector<Float> radii, float dt) {
		this.points = points;
		this.pointsOld=new Vector<Point3>();
		this.vel = vel;
		this.radii = radii;
		this.masses=new Vector<Float>();
		for (int i = 0; i < points.size(); i++) {
			this.masses.add((float) Math.pow(radii.get(i), 3));
		}
		this.firstIteration = true;
		this.dt = dt;
	}

	public void mainLoop(int n) {
		for (int i = 0; i < n; i++) {
			oneStep();
		}
	}
}
