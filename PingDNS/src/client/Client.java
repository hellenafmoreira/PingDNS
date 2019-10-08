package client;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import interf.Interface;
import interf.InterfaceClient;

public class Client {
	
	 Usuario usuario = new Usuario();
	
	
	public  void autenticar(Interface remote) throws RemoteException, UnknownHostException {

		usuario.setNome(JOptionPane.showInputDialog("Digite seu nick"));
		usuario.setIp(Inet4Address.getLocalHost().getHostAddress());

		Boolean ok = remote.autenticaCliente(usuario);

		if (ok == false) {
			usuario.setNome(JOptionPane.showInputDialog("Esse nick já existe!"));
		}
	}

	public  void listaUsuarios(Interface remote) throws RemoteException {

		String conteudo = remote.listaUsuarios();
		JOptionPane.showMessageDialog(null, conteudo);
	}

	

}
