package rogueproject;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection implements Runnable {
	
	private Socket socket;
	String hostName;				//client name
	boolean quit = false;
	ObjectOutputStream socketOut;
	ObjectInputStream socketIn;
	GameState state;
	
	Connection(Socket s, GameState gs){
		socket = s;
		state = gs;
	}

	@Override
	public void run() {
		
			
			try {
				estConn();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.print("Error connecting to client");
				}
		
		//open object streams
	    try {
	      socketOut = new ObjectOutputStream(socket.getOutputStream());
	      socketIn = new ObjectInputStream(socket.getInputStream());
	    }
	    catch(IOException e){
			e.printStackTrace();
			System.out.print("Error opening streams");
	    }
		
	    while(!quit){
	    	//loop to write game state to client and get user input from client
		    	
			try {
				socketOut.writeObject(state);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.print("Error Writing game state to client.");
			}
			
			Command c = socketIn.readObject();
			
	    }
	    
	}
	
	private void estConn()throws IOException{
		
		InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
        hostName = address.getAddress().getHostAddress();
	}
}
