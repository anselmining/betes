package betes.gui.dessin;

import java.awt.Graphics;


public class ImageDrawable extends FormDrawable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9027481611465426887L;
	
	private int x;
	private int y;

	public ImageDrawable(String filepath, int x, int y) {
		super(filepath, x, y);
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g) {
		g.drawImage(img,x,y,null);
	}
}
