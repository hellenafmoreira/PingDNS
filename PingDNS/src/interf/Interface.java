package interf;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.Usuario;

public interface Interface extends Remote {

	String retornaIP(String nome) throws RemoteException;

	String listaUsuarios() throws RemoteException;

	boolean autenticaCliente(Usuario usuario) throws RemoteException;
	
	public boolean verificaSeAlguemAutenticou() throws RemoteException;
	

}
