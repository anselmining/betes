package betes.gui.moteur;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import betes.donnees.GenerationAleatoire;

public class ImageLoading {

	public ImageLoading() {
	}

	public Image getImage(String filename) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(filename);
			return ImageIO.read(in);
		} catch (IOException e) {
			getImage("ressources/img/betes/temp.png");
		}
		return null;
	}

	public String creerFilet() {
		return "ressources/img/grid/grille1.png";
	}
	
	public String imageAccueil() {
		return "ressources/img/BetesImg2.png";
	}

	public String randomBeteImage(String sexe, String direction) {
		String name = "ressources/img/betes/temp.png";
		if (sexe == "male") {
			if (direction == "Nord") {
				name = "ressources/img/betes/betefN.png";
			}
			if (direction == "Sud") {
				name = "ressources/img/betes/betefS.png";
			}
			if (direction == "Est") {
				name = "ressources/img/betes/betefE.png";
			}
			if (direction == "Ouest") {
				name = "ressources/img/betes/betefW.png";
			}
		} else {
			if (direction == "Nord") {
				name = "ressources/img/betes/betemN.png";
			}
			if (direction == "Sud") {
				name = "ressources/img/betes/betemS.png";
			}
			if (direction == "Est") {
				name = "ressources/img/betes/betemE.png";
			}
			if (direction == "Ouest") {
				name = "ressources/img/betes/betemW.png";
			}
		}
		return name;
	}

	public String randomFeuilleImage() {
		return "ressources/img/betes/nourriture1.png";
	}

	public String randomDecorImage() {
		String name = "ressources/img/betes/temp.png";
		switch (GenerationAleatoire.nombreAleatoire(1, 4)) {
		case 1:
			name = "ressources/img/grid/decor1.png";
			break;
		case 2:
			name = "ressources/img/grid/decor2.png";
			break;
		case 3:
			name = "ressources/img/grid/decor3.png";
			break;
		case 4:
			name = "ressources/img/grid/decor4.png";
			break;
		default:
			name = "ressources/img/grid/decor1.png";
			break;
		}
		return name;
	}

	public String randomNatureImage() {
		String name = "ressources/img/betes/temp.png";
		switch (GenerationAleatoire.nombreAleatoire(1, 16)) {
		case 1:
			name = "ressources/img/grid/bush.png";
			break;
		case 2:
			name = "ressources/img/grid/bush2.png";
			break;
		case 3:
			name = "ressources/img/grid/bush3.png";
			break;
		case 4:
			name = "ressources/img/grid/bush4.png";
			break;
		case 5:
			name = "ressources/img/grid/grass.png";
			break;
		case 6:
			name = "ressources/img/grid/grass2.png";
			break;
		case 7:
			name = "ressources/img/grid/grass3.png";
			break;
		case 8:
			name = "ressources/img/grid/grass4.png";
			break;
		case 9:
			name = "ressources/img/grid/ground.png";
			break;
		case 10:
			name = "ressources/img/grid/ground2.png";
			break;
		case 11:
			name = "ressources/img/grid/rock.png";
			break;
		case 12:
			name = "ressources/img/grid/rock2.png";
			break;
		case 13:
			name = "ressources/img/grid/rock3.png";
			break;
		case 14:
			name = "ressources/img/grid/rock4.png";
			break;
		case 15:
			name = "ressources/img/grid/rock5.png";
			break;
		case 16:
			name = "ressources/img/grid/rock6.png";
			break;
		default:
			name = "ressources/img/grid/bush.png";
			break;
		}
		return name;
	}

}
