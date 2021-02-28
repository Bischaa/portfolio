package modele.enregistrement;

import modele.SeanceID;
import modele.compte.CompteMembre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Seance extends Enregistrement
{
	private List<Inscription> inscriptions;
	private SeanceID numeroSeance;
	private Service service;

	private LocalDate dateDebut;
	private LocalDate dateFin;
	private int heure;
	private LocalDate[] recurrence;
	private int capacite;
	private double frais;

	public Seance(LocalDate dateDebut, LocalDate dateFin,
				  int heure, LocalDate[] recurrence, int capacite, double frais, String commentaire, Service service, SeanceID numeroSeance)
	{
		super(service.getNomService(), commentaire);
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.heure = heure;
		this.recurrence = recurrence;
		this.capacite = capacite;
		this.frais = frais;

		this.service = service;
		this.numeroSeance = numeroSeance;

		inscriptions = new ArrayList<>();
	}

	/**
	 * Vérifier si le membre est inscrit à cette séance.
	 * @param membre Le membre à vérifier
	 */
	public boolean checkInscription(CompteMembre membre) {
		for (Inscription inscription : this.inscriptions)
		{
			if (inscription.getMembre() == membre)
			{
				return true;
			}
		}
		return false;
	}

	public LocalDate getDateDebut() {
		return this.dateDebut;
	}
	public LocalDate getDateFin() {
		return this.dateFin;
	}
	public int getHeure() {
		return this.heure;
	}
	public LocalDate[] getRecurrence() {
		return this.recurrence;
	}
	public int getCapacite() {
		return this.capacite;
	}
	public double getFrais() {
		return this.frais;
	}
	public Service getService() {
		return service;
	}
	public SeanceID getNumeroSeance() {
		return numeroSeance;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}
	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}
	public void setHeure(int heure) {
		this.heure = heure;
	}
	public void setRecurrence(LocalDate[] recurrence) {
		this.recurrence = recurrence;
	}
	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}
	public void setFrais(double frais) {
		this.frais = frais;
	}

	/**
	 * Ajouter une inscription pour cette séance.
	 * @param inscription L'inscription à ajouter
	 */
	public void addInscription(Inscription inscription) {
		this.inscriptions.add(inscription);
	}
	/**
	 * Enlever une inscription à cette séance.
	 * @param inscription L'inscription à enlever
	 */
	public void removeInscription(Inscription inscription) {
		inscriptions.remove(inscription);
	}
	public List<Inscription> getInscriptions() {
		return inscriptions;
	}
}
