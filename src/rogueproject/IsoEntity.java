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
	public Vector getwWorldOrigin() {
		return wWorldOrigin;
	}

	public void setwWorldOrigin(Vector wWorldOrigin) {
		this.wWorldOrigin = wWorldOrigin;
	}

	public float getzHeight() {
		return zHeight;
	}

	public void setzHeight(float zHeight) {
		this.zHeight = zHeight;
	}

	public Vector getFooting() {
		return footing;
	}

	public void setFooting(Vector footing) {
		this.footing = footing;
	}

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

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
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


