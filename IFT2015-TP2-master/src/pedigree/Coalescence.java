//Code fait par Pierre-Olivier Tremblay: 20049076  et Maxime Ton: 20143044

package pedigree;

// import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Coalescence {

	private Simulation simulC = new Simulation();
	private PriorityQueue<Sim> PA_m = new PriorityQueue<Sim>();
	protected HashMap<Double, Integer> PA_set_m = new HashMap<Double, Integer>();
	private PriorityQueue<Sim> PA_f = new PriorityQueue<Sim>();
	protected HashMap<Double, Integer> PA_set_f = new HashMap<Double, Integer>();
	protected HashMap<Double, Integer> taillePop = new HashMap<Double, Integer>();

	public void init() {
		// On part la simulation pendant un certain temps
		simulC.simulate(100, 1000); // Arbitraire
		int taille;

		// On copie la population finale pour chaque sexe de notre simulation
		ArrayList<Sim> populationH = new ArrayList<Sim>();
		ArrayList<Sim> populationF = new ArrayList<Sim>();
		taille = simulC.population.heapMin.size(); // Population initiale
		taillePop.put((double) 15000, taille);

		for (Sim perso : simulC.population.heapMin) {
			if (perso.getSex() == pedigree.Sim.Sex.F) {
				populationF.add(perso);
			} else {
				populationH.add(perso);
			}
		}

		// Tant qu'il reste plus d'un p�re
		while (populationH.size() > 1) {
			// Si le p�re n'est pas encore dans l'ensemble PA, ajouter
			Sim youngest = getYoungest(populationH);
			if (!PA_m.contains(youngest.getFather())) {
				PA_m.add(youngest);
				// Si le p�re se retrouve d�j� dans l'ensemble PA, cr�er un nouveau point de
				// coalescence
			} else {
				PA_set_m.put(youngest.getBirthTime(), PA_m.size());
			}
			// � chaque it�ration, retirer l'individu le plus jeune
			populationH.remove(populationH.indexOf(youngest));
			taille--; // Retire une personne de la population
			taillePop.put(youngest.getBirthTime(), taille);
		}

		// Tant qu'il reste plus d'une m�re
		while (populationF.size() > 1) {
			// Si le p�re n'est pas encore dans l'ensemble PA, ajouter
			Sim youngest = getYoungest(populationF);
			if (!PA_f.contains(youngest.getMother())) {
				PA_f.add(youngest);
				// Si le p�re se retrouve d�j� dans l'ensemble PA, cr�er un nouveau point de
				// coalescence
			} else {
				PA_set_f.put(youngest.getBirthTime(), PA_f.size());
			}
			// � chaque it�ration, retirer l'individu le plus jeune
			populationF.remove(populationF.indexOf(youngest));
			taille--; // Retire une personne de la population
			taillePop.put(youngest.getBirthTime(), taille);
		}
	}

	// M�thode qui retourne l'index du plus jeune membre d'une population
	public Sim getYoungest(ArrayList<Sim> population) {
		Sim youngestM = population.get(0);

		for (int i = 0; i < population.size(); i++) {
			if (population.get(i).getBirthTime() < youngestM.getBirthTime()) {
				youngestM = population.get(i);
			}
		}
		return youngestM;
	}

	public static void main(String[] args) {
		Coalescence test = new Coalescence();
		test.init();
	}

}
