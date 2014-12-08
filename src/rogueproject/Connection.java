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
	Command c;
	Player player;
	Connection(Socket s, RogueGame gs){
		socket = s;
		RG = gs;
	}

	@Override
	public void run() {
		
		//create player
		if(RogueGame.player == null){
			RG.player = new Player(RogueGame.WORLD_SIZE, new Vector(2*RogueGame.TILE_SIZE, 2*RogueGame.TILE_SIZE),1);
			RogueGame.blocks.add(RG.player);
			player = RG.player;
			RG.state.player.setX(player.getX());
			RG.state.player.setY(player.getY());
			RG.state.firstPlayer = true;
		}
		
		else if( RG.player2 == null){
			RG.player2 = new Player(RogueGame.WORLD_SIZE, new Vector(2*RogueGame.TILE_SIZE, 2*RogueGame.TILE_SIZE),1);
			RogueGame.blocks.add(RG.player2);
			player = RG.player2;
			RG.state.player2.setX(player.getX());
			RG.state.player2.setY(player.getY());
			RG.state.secondPlayer= true;
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
				socketOut.writeObject(RG.state);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error Writing game state to client.");
			}
			
			try {
				c = (Command) socketIn.readObject();
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("Error reading command.");
				e.printStackTrace();
			}
			
			if(c != null){
				System.out.println("Command from server: " + c.toString());
				c.execute(player);
			}
			
			if(player == RG.player){
			RG.state.player.setX(player.getX());
			RG.state.player.setY(player.getY());
		//	RG.state.player.setTheta(player.getRotation());
			}
			else if(player == RG.player2){
				RG.state.player2.setX(player.getX());
				RG.state.player2.setY(player.getY());
			}
			
			
	    }
	    
	}
	
	private void estConn()throws IOException{
		
		InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
        hostName = address.getAddress().getHostAddress();
	}
}
