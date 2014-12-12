package rogueproject;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bullet extends IsoEntity{

	Vector velocity;
	Vector domain;
	Vector originalPos; //used to prevent bullets from being 
	boolean active = true;
	int speed = 1; // scale movement
	
	public Bullet(Vector wWorldSize, Vector wPosition) {
		super(wWorldSize,RogueGame.TILE_SIZE);
		//super(wPosition.getX(),wPosition.getY());
		setPosition(wPosition);
		velocity = new Vector(0f,.01f);
		domain = wWorldSize;
		addImageWithBoundingBox(ResourceManager
				.getImage(RogueGame.bulletResource));
	}
	
	public void update(float delta) {
		//setPosition(getPosition().add(direction).scale(speed));
		translate(velocity.scale(delta));
	}
	
	public boolean isActive() {
	/*	
		if (this.getX() < 0 || this.getX() > domain.getX() ||
				this.getY() < 0 || this.getY() > domain.getY()) active = false;
		*/
		return active;
	}
	
	public void setActiveVar(boolean a){
		active = a;
	}
	
	
}
