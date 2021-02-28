package modele.enregistrement;

import modele.CompteID;
import modele.ServiceID;

public class Confirmation extends Enregistrement {

	private CompteID numeroCompteProfessionnel;
	private CompteID numeroCompteMembre;
	private ServiceID numeroService;

	public Confirmation(String nomService, String commentaire, CompteID numeroCompteMembre, CompteID numeroCompteProfessionnel, ServiceID numeroService)
	{
		super(nomService, commentaire);
		this.numeroCompteMembre = numeroCompteMembre;
		this.numeroCompteProfessionnel = numeroCompteProfessionnel;
		this.numeroService = numeroService;
	}

	public CompteID getNumeroCompteProfessionnel() {
		return this.numeroCompteProfessionnel;
	}
	public CompteID getNumeroCompteMembre() {
		return this.numeroCompteMembre;
	}
	public ServiceID getNumeroService() {
		return this.numeroService;
	}
}
