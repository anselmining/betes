package betes.gui.dessin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

public class RectangleDrawable extends FormDrawable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9027481611465426887L;

	public RectangleDrawable(Color color, Point pos, Dimension dim) {
		super(color, pos, dim);
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.fillRect(rect.x, rect.y, rect.height, rect.width);
		g.setColor(c);
	}
}