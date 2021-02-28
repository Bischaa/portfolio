package controleur;

import modele.Filtre;
import modele.compte.Compte;
import modele.compte.RepertoireCompte;
import vue.Application;

import java.util.List;
import java.util.NoSuchElementException;

public class ControleurPrincipal {

	private Application vue;

	public ControleurPrincipal(Application vue) {
		this.vue = vue;
	}
	/**
	 * Connecter un utilisateur à l'application.
	 * @param email L'email de l'utilisateur
	 */
	public Compte connexion(String email) {
		//Trouve un membre dont le courriel match avec celui fourni
		Filtre<Compte> filtreEmail = compte -> compte.getEmail().equals(email);

		List<Compte> listeComptes = RepertoireCompte.lireComptes(filtreEmail);
		if(listeComptes.size() > 1)
		{
			throw new NoSuchElementException("Plus d'un compte avec cet email: " + email);
		}
		else if(listeComptes.size() == 0)
		{
			return null;
		}
		else
		{
			return listeComptes.remove(0);
		}
	}

}
