package pistolcave;

import java.io.Serializable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

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
public class Actor extends IsoEntity {
	private static final int Up = 0, UpRight=1, Right=2, DownRight=3, Down=4, DownLeft=5, Left=6, UpLeft=7, CTRL=8;

	Animation[] walking = new Animation[8];
	Animation[] Attack = new Animation[8];
	Animation currentAnimation;
	
	private float maxHitPoints;
	private float hitPoints;
	private float armor;
	private int experience; // for leveling up. Player accrues the experience enemies hold.
	// Graphics attributes
	public int type; // Player, creature, etc.
	private int level;
	private float attack;
	
	public int playerType = 0;
	public int enemyType = 1;
	public PathFinder pathToUser1;
	public PathFinder pathToUser2;
	public Animation anim;
	int current = Right; // current direction used for animation
	int shootingDirection;
	Vector wWorldSz;
	Vector lastWPosition;
	
	/* Constructors */
	/**
	 * 
	 * @param vector 
	 * @param settype actor type
	 * @param setx tile x coordinate
	 * @param sety tile y coordinate
	 */	
	public Actor(Vector wWorldSize, Vector vector, int type){
		super(wWorldSize, PistolCaveGame.TILE_SIZE);
		this.getTypeImage(type);
		this.type = type;
		this.setTypeAttributes(type,vector);
		//wWorldSz = wWorldSize;
		//removeAnimation(walking[current]);
		
		//setPosition(wPosition);
		//lastWPosition = wPosition;
	}

	/**
	 * Constructor for Player
	 */
	public Actor(Vector wWorldSize){
		super(wWorldSize, PistolCaveGame.TILE_SIZE);
	}
	
	/* Getters */
	
	public int getLevel()			{return level;}
	public float getMaxHitPoints()	{return maxHitPoints;}
	public float getHitPoints()		{return hitPoints;}
	public float getAttack()		{return attack;}
	public float getArmor()			{return armor;}
	public int getType()			{return type;}
	public int getExp()				{return this.experience;}

	public int getTileX()			{return (int) (getX() / PistolCaveGame.TILE_SIZE);}
	public int getTileY()			{return (int) (getY()/ PistolCaveGame.TILE_SIZE);}
	public Vector getTilePosition()	{return new Vector(getTileX(), getTileY());}

