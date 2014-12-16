package pistolcave;

import jig.Entity;
import jig.ResourceManager;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.TrueTypeFont;
import java.io.InputStream;
import java.awt.Font;



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
public class Button extends Entity {
	
	public static final int MENU_LARGE = 0;
	
	private java.lang.String text;
	private float textSize;
	private float width;
	private float height;
	private int style;
	private Font awtFont;
	private TrueTypeFont custom;
	
	/* Constructor */
	
	public Button(java.lang.String string, float x, float y, int bstyle, int tsize)
			throws SlickException{
		super(x, y);
		text = string;
		textSize = tsize;
		style = bstyle;	
		getStyleImage();
		setFont();
	}
	
	public Button(java.lang.String string, float x, float y, int tsize)
			throws SlickException{
		super(x, y);
		text = string;	
		textSize = tsize;
		style = -1;
		getStyleImage();
		setFont();
	}
	
	public Button(java.lang.String string, float x, float y)
			throws SlickException{
		super(x, y);
		text = string;	
		style = -1;
		getStyleImage();
		setFont();
	}
	
	/* Getters */
	public String getText(){
		return text;
	}
	
	public float getTextSize(){
		return textSize;
	}
	
	public int getStyle(){
		return style;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public void getStyleImage(){
		//Image gui = ResourceManager.getImage(RogueGame.GOLDGUI_IMG_RSC);
		switch(style){
		case 0:
			addImageWithBoundingBox(ResourceManager.getImage(PistolCaveGame.GUI_MENULARGE_IMG_RSC));
		default:
			break;
		}
	}
	
	/* Static Getter for Style*/
	public static float getHeightfromStyle(int style){
		switch(style){
		case 0:
			return 40;
		default:
			return 0;
		}
	}
	
	/* Setters */
	public void setText(java.lang.String set){
		text = set;
	}
	
	public void setTextSize(int set){
		textSize = set;
	}
	
	public void setStyle(int set){
		style = set;
	}
	
	public void setWidth(float set){
		width = set;
	}
	
	public void setFont(){

		try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream(PistolCaveGame.ALAGARD_FONT_RSC);
			
			awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont = awtFont.deriveFont(this.textSize); // set font size
			custom = new TrueTypeFont(awtFont, false);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	*/
	@Override
	public void render(Graphics g){
		super.render(g);

		g.setFont(custom);
		g.drawString(text, 
				getX() - (custom.getWidth(text)/2), 
				getY() - (custom.getHeight(text)/2));
		
		/**
		UnicodeFont alagardFont = new UnicodeFont(
				new java.awt.Font("Alagard", Font.BOLD, textSize));
		alagardFont.getEffects().add(new ColorEffect(java.awt.Color.white));
		alagardFont.addNeheGlyphs();
		try {
			alagardFont.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		g.setFont(alagardFont);
		g.drawString(text,
			getX() - (alagardFont.getWidth(text)/2),
			getY() - (alagardFont.getHeight(text)/2));	
		**/
	}
	
	/* Action */
	// TODO: clickButton(), toggleButton()
}
