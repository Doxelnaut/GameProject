package rogueproject;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.Animation;

/**
 * A class representing a transient hit. The game should monitor
 * hits to determine when they are no longer active and remove/hide
 * them at that point.
 */
class Damage extends Entity {
	private Animation hit;

	public Damage(final float x, final float y, int amount) {
		super(x, y);
		switch(amount){
		case 0:
			hit = new Animation(ResourceManager.getSpriteSheet(
				RogueGame.HIT_REDNUMBERS0_IMG_RSC, 8, 8), 0, 0, 0, 0, true, 250,
				false);
			hit.addFrame(ResourceManager.getSpriteSheet(
					RogueGame.HIT_REDNUMBERS1_IMG_RSC, 8, 8).getSprite(0, 0), 250);
			break;
		default:
			break;
		}
		addAnimation(hit);
		hit.setLooping(false);
		//ResourceManager.getSound(RogueGame.SOMESOUND).play();
	}

	public boolean isActive() {
		return !hit.isStopped();
	}
}