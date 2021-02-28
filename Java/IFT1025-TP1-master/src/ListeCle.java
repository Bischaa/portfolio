//IFT1025-TP1
//Maxime Ton et Youssef Hilout

public class ListeCle {
	//Attributs
	NoeudCle premier;
	boolean isInverse = false;
	
	//M�thode pour ajouter une valeur � la fin de la liste
	public void ajoutFin(String cle) {
		NoeudCle temp = premier;
		if (premier == null) {
			premier = new NoeudCle(cle,null);
		}
		else {
			while (temp.prochain != null) {
				temp = temp.prochain;
			}
			temp.prochain = new NoeudCle(cle, null);
		}
	}
	
	//M�thode qui retourne une valeur cherch�e dans la liste, retourne null sinon
	public NoeudCle trouver(String cle) {
		NoeudCle n = premier;
		while (n !=  null && !(n.cle.equals(cle))) {
			n = n.prochain;
		}
		return n;
	}
	
	//M�thode pour afficher la liste de cl�s
	public void print() {
		NoeudCle n = premier;
		while (n != null) {
			System.out.println("Cl� = " + n.cle + "->");
			n.mot.print();
			n = n.prochain;
		}
		System.out.println("null");
	}
	//M�thode pour aller chercher le noeud � la i-�me position
	public NoeudCle getNoeud(int i) {
		NoeudCle p = premier;
		int position = 0;
		while (position != i && p.prochain != null) {
			p = p.prochain;
			position = position + 1;
		}
		return p;
	}
	
	//M�thode qui retourne le nombre de noeuds dans la liste cha�n�e
	public int longueur() {
		NoeudCle n = premier;
		int nb = 0;
		if(premier == null) {return 0;}
		while (n != null) {
			nb++;
			n = n.prochain;
		}
		return nb;
	}
}