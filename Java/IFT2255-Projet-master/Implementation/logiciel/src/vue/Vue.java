package vue;

import modele.CompteID;
import modele.SeanceID;
import modele.enregistrement.Inscription;
import modele.enregistrement.Seance;
import modele.enregistrement.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public interface Vue {

	/**
	 * Simuler une interface utilisateur.
	 * @throws Exception S'il y a un problème lors de la simulation
	 */
	void simulation() throws Exception;

	/**
	 * Méthode appeler lorsque la simulation se termine.
	 */
	void quitter();

	default CompteID lireNumeroCompte() {
		throw new UnsupportedOperationException("Pas encore implémenter");
	}

	default SeanceID lireNumeroSeance() {
		throw new UnsupportedOperationException("Pas encore implémenter");
	}

	/**
	 * Permet à l'utilisateur de sélectionner un service parmi une liste donnée.
	 * @param services Liste des services parmi laquelle sélectionner un service
	 * @return Le service sélectionné
	 */
	default Service selectionnerService(List<Service> services) {
		for(int i = 0; i < services.size(); i++)
		{
			Service service = services.get(i);
			System.out.print(i);
			afficherService(service);
		}

		System.out.print("Sélectionner un service: ");
		int index = new Scanner(System.in).nextInt();

		return services.get(index);
	}

	/**
	 * Permet à l'utilisateur de sélectionner une séance parmi une liste donnée.
	 * @param seances Liste des séances parmi laquelle sélectionner une séance
	 * @return La séance sélectionnée
	 */
	default Seance selectionnerSeance(List<Seance> seances) {
		for(int i = 0; i < seances.size(); i++)
		{
			Seance seance = seances.get(i);
			System.out.print(i);
			afficherSeance(seance);
		}

		System.out.print("Sélectionner une séance: ");
		int index = new Scanner(System.in).nextInt();

		return seances.get(index);
	}

	/**
	 * Permet à l'utilisateur de sélectionner une inscfription parmi une liste donnée.
	 * @param inscriptions La liste d'inscriptions
	 * @return L'inscription sélectionnée
	 */
	default Inscription selectionnerInscription(List<Inscription> inscriptions) {
		for(int i = 0; i < inscriptions.size(); i++)
		{
			Inscription inscription = inscriptions.get(i);
			System.out.print(i);
			afficherInscription(inscription);
		}

		System.out.print("Sélectionner un service: ");
		int index = new Scanner(System.in).nextInt();

		return inscriptions.get(index);
	}

	/**
	 * Afficher les informations d'un service à l'utilisateur
	 * @param service Le service à afficher
	 */
	default void afficherService(Service service) {
		System.out.println(service.getNomService() + " (" + service.getNumeroService().getNumero() + ")");

		if(service.getProfessionnel() != null)
		{
			System.out.println("\t-Par " + service.getProfessionnel().getNom());
		}
	}

	/**
	 * Afficher les informations d'une séance à l'utilisateur
	 * @param seance La séance à afficher
	 */
	default void afficherSeance(Seance seance) {
		System.out.println(seance.getNomService() + " (" + seance.getNumeroSeance().getNumero() + ")");
		System.out.println("\t-Par " + seance.getService().getProfessionnel().getNom());
		System.out.println("\t-Du " + seance.getDateDebut() + " au " + seance.getDateFin());
		System.out.print("\t-Le ");
		for(LocalDate date : seance.getRecurrence())
		{
			System.out.print(date.getDayOfWeek() + ", ");
		}
		System.out.println();
		System.out.println("\t-À " + seance.getHeure());
		System.out.println("\t-Capacité: " + seance.getCapacite());
		System.out.println("\t-Frais: " + seance.getFrais());
	}

	default void afficherInscription(Inscription inscription) {
		System.out.println(inscription.getNomService());
		System.out.println("\t-Par " + inscription.getSeance().getService().getProfessionnel().getNom());
		System.out.println("\t-Pour " + inscription.getMembre().getNom());
	}

	/**
	 * Afficher une question à l'utilisateur et renvoit sa réponse.
	 * La réponse renvoyée une est String.
	 * @param message Le message à afficher (la question)
	 * @return La réponse de l'utilisateur
	 */
	default String demanderInformationString(String message) {
		System.out.print(message);

		return new Scanner(System.in).next();
	}
	/**
	 * Afficher une question à l'utilisateur et renvoit sa réponse.
	 * La réponse renvoyée est un int.
	 * @param message Le message à afficher (la question)
	 * @return La réponse de l'utilisateur
	 */
	default int demanderInformationInt(String message) {
		System.out.print(message);

		return new Scanner(System.in).nextInt();
	}
	/**
	 * Afficher une question à l'utilisateur et renvoit sa réponse.
	 * La réponse renvoyée est un double.
	 * @param message Le message à afficher (la question)
	 * @return La réponse de l'utilisateur
	 */
	default double demanderInformationDouble(String message) {
		System.out.print(message);

		return new Scanner(System.in).nextDouble();
	}

	/**
	 * Demander une confirmation à l'utilisateur.
	 * @param message Le message à afficher
	 * @return True si l'utilisateur confirme, False sinon
	 */
	default boolean confirmer(String message) {
		System.out.print(message + " (y/n) ");

		String reponse = new Scanner(System.in).next();

		if(reponse.equals("yes") || reponse.equals("y") || reponse.equals("Yes") || reponse.equals("Y"))
		{
			return true;
		}

		return false;
	}

}
