package betes.astar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import log.LoggerUtility;

import org.apache.log4j.Logger;

import betes.donnees.EnvironnementDepot;
import betes.modeles.environnement.Case;
import betes.modeles.environnement.Grille;

/**
 * Classe fournissant un cas de test concret pour l'algo A*
 * 
 * On veut parcourir une matric de chaine de caractère, un espace représentant
 * un chemin exploitable un | représentant un chemin interdit. On peut se
 * déplacer a gauche, a droite, en haut, en bas, mais pas en diagonale
 * 
 * @author blemoine
 */
public class RechercheAstar {
	private static Logger logger = LoggerUtility.getLogger(RechercheAstar.class);

	/**
	 * Donne la prochaine Case la plus optimale pour atteindre une Case de destination
	 * 
	 * @param debut la Case de départ
	 * @param fin la Case de destination
	 * @return Case la prochaine Case la plus optimale
	 */
	public Case donneCaseProche(Case debut, Case fin) {
		final EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
		Grille grille = eDepot.getGrille();
		final int width = grille.getTailleY()-1;
		final int height = grille.getTailleX()-1;

		debut = new Case(debut.getY(), debut.getX());
		fin = new Case(fin.getY(), fin.getX());

		final SuccessorComputer<Case> successorComputer = new SuccessorComputer<Case>() {
			/**
			 * Doit renvoyer les Cases a gauche, a droite, en haut en bas du
			 * noeud passé en paramètre en supprimant la position du noeud
			 * parent de ce noeud
			 * 
			 * @param node
			 *            le noeud dont on cherche les voisins
			 * @return la liste des voisins du noeud diminué de la position du
			 *         parent
			 */
			@Override
			public Collection<Case> computeSuccessor(final Node<Case> node) {
				final Case index = node.getIndex();
				final int x = (int) index.getX();
				final int y = (int) index.getY();

				final List<Case> resultat = new ArrayList<Case>();
				if (x > 0) {
					resultat.add(new Case(x - 1, y));
				}
				if (x < width) {
					resultat.add(new Case(x + 1, y));
				}

				if (y > 0) {
					resultat.add(new Case(x, y - 1));
				}
				if (y < height) {
					resultat.add(new Case(x, y + 1));
				}
				if (node.getParent() != null) {
					resultat.remove(node.getParent().getIndex());
				}
				return resultat;
			}
		};

		final NodeFactory<Case> nodeFactory = new NodeFactory<Case>() {
			@Override
			protected double computeReel(final Case parentIndex, final Case index) {
				if (index.getY() > height || index.getX() > width) {
					logger.info("Chemins : [" + index.getY() + "/"+height+"][" + index.getX() + "/"+width+"]");
				}
				if (parentIndex != null && parentIndex.equals(index)) {
					return 0;
				}

				if (eDepot.rechercher((int) index.getY(), (int) index.getX()).isTraversable().equals(true)) {
					return 1;
				}
				return Double.MAX_VALUE;
			}

			@Override
			protected double computeTheorique(final Case index, final Case goal) {
				// Distance de manhattan
				return Math.abs(index.getX() - goal.getX()) + Math.abs(index.getY() - goal.getY());
			}
		};

		final Astar<Case> astart = new Astar<Case>(successorComputer, nodeFactory);

		List<Case> result = astart.compute(debut, fin);
		logger.info("Déplacement de Case[" + debut.getY() + "][" + debut.getX() + "] à Case[" + fin.getY() + "][" + fin.getX() + "]");
		
		if (debut.getY() == fin.getY() && debut.getX() == fin.getX() || result.get(1) == null) {
			return eDepot.rechercher(debut);
		}
		
		// On intégre le résultat dans la matrice de base, et on l'affiche
//		for (final Case laCase : result) {
////			logger.info("Chemins : [" + laCase.getY() + "][" + laCase.getX() + "]");
//		}
		
		logger.info("Déplacement à Case[" + result.get(1).getY() + "][" + result.get(1).getX() + "]");
		
		return eDepot.rechercher( result.get(1).getY(), result.get(1).getX());
	}

}
