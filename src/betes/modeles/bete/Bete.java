package betes.modeles.bete;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import betes.donnees.ContraintesParametres;
import betes.donnees.EnvironnementDepot;
import betes.donnees.EnvironnementFabrique;
import betes.donnees.GenerationAleatoire;
import betes.modeles.environnement.Case;
import betes.visiteurs.Visiteur;
import betes.visiteurs.VisiteurVision;

public class Bete implements Serializable {

	private static final long serialVersionUID = -1907302985699359037L;
	private int idBete;
	private String nomBete;
	private String sexe;
	private int age;
	private int energie;
	private int energieMax;
	private int positionAvantX;
	private int positionAvantY;
	private int positionX;
	private int positionY;
	private int nombreInstruments;
	private HashMap<String, Arme> armes = new HashMap<String, Arme>();
	private HashMap<String, Armure> armures = new HashMap<String, Armure>();
	private int satieteBete;
	private int nbCombats;
	private int nbAccouplements;
	private int distanceParcourue;
	private int distanceVision;
	protected int facteurReproduction;
	private Boolean intelligente;

	public Bete() {
		this.satieteBete = GenerationAleatoire.nombreAleatoire(ContraintesParametres.SATIETE_MIN, ContraintesParametres.SATIETE_MAX/2);
		this.distanceVision = GenerationAleatoire.nombreAleatoire(ContraintesParametres.DISTANCE_VISION_MIN, ContraintesParametres.DISTANCE_VISION_MAX);
		this.intelligente = GenerationAleatoire.nombreAleatoire(0, 100) <= ContraintesParametres.POURCENTAGE_BETES_INTELLIGENTES;
	}

	public Bete(String nomBete, String sexe, int age, int energie, int energieMax, int nbCombat, int nbAccouplements) {
		this();
		EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
		this.idBete = eDepot.getBetes().size();
		this.nomBete = nomBete;
		this.sexe = sexe;
		this.age = age;
		this.energie = energie;
		this.energieMax = energieMax;
		this.nbCombats = nbCombat;
		this.nbAccouplements = nbAccouplements;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setEnergie(int energie) {
		this.energie = energie;
	}

	public void setPosition(int x, int y) {
		positionX = x;
		positionY = y;
	}

	public void setPosition(Case laCase) {
		positionX = laCase.getX();
		positionY = laCase.getY();
	}

	public void setSatieteBete(int statiteBete) {
		this.satieteBete = statiteBete;
	}

	public void setNbCombat(int nbCombat) {
		this.nbCombats = nbCombat;
	}

	public void setNbAccouplements(int nbAccouplements) {
		this.nbAccouplements = nbAccouplements;
	}

	public int getIdBete() {
		return idBete;
	}

	public String getNomBete() {
		return nomBete;
	}

	public String getSexe() {
		return sexe;
	}

	public int getAge() {
		return age;
	}

	public int getFacteurReproduction() {
		return facteurReproduction;
	}

	public int getEnergie() {
		return energie;
	}

	public int getEnergieMax() {
		return energieMax;
	}

	public void setEnergieMax(int energieMax) {
		this.energieMax = energieMax;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}
	
	public Case getPosition() {
		EnvironnementFabrique eFabrique = new EnvironnementFabrique();
		Case laCase = eFabrique.creer(positionX, positionY);
		return laCase;
	}

	public int getSatieteBete() {
		return satieteBete;
	}

	public int getNbCombat() {
		return nbCombats;
	}

	public int getNbAccoulpements() {
		return nbAccouplements;
	}

	public void setIdBete(int idBete) {
		this.idBete = idBete;
	}

	public void setNomBete(String nomBete) {
		this.nomBete = nomBete;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public Boolean getIntelligente() {
		return intelligente;
	}

	public HashMap<String, Arme> getArmes() {
		return armes;
	}

	public void setArmes(HashMap<String, Arme> armes) {
		this.armes = armes;
	}

	public HashMap<String, Armure> getArmures() {
		return armures;
	}

	public void setArmures(HashMap<String, Armure> armures) {
		this.armures = armures;
	}

	public void ajouterArme(Arme arme) {
		nombreInstruments++;
		armes.put(arme.getNomInstrument(), arme);
	}

	public void ajouterArmure(Armure armure) {
		nombreInstruments++;
		armures.put(armure.getNomInstrument(), armure);
	}

	public int getDistanceParcourue() {
		return distanceParcourue;
	}

	public void setDistanceParcourue(int distanceParcourue) {
		this.distanceParcourue = distanceParcourue;
	}

	public int getNombreInstruments() {
		return nombreInstruments;
	}

	public void setNombreInstruments(int nombreInstruments) {
		this.nombreInstruments = nombreInstruments;
	}

	public int getDistanceVision() {
		return distanceVision;
	}

	public void setDistanceVision(int distanceVision) {
		this.distanceVision = distanceVision;
	}

	@Override
	public String toString() {
		String description = "[---------[ (" + idBete + ")";

		if (sexe.equals("male")) {
			description += " ♂ ";
		} else {
			description += " ♀ ";
		}

		description += nomBete + "[" + positionX + "][" + positionY + "] [" + energie + "/" + energieMax + "♥] [" + age + " µs] [" + distanceParcourue
				+ "mm parcourus]";

		if (satieteBete > 50) {
			description += " N'a pas faim. ]--*--*--] ";
		} else {
			description += "  A faim. ]--*--*--] ";
		}

		description += "\n[---------[ [Distance Vision : " + distanceVision + "]";

		if (intelligente) {
			description += " [Intelligence Accrue] ";
		}

		else {
			description += " [Intelligence Réduite] ";
		}

		description += "Activité : " + nbAccouplements + " accouplements, " + nbCombats + " combats.]\n";

		Collection<Arme> armesDescription = armes.values();

		description += "[---------[";

		for (Arme arme : armesDescription) {
			description += " [Arme] " + arme.getNomInstrument() + " [Attaque: " + arme.getAttaque() + "]";
		}

		description += "\n[---------[";

		Collection<Armure> armuresDescription = armures.values();
		for (Armure armure : armuresDescription) {
			description += " [Armure] " + armure.getNomInstrument() + " [Défense: " + armure.getDefense() + "]";
		}

		description += "\n\n";

		return description;
	}

	public String getVision() {
		VisiteurVision vision = new VisiteurVision(distanceVision);
		vision.visite(this);
		return vision.getDescription();
	}

	public void accepteVisiteur(Visiteur visiteur) {
		visiteur.visite(this);
	}

	public Arme getArme(String nomArme) {
		return armes.get(nomArme);
	}

	public Armure getArmure(String nomArmure) {
		return armures.get(nomArmure);
	}

	public int getPositionAvantX() {
		return positionAvantX;
	}

	public void setPositionAvantX(int positionAvantX) {
		this.positionAvantX = positionAvantX;
	}

	public int getPositionAvantY() {
		return positionAvantY;
	}

	public void setPositionAvantY(int positionAvantY) {
		this.positionAvantY = positionAvantY;
	}

}
