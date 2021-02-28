package modele.enregistrement;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Enregistrement {

	protected LocalDateTime dateHeureEntree;
	protected String commentaire;
	protected String nomService;

	protected Enregistrement(String nomService, String commentaire)
	{
		this.nomService = nomService;
		this.commentaire = commentaire;

		dateHeureEntree = LocalDateTime.now();
	}

	public LocalDateTime getDateHeureEntree() {
		return this.dateHeureEntree;
	}

	public String getNomService() {
		return this.nomService;
	}

	public String getCommentaire() {
		return this.commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
}
