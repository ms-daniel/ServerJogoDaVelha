package server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread{
	private int port = 0;
	private ServerSocket servidor;
	private Socket p1;
	private InetAddress ip_serv;  
	//private SocketAddress serveAddress = new InetSocketAddress("10.20.6.186", 0);
	private static int players = 0 ;
	private static JTextField plays;
	private ConexaoJogador t1;
	private ConexaoJogador t2;
	
	public Servidor(int port) {
		this.port = port;
	}
	
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
					t1.rodar();
				}else if (players == 2) {
					System.out.println("player 2 conectado");
					t2 = new ConexaoJogador(p1, plays);
					t2.rodar();
				}else {
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
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
	
	public boolean isClose() {
		return servidor.isClosed();
	}
	
}
