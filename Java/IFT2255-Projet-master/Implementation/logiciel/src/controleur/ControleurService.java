package controleur;

import modele.CompteID;
import modele.Filtre;
import modele.SeanceID;
import modele.compte.Compte;
import modele.compte.CompteProfessionnel;
import modele.compte.RepertoireCompte;
import modele.enregistrement.Inscription;
import modele.enregistrement.RepertoireService;
import modele.enregistrement.Seance;
import modele.enregistrement.Service;
import vue.LogicielAdministration;

import java.time.LocalDate;
import java.util.*;

public class ControleurService {

	private LogicielAdministration vue;

	public ControleurService(LogicielAdministration vue) {
		this.vue = vue;

	}

	/**
	 * Créer une séance pour le professionnel
	 * @param numeroCompte Le numéro de compte du professionnel
	 */
	public void creerSeance(CompteID numeroCompte) {
		//Lire le compte professionnel (est validé avant cette méthode)
		List<Compte> comptes = RepertoireCompte.lireComptes(compte -> compte.getNumeroCompte() == numeroCompte);
		Compte compte = comptes.get(0);

		if(!(compte instanceof CompteProfessionnel))
		{
			throw new NoSuchElementException("Pas un compte professionnel");
		}
		CompteProfessionnel professionnel = (CompteProfessionnel) compte;

		//Sélectionner un service
		Service[] defaultServices = RepertoireService.getDefaultService();
		Service defaultService = vue.selectionnerService(Arrays.asList(defaultServices));
		Service service = new Service(defaultService.getNomService(), defaultService.getCommentaire(),
				professionnel, defaultService.getNumeroService());

		//Les dates
		LocalDate dateDebut = null;
		LocalDate dateFin = null;
		LocalDate[] recurrence;

		String[] dates = vue.demanderInformationString("À quel(s) jour(s) est offerte la séance (jj/mm, séparé par des virgules): ").split(",");
		recurrence = new LocalDate[dates.length];
		for(int i = 0; i < dates.length; i++)
		{
			String[] temp = dates[i].split("/");
			LocalDate date = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(temp[1]), Integer.parseInt(temp[0]));

			if(i == 0)
			{
				dateDebut = date;
			}
			else if(i == dates.length - 1)
			{
				dateFin = date;
			}
			recurrence[i] = date;
		}

		//L'heure
		int heure = vue.demanderInformationInt("Heure: ");

		//Capacité
		int capacite = vue.demanderInformationInt("Capacité: ");

		//Frais
		double frais = vue.demanderInformationDouble("Frais: ");

		//Commentaire
		String commentaire = vue. demanderInformationString("Commentaire: ");

		RepertoireService.creerSeance(dateDebut, dateFin, heure, recurrence, capacite, frais, commentaire, service);
	}

	/**
	 * Modifier les informations d'une séance.
	 * @param numeroSeance Le numéro de la séance à modifier
	 */
	public void modifierSeance(SeanceID numeroSeance) {
		//Lire le compte professionnel (est validé avant cette méthode)
		List<Seance> seances = RepertoireService.lireSeances(seance -> seance.getNumeroSeance() == numeroSeance);

		if(seances.size() != 1) {
			throw new NoSuchElementException("Aucun ou trop de séance avec ce numéro: " + numeroSeance);
		}
		Seance seance = seances.get(0);

		boolean loop = true;
		while(loop)
		{
			System.out.println("Informations du compte");
			System.out.print("1. Dates: ");
			StringBuilder dates = new StringBuilder();
			for(LocalDate date : seance.getRecurrence())
			{
				dates.append(date.toString()).append(", ");
			}
			System.out.println(dates.toString());
			System.out.println("2. Heure: " + seance.getHeure());
			System.out.println("3. Capacité: " + seance.getCapacite());
			System.out.println("4. Frais: " + seance.getFrais());
			System.out.println("5. Terminer");
			System.out.print("Quelles informations voulez-vous modifier? ");
			int option = new Scanner(System.in).nextInt();

			switch(option)
			{
				case 1:
					String[] datesStr = vue.demanderInformationString("À quel(s) jour(s) est offerte la séance (jj/mm, séparé par des virgules): ").split(",");
					LocalDate[] recurrence = new LocalDate[datesStr.length];
					for(int i = 0; i < datesStr.length; i++)
					{
						String[] temp = datesStr[i].split("/");
						LocalDate date = LocalDate.of(LocalDate.now().getYear(), Integer.parseInt(temp[1]), Integer.parseInt(temp[0]));

						if(i == 0)
						{
							seance.setDateDebut(date);
						}
						else if(i == datesStr.length - 1)
						{
							seance.setDateFin(date);
						}
						seance.setRecurrence(recurrence);
					}
					break;
				case 2:
					int heure = vue.demanderInformationInt("Heure: ");
					seance.setHeure(heure);
					break;
				case 3:
					int capacite = vue.demanderInformationInt("Capacité: ");
					seance.setCapacite(capacite);
					break;
				case 4:
					double frais = vue.demanderInformationDouble("Frais: ");
					seance.setFrais(frais);
					break;
				case 5:
				default:
					loop = false;
			}
		}

		//Inutile dans l'implémentation actuelle
		//RepertoireService.enregistrerSeance(seance);
	}

	/**
	 * Supprimer une séance
	 * @param numeroSeance Le numéro de la séance à supprimer
	 */
	public void supprimerSeance(SeanceID numeroSeance) {
		//Trouve une séance dont le numéro match
		Filtre<Seance> filtreSeance = seance -> seance.getNumeroSeance() == numeroSeance;
		List<Seance> listeSeances = RepertoireService.lireSeances(filtreSeance);

		if(listeSeances.size() != 1)
		{
			throw new NoSuchElementException("Aucun ou trop de séance associée à ce numéro: " + numeroSeance);
		}

		Seance seance = listeSeances.get(0);
		RepertoireService.supprimerSeance(seance);
	}

	/**
	 * Consulter les inscriptions aux séances d'un professionnel.
	 * @param numeroCompte Le numéro du compte du professionnel
	 */
	public List<Inscription> consulterInscriptions(CompteID numeroCompte) {
		//Lire le compte du professionnel (à été validé avant l'appel)
		List<Compte> comptes = RepertoireCompte.lireComptes(compte -> compte.getNumeroCompte() == numeroCompte);
		Compte compte = comptes.get(0);

		if(!(compte instanceof CompteProfessionnel))
		{
			throw new NoSuchElementException("Pas un compte de professionnel");
		}

		List<Inscription> inscriptions = new ArrayList<>();

		List<Service> services = ((CompteProfessionnel) compte).getServices();
		for(Service service : services)
		{
			List<Seance> seances = service.getSeances();
			for(Seance seance : seances)
			{
				inscriptions.addAll(seance.getInscriptions());
			}
		}

		return inscriptions;
	}

}
