package betes.gui.dessin;


import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import betes.donnees.ContraintesParametres;
import betes.donnees.EnvironnementDepot;
import betes.modeles.environnement.Grille;

import java.awt.Graphics;
import java.awt.Point;

public class JCanvas extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9110685168260612529L;
	private CopyOnWriteArrayList<IDrawable> drawables = new CopyOnWriteArrayList<IDrawable>();

	public CopyOnWriteArrayList<Object> findDrawables(Point p) {
		CopyOnWriteArrayList<Object> l = new CopyOnWriteArrayList<>();
		for (Iterator<IDrawable> iter = drawables.iterator(); iter.hasNext();) {
			IDrawable element = (IDrawable) iter.next();
			if (element.getRectangle().contains(p)) {
				l.add(element);
			}
		}
		return l;
	}

	public void paint(Graphics g) {
		EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
		Grille grille = eDepot.getGrille();
		int grilleX = grille.getTailleX();
		int grilleY = grille.getTailleY();
		for (Iterator<IDrawable> iter = drawables.iterator(); iter.hasNext();) {
			 iter.next().draw(g);
		}
		for (int i=0;i<=grilleY; i++) {
			g.drawString(String.valueOf(i), 1, i*ContraintesParametres.DIMY+ContraintesParametres.DIMY);
		}
		for (int i=1;i<=grilleX; i++) {
			g.drawString(String.valueOf(i), i*ContraintesParametres.DIMX, 2+ContraintesParametres.DIMX);
		}
	}

	public void addDrawable(IDrawable d) {
		drawables.add(d);
	}

	public void removeDrawable(IDrawable d) {
		drawables.remove(d);
	}

	public void clear() {
		drawables.clear();
	}}