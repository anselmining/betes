package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.donnees.GenerationAleatoire;
import betes.donnees.GenerationAleatoireEtFixeConsole;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;

public class VisiteurPlacerNourriture extends VisiteurPlacerDecor implements Visiteur {
	private static Logger logger = LoggerUtility.getLogger(VisiteurPlacerNourriture.class);

	/**Place de la nourriture sur la Grille.
	 * 
	 */
	public static void placerNourriture() {
		VisiteurPlacerNourriture cultivateur = new VisiteurPlacerNourriture();
		// ClasseMère.visite(Grille) appelle ensuite ClasseFille.visite(Case)
		eDepot.getGrille().accepteVisiteur(cultivateur);
	}

	@Override
	public void visite(Case laCase) throws CaseInexistanteException {
		// placement seulement sur cases vides
		if (laCase.isTraversable().equals(true) && laCase.getNomDecor() == null) {

			int frequencePlacement = GenerationAleatoire.nombreAleatoire(0, 20000);

			switch (frequencePlacement) {
			case 0:
				creerBouffe(laCase, "Graine", " ☉ ", 10, true);
				break;
			case 1:
				creerBouffe(laCase, "Feuille", " ☙ ", 20, true);
				break;
			}
		}
	}

	/**Création de la nourriture.
	 * @param laCase: la case ciblée.
	 * @param nom :nom de la nourriture.
	 * @param image:image de la nourriture.
	 * @param satiete :satiete de la nourriture.
	 * @param traversable :case traversable.
	 */
	public void creerBouffe(Case laCase, String nom, String image, int satiete, Boolean traversable) {
		laCase = eFabrique.creerNourriture(laCase.getX(), laCase.getY(), nom, image, satiete, true);
		eDepot.enregistrer(laCase);
	}

	/**Placer manuellement la nourrture sur la Grille.
	 * @return (Boolean): faux si l'utilisateur décide de quitter le placemenet de la nourriture, vrai sinon. 
	 */
	public Boolean placerManuellementNourriture() {
		String nom = "", image = "";
		Boolean traversable = true;
		int satiete = 0;
		int choix = 0, choixPlacement = 0;
		int caseX1 = 0, caseX2 = 0, caseY1 = 0, caseY2 = 0, caseTemp;
		Grille grille = eDepot.getGrille();
		Case laCase;

		logger.warn("[         [ Comment placer la nourriture ? (0) Globalement (1) Rectangulairement (2) Localement (3) quitter");
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

		logger.warn("[         [ Quelle nourriture placer ? (0) Graine (1) Feuille");
		choix = GenerationAleatoireEtFixeConsole.nombre(0, 1);

		switch (choix) {
		case 0:
			nom = "Graine";
			image = " ☉ ";
			satiete = 10;
			traversable = true;
			break;
		case 1:
			nom = "Feuille";
			image = " ☙ ";
			satiete = 20;
			traversable = true;
			break;
		}

		switch (choixPlacement) {
		case 0:// placement global
			for (int i = 0; i <= grille.getTailleX(); i++) {
				for (int j = 0; j <= grille.getTailleY(); j++) {
					if (eDepot.rechercherBete(eFabrique.creer(i, j)) == null) {
						laCase = eFabrique.creerNourriture(i, j, nom, image, satiete, traversable);
						eDepot.enregistrer(laCase);
					}
				}
			}
			break;
		case 1:// placement rectangulaire
			for (int i = caseX1; i <= caseX2; i++) {
				for (int j = caseY1; j <= caseY2; j++) {
					if (eDepot.rechercherBete(eFabrique.creer(i, j)) == null) {
						laCase = eFabrique.creerNourriture(i, j, nom, image, satiete, traversable);
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
					creerBouffe(laCase, nom, image, satiete, traversable);
				}
			}
			break;
		}

		return true;
	}
}
