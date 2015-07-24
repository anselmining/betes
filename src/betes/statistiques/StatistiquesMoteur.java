package betes.statistiques;

import java.util.Collection;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.donnees.EnvironnementDepot;
import betes.modeles.bete.Bete;

public class StatistiquesMoteur {
	@SuppressWarnings("unused")
	private static Logger logger = LoggerUtility
			.getLogger(StatistiquesMoteur.class);
	private static EnvironnementDepot eDepot = EnvironnementDepot.getInstance();

	/**
	 * Enregistre le nombre de bêtes vivantes dans un tableau du Dépôt.
	 */
	public static void enregistrerNombreDeBetesVivantes() {
		eDepot.setNombreBetes(eDepot.getNombreBetesVivantes());
	}

	/**
	 * Enregistre le nombre d'accouplement dans un tableau du Dépôt.
	 * 
	 * @return (int) somme : la somme d'accouplements de bêtes du Dépôt.
	 */
	public static int enregistrerNombreAccouplements() {
		int somme = 0;
		Collection<Bete> values = eDepot.getBetes().values();
		for (Bete bete : values) {
			somme += bete.getNbAccoulpements();
		}
		return somme;
	}

	public static void enregistrerNombreMales() {
		int sommeM = 0;
		int sommeF = 0;
		Collection<Bete> values = eDepot.getBetes().values();
		for (Bete bete : values) {
			if (bete.getEnergie() > 0) {

				if (bete.getSexe().equals("male")) {
					sommeM++;
				} else {
					sommeF++;
				}

			}
		}
		eDepot.setNombreDeMales(sommeM);
		eDepot.setNombreDeFemelles(sommeF);

	}
}
