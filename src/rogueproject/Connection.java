package rogueproject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection implements Runnable {
	
	private Socket socket;
	String hostName;				//client name
	boolean quit = false;
	ObjectOutputStream socketOut;
	ObjectInputStream socketIn;
	GameState state;
	Command c;
	Actor player;
	
	Connection(Socket s, GameState gs){
		socket = s;
		state = gs;
		if(state.player == null)
			player = state.player;
		else if(state.player2 == null)
			player = state.player2;
		//else
			//handle no more available player spots 
	}

	@Override
	public void run() {
		
			
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
		
	    while(!quit){
	    	//loop to write game state to client and get user input from client
		    	
			try {
				socketOut.writeObject(state);
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
			
			c.execute(player);
			
	    }
	    
	}
	
	private void estConn()throws IOException{
		
		InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
        hostName = address.getAddress().getHostAddress();
	}
}
