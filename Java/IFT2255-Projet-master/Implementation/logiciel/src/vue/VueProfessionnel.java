package vue;

import controleur.ControleurCompte;
import controleur.ControleurMembre;
import controleur.ControleurProfessionnel;
import modele.compte.Compte;
import modele.compte.CompteProfessionnel;
import modele.enregistrement.Inscription;
import modele.enregistrement.Service;
import vue.VueCompte;

import java.util.List;
import java.util.Scanner;

public class VueProfessionnel extends VueCompte
{
	public VueProfessionnel(CompteProfessionnel professionnel)
	{
		super(professionnel, null);
		this.controleurCompte = new ControleurProfessionnel(this);
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
			System.out.println("\t3. Consulter les services offerts");
			System.out.println("\t4. Consulter les inscriptions à une séance");
			System.out.println("\t5. Confirmer la présence d'un membre à une séance");
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
					consulterServicesOfferts();
					break;
				case 4:
					consulterInscriptions();
					break;
				case 5:
					confirmerPresence();
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
	 * Permettre au professionnel de consulter les services qu'il offre.
	 */
	private void consulterServicesOfferts() {
		List<Service> services = ((CompteProfessionnel) compte).getServices();
		for(Service service : services)
		{
			afficherService(service);
		}
	}

	/**
	 * Permettre au professionnel de consulter les inscriptions aux séances qu'il offre.
	 */
	private void consulterInscriptions() {
		List<Inscription> inscriptions = ((ControleurProfessionnel) controleurCompte).consulterInscriptions((CompteProfessionnel) compte);

		if(inscriptions.size() > 0)
		{
			afficherSeance(inscriptions.get(0).getSeance());

			for (Inscription inscription : inscriptions)
			{
				System.out.println("-" + inscription.getMembre().getNom());
			}
		}
	}

	/**
	 * Permettre au professionnel de confirmer la présence d'un membre à une séance.
	 */
	private void confirmerPresence() {
		if(((ControleurProfessionnel) controleurCompte).confirmerPresence((CompteProfessionnel) compte))
		{
			System.out.println("Membre valide");
		}
		else
		{
			System.out.println("Membre invalide");
		}
	}
}
