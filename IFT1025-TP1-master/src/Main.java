//IFT1025-TP1
//Maxime Ton et Youssef Hilout

import java.util.Scanner;
//Classe principale qui permet d'utiliser l'engin de recherche
public class Main {

	public static void main(String[] args) {
		
		//Initialiser un nouvel Engin qui va guarder les actions en m�moire
		Engin d = new Engin();
		
		//Demande la premi�re commande
		Scanner Ask = new Scanner(System.in);
		System.out.println("�crire stop pour arr�ter l'engin. \nEntrer votre instruction: ");
		String answer = Ask.nextLine();
		
		//Permet d'arr�ter l'engin en �crivant stop
		while(!(answer.equals("stop") || answer.equals("Stop"))) {
			String[] arr = answer.split(" ", 2);
			
			//Commande pour indexer un fichier txt ou un r�pertoire contenant des fichiers txt
			if(arr[0].equals("indexer")) {
				d.indexer(arr[1]);
				System.out.println(arr[1] + " a �t� index�.");
			}
			
			//Cr�er la deuxi�me structure qui est la premi�re liste invers�e
			if(answer.equals("inverser") || answer.equals("Inverser")) {
				d.indexerInv();
				System.out.println("La liste a �t� invers�e.");
			}
			
			//Permet d'afficher les deux listes si elles existent
			if(answer.equals("afficher") || answer.equals("Afficher")) {
				d.afficher();
			}
			
			//Permet de rechercher un mot dans les documents index�s
			if(arr[0].equals("rechercher")) {
				d.rechercher(arr[1]);
			}
			
			//Demande la prochaine commande
			System.out.println("�crire stop pour arr�ter l'engin. \nEntrer votre instruction: ");
			answer = Ask.nextLine();
		}
	}
}