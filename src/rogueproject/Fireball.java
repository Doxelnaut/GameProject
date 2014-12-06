package rogueproject;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;

public class Fireball extends IsoEntity {

	Animation[] moving = new Animation[5];
	int current;
	static boolean shot = false;
	static final int LEFT = 0;
	static final int RIGHT = 1;
	static final int UP = 2;
	static final int DOWN = 3;
	static final int KABOOM = 4;
	Vector domain;
	
	public Fireball(Vector wWorldSize, Vector wPosition, int dir) {
		super(wWorldSize, RogueGame.TILE_SIZE);
		domain = wWorldSize;

		moving[LEFT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireballSheetPath, 64, 64), 0,7,7,7, true, 20, true);
		moving[RIGHT] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireballSheetPath, 64, 64), 0,3,7,3, true, 20, true);
		moving[UP] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireballSheetPath, 64, 64), 0,1,7,1, true, 20, true);
		moving[DOWN] = new Animation(ResourceManager.getSpriteSheet(RogueGame.fireballSheetPath, 64, 64), 0,5,7,5, true, 20, true);
		moving[KABOOM] = new Animation(ResourceManager.getSpriteSheet(RogueGame.explosionSheetPath, 256, 128), 20);
		current = dir;
		addAnimation(moving[current]);
		setZHeightFromIsoImage(moving[current].getCurrentFrame(), 32);
		//System.out.println("Fireball zh:" + zHeight);
		setPosition(wPosition);
	}
	public void update(float n) {
		float x, y;
		if (current == KABOOM) return;
		
		if (current < 2) {x = n; y = 0;} 
		else {x = 0; y=n;}
		if (current % 2 == 0) {x = -x; y = -y;}
		
		setPosition(getPosition().add(new Vector(x,y)));
	}
	public void kaboom() {
		removeAnimation(moving[current]);
		current = KABOOM;
		addAnimation(moving[current]);
		moving[current].setLooping(false);
	}
	public boolean done() {
		
		if (wPosition.getX() < 0 || wPosition.getX() > domain.getX() ||
				wPosition.getY() < 0 || wPosition.getY() > domain.getY()) return true;
		if (current == KABOOM) return moving[current].isStopped();
		return false;
	}
	public boolean getShot(){
		return shot;
	}
}