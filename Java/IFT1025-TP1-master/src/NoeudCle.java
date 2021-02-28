//IFT1025-TP1
//Maxime Ton et Youssef Hilout

public class NoeudCle{
	//Attributs
	NoeudCle prochain;
	String cle;
	ListeValeur mot = new ListeValeur();
	
	//Constructeur
	public NoeudCle(String cle, NoeudCle prochain) {
		this.cle = cle;
		this.prochain = prochain;
	}
	
}
