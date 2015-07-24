package betes.visiteurs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.NourritureSurCaseException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.frames.InGame;
import betes.modeles.bete.Arme;
import betes.modeles.bete.Armure;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;

public class VisiteurCombat implements Visiteur {
	private static Logger logger = LoggerUtility
			.getLogger(VisiteurCombat.class);
	// HM < NomEnvironnement, HM < NomInstrument, Coef> >
	HashMap<String, HashMap<String, Integer>> influenceEnvironnementale = new HashMap<String, HashMap<String, Integer>>();
	HashMap<String, Integer> instrumentCoefs = new HashMap<String, Integer>();
	HashMap<String, String> instrumentContres = new HashMap<String, String>();
	ArrayList<String> nomInstruments = new ArrayList<String>();

	public VisiteurCombat() {
		nomInstruments.add("Pinces");
		nomInstruments.add("Mâchoires");
		nomInstruments.add("Venin");
		nomInstruments.add("Carapace");
		nomInstruments.add("Épines");
		nomInstruments.add("Immunité");

		for (String instrument : nomInstruments) {
			ajouterCoefInstrument("Herbe", instrument, 2);
			ajouterCoefInstrument("Vide", instrument, 1);
			ajouterCoefInstrument("Roche", instrument, 3);
		}

		// chaque Arme est contrée par une Armure
		ajouterContre("Pinces", "Carapace");
		ajouterContre("Mâchoires", "Épines");
		ajouterContre("Venin", "Immunité");

	}

	private void ajouterCoefInstrument(String nomEnvironnement,
			String nomInstrument, int coef) {
		instrumentCoefs.put(nomInstrument, coef);
		influenceEnvironnementale.put(nomEnvironnement, instrumentCoefs);
	}

	private int rechercherInfluenceEnvironnementale(String nomEnvironnement,
			String nomInstrument) {
		if (!influenceEnvironnementale.containsKey(nomEnvironnement)) {
			nomEnvironnement = "Vide"; // nom par défaut d'un environnement
										// inconnu
		}
		return influenceEnvironnementale.get(nomEnvironnement).get(
				nomInstrument);
	}

	private void ajouterContre(String instrument, String contre) {
		instrumentContres.put(instrument, contre);
	}

	private String rechercherContre(String nomInstrument) {
		logger.trace("Recherche d'un contre de " + nomInstrument);
		return instrumentContres.get(nomInstrument);
	}

	@Override
	public void visite(Bete bete, Bete autreBete) {
		logger.info("Combat en cours entre " + bete.getNomBete() + "["
				+ bete.getPositionX() + "][" + bete.getPositionY() + "] et "
				+ autreBete.getNomBete() + "[" + autreBete.getPositionX()
				+ "][" + autreBete.getPositionY() + "]");

		rEvent.registreEvent("- Combat entre " + bete.getNomBete() + "["
				+ bete.getPositionX() + "][" + bete.getPositionY() + "] et "
				+ autreBete.getNomBete() + "[" + autreBete.getPositionX()
				+ "][" + autreBete.getPositionY() + "]!");
		
		bete.setNbCombat(bete.getNbCombat() + 1);
		autreBete.setNbCombat(autreBete.getNbCombat() + 1);

		Case caseBete1 = eDepot.rechercher(bete.getPosition());
		Case caseBete2 = eDepot.rechercher(autreBete.getPosition());

		// coefficients environnementaux par défaut x1
		int coefEnvironnementalBete1 = 1;
		int coefEnvironnementalBete2 = 1;

		// la bête possédant le plus grand score obtient la victoire
		int scoreBete1 = 0, scoreBete2 = 0;

		HashMap<String, Arme> armesBete1 = bete.getArmes();
		HashMap<String, Arme> armesBete2 = autreBete.getArmes();
		HashMap<String, Armure> armuresBete1 = bete.getArmures();
		HashMap<String, Armure> armuresBete2 = autreBete.getArmures();

		Collection<String> nomArmes = armesBete1.keySet();
		Arme arme1, arme2;
		Armure contreArme1, contreArme2;

		for (Iterator<String> it = nomArmes.iterator(); it.hasNext();) {
			String nomArmeActuelle = it.next();
			// mise en place des coefficients environnementaux
			if (caseBete1.getNomDecor() != null
					&& !caseBete1.getNomDecor().equals("Vide")) {
				coefEnvironnementalBete1 = rechercherInfluenceEnvironnementale(
						caseBete1.getNomDecor(), nomArmeActuelle);
			}
			if (caseBete2.getNomDecor() != null
					&& !caseBete2.getNomDecor().equals("Vide")) {
				coefEnvironnementalBete2 = rechercherInfluenceEnvironnementale(
						caseBete2.getNomDecor(), nomArmeActuelle);
			}

			arme1 = armesBete1.get(nomArmeActuelle);
			arme2 = armesBete2.get(nomArmeActuelle);
			contreArme1 = armuresBete2.get(rechercherContre(nomArmeActuelle));
			contreArme2 = armuresBete1.get(rechercherContre(nomArmeActuelle));

			logger.trace("L'Arme " + nomArmeActuelle + " est contrée par "
					+ contreArme1.getNomInstrument());

			// Bete 1 attaque Bete2
			if (arme1.getAttaque() > contreArme1.getDefense()
					* coefEnvironnementalBete1) {
				scoreBete1++;
			} else {
				scoreBete2++;
			}

			// Bete 1 attaque Bete2
			if (arme2.getAttaque() > contreArme2.getDefense()
					* coefEnvironnementalBete2) {
				scoreBete2++;
			} else {
				scoreBete1++;
			}
		}

		VisiteurFuire fuite;

		/*
		 * Le gagnant absorbe l'énergie courrante de la bête perdante. La bête
		 * perdante voit son énergie divisée par deux, Le perdant finit par
		 * s'échapper d'une distance définie par DISTANCE_FUITE_MIN et
		 * DISTANCE_FUITE_MAX de la classe ContraintesParametres
		 */
		if (scoreBete1 >= scoreBete2) {
			logger.info("[---------[ " + bete.getNomBete()
					+ " (Bête attaquante)  Gagne car " + scoreBete1 + " >= "
					+ scoreBete2);
			bete.setEnergie(bete.getEnergie() + autreBete.getEnergie());
			if (bete.getEnergie() > 100) {
				bete.setEnergie(100);
			}
			autreBete.setEnergie(autreBete.getEnergie() / 2);
			fuite = new VisiteurFuire(caseBete2);
			fuite.visite(autreBete);
		} else {
			logger.info("[---------[ " + autreBete.getNomBete()
					+ " (Bête attaquée) Gagne car " + scoreBete1 + " < "
					+ scoreBete2);
			autreBete.setEnergie(autreBete.getEnergie() + bete.getEnergie());
			if (autreBete.getEnergie() > 100) {
				autreBete.setEnergie(100);
			}
			bete.setEnergie(bete.getEnergie() / 2);
			fuite = new VisiteurFuire(caseBete1);
			fuite.visite(bete);
		}

	}

