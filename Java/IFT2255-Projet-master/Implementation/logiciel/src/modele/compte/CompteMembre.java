package modele.compte;

import modele.CompteID;
import modele.enregistrement.Inscription;

import java.util.*;

public class CompteMembre extends Compte {

	private List<Inscription> inscriptions;
	private boolean hasPaid;

	public CompteMembre(String nom, String adresse, String telephone, String email, CompteID numeroCompte)
	{
		super(nom, adresse, telephone, email, numeroCompte);
		inscriptions = new ArrayList<>();
	}

	/**
	 * Ajouter une inscription à ce membre
	 * @param inscription L'inscription à ajouter
	 */
	public void addInscription(Inscription inscription) {
		inscriptions.add(inscription);
	}
	/**
	 * Enlever une inscription du membre
	 * @param inscription L'inscription à enlever
	 */
	public void removeInscription(Inscription inscription) {
		inscriptions.remove(inscription);
	}

	public List<Inscription> getInscriptions()
	{
		return inscriptions;
	}

	public boolean hasPaid() {
		return this.hasPaid;
	}
	public void setHasPaid(boolean hasPaid) {
		this.hasPaid = hasPaid;
	}
}
