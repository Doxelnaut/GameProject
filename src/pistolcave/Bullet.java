package pistolcave;

import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
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
	
	public Bullet(Vector wWorldSize, Vector wPosition, double t, int translate) {
		super(wWorldSize,PistolCaveGame.TILE_SIZE);
		if(translate == 1){
			wPosition = wPosition.setX(wPosition.getX() + 29); //moves the bullet over on top of player
			wPosition = wPosition.setY(wPosition.getY() - 8);  //moves the bullet up to match player height
			setPosition(wPosition);
		}
		else
			setPositionNoTranslate(wPosition);
		theta = t;
		velocity = new Vector(0f,-.5f).setRotation(theta);
		domain = wWorldSize;
		addImageWithBoundingBox(ResourceManager
				.getImage(PistolCaveGame.bulletResource));
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
