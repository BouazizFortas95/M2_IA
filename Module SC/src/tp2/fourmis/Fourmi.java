/**
 * 
 */
package tp2.fourmis;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.shape.Circle;

/**
 * @author Bouaziz Fortas
 *
 */
public class Fourmi {

	/** position de la fourmi */
	Point p;
	/** direction de la fourmi */
	private Direction direction;
	/** etat de la fourmi */
	private EtatFourmi etat;
	/** dose de pheromone que dépose la fourmi */
	private double dosePhero;
	/** dose maximal de pheromone que peut deposer une fourmi */
	public static final double dosePheroMax = 30d;
	/** dose de nourriture que peut porter une fourmi */
	public static final double doseNourriture = 30d;
	/** lien vers le terrain dans lequel se trouve la fourmi */
	private Terrain terrain;
	/** taille du terrain */
	private int taille;
	/** taille d'un pas */
	private int pas;
	/** objet necessaire pour le tirage aleatoire de la prochaine direction */
	private Random hasard;
	/** objet graphique associe a la fourmi */
	private Circle dessin;

	public Fourmi() {
	}

	/**
	 * construit une fourmi
	 * 
	 * @param _x       coordonee x initiale de la fourmi
	 * @param _y       coordonee y initiale de la fourmi
	 * @param _terrain terrain ou se trouve la fourmi
	 */
	public Fourmi(int _x, int _y, Terrain _terrain) {
		p = new Point(_x, _y);
		terrain = _terrain;
		taille = terrain.getTaille();
		direction = Direction.randomDirection();
		etat = EtatFourmi.CHERCHER;
		hasard = new Random();
		dosePhero = Fourmi.dosePheroMax;
	}

	/** active les actions de la fourmi selon son etat */
	public void evoluer() {
		Cellule[][] grille = terrain.getGrille();
		int x = p.x;
		int y = p.y;
		switch (etat) {
		case CHERCHER: // recherche de nourriture
			direction = getBestDirection(); // s'orienter vers la case offrant le plus de nourriture, sinon le plus de
											// ph�romone, sinon au hasard devant
			if (grille[x][y].getNourriture() <= 0)
				bougerVersDirection(); // avancer si possible
			if (grille[x][y].getNourriture() > 0) // si on trouve de la nourriture, on passe a l'etat suivant
				etat = EtatFourmi.PRENDRE;
			break;
		case PRENDRE:
			grille[x][y].oterNourriture(doseNourriture);// la foumi prend une dose de nourriture dans la cellule
			direction = Direction.getInverse(direction);// elle fait demi tour
			bougerVersDirection(); // avance
			etat = EtatFourmi.REVENIR; // et passe a l'etat suivant
			break;
		case REVENIR:
			if (dosePhero > 0)
				grille[x][y].setPheromone(dosePhero--); // la foumi depose une goute de plus en plus petite de pheromone
			direction = getBestDirectionNid(); // elle s'ortiente vers la cellule ayant la plus forte odeur de nid
			bougerVersDirection(); // et avance
			if (grille[x][y].isNid()) // si elle est dans le nid, elle passe a l'�tat suivant
				etat = EtatFourmi.DEPOSER;
			break;
		case DEPOSER:
			dosePhero = Fourmi.dosePheroMax; // la fourmi regenere sa dose de pheromone
			direction = Direction.getInverse(direction); // la fourmi fait demi-tour
			direction = getBestDirection();// recherche la meilleure direction
			bougerVersDirection();// et avance
			etat = EtatFourmi.CHERCHER; // elle passe a l'�tat suivant
			break;
		}
	}

	/**
	 * recherche la direction menant vers la case devant possedant le plus de
	 * nourriture, sinon le plus de pheromone, sinon retourne une des trois
	 * directions devant la fourmi<br>
	 * pour un comportement "r�aliste" la fourmi a un rayon de braquage de 45� <br>
	 * -> elle peut aller soit tout droit, � gauche devant ou � droite devant
	 */
	private Direction getBestDirection() {
		Direction bestDirection = direction;
		double bestPhero = 0d;
		double bestNourriture = 0d;
		Direction[] dirAlentours = Direction.get3Dir(direction);
		for (Direction dir : dirAlentours) // recherche de trace de nourriture devant
		{
			double nourriture = getNourritureProchaineCase(dir);
			if (nourriture > bestNourriture) {
				bestNourriture = nourriture;
				bestDirection = dir;
			}
		}
		if (bestNourriture == 0) {
			for (Direction dir : dirAlentours) // si pas trouve, recherche de trace de pheromone devant
			{
				double phero = getPheroProchaineCase(dir);
				if (phero > bestPhero) {
					bestPhero = phero;
					bestDirection = dir;
				}
			}
			if (bestPhero == 0) // si pas trouve, prendre une direction au hasard devant non occupee
			{
				ArrayList<Direction> listeDir = possibleNextDirections(dirAlentours);
				if (!listeDir.isEmpty()) {
					int i = hasard.nextInt(listeDir.size());
					bestDirection = listeDir.get(i);
				} else // si pas possible, faire demi-tour
					bestDirection = Direction.getInverse(direction);
			}
		}
		return bestDirection;
	}

