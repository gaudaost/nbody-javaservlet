package com.bredebasert.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bredebasert.domain.Integrator;
import com.bredebasert.domain.Point3;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@WebServlet(name = "testServlet", urlPatterns = { "/hello" }, initParams = {
		@WebInitParam(name = "simpleParam", value = "paramValue") })
public class Model extends HttpServlet {
	
	private float randomFloat(float min, float max) {
		return (float)Math.random()*(max-min)+min;
	}
	
	private Point3 randomPoint3(float min, float max) {
		float x=randomFloat(min, max);
		float y=randomFloat(min, max);
		float z=randomFloat(min, max);
		return new Point3(x,y,z);
	}
	
	private Vector<Point3> main() {
		Vector<Point3> points=new Vector<Point3>();
		Vector<Point3> vel=new Vector<Point3>();
		Vector<Float> radii=new Vector<Float>();
		float dt=0.1f;
		int nPoints=4000;
		for (int i = 0; i < nPoints; ++i) {
			points.add(randomPoint3(-500,500));
			vel.add(randomPoint3(-50,50));
			radii.add(randomFloat(0,20));
		}
		Integrator integrator=new Integrator(points,vel,radii,dt);
		integrator.mainLoop(1);
		return points;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
 
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
 
        Gson gson = new Gson(); 
        JsonObject myObj = new JsonObject();
		HttpSession session = request.getSession();
		if (session.getAttribute("count")==null) {
			session.setAttribute("count", 0);
		}
        int count = (Integer) session.getAttribute("count");
        session.setAttribute("count", count+1);
        JsonElement countryObj = gson.toJsonTree(main());
        myObj.addProperty("success", true);
        myObj.add("data", countryObj);
        myObj.add("count", gson.toJsonTree(count));
        out.println(myObj.toString());
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}