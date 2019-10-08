package interf;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceClient extends Remote{
	
	
	void enviaMensagemCliente(String mensagem) throws RemoteException;
}
