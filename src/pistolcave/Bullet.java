package pistolcave;

import jig.ResourceManager;
import jig.Vector;

/**
 * 
 * @author Corey Amoruso
 * @author Ryan Bergquist
 * @author Zacharias Shufflebarger
 * 
 *	This file is part of Pistol Cave.
 *
 *  Pistol Cave is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Pistol Cave is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Pistol Cave.  If not, see <http://www.gnu.org/licenses/>.
 *
 *	Copyright 2014 Corey Amoruso, Ryan Bergquist, Zacharias Shufflebarger
 */
public class Bullet extends IsoEntity{

	Vector velocity;
	Vector domain;
	boolean active = true;
	int speed = 1; // scale movement
	double theta;
	float damage;
	
	public Bullet(Vector wWorldSize, Vector wPosition, double t, int translate, float attack) {
		super(wWorldSize,PistolCaveGame.TILE_SIZE);
		if(translate == 1){
			//adjust the bullets to fire from about shoulder height
			wPosition = wPosition.setX(wPosition.getX() + 32); //moves the bullet over on top of player
			wPosition = wPosition.setY(wPosition.getY() - 32);  //moves the bullet up to match player height
			setPosition(wPosition);
		}
		else
			setPosition/*NoTranslate*/(wPosition);
		theta = t;
		velocity = new Vector(1f,0f).setRotation(theta + 45);
//		velocity.setY(velocity.getY()/2);
		System.out.println("velocity: " + velocity + ", theta: " + theta + ", theta+45: " + (theta+45));
		domain = wWorldSize;
		damage = attack;
		addImageWithBoundingBox(ResourceManager
				.getImage(PistolCaveGame.bulletResource));
	}
	
	/* Getters */
	public float getDamage(){return this.damage;}
	
	/* Setters */
	
	public void setActiveVar(boolean a){active = a;}
	public void setDamage(float set){this.damage = set;}
	
	/* Other Methods */
	
	public void update(float delta) {
		//setPosition(getPosition().add(direction).scale(speed));
		//translate(velocity.scale(delta));
		this.setPosition(this.getPosition().add(velocity.scale(delta/4)));
	}
	
	public boolean isActive() {
		
		if (this.getX() < 0 || this.getX() > domain.getX() ||
				this.getY() < 0 || this.getY() > domain.getY()) active = false;
		
		return active;
	}

	
}
