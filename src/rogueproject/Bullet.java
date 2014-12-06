package rogueproject;

import jig.Vector;

public class Bullet extends IsoEntity{

	Vector domain;
	Vector direction;
	int speed = 1; // scale movement
	
	public Bullet(Vector wWorldSize, Vector wPosition, Vector wDirection) {
		super(wWorldSize, RogueGame.TILE_SIZE);
		domain = wWorldSize;
		setPosition(wPosition);
		direction = wDirection;
	}
	
	public void update(float n) {
		setPosition(getPosition().add(direction).scale(speed));
	}
	
	public boolean done() {
		
		if (wPosition.getX() < 0 || wPosition.getX() > domain.getX() ||
				wPosition.getY() < 0 || wPosition.getY() > domain.getY()) return true;
		return false;
	}
	
}
