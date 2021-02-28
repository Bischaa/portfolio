package controleur;

import modele.CompteID;
import modele.Filtre;
import modele.SeanceID;
import modele.ServiceID;
import modele.compte.Compte;
import modele.compte.CompteMembre;
import modele.compte.CompteProfessionnel;
import modele.compte.RepertoireCompte;
import modele.enregistrement.*;
import vue.LogicielAdministration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;



public class ControleurAdministratif {
	private LogicielAdministration vue;

	public ControleurAdministratif(LogicielAdministration vue) {
		this.vue = vue;
	}

	/**
	 * Créer un compte de membre.
	 * @param nom Le nom
	 * @param adresse L'adresse
	 * @param telephone Le téléphone
	 * @param email L'email
	 */
	public void creerCompteMembre(String nom, String adresse, String telephone, String email) {
		RepertoireCompte.creerCompteMembre(nom, adresse, telephone, email);
	}

	/**
	 * Créer un compte de professionnel
	 * @param nom Le nom
	 * @param adresse L'adresse
	 * @param telephone Le téléphone
	 * @param expertise L'expertise
	 * @param email L'email
	 */
	public void creerCompteProfessionnel(String nom, String adresse, String telephone, String expertise, String email) {
		RepertoireCompte.creerCompteProfessionnel(nom, adresse, telephone, expertise, email);
	}

	/**
	 * Vérifier si un numéro de compte correspond à ce compte et si ce compte est valide.
	 * @param numeroCompte Le numéro du compte à valider
	 */
	public boolean validerCompte(CompteID numeroCompte) {
		//Filtre qui retourne true/false si le numéro de compte match avec celui dans le repertoire
		Filtre<Compte> filtreCompte = compte -> compte.getNumeroCompte().equals(numeroCompte);

		List<Compte> listeComptes = RepertoireCompte.lireComptes(filtreCompte);  //Devrait retourner une liste avec un Compte, sinon vide
		if (listeComptes.size() > 1) {
			throw new NoSuchElementException("Plus d'un compte associé au numéro de compte: " + numeroCompte);
		}
		else if(listeComptes.size() == 1) {
			return listeComptes.remove(0).getValide();
		}

		return false;
	}

