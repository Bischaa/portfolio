import java.util.ArrayList;

public class Dictionnaire {
	ArrayList<String> dict = new ArrayList<String>();
	
	public Dictionnaire() {
		this.dict = new ArrayList<String>();
	}
	
	public void indexer_dictionnaire(String nom) {
		LectureFichier lec = new LectureFichier();
		dict = lec.token_lignes(nom);
		System.out.println("Fichier indexé.");
		//System.out.println(dict.get(1));
	}
}
