package rogueproject;

import java.io.Serializable;
import java.util.ArrayList;

import jig.Vector;

public class clientState implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	//represents the clients players wanted state, will be checked by server to make sure everything is ok (collison detection).
	//If this state is ok with the server, the server will change gameState to reflect these changes.
	NetVector playerNewState;
	
	int playerNum = 0;
	int delta;
	ArrayList<NetVector> bullets = new ArrayList<NetVector>();
	
	clientState(){
		playerNewState = new NetVector();
	}
	
	clientState(int i){
		playerNum = i;
		playerNewState = new NetVector();

	}
	
	
}
