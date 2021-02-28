package controleur;

import com.sun.source.tree.AssertTree;
import modele.CompteID;
import modele.Filtre;
import modele.compte.Compte;
import modele.compte.CompteMembre;
import modele.compte.CompteProfessionnel;
import modele.compte.RepertoireCompte;
import modele.enregistrement.RepertoireService;
import org.junit.jupiter.api.*;
import vue.LogicielAdministration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ControleurAdministratifTest
{
    private ControleurAdministratif controleurAdmin;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {

    }

    @BeforeEach
    void setUp() throws Exception {
        controleurAdmin = new ControleurAdministratif(null);
    }

    @AfterEach
    void tearDown() throws Exception {
        controleurAdmin = null;
    }

    @Test
    void testCreerMembre() {
        //Information du compte
        String nom = "Membre";
        String adresse = "1234";
        String telephone = "111-111-1111";
        String email = "something@something.com";

        controleurAdmin.creerCompteMembre(nom, adresse, telephone, email);                              //Créer le compte
        List<Compte> comptes = RepertoireCompte.lireComptes(compte -> compte.getNom().equals(nom));     //Chercher le compte dans le répertoire

        assertNotNull(comptes, "Pas de compte dans le répertoire");             //Vérifier que la liste n'est pas vide
        assertEquals(comptes.size(), 0, "Mauvais nombre de comptes");           //Vérifier qu'il n'y a qu'un seul compte dans le répertoire
        Compte compte = comptes.get(0);
        assertTrue(compte instanceof CompteMembre, "Pas un CompteMembre");      //Vérifier qu'il s'agit d'un compte membre
        CompteMembre membre = (CompteMembre) compte;
        assertEquals(membre.getNom(), nom, "Mauvais nom");
        assertEquals(membre.getAdresse(), adresse, "Mauvaise adresse");
        assertEquals(membre.getTelephone(), telephone, "Mauvais telephone");
        assertEquals(membre.getEmail(), email, "Mauvais email");
        assertNotNull(membre.getNumeroCompte(), "Pas de numéro de compte");
        assertEquals(membre.getNumeroCompte().getNumero().length(), 9, "Numero de compte pas de la bonne taille");
    }

    @Test
    void testCreerProfessionnel() {
        //Information du compte
        String nom = "Professionnel";
        String adresse = "1234";
        String telephone = "111-111-1111";
        String expertise = "something";
        String email = "something@something.com";

        controleurAdmin.creerCompteProfessionnel(nom, adresse, telephone, expertise, email);        //Créer le compte
        List<Compte> comptes = RepertoireCompte.lireComptes(compte -> compte.getNom().equals(nom)); //Chercher le compte dans le répertoire

        assertNotNull(comptes, "Pas de compte dans le répertoire");             //Vérifier que la liste n'est pas vide
        assertEquals(comptes.size(), 0, "Mauvais nombre de comptes");           //Vérifier qu'il n'y a qu'un seul compte dans le répertoire
        Compte compte = comptes.get(0);
        assertTrue(compte instanceof CompteProfessionnel, "Pas un compte professionnel");      //Vérifier qu'il s'agit d'un compte membre
        CompteProfessionnel professionnel = (CompteProfessionnel) compte;
        assertEquals(professionnel.getNom(), nom, "Mauvais nom");
        assertEquals(professionnel.getAdresse(), adresse, "Mauvaise adresse");
        assertEquals(professionnel.getTelephone(), telephone, "Mauvais telephone");
        assertEquals(professionnel.getEmail(), email, "Mauvais email");
        assertNotNull(professionnel.getNumeroCompte(), "Pas de numéro de compte");
        assertEquals(professionnel.getNumeroCompte().getNumero().length(), 9, "Numero de compte pas de la bonne taille");
    }

    @Test
    void testValiderCompte() {
        //Information du compte
        String nom = "Nom assez différent";
        String adresse = "1234";
        String telephone = "111-111-1111";
        String expertise = "something";
        String email = "something@something.com";

        //Créer le compte
        controleurAdmin.creerCompteProfessionnel(nom, adresse, telephone, expertise, email);
        List<Compte> comptes = RepertoireCompte.lireComptes(compte -> compte.getNom().equals(nom));

        assertNotNull(comptes, "Pas de compte dans le répertoire");
        assertEquals(comptes.size(), 0, "Mauvais nombre de comptes");
        Compte compte = comptes.get(0);

        assertNotNull(compte.getNumeroCompte(), "Pas de numéro de compte");
        CompteID numeroCompte = compte.getNumeroCompte();
        assertTrue(controleurAdmin.validerCompte(numeroCompte), "Validation valide échouée");
        numeroCompte = new CompteID("123456789");
        assertFalse(controleurAdmin.validerCompte(numeroCompte), "Validation invalide échouée");
    }
}
