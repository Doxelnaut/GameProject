package rogueproject;


/*
 * Hopefully going to be used to serialize the actor information to be sent across the internet.
 */
public class NetVector {
	double x;
	double y;
	double theta;
	
	NetVector(double X, double Y, double t){
		x = X;
		y = Y;
		theta = t;
	}
	
	public String toString(){
		return "NetVector: [x = " + x + ", y = " + y + ", theta = " + theta + "]";
	}

}
