package modele.enregistrement;

import modele.ServiceID;
import modele.compte.CompteProfessionnel;

import java.time.LocalDate;
import java.util.*;

public class Service extends Enregistrement
{
	private CompteProfessionnel professionnel;
	private List<Seance> seances;

	private ServiceID numeroService;

	public Service(String nomService, String commentaire, CompteProfessionnel professionnel, ServiceID numeroService)
	{
		super(nomService, commentaire);
		this.professionnel = professionnel;
		this.numeroService = numeroService;

		seances = new ArrayList<>();
	}

	protected void addSeance(Seance seance) {
		seances.add(seance);
	}
	protected void removeSeance(Seance seance) {
		seances.remove(seance);
	}
	public List<Seance> getSeances()
	{
		return seances;
	}

	public CompteProfessionnel getProfessionnel()
	{
		return professionnel;
	}

	public ServiceID getNumeroService()
	{
		return numeroService;
	}
}
