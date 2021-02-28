//IFT1025-TP1
//Maxime Ton et Youssef Hilout

import java.util.Scanner;
//Classe principale qui permet d'utiliser l'engin de recherche
public class Main {

	public static void main(String[] args) {
		
		//Initialiser un nouvel Engin qui va guarder les actions en mémoire
		Engin d = new Engin();
		
		//Demande la première commande
		Scanner Ask = new Scanner(System.in);
		System.out.println("Écrire stop pour arrêter l'engin. \nEntrer votre instruction: ");
		String answer = Ask.nextLine();
		
		//Permet d'arrêter l'engin en écrivant stop
		while(!(answer.equals("stop") || answer.equals("Stop"))) {
			String[] arr = answer.split(" ", 2);
			
			//Commande pour indexer un fichier txt ou un répertoire contenant des fichiers txt
			if(arr[0].equals("indexer")) {
				d.indexer(arr[1]);
				System.out.println(arr[1] + " a été indexé.");
			}
			
			//Créer la deuxième structure qui est la première liste inversée
			if(answer.equals("inverser") || answer.equals("Inverser")) {
				d.indexerInv();
				System.out.println("La liste a été inversée.");
			}
			
			//Permet d'afficher les deux listes si elles existent
			if(answer.equals("afficher") || answer.equals("Afficher")) {
				d.afficher();
			}
			
			//Permet de rechercher un mot dans les documents indexés
			if(arr[0].equals("rechercher")) {
				d.rechercher(arr[1]);
			}
			
			//Demande la prochaine commande
			System.out.println("Écrire stop pour arrêter l'engin. \nEntrer votre instruction: ");
			answer = Ask.nextLine();
		}
	}
}