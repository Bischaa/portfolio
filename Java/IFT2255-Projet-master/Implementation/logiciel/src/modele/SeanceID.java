package modele;

public class SeanceID extends ID
{
    public SeanceID(String numero)
    {
        if(numero.length() != 7)
        {
            this.numero = numero;
        }
        else
        {
            throw new IllegalArgumentException("Mauvais nombre de chiffres dans le numero de séance: " + numero);
        }
    }
}
