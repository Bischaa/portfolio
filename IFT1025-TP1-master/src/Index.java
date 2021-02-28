//IFT1025-TP1
//Maxime Ton et Youssef Hilout

public class Index {
	//Attributs
	ListeCle a = null;
	ListeCle b = null;
	
	//M�thode pour ins�rer une nouvelle valeur dans la liste
	public void inserer(String cle, String valeur, int freq) {
		//Si la premi�re liste est vide, cr�er une nouvelle premi�re liste et ins�rer notre mot � l'int�rieur
		if(a == null) {
			a = new ListeCle();
			a.ajoutFin(cle);
			a.premier.mot.ajoutFin(valeur, freq);
		}
		//Sinon, ajouter le mot dans la structure deja existante si le document n'est pas deja dedans
		else if (a.trouver(cle) == null){
			a.ajoutFin(cle);
			a.trouver(cle).mot.inserer(valeur, freq);
		}
		//Si le document se retrouve deja dans la premi�re structure, simplement ins�rer le prochain mot et sa fr�quence
		else {
			a.trouver(cle).mot.inserer(valeur, freq);
		}
	}
	
	//M�thode pour ins�rer une nouvelle valeur dans la liste invers�e (voir explications plus haut)
	public void insererInv(String valeur, String cle, int freq) {
		if(b == null) {
			b = new ListeCle();
			b.ajoutFin(valeur);
			b.premier.mot.ajoutFin(cle, freq);
		}
		else if (b.trouver(valeur) == null){
			b.ajoutFin(valeur);
			b.trouver(valeur).mot.inserer(cle, freq);
		}
		else {
			b.trouver(valeur).mot.inserer(cle, freq);
		}
	}
	
	//M�thode qui permet d'inverser la premi�re liste pour cr�er une deuxi�me liste invers�e
	public void inverser(ListeCle list) {
		if(a.isInverse) {}
		else {
			for(int j = 0; j < list.longueur(); j++) {
				for(int i = 0; i < list.getNoeud(j).mot.longueur(); i++) {
					insererInv(list.getNoeud(j).mot.getNoeud(i).valeur, list.getNoeud(j).cle, list.getNoeud(j).mot.getNoeud(i).freq);
				}
				b.getNoeud(j).mot.trierFreq();
			}
			a.isInverse = true;
		}
	}
}
