package vue;

import controleur.ControleurAdministratif;
import controleur.ControleurService;
import modele.CompteID;
import modele.SeanceID;
import modele.enregistrement.Inscription;
import vue.Vue;

import java.util.List;
import java.util.Scanner;

public class LogicielAdministration implements Vue
{
	private ControleurAdministratif controleurAdmin;
	private ControleurService controleurService;

	public LogicielAdministration()
	{
		controleurAdmin = new ControleurAdministratif(this);
		controleurService = new ControleurService(this);
	}

	@Override
	public void simulation() throws Exception
	{
		boolean loop = true;

		while(loop)
		{
			System.out.println("\n----- Menu -----");
			System.out.println("\t1. Accéder au centre");
			System.out.println("\t2. Créer un compte");
			System.out.println("\t-");
			System.out.println("\t3. Créer une séance de service");
			System.out.println("\t4. Modifier une séance de service");
			System.out.println("\t5. Supprimer une séance de service");
			System.out.println("\t-");
			System.out.println("\t6. Consulter les inscriptions d'un professionnel");
			System.out.println("\t-");
			System.out.println("\t7. Procédure comptable principale");
			System.out.println("\t8. Rapport de synthèse");
			System.out.println("\t9. Rapport des membres");
			System.out.println("\t10. Rapport des professionnels");
			System.out.println("\t-");
			System.out.println("\t11. Quitter");

			System.out.print("Sélectionnez une option: ");
			int option = new Scanner(System.in).nextInt();
			System.out.println("---------------------------------");

			switch (option)
			{
				case 1:
					accederCentre();
					break;

				case 2:
					creerCompte();
					break;

				case 3:
					creerSeance();
					break;

				case 4:
					modifierSeance();
					break;

				case 5:
					supprimerSeance();
					break;

				case 6:
					consulterInscriptions();
					break;

				case 7:
					procedureComptablePrincipale();
					break;

				case 8:
					rapportDeSynthese();
					break;

				case 9:
					rapportDesMembres();
					break;

				case 10:
					rapportDesProfessionnels();
					break;

				case 11:
				default:
					quitter();
					loop = false;
					break;
			}
		}
	}

	@Override
	public void quitter()
	{
		controleurAdmin = null;
		controleurService = null;
	}

	/**
	 * Permettre à un membre/professionnel d'accéder au centre
	 */
	private void accederCentre() {
		CompteID compteID = lireNumeroCompte();

		if(controleurAdmin.validerCompte(compteID))
		{
			System.out.println("Compte valide");
		}
		else
		{
			System.out.println("Compte invalide");
		}
	}

	/**
	 * Créer un compte pour un client
	 * @throws Exception S'il y a une erreur dans le processus de création de compte
	 */
	private void creerCompte() throws Exception
	{
		boolean isProfessionnal;

		System.out.println("Type de compte:");
		System.out.println("1. Membre");
		System.out.println("2. Professionnel");

		System.out.print("Sélectionnez une option: ");
		int option = new Scanner(System.in).nextInt();

		switch(option)
		{
			case 1:
				isProfessionnal = false;
				break;
			case 2:
				isProfessionnal = true;
				break;
			default:
				throw new Exception("Wrong input");

		}

		String email = demanderInformationString("Email: ");
		String nom = demanderInformationString("Nom: ");
		String adresse = demanderInformationString("Adresse: ");
		String telephone = demanderInformationString("Telephone: ");

		if(isProfessionnal)
		{
			String expertise = demanderInformationString("Expertise: ");
			controleurAdmin.creerCompteProfessionnel(nom, adresse, telephone, expertise, email);
		}
		else
		{
			controleurAdmin.creerCompteMembre(nom, adresse, telephone, email);
		}
	}

	/**
	 * Permettre à un professionnel de créer une séance de service.
	 */
	private void creerSeance() {
		CompteID compteID = lireNumeroCompte();

		if(controleurAdmin.validerCompte(compteID))
		{
			controleurService.creerSeance(compteID);
		}
		else
		{
			System.out.println("Numéro de compte invalide");
		}
	}

	/**
	 * Permettre à un professionnel de modifier une séance de service.
	 */
	private void modifierSeance() {
		CompteID compteID = lireNumeroCompte();

		if(controleurAdmin.validerCompte(compteID))
		{
			SeanceID seanceID = lireNumeroSeance();
			controleurService.modifierSeance(seanceID);
		}
		else
		{
			System.out.println("Numéro de compte invalide");
		}
	}

	/**
	 * Permettre à un professionnel de supprimer une séance de service.
	 */
	private void supprimerSeance() {
		CompteID compteID = lireNumeroCompte();

		if(controleurAdmin.validerCompte(compteID))
		{
			SeanceID seanceID = lireNumeroSeance();
			controleurService.supprimerSeance(seanceID);
		}
		else
		{
			System.out.println("Numéro de compte invalide");
		}
	}

	/**
	 * Permettre à un professionnel de consulter les inscription à ses séances.
	 */
	private void consulterInscriptions() {
		CompteID compteID = lireNumeroCompte();

		if(controleurAdmin.validerCompte(compteID))
		{
			List<Inscription> inscriptions = controleurService.consulterInscriptions(compteID);
			for(Inscription inscription : inscriptions)
			{
				afficherInscription(inscription);
			}
		}
		else
		{
			System.out.println("Numéro de compte invalide");
		}
	}

	/**
	 * Lancer la procédure comptable principale.
	 */
	private void procedureComptablePrincipale() {
		controleurAdmin.procedureComptablePrincipale();
	}

	/**
	 * Créer le rapport de synthèse.
	 */
	private void rapportDeSynthese() {
		controleurAdmin.rapportDeSynthese();
	}

	/**
	 * Créer les rapports pour les membres.
	 */
	private void rapportDesMembres() {
		controleurAdmin.rapportDesMembres();
	}

	/**
	 * Créer les rapports pour les professionnels.
	 */
	private void rapportDesProfessionnels() {
		controleurAdmin.rapportDesProfessionnels();
	}
}
