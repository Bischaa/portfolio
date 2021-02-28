package controleur;

import modele.Filtre;
import modele.compte.Compte;
import modele.compte.CompteMembre;
import modele.compte.CompteProfessionnel;
import modele.compte.RepertoireCompte;
import modele.enregistrement.Inscription;
import modele.enregistrement.RepertoireService;
import modele.enregistrement.Seance;
import modele.enregistrement.Service;
import vue.VueCompte;

import java.time.LocalDate;
import java.util.List;

public class ControleurMembre extends ControleurCompte {

	public ControleurMembre(VueCompte vue) {
		super(vue);
	}

	/**
	 * Inscrire le membre à une séance.
	 * @param membre Le membre à inscrire.
	 */
	public void inscription(CompteMembre membre) {
		//Sélectionner un service
		List<Service> services = RepertoireService.lireServices(service -> true);
		Service service = vue.selectionnerService(services);

		//Sélectionner une séance
		List<Seance> seances = service.getSeances();
		Seance seance = vue.selectionnerSeance(seances);

		//Vérifier les conflits d'horaire
		boolean conflitHoraire = false;
		int i = 0;
		while(!conflitHoraire && i < seance.getRecurrence().length)
		{
			LocalDate date = seance.getRecurrence()[i];
			int j = 0;

			while (!conflitHoraire && j < membre.getInscriptions().size())
			{
				Seance otherSeance = membre.getInscriptions().get(j).getSeance();

				int k = 0;
				while (!conflitHoraire && k < otherSeance.getRecurrence().length)
				{
					LocalDate otherDate = otherSeance.getRecurrence()[k];

					if(date.isEqual(otherDate) && seance.getHeure() == otherSeance.getHeure())
					{
						conflitHoraire = true;
					}

					k++;
				}

				j++;
			}

			i++;
		}

		if(!conflitHoraire)
		{
			String commentaire = vue.demanderInformationString("Commentaire: ");

			RepertoireService.creerInscription(seance, membre, commentaire);
		}

	}

	/**
	 * Annuler une inscription du membre.
	 * @param membre Le membre
	 */
	public void annulerInscription(CompteMembre membre) {
		Inscription inscription = vue.selectionnerInscription(membre.getInscriptions());

		if(vue.confirmer("Êtes-vous sûr de vouloir supprimer cette inscription?"))
		{
			RepertoireService.supprimerInscription(inscription);
		}
	}

	/**
	 * Consulter les inscriptions d'un membre
	 * @param membre Le membre
	 */
	public List<Inscription> consulterInscriptions(CompteMembre membre) {
		return membre.getInscriptions();
	}

}
