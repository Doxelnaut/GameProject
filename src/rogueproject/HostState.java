package rogueproject;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HostState extends BasicGameState {
	
	ArrayList<Connection> connections;

	public void init(GameContainer container, StateBasedGame RG)
			throws SlickException {
		
		connections = new ArrayList<Connection>();
		ServerSocket ss = null;
		
		try {
			ss = new ServerSocket(6667);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.print("Error creating server socket.");
		}
		
		while (true)
        {
        	//loop to accept incoming connections
            Socket s = null;
			try {
				s = ss.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RogueGame game = (RogueGame) RG;
            Connection con = new Connection(s, game.state);
            Thread thread = new Thread(con);
            thread.start();
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
