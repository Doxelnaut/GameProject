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
	
	// RPG attributes
	private int level;
	private float attack;
	// Graphics attributes

	public boolean energyGained = false; // false = no energy gained this turn.
	private float energy, gain; // energy is used for actions per turn
	// consider also attackCost to differentiate from movement and attack costs
	// i.e. faster movement, but regular attack speed.
	
	public int playerType = 0;
	public int enemyType = 1;
	
	/* Constructors */
	/**
	 * 
	 * @param settype actor type
	 * @param setx tile x coordinate
	 * @param sety tile y coordinate
	 */	
	public Actor(Vector wWorldSize, int type){
		super(wWorldSize, RogueGame.TILE_SIZE);
		this.type = type;
		this.energy = 0;
		this.setTypeAttributes();
	
	}

	/* Getters */
	
	public int getLevel()			{return level;}
	public float getMaxHitPoints()	{return maxHitPoints;}
	public float getHitPoints()		{return hitPoints;}
	public float getAttack()		{return attack;}
	public float getArmor()			{return armor;}
	public int getType()			{return type;}
	public float getEnergy()		{return energy;}
	public float getGain()			{return gain;}
	public boolean getGained()		{return this.energyGained;}
	public int getExp()				{return this.experience;}

	public int getTileX()			{return (int) (getX() / RogueGame.TILE_SIZE);}
	public int getTileY()			{return (int) (getY()/ RogueGame.TILE_SIZE);}
	public Vector getTilePosition()	{return new Vector(getTileX(), getTileY());}



	/* Setters */
	
	public void setLevel(int set)			{this.level = set;}
	public void setMaxHitPoints(float set)	{this.maxHitPoints = set;}
	public void setHitPoints(float set)	 	{this.hitPoints = Math.min(set, this.maxHitPoints);}
	public void setAttack(float set)		{this.attack = set;}
	public void setArmor(float set)			{this.armor = set;}
	public void setEnergy(float set)		{this.energy = set;}
	public void setGain(float set)			{this.gain = set;}
	public void setGained(boolean set)		{this.energyGained = set;}
	public void setExp(int set)				{this.experience = set;}
	
	
	public void setTilePosition(int setx, int sety){
		this.setPosition(setx * RogueGame.TILE_SIZE, sety * RogueGame.TILE_SIZE);
	}
	
	public void setTypeAttributes(){
		switch(this.getType()){
		// Undead
		case 0: // little zombie
			this.setLevel(1);
			this.setMaxHitPoints(7);
			this.setHitPoints(7);
			this.setAttack(1);
			this.setArmor(0);
			this.setGain(0.5f);
			this.setExp(1);
			break;
		case 1: // little mummy
			this.setLevel(1);
			this.setMaxHitPoints(5);
			this.setHitPoints(5);
			this.setAttack(1);
			this.setArmor(0);
			this.setGain(1);
			this.setExp(2);
			break;
		case 2: // skeleton
			this.setLevel(1);
			this.setMaxHitPoints(5);
			this.setHitPoints(5);
			this.setAttack(1);
			this.setArmor(1);
			this.setGain(1.5f);
			this.setExp(3);
			break;
		case 3: // large zombie
			this.setLevel(1);
			this.setMaxHitPoints(9);
			this.setHitPoints(5);
			this.setAttack(1);
			this.setArmor(0);
			this.setGain(0.75f);
			this.setExp(3);
			break;
		case 4: // large mummy
			this.setLevel(1);
			this.setMaxHitPoints(8);
			this.setHitPoints(5);
			this.setAttack(1);
			this.setArmor(1);
			this.setGain(1);
			this.setExp(3);
			break;
		case 5: // death
			this.setLevel(1);
			this.setMaxHitPoints(12);
			this.setHitPoints(12);
			this.setAttack(3);
			this.setArmor(1);
			this.setGain(0.8f);
			this.setExp(5);
			break;
		// Pests
		case 6: // little spider
			this.setLevel(2);
			this.setMaxHitPoints(15);
			this.setHitPoints(15);
			this.setAttack(2);
			this.setArmor(1);
			this.setGain(1);
			this.setExp(4);
			break;
		case 7: // little scorpion
			this.setLevel(2);
			this.setMaxHitPoints(15);
			this.setHitPoints(15);
			this.setAttack(2.25f);
			this.setArmor(1.5f);
			this.setGain(1);
			this.setExp(5);
			break;
		case 8: // slug
			this.setLevel(2);
			this.setMaxHitPoints(20);
			this.setHitPoints(20);
			this.setAttack(2);
			this.setArmor(3);
			this.setGain(0.7f);
			this.setExp(6);
			break;
		case 9: // large spider
			this.setLevel(2);
			this.setMaxHitPoints(20);
			this.setHitPoints(20);
			this.setAttack(3);
			this.setArmor(1.5f);
			this.setGain(1.5f);
			this.setExp(7);
			break;
		case 10: // large scorpion
			this.setLevel(2);
			this.setMaxHitPoints(20);
			this.setHitPoints(20);
			this.setAttack(3);
			this.setArmor(2);
			this.setGain(1.4f);
			this.setExp(8);
			break;
		case 11: // red leech
			this.setLevel(2);
			this.setMaxHitPoints(20);
			this.setHitPoints(20);
			this.setAttack(5);
			this.setArmor(2);
			this.setGain(1);
			this.setExp(15);
			break;
		default:
			break;
		}
		
	}
	
	/* Actions */

	/**
	 * Reciever method for MoveUpCommand. 
	 */
	public void move(int direction) {
		//TODO: move character
	}
	
	public void shoot(Vector direction){
		//TODO: shoot bullets
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
