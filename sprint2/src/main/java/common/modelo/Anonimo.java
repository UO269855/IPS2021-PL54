package common.modelo;

public class Anonimo extends User {

	public Anonimo() {
		super("");
	}

	@Override
	public String getUsuario() {
		return "Anonimo";
	}

}