	/**
	 * retourne une liste de directions possibles vers des cases videsdans les
	 * directions donnees
	 * 
	 * @param directions tableaux des drections dans lesquelles il faut tester si
	 *                   les cellules sont vides de fourmis
	 * @return une liste de directions possibles vers des cases vides de fourmis
	 */
	private ArrayList<Direction> possibleNextDirections(Direction[] directions) {
		ArrayList<Direction> liste = new ArrayList<Direction>();
		for (Direction dir : directions) {
			Cellule cell = getNextCellule(dir);
			if (cell != null && !cell.isFourmis())
				liste.add(dir);
		}
		return liste;
	}

	/**
	 * recherche la direction menant vers la case ayant l'odeur de nid la plus
	 * forte, sinon retourne une des trois directions devant la fourmi
	 * 
	 * @return la direction de la case ayant la valeur la plus elevee de odeurNid
	 */
	private Direction getBestDirectionNid() {
		Direction bestDirection = direction;
		double bestNid = 0d;
		Direction[] dirAlentours = Direction.get3Dir(direction);

		for (Direction dir : dirAlentours) // recherche de trace d'odeur de nid devant
		{
			double odeurNid = getOdeurNidProchaineCase(dir);
			if (odeurNid > bestNid) {
				bestNid = odeurNid;
				bestDirection = dir;
			}
		}
		if (bestNid == 0) // si pas trouve, prendre une direction au hasard devant non occupee
		{
			ArrayList<Direction> listeDir = possibleNextDirections(dirAlentours);
			if (!listeDir.isEmpty()) {
				int i = hasard.nextInt(listeDir.size());
				bestDirection = listeDir.get(i);
			} else // si pas possible, faire demi-tour
				bestDirection = Direction.getInverse(direction);
		}
		return bestDirection;
	}

	/**
	 * fait avancer la fourmi dans sa direction si la case devant existe et est non
	 * occupee
	 */
	private void bougerVersDirection() {
		Cellule cell = getNextCellule(direction);
		if (cell != null && !cell.isFourmis()) {
			Cellule[][] grille = terrain.getGrille();
			grille[p.x][p.y].setFourmis(false);
			p.x = cell.getX();
			p.y = cell.getY();
			dessin.setCenterX((p.x + 1) * pas);
			dessin.setCenterY((p.y + 2) * pas);
			cell.setFourmis(true);
		}
	}

	/**
	 * retourne le degre de pheromone dans la case voisine situe dans la direction
	 * dir
	 * 
	 * @param dir direction vers laquelle il faut effectuer le test
	 * @return le degre de pheromone dans la case voisine situee dans la direcion
	 *         dir
	 */
	private double getPheroProchaineCase(Direction dir) {
		double phero = -1;
		Cellule cell = getNextCellule(dir);
		if (cell != null)
			if (!cell.isFourmis())
				phero = cell.getPheromone();
		return phero;
	}

	/**
	 * retourne le degre de nourriture dans la case voisine situe dans la direction
	 * dir
	 * 
	 * @param dir direction vers laquelle il faut effectuer le test
	 * @return le degre de nourriture dans la case voisine situee dans la direcion
	 *         dir
	 */
	private double getNourritureProchaineCase(Direction dir) {
		double nourriture = -1;
		Cellule cell = getNextCellule(dir);
		if (cell != null)
			if (!cell.isFourmis())
				nourriture = cell.getNourriture();
		return nourriture;
	}

	/**
	 * retourne le degre de nourriture dans la case voisine situe dans la direction
	 * dir
	 * 
	 * @param dir direction vers laquelle il faut effectuer le test
	 * @return le degre d'odeur de nid dans la case voisine situee dans la direcion
	 *         dir
	 */
	private double getOdeurNidProchaineCase(Direction dir) {
		double odeurNid = -1;
		Cellule cell = getNextCellule(dir);
		if (cell != null)
			if (!cell.isFourmis())
				odeurNid = cell.getOdeurNid();
		return odeurNid;
	}

	/**
	 * donne la prochaine case dans la direction donn�e
	 * 
	 * @param dir la direction
	 * @return la cellule voisine dans la direction donn�e, null si aucune cellule
	 */
	private Cellule getNextCellule(Direction dir) {
		Cellule cell = null;
		Point newPoint = Direction.getNextPoint(p, dir);
		if ((newPoint.x >= 0 && newPoint.x < taille) && (newPoint.y >= 0 && newPoint.y < taille)) {
			Cellule[][] grille = terrain.getGrille();
			cell = grille[newPoint.x][newPoint.y];
		}
		return cell;
	}
	/*
	 * orienter la fourmi vers le nid, <br> calcule le vecteur normal de la fourmi
	 * vers le nid<br> puis demande le calcul de la direction correspondant a ce
	 * vecteur. non utilisee dans cette version du code
	 * 
	 * private void orienterVersNid() { int xNid = terrain.getxNid(); int yNid =
	 * terrain.getyNid(); int distNormaleX = (x==xNid?0:(xNid - x)/Math.abs((xNid -
	 * x))); int distNormaleY = (y==yNid?0:(yNid - y)/Math.abs((yNid - y)));
	 * direction = Direction.getDirectionFromVector(distNormaleX, distNormaleY); }
	 */

	/**
	 * @return the dessin
	 */
	public Circle getDessin() {
		return dessin;
	}

	/**
	 * @param dessin the dessin to set
	 */
	public void setDessin(Circle dessin) {
		this.dessin = dessin;
	}

	/**
	 * @param pas the pas to set
	 */
	public void setPas(int pas) {
		this.pas = pas;
	}

}
