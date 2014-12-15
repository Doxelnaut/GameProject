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
public class GameState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//int[][] map;
	NetVector player;
	NetVector player2;
	boolean secondPlayer = false;
	boolean firstPlayer = false;
	ArrayList<NetVector> bullets = new ArrayList<NetVector>();
	
	public GameState(){
		
		//map = new int[100][100];
		player = new NetVector();
		player2 = new NetVector();
		return;
	}
	
	/*public String toString(){
		return "GameState: [ bullets =" + player + "]"; 
	}*/
	
}
