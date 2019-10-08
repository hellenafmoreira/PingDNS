package client;

import interf.ConstantDNS;
import interf.ConstantServerLocal;
import interf.Interface;
import interf.InterfaceClient;
import pings.PingClient;
import server.ServidorDNS;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import javax.crypto.CipherInputStream;
import javax.swing.JOptionPane;

public class RMIClient implements InterfaceClient {

	static String nome;

	static Interface remoteDns;
	static InterfaceClient remoteClient;

	PingClient pingClient;
	
	public RMIClient() {
		pingClient = new PingClient();

	}

	public void lookupDNS() throws RemoteException, NotBoundException {
		String ipServidorEscolhido = pingClient.obterServidor(pingClient.getIpServidor1(), pingClient.getIpServidor2());

		// conectar no dns
		Registry registry = LocateRegistry.getRegistry(ipServidorEscolhido, ConstantDNS.DNS_PORT);
		final Interface remote = (Interface) registry.lookup(ConstantDNS.DNS_ID);
		
		System.out.println("Servidor com menor tempo: " + ipServidorEscolhido);

		remoteDns = remote;

	}

	public void bindLocal() throws RemoteException, AlreadyBoundException {
		// servidor do cliente
		RMIClient client = new RMIClient();
		InterfaceClient InterfaceClient = (InterfaceClient) UnicastRemoteObject.exportObject(client,
				ConstantServerLocal.Server_PORT);
		Registry registry2 = LocateRegistry.createRegistry(ConstantServerLocal.Server_PORT);
		registry2.bind(ConstantServerLocal.SERVER_ID, InterfaceClient);
		System.out.println("Servidor do cliente iniciado!");

	}

	public void lookupClient(String ip) throws RemoteException, NotBoundException {

		// servidor do destinatário

		Registry registry3 = LocateRegistry.getRegistry(ip, ConstantServerLocal.Server_PORT);
		final InterfaceClient remote3 = (InterfaceClient) registry3.lookup(ConstantServerLocal.SERVER_ID);

		remoteClient = remote3;

	}

	@Override
	public void enviaMensagemCliente(String mensagem) throws RemoteException {
		System.out.println("Mensagem:" + mensagem);

	}

	public static void main(String[] args)
			throws RemoteException, NotBoundException, UnknownHostException, AlreadyBoundException {
		RMIClient rmiClient = new RMIClient();
		Client client = new Client();

		rmiClient.lookupDNS();
		rmiClient.bindLocal();

		int opcao = 0;

		while (opcao != 4) {

			opcao = Integer.parseInt(JOptionPane.showInputDialog(
					"Escolha uma opção:\n 1. Autenticar" + "\n 2. Listar usuários onlie\n 3. Iniciar chat\n 4. Sair"));

			switch (opcao) {
			case 1:

				client.autenticar(remoteDns);

				break;

			case 2:

				client.listaUsuarios(remoteDns);
				break;

			case 3:

				String ip = remoteDns.retornaIP(JOptionPane.showInputDialog("Digite o nick do usuário"));
				rmiClient.lookupClient(ip);

				while (true) {

					String mensagem = JOptionPane.showInputDialog("Digite sua mensagem:");
					remoteClient.enviaMensagemCliente(mensagem);

					if (remoteDns.verificaSeAlguemAutenticou()) {
						client.listaUsuarios(remoteDns);
					}
				}

			default:
				break;
			}
		}

	}

}
