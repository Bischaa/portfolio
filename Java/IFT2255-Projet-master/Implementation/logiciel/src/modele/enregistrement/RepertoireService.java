package modele.enregistrement;

import modele.CompteID;
import modele.Filtre;
import modele.SeanceID;
import modele.ServiceID;
import modele.compte.Compte;
import modele.compte.CompteMembre;
import modele.compte.CompteProfessionnel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class RepertoireService {

	private static Service[] defaultServices = new Service[] {
			new Service("Zumba", null, null, new ServiceID("111")),
			new Service("Musculation", null, null, new ServiceID("234")),
			new Service("Course", null, null, new ServiceID("987"))
	};

	private static List<Inscription> inscriptions = new ArrayList<>();
	private static List<Service> services = new ArrayList<>();
	private static List<Seance> seances = new ArrayList<>();
	private static List<Confirmation> confirmations = new ArrayList<>();

	/**
	 * Créer un nouveau numéro de service.
	 * Pas vraiment utile dans l'implémentation actuelle.
	 * @return Un numéro de service
	 */
	private static ServiceID creerNumeroService() {
		int id = 111;

		if(!services.isEmpty())
		{
			id = Integer.parseInt(services.get(services.size() - 1).getNumeroService().getNumero());
			id++;
		}


		return new ServiceID(String.valueOf(id));
	}

	/**
	 * Créer un nouveau numéro de séance
	 * @param numeroService Le service pour lequel la séance est offerte
	 * @param numeroProfessionnel Le professionnel qui offre la séance
	 */
	private static SeanceID creerNumeroSeance(ServiceID numeroService, CompteID numeroProfessionnel) {
		Random rand = new Random();
		String numeroSeance = "";

		//Ajouter le numéro de service
		numeroSeance += numeroService.getNumero();

		//Créer un nouveau numéro de séance aléatoire
		numeroSeance += String.valueOf(rand.nextInt(10));
		numeroSeance += String.valueOf(rand.nextInt(10));

		//Ajouter les deux premiers chiffres du numéro de professionnel
		numeroSeance += numeroProfessionnel.getNumero().substring(0,2);

		return new SeanceID(numeroSeance);
	}

	/**
	 * Lire les services répondant à certains critères sur le disque. Les critères de sélection sont donnés pas
	 * le filtre.
	 * @param filtre Filtre implémentant la méthode "boolean filtre(Service service)" et permettant de déterminer
	 *               si un service répond au critère de sélection.
	 */
	public static List<Service> lireServices(Filtre<Service> filtre) {
		ArrayList<Service> servicesFiltrer = new ArrayList<>();

		for(Service service : services){
			if(filtre.filtre(service))
			{
				servicesFiltrer.add(service);
			}
		}

		return servicesFiltrer;
	}

	/**
	 * Retourner les services par défaut utilisé pour créer les autres services.
	 * @return Une array de service
	 */
	public static Service[] getDefaultService() {
		return defaultServices;
	}

	/**
	 * Créer une nouvelle séance pour un service.
	 * @param dateDebut La date à partir de laquelle la séance est offerte
	 * @param dateFin La date après laquelle la séance n'est plus offerte
	 * @param heure L'heure de début d'une séance
	 * @param recurrence Les jours pour lesquelles sont offerts la séance
	 * @param capacite Le nombre maximal de membre pour assister à la séance
	 * @param frais Les frais de la séance
	 * @param commentaire Un commentaire sur la séance
	 * @param service Le service pour lequelle la séance est offerte
	 */
	public static Seance creerSeance(LocalDate dateDebut, LocalDate dateFin, int heure, LocalDate[] recurrence,
							  int capacite, double frais, String commentaire, Service service) {
		SeanceID numeroSeance = creerNumeroSeance(service.getNumeroService(), service.getProfessionnel().getNumeroCompte());
		Seance seance = new Seance(dateDebut, dateFin, heure, recurrence, capacite, frais, commentaire, service, numeroSeance);

		service.addSeance(seance);

		seances.add(seance);
		return seance;
	}

	/**
	 * Lire les séances répondant à certain critère sur le disque. Les critères de sélection sont donnés pas
	 * le filtre.
	 * @param filtre Filtre implémentant la méthode "boolean filtre(Seance seance)" et permettant de déterminer
	 *               si une séance répond au critère de sélection.
	 */
	public static List<Seance> lireSeances(Filtre<Seance> filtre) {
		ArrayList<Seance> seanceFiltre = new ArrayList<>();
		for(Seance seance : seances)
		{
			if(filtre.filtre(seance))
			{
				seanceFiltre.add(seance);
			}
		}

		return seanceFiltre;
	}

	/**
	 * Enregistrer une séance sur le disque. Doit être appelé après avoir modifé la séance pour enregistrer les changements.
	 * Dans l'implémentation actuelle, cette méthode ne sert à rien.
	 * @param seance La séance à enregistrer
	 */
	public static void enregistrerSeance(Seance seance) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * Supprimer une séance sur le disque.
	 * @param seance La séance à supprimer
	 */
	public static void supprimerSeance(Seance seance) {
		seances.remove(seance);
	}

	/**
	 * Créer une inscription sur une séance.
	 * @param seance La séance de l'inscription
	 * @param membre La membre inscrit
	 * @param commentaire Un commmentaire sur l'inscription
	 */
	public static Inscription creerInscription(Seance seance, CompteMembre membre, String commentaire) {
		Inscription inscription = new Inscription(membre, seance, commentaire);
		inscriptions.add(inscription);
		seance.addInscription(inscription);
		membre.addInscription(inscription);

		return inscription;
	}

	/**
	 * Lire les inscriptions répondant à certains critères sur le disque. Les critères de sélection sont donnés pas
	 * le filtre.
	 * @param filtre Filtre implémentant la méthode "boolean filtre(Inscription inscription)" et permettant de déterminer
	 *               si une inscription répond au critère de sélection.
	 */
	public static List<Inscription> lireInscriptions(Filtre<Inscription> filtre) {
		ArrayList<Inscription> inscriptionsFiltre = new ArrayList<>();
		for(Inscription inscription : inscriptions)
		{
			if(filtre.filtre(inscription))
			{
				inscriptionsFiltre.add(inscription);
			}
		}

		return inscriptionsFiltre;
	}

	/**
	 * Supprimer une inscription sur le disque.
	 * @param inscription L'inscription à supprimer
	 */
	public static void supprimerInscription(Inscription inscription) {
		inscriptions.remove(inscription);
	}

	/**
	 * Créer une confirmation pour un service offert à un membre.
	 * @param membre Le membre
	 * @param service Le service qui a été offert
	 * @param commentaire Un commentaire sur la confirmation
	 */
	public static void creerConfirmation(CompteMembre membre, Service service, String commentaire) {
		Confirmation confirmation = new Confirmation(service.getNomService(), commentaire, membre.getNumeroCompte(),
				service.getProfessionnel().getNumeroCompte(), service.getNumeroService());

		confirmations.add(confirmation);
	}

	/**
	 * Supprimer une confirmation
	 * @param confirmation La confirmation à supprimer
	 */
	public static void supprimerConfirmation(Confirmation confirmation) {
		confirmations.remove(confirmation);
	}

	/**
	 * Lire les confirmations répondant à certain critère sur le disque. Les critères de sélection sont donnés pas
	 * le filtre.
	 * @param filtre Filtre implémentant la méthode "boolean filtre(Confirmation confirmation)" et permettant de déterminer
	 *               si une confirmation répond au critère de sélection.
	 */
	public static List<Confirmation> lireConfirmations(Filtre<Confirmation> filtre) {
		ArrayList<Confirmation> confirmationsFiltre = new ArrayList<>();
		for(Confirmation confirmation : confirmations)
		{
			if(filtre.filtre(confirmation))
			{
				confirmationsFiltre.add(confirmation);
			}
		}

		return confirmationsFiltre;
	}
}
