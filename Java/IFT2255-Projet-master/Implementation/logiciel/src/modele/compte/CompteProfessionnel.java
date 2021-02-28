package modele.compte;

import modele.CompteID;
import modele.enregistrement.Service;

import java.util.*;

public class CompteProfessionnel extends Compte {

	private List<Service> services;
	private String expertise;

	public CompteProfessionnel(String nom, String adresse, String telephone, String email, String expertise, CompteID numeroCompte)
	{
		super(nom, adresse, telephone, email, numeroCompte);
		this.expertise = expertise;
		services = new ArrayList<>();
	}


	protected void addService(Service service) {
		services.add(service);
	}
	protected void removeService(Service service) {
		services.remove(service);
	}
	public List<Service> getServices() {
		return this.services;
	}

	public String getExpertise() {
		return this.expertise;
	}
	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

}
