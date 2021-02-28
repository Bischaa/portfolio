package vue;

import controleur.ControleurPrincipal;
import modele.Filtre;
import modele.compte.Compte;
import modele.compte.CompteMembre;
import modele.compte.CompteProfessionnel;
import vue.Vue;

import java.awt.desktop.AppForegroundListener;
import java.util.Scanner;

public class Application implements Vue
{
	private VueCompte vueCompte;
	protected ControleurPrincipal controleur;

	public Application()
	{
		controleur = new ControleurPrincipal(this);
	}

	@Override
	public void simulation() throws Exception
	{
		boolean loop = true;

		while(loop)
		{
			System.out.println("\n----- Menu -----");
			System.out.println("\t1. Connexion");
			System.out.println("\t2. Quitter");

			System.out.print("Sélectionnez une option: ");
			int option = new Scanner(System.in).nextInt();

			switch (option)
			{
				case 1:
					connexion();
					break;

				case 2:
				default:
					quitter();
					loop = false;
			}
		}
	}

	@Override
	public void quitter()
	{
		controleur = null;
	}

	/**
	 * Permettre à l'utilisateur de se connecter à l'application
	 */
	private void connexion() throws Exception
	{
		System.out.print("Entrez votre email: ");
		String email = new Scanner(System.in).next();

		Compte compte = controleur.connexion(email);

		if(compte instanceof CompteMembre)
		{
			vueCompte = new VueMembre((CompteMembre) compte);

			vueCompte.simulation();			//Lancer la simulation de l'application pour un membre

			vueCompte = null;				//La simulation est terminer, retourner à l'écran de connexion
		}
		else if(compte instanceof CompteProfessionnel)
		{
			vueCompte = new VueProfessionnel((CompteProfessionnel) compte);

			vueCompte.simulation();			//Lancer la simulation de l'application pour un membre

			vueCompte = null;				//La simulation est terminer, retourner à l'écran de connexion
		}
		else
		{
			System.out.println("Email invalide");
		}
	}
}
