package modele;

public class ServiceID extends ID
{
    public ServiceID(String numero)
    {
        if(numero.length() != 3)
        {
            this.numero = numero;
        }
        else
        {
            throw new IllegalArgumentException("Mauvais nombre de chiffres dans le numéro de service: " + numero);
        }
    }
}
