package pistolcave;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Image;

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
public class Block extends IsoEntity {
	int tall;
	public Block(Vector wWorldSize, Vector wPosition, int i) {
		super(wWorldSize, PistolCaveGame.TILE_SIZE);
		this.tall = i;
		Image isoImg = null;
		switch(i){
		case 0: 
		//	isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveWallsTileSetPath, 64, 105).getSprite(0,0); 
			isoImg = ResourceManager.getImage(PistolCaveGame.tallBlockImgPath);
			break;
		case 1:
			isoImg = ResourceManager.getImage(PistolCaveGame.shortBlockImgPath);
			break;
		case 2:
			isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveWallsTileSetPath, 64, 105).getSprite(0,0); 
			break;
		case 3:
			//isoImg =ResourceManager.getImage(PistolCaveGame.tallBlockImgPath);
			isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveWallsTileSetPath, 64, 105).getSprite(11,0); 
			break;
		case 4:
			isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveWallsTileSetPath, 64, 105).getSprite(10,0); 
			break;
		case 5:
			isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveWallsTileSetPath, 64, 105).getSprite(1,0); 
			break;
		case 6:
			isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveWallsTileSetPath, 64, 105).getSprite(2,0); 
			break;
		case 7:
			isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveWallsTileSetPath, 64, 105).getSprite(3,0); 
			break;
		case 8:
			isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveWallsTileSetPath, 64, 105).getSprite(1,0); 
			break;
		case 9:
			isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveWallsTileSetPath, 64, 105).getSprite(1,0); 
			break;
			
		default:
			isoImg = ResourceManager.getImage(PistolCaveGame.tallBlockImgPath); 
		}

		addImage(isoImg);
		setZHeightFromIsoImage(isoImg);
		setPosition(wPosition);
	}
	public int gettall(){
		return tall;
	}
	
}