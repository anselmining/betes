package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.ContraintesParametres;
import betes.donnees.EnvironnementDepot;
import betes.donnees.GenerationAleatoire;
import betes.donnees.exceptions.BeteSurCaseException;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
import betes.donnees.exceptions.NourritureSurCaseException;
import betes.donnees.exceptions.PasDeCaseDisponibleException;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.frames.InGame;
import betes.modeles.bete.Bete;
import betes.modeles.bete.Femelle;
import betes.modeles.bete.Male;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;
import betes.statistiques.StatistiquesMoteur;

public class VisiteurAccouplement implements Visiteur {
	private static Logger logger = LoggerUtility.getLogger(VisiteurAccouplement.class);
	private Bete bete1;
	private Bete bete2;
	private Case laCase;
	private String nomBete, sexe;
	private int age = 0, energie, energieMax, nbCombats = 0, nbAccouplements = 0, facteurProcreation;

	public VisiteurAccouplement(Bete bete1, Bete bete2) {
		this.bete1 = bete1;
		this.bete2 = bete2;
	}

	/*
	 * Crée un bébé Bête de caractéristiques issues de ses parents
	 * 
	 * @param beteRencontrante Bête à l'initiative de l'accouplement.
	 * 
	 * @param beteRencontree Seconde Bête participant à l'accouplement.
	 */
	public void procreerParTransfert(Bete beteRencontrante, Bete beteRencontree) throws PasDeCaseDisponibleException {
		String bonjourBete = "";

		sexe = GenerationAleatoire.sexeAleatoire();
		if (sexe == "femelle") {// génération de nom aléatoire selon sexe
			nomBete = GenerationAleatoire.nomFemelleAleatoire();
		} else {
			nomBete = GenerationAleatoire.nomMaleAleatoire();
		}

		logger.info("[---------[ " + nomBete + " est procrée par " + bete1.getNomBete() + " et " + bete2.getNomBete());

		energieMax = transfertEnergieMax(beteRencontrante, beteRencontrante);
		energie = energieMax;
		facteurProcreation = transferFacteurProcreation(beteRencontrante, beteRencontree);
		laCase = this.positionAleatoire();

		VisiteurDonnerInstrument instrumentaliste = new VisiteurDonnerInstrument();

		if (sexe == "femelle") {// fabrication de bête selon sexe
			Femelle femelle = eFabrique.creerFemelle(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
			femelle.setArmes(instrumentaliste.transfertArmes(beteRencontrante, beteRencontree));
			femelle.setArmures(instrumentaliste.transfertArmures(beteRencontrante, beteRencontree));
			eDepot.enregistrerNouvelleBete(femelle);
			femelle.setPosition(laCase);
		} else {
			Male male = eFabrique.creerMale(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
			male.setArmes(instrumentaliste.transfertArmes(beteRencontrante, beteRencontree));
			male.setArmures(instrumentaliste.transfertArmures(beteRencontrante, beteRencontree));
			eDepot.enregistrerNouvelleBete(male);
			male.setPosition(laCase);
		}

		if (ContraintesParametres.modeConsole.equals(true)) {
			Console.afficherGrille();
		}
		bonjourBete += "\n" + nomBete + "[" + laCase.getX() + "][" + laCase.getY() + "] commence son aventure !";

		logger.info(bonjourBete + "\n");
	}

	/*
	 * Crée un bébé Bête de caractéristiques aléatoires (mutation)
	 * 
	 * @param beteRencontrante Bête à l'initiative de l'accouplement.
	 * 
	 * @param beteRencontree Seconde Bête participant à l'accouplement.
	 */
	public void procreerAleatoirement(Bete beteRencontrante, Bete beteRencontree) throws PasDeCaseDisponibleException {
		String bonjourBete = "";

		sexe = GenerationAleatoire.sexeAleatoire();
		if (sexe == "femelle") {// génération de nom aléatoire selon sexe
			nomBete = GenerationAleatoire.nomFemelleAleatoire();
		} else {
			nomBete = GenerationAleatoire.nomMaleAleatoire();
		}

		logger.info("[---------[ " + nomBete + " (mutée) est procrée par " + bete1.getNomBete() + " et " + bete2.getNomBete());

		energieMax = GenerationAleatoire.nombreAleatoire(0, 100);
		energie = energieMax;
		facteurProcreation = GenerationAleatoire.nombreAleatoire(0, 10);
		laCase = this.positionAleatoire();

		VisiteurDonnerInstrument instrumentaliste = new VisiteurDonnerInstrument();

		if (sexe == "femelle") {// fabrication de bête selon sexe
			Femelle femelle = eFabrique.creerFemelle(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
			instrumentaliste.visite(femelle);
			eDepot.enregistrerNouvelleBete(femelle);
			femelle.setPosition(laCase);
		} else {
			Male male = eFabrique.creerMale(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
			instrumentaliste.visite(male);
			eDepot.enregistrerNouvelleBete(male);
			male.setPosition(laCase);
		}

		if (ContraintesParametres.modeConsole.equals(true)) {
			Console.afficherGrille();
		}
		bonjourBete += "\n" + nomBete + "[" + laCase.getX() + "][" + laCase.getY() + "] commence son aventure !";

		logger.info(bonjourBete + "\n");
		rEvent.registreEvent(bonjourBete);

		StatistiquesMoteur.enregistrerNombreDeBetesVivantes();

	}

	/*
	 * Donne une Case aléatoire à côté de la bete à l'inititive de
	 * l'accouplement..
	 * 
	 * @return Une instance de la classe Case.
	 */
	private Case positionAleatoire() throws PasDeCaseDisponibleException {
		EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
		Grille grille = eDepot.getGrille();

		int positionX = 0, positionY = 0; // positions de la case aléatoire
		int nombreDeCasesRecherche = 0;

		Boolean traversable = false;
		Case laCase = null;
		Bete beteSurCase = null;

		do {
			if (nombreDeCasesRecherche > 10) {
				throw new PasDeCaseDisponibleException();
			}

			// position aléatoire à + ou - une case (en x et y) de la bête à
			// l'initative de l'accouplement
			switch (GenerationAleatoire.nombreAleatoire(0, 1)) {
			case 0:
				positionX = bete1.getPositionX() + GenerationAleatoire.nombreAleatoire(0, 1);
				break;
			case 1:
				positionX = bete1.getPositionX() - GenerationAleatoire.nombreAleatoire(0, 1);
				break;
			}
			switch (GenerationAleatoire.nombreAleatoire(0, 1)) {
			case 0:
				positionY = bete1.getPositionY() + GenerationAleatoire.nombreAleatoire(0, 1);
				break;
			case 1:
				positionY = bete1.getPositionY() - GenerationAleatoire.nombreAleatoire(0, 1);
				break;
			}

			laCase = eDepot.rechercher(positionX, positionY);
			traversable = laCase.isTraversable();
			beteSurCase = eDepot.rechercherBete(laCase);

			if (beteSurCase != null || positionX < 1 || positionY < 1 || positionX >= grille.getTailleX() || positionY >= grille.getTailleY()) {
				nombreDeCasesRecherche++;
				continue;
			}

			nombreDeCasesRecherche++;
		} while (traversable.equals(false));

		return laCase;
	}

	private int transfertEnergieMax(Bete bete1, Bete bete2) {
		if (GenerationAleatoire.nombreAleatoire(0, 1) == 0) {
			return bete1.getEnergieMax();
		}
		return bete2.getEnergieMax();
	}

	private int transferFacteurProcreation(Bete bete1, Bete bete2) {
		if (GenerationAleatoire.nombreAleatoire(0, 1) == 0) {
			return bete1.getFacteurReproduction();
		} else {
			return bete2.getFacteurReproduction();
		}
	}

	@Override
	public void visite(Bete bete, Bete autreBete) {
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
