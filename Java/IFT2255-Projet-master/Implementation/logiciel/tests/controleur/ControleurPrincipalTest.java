package controleur;

import modele.compte.Compte;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ControleurPrincipalTest {

    private ControleurPrincipal controleurPrincipal;
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
        controleurPrincipal = new ControleurPrincipal(null);
    }

    @AfterEach
    void tearDown() throws Exception {
        controleurAdmin = null;
        controleurPrincipal = null;
    }

    @Test
    public void TestConnexion(){
        //Information du compte
        String nom = "Membre";
        String adresse = "1234";
        String telephone = "111-111-1111";
        String email = "something@something.com";

        controleurAdmin.creerCompteMembre(nom, adresse, telephone, email);
        Compte compteTest = controleurPrincipal.connexion(email);

        assertNotNull(compteTest, "Connexion efface le compte.");

        assertEquals(compteTest.getNom(), nom, "Connexion change le nom.");
        assertNotNull(compteTest.getNom(), "Connexion efface le nom.");

        assertEquals(compteTest.getAdresse(), adresse, "Connexion change l'adresse.");
        assertNotNull(compteTest.getAdresse(), "Connexion efface l'adresse.");

        assertEquals(compteTest.getTelephone(), telephone, "Connexion change le numéro de téléphone.");
        assertNotNull(compteTest.getTelephone(), "Connexion efface le numéro de téléphone.");

        assertEquals(compteTest.getEmail(), email, "Connexion change le email.");
        assertNotNull(compteTest.getEmail(), "Connexion efface le email.");

        assertNotNull(compteTest.getNumeroCompte(), "Pas de numéro de compte");
        assertEquals(compteTest.getNumeroCompte().getNumero().length(), 9, "Numero de compte pas de la bonne taille");
    }
}
