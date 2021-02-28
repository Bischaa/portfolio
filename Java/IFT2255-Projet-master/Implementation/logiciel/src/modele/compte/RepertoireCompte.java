package modele.compte;

import modele.CompteID;
import modele.Filtre;
import modele.compte.Compte;
import modele.compte.CompteMembre;
import modele.compte.CompteProfessionnel;

import java.util.*;

public class RepertoireCompte {

	private static List<CompteProfessionnel> professionnels = new ArrayList<>();
	private static List<CompteMembre> membres = new ArrayList<>();

	/**
	 * Créer un nouveau numéro de compte
	 * @return Un numero de compte
	 */
	private static CompteID creerNumeroCompte() {
		int id = 111111111;

		//Find last ID
		if(!professionnels.isEmpty())
		{
			id = Integer.parseInt(professionnels.get(professionnels.size() - 1).getNumeroCompte().getNumero());
		}
		if(!membres.isEmpty())
		{
			int lastMemberID = Integer.parseInt(membres.get(membres.size() - 1).getNumeroCompte().getNumero());
			if(lastMemberID > id)
			{
				id = lastMemberID;
			}
		}

		//Nouvel ID
		id++;

		return new CompteID(String.valueOf(id));
	}

	/**
	 * Créer un nouveau compte membre avec les informations données.
	 * @param nom Le nom associé au compte
	 * @param adresse L'adresse associée au compte
	 * @param telephone Le téléphone associé au compte
	 * @param email Le email associé au compte
	 */
	public static CompteMembre creerCompteMembre(String nom, String adresse, String telephone, String email) {
		CompteID numeroCompte = creerNumeroCompte();

		CompteMembre membre = new CompteMembre(nom, adresse, telephone, email, numeroCompte);
		membres.add(membre);

		return membre;
	}

	/**
	 * Créer un nouveau compte professionnel avec les informations données.
	 * @param nom Le nom associé au compte
	 * @param adresse L'adresse associée au compte
	 * @param telephone Le téléphone associé au compte
	 * @param expertise L'expertise associé au professionnel
	 * @param email L'email associé au compte
	 */
	public static CompteProfessionnel creerCompteProfessionnel(String nom, String adresse, String telephone, String expertise, String email) {
		CompteID numeroCompte = creerNumeroCompte();

		CompteProfessionnel professionnel = new CompteProfessionnel(nom, adresse, telephone, expertise, email, numeroCompte);
		professionnels.add(professionnel);

		return professionnel;
	}

	/**
	 * Lire les comptes répondant à certain critère sur le disque. Les critères de sélection sont donnés par
	 * le filtre.
	 * @param filtre Filtre implémentant la méthode "boolean filtre(Compte compte)" et permettant de déterminer
	 *               si un compte répond au critère de sélection.
	 */
	public static List<Compte> lireComptes(Filtre<Compte> filtre) {
		ArrayList<Compte> comptesFiltres = new ArrayList<>();
		for(CompteMembre membre : membres)
		{
			if(filtre.filtre(membre))
			{
				comptesFiltres.add(membre);
			}
		}
		for(CompteProfessionnel professionnel : professionnels)
		{
			if(filtre.filtre(professionnel))
			{
				comptesFiltres.add(professionnel);
			}
		}

		return comptesFiltres;
	}

	/**
	 * Enregistre un compte sur le disque. Doit être appelé après avoir modifié les informations du compte
	 * pour enregistrer les changements.
	 * Dans l'implémentation actuelle, cette méthode n'est pas utile.
	 * @param compte Le compte à enregistrer
	 */
	public static void enregistrerCompte(Compte compte) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * Supprimer un compte et ses informations du disque.
	 * @param compte Le compte à supprimer
	 */
	public static void supprimerCompte(Compte compte) {
		if(compte instanceof CompteMembre)
		{
			membres.remove(compte);
		}
		else if(compte instanceof CompteProfessionnel)
		{
			professionnels.remove(compte);
		}
	}

}
