package rogueproject;

import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bullet extends IsoEntity{

	Vector velocity;
	Vector domain;
	boolean active = true;
	int speed = 1; // scale movement
	int direction;
	
	public Bullet(Vector wWorldSize, Vector wPosition, int d, int translate) {
		super(wWorldSize,RogueGame.TILE_SIZE);
		if(translate == 1)
			setPosition(wPosition);
		else
			setPositionNoTranslate(wPosition);
		direction = d;
		velocity = new Vector(0f,-.1f).rotate(direction * 45);
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
