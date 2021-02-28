//IFT1025-TP1
//Maxime Ton et Youssef Hilout

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class Lecture {
	
	//Méthode utilisant notre dernière fonction pour lire un répertoire
	public ArrayList<String> lire_fichier(String repertoire) {
		File folder = new File(repertoire);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> starray = new ArrayList<String>();
		for (File file : listOfFiles) {
	    	if (file.isFile()) {
	        	starray.addAll(token_lignes(repertoire + "/" + file.getName()));
	    	}
		}
		return (starray);
	}
	
	//Méthode donnée dans le document de TP
	public void lire_lignes(String nom) {
		// ouverture du fichier
		try {
			BufferedReader input = new BufferedReader(
								   new FileReader(nom));
			// lire et traiter chaque ligne
			String ligne;
			ligne = input.readLine();
			while (ligne != null) {
				System.out.println(ligne);
				//remplacer cette ligne par votre traitement.
				ligne = input.readLine();
			}
			input.close();
		}
		catch (IOException e) {
			System.err.println("erreur fichier" + e.toString());
		}
	}
	
	//Méthode modifiée permettant de tokenizer les lignes d'un fichier donné
	public ArrayList<String> token_lignes(String nom) {
		// ouverture du fichier
		ArrayList<String> starray = new ArrayList<String>();
		try {
			BufferedReader input = new BufferedReader(
								   new FileReader(nom));
			// lire et traiter chaque ligne
			String ligne;
			ligne = input.readLine();
			while (ligne != null) {
				//System.out.println(ligne);
				StringTokenizer st = new StringTokenizer(ligne);
				while (st.hasMoreTokens()) {
					starray.add(st.nextToken());
				}
				//remplacer cette ligne par votre traitement.
				ligne = input.readLine();
			}
			input.close();
		}
		catch (IOException e) {
			System.err.println("erreur fichier" + e.toString());
		}
		//for (int i = 0; i < starray.size(); i++) {
			//System.out.println(starray.get(i));
		//}
		return starray;
	}
}