package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.GenerationAleatoire;
import betes.donnees.GenerationAleatoireEtFixeConsole;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.frames.InGame;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;

public class VisiteurPlacerDecor implements Visiteur {
	private static Logger logger = LoggerUtility.getLogger(VisiteurPlacerDecor.class);

	@Override
	public void visite(Grille grille) {

		for (int i = 1; i <= grille.getTailleX(); i++) {
			for (int j = 1; j <= grille.getTailleY(); j++) {

				try {
					visite(eDepot.rechercher(i, j));
				} catch (CaseInexistanteException e) {
					logger.fatal("Case introuvable sur la grille[" + grille.getTailleX() + "][" + grille.getTailleY() + "]");
				}

			}
		}
	}

	@Override
	public void visite(Case laCase) throws CaseInexistanteException {
		int frequencePlacement = GenerationAleatoire.nombreAleatoire(0, 8);

		if (laCase == null) {
			throw new CaseInexistanteException();
		}

		switch (frequencePlacement) {
		case 2:
			creerDecor(laCase, "Roche", " # ", false);
			break;
		default:
			creerDecor(laCase, "Herbe", " , ", true);
		}
	}

	/**Création du décor sur la Grille.
	 * @param laCase :Case à décorer.
	 * @param nom :nom du décor.
	 * @param image :image du décor.
	 * @param traversable :la traversabilité de la case.
	 */
	public void creerDecor(Case laCase, String nom, String image, Boolean traversable) {
		laCase = eFabrique.creer(laCase.getX(), laCase.getY(), nom, image, traversable);
		eDepot.enregistrer(laCase);
	}

	@Override
	public void visite(Bete bete) {
	}

	@Override
	public void visite(Console console) {
	}

	@Override
	public void visite(Bete bete, Bete autreBete) {
	}

	/**Place le décor (en mode console uniquement).
	 * @return (Boolean):faux si l'utilisateur décide de quitter le placemenet du décor, vrai sinon. 
	 */
	public Boolean placerDecor() {
		String nom = "", image = "";
		Boolean traversable = true;
		int choixPlacement, choixDecor;
		int caseX1 = 0, caseX2 = 0, caseY1 = 0, caseY2 = 0, caseTemp;
		Grille grille = eDepot.getGrille();
		Case laCase;

		logger.warn("[         [ Comment placer le décor ? (0) Globalement (1) Rectangulairement (2) Localement (3) quitter");
		choixPlacement = GenerationAleatoireEtFixeConsole.nombre(0, 3);
		if (choixPlacement == 3) {
			return false;
		}
		if (choixPlacement == 1) {
			logger.warn("[         [ Définissez la ligne de début du rectangle décoré.");
			caseX1 = GenerationAleatoireEtFixeConsole.nombre(0, grille.getTailleX());
			logger.warn("[         [ Définissez la ligne de fin inférieur du rectangle décoré.");
			caseX2 = GenerationAleatoireEtFixeConsole.nombre(0, grille.getTailleX());
			logger.warn("[         [ Définissez la colonne de début du rectangle décoré.");
			caseY1 = GenerationAleatoireEtFixeConsole.nombre(0, grille.getTailleY());
			logger.warn("[         [ Définissez la colonne de fin du rectangle décoré.");
			caseY2 = GenerationAleatoireEtFixeConsole.nombre(0, grille.getTailleY());
			if (caseX1 > caseX2) {
				caseTemp = caseX1;
				caseX1 = caseX2;
				caseX2 = caseTemp;
			}
			if (caseY1 > caseY2) {
				caseTemp = caseY1;
				caseY1 = caseY2;
				caseY2 = caseTemp;
			}
		}

		logger.warn("[         [ Quel décor placer ? (0) Vide (1) Herbe (2) Roche (3) quitter");
		choixDecor = GenerationAleatoireEtFixeConsole.nombre(0, 3);
		if (choixDecor == 3) {
			return false;
		}

		switch (choixDecor) {
		case 0:
			nom = "Vide";
			image = " . ";
			traversable = true;
			break;
		case 1:
			nom = "Herbe";
			image = " , ";
			traversable = true;
			break;
		case 2:
			nom = "Roche";
			image = " # ";
			traversable = false;
			break;
		case -1:
			return false;
		}

		switch (choixPlacement) {
		case 0:// placement global
			for (int i = 1; i <= grille.getTailleX(); i++) {
				for (int j = 1; j <= grille.getTailleY(); j++) {
					if (eDepot.rechercherBete(eFabrique.creer(i, j)) == null) {
						laCase = eFabrique.creer(i, j, nom, image, traversable);
						eDepot.enregistrer(laCase);
					}
				}
			}
			break;
		case 1:// placement rectangulaire
			for (int i = caseX1; i <= caseX2; i++) {
				for (int j = caseY1; j <= caseY2; j++) {
					if (eDepot.rechercherBete(eFabrique.creer(i, j)) == null) {
						laCase = eFabrique.creer(i, j, nom, image, traversable);
						eDepot.enregistrer(laCase);
					}
				}
			}
			break;
		case 2:// placement local
			laCase = GenerationAleatoireEtFixeConsole.position();
			if (laCase == null) {
				logger.warn("[         [ Pas de case sélectionnée.\n");
				return false;
			} else {
				if (eDepot.rechercherBete(eFabrique.creer(laCase.getX(), laCase.getY())) == null) {
					creerDecor(laCase, nom, image, traversable);
				}
			}
			break;
		}

		return true;
	}

	@Override
	public void visite(GraphicUserInterface gui) {
	}

	@Override
	public JCanvas visite(InGame ig, JCanvas jc, int r, int g, int b) {
		return jc;
	}

}
