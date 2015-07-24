package betes.visiteurs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import betes.console.Console;
import betes.donnees.ContraintesParametres;
import betes.donnees.GenerationAleatoire;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.NourritureSurCaseException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.ImageDrawable;
import betes.gui.dessin.JCanvas;
import betes.gui.dessin.RectangleDrawable;
import betes.gui.frames.InGame;
import betes.gui.moteur.EvenementsRegistre;
import betes.gui.moteur.ImageLoading;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;
import betes.modeles.environnement.Nourriture;

public class VisiteurMiseAjourGrille implements Visiteur {

	public int grilleX = eDepot.getGrille().getTailleX() + 1;
	public int grilleY = eDepot.getGrille().getTailleY() + 1;
	private String direction;
	private ImageDrawable[][] imagesBetes = new ImageDrawable[grilleX][grilleY];
	private ImageDrawable[][] imagesNourriture = new ImageDrawable[grilleX][grilleY];
	private RectangleDrawable[][] rectangles = new RectangleDrawable[grilleX][grilleY];
	private ImageDrawable[][] rectanglesFilet = new ImageDrawable[grilleX][grilleY];
	private ImageDrawable[][] imagesNature = new ImageDrawable[grilleX][grilleY];
	private ImageLoading loadImage = new ImageLoading();
	private Case[][] tempCase = new Case[grilleX][grilleY];
	private VisiteurPlacerDecor decorateur = new VisiteurPlacerDecor();
	private EvenementsRegistre rEvent = EvenementsRegistre.getInstance();

	/**
	 * Rafraichissement des images de nourriture.
	 * 
	 * @param jc
	 *            :JCanvas pour les images.
	 * @return (jc):JCanvas pour les images.
	 */
	private JCanvas updateNourritureGrille(JCanvas jc) {
		tempCase = eDepot.getCases();
		for (int i = 0; i < grilleX; i++) {
			for (int j = 0; j < grilleY; j++) {
				if (tempCase[i][j] instanceof Nourriture) {
					int ipos = tempCase[i][j].getX();
					int jpos = tempCase[i][j].getY();
					imagesNourriture[ipos][jpos] = new ImageDrawable(
							loadImage.randomFeuilleImage(), ipos
									* ContraintesParametres.DIMX, jpos
									* ContraintesParametres.DIMY);
					imagesNourriture[ipos][jpos].setPositionX(ipos);
					imagesNourriture[ipos][jpos].setPositionY(jpos);
					jc.addDrawable(imagesNourriture[ipos][jpos]);
				}
			}
		}
		eDepot.setImagesNourriture(imagesNourriture);
		return jc;
	}

	/**
	 * Rafraichissement des images des betes.
	 * 
	 * @param jc
	 *            :JCanvas pour les images.
	 * @return (jc):JCanvas pour les images.
	 */
	private JCanvas updateBetesGrille(JCanvas jc) {
		int k;
		ClearBetesGrille(jc);
		if (eDepot.getNomreBetesAccueil() != 0) {
			for (k = 0; k < eDepot.getNomreBetesAccueil(); k++) {
				int i = eDepot.rechercher(k).getPositionX();
				int j = eDepot.rechercher(k).getPositionY();
				if (i > -1 && j > -1) {
					int t = eDepot.rechercher(k).getPositionAvantX();
					int p = eDepot.rechercher(k).getPositionAvantY();

					if (i - t > 0) {
						direction = "Est";
					} else if (i - t < 0) {
						direction = "Ouest";
					}

					if (j - p > 0) {
						direction = "Sud";
					} else if (j - p < 0) {
						direction = "Nord";
					}

					imagesBetes[i][j] = new ImageDrawable(
							loadImage.randomBeteImage(eDepot.rechercher(k)
									.getSexe(), direction),
							(i * ContraintesParametres.DIMX),
							(j * ContraintesParametres.DIMY));

					imagesBetes[i][j].setPositionX(i);
					imagesBetes[i][j].setPositionY(j);
					jc.addDrawable(imagesBetes[i][j]);
				}
				eDepot.setImagesBetes(imagesBetes);
			}
		}
		return jc;
	}

