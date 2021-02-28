import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class LectureFichier {
	//Méthode modifiée permettant de tokenizer les lignes d'un fichier donné
		public ArrayList<String> token_lignes(String nom) {
			// ouverture du fichier
			ArrayList<String> starray = new ArrayList<String>();
			try {
				BufferedReader input = new BufferedReader(
									   new InputStreamReader(
									   new FileInputStream(nom), "UTF-8"));
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
		//Méthode modifiée permettant de tokenizer les lignes d'un fichier donné
				public String lire_lignes(String nom) {
					// ouverture du fichier
					String s = "";
					ArrayList<String> starray = new ArrayList<String>();
					try {
						BufferedReader input = new BufferedReader(
											   new InputStreamReader(
											   new FileInputStream(nom), "UTF-8"));
						// lire et traiter chaque ligne
						String ligne;
						ligne = input.readLine();
						while (ligne != null) {
							//System.out.println(ligne);
							s += ligne+"\n";
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
					return s;
				}
}
