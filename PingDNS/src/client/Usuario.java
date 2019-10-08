package client;

import java.io.Serializable;

public class Usuario implements Serializable{
	
	private String ip;
	private String usuario;
	
	private static final long serialVersionUID = 80L;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNome() {
		return usuario;
	}
	public void setNome(String usuario) {
		this.usuario = usuario;
	}
	
	

}
