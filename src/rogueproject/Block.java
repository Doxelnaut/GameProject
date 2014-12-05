package rogueproject;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Image;

public class Block extends IsoEntity {
	boolean tall;
	public Block(Vector wWorldSize, Vector wPosition, boolean tall) {
		super(wWorldSize, IsoWorldGame.tileSize);
		this.tall = tall;
		Image isoImg;
		if (tall)
			isoImg = ResourceManager.getImage(IsoWorldGame.tallBlockImgPath); 
		else
			isoImg = ResourceManager.getImage(IsoWorldGame.shortBlockImgPath);
		
		addImage(isoImg);
		setZHeightFromIsoImage(isoImg);
		setPosition(wPosition);
	}
	public boolean gettall(){
		return tall;
	}
	
}