	public void getTypeImage(int type2){
		switch(type2){
		case 1: break;
		case 2: 
			walking[Left] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1WalkLeft, 178, 111), 0,0,13,0,true, 70, true);
			walking[Right] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1WalkRight,178, 111), 0,0,13,0, true, 70, true);
			walking[Up] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1WalkUp, 178, 111), 0,0,13,0, true, 70, true);
			walking[Down] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1WalkDown, 178, 111), 0,0,13,0, true, 70, true);
			walking[UpLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1WalkUpLeft, 178, 111), 0,0,13,0, true, 70, true);
			walking[UpRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1WalkUpRight, 178, 111), 0,0,13,0, true, 70, true);
			walking[DownLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1WalkDownLeft, 178, 111), 0,0,13,0, true, 70, true);
			walking[DownRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1WalkDownRight, 178, 111), 0,0,13,0, true, 70, true);
			
			Attack[Left] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1AttackLeft, 162, 95), 0,0,16,0, true, 70, true);
			Attack[Right] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1AttackRight,162, 95), 0,0,16,0, true, 70, true);
			Attack[Up] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1AttackUp, 162, 95), 0,0,16,0, true, 70, true);
			Attack[Down] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1AttackDown, 162, 95), 0,0,16,0, true, 70, true);
			Attack[UpLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1AttackUpLeft, 162, 95), 0,0,16,0, true, 70, true);
			Attack[UpRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1AttackUpRight, 162, 95), 0,0,16,0, true, 70, true);
			Attack[DownLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1AttackDownLeft, 162, 95), 0,0,16,0, true, 70, true);
			Attack[DownRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy1AttackDownRight, 162, 95), 0,0,16,0, true, 70, true);
			break;
		case 3:
			walking[Left] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2WalkLeft, 117, 75), 0,0,7,0, true, 70, true);
			walking[Right] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2WalkRight,117, 75), 0,0,7,0, true, 70, true);
			walking[Up] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2WalkUp, 117, 75), 0,0,7,0, true, 70, true);
			walking[Down] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2WalkDown, 117, 75), 0,0,7,0, true, 70, true);
			walking[UpLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2WalkUpLeft, 117, 75), 0,0,7,0, true, 70, true);
			walking[UpRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2WalkUpRight, 117, 75), 0,0,7,0, true, 70, true);
			walking[DownLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2WalkDownLeft, 117, 75), 0,0,7,0, true, 70, true);
			walking[DownRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2WalkDownRight, 117, 75), 0,0,7,0, true, 70, true);
			
			Attack[Left] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2AttackLeft, 186, 112), 0,0,15,0, true, 70, true);
			Attack[Right] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2AttackRight,186, 112), 0,0,15,0, true, 70, true);
			Attack[Up] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2AttackUp, 186, 112), 0,0,15,0, true, 70, true);
			Attack[Down] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2AttackDown, 186, 112), 0,0,15,0, true, 70, true);
			Attack[UpLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2AttackUpLeft, 186, 112), 0,0,15,0, true, 70, true);
			Attack[UpRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2AttackUpRight, 186, 112), 0,0,15,0, true, 70, true);
			Attack[DownLeft] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2AttackDownLeft, 186, 112), 0,0,15,0, true, 70, true);
			Attack[DownRight] = new Animation(ResourceManager.getSpriteSheet(PistolCaveGame.Enemy2AttackDownRight, 186, 112), 0,0,15,0, true, 70, true);
		}
	}
	
	/* Setters */
	public void setLevel(int set)			{this.level = set;}
	public void setMaxHitPoints(float set)	{this.maxHitPoints = set;}
	public void setHitPoints(float set)	 	{this.hitPoints = Math.min(set, this.maxHitPoints);}
	public void setAttack(float set)		{this.attack = set;}
	public void setArmor(float set)			{this.armor = set;}
	public void setExp(int set)				{this.experience = set;}
	public void setType(int set)			{this.type = set;}
	
	
	public void setTilePosition(int setx, int sety){
		this.setPosition(setx * PistolCaveGame.TILE_SIZE, sety * PistolCaveGame.TILE_SIZE);
	}
	
	public void setTypeAttributes(int type2, Vector vector){
		addAnimation(walking[current]);
		currentAnimation = walking[current];
		setZHeightFromIsoImage(walking[current].getCurrentFrame(), 32);
		switch(type2){
		case 2:
			this.setPosition(vector);
			break;
		case 3: 
			this.setPosition(vector);
			break;
		}
	}
	public void pathFinder(Player user, int i) {
			PathFinder pf = new PathFinder(this,user);
			if(i == 1){
				this.pathToUser1 = pf;
			}
			else{
				this.pathToUser2 = pf;
			}
			//pf.showmypath();
			
		

	}
	
	/* Actions */

	/**
	 * Reciever method for MoveUpCommand. 
	 */
	public void move(int direction) {
		//TODO: move character. Need to change collision detection to use tiles again, 
		// or implement a scan line to lower the number of entities that are checked
		// for collision. Perhaps the scan line can just use the camera view coords?
		// I would much rather re-implement tiles to save on speed. I'm sure part of 
		// the slow down came from checking for collisions too much.
		
		// Set movement in world coordinates using a unit vector that points in any one
		// of the cardinal or diagonal directions. 
		int theta = direction * 45; // angle of directional unit vector from North.
		Vector unitDirection = new Vector(0, -1);
		unitDirection = unitDirection.rotate(theta);
		this.setPosition(this.getPosition().add(unitDirection.scale(5)));
	
		this.getWalkingAnimation();
				
		// TODO: basic movement handled. Need to check for collision first, though.
		// TODO: choose and create animation based on direction.
	}
	
	public void shoot(Vector direction, StateBasedGame game){
	}
	
	public void getWalkingAnimation(){
//		anim = new Animation();
	}
	
	/* Update */
	public void update(final int delta){
		
	}
	
	/* Render */
	
	@Override
	public void render (Graphics g){
		/*
		 * Position in Tile coordinates is tracked by the top left corner of each tile,
		 * but Entity position renders sprite images centered at the Entity's position.
		 * To fix this, the Actor's Entity position is translated by half of the tile size
		 * towards the bottom right corner of the tile, so the Actor's Entity position
		 * is the middle of the tile. Then the Actor gets rendered correctly to the screen.
		 * After the Actor is rendered, its Entity position is translated back to Tile
		 * coordinates.
		 */
		
		/* moving in isometric */
		
	//	getX()*(RogueGame.TILE_SIZE/2) + getY()*(RogueGame.TILE_SIZE/2))
		setPosition(getPosition().add(new Vector(PistolCaveGame.TILE_SIZE/2, PistolCaveGame.TILE_SIZE/2)));
		super.render(g);
		setPosition(getPosition().add(new Vector(-PistolCaveGame.TILE_SIZE/2, -PistolCaveGame.TILE_SIZE/2)));
	}

	public void crouch() {
		//VOID
	}
	public PathFinder getPath()				{return this.pathToUser1;}


	
}
