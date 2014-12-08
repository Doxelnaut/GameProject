package rogueproject;



import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import jig.ResourceManager;
import jig.Vector;

public class HostState extends BasicGameState {
	
	ArrayList<Connection> connections;
	ServerSocket ss = null;
	RogueGame RG;
	int renderWait = 0;
	Button hostButton;
	Button client1Button;
	Button client2Button;
	Button hostAddressButton;
	Button clientAddressButton;
	Button clientAddress2Button;
	String hostAddress;
	Button hostAddress1Button;
	
	public void init(GameContainer container, StateBasedGame RG)
			throws SlickException {
		
		
	}
	
	public void enter(GameContainer container, StateBasedGame game) throws SlickException{
		connections = new ArrayList<Connection>();
		RG = (RogueGame) game;
		
		hostButton = new Button("Hosting",
				(RG.ScreenWidth * 0.5f), (RG.ScreenHeight * 0.1f), 50);
		client1Button = new Button("Client1:",
				(RG.ScreenWidth * 0.3f), (RG.ScreenHeight * 0.43f), 25);
		client2Button = new Button("Client2:",
				(RG.ScreenWidth * 0.3f), (RG.ScreenHeight * 0.54f), 25);
		hostAddressButton = new Button("Host Adress:",
				(RG.ScreenWidth * 0.3f), (RG.ScreenHeight * 0.28f), 30);
		
		
		//Create map to transfer to client
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("src/resource/Map.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	   
		String line = null;
	        
	    int r = 0;
	    int c = 0;
		
	    try {
			while ((line = reader.readLine()) != null) {

			    String[] parts = line.split("\\s");
			    System.out.println(parts.length);
			    for(int i = 0; i < parts.length;i++){
			    	if(Integer.valueOf(parts[i]) == 1){
						RogueGame.walls.add(new Block(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE), true) );
						RogueGame.stop.add(new Ground(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
						RG.state.map[c] = 1;
			    	}
			    	else if(Integer.valueOf(parts[i]) == 2){
			    		RogueGame.blocks.add(new Block(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE), false) );
			    		RogueGame.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
			    		RG.state.map[c] =2;
			    	}
			    	else{
			    		RogueGame.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
			    		RG.state.map[c] = 0;
			    	}

			    	c++;
			    }
			    	r++;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		try {
			ss = new ServerSocket(1666);
			System.out.println("Created server socket.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error creating server socket.");
		}
		
		//****************************************************************************************
		//Gets local IP address
		/*try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//******************************************************************************************
		
		hostAddress = "192.168.1.100";
		hostAddress1Button = new Button(hostAddress,
				(RG.ScreenWidth * 0.5f), (RG.ScreenHeight * 0.28f), 25);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {	
		
		//draw host screen
		g.drawImage(ResourceManager
				.getImage(RogueGame.host_background), 0,0);
		
		hostButton.render(g);
		client1Button.render(g);
		client2Button.render(g);
		hostAddressButton.render(g);
		
		if(connections.size() == 1){
			clientAddressButton = new Button(connections.get(0).hostName,
					(RG.ScreenWidth * 0.5f), (RG.ScreenHeight * 0.43f), 20);
			clientAddressButton.render(g);
		}
		
		else if(connections.size()==2){
			clientAddressButton = new Button(connections.get(0).hostName,
					(RG.ScreenWidth * 0.5f), (RG.ScreenHeight * 0.43f), 20);
			clientAddressButton.render(g);

			clientAddress2Button = new Button(connections.get(1).hostName,
					(RG.ScreenWidth * 0.5f), (RG.ScreenHeight * 0.54f), 20);
			clientAddress2Button.render(g);

		}
		
		hostAddress1Button.render(g);
		
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		RG = (RogueGame) game;
    	//loop to accept incoming connections
        Socket s = null;
        
        if(renderWait != 0 && connections.size() < 2){
			try {
				s = ss.accept();
				System.out.println("Connecting to client.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        Connection con = new Connection(s, RG);
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
		return RogueGame.HOSTSTATE;
	}

}
