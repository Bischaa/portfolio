//IFT1025-TP1
//Maxime Ton et Youssef Hilout

public class ListeCle {
	//Attributs
	NoeudCle premier;
	boolean isInverse = false;
	
	//Méthode pour ajouter une valeur à la fin de la liste
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
	
	//Méthode qui retourne une valeur cherchée dans la liste, retourne null sinon
	public NoeudCle trouver(String cle) {
		NoeudCle n = premier;
		while (n !=  null && !(n.cle.equals(cle))) {
			n = n.prochain;
		}
		return n;
	}
	
	//Méthode pour afficher la liste de clés
	public void print() {
		NoeudCle n = premier;
		while (n != null) {
			System.out.println("Clé = " + n.cle + "->");
			n.mot.print();
			n = n.prochain;
		}
		System.out.println("null");
	}
	//Méthode pour aller chercher le noeud à la i-ème position
	public NoeudCle getNoeud(int i) {
		NoeudCle p = premier;
		int position = 0;
		while (position != i && p.prochain != null) {
			p = p.prochain;
			position = position + 1;
		}
		return p;
	}
	
	//Méthode qui retourne le nombre de noeuds dans la liste chaînée
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