package controleur;

import modele.compte.Compte;
import modele.compte.CompteMembre;
import modele.compte.RepertoireCompte;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControleurCompteTest {
    private ControleurAdministratif controleurAdmin;
    private ControleurCompte controleurCompte;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {

    }

    @BeforeEach
    void setUp() throws Exception {
        controleurAdmin = new ControleurAdministratif(null);
        controleurCompte = new ControleurCompte(null);
    }

    @AfterEach
    void tearDown() throws Exception {
        controleurAdmin = null;
        controleurCompte = null;
    }

    @Test
    public void TestModifierCompte(){
        //Informations initiales du compte
        String nom = "Membre";
        String adresse = "1234";
        String telephone = "111-111-1111";
        String email = "something@something.com";

        controleurAdmin.creerCompteMembre(nom, adresse, telephone, email);
        List<Compte> comptes = RepertoireCompte.lireComptes(compte -> compte.getNom().equals(nom));
        Compte compte = comptes.get(0);

        //Nouvelles informations du compte
        String nomF = "PasMembre";
        String adresseF = "4321";
        String telephoneF = "222-222-2222";
        String emailF = "allo@bonjour.com";

        controleurCompte.modifierCompte(nomF, adresseF, telephoneF, emailF, compte);
        Compte compteMod = comptes.get(0);

        assertTrue(compteMod instanceof CompteMembre, "Pas un CompteMembre");
        CompteMembre membre = (CompteMembre) compteMod;

        assertNotEquals(membre.getNom(), nom, "On n'a pas modifié le nom");
        assertNotEquals(membre.getAdresse(), adresse, "On n'a pas modifié l'adresse");
        assertNotEquals(membre.getTelephone(), telephone, "On n'a pas modifié le numéro de téléphone");
        assertNotEquals(membre.getEmail(), email, "On n'a pas modifié le email");

        assertEquals(membre.getNom(), nomF, "Mauvais nom");
        assertEquals(membre.getAdresse(), adresseF, "Mauvaise adresse");
        assertEquals(membre.getTelephone(), telephoneF, "Mauvais téléphone");
        assertEquals(membre.getEmail(), emailF, "Mauvais email");

        assertNotNull(membre.getNumeroCompte(), "Pas de numéro de compte");
        assertEquals(membre.getNumeroCompte().getNumero().length(), 9, "Numero de compte pas de la bonne taille");
    }
}
