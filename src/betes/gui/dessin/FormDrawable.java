package betes.gui.dessin;

import java.awt.*;

import betes.gui.moteur.ImageLoading;
import betes.modeles.environnement.Case;

public abstract class FormDrawable extends Case implements IDrawable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3203809627538908270L;
	protected Rectangle rect;
	protected Color color;
	protected Point point;
	protected Image img;
	protected ImageLoading loadImage = new ImageLoading();
	
	public FormDrawable () {
	}
	
	public FormDrawable (String filepath, int x, int y){
		this.img = loadImage.getImage(filepath);
	}

	public FormDrawable(Color color, Point pos, Dimension dim) {
		this.color = color;
		this.rect = new Rectangle(pos, dim);
	}

	public FormDrawable(Point pos){
		this.point = new Point(pos);
	}

	public abstract void draw(Graphics g);

	public Rectangle getRectangle() {
		return (Rectangle) rect.clone();
	}

}