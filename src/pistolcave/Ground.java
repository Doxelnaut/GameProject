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
public class Ground extends IsoEntity {
	public Ground(Vector wWorldSize, Vector wPosition) {
		super(wWorldSize,PistolCaveGame.TILE_SIZE);
		
//		Image isoImg = ResourceManager.getSpriteSheet(RogueGame.groundSheetPath, 64, 32).getSprite(0,0); 
		Image isoImg = ResourceManager.getSpriteSheet(PistolCaveGame.caveTileSetPath, 64, 32).getSprite(0,0); 
		addImage(isoImg);
		setZHeightFromIsoImage(isoImg);
		setPosition(wPosition);
	}
}
