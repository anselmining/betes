package betes.visiteurs;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.ContraintesParametres;
import betes.donnees.GenerationAleatoire;
import betes.donnees.GenerationAleatoireEtFixeConsole;
import betes.donnees.exceptions.CaseInexistanteException;
import betes.donnees.exceptions.CaseNonTraversableException;
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

public class VisiteurCreerBete implements Visiteur {
	private static Logger logger = LoggerUtility.getLogger(VisiteurCreerBete.class);
	Case laCase;
	String nomBete, sexe;
	int age = 0, energie, energieMax, nbCombats = 0, nbAccouplements = 0, facteurProcreation;

	@Override
	public void visite(Bete bete, Bete autreBete) {
	}

	@Override
	public void visite(Case laCase) throws CaseInexistanteException, CaseNonTraversableException {
	}

	@Override
	public void visite(Grille grille) {
	}

	@Override
	public void visite(Console console) {
		int nombreDeBetes;

		if (console.getModeAleatoire() == 0) {
			logger.warn("\nÉtape 2/3 : Entrez le nombre de Bêtes à créer.\n");
			nombreDeBetes = GenerationAleatoireEtFixeConsole.nombre(1, 3);
			creationElevage(nombreDeBetes);
		} else {
			nombreDeBetes = GenerationAleatoire.nombreAleatoire(ContraintesParametres.NOMBRE_BETES_GENEREES_NATURE_MIN - 1,
					ContraintesParametres.NOMBRE_BETES_GENEREES_NATURE_MAX - 1);
			creationNature(nombreDeBetes);

		}
		StatistiquesMoteur.enregistrerNombreDeBetesVivantes();

	}

	private void creationElevage(int nombreDeBetes) {
		String bonjourBete = "";

		for (int i = 1; i <= nombreDeBetes; i++) {
			logger.warn("\nDéterminez les caractéristiques de Bête(" + i + "/" + nombreDeBetes + ").");

			nomBete = GenerationAleatoireEtFixeConsole.nomBete();
			sexe = GenerationAleatoireEtFixeConsole.sexe();
			System.out.print("\nL'énergie maximale de la Bête ; ");
			energieMax = GenerationAleatoireEtFixeConsole.nombre(ContraintesParametres.ENERGIE_BETE_MIN, 100);
			System.out.print("\nL'énergie courrante de la Bête ; ");
			energie = GenerationAleatoireEtFixeConsole.nombre(ContraintesParametres.ENERGIE_BETE_MIN, energieMax);

			if (sexe == "femelle") {
				System.out.print("\nLe taux d'attractivité de la Bête ; ");
			} else {
				System.out.print("\nLe taux de virilité de la Bête ; ");
			}

			facteurProcreation = GenerationAleatoireEtFixeConsole.nombre(0, 10);
			laCase = GenerationAleatoireEtFixeConsole.position();

			if (laCase == null) {
				try {
					laCase = GenerationAleatoire.positionAleatoire(true);
					logger.warn("[     Case[" + laCase.getX() + "][" + laCase.getY() + "] sélectionnée aléatoirement.    [ \n");
				} catch (PasDeCaseDisponibleException e) {
					logger.warn("[         [ Pas de case sélectionnée.\n");
					continue;
				}
			}

			VisiteurDonnerInstrument instrumentaliste = new VisiteurDonnerInstrument();

			if (sexe == "femelle") {
				Femelle femelle = eFabrique.creerFemelle(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
				femelle.setEnergieMax(energieMax);
				instrumentaliste.visite(femelle);
				femelle.setPosition(laCase);
				eDepot.enregistrerNouvelleBete(femelle);
			} else {
				Male male = eFabrique.creerMale(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
				male.setEnergieMax(energieMax);
				instrumentaliste.visite(male);
				male.setPosition(laCase);
				eDepot.enregistrerNouvelleBete(male);
			}

			bonjourBete += "\n" + nomBete + "[" + laCase.getX() + "][" + laCase.getY() + "] commence son aventure !";
		}

		logger.warn(bonjourBete + "\n");
	}

	private void creationNature(int nombreDeBetes) {
		String bonjourBete = "";

		for (int i = 0; i <= nombreDeBetes; i++) {

			sexe = GenerationAleatoire.sexeAleatoire();
			if (sexe == "femelle") {// génération de nom aléatoire selon sexe
				nomBete = GenerationAleatoire.nomFemelleAleatoire();
			} else {
				nomBete = GenerationAleatoire.nomMaleAleatoire();
			}

			age = GenerationAleatoire.nombreAleatoire(0, 2014);
			energieMax = GenerationAleatoire.nombreAleatoire(ContraintesParametres.ENERGIE_BETE_MIN, 100);
			energie = GenerationAleatoire.nombreAleatoire(ContraintesParametres.ENERGIE_BETE_MIN, energieMax);
			nbCombats = 0;
			nbAccouplements = 0;
			facteurProcreation = GenerationAleatoire.nombreAleatoire(0, 10);
			try {
				laCase = GenerationAleatoire.positionAleatoire(true);
			} catch (PasDeCaseDisponibleException pdcde) {
				break;
			}

			VisiteurDonnerInstrument instrumentaliste = new VisiteurDonnerInstrument();

			if (sexe == "femelle") {// fabrication de bête selon sexe
				Femelle femelle = eFabrique.creerFemelle(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
				eDepot.enregistrerNouvelleBete(femelle);
				instrumentaliste.visite(femelle);
				femelle.setPosition(laCase.getX(), laCase.getY());
			} else {
				Male male = eFabrique.creerMale(nomBete, sexe, age, energie, energieMax, nbCombats, nbAccouplements, facteurProcreation);
				eDepot.enregistrerNouvelleBete(male);
				instrumentaliste.visite(male);
				male.setPosition(laCase.getX(), laCase.getY());
			}

			bonjourBete += "\n" + nomBete + "[" + laCase.getX() + "][" + laCase.getY() + "] commence son aventure !";
		}

		logger.warn(bonjourBete + "\n");
	}

	@Override
	public void visite(Bete bete) {
	}

	/**
	 * Vend une bête (en mode console uniquement). 
	 */
	public void vendreBete() {
		logger.warn("[         [ Sélectionnez une Bête à vendre.");
		Case laCase = GenerationAleatoireEtFixeConsole.position();
		Bete bete = eDepot.rechercherBete(laCase);

		if (bete == null) {
			logger.warn("[         [ Aucune Bête trouvée à cette position.\n");
		} else {
			eDepot.setNombreBetesVivantes(eDepot.getNombreBetesVivantes() - 1);
			StatistiquesMoteur.enregistrerNombreDeBetesVivantes();
			bete.setPosition(-1, -1);
			bete.setEnergie(0);
			logger.warn("[         [ Bête " + bete.getNomBete() + " vendue !");
		}
	}

	@Override
	public void visite(GraphicUserInterface gui) {
	}


	@Override
	public JCanvas visite(InGame ig, JCanvas jc, int r, int g, int b) {
		return jc;
	}

}
