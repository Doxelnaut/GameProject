package pistolcave;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Corey Amoruso
 * @author Ryan Bergquist
 * @author Zacharias Shufflebarger
 * 
 *	This file is part of Pistol Cave.
 *
 *  Pistol Cave is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Pistol Cave is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Pistol Cave.  If not, see <http://www.gnu.org/licenses/>.
 *
 *	Copyright 2014 Corey Amoruso, Ryan Bergquist, Zacharias Shufflebarger
 */
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
	ArrayList<NetVector>enemies = new ArrayList<NetVector>();

	
	clientState(){
		playerNewState = new NetVector();
	}
	
	clientState(int i){
		playerNum = i;
		playerNewState = new NetVector();

	}
	
	
}
