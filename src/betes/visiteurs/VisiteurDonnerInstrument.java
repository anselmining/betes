package betes.visiteurs;

import java.util.HashMap;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.console.Console;
import betes.donnees.GenerationAleatoire;
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

public class VisiteurDonnerInstrument implements Visiteur {
	private static Logger logger = LoggerUtility.getLogger(VisiteurDonnerInstrument.class);
	String[] nomArme = { "Pinces", "Mâchoires", "Venin" };
	String[] nomArmure = { "Carapace", "Épines", "Immunité" };

	@Override
	public void visite(Bete bete) {

		for (int i = 0; i < nomArme.length; i++) {
			Arme arme = eFabrique.creerArme(GenerationAleatoire.nombreAleatoire(0, 100));
			arme.setNomInstrument(nomArme[i]);
			logger.trace("Arme [Nom: " + arme.getNomInstrument() + "] [Attaque: " + arme.getAttaque() + "] crée.");
			bete.ajouterArme(arme);
		}

		for (int i = 0; i < nomArme.length; i++) {
			Armure armure = eFabrique.creerArmure(GenerationAleatoire.nombreAleatoire(0, 100));
			armure.setNomInstrument(nomArmure[i]);
			logger.trace("Armure [Nom: " + armure.getNomInstrument() + "] [Défense: " + armure.getDefense() + "] crée.");
			bete.ajouterArmure(armure);
		}

		eDepot.enregistrerArmes(bete, bete.getArmes());
		eDepot.enregistrerArmures(bete, bete.getArmures());

	}

	/**Tranfère les armes d'un parent à son enfant.
	 * @param bete1 :première bête.
	 * @param bete2 :deuxième bête.
	 * @return (HashMap<String, Arme>): les armes du parent.
	 */
	public HashMap<String, Arme> transfertArmes(Bete bete1, Bete bete2) {
		HashMap<String, Arme> armes = new HashMap<String, Arme>();
		Arme arme;

		for (int i = 0; i < nomArme.length; i++) {
			if (GenerationAleatoire.nombreAleatoire(0, 1) == 0) {
				arme = bete1.getArme(nomArme[i]);
			} else {
				arme = bete2.getArme(nomArme[i]);
			}

			armes.put(arme.getNomInstrument(), arme);
		}

		return armes;
	}

	/**Tranfère les armures d'un parent à son enfant.
	 * @param bete1 :première bête.
	 * @param bete2 :deuxième bête.
	 * @return (HashMap<String, Armure>): les armures du parent.
	 */
	public HashMap<String, Armure> transfertArmures(Bete bete1, Bete bete2) {
		HashMap<String, Armure> armures = new HashMap<String, Armure>();
		Armure arme;

		for (int i = 0; i < nomArmure.length; i++) {
			if (GenerationAleatoire.nombreAleatoire(0, 1) == 0) {
				arme = bete1.getArmure(nomArmure[i]);
			} else {
				arme = bete2.getArmure(nomArmure[i]);
			}

			armures.put(arme.getNomInstrument(), arme);
		}

		return armures;
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
