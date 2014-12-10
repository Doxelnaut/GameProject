package rogueproject;

import java.io.Serializable;

import jig.Vector;


/*
 * Hopefully going to be used to serialize the actor information to be sent across the internet.
 */
public class NetVector implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Vector currentPos;
	int direction;
	boolean crouched = false;
	boolean attacking = false;
	
	
	public NetVector(){
	}
	
	public NetVector(float X, float Y, int d){
		currentPos = new Vector(X,Y);
		direction = d;
	}
	
	public void setPos(Vector v){
		currentPos = v;
	}
	
	public Vector getPos(){
		return currentPos;
	}
	
	public void setDirection(int d){
		direction = d;
	}
	
	public int getDirection(){
		return direction;
	}
	
	public void setCrouched(boolean c){
		crouched = c;
	}
	
	public boolean getCrouched(){
		return crouched;
	}
}
