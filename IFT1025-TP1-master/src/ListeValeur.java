//IFT1025-TP1
//Maxime Ton et Youssef Hilout

public class ListeValeur {
	//Attributs
	NoeudVal premier;
	
	//Méthode pour insérer une nouvelle valeur dans la sous-liste (liste horizontale)
	public void inserer(String valeur, int freq) {
		if (this.trouver(valeur) == null) {
			ajoutFin(valeur, freq);
		}
		else {
			this.trouver(valeur).freq = this.trouver(valeur).freq + freq;
		}
	}
	
	//Méthode pour ajouter un élément à la fin de la sous-liste
	public void ajoutFin(String valeur, int freq) {
		NoeudVal temp = premier;
		if (premier == null) {
			premier = new NoeudVal(valeur, freq, null);
		}
		else {
			while (temp.prochain != null) {
				temp = temp.prochain;
			}
			temp.prochain = new NoeudVal(valeur, freq, null);
		}
	}
	
	//Méthode permettant de trouver un noeud par sa valeur dans la liste
	public NoeudVal trouver(String valeur) {
		NoeudVal n = premier;
		while (n != null && !(n.valeur.equals(valeur))) {
			n = n.prochain;
		}
		return n;
	}
	
	//Méthode permettant d'aller chercher le noeud précédent à celui que l'on cherche
	public NoeudVal trouverPrecedent(String valeur) {
		NoeudVal n = premier;
		while (n.prochain != null && !(n.prochain.valeur.equals(valeur))) {
			n = n.prochain;
		}
		return n;
	}
	
	//Méthode pour trier dans l'ordre décroisssant de la fréquence (itératif, voir readme pour explication)
	public void trierFreq() {
		for (int i = 1; i < this.longueur(); i++) {
			NoeudVal courant = getNoeud(i);
			int j = i-1;
			int k = 0;
			while (k <= j && courant.freq < getNoeud(k).freq) {
				k++;
			}
			insert(courant, getNoeud(k));
		}
	}
	
	//Méthode permettant d'insérer un noeud remplace à la place d'un autre noeud position
	public void insert(NoeudVal remplace, NoeudVal position) {
		NoeudVal r = remplace;
		NoeudVal p = position;
		NoeudVal precedent = this.trouverPrecedent(position.valeur);
		(this.trouverPrecedent(remplace.valeur)).prochain = remplace.prochain;
		position = r;
		position.prochain = p;
		if(p == this.premier) {
			this.premier = position;
		}
		else {
			this.trouver(precedent.valeur).prochain = position;
		}
	}
	
	//Méthode pour aller chercher le noeud à la i-ème position
	public NoeudVal getNoeud(int i) {
		NoeudVal p = premier;
		int position = 0;
		while (position != i && p.prochain != null) {
			p = p.prochain;
			position = position + 1;
		}
		return p;
	}
	
	//Méthode qui retourne le nombre de noeuds dans la liste
	public int longueur() {
		NoeudVal n = premier;
		int nb = 0;
		if(premier == null) {return 0;}
		while (n != null) {
			nb++;
			n = n.prochain;
		}
		return nb;
	}
	
	//Méthode pour enlever un noeud
	public void enlever(String valeur) {
		NoeudVal n = premier;
		if(premier == null) {return;}
		if(premier.valeur.equals(valeur)) {
			premier = premier.prochain;
			return;
		}
		while (n.prochain != null && n.prochain.valeur != valeur) {
			n = n.prochain;
		}
		if(n.prochain != null) {
			n.prochain = n.prochain.prochain;
		}
	}
	
	//Méthode pour afficher la sous-liste
	public void print() {
		NoeudVal n = premier;
		while (n != null) {
			System.out.println("Valeur = " + n.valeur + " Fréquence = " + n.freq + "->");
			n = n.prochain;
		}
		System.out.println("null");
	}
}
