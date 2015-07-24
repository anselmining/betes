package betes.gui;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.donnees.ContraintesParametres;
import betes.donnees.EnvironnementDepot;
import betes.donnees.EnvironnementFabrique;
import betes.donnees.GenerationAleatoire;
import betes.donnees.exceptions.PasDeCaseDisponibleException;
import betes.gui.dessin.RectangleDrawable;
import betes.gui.moteur.EvenementsRegistre;
import betes.modeles.bete.Femelle;
import betes.modeles.bete.Male;
import betes.visiteurs.VisiteurCreerBete;
import betes.visiteurs.VisiteurDonnerInstrument;
import betes.visiteurs.VisiteurPlacerNourriture;

public class GraphicUserInterface {
	int age, energie, energieMax, nbCombats, nbAccouplements,
			facteurProcreation;
	RectangleDrawable rectangle;
	String nomBete, sexe;

	@SuppressWarnings("unused")
	private static Logger logger = LoggerUtility
			.getLogger(GraphicUserInterface.class);
	private static EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
	private EnvironnementFabrique eFabrique = new EnvironnementFabrique();
	private static GraphicUserInterface instance = new GraphicUserInterface();
	private static EvenementsRegistre rEvent = EvenementsRegistre.getInstance();
	private int modeAleatoire;
	private int idBeteControlee;

	private GraphicUserInterface() {
	}

	public static GraphicUserInterface getInstance() {
		return instance;
	}

	public int getModeAleatoire() {
		return modeAleatoire;
	}

	public void setModeAleatoire(int modeAleatoire) {
		this.modeAleatoire = modeAleatoire;
	}

	public String bienvenue() {
		return ("Bonjour! \n");
	}

	public int getIdBeteControlee() {
		return idBeteControlee;
	}

	public void setIdBeteControlee(int idBeteControlee) {
		this.idBeteControlee = idBeteControlee;
	}

	public void creerBete() {
		VisiteurCreerBete createurDeBetes = new VisiteurCreerBete();
		createurDeBetes.visite(this);
		if (modeAleatoire == 0) {
			setIdBeteControlee(0);
		} else {
			setIdBeteControlee(-1);
		}

	}

	public void initialiserNourriture() {
		VisiteurPlacerNourriture cultivateur = new VisiteurPlacerNourriture();
		eDepot.getGrille().accepteVisiteur(cultivateur);
	}
	
	public void initialiserBetesNature(int nombreDeBetes) {
		String bonjourBete = "Création automatique initialisée! \n";

		for (int i = 0; i <= nombreDeBetes; i++) {

			sexe = GenerationAleatoire.sexeAleatoire();
			if (sexe == "femelle") {// génération de nom aléatoire selon sexe
				nomBete = GenerationAleatoire.nomFemelleAleatoire();
			} else {
				nomBete = GenerationAleatoire.nomMaleAleatoire();
			}

			age = GenerationAleatoire.nombreAleatoire(0, 2014);
			energieMax = GenerationAleatoire.nombreAleatoire(
					ContraintesParametres.ENERGIE_BETE_MIN, 100);
			energie = GenerationAleatoire.nombreAleatoire(
					ContraintesParametres.ENERGIE_BETE_MIN, energieMax);
			nbCombats = 0;
			nbAccouplements = 0;
			facteurProcreation = GenerationAleatoire.nombreAleatoire(0, 10);
			try {
				rectangle = GenerationAleatoire
						.positionRectangleAleatoire(true);
			} catch (PasDeCaseDisponibleException pdcde) {
				break;
			}

			VisiteurDonnerInstrument instrumentaliste = new VisiteurDonnerInstrument();

			if (sexe == "femelle") {// fabrication de bête selon sexe
				Femelle femelle = eFabrique.creerFemelle(nomBete, sexe, age,
						energie, energieMax, nbCombats, nbAccouplements,
						facteurProcreation);
				eDepot.enregistrerNouvelleBete(femelle);
				instrumentaliste.visite(femelle);
				femelle.setPosition(rectangle.getX(),
						rectangle.getY());
			} else {
				Male male = eFabrique.creerMale(nomBete, sexe, age, energie,
						energieMax, nbCombats, nbAccouplements,
						facteurProcreation);
				eDepot.enregistrerNouvelleBete(male);
				instrumentaliste.visite(male);
				male.setPosition(rectangle.getX(),
						rectangle.getY());
			}

			bonjourBete += "\n-> " + nomBete + "[" + rectangle.getX()
					+ "][" + rectangle.getY()
					+ "] commence son aventure!";
		}

		rEvent.creerEvent(bonjourBete);
	}
}
