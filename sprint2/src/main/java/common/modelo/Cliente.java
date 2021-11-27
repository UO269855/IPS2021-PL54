package common.modelo;

public class Cliente extends User {
	
	private String dni;
	
	public Cliente(String dni, String direccion) {
		super(direccion);
		this.dni = dni;
	}

	@Override
	public String getUsuario() {
		return dni;
	}

}
