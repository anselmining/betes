package betes.donnees;

import java.util.Collection;
import java.util.HashMap;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.gui.dessin.ImageDrawable;
import betes.gui.dessin.RectangleDrawable;
import betes.modeles.bete.Arme;
import betes.modeles.bete.Armure;
import betes.modeles.bete.Bete;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;
import betes.statistiques.StatistiquesMoteur;

public class EnvironnementDepot {
	private static Logger logger = LoggerUtility
			.getLogger(EnvironnementDepot.class);
	private String nomEnvironnement;
	private Case[][] cases;
	private ImageDrawable[][] imagesNature;
	private ImageDrawable[][] imagesBetes;
	private ImageDrawable[][] imagesNourriture;
	private RectangleDrawable[][] rectangles;
	private ImageDrawable[][] rectanglesFilet;
	// id bête en clé pour les 3 hashmaps suivants
	// le String est le nom de arme ou de l'armure
	private HashMap<Integer, Bete> betes = new HashMap<Integer, Bete>();

	private HashMap<Integer, HashMap<String, Arme>> armes = new HashMap<Integer, HashMap<String, Arme>>();
	private int[] nombreBetes = new int[1000];
	private int[] nombreAcouplements = new int[1000];
	private int[] nombreDeMales = new int[1000];
	private int[] nombreDeFemelles = new int[1000];
	private int jour;

	public int getJour() {
		return jour;
	}

	public void setJour(int jour) {
		this.jour = jour;
	}

	private HashMap<Integer, HashMap<String, Armure>> armures = new HashMap<Integer, HashMap<String, Armure>>();
	private static EnvironnementDepot instance = new EnvironnementDepot(
			"Bêteplanète");
	private Grille grille;
	private int nombreBetesVivantes;
	private int tempsEcoule;
	private int nbNourriture;
	private int r, g, b;

	public int getTempsEcoule() {
		return tempsEcoule;
	}

	public RectangleDrawable[][] getRectangles() {
		return rectangles;
	}

	public ImageDrawable[][] getImagesNature() {
		return imagesNature;
	}

	public ImageDrawable[][] getImagesBetes() {
		return imagesBetes;
	}

	public ImageDrawable[][] getImagesNourriture() {
		return imagesNourriture;
	}
	
	public ImageDrawable[][] getRectanglesFilet() {
		return rectanglesFilet;
	}

	public void setImagesNature(ImageDrawable[][] imagesNature) {
		this.imagesNature = imagesNature;
	}

	public void setImagesBetes(ImageDrawable[][] imagesBetes) {
		this.imagesBetes = imagesBetes;
	}

	public void setImagesNourriture(ImageDrawable[][] imagesNourriture) {
		this.imagesNourriture = imagesNourriture;
	}

	public void setRectangles(RectangleDrawable[][] rectangles) {
		this.rectangles = rectangles;
	}

	public void setRectanglesFilet(ImageDrawable[][] rectanglesFilet) {
		this.rectanglesFilet = rectanglesFilet;
	}

	public void setTempsEcoule(int tempsEcoule) {
		this.tempsEcoule = tempsEcoule;
	}

	private EnvironnementDepot(String nomEnvironnement) {
		this.nomEnvironnement = nomEnvironnement;
		cases = new Case[ContraintesParametres.TAILLE_GRILLE_MAX_X + 1][ContraintesParametres.TAILLE_GRILLE_MAX_Y + 1];
	}

	/**
	 * Relance une partie de bêtes
	 */
	public void rejouerBetes() {
		setNombreBetesVivantes(0);
		setTempsEcoule(0);
		for (int i = 0; i < nombreBetes.length; i++) {
			nombreBetes[i] = 0;
		}
		nombreBetes = new int[10000];
		betes.clear();
	}

	public void enregistrer(Case laCase) {
		cases[laCase.getX()][laCase.getY()] = laCase;
	}

	public void enregistrer(Bete bete) {
		betes.put(bete.getIdBete(), bete);
	}

	public int retourNombreBetesCourant(int i) {
		return getNombreBetes()[i];
	}

