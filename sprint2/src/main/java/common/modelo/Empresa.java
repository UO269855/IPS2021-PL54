package common.modelo;

public class Empresa extends User {

	private String email;
	
	public Empresa(String email, String direccion) {
		super(direccion);
		this.email = email;
	}

	@Override
	public String getUsuario() {
		return email;
	}

}
