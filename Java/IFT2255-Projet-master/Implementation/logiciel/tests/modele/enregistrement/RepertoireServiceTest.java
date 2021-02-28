package modele.enregistrement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepertoireServiceTest {

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }




    @Test
    void testCreerSeance() {
        //Nouvelle séance
        RepertoireService.creerSeance(LocalDate.parse("2020-09-14"), LocalDate.parse("2020-12-22"), 8,
                new LocalDate[]{LocalDate.parse("2020-11-20"), LocalDate.parse("2020-11-25")}, 20, 14.25,
                "Apporter bouteille d'eau", RepertoireService.getDefaultService()[0]);

        //Nouvelle séance
        RepertoireService.creerSeance(LocalDate.parse("2020-09-17"), LocalDate.parse("2020-12-24"), 8,
                new LocalDate[]{LocalDate.parse("2020-11-10"), LocalDate.parse("2020-11-12")}, 30, 18,
                "Apporter serviette", RepertoireService.getDefaultService()[1]);

        List<Seance> seances = RepertoireService.lireSeances(seance -> seance.getCapacite() == 20);
        assertEquals(seances.size(), 1, "Mauvais nombre de séances");
        assertEquals(seances.get(0).getFrais(), 20, "Mauvais nombre de séances");
        assertEquals(seances.get(0).getNomService(), "Zumba", "Mauvais nom de service");
        assertNotNull(seances.get(0).getNumeroSeance(), "Erreur numéro de séance");


        seances = RepertoireService.lireSeances(seance -> seance.getHeure() == 8);
        assertEquals(seances.size(), 2, "Mauvais nombre de séances");
        assertEquals(seances.get(1).getNomService(), "Musculation", "Mauvais nom de service");

        seances = RepertoireService.lireSeances(seance -> seance.getDateDebut().isAfter(LocalDate.parse("2020-09-16")));
        assertEquals(seances.size(), 1, "Mauvais nombre de séances");
        assertEquals(seances.get(0).getNomService(), "Musculation", "Mauvais nom de service");
    }


    @Test
    void testgetDefaultService() {
        Service[] defaultServices = RepertoireService.getDefaultService();
        assertEquals(defaultServices.length, 3);
        assertEquals(defaultServices[0].getNomService(), "Zumba");
        assertEquals(defaultServices[1].getNumeroService().getNumero(), "234");
        assertNull(defaultServices[0].getCommentaire(), "Commentaire non nul");
        assertNull(defaultServices[2].getProfessionnel(), "Professionnel non nul");

    }


}