	/**
	 * Nettoyage des images des betes.
	 * 
	 * @param jc
	 *            :JCanvas pour les images.
	 * @return (jc):JCanvas pour les images.
	 */
	private JCanvas ClearBetesGrille(JCanvas jc) {
		int k;
		for (k = 0; k < eDepot.getNomreBetesAccueil(); k++) {
			for (int i = 0; i < grilleX; i++) {
				for (int j = 0; j < grilleY; j++) {
					if (imagesBetes[i][j] != null) {
						jc.removeDrawable(imagesBetes[i][j]);
					}
				}
			}
		}
		eDepot.setImagesBetes(imagesBetes);
		jc.clear();

		for (int i = 0; i < grilleX; i++) {
			for (int j = 0; j < grilleY; j++) {
				jc.addDrawable(rectangles[i][j]);

				if (imagesNature[i][j] != null) {
					jc.addDrawable(imagesNature[i][j]);
				}

				if (rectanglesFilet[i][j] != null) {
					jc.addDrawable(rectanglesFilet[i][j]);
				}
			}
		}
		return jc;
	}

	/**
	 * Creation de la toile de rectangle pour les images.
	 * 
	 * @param jc
	 *            :JCanvas pour les images.
	 * @param i
	 *            :coordonnee X
	 * @param j
	 *            :coordonnee Y
	 * @return (jc):JCanvas pour les images.
	 */
	public JCanvas creerGrille(JCanvas jc, int r, int g, int b) {
		Dimension dim = new Dimension(ContraintesParametres.DIMX,
				ContraintesParametres.DIMY);
		for (int i = 0, pi = 0; i < grilleX; i++, pi = pi
				+ ContraintesParametres.DIMX) {
			for (int j = 0, pj = 0; j < grilleY; j++, pj = pj
					+ ContraintesParametres.DIMY) {
				rectangles[i][j] = new RectangleDrawable(new Color(r, g, b),
						new Point(pi, pj), dim);
				rectangles[i][j].setPositionX(i);
				rectangles[i][j].setPositionY(j);
				jc.addDrawable(rectangles[i][j]);
			}
		}
		eDepot.setRectangles(rectangles);
		return jc;
	}

	/**
	 * Creation du filet de coordonnees.
	 * 
	 * @param jc
	 *            :JCanvas pour les images.
	 * @return (jc):JCanvas pour les images.
	 */
	public JCanvas creerFilet(JCanvas jc) {
		for (int i = 0; i < grilleX; i++) {
			for (int j = 0; j < grilleY; j++) {
				rectanglesFilet[i][j] = new ImageDrawable(
						loadImage.creerFilet(), i * ContraintesParametres.DIMX,
						j * ContraintesParametres.DIMY);
				rectanglesFilet[i][j].setPositionX(i);
				rectanglesFilet[i][j].setPositionY(j);
				jc.addDrawable(rectanglesFilet[i][j]);
			}
		}
		eDepot.setRectanglesFilet(rectanglesFilet);
		return jc;
	}

	/**
	 * Creation de la decor nature.
	 * 
	 * @param jc
	 *            :JCanvas pour les images.
	 * @return (jc):JCanvas pour les images.
	 */
	public JCanvas creerNature(JCanvas jc) {
		tempCase = eDepot.getCases();
		for (int i = 0; i < grilleX; i++) {
			for (int j = 0; j < grilleY; j++) {
				if (GenerationAleatoire.nombreAleatoire(0, 10) < 3) {
					try {
						if (eDepot.rechercherBete(tempCase[i][j]) == null) {
							decorateur.visite(tempCase[i][j]);
						}
					} catch (CaseInexistanteException e) {
					}
					if (tempCase[i][j].isTraversable()) {
						imagesNature[i][j] = new ImageDrawable(
								loadImage.randomNatureImage(), i
										* ContraintesParametres.DIMX, j
										* ContraintesParametres.DIMY);
					} else {
						imagesNature[i][j] = new ImageDrawable(
								loadImage.randomDecorImage(), i
										* ContraintesParametres.DIMX, j
										* ContraintesParametres.DIMY);
					}
					imagesNature[i][j].setPositionX(i);
					imagesNature[i][j].setPositionY(j);
					jc.addDrawable(imagesNature[i][j]);
				}
			}
		}
		eDepot.setImagesNature(imagesNature);
		return jc;
	}

