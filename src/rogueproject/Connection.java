package rogueproject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import jig.Vector;

public class Connection implements Runnable {
	
	private Socket socket;
	String hostName;				//client name
	boolean quit = false;
	ObjectOutputStream socketOut;
	ObjectInputStream socketIn;
	RogueGame RG;
	clientState playerState;
	
	
	Connection(Socket s, RogueGame gs){
		socket = s;
		RG = gs;
	}

	@Override
	public void run() {
		
		//create player
		if(!RG.player1Connected){
			RG.player1Connected = true;
			RG.state.player.setPos(new Vector(2*RogueGame.TILE_SIZE, 2*RogueGame.TILE_SIZE));
			RG.state.player.setDirection(2);
			RG.state.firstPlayer = true;
		}
		
		else if(!RG.player2Connected){
			RG.player2Connected = true;
			RG.state.player2.setPos(new Vector(2*RogueGame.TILE_SIZE, 2*RogueGame.TILE_SIZE));
			RG.state.player2.setDirection(2);
			RG.state.secondPlayer= true;
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
			socketOut.writeObject(RG.state);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    while(!quit){
	    	//loop to write game state to client and get user input from client
		    	
			try {
				socketOut.reset();
				socketOut.writeObject(RG.state);
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
				
			RG.update(playerState);
	    }
	    
	}
	
	private void estConn()throws IOException{
		
		InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
        hostName = address.getAddress().getHostAddress();
	}
}
