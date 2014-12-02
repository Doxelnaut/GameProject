package rogueproject;

import jig.ConvexPolygon;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

public class IsoEntity extends Entity implements Comparable<IsoEntity> {

	private Vector wWorldOrigin;
	float wTileSize;
	Vector wPosition; // position in world coordinates
	float zHeight;
	Vector footing;
	
	public IsoEntity(Vector wWorldSz, float wTileSz) {
		super();
		wTileSize = wTileSz;
		wWorldOrigin = new Vector(40, wWorldSz.getY()/2);
	}

	public void setZHeightFromIsoImage(Image i) {
		setZHeightFromIsoImage(i, 0);
	}
	/**
	 * Not all images are aligned the way I expect,
	 * so instead of tweaking images, add a fudge factor
	 * to tweak the zheight
	 * 
	 * @param delta - a fudge factor for the height
	 */
	public void setZHeightFromIsoImage(Image i, int delta) {
		// height = 2*wTileSize.getY() + zHeight
		zHeight = i.getHeight() - 2*wTileSize - delta;
		
		float [] points = new float[8];
		points[0] = -wTileSize*2;
		points[1] = zHeight/2;
		points[2] = 0;
		points[3] = zHeight/2+wTileSize;

		points[4] = wTileSize*2;
		points[5] = zHeight/2;
		points[6] = 0;
		points[7] = zHeight/2-wTileSize;
		
		// we want a bounding shape that is at the "ground level"
		// based on the tile's footprint, not based on the
		// entire image height...
		addShape(new ConvexPolygon(points));
		
	}

	/**
	 * Takes a position in the world coordinate system and computes
	 * a position in the (screen) rendering coordinate system
	 * 
	 *     p = rendering position anchor
	 *     W = wPosition 'center'
	 *     
	 *       wTileSize*2
	 *      <   > 
	 *     |     |
	 *     p     +             ---------
	 *         ++ ++           ^        ^ wTileSize
	 *       ++     ++                  v
	 *     ++         ++           -----
	 *     | ++     ++ |               ^
	 *     |   ++ ++   |
	 *     |     +     |
	 *     |     |     |
	 *     |           |
	 *     |     |     |                zHeight
	 *     |     |     |               v
	 *     ++    W    ++              --
	 *       ++  |  ++          iso image size
	 *         ++|++          v
	 *           +            ----
	 */
	public void setPosition(Vector w) {
		wPosition = w;
		Vector p = new Vector(wWorldOrigin.getX() + 2*wTileSize + (2*w.getX() + 2*w.getY()),
				wWorldOrigin.getY() - zHeight/2 + wTileSize + w.getY() - w.getX());
		footing = new Vector(p.getX(), p.getY()+zHeight/2);

	//	System.out.println(this.getClass().toString() + "  ZH  " + zHeight);
		super.setPosition(p);

	}
	/**
	 * @return the position in the world coordinate system
	 */
	public Vector getPosition() {
		// gets wPosition
		return wPosition;
	}

	@Override
	public int compareTo(IsoEntity other) {
		// {(x, y) such that x.compareTo(y) <= 0}.
		// order from LOW to HIGH on Y axis
		if (footing.getY() > other.footing.getY() ) return 1;   // this AFTER other
		if (footing.getY() < other.footing.getY() ) return -1;  // this BEFORE other
	
		// Break ties with X axis
		// order from HIGH to LOW on X axis
		if (footing.getX() < other.footing.getX() ) return 1; 	// this AFTER other  
		if (footing.getX() > other.footing.getX() ) return -1;	// this BEFORE other

		return 0;
	}


}


class Ground extends IsoEntity {
	public Ground(Vector wWorldSize, Vector wPosition) {
		super(wWorldSize,IsoWorldGame.tileSize);
		
		Image isoImg = ResourceManager.getSpriteSheet(IsoWorldGame.groundSheetPath, 64, 32).getSprite(0,0); 
		addImage(isoImg);
		setZHeightFromIsoImage(isoImg);
		setPosition(wPosition);
	}
}

class Block extends IsoEntity {
	public Block(Vector wWorldSize, Vector wPosition, boolean tall) {
		super(wWorldSize, IsoWorldGame.tileSize);
		Image isoImg;
		if (tall)
			isoImg = ResourceManager.getImage(IsoWorldGame.tallBlockImgPath); 
		else
			isoImg = ResourceManager.getImage(IsoWorldGame.shortBlockImgPath);
		
		addImage(isoImg);
		setZHeightFromIsoImage(isoImg);
		setPosition(wPosition);
	}
	
}