	public void enregistrerArmes(Bete bete, HashMap<String, Arme> armesHM) {
		int idBete = bete.getIdBete();
		armes.put(idBete, armesHM);
	}

	public void enregistrerArmures(Bete bete, HashMap<String, Armure> armuresHM) {
		int idBete = bete.getIdBete();
		armures.put(idBete, armuresHM);
	}

	public void enregistrerNouvelleBete(Bete bete) {
		nombreBetesVivantes++;
		betes.put(betes.size(), bete);
	}

	public void enregistrerBetesVivantes() {
	}

	public Case rechercher(float g, float f) {
		if (g > grille.getTailleX() || f > grille.getTailleY()) {
			logger.warn("Recherche de case à (" + g + ";" + f + ")");
			System.exit(1);
		}
		return cases[(int) g][(int) f];
	}

	public RectangleDrawable rechercherRectangle(int x, int y) {
		return rectangles[x][y];
	}

	public Case rechercher(Case laCase) {
		return cases[laCase.getX()][laCase.getY()];
	}

	public Bete rechercherBete(Case laCase) {
		Collection<Bete> valeurs = betes.values();
		for (Bete bete : valeurs) {
			if (bete.getPositionX() == laCase.getX()
					&& bete.getPositionY() == laCase.getY()) {
				return bete;
			}
		}
		return null;
	}

	public Bete rechercher(Bete bete) {
		return betes.get(bete.getIdBete());
	}

	public HashMap<String, Arme> rechercherArmes(Bete bete) {
		return armes.get(bete.getIdBete());
	}

	public HashMap<String, Armure> rechercherArmures(Bete bete) {
		return armures.get(bete.getIdBete());
	}

	public Bete rechercher(int id) {
		return betes.get(id);
	}

	public static EnvironnementDepot getInstance() {
		return instance;
	}

	public String getNomEnvironnement() {
		return nomEnvironnement;
	}

	public Grille getGrille() {
		return grille;
	}

	public void setGrille(Grille grille) {
		this.grille = grille;
	}

	public Case[][] getCases() {
		return cases;
	}

	public HashMap<Integer, Bete> getBetes() {
		return betes;
	}

	public int getNombreBetesVivantes() {
		return nombreBetesVivantes;
	}

	public void setNombreBetesVivantes(int nombreBetesVivantes) {
		this.nombreBetesVivantes = nombreBetesVivantes;
	}

	public int[] getNombreBetes() {
		return nombreBetes;
	}

	public int[] getNombreDeMales() {
		return nombreDeMales;
	}

	public void setNombreBetes(int nombrebetes) {
		this.nombreBetes[getTempsEcoule()] = nombreBetesVivantes;
	}

	public int[] getNombreAcouplements() {
		return nombreAcouplements;
	}

	public void setNombreAcouplements() {
		this.nombreAcouplements[tempsEcoule] = StatistiquesMoteur
				.enregistrerNombreAccouplements();
	}

	public void setNombreDeMales(int sommeM) {
		this.nombreDeMales[tempsEcoule] = sommeM;
	}

	public int[] getNombreDeFemelles() {
		return nombreDeFemelles;
	}

	public void setNombreDeFemelles(int sommeF) {
		this.nombreDeFemelles[tempsEcoule] = sommeF;
	}

	public int getNomreBetesAccueil() {
		return betes.size();
	}

	public int getNombreNourriture() {
		return nbNourriture;
	}

	public void incrementNourriture() {
		nbNourriture++;
	}

	public void decrementNourriture() {
		nbNourriture--;
	}
	
	public void setClimat(String typeClimat) {
		if (typeClimat == "Tempéré") {
			this.r = 110;
			this.g = 133;
			this.b = 25;
		}

		if (typeClimat == "Tropique") {
			this.r = 76;
			this.g = 110;
			this.b = 73;
		}

		if (typeClimat == "Aride") {
			this.r = 212;
			this.g = 202;
			this.b = 120;
		}
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public void setR(int r) {
		this.r = r;
	}

	public void setG(int g) {
		this.g = g;
	}

	public void setB(int b) {
		this.b = b;
	}
	
	

}