	/**
	 * Suppression de la decor intraversible.
	 * 
	 * @param jc
	 *            :JCanvas pour les images.
	 * @param i
	 *            :coordonnee X
	 * @param j
	 *            :coordonnee Y
	 * @return (jc):JCanvas pour les images.
	 */
	public JCanvas supprimerDecor(JCanvas jc, int i, int j) {
		tempCase = eDepot.getCases();
		imagesNature = eDepot.getImagesNature();

		if (!tempCase[i][j].isTraversable()) {
			tempCase[i][j].setTraversable(true);

			if (imagesNature[i][j] != null) {
				jc.removeDrawable(imagesNature[i][j]);
				imagesNature[i][j] = new ImageDrawable(
						loadImage.randomNatureImage(), i
								* ContraintesParametres.DIMX, j
								* ContraintesParametres.DIMY);

				imagesNature[i][j].setPositionX(i);
				imagesNature[i][j].setPositionY(j);
				jc.addDrawable(imagesNature[i][j]);

				rEvent.registreEvent("Le décor [" + i + "]-[" + j
						+ "] est supprimé!");
			}
		} else {
			rEvent.registreEvent("La cellule [" + i + "]-[" + j
					+ "] n'est pas un décor!");
		}

		eDepot.setImagesNature(imagesNature);
		return jc;
	}

	/**
	 * Creation de la decor intraversible.
	 * 
	 * @param jc
	 *            :JCanvas pour les images.
	 * @param i
	 *            :coordonnee X
	 * @param j
	 *            :coordonnee Y
	 * @return (jc):JCanvas pour les images.
	 */
	public JCanvas ajouterDecor(JCanvas jc, int i, int j) {
		tempCase = eDepot.getCases();
		imagesNature = eDepot.getImagesNature();

		if (tempCase[i][j].isTraversable()) {
			tempCase[i][j].setTraversable(false);

			if (imagesNature[i][j] != null) {
				jc.removeDrawable(imagesNature[i][j]);
			}

			imagesNature[i][j] = new ImageDrawable(
					loadImage.randomDecorImage(), i
							* ContraintesParametres.DIMX, j
							* ContraintesParametres.DIMY);
			imagesNature[i][j].setPositionX(i);
			imagesNature[i][j].setPositionY(j);
			jc.addDrawable(imagesNature[i][j]);

			rEvent.registreEvent("Le décor [" + i + "]-[" + j
					+ "] est ajouté!");

		} else {

			rEvent.registreEvent("La cellule [" + i + "]-[" + j
					+ "] ne peut pas être modifié! ");
		}

		eDepot.setImagesNature(imagesNature);
		return jc;
	}

	@Override
	public void visite(Bete bete) {
	}

	@Override
	public void visite(Bete bete, Bete autreBete) {
	}

	@Override
	public void visite(Case laCase) throws CaseInexistanteException,
			CaseNonTraversableException, BeteSurCaseException,
			NourritureSurCaseException {
	}

	@Override
	public void visite(Grille grille) {
	}

	@Override
	public void visite(Console console) {
	}

	@Override
	public void visite(GraphicUserInterface gui) {
	}

	@Override
	public JCanvas visite(InGame ig, JCanvas jc, int r, int g, int b) {
		jc = updateBetesGrille(jc);
		jc = updateNourritureGrille(jc);
		return jc;
	}

}
