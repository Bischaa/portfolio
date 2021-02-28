package controleur;

import modele.compte.Compte;
import modele.compte.RepertoireCompte;
import vue.VueCompte;

public class ControleurCompte {

	protected VueCompte vue;

	public ControleurCompte(VueCompte vue) {
		this.vue = vue;
	}

	/**
	 * Modifier un compte avec les informations données.
	 * @param nom Le nouveau nom
	 * @param adresse La nouvelle adresse
	 * @param telephone Le nouveau numéro de téléphone
	 * @param email Le nouveau email
	 * @param compte Le compte dont on veut modifier les informations
	 */
	public void modifierCompte(String nom, String adresse, String telephone, String email, Compte compte) {
		compte.setNom(nom);
		compte.setAdresse(adresse);
		compte.setTelephone(telephone);
		compte.setEmail(email);

		//Inutile dans l'implémentation actuelle
		//RepertoireCompte.enregistrerCompte(compte);
	}

	/**
	 * Supprimer un compte
	 * @param compte Le compte à supprimer
	 */
	public void supprimerCompte(Compte compte) {
		RepertoireCompte.supprimerCompte(compte);
	}
}
