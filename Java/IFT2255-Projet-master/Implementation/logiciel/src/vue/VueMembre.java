package vue;

import controleur.ControleurMembre;
import modele.compte.CompteMembre;
import modele.enregistrement.Inscription;
import vue.VueCompte;

import java.util.List;
import java.util.Scanner;

public class VueMembre extends VueCompte
{

	public VueMembre(CompteMembre membre)
	{
		super(membre, null);
		this.controleurCompte = new ControleurMembre(this);
	}

	@Override
	public void simulation()
	{
		boolean loop = true;

		while(loop)
		{
			System.out.println("\n----- Menu -----");
			System.out.println("Compte: " + compte.getNom() + " (" + compte.getNumeroCompte() + ")");
			System.out.println("\t1. Modifier le compte");
			System.out.println("\t2. Supprimer le compte");
			System.out.println("\t-");
			System.out.println("\t3. S'inscrire à une séance");
			System.out.println("\t4. Annuler une inscription");
			System.out.println("\t5. Consulter les inscriptions");
			System.out.println("\t-");
			System.out.println("\t6. Déconnexion");

			System.out.print("Sélectionnez une option: ");
			int option = new Scanner(System.in).nextInt();

			switch (option)
			{
				case 1:
					modifierCompte();
					break;
				case 2:
					supprimerCompte();
					break;
				case 3:
					inscription();
					break;
				case 4:
					annulerInscription();
					break;
				case 5:
					consulterInscriptions();
					break;
				case 6:
				default:
					quitter();
					loop = false;
					break;
			}
		}
	}

	/**
	 * Permettre au membre de s'inscrire à une séance de service.
	 */
	private void inscription() {
		((ControleurMembre) controleurCompte).inscription((CompteMembre) compte);
	}

	/**
	 * Permettre au membre d'annuler une inscription.
	 */
	private void annulerInscription() {
		((ControleurMembre) controleurCompte).annulerInscription((CompteMembre) compte);
	}

	/**
	 * Permettre au membre de consulter les séances auquelles il est inscrit.
	 */
	private void consulterInscriptions() {
		List<Inscription> inscriptions = ((ControleurMembre) controleurCompte).consulterInscriptions((CompteMembre) compte);
		for(Inscription inscription : inscriptions)
		{
			afficherInscription(inscription);
		}
	}
}
