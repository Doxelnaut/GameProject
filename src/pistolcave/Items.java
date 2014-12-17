package pistolcave;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

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
public class Items  extends IsoEntity{

	Vector direction;
	
	boolean picked_up;
	boolean used; // scale movement
	private int type;
	Image potion;
	
	String name;
	int healing_amount;
	int shield_amount;
	int attack_increase;
	int speed_increase;
	float duration;
	SpriteSheet x = ResourceManager.getSpriteSheet(PistolCaveGame.potions, 29, 29);
	
	public Items(Vector wWorldSize, Vector wPosition,int type) {
		super(wWorldSize, PistolCaveGame.TILE_SIZE);
		this.type = type;
		if(type == 0){
			potion = x.getSubImage(1, 0);
			addImage(potion);
			setZHeightFromIsoImage(potion);
			setPosition(wPosition);
			
			name = "healing";
			healing_amount = 10;
			shield_amount = 0;
			speed_increase = 0;
			duration = 0;
			used = false;
			picked_up = false;
		}
		else if(type == 1){
			potion = x.getSubImage(5, 1);

			addImage(potion);
			setZHeightFromIsoImage(potion);
			setPosition(wPosition);
			
			name = "strength";
			healing_amount = 0;
			shield_amount = 10;
			attack_increase = 10;
			speed_increase = 0;
			duration = 15;
			used = false;
			picked_up = false;
		}
		else{

			potion = x.getSubImage(2, 2);

			addImage(potion);
			setZHeightFromIsoImage(potion);
			setPosition(wPosition);
			
			name = "energy";
			healing_amount = 0;
			shield_amount = 0;
			attack_increase = 0;
			speed_increase = 2;
			duration = 15;
			used = false;
			picked_up = false;
		}
		//setTypeAttributes();
	}
	
	protected boolean isPicked_up() {return picked_up;}
	protected void setPicked_up(boolean picked_up) {this.picked_up = picked_up;}
	protected boolean isUsed() {return used;}
	protected void setUsed(boolean used) {this.used = used;}
	protected int getType() {return type;}
	protected void setType(int type) {this.type = type;}

	public void update(float n) {
	}
	
	public boolean pickedUp() {
		
		return true;
	}
	public void setTypeAttributes(int type){
		switch(type){
		case 0:
			
			potion = x.getSubImage(1, 0);
			addImage(potion);
			setZHeightFromIsoImage(potion);
			setPosition(wPosition);
			
			name = "healing";
			healing_amount = 10;
			shield_amount = 0;
			speed_increase = 0;
			duration = 0;
			used = false;
			picked_up = false;
			
			break;
			
		case 1: 
			potion = x.getSubImage(5, 1);

			addImage(potion);
			setZHeightFromIsoImage(potion);
			setPosition(wPosition);
			
			name = "strength";
			healing_amount = 0;
			shield_amount = 10;
			attack_increase = 10;
			speed_increase = 0;
			duration = 15;
			used = false;
			picked_up = false;
			break;
			
		case 3:
			
			potion = x.getSubImage(2, 2);

			addImage(potion);
			setZHeightFromIsoImage(potion);
			setPosition(wPosition);
			
			name = "energy";
			healing_amount = 0;
			shield_amount = 0;
			attack_increase = 0;
			speed_increase = 2;
			duration = 15;
			used = false;
			picked_up = false;
			break;
			
		default:
			break;
		}
	}
	
}
