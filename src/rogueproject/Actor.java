package rogueproject;

import java.io.Serializable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

import jig.Entity;
import jig.Vector;

/**
 * 
 * @author Zacharias Shufflebarger
 *
 *	This file is part of El Rogue del Rey.
 *
 *  El Rogue del Rey is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  El Rogue del Rey is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with El Rogue del Rey.  If not, see <http://www.gnu.org/licenses/>.
 *
 *	Copyright 2014 Zacharias Shufflebarger
 *
 */
public class Actor extends IsoEntity {
	
	
	private float maxHitPoints;
	private float hitPoints;
	private float armor;
	private int experience; // for leveling up. Player accrues the experience enemies hold.
	// Graphics attributes
	private int type; // Player, creature, etc.
	private int level;
	private float attack;
	
	public int playerType = 0;
	public int enemyType = 1;
	
	public Animation anim;
	
	/* Constructors */
	/**
	 * 
	 * @param settype actor type
	 * @param setx tile x coordinate
	 * @param sety tile y coordinate
	 */	
	public Actor(Vector wWorldSize, int type){
		super(wWorldSize, RogueGame.TILE_SIZE);
		this.getTypeImage();
		this.type = type;
		this.setTypeAttributes();
	
	}

	/**
	 * Constructor for Player
	 */
	public Actor(Vector wWorldSize){
		super(wWorldSize, RogueGame.TILE_SIZE);
	}
	
	/* Getters */
	
	public int getLevel()			{return level;}
	public float getMaxHitPoints()	{return maxHitPoints;}
	public float getHitPoints()		{return hitPoints;}
	public float getAttack()		{return attack;}
	public float getArmor()			{return armor;}
	public int getType()			{return type;}
	public int getExp()				{return this.experience;}

	public int getTileX()			{return (int) (getX() / RogueGame.TILE_SIZE);}
	public int getTileY()			{return (int) (getY()/ RogueGame.TILE_SIZE);}
	public Vector getTilePosition()	{return new Vector(getTileX(), getTileY());}

	public void getTypeImage(){
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
		this.setPosition(setx * RogueGame.TILE_SIZE, sety * RogueGame.TILE_SIZE);
	}
	
	public void setTypeAttributes(){
		//TODO
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
	
	public void shoot(Vector direction){
		//TODO: shoot bullets
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
		setPosition(getPosition().add(new Vector(RogueGame.TILE_SIZE/2, RogueGame.TILE_SIZE/2)));
		super.render(g);
		setPosition(getPosition().add(new Vector(-RogueGame.TILE_SIZE/2, -RogueGame.TILE_SIZE/2)));
	}

	
}
