package pings;

import java.io.*;
import java.net.*;
import java.util.*;

/*
 * Server to process ping requests over UDP.
 */
public class PingServer {

	private static final double PERDA = 0.3;
	private static final int ATRASO = 100; // milliseconds

	public void start(int porta) throws Exception {
		
		// Cria gerador de números aleatórios para uso na simulação
		// perda de pacotes e atraso na rede.
		Random random = new Random();

		// Cria um soquete de datagrama para receber e enviar pacotes UDP
		// através da porta especificada na linha de comando.
		DatagramSocket socket = new DatagramSocket(porta);

		// Processando loop.
		while (true) {
			// Cria um pacote de datagrama para manter o pacote UDP de chegada.
			DatagramPacket solicitacao = new DatagramPacket(new byte[1024], 1024);

			// Bloqueia até que o host receba um pacote UDP.
			socket.receive(solicitacao);

			// Mostra os dados recebidos.
			printData(solicitacao);

			// Decide se deve responder ou simular a perda de pacotes.
			if (random.nextDouble() < PERDA) {
				System.out.println("   Resposta não enviada.");
				continue;
			}

			// Simula o atraso da rede.
			//Thread.sleep((int) (random.nextDouble() * 2 * ATRASO));

			// Enviar resposta
			InetAddress clienteHost = solicitacao.getAddress();
			int clientePorta = solicitacao.getPort();
			byte[] buf = solicitacao.getData();
			DatagramPacket resposta = new DatagramPacket(buf, buf.length, clienteHost, clientePorta);
			socket.send(resposta);

			System.out.println("   Resposta enviada.");
			
		}
	}

	/*
	 * Mostra dados de ping no fluxo de saída padrão.
	 */
	private static void printData(DatagramPacket solicitacao) throws Exception {
		// Obtem referências à matriz de bytes do pacote.
		byte[] buf = solicitacao.getData();

		// Envolve os bytes em um fluxo de entrada de matriz de bytes,
		// para ler os dados como um fluxo de bytes.
		ByteArrayInputStream bais = new ByteArrayInputStream(buf);

		// Envolve o fluxo de saída da matriz de bytes em um leitor de fluxo de entrada,
		// para ler os dados como um fluxo de caracteres.
		InputStreamReader isr = new InputStreamReader(bais);

		// Envolve o leitor de fluxo de entrada em um leitor de buffer
		// ler os dados do personagem uma linha por vez.
		// (Uma linha é uma sequência de caracteres terminados por qualquer combinação de \ r e \ n.)
		BufferedReader br = new BufferedReader(isr);

		// Os dados da mensagem estão contidos em uma única linha, então leia esta linha.
		String linha = br.readLine();

		// Mostra o endereço do host e os dados recebidos dele.
		System.out.println("Recebido de" + solicitacao.getAddress().getHostAddress() + ": " + new String(linha));
	}
}
