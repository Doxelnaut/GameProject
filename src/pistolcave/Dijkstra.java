package pistolcave;

import java.util.ArrayList;
import java.util.Iterator;

public class Dijkstra {
	Node[][] G = new Node[100][100];
	int[][] weights = new int[100][100];
	int infinity = 9999;
	
	public void buildGraph(int[] source){
		ArrayList<Node> Q = new ArrayList<Node>();
		for(int i = 0; i < 100; i++){
			for(int j = 0; j < 100; j++){
				G[j][i] = new Node(j,i);   	//init graph
				if(PistolCaveGame.map[j][i] != 0){
					G[j][i].setCost(infinity);
					weights[j][i] = infinity;
				}
				Q.add(G[j][i]);			//add new node to Q (unvisited list)
			}
		}
		Node start = G[source[0]][source[1]];
		weights[source[0]][source[1]] = 0;
		start.setCost(0);
		
		while(Q.size()>1){
			Iterator<Node> iter = Q.iterator();
			Node u = (Node) iter.next();
			Node temp = (Node) iter.next();
			for(int i = 0; i < Q.size()-2; i++){
				if(u.getCost() > temp.getCost()){
					u = temp;
				}
				temp = (Node) iter.next();
			}
			iter = Q.iterator();
			temp = iter.next();
			
			while(temp.getX() != u.getX() || temp.getY() != u.getY()){
				temp = iter.next();
			}
			iter.remove();
			
			iter = Q.iterator();
			temp = iter.next();
			for(int i = 0; i < Q.size()-1; i++){
				if(isNeighbor(temp,u)){
					int dist = u.getCost() + weights[temp.getX()][temp.getY()];
					if(dist < temp.getCost()){
						temp.setCost(dist);
						temp.setParent(u);
					}
				}
				temp = iter.next();
			}
		}
	}
//--------------------------------------------------------------------------------------
/*
 * Checks if the two nodes are neighboring each other
 */
	public boolean isNeighbor(Node a, Node b){
		float ax = a.getX();
		float ay = a.getY();
		float bx = b.getX();
		float by = b.getY();
		
		if(ax == bx-1 && (ay == by || ay == by-1 || ay == by +1)){
			return true;
		}
		else if(ax == bx+1 && (ay == by || ay == by-1 || ay == by +1)){
			return true;
		}
		else if(ax == bx && (ay == by || ay == by-1 || ay == by +1)){
			return true;
		}
		return false;
	}
}
