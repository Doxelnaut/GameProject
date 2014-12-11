package rogueproject;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import jig.ResourceManager;
import jig.Vector;

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
	
	public Items(Vector wWorldSize, Vector wPosition,int type) {
		super(wWorldSize, RogueGame.TILE_SIZE);
		setType(type);
		
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
	public void setTypeAttributes(){
		SpriteSheet x = ResourceManager.getSpriteSheet(RogueGame.potions, 29, 29);
		switch(this.getType()){
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
			
		default:
			break;
		}
	}
	
}
