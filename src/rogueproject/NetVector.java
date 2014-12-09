package rogueproject;

import java.io.Serializable;


/*
 * Hopefully going to be used to serialize the actor information to be sent across the internet.
 */
public class NetVector implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	float x;
	float y;
	double theta;
	
	public NetVector(){
		x = 0;
		y = 0;
	}
	
	public NetVector(float X, float Y, float t){
		x = X;
		y = Y;
		theta = t;
	}
	
	public void setX(float posX){
		x = posX;
	}
	
	public void setY(float posY){
		y = posY;
	}
	 public void setTheta(Double t){
		 theta = t;
	 }
	 
	 public double getTheta(){
		 return theta;
	 }
	 
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public String toString(){
		return "NetVector: [x = " + x + ", y = " + y + ", theta = " + theta + "]";
	}

}
