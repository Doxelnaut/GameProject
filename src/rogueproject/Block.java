package rogueproject;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Image;

public class Block extends IsoEntity {
	boolean tall;
	public Block(Vector wWorldSize, Vector wPosition, boolean tall) {
		super(wWorldSize, RogueGame.TILE_SIZE);
		this.tall = tall;
		Image isoImg;
		if (tall)
			isoImg = ResourceManager.getImage(RogueGame.tallBlockImgPath); 
		else
			isoImg = ResourceManager.getImage(RogueGame.shortBlockImgPath);
		
		addImage(isoImg);
		setZHeightFromIsoImage(isoImg);
		setPosition(wPosition);
	}
	public boolean gettall(){
		return tall;
	}
	
}