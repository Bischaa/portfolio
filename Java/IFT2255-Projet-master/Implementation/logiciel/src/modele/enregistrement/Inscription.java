package modele.enregistrement;

import modele.compte.CompteMembre;

public class Inscription extends Enregistrement
{
	private CompteMembre membre;
	private Seance seance;

	public Inscription(CompteMembre membre, Seance seance, String commentaire)
	{
		super(seance.getService().nomService, commentaire);
		this.membre = membre;
		this.seance = seance;
	}

	public CompteMembre getMembre() {
		return this.membre;
	}
	public Seance getSeance() {
		return seance;
	}
}
