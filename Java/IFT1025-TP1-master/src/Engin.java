//IFT1025-TP1
//Maxime Ton et Youssef Hilout

import java.util.ArrayList;
import java.util.StringTokenizer;

//Engin qui permet de garder une session en m�moire
public class Engin {
	
	//L'engin initialise un nouvel index
	Index Ind= null;
	
	//M�thode appel�e lorsqu'on demande d'indexer un document ou r�pertoire
	public void indexer(String source) {
		String[] ext = source.split("\\.", 2);
		Lecture c = new Lecture();
		
		//Si l'on veut indexer un ficher txt seul
		if(ext[ext.length-1].equals("txt")) {
			//On tokenise les mots et on les place dans un arraylist
			ArrayList<String> arr = c.token_lignes(source);
			//Si notre index est vide, on cr�er un nouvel index et on ins�re notre premier �l�ment
			if(Ind == null) {
				Ind = new Index();
				for(int i = 0; i < arr.size(); i++) {
					Ind.inserer(source, arr.get(i), 1);
				}
			}
			//Si notre index contient deja des �l�ments, nous ajoutons un nouvel �l�ment dans cet index existant
			else {
				for(int i = 0; i < arr.size(); i++) {
					Ind.inserer(source, arr.get(i), 1);
				}
				for(int i = 0; i < Ind.a.longueur(); i++) {
					Ind.a.getNoeud(i).mot.trierFreq();
				}
			}
		}
		//Si l'on veut indexer un r�pertoire
		else {
			ArrayList<String> arr = c.lire_fichier(source);
			//Si notre index est vide...
			if(Ind == null) {
				Ind = new Index();
				for(int i = 0; i < arr.size(); i++) {
					Ind.inserer(source, arr.get(i), 1);
				}
			}
			//Si notre index contient deja des �l�ments...
			else {
				for(int i = 0; i < arr.size(); i++) {
					Ind.inserer(source, arr.get(i), 1);
				}
				for(int i = 0; i < Ind.a.longueur(); i++) {
					Ind.a.getNoeud(i).mot.trierFreq();
				}
			}
		}
		//Trions la liste en ordre d�croissant
		for(int i = 0; i < Ind.a.longueur(); i++) {
			Ind.a.getNoeud(i).mot.trierFreq();
		}
	}
	
	//M�thode appel�e lorsqu'on inverse une liste
	public void indexerInv() {
		Ind.inverser(Ind.a);
	}
	
	//M�thode appel�e lorsqu'on veut afficher les listes
	public void afficher() {
		//Si on a que la premi�re liste
		if(Ind.b == null) {
			Ind.a.print();
		}
		//Si nous avons les deux listes
		else {
			Ind.a.print();
			Ind.b.print();
		}
	}
	
	//M�thode appel�e lorsqu'on recherche un mot dans la deuxi�me liste
	public ListeValeur rechercher(String requete) {
		//Cr�er la deuxi�me liste invers�e si ce n'est pas deja fait
		indexerInv();
		ListeValeur resultats = new ListeValeur();
		//Tokenizer la requ�te pour conna�tre tous les mots
		ArrayList<String> arr = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(requete);
		while (st.hasMoreTokens()) {
			arr.add(st.nextToken());
		}
		//Si notre premier mot existe dans notre 2e liste, ajouter les documents qui le contient dans notre liste de r�sultats
		if(Ind.b.trouver(arr.get(0)) != null) {
			for(int j = 0; j < Ind.b.trouver(arr.get(0)).mot.longueur(); j++) {
				resultats.ajoutFin(Ind.b.trouver(arr.get(0)).mot.getNoeud(j).valeur, 1);
			}
		}
		//Sinon, retournons une liste vide
		else {
			resultats = null;
			return resultats;
		}
		//Pour tous les mots qui suivent, faire la m�me d�marche
		for(int i = 0; i < arr.size(); i++) {
			if(Ind.b.trouver(arr.get(i)) != null) {
				if(resultats.trouver(arr.get(i)) != null) {
					resultats.trouver(arr.get(i)).freq = resultats.trouver(arr.get(i)).freq + 1;
				}
			}
			else {
				resultats = null;
				return resultats;
			}
			//Si un document dans la liste de r�sultat ne contient pas un mot de la liste, enlever ce document de la liste de r�sultats
			for(int k = 0; k < resultats.longueur(); k++) {
				if(Ind.b.trouver(arr.get(i)).mot.trouver(resultats.getNoeud(k).valeur) == null) {
					resultats.enlever(resultats.getNoeud(k).valeur);
				}
			}
		}
		//Trier les documents de la liste de r�sultats pour avoir le document ayant la plus haute fr�quence en haut
		resultats.trierFreq();
		resultats.print();
		return resultats;
	}

}
