package rogueproject;

import java.util.Iterator;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Sound;

public class Minotaur extends IsoEntity {
	Animation[] walking = new Animation[8];
	private int level;
	private float maxHitPoints;
	private float hitPoints;
	private float attack;
	private float armor;
	private int experience; // for leveling up. Player accrues the experience enemies hold.
	// Graphics attributes
	private int type; // Player, creature, etc.
	private int depth; // track the player's depth in the dungeon
	private int orders = PlayingState.WAIT; // store the input here for acting
	private int classtype;
	private float energy, gain; // energy is used for actions per turn
	static final int LEFT = 0;
	static final int RIGHT = 1;
	static final int UP = 2;
	static final int DOWN = 3;
	static final int UpLEFT = 4;
	static final int UpRIGHT = 5;
	static final int DownLEFT = 6;
	static final int DownRIGHT = 7;


	int current;
	Vector wWorldSz;
	Vector lastWPosition;
	
	Sound ouch;
	
	public Minotaur(Vector wWorldSize, Vector wPosition,int charClass) {
		super(wWorldSize, IsoWorldGame.tileSize);
		wWorldSz = wWorldSize;
		
		removeAnimation(walking[current]);
		wWorldSz = wWorldSize;
		
		type = charClass;
		
		getTypeImage();
		
		addAnimation(walking[current]);
		setZHeightFromIsoImage(walking[current].getCurrentFrame(), 32);
		setPosition(wPosition);
		lastWPosition = wPosition;
		}
	
	private void getTypeImage() {
		switch(type){
			case(GameState.WARRIOR):{
				walking[LEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkLeft, 99, 135), 0,0,14,0, true, 70, true);
				walking[RIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkRight,99, 135), 0,0,14,0, true, 70, true);
				walking[UP] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkUp, 95, 135), 0,0,14,0, true, 70, true);
				walking[DOWN] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkDown, 93, 135), 0,0,14,0, true, 70, true);
				walking[UpLEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkUpLeft, 101, 127), 0,0,14,0, true, 70, true);
				walking[UpRIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkUpRight, 85, 138), 0,0,14,0, true, 70, true);
				walking[DownLEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkDownLeft, 83, 139), 0,0,14,0, true, 70, true);
				walking[DownRIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkDownRight, 105, 127), 0,0,14,0, true, 70, true);
				current = RIGHT;
				
				
				
				//System.out.println("Minotaur zh:" + zHeight);
				
				//ouch = ResourceManager.getSound(IsoWorldGame.ouchSoundPath);
			}
		}
	}

	public void sayOuch() {
		if (ouch.playing()) return;
		ouch.play();
	}
	
	public void go(int dir, float n,int image) {
		if (current != image) {
			removeAnimation(walking[current]);
			current = image;
			addAnimation(walking[current]);
		}
		if (walking[current].isStopped()) walking[current].start();
		
	
		float x, y;
		if (dir < 2) {x = n; y = 0;} 
		else {x = 0; y=n;}
		if (dir % 2 == 0) {x = -x; y = -y;}
		
		lastWPosition = getPosition();
		setPosition(lastWPosition.add(new Vector(x,y)));
	}
	
	/**
	 * Undo the minotaur's last move, e.g., if he hit something.
	 */
	public void ungo() {
		setPosition(lastWPosition);
	}
	public void halt() {
		walking[current].stop();
	}
	public Fireball launchFireball() {
		Fireball f = new Fireball(wWorldSz, wPosition, current);
		return f;
	}
	public static boolean canMove(){
		IsoEntity other;
		for (Iterator<IsoEntity> iie = GameState.stop.iterator(); iie.hasNext(); ) {
			other = iie.next();
			
			if (GameState.minotaur.collides(other) != null) {
				return false;
			}
			
		}
		return true;
	
	}

	public int getDepth()		{return this.depth;}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public float getMaxHitPoints() {
		return maxHitPoints;
	}

	public void setMaxHitPoints(float maxHitPoints) {
		this.maxHitPoints = maxHitPoints;
	}

	public float getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(float hitPoints) {
		this.hitPoints = hitPoints;
	}

	public float getAttack() {
		return attack;
	}

	public void setAttack(float attack) {
		this.attack = attack;
	}

	public float getArmor() {
		return armor;
	}

	public void setArmor(float armor) {
		this.armor = armor;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public int getClasstype() {
		return classtype;
	}

	public void setClasstype(int classtype) {
		this.classtype = classtype;
	}

	public float getEnergy() {
		return energy;
	}

	public void setEnergy(float energy) {
		this.energy = energy;
	}

	public float getGain() {
		return gain;
	}

	public void setGain(float gain) {
		this.gain = gain;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	
}
