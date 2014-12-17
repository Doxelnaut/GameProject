package pistolcave;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import jig.Vector;

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
public class Connection implements Runnable {
	
	private Socket socket;
	String hostName;				//client name
	boolean quit = false;
	ObjectOutputStream socketOut;
	ObjectInputStream socketIn;
	PistolCaveGame PC;
	clientState playerState;
	
	
	Connection(Socket s, PistolCaveGame gs){
		socket = s;
		PC = gs;
	}

	@Override
	public void run() {
		
		//create player
		if(!PC.player1Connected){
			PC.player1Connected = true;
			PC.state.player.setPos(new Vector(2*PistolCaveGame.TILE_SIZE, 2*PistolCaveGame.TILE_SIZE));
			PC.state.player.setDirection(2);
			PC.state.firstPlayer = true;
		}
		
		else if(!PC.player2Connected){
			PC.player2Connected = true;
			PC.state.player2.setPos(new Vector(2*PistolCaveGame.TILE_SIZE, 2*PistolCaveGame.TILE_SIZE));
			PC.state.player2.setDirection(2);
			PC.state.secondPlayer= true;
		}
		
		else{
			//handle too many connections
		}
	
			try {
				estConn();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.print("Error connecting to client");
				}
			
			System.out.println("Connected to " + hostName);		
			
		//open object streams
	    try {
	      socketOut = new ObjectOutputStream(socket.getOutputStream());
	      socketIn = new ObjectInputStream(socket.getInputStream());
	    }
	    catch(IOException e){
			e.printStackTrace();
			System.out.println("Error opening streams");
	    }
	    
	    try {
			socketOut.writeObject(PC.state);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    while(!quit){
	    	//loop to write game state to client and get user input from client
		    	
			try {
				socketOut.reset();
				socketOut.writeObject(PC.state);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error Writing game state to client.");
			}
			
			try {
				playerState = (clientState) socketIn.readObject();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("Error reading clientState.");
				e.printStackTrace();
			}
				
			PC.update(playerState);
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	}
	
	private void estConn()throws IOException{
		
		InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
        hostName = address.getAddress().getHostAddress();
	}
}
