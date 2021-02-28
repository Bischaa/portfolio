package controleur;

import modele.CompteID;
import modele.Filtre;
import modele.compte.Compte;
import modele.compte.CompteMembre;
import modele.compte.CompteProfessionnel;
import modele.compte.RepertoireCompte;
import modele.enregistrement.Inscription;
import modele.enregistrement.Seance;
import modele.enregistrement.Service;
import vue.VueCompte;

import java.util.List;
import java.util.NoSuchElementException;

public class ControleurProfessionnel extends ControleurCompte {

	public ControleurProfessionnel(VueCompte vue) {
		super(vue);
	}

	/**
	 * Consulter les inscriptions à une séance offerte par le professionnel.
	 * @param professionnel Le professionnel
	 */
	public List<Inscription> consulterInscriptions(CompteProfessionnel professionnel) {

		Seance seance  = consulterSeance(professionnel);
		return seance.getInscriptions();
	}

	/**
	 * Confirmer la présence d'un membre à une séance.
	 * @param professionnel Le professionnel qui offre la séance
	 */
	public boolean confirmerPresence(CompteProfessionnel professionnel) {
		Seance seance = consulterSeance(professionnel);

		//Reçevoir un numéro de compte
		CompteID numeroCompte = vue.lireNumeroCompte();
		List<Compte> comptes = RepertoireCompte.lireComptes(compte1 -> compte1.getNumeroCompte() == numeroCompte);
		if(comptes.size() != 1)
		{
			throw new NoSuchElementException("Plus d'un compte à ce numéro: " + numeroCompte);
		}
		else if(comptes.get(0) instanceof CompteMembre)
		{
			return seance.checkInscription((CompteMembre) comptes.get(0));
		}

		return false;
	}

	/**
	 * Permettre au professionnel de sélectionner une séance qu'il offre
	 * @param professionnel Le professionnel
	 * @return La séance sélectionnée
	 */
	private Seance consulterSeance(CompteProfessionnel professionnel) {
		//Sélectionner un service
		List<Service> services = professionnel.getServices();
		Service service = vue.selectionnerService(services);

		//Sélectionner une séance
		List<Seance> seances = service.getSeances();

		return vue.selectionnerSeance(seances);
	}

}
