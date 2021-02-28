package vue;

import controleur.ControleurCompte;
import modele.compte.Compte;
import vue.Vue;

import java.util.Scanner;

public abstract class VueCompte implements Vue
{
	protected ControleurCompte controleurCompte;
	protected Compte compte;

	VueCompte(Compte compte, ControleurCompte controleurCompte)
	{
		this.compte = compte;
		this.controleurCompte = controleurCompte;
	}

	@Override
	public void quitter()
	{
		controleurCompte = null;
		compte = null;
	}

	/**
	 * Permettre à l'utilisateur de modifier son compte
	 */
	public void modifierCompte() {
		String nom = compte.getNom();
		String adresse = compte.getAdresse();
		String telephone = compte.getTelephone();
		String email = compte.getEmail();

		boolean loop = true;
		while(loop)
		{
			System.out.println("Informations du compte");
			System.out.println("1. Nom: " + nom);
			System.out.println("2. Adresse: " + adresse);
			System.out.println("3. Telephone: " + telephone);
			System.out.println("4. Email: " + email);
			System.out.println("5. Terminer");
			System.out.print("Quelles informations voulez-vous modifier? ");
			int option = new Scanner(System.in).nextInt();

			switch(option)
			{
				case 1:
					nom = demanderInformationString("Nom: ");
					break;
				case 2:
					adresse = demanderInformationString("Adresse: ");
					break;
				case 3:
					telephone = demanderInformationString("Telephone: ");
					break;
				case 4:
					email = demanderInformationString("Email: ");
					break;
				case 5:
				default:
					loop = false;
			}
		}

		controleurCompte.modifierCompte(nom, adresse, telephone, email, compte);
	}

	/**
	 * Permettre à l'utilisateur de supprimer son compte
	 */
	void supprimerCompte() {
		controleurCompte.supprimerCompte(compte);
	}

	public Compte getCompte() {
		return this.compte;
	}
}
