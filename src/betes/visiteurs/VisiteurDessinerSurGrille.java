package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.frames.InGame;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;

public class VisiteurDessinerSurGrille implements Visiteur {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerUtility.getLogger(VisiteurDessinerSurGrille.class);
	private int idBeteControlee;
	public static VisiteurDessinerSurGrille instance = new VisiteurDessinerSurGrille();
	long pasX;
	int pasY, nbreGraduations = 20;

	public static VisiteurDessinerSurGrille getInstance() {
		return instance;
	}

	private VisiteurDessinerSurGrille() {

	}

	@Override
	public void visite(Grille grille) {
		String entete = "";

		System.out.print("   ");

		for (int j = 1; j <= grille.getTailleY(); j++) {
			entete = String.valueOf(j);
			entete = rendreEntetesATroisCaracteres(entete, true);
			System.out.print(entete);
		}

		System.out.print("\n");
		entete = "";

		for (int i = 1; i <= grille.getTailleX(); i++) {

			entete = String.valueOf(i);
			entete = rendreEntetesATroisCaracteres(entete, false);
			System.out.print(entete);

			for (int j = 1; j <= grille.getTailleY(); j++) {
				dessinerSurCase(i, j);
			}
			System.out.print("\n");
		}
	}

	private void dessinerSurCase(int x, int y) {
		Case laCase = eDepot.rechercher(x, y);
		Bete beteDeLaCase = eDepot.rechercherBete(laCase);

		if (beteDeLaCase != null) { // l'affichage graphique de bête est
									// prioritaire

			if (getIdBeteControlee() == beteDeLaCase.getIdBete()) {
				System.out.print("{" + beteDeLaCase.getNomBete().substring(0, 1) + "}");
			} else {
				if (beteDeLaCase.getEnergie() <= 0) {
					VisiteurBouger.getInstance().verifierMortBeteIA(beteDeLaCase);
					System.out.print(" ☠ ");
				} else {
					if (beteDeLaCase.getSexe().equals("femelle")) {
						System.out.print("IAF");
					} else {
						System.out.print("IAM");
					}
				}
			}
		} else {
			System.out.print(laCase.getImage());
		}

	}

	/**
	 * Affiche en mode console un graphique représentant le nombre de bêtes par rapport au temps.
	 */
	public void statsNbBetesParRapportAuTemps() {
		String entete = "";
		int valeurDuTableau, petitPasX, petitPasY;
		long valeurArrondie, axeYarrondi;
		pasX = Math.round(Math.ceil(eDepot.getTempsEcoule() / nbreGraduations) * nbreGraduations);
		petitPasX = (int) (pasX / 10);
		int nouveauPasY = (int) Math.round(Math.ceil(eDepot.getNombreBetes()[eDepot.getTempsEcoule()] / nbreGraduations)) * nbreGraduations;
		if (pasY < nouveauPasY || eDepot.getTempsEcoule() == 0) {
			pasY = nouveauPasY;
		}

		petitPasY = pasY / 10;
		if (petitPasY == 0) {
			petitPasY = 1;
		}

		if (petitPasX == 0) {
			petitPasX = 1;
			pasX = 10;
		}

		System.out.println("[ Nombre de Bêtes ]");
		for (int y = pasY; y >= 0; y = y - petitPasY) {
			entete = String.valueOf(y);

			entete = rendreEntetesATroisCaracteres(entete, false);
			System.out.print(entete);

			for (int x = 0; x <= pasX; x = x + petitPasX) {

				valeurDuTableau = eDepot.getNombreBetes()[x];
				valeurArrondie = Math.round(Math.ceil(valeurDuTableau / 10)) * 10;
				axeYarrondi = Math.round(Math.ceil(y / 10)) * 10;
				if (valeurArrondie == axeYarrondi && !(petitPasX == 1 && axeYarrondi == 0)) {
					System.out.print(" +  ");
				} else {
					System.out.print("    ");
				}
			}

			System.out.print("\n");
		}

		entete = "";

		System.out.print("   ");

		for (int x = 0; x <= pasX; x = x + petitPasX) {

			entete = String.valueOf(x);
			entete = rendreEntetesAQuatreCaracteres(entete);
			System.out.print(entete);

		}

		System.out.print(" [ Temps (ms) ]");

	}

	// La méthode qui renvoyer le nombre de betes :

	private String rendreEntetesATroisCaracteres(String entete, Boolean mettreAuMilieu) {
		if (entete.length() == 1) {
			if (mettreAuMilieu) {
				return " " + entete + " ";
			} else {
				return "  " + entete;
			}

		} else {
			if (entete.length() == 2) {
				return " " + entete;
			}
		}
		return entete;
	}

	private String rendreEntetesAQuatreCaracteres(String entete) {
		if (entete.length() == 1) {
			return " " + entete + "  ";

		} else {
			if (entete.length() == 2) {
				return " " + entete + " ";
			} else {
				if (entete.length() == 3) {
					return " " + entete;
				}

			}

		}
		return entete;
	}

	public int getIdBeteControlee() {
		return idBeteControlee;
	}

	public void setIdBeteControlee(int idBeteControlee) {
		this.idBeteControlee = idBeteControlee;
	}

	@Override
	public void visite(Bete bete, Bete autreBete) {
	}

	@Override
	public void visite(Case laCase) throws CaseInexistanteException {
	}

	@Override
	public void visite(Console console) {
	}

	@Override
	public void visite(Bete bete) {
	}

	@Override
	public void visite(GraphicUserInterface gui) {
	}


	@Override
	public JCanvas visite(InGame ig, JCanvas jc, int r, int g, int b) {
		return jc;
	}
}
