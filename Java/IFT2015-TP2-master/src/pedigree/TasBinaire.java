//Code fait par Pierre-Olivier Tremblay: 20049076  et Maxime Ton: 20143044

package pedigree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class TasBinaire implements Comparator<Sim> {

	// ArrayList qui gardera en m�moire nos sims
	ArrayList<Sim> heapMin = new ArrayList<Sim>();

	// On compare 2 sims selon leur temps de mort
	public int compare(Sim o1, Sim o2) {
		if (o1.getDeathTime() == o2.getDeathTime()) {
			return 0;
		}
		if (o1.getDeathTime() < o2.getDeathTime()) {
			return -1;
		} else {
			return 1;
		}
	}

	// M�thode pour trouver les enfants d'un certain sim
	public Sim[] getChild(Sim o) {
		Sim[] children = new Sim[2];
		for (int i = 0; i < heapMin.size(); i++) {
			if (o == heapMin.get(i)) {
				children[0] = heapMin.get(i * 2 + 1);
				children[1] = heapMin.get(i * 2 + 2);
			}
		}
		return children;
	}

	// M�thode pour trouver le parent d'un certain sim
	public Sim getParent(Sim o) {
		Sim parent = null;
		for (int i = 0; i < heapMin.size(); i++) {
			if (o == heapMin.get(i)) {
				parent = heapMin.get((i - 1) / 2);
			}
		}
		return parent;
	}

	// M�thode insert
	public void insert(Sim o) {
		heapMin.add(o);
		swim(heapMin.size() - 1);
	}

	// M�thode peek
	public Sim peek() {
		return heapMin.get(0);
	}

	// M�thode deleteMin(), basé sur le modèle de la Démo 4
	public Sim deleteMin() {
		if (heapMin.size() == 0) {
			return null;
		}

		Sim out = heapMin.get(0);
		Collections.swap(heapMin, 0, heapMin.size() - 1); // Échange avec le dernier
		heapMin.remove(heapMin.size() - 1); // Retire l'ancien premier élément
		sink(); // On plonge dans le nouveau tas en triant au passage

		return out; // Retourne le Sim qui a été supprimé

	}

	// M�thode pour swim dans le tas, bas� sur le mod�le vu lors de la D�mo 3
	public void swim(int i) {
		int indexParent;
		while (i > 0) {
			indexParent = ((i - 1) / 2);
			if (this.compare(heapMin.get(i), heapMin.get(indexParent)) == -1) {
				Collections.swap(heapMin, i, indexParent);
				i = indexParent;
			} else {
				break;
			}
		}
	}

	// M�thode pour sink dans le tas � partir de la t�te, bas� sur le mod�le vu lors
	// de la D�mo 3
	public void sink() {

		int childIndex;
		int i = 0; // T�te du tas
		while (i < heapMin.size()) {
			i = i * 2;
			childIndex = (this.compare(heapMin.get((i * 2) + 1), heapMin.get((i * 2) + 2)) == -1) ? i * 2 + 1
					: i * 2 + 2;
			if (this.compare(heapMin.get(childIndex), heapMin.get(i)) == -1) {
				Collections.swap(heapMin, childIndex, i);
			} else {
				break;
			}
		}
	}

	// Méthode de sélection aléatoire d'un Sim du tas
	public Sim randomSim() {
		Random rnd = new Random();

		return heapMin.get(rnd.nextInt(heapMin.size()));
	}

}
