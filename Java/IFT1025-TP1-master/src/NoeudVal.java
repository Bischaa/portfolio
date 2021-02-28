//IFT1025-TP1
//Maxime Ton et Youssef Hilout

public class NoeudVal {
	//Attributs
	NoeudVal prochain;
	String valeur;
	int freq;
	int Index;
	
	//Constructeur
	public NoeudVal(String valeur, int freq, NoeudVal prochain) {
		this.valeur = valeur;
		this.freq = freq;
		this.prochain = prochain;
	}
	
	//M�thode pour retourner la fr�quence
	public int getFreq() {
		return this.freq;
	}
	
	//M�thode pour retourner la valeur
	public String getVal() {
		return this.valeur;
	}
}
