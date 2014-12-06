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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import jig.Vector;

public class HostState extends BasicGameState {
	
	ArrayList<Connection> connections;

	public void init(GameContainer container, StateBasedGame RG)
			throws SlickException {
		
		
	}
	
	public void enter(GameContainer container, StateBasedGame game){
		connections = new ArrayList<Connection>();
		
		RogueGame RG = (RogueGame) game;
		
		//Create map to transfer to client
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("src/resource/Map.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	        String line = null;
	        
	        int r = 0;
		try {
			while ((line = reader.readLine()) != null) {

			    String[] parts = line.split("\\s");
			    System.out.println(parts.length);
			    for(int i = 0; i < parts.length;i++){
			    	if(Integer.valueOf(parts[i]) == 1){
						RogueGame.walls.add(new Block(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE), true) );
						RogueGame.stop.add(new Ground(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
						RG.state.map[i] = 1;
			    	}
			    	else if(Integer.valueOf(parts[i]) == 2){
			    		RogueGame.blocks.add(new Block(RogueGame.WORLD_SIZE,new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE), false) );
			    		RogueGame.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
			    		RG.state.map[i] =2;
			    	}
			    	else{
			    		RogueGame.ground.add(new Ground(RogueGame.WORLD_SIZE, new Vector(r*RogueGame.TILE_SIZE, i*RogueGame.TILE_SIZE)) );
			    		RG.state.map[i] = 0;
			    	}

			    }
			    	r++;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServerSocket ss = null;
		
		try {
			ss = new ServerSocket(1666);
			System.out.println("Created server socket.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error creating server socket.");
		}
		
		while(true)
        {
        	//loop to accept incoming connections
            Socket s = null;
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
	}
	
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2)
			throws SlickException {		
	}

	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return RogueGame.HOSTSTATE;
	}

}