class Minotaur extends IsoEntity {
	Animation[] walking = new Animation[8];
	
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
	
	public Minotaur(Vector wWorldSize, Vector wPosition) {
		super(wWorldSize, IsoWorldGame.tileSize);
		wWorldSz = wWorldSize;
		
		removeAnimation(walking[current]);
		wWorldSz = wWorldSize;
		
		walking[LEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkLeft, 99, 135), 0,0,14,0, true, 70, true);
		walking[RIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkRight,99, 135), 0,0,14,0, true, 70, true);
		walking[UP] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkUp, 95, 135), 0,0,14,0, true, 70, true);
		walking[DOWN] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkDown, 93, 135), 0,0,14,0, true, 70, true);
		walking[UpLEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkUpLeft, 101, 127), 0,0,14,0, true, 70, true);
		walking[UpRIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkUpRight, 85, 138), 0,0,14,0, true, 70, true);
		walking[DownLEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkDownLeft, 83, 139), 0,0,14,0, true, 70, true);
		walking[DownRIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.WalkDownRight, 105, 127), 0,0,14,0, true, 70, true);
		current = RIGHT;

		
//		public static final String WalkLeft = "resource/Walk_10(15,69,111).png";
//		public static final String WalkRight = "resource/Walk_2(15,62,111).png";
//		public static final String WalkUp = "resource/Walk_6(15,66,111).png";
//		public static final String WalkDown = "resource/Walk_14(15,61,108).png";
//		public static final String WalkUpLeft = "resource/Walk_8(15,68,110).png";
//		public static final String WalkUpRight = "resource/Walk_4(15,63,112).png";
//		public static final String WalkDownLeft = "resource/Walk_12(15,63,108).png";
//		public static final String WalkDownRight = "resource/Walk_0(15,63,110).png";

		
		addAnimation(walking[current]);
		setZHeightFromIsoImage(walking[current].getCurrentFrame(), 32);
		setPosition(wPosition);
		lastWPosition = wPosition;
		
		System.out.println("Minotaur zh:" + zHeight);

		ouch = ResourceManager.getSound(IsoWorldGame.ouchSoundPath);
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

}

class Fireball extends IsoEntity {

	Animation[] moving = new Animation[5];
	int current;
	static final int LEFT = 0;
	static final int RIGHT = 1;
	static final int UP = 2;
	static final int DOWN = 3;
	static final int KABOOM = 4;
	Vector domain;
	
	public Fireball(Vector wWorldSize, Vector wPosition, int dir) {
		super(wWorldSize, IsoWorldGame.tileSize);
		domain = wWorldSize;

		moving[LEFT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.fireballSheetPath, 64, 64), 0,7,7,7, true, 20, true);
		moving[RIGHT] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.fireballSheetPath, 64, 64), 0,3,7,3, true, 20, true);
		moving[UP] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.fireballSheetPath, 64, 64), 0,1,7,1, true, 20, true);
		moving[DOWN] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.fireballSheetPath, 64, 64), 0,5,7,5, true, 20, true);
		moving[KABOOM] = new Animation(ResourceManager.getSpriteSheet(IsoWorldGame.explosionSheetPath, 256, 128), 20);
		current = dir;
		addAnimation(moving[current]);
		setZHeightFromIsoImage(moving[current].getCurrentFrame(), 32);
		System.out.println("Fireball zh:" + zHeight);
		setPosition(wPosition);
	}
	public void update(float n) {
		float x, y;
		if (current == KABOOM) return;
		
		if (current < 2) {x = n; y = 0;} 
		else {x = 0; y=n;}
		if (current % 2 == 0) {x = -x; y = -y;}
		
		setPosition(getPosition().add(new Vector(x,y)));
	}
	public void kaboom() {
		removeAnimation(moving[current]);
		current = KABOOM;
		addAnimation(moving[current]);
		moving[current].setLooping(false);
	}
	public boolean done() {
		
		if (wPosition.getX() < 0 || wPosition.getX() > domain.getX() ||
				wPosition.getY() < 0 || wPosition.getY() > domain.getY()) return true;
		if (current == KABOOM) return moving[current].isStopped();
		return false;
	}
}


