package server;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import client.Usuario;
import interf.ConstantDNS;
import interf.Interface;
import pings.PingServer;

public class ServidorDNS implements Interface {

	private List<Usuario> usuarios;
	int tamanho = 0;
	
	public ServidorDNS() {
		usuarios = new ArrayList<Usuario>();
	}

	public String retornaIP(String nome) throws RemoteException {

		for (int i = 0; i < usuarios.size(); i++) {
			if (usuarios.get(i).getNome().equals(nome)) {
				return usuarios.get(i).getIp();
			}
		}
		return null;
	}

	public String listaUsuarios() {

		String texto = "";
		for (int i = 0; i < usuarios.size(); i++) {
			texto += "\n\nUsuario:" + usuarios.get(i).getNome() + "\nip:"
					+ usuarios.get(i).getIp();
		}

		return texto;
	}

	@Override
	public boolean autenticaCliente(Usuario usuario) throws RemoteException {
		
		for (int i = 0; i > usuarios.size(); i++) {
			if (usuarios.get(i).getNome().equals(usuario.getNome())) {
				return false;
			}
		}
		this.usuarios.add(usuario);
		
		return true;

	}
	
	public boolean verificaSeAlguemAutenticou() {
		if(tamanho<usuarios.size()) {
			tamanho = usuarios.size();
			return true;
		}else {
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
	
		new Thread() {
			public void run() {
				PingServer pingServer = new PingServer();
				try {
					pingServer.start(1235);
				}catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
		
		try {

			ServidorDNS DNSServer = new ServidorDNS();
			Interface serverInterface = (Interface) UnicastRemoteObject.exportObject(DNSServer, 1235);

			Registry registry = LocateRegistry.createRegistry(ConstantDNS.DNS_PORT);

			registry.bind(ConstantDNS.DNS_ID, serverInterface);

			System.out.println("Servidor DNS iniciado!");

		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
