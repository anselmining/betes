package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.ContraintesParametres;
import betes.donnees.GenerationAleatoire;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.NourritureSurCaseException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.frames.InGame;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;

public class VisiteurFuire implements Visiteur {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerUtility.getLogger(VisiteurFuire.class);
	Case caseAFuir;

	public VisiteurFuire(Case caseAfuire) {
		this.caseAFuir = caseAfuire;
	}

	@Override
	public void visite(Bete bete) {
		int distanceFuite = GenerationAleatoire.nombreAleatoire(ContraintesParametres.DISTANCE_FUITE_MIN, ContraintesParametres.DISTANCE_FUITE_MAX);
		int x = bete.getPositionX();
		int y = bete.getPositionY();
		Grille grille = eDepot.getGrille();
		int grilleX = grille.getTailleX();
		int grilleY = grille.getTailleY();
		Boolean fuiteTerminee = false;

		while (fuiteTerminee.equals(false) && distanceFuite > 0) {
			for (int i = x - distanceFuite; fuiteTerminee.equals(false) && i <= x + distanceFuite; i = i + distanceFuite++) {
				for (int j = y - distanceFuite; fuiteTerminee.equals(false) && j <= y + distanceFuite; j = j + distanceFuite) {
					if (i == x && j == y) { // ignore la position de la Bête
						continue;
					}
					if (i < 1 || j < 1 || i > grilleX || j > grilleY) {
						continue;
					}

					Case caseVue = eDepot.rechercher(i, j);
					if (eDepot.rechercherBete(caseVue) == null && caseVue.isTraversable().equals(true)) {
						bete.setPosition(caseVue);
						fuiteTerminee = true;
					}

				}
			}
			distanceFuite--;
		}

		if (ContraintesParametres.modeConsole.equals(true)) {
			Console.afficherGrille();
		}
	}

	/**Donne une Case pour qu'une bête puisse s'échapper.
	 * @param bete :une bête.
	 * @return (Case):Case libre pour la bête.
	 */
	public Case donneCaseDEchappement(Bete bete) {
		int distanceFuite = GenerationAleatoire.nombreAleatoire(ContraintesParametres.DISTANCE_FUITE_MIN, ContraintesParametres.DISTANCE_FUITE_MAX);
		int x = bete.getPositionX();
		int y = bete.getPositionY();
		Grille grille = eDepot.getGrille();
		int grilleX = grille.getTailleX();
		int grilleY = grille.getTailleY();
		Case caseVue = null;

		while (distanceFuite > 0) {
			for (int i = x - distanceFuite; i <= x + distanceFuite; i = i + distanceFuite++) {
				for (int j = y - distanceFuite; j <= y + distanceFuite; j = j + distanceFuite) {
					if (i == x && j == y) { // ignore la position de la Bête
						continue;
					}
					if (i < 1 || j < 1 || i >= grilleX || j >= grilleY) {
						continue;
					}
					caseVue = eDepot.rechercher(i, j);

					if (eDepot.rechercherBete(caseVue) == null && caseVue.isTraversable().equals(true)) {
						return caseVue;
					}

				}
			}
			distanceFuite--;
		}

		return bete.getPosition();
	}

	@Override
	public void visite(Case laCase) throws CaseInexistanteException, CaseNonTraversableException, BeteSurCaseException, NourritureSurCaseException {
	}

	@Override
	public void visite(Grille grille) {
	}

	@Override
	public void visite(Console console) {
	}

	@Override
	public void visite(Bete bete, Bete autreBete) {
	}

	@Override
	public void visite(GraphicUserInterface gui) {
	}


	@Override
	public JCanvas visite(InGame ig, JCanvas jc, int r, int g, int b) {
		return jc;
	}
}