	/**
	 * Procédure comptable principale
	 */
	public void procedureComptablePrincipale() {
		//Filtre pour retourner seulement des CompteProfessionnel
		Filtre<Compte> filtreProfessionnels = compte -> compte instanceof CompteProfessionnel;

		//Professionnels
		List<Compte> listeProfessionnels = RepertoireCompte.lireComptes(filtreProfessionnels);
		for (Compte compte: listeProfessionnels) {
			CompteProfessionnel compteProfessionnel = (CompteProfessionnel) compte;
			List<Service> listeServicesProfessionnel = compteProfessionnel.getServices();

			double montantProfessionnel = 0;

			for (Service service : listeServicesProfessionnel) {

				List<Seance> listeSeancesProfessionnel = service.getSeances();
				LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1); //Vendredi dernier
				List<Seance> seancesSemaineDerniere = null;

				for (Seance seanceSemaine : listeSeancesProfessionnel) {
					if (seanceSemaine.getDateDebut().isAfter(oneWeekAgo)) {
						seancesSemaineDerniere.add(seanceSemaine);
					}
				}

				for (Seance seance : seancesSemaineDerniere) {
					montantProfessionnel += seance.getFrais();
				}
			}


			//Infos à inclure dans le fichier TEF:
			String contenuTEF = "";
			contenuTEF += "Nom du professionnel: "+compteProfessionnel.getNom() + '\n';
			contenuTEF += "Numéro: "+compteProfessionnel.getNumeroCompte() + '\n';
			contenuTEF += "Montant à payer: "+montantProfessionnel + '\n';

			//TODO: Nom du fichier ?
			ecrireFichier(compteProfessionnel.getNom()+"_"+LocalDate.now()+"_TEF.txt", contenuTEF);
		}
	}

	/**
	 * Produit un rapport de synthèse pour les comptes payables
	 */
	public void rapportDeSynthese() {
		// TODO - implement controleur.ControleurAdministratif.rapportDeSynthese
		//Utiliser objets rapportMembre et rapportProfessionnel ?

		//Afficher:
		//Liste des professionnels à payer
		//Pour chaque professionnel: nombre de séances, montant à payer
		//Nombre de professionnels ayant fourni un service cette semaine-là
		//Nombre total de séances
		//Montant total à payer

		int montantTotal = 0;
		int nbSeanceTotal = 0;
		String contenuRapport = "";

		//Ensemble des professionnels qui ont fourni une séance cette semaine:
		HashSet<CompteProfessionnel> ensembleProfessionnelsSemaine = null;

		//Filtre pour retourner seulement des CompteProfessionnel
		Filtre<Compte> filtreProfessionnels = compte -> compte instanceof CompteProfessionnel;

		//Professionnels
		List<Compte> listeProfessionnels = RepertoireCompte.lireComptes(filtreProfessionnels);
		for (Compte compte: listeProfessionnels) {
			CompteProfessionnel compteProfessionnel = (CompteProfessionnel) compte;
			List<Service> listeServicesProfessionnel = compteProfessionnel.getServices();

			double montantProfessionnel = 0;
			int nbSeanceProfessionnel = 0;

			for (Service service : listeServicesProfessionnel) {

				List<Seance> listeSeancesProfessionnel = service.getSeances();
				LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1); //Vendredi dernier
				List<Seance> seancesSemaineDerniere = null;

				for (Seance seanceSemaine : listeSeancesProfessionnel) {
					if (seanceSemaine.getDateDebut().isAfter(oneWeekAgo)) {
						seancesSemaineDerniere.add(seanceSemaine);
					}
				}

				if(seancesSemaineDerniere != null)
				{
					for (Seance seance : seancesSemaineDerniere)
					{
						montantProfessionnel += seance.getFrais();
						nbSeanceProfessionnel += 1;

						//Ajouter les professionnels qui ont offert une séance cette semaine-là:
						ensembleProfessionnelsSemaine.add(seance.getService().getProfessionnel());
					}
				}

				//Infos à inclure dans le fichier TEF:
				String contenuProf = "";
				contenuProf += "Nom du professionnel: "+compteProfessionnel.getNom() + '\n';
				contenuProf += "Numéro: "+compteProfessionnel.getNumeroCompte() + '\n';
				contenuProf += "Montant à payer: "+montantProfessionnel + "\n\n";
				contenuRapport += contenuProf;

				montantTotal += montantProfessionnel;
				nbSeanceTotal += nbSeanceProfessionnel;
			}
		}

		contenuRapport += "Nombre de professionnels ayant fourni un service cette semaine: " + ensembleProfessionnelsSemaine.size() + "\n";
		contenuRapport += "Nombre total de séances offertes durant la semaine: " + nbSeanceTotal + '\n';
		contenuRapport += "Montant total: " + montantTotal + '\n';

		ecrireFichier("RapportDeSynthese.txt", contenuRapport);
	}

	/**
	 * Produit un rapport pour chaque membre concernant les services reçus de la semaine et le montant à payer
	 */
	public void rapportDesMembres() {

		//Filtre qui retourne seulement des CompteMembre
		Filtre<Compte> filtreMembres = compte -> compte instanceof CompteMembre;

		List<Compte> listeComptesMembres = RepertoireCompte.lireComptes(filtreMembres);
		for (Compte compteMembre : listeComptesMembres) {
			//chercher la liste des inscriptions de cette semaine pour ce membre
			//L'usager paie pour les inscriptions aux séances
			LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1); //Vendredi dernier
			Filtre<Inscription> filtreInscriptionsSemaine =
					inscription -> inscription.getSeance().getDateDebut().isAfter(oneWeekAgo);
			List<Inscription> listeInscriptions = RepertoireService.lireInscriptions(filtreInscriptionsSemaine);

			//Trier par ordre chronologique:
			listeInscriptions.sort(Comparator.comparing(x -> x.getSeance().getDateDebut()));


			//Infos à inclure dans le rapport d'un membre:
			String contenuRapportMembre = "";
			contenuRapportMembre += "Nom: " + compteMembre.getNom() + "\n";
			contenuRapportMembre += "Numéro: " + compteMembre.getNumeroCompte() + "\n";
			contenuRapportMembre += "Adresse: " + compteMembre.getAdresse() + "\n";

			contenuRapportMembre += "Services reçus:\n";

			double montantTotalMembre = 0;

			for (Inscription inscription : listeInscriptions) {
				Seance seance = inscription.getSeance();

				contenuRapportMembre += "Date du service: "+ seance.getDateDebut().toString()+"\n";
				contenuRapportMembre += "Nom du professionnel: "+ seance.getService().getProfessionnel().getNom()+"\n";
				contenuRapportMembre += "Nom du service: "+ seance.getNomService()+"\n";

				double montantSeance = seance.getFrais();
				montantTotalMembre += montantSeance;
				contenuRapportMembre += "Frais: "+ montantSeance+"$\n\n";

			}

			contenuRapportMembre += "Total à payer: "+ montantTotalMembre+"\n";

			//Écrire rapportMembre:
			String nomFichier = compteMembre.getNom()+LocalDate.now()+".txt";
			ecrireFichier(nomFichier, contenuRapportMembre);
		}

	}

	/**
	 * Produit un rapport pour chaque professionnel concernant les services offerts et les montants payables de la semaine
	 */
	public void rapportDesProfessionnels() {
		//Filtre pour retourner seulement des CompteProfessionnel
		Filtre<Compte> filtreProfessionnels = compte -> compte instanceof CompteProfessionnel;

		List<Compte> listeComptesProfessionnels = RepertoireCompte.lireComptes(filtreProfessionnels);
		for (Compte compteProfessionnel : listeComptesProfessionnels) {
			//Vendredi dernier
			LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
			//Filtre des seances du professionnel pour cette semaine
			Filtre<Seance> filtreSeanceSemaine = seance ->
					seance.getService().getProfessionnel() == compteProfessionnel
					&& seance.getDateDebut().isAfter(oneWeekAgo);

			List<Seance> listeSeances = RepertoireService.lireSeances(filtreSeanceSemaine);
			//Filtrer par ordre chronologique:
			listeSeances.sort(Comparator.comparing(x -> x.getDateDebut()));

			//Infos à inclure dans le rapport d'un professionnel:
			String contenuRapportProfessionnel = "";
			contenuRapportProfessionnel += "Nom: " + compteProfessionnel.getNom() + "\n";
			contenuRapportProfessionnel += "Numéro: " + compteProfessionnel.getNumeroCompte() + "\n";
			contenuRapportProfessionnel += "Adresse: " + compteProfessionnel.getAdresse() + "\n";
			double montantTotalProfessionnel = 0;

			for (Seance seance : listeSeances) {
				//Infos pour chaque séance
				contenuRapportProfessionnel += "Date du service: " + seance.getDateDebut();
				contenuRapportProfessionnel += "Date-Heure d'enregistrement: " + seance.getDateHeureEntree();
				contenuRapportProfessionnel += "Participants: \n";
						//Afficher liste de participants avec nom et numéro de membre
				List<Inscription> listeInscriptions =  seance.getInscriptions();
				for (Inscription inscription : listeInscriptions) {
					contenuRapportProfessionnel +=  inscription.getMembre().getNom();
					contenuRapportProfessionnel += "(" +inscription.getMembre().getNumeroCompte() + ")\n";
				}
				contenuRapportProfessionnel += "Code de la séance: " + seance.getNumeroSeance()+"\n";
				double montantSeance = seance.getFrais() * listeInscriptions.size();
				montantTotalProfessionnel += montantSeance;
				contenuRapportProfessionnel += "Montant: "+ montantSeance+"$\n";

			}
			contenuRapportProfessionnel += "Montant total à recevoir: " + montantTotalProfessionnel+"$\n";

			String nomFichier = compteProfessionnel.getNom()+LocalDate.now()+".txt";
			ecrireFichier(nomFichier, contenuRapportProfessionnel);
		}
	}

	/**
	 * Écrire une fichier sur le disque.
	 * Dans l'implémentation actuelle, affiche le contenu du fichier dans la console.
	 * @param nomFichier Le nom du fichier
	 * @param contenu Le contenu du fichier.
	 */
	private void ecrireFichier(String nomFichier, String contenu) {
		System.out.println(nomFichier + ": ");
		System.out.println(contenu);

		/*try {
			File fichier = new File(nomFichier);
			if (fichier.createNewFile()) {
				System.out.println("Fichier créé: "+nomFichier;
			} else {
				System.out.println("Erreur: le fichier n'a pas pu être créé.");
				FileWriter myWriter = new FileWriter("filename.txt");
				myWriter.write(contenu);
				myWriter.close();
			}
		} catch (IOException e) {
			System.out.println("An error occurred while trying to write to the file.");
			e.printStackTrace();
		}*/
	}
}
