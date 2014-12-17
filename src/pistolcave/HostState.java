package pistolcave;



import org.newdawn.slick.GameContainer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import jig.ResourceManager;
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
/*
 * Class that listens for incoming connections from clients. It also displays relevant server information 
 * such as server ip address and connected clients ip addresses (eventually chosen alias).
 */
public class HostState extends BasicGameState {
	
	ArrayList<Connection> connections;  //list of currently connected clients
	ServerSocket ss = null;
	PistolCaveGame PC;
	int renderWait = 0;					//used in update to skip over first call to update since it waits for client connections (needed to render the new screen).
	Button hostButton;
	Button client1Button;
	Button client2Button;
	Button hostAddressButton;
	Button clientAddressButton;
	Button clientAddress2Button;
	String hostAddress;
	Button hostAddress1Button;
	
	public void init(GameContainer container, StateBasedGame PC)
			throws SlickException {
	}
	
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		connections = new ArrayList<Connection>();
		PC = (PistolCaveGame) game;
		
		//create buttons (labels)
		hostButton = new Button("Hosting",
				(PC.ScreenWidth * 0.5f), (PC.ScreenHeight * 0.1f), 50);
		client1Button = new Button("Client1:",
				(PC.ScreenWidth * 0.3f), (PC.ScreenHeight * 0.43f), 25);
		client2Button = new Button("Client2:",
				(PC.ScreenWidth * 0.3f), (PC.ScreenHeight * 0.54f), 25);
		hostAddressButton = new Button("Host Adress:",
				(PC.ScreenWidth * 0.3f), (PC.ScreenHeight * 0.28f), 30);
		
					
	    //create socket on the server to listen on.
		try {
			ss = new ServerSocket(1666);
			System.out.println("Created server socket.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error creating server socket.");
		}
		
		
		//does not work on linux thus far feel free to try on other OS and let me know
		//****************************************************************************************
		//Gets local IP address
		/*try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//******************************************************************************************
		
		//set default host address (will need to be commented out if the above code is used).
		hostAddress = "192.168.1.100";
		
		//create host label
		hostAddress1Button = new Button(hostAddress,
				(PC.ScreenWidth * 0.5f), (PC.ScreenHeight * 0.28f), 25);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {	
		
		//draw host screen
		g.drawImage(ResourceManager
				.getImage(PistolCaveGame.host_background), 0,0);
		
		hostButton.render(g);
		client1Button.render(g);
		client2Button.render(g);
		hostAddressButton.render(g);
		
		
	/*	//displays one or both clients names
		if(connections.size() == 1){
			clientAddressButton = new Button(connections.get(0).hostName,
					(PC.ScreenWidth * 0.5f), (PC.ScreenHeight * 0.43f), 20);
			clientAddressButton.render(g);
		}
		
		else if(connections.size()==2){
			clientAddressButton = new Button(connections.get(0).hostName,
					(PC.ScreenWidth * 0.5f), (PC.ScreenHeight * 0.43f), 20);
			clientAddressButton.render(g);

			clientAddress2Button = new Button(connections.get(1).hostName,
					(PC.ScreenWidth * 0.5f), (PC.ScreenHeight * 0.54f), 20);
			clientAddress2Button.render(g);

		}
		*/
		hostAddress1Button.render(g);
		
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		PC = (PistolCaveGame) game;
    	
		//socket connecting to client
        Socket s = null;
        
        
        //connect to client
        if(renderWait != 0 && connections.size() < 2){
			try {
				s = ss.accept();
				System.out.println("Connecting to client.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//add new connection to connection array and start a new thread for the client.
	        Connection con = new Connection(s, PC);
	        Thread thread = new Thread(con);
	        thread.start();
	        System.out.println("Started new thread.");
	        connections.add(con);
        }
        renderWait = 1;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return PistolCaveGame.HOSTSTATE;
	}

}
