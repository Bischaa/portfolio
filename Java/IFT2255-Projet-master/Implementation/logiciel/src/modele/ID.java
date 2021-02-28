package modele;

public abstract class ID {

	protected String numero;

	protected ID()
	{
		numero = null;
	}
	protected ID(String numero)
	{
		this.numero = numero;
	}

	public String getNumero() {
		return this.numero;
	}
}
