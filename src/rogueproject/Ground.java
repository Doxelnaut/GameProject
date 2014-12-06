package rogueproject;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Image;

public class Ground extends IsoEntity {
	public Ground(Vector wWorldSize, Vector wPosition) {
		super(wWorldSize,RogueGame.TILE_SIZE);
		
		Image isoImg = ResourceManager.getSpriteSheet(RogueGame.groundSheetPath, 64, 32).getSprite(0,0); 
		addImage(isoImg);
		setZHeightFromIsoImage(isoImg);
		setPosition(wPosition);
	}
}
