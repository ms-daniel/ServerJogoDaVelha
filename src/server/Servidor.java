package server;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
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
	private int players = 0 ;
	private JTextField plays;
	private ConexaoJogador t1;
	private ConexaoJogador t2;
	
	public Servidor(int port) {
		this.port = port;
	}
	
	public void setFieldPlayer(JTextField field) {
		this.plays = field;
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
			
		while(players<1) { //fazer um "semnaforo aqui"
			try {
				//System.out.println(InetAddress.getByName("localhost"));
				if(players<1) { // aceita conexao de ate dois jogadores
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
					t1 = new ConexaoJogador(p1);
					t1.rodar();
				}else if (players == 2) {
					
				}else {
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(t1.isAlive()) {
			System.out.println("viv");
		}
	}
	
	/*public void iniciar() throws IOException {
		if(this.port == 0)
			servidor = new ServerSocket(6789);
		else
			servidor = new ServerSocket(port);
		
		while(true) {
			Socket p1 = servidor.accept();
			System.out.println("Connectado! Ip: " + p1.getInetAddress());
			ConexaoJogador t1 = new ConexaoJogador(p1);
			t1.rodar();
		}
	}*/
	
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