	/**
	 * lance une simulation de combat entre deux bêtes pour vérifier laquelle va
	 * traquer l'autre.
	 * 
	 * @param (Bete) bete: une bête combattante.
	 * @param autreBete
	 *            : une autre bête combattante.
	 * @return (Boolean) :vrai si la première bête est susceptible de gagner le
	 *         combat,faux sinon.
	 * 
	 */
	public Boolean simulationCombat(Bete bete, Bete autreBete) {
		logger.info("Simulation de combat en cours entre " + bete.getNomBete()
				+ "[" + bete.getPositionX() + "][" + bete.getPositionY()
				+ "] et " + autreBete.getNomBete() + "["
				+ autreBete.getPositionX() + "][" + autreBete.getPositionY()
				+ "]");


		bete.setNbCombat(bete.getNbCombat() + 1);
		autreBete.setNbCombat(autreBete.getNbCombat() + 1);

		Case caseBete1 = eDepot.rechercher(bete.getPosition());
		Case caseBete2 = eDepot.rechercher(autreBete.getPosition());

		// coefficients environnementaux par défaut x1
		int coefEnvironnementalBete1 = 1;
		int coefEnvironnementalBete2 = 1;

		// la bête possédant le plus grand score obtient la victoire
		int scoreBete1 = 0, scoreBete2 = 0;

		HashMap<String, Arme> armesBete1 = bete.getArmes();
		HashMap<String, Arme> armesBete2 = autreBete.getArmes();
		HashMap<String, Armure> armuresBete1 = bete.getArmures();
		HashMap<String, Armure> armuresBete2 = autreBete.getArmures();

		Collection<String> nomArmes = armesBete1.keySet();
		Arme arme1, arme2;
		Armure contreArme1, contreArme2;

		for (Iterator<String> it = nomArmes.iterator(); it.hasNext();) {
			String nomArmeActuelle = it.next();
			// mise en place des coefficients environnementaux
			if (caseBete1.getNomDecor() != null
					&& !caseBete1.getNomDecor().equals("Vide")) {
				coefEnvironnementalBete1 = rechercherInfluenceEnvironnementale(
						caseBete1.getNomDecor(), nomArmeActuelle);
			}
			if (caseBete2.getNomDecor() != null
					&& !caseBete2.getNomDecor().equals("Vide")) {
				coefEnvironnementalBete2 = rechercherInfluenceEnvironnementale(
						caseBete2.getNomDecor(), nomArmeActuelle);
			}

			arme1 = armesBete1.get(nomArmeActuelle);
			arme2 = armesBete2.get(nomArmeActuelle);
			contreArme1 = armuresBete2.get(rechercherContre(nomArmeActuelle));
			contreArme2 = armuresBete1.get(rechercherContre(nomArmeActuelle));

			logger.trace("L'Arme " + nomArmeActuelle + " est contrée par "
					+ contreArme1.getNomInstrument());

			// Bete 1 attaque Bete2
			if (arme1.getAttaque() > contreArme1.getDefense()
					* coefEnvironnementalBete1) {
				scoreBete1++;
			} else {
				scoreBete2++;
			}

			// Bete 1 attaque Bete2
			if (arme2.getAttaque() > contreArme2.getDefense()
					* coefEnvironnementalBete2) {
				scoreBete2++;
			} else {
				scoreBete1++;
			}
		}

		if (scoreBete1 >= scoreBete2) {
			logger.info("[---------[ " + bete.getNomBete()
					+ " (Bête attaquante)  Gagne car " + scoreBete1 + " >= "
					+ scoreBete2);
			return true;
		}

		logger.info("[---------[ " + autreBete.getNomBete()
				+ " (Bête attaquée) Gagne car " + scoreBete1 + " < "
				+ scoreBete2);
		return false;

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
