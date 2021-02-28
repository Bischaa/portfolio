package modele;

public class CompteID extends ID {

    public CompteID(String numero)
    {
        super();

        if(numero.length() != 9)
        {
            this.numero = numero;
        }
        else
        {
            throw new IllegalArgumentException("Mauvais nombre de chiffre dans le numero de compte: " + numero);
        }
    }
}
