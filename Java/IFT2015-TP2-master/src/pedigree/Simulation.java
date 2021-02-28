//Code fait par Pierre-Olivier Tremblay: 20049076  et Maxime Ton: 20143044

package pedigree;

import java.util.PriorityQueue; //Importe la classe de file de priorité pour les évènements
import java.util.Random;
// import java.io.FileWriter;

public class Simulation {

    private double fidelite = 0.9; // Paramètre de fidélité
    protected TasBinaire population = new TasBinaire(); // Tas de la population vivante
    private PriorityQueue<Event> eventQ = new PriorityQueue<Event>(); // File de priorité des
                                                                      // évènements

    // Méthode qui exécute la simulation
    public void simulate(int n, double Tmax) {

        for (int i = 0; i < n; i++) {
            Sim fondateur = new Sim(randomSex()); // Sim fondateur
            Event E = new Event(fondateur, 0.0, "naissance"); // Naissance fondateur
            this.eventQ.add(E); // Insertion dans la file de priorité
        }

        while (!this.eventQ.isEmpty()) {
            Event E = this.eventQ.poll(); // Équivalent de deleteMin
            if (E.time > Tmax) {
                break; // Arrêt à Tmax
            }
            if (E.subject.getDeathTime() > E.time) { // L'évènement peut avoir lieu
                // Traiter les évènements

                switch (E.type) {
                    case "naissance": // Naissance

                        // Durée de vie du nouveau Sim
                        Random RND = new Random();
                        AgeModel vieSim = new AgeModel(); // Pour déterminer la durée de vie du
                                                          // nouveau Sim
                        double dureeVie = vieSim.randomAge(RND);
                        double deathtime = E.time + dureeVie;
                        this.eventQ.add(new Event(E.subject, deathtime, "mort")); // Ajout de la mort du Sim

                        // Attente de reproduction
                        if (E.subject.getSex() == pedigree.Sim.Sex.F) {
                            double timeWaiting = waitingTime();
                            this.eventQ.add(new Event(E.subject, E.time + timeWaiting, "reproduction")); // Nouvel
                            // évènement
                        }

                        // Enregistrement du nouveau Sim dans la population
                        this.population.insert(E.subject);
                        break;

                    case "reproduction": // Reproduction
                        // Vérifie si Sim est mort
                        if (E.subject.getDeathTime() < E.time) {
                            break;
                        }

                        // Vérifie âge de reproduction
                        if (E.time - E.subject.getBirthTime() >= Sim.MIN_MATING_AGE_F) {
                            Sim pere = chooseFather(E.subject, E); // Choisir le père

                            // Créer le bébé Sim
                            Sim bebe = new Sim(E.subject, pere, E.time, randomSex());
                            this.eventQ.add(new Event(bebe, E.time, "naissance")); // Évènement naissance

                            // Enregistrement partenaire
                            pere.setMate(E.subject);
                            E.subject.setMate(pere);
                        }

                        // Reproduction suivante
                        double timeWaiting = waitingTime();
                        this.eventQ.add(new Event(E.subject, E.time + timeWaiting, "reproduction"));

                        break;

                    default: // Mort du sim
                        /*
                         * On doit seulement faire deleteMin, car la population est triée par temps de
                         * mort. Donc le min est celui qui décède
                         */
                        this.population.deleteMin();
                        break;
                }
            }
            // Sinon on ne fait rien, car le Sim est mort
        }
    }

    // Méthode pour obtenir le sex d'un Sim au hasarc
    public pedigree.Sim.Sex randomSex() {
        Random rnd = new Random();

        // 50% de chance d'avoir un homme
        if (rnd.nextDouble() <= 0.5) {
            return pedigree.Sim.Sex.F;
        } else {
            return pedigree.Sim.Sex.M;
        }

    }

    // Méthode pour obtenir tant d'attente
    public double waitingTime() {
        Random RND = new Random();
        AgeModel vieSim = new AgeModel();
        double parentHood = vieSim.expectedParenthoodSpan(Sim.MIN_MATING_AGE_F, Sim.MAX_MATING_AGE_F);
        return AgeModel.randomWaitingTime(RND, 2.0 / parentHood); // Temps avant reproduction

    }

    // Méthode pour la sélection du père (partenaire) [Méthode donné dans
    // description du devoir]
    public Sim chooseFather(Sim mere, Event E) {
        Random RND = new Random();
        Sim y = null;
        Sim z = null;

        if (!mere.isInARelationship(E.time) || RND.nextDouble() > this.fidelite) {
            do {
                z = this.population.randomSim();
                // Vérifie si sexe opposée et en âge de reproduction
                if (z.getSex() != mere.getSex() && z.isMatingAge(E.time)) {
                    // z accepte si mere est fidèle
                    if (mere.isInARelationship(E.time) || !z.isInARelationship(E.time)
                            || RND.nextDouble() > this.fidelite) {
                        y = z;
                    }
                }
            } while (y == null);
        }

        else {
            y = mere.getMate();
        }

        return y;
    }

    /*
     * Pour tester la partie simulation
     * 
     * // Méthode pour échantilloner la population public void populationSample(int
     * time) {
     * 
     * int pop_tot = 0; int pop_homme = 0; int pop_femme = 0;
     * 
     * // Calculer la population homme, femme totale for (Sim perso :
     * this.population.heapMin) { pop_tot++; if (perso.getSex() ==
     * pedigree.Sim.Sex.F) { pop_femme++; } else { pop_homme++; } }
     * 
     * try { FileWriter csvWriter = new FileWriter("simulationTest2.csv", true);
     * 
     * csvWriter.append("" + time); csvWriter.append(","); csvWriter.append("" +
     * pop_homme); csvWriter.append(","); csvWriter.append("" + pop_femme);
     * csvWriter.append(","); csvWriter.append("" + pop_tot);
     * csvWriter.append("\n");
     * 
     * csvWriter.close();
     * 
     * } catch (Exception e) { // Ne rien faire }
     * 
     * }
     * 
     * public static void main(String[] args) { int n = Integer.parseInt(args[0]);
     * int Tmax = Integer.parseInt(args[1]);
     * 
     * Simulation simul = new Simulation(); simul.simulate(n, Tmax);
     * 
     * }
     */

}
