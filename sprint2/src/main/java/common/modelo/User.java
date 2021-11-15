package common.modelo;

public abstract class User {
	
	private String direccion;
	
	public User(String direccion) {
		this.direccion = direccion;
	}
	
	public abstract String getUsuario();
	
	public String getDireccion() {
		return direccion;
	}
}
