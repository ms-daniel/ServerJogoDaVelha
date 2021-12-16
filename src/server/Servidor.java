package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread{
	private int port = 0;
	private ServerSocket servidor;
	private Socket p1;
	//private SocketAddress serveAddress = new InetSocketAddress("10.20.6.186", 0);
	private static int players = 0 ;
	private static JTextField plays;
	
	//instanciara dois objetos para melhor controlar o fluxo
	private ConexaoJogador t1;
	private ConexaoJogador t2;
	
	//boolean para saber quem começa
	private boolean first_t1;
	private boolean first_t2;
	
	public Servidor(int port) {
		this.port = port;
	}
	
	//recebe referencia do box de texto para quantiadades de jogadores conectados
	public void setFieldPlayer(JTextField field) {
		this.plays = field;
	}
	
	//diminui quantidade de jogadores e pode ser chamado em qualquer class do package
	public static void lessPlayers() {
		players--;
		plays.setText(Integer.toString(players));
	}
	
	public void run(){
		//try catch iniciar servidor socket
		//atribuição da porta que é enviada pela interface grafica
		if(this.port == 0) {
			try {
				servidor = new ServerSocket(9000);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}
			
		} else {
			try {
				servidor = new ServerSocket(port);
			} catch (BindException e) {
				JOptionPane.showMessageDialog(null, "essa porta ja esta aberta em algum servidor nesse host!", "Port Error", 1);
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//	=======================================
			
		while(players<2) { //fazer um "semnaforo aqui"
			try {
				//System.out.println(InetAddress.getByName("localhost"));
				if(players<2) { // aceita conexao de ate dois jogadores
					p1 = servidor.accept();
					System.out.println("Connectado! Ip: " + p1.getInetAddress());
					players++;
					plays.setText(Integer.toString(players));
					
				}else {//rejeita conexao de um terceiro jogador
					servidor.close();
					System.out.println("Fechado!");
				}
				
				if(players == 1) {
					System.out.println("player 1 conectado");
					t1 = new ConexaoJogador(p1, plays);
					//t1.rodar();
				}else if (players == 2) {
					System.out.println("player 2 conectado");
					t2 = new ConexaoJogador(p1, plays);
					//t2.rodar();
				}
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//sleep para dar tempo de notificar os usuarios e não bugar os paineis de dialogo
		Dormir(5000);
		
		//liga as saidas dos outros sockets
		try {
			t1.setSaidaPlayer2(new DataOutputStream(t2.getSocket().getOutputStream()));
			t2.setSaidaPlayer2(new DataOutputStream(t1.getSocket().getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		t1.setNome("cliente 1");
		t2.setNome("cliente 2");
		//avisa para os dois que ja estão prontos
		t1.notificar(t1.getSocket(), t2.getSocket());
		
		//depois dos dois jogadores conectados
		//WhoFirst(); //randomicamente seta algum dos dois para começar
		first_t1 = true;
		first_t2 = false;
		t1.First(first_t1); //diz se ele será o primeiro
		t2.First(first_t2); //ou o outro
		
		//Dormir(2300);
		//ordem para entrar no synchronized
		if(first_t1){
			System.out.println("t1");
			while(!t1.Winner()) {
				t1.iniciar();
				t2.iniciar();
			}
			
		}else{
			System.out.println("t2");
			t2.iniciar();
			Dormir(10);
			t1.iniciar();
		}
	}
	//recebe porta da itnerface grafica 
	public boolean setPort(int n) {
		if(servidor.isClosed()) {
			this.port = n;
		}else
			return false;
		
		return true;
	}
	
	public int getPort() {
		return this.port;
	}
	//fecha servidor
	public boolean closeServer() throws IOException {
		servidor.close();
		p1.close();
		t1.beClose();
		
		this.port = 0;
		if(servidor.isClosed() && p1.isClosed())
			return true;
		else
			return false;
	}
	//verifica se servidor esta fechado
	public boolean isClose() {
		return servidor.isClosed();
	}
	
	private void WhoFirst() {
		Random randomico = new Random();
		int n = randomico.nextInt(999);
		
		//se o numero for pá o primeiro jogador começa
		if((n%2)==0) {
			first_t1 = true;
			first_t2 = false;
		}else {
			first_t1 = false;
			first_t2 = true;
		}
		
	}
	
	public void Dormir(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
