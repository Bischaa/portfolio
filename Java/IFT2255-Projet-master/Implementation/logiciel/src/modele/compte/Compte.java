package modele.compte;

import modele.CompteID;

public abstract class Compte {

	private CompteID numeroCompte;
	private boolean isValide;

	private String nom;
	private String adresse;
	private String telephone;
	private String email;

	protected Compte(String nom, String adresse, String telephone, String email, CompteID numeroCompte)
	{
		this.nom = nom;
		this.adresse = adresse;
		this.telephone = telephone;
		this.email = email;
		this.numeroCompte = numeroCompte;
	}

	public CompteID getNumeroCompte () {
		return this.numeroCompte;
	}
	public String getNom() {
		return this.nom;
	}
	public String getAdresse() {
		return this.adresse;
	}
	public String getTelephone() {
		return this.telephone;
	}
	public String getEmail() {
		return this.email;
	}
	public boolean getValide() {
		return this.isValide;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setValide(boolean isValide) {
		this.isValide = isValide;
	}



}
