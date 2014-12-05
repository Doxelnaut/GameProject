package rogueproject;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HostState extends BasicGameState {
	
	ArrayList<Connection> connections;

	public void init(GameContainer container, StateBasedGame RG)
			throws SlickException {
		
		
	}
	
	public void enter(GameContainer container, StateBasedGame game){
		connections = new ArrayList<Connection>();
		ServerSocket ss = null;
		
		try {
			ss = new ServerSocket(1666);
			System.out.println("Created server socket.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error creating server socket.");
		}
		
		while (true)
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
			RogueGame RG = (RogueGame) game;
            Connection con = new Connection(s, RG.state);
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
