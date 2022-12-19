/**
 * 
 */
package tp2.fourmis;


import java.awt.Point;

/**
 * @author Bouaziz Fortas
 *
 */
public enum Direction {
	
	NORD(0, 0, -1), NORD_EST(1, 1, -1), EST(2,1,0), SUD_EST(3,1,1), SUD(4,0,1), SUD_OUEST(5,-1,1), OUEST(6,-1,0), NORD_OUEST(7,-1,-1);

	/**no d'indice de la direction*/
	int no;
	/**vecteur directeur*/
	Point v;
	
	/**
	 * @param _no no d'indice de la direction
	 * @param _x coordonnee x du vecteur de la direction
	 * @param _y coordonnee y du vecteur de la direction
	 */
	Direction(int _no, int _x, int _y) {
		this.no = _no; 
		this.v = new Point(_x, _y);
	}
	
	/**
	 * @param dir direction dont on souhaite calculer l'inverse
	 * @return retourne la direction inverse a dir */
	static Direction getInverse(Direction dir)
	{
		Direction [] tab = Direction.values();
		int indice = (dir.no + 4) % 8;
		return tab[indice];
	}
	
	/**
	 * @param dir direction dont on souhaite calculer les directions voisines
	 * @return retourne les trois directions entourant la direction en parametre*/
	static Direction[] get3Dir(Direction dir)
	{
		Direction[] dir3 = new Direction[3];
		Direction [] tab = Direction.values();
		int j=0;
		for(int i=-1; i<=1; i++)
		{
			int indice = (dir.no + i + 8) % 8;
			dir3[j++] = tab[indice];
		}
		return dir3;
	}
	
	/**
	 * @param a coordonnee x du vecteur dont on souhaite la direction
	 * @param b coordonnee y du vecteur dont on souhaite la direction
	 * @return la direction correspondante au vecteur (a,b)*/
	static Direction getDirectionFromVector(Point vect)
	{
		Direction dir = null;
		for(Direction d:Direction.values())
			if(d.v.equals(vect)){dir = d; break;}
		return dir;
	}
	
	/**
	 * @param a coordonnee x du vecteur dont on souhaite la direction
	 * @param b coordonnee y du vecteur dont on souhaite la direction
	 * @return la direction correspondante au vecteur (a,b)*/
	static Point getNextPoint(Point p, Direction d)
	{
		Point result = new Point( p.x + d.v.x, p.y + d.v.y);
		return result;
	}
	

	/**retourne une direction tir�e al�atoirement
	 * @return une direction tir�e al�atoirement*/
	static Direction randomDirection()
	{
		Direction[]tab = Direction.values();
		int i = (int)(Math.random()*tab.length);
		return tab[i];
	}
	

}
