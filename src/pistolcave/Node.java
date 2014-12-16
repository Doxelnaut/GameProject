package pistolcave;
/*
 * Node: used as vertices in Dijkstra's Algorithm
 */
public class Node {
	private int x; 
	private int y;
	private Node parent;
	private int cost;
	
	public Node(int tx, int ty){
		x = tx;
		y = ty;
		parent = null;
		cost = 1000;
	}
	
	public void setParent(Node n){
		parent = n;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public float[] getPosition(){
		float[] pos = new float[2];
		pos[0] = x;
		pos[1] = y;
		return pos;
	}
	
	public Node getParent(){
		return parent;
	}
	
	public void setCost(int c){
		cost = c;
	}
	
	public int getCost(){
		return cost;
	}
}