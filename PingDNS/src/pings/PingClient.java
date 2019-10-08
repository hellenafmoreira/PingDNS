package pings;

import java.io.*;
import java.net.*;
import java.util.*;

import client.RMIClient;

/*
 * Cliente para gerar solicitações de ping sobre o UDP.
 * Código foi iniciado no PingServer.java
 */

public class PingClient {

	private double tempoPing;
	private double tempoRespostaServidor1 = 0.0;
	private double tempoRespostaServidor2 = 0.0;
	private String ipServidor1;
	private String ipServidor2;

	private static final int TEMPO_MAX = 1000; // milliseconds

	public String getIpServidor1() {
		return ipServidor1;
	}

	public void setIpServidor1(String ipServidor1) {
		this.ipServidor1 = ipServidor1;
	}

	public String getIpServidor2() {
		return ipServidor2;
	}

	public void setIpServidor2(String ipServidor2) {
		this.ipServidor2 = ipServidor2;
	}

	public double getTempoRespostaServidor1() {
		return tempoRespostaServidor1;
	}

	public void setTempoRespostaServidor1(double tempoRespostaServidor1) {
		this.tempoRespostaServidor1 = tempoRespostaServidor1;
	}

	public double getTempoRespostaServidor2() {
		return tempoRespostaServidor2;
	}

	public void setTempoRespostaServidor2(double tempoRespostaServidor2) {
		this.tempoRespostaServidor2 = tempoRespostaServidor2;
	}

	public String obterServidor(String ipServidor1, String ipServidor2) {

		ipServidor1 = "172.16.0.53";
		ipServidor2 = "172.16.0.55";

		if (tempoRespostaServidor1 > tempoRespostaServidor2) {
			return ipServidor2;
		} else {
			return ipServidor1;
		}

	}

	public double calculoTempo(String porta, String ip) throws Exception {

		// public static void main(String[] args) throws Exception {
		// Pega o comando na linha de argumentos
		// if (args.length != 2) {
		// System.out.println("Argumentos obrigatórios: porta do servidor");
		// return;
		// }

		// número da porta de acesso
		int port = Integer.parseInt(porta);
		// Servidor para Ping (tem que ter o PingServer rodando)
		InetAddress servidor;
		servidor = InetAddress.getByName(ip);

		// Cria um soquete de datagrama para enviar e receber pacotes UDP
		// através da porta especificada na linha de comando.
		DatagramSocket socket = new DatagramSocket(port);

		int sequenciaNumeros = 0;
		// Processando loop.
		while (sequenciaNumeros < 10) {
			// Timestamp em ms quando enviamos
			Date now = new Date();
			double msSend = now.getTime();
			// Cria string para enviar e transferir i para uma matriz de bytes
			String str = "PING " + sequenciaNumeros + " " + msSend + " \n";
			byte[] buf = new byte[1024];
			buf = str.getBytes();
			// Cria um pacote de datagrama para enviar como um pacote UDP.
			DatagramPacket ping = new DatagramPacket(buf, buf.length, servidor, port);

			// Envia o datagrama Ping para o servidor especificado
			socket.send(ping);
			// Tente receber o pacote - mas ele pode falhar (timeout)
			try {
				// Configura o tempo limite de 1000 ms = 1 segundo
				socket.setSoTimeout(TEMPO_MAX);
				// Configurar um pacote UPD para receber
				DatagramPacket resposta = new DatagramPacket(new byte[1024], 1024);
				// Tenta receber a resposta do ping
				socket.receive(resposta);
				// Se a resposta for recebida, o código continuará aqui, caso contrário será
				// continue na captura

				// timestamp para quando receber o pacote
				now = new Date();
				double msReceived = now.getTime();
				// Mostra o pacote e o atraso
				printData(resposta, msReceived - msSend);
				tempoPing = msReceived - msSend;
			} catch (IOException e) {
				// Mostra qual pacote atingiu o tempo limite
				System.out.println("Atraso por pacote " + sequenciaNumeros);
			}
			// next packet
			sequenciaNumeros++;

		}
		if (ip.equals(ipServidor1)) {
			this.tempoRespostaServidor1 = tempoPing;
			return tempoRespostaServidor1;

		} else if (ip.equals(ipServidor2)) {
			this.tempoRespostaServidor2 = tempoPing;
			return tempoRespostaServidor2;

		}
		return tempoRespostaServidor1;

	}

	/*
	 * Imprime dados de ping no fluxo de saída padrão.
	 */
	private static void printData(DatagramPacket solicitacao, double d) throws Exception {
		// Obtenha referências à matriz de bytes do pacote.
		byte[] buf = solicitacao.getData();

		// Envolve os bytes em um fluxo de entrada da matriz de bytes
		// para ler os dados como um fluxo de bytes.
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);

		// Envolve o fluxo de saída da matriz de bytes em um leitor de fluxo de entrada,
		// para ler os dados como um fluxo de caracteres.
		InputStreamReader isr = new InputStreamReader(bais);

		// Enrola o leitor de fluxo de entrada em um leitor de buffer,
		// para ler os dados do personagem uma linha por vez.
		// (Uma linha é uma sequência de caracteres terminados por qualquer combinação
		// de \ r e \ n.)
		BufferedReader br = new BufferedReader(isr);

		// Os dados da mensagem estão contidos em uma única linha, então a linha será
		// lida.
		String line = br.readLine();

		// Mostra o endereço do host e os dados recebidos dele.
		System.out.println("Recebido de " + solicitacao.getAddress().getHostAddress() + ": " + new String(line)
				+ " Atraso: " + d + " ms");
	}
}
