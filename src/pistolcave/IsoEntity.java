package pistolcave;

import jig.ConvexPolygon;
import jig.Entity;
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
public class IsoEntity extends Entity implements Comparable<IsoEntity> {

	private Vector wWorldOrigin;
	float wTileSize;
	Vector wPosition; // position in world coordinates
	float zHeight;
	Vector footing;
	
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

	public void setPosition(Vector w) {
		wPosition = w;
		Vector p = new Vector(wWorldOrigin.getX() + 2*wTileSize + (2*w.getX() + 2*w.getY()),
				wWorldOrigin.getY() - zHeight/2 + wTileSize + w.getY() - w.getX());
		footing = new Vector(p.getX(), p.getY()+zHeight/2);

	//	System.out.println(this.getClass().toString() + "  ZH  " + zHeight);
		super.setPosition(p);

	}
	
	public void setPositionNoTranslate(Vector w){
		wPosition = w;
		super.setPosition(w);
	}
	
	/** 
	 * @return the position in the world coordinate system
	 */
	public Vector getPosition() {
		// gets wPosition
		return wPosition;
	}
	
	public Vector getEPosition(){
		return super.getPosition();
	}
	
	@Override
	public void translate(final Vector t) {
		this.setPosition( wPosition.add(t));
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
	public PathFinder getPath(){return null;}
